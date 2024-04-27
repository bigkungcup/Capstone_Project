import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useLogin } from "./login";

export const useReviews = defineStore("Reviews", () => {
  const roleToken = ref(localStorage.getItem("role"));
  const router = useRouter();
  const login = useLogin();
  const reviewList = ref({
    data: {
      content: [],
      pageable: {
        totalPages: 1,
      },
    },
  });
  const reviewPage = ref(0);
  const reviewDetail = ref({
    data: {
      reviewId: 0,
      rating: 0,
      detail: "",
      title: "",
      spoileFlag: false,
      book: {
        bookId: 0,
      },
    },
  });
  const newReview = ref({
    rating: 1,
    detail: "",
    title: "",
    userId: "1",
    bookId: "",
    spoileFlag: false,
  });
  const editReview = ref();
  const successfulPopup = ref("hide");
  const leavePopup = ref(true);
  const myReviewList = ref({
    data: {
      content: [
        {
          bookDetail: {},
        },
      ],
      pageable: {
        totalPages: 1,
      },
    },
  });
  const myReviewPage = ref(0);
  const sortReview = ref("desc");
  const filterReview = ref(null);
  const newReviewList = ref({
    data: [],
  });
  const loadSection = ref("not");
  // const likeStatus = ref(0); //0 = clear, 1 = like, 2 = dislike

  //Get reviews
  async function getReview(bookId) {
    let status = 0;
    let sortByReview = "";
    let sortTypeReview = "";
    loadSection.value = 'load';
    if (sortReview.value == "desc" || sortReview.value == "asc") {
      sortByReview = sortReview.value;
    } else {
      sortByReview = "desc";
      sortTypeReview = sortReview.value;
    }
    const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/review`, {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${login.accessToken}`,
        };
        options.params = {
          bookId: bookId,
          page: reviewPage.value,
          size: 10,
          sortType: sortByReview,
          sortBy: sortTypeReview,
          reviewRating: filterReview.value,
        };
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          loadSection.value = 'done';
          reviewList.value = {
            data: {
              content: [],
              pageable: {
                totalPages: 1,
              },
            },
          };
        }
      },
    });
    if (status == 200) {
      loadSection.value = 'done';
      reviewList.value = data.value;
      // console.log("get review list completed");
    } else if (status == 401) {
      loadSection.value = 'not';
        await login.handleRefresh();
        await getReview(bookId);
    } else if (status == 404) {
      loadSection.value = 'done';
      clearReviewList();
      // console.log("get review list uncompleted");
    }
  }

  //Get reviews by guest
  async function getReviewByGuest(bookId) {
    let status = 0;
    let sortByReview = "";
    let sortTypeReview = "";
    loadSection.value = 'load';
    if (sortReview.value == "desc" || sortReview.value == "asc") {
      sortByReview = sortReview.value;
    } else {
      sortByReview = "desc";
      sortTypeReview = sortReview.value;
    }
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/review/guest`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
          };
          options.params = {
            bookId: bookId,
            page: reviewPage.value,
            size: 10,
            sortType: sortByReview,
            sortBy: sortTypeReview,
            reviewRating: filterReview.value,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
          if (status == 404) {
            loadSection.value = 'done';
            reviewList.value = {
              data: {
                content: [],
                pageable: {
                  totalPages: 1,
                },
              },
            };
            // console.log("get review list uncompleted");
          }
        },
      }
    );
    if (status == 200) {
      loadSection.value = 'done';
      reviewList.value = data.value;
      // console.log("get review list completed");
    } else {
      loadSection.value = 'done';
    }
  }

  //Get my reviews
  async function getMyReview(userId) {
    let status = 0;
    loadSection.value = "load";

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/review/me`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
          options.params = {
            userId: userId,
            page: myReviewPage.value,
            size: 10,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
          if (status == 400) {
            myReviewList.value = {
              data: {
                content: [],
                pageable: {
                  totalPages: 1,
                },
              },
            };
            // console.log("get my review list uncompleted");
          }
        },
      }
    );
    if (status == 200) {
      loadSection.value = "done";
      myReviewList.value = data.value;
      // console.log("get my review list completed");
    } else if (status == 401) {
      if (refreshToken.value !== null && refreshToken.value !== undefined) {
        loadSection.value = "not";
        await login.handleRefresh();
        await getMyReview(userId);
      }
    } else {
      loadSection.value = "done";
      clearMyReviewList();
      // console.log("get my review list uncompleted");
    }
  }

  //Get my reviews by guest
  async function getMyReviewByGuest(userId) {
    let status = 0;
    loadSection.value = "load";

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/review/me`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
          };
          options.params = {
            userId: userId,
            page: myReviewPage.value,
            size: 10,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
          if (status == 400) {
            myReviewList.value = {
              data: {
                content: [],
                pageable: {
                  totalPages: 1,
                },
              },
            };
            // console.log("get my review list uncompleted");
          }
        },
      }
    );
    if (status == 200) {
      loadSection.value = "done";
      myReviewList.value = data.value;
      // console.log("get my review list completed");
    } else {
      loadSection.value = "done";
      clearMyReviewList();
      // console.log("get my review list uncompleted");
    }
  }

  //Get all new review
  async function getNewReviewList() {
    let status = 0;

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/review/newReview`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        newReviewList.value = data.value;
      }
      // console.log("get new review list completed");
    } else if (status == 404) {
      clearNewReviewList();
      // console.log("get new review list uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getNewReviewList();
    }
  }

  //Get all new review by guest
  async function getNewReviewListByGuest() {
    let status = 0;

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/review/newReview`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        newReviewList.value = data.value;
      }
      // console.log("get new review list completed");
    } else if (status == 404) {
      clearNewReviewList();
      // console.log("get new review list uncompleted");
    }
  }

  //Get review detail
  async function getReviewDetail(reviewId) {
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/review/${reviewId}`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      reviewDetail.value = data.value;
      // console.log("get review detail completed");
    } else if (status == 400) {
      // console.log("get review detail uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getReviewDetail(reviewId);
    } else if (status == 404) {
      router.push("/PageNotFound/");
    }
  }

  //Get review detail
  async function getReviewDetailByGuest(reviewId) {
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/review/guest/${reviewId}`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      reviewDetail.value = data.value;
      // console.log("get review detail completed");
    } else if (status == 400) {
      // console.log("get review detail uncompleted");
    } else if (status == 404) {
      router.push("/PageNotFound/");
    }
  }

  //Create review
  async function createReview() {
    let status = 0;
    successfulPopup.value = "load";
    await $fetch(`${import.meta.env.VITE_BASE_URL}/review`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${login.accessToken}`,
      },
      body: {
        rating: newReview.value.rating,
        detail: newReview.value.detail,
        title: newReview.value.title,
        userId: localStorage.getItem("id"),
        bookId: newReview.value.bookId,
        spoileFlag: newReview.value.spoileFlag == false ? 0 : 1,
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          successfulPopup.value = "hide";
          // console.log("upload review uncompleted");
        }
      },
    });
    if (status == 201) {
      successfulPopup.value = "show";
      // console.log("upload review completed");
    } else if (status == 401) {
      successfulPopup.value = "hide";
      await login.handleRefresh();
      await createReview();
    } else if (status == 404) {
      successfulPopup.value = "hide";
      router.push("/PageNotFound/");
    }
  }

  //Update review
  async function updateReview(reviewId) {
    let status = 0;
    await $fetch(`${import.meta.env.VITE_BASE_URL}/review/${reviewId}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${login.accessToken}`,
      },
      body: {
        rating: editReview.value.rating,
        detail: editReview.value.detail,
        title: editReview.value.title,
        bookId: editReview.value.bookId,
        spoileFlag: editReview.value.spoileFlag == false ? 0 : 1,
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          successfulPopup.value = "hide";
          // console.log("update review uncompleted");
        }
      },
    });
    if (status == 200) {
      successfulPopup.value = "show";
      // console.log("update review completed");
    } else if (status == 401) {
      successfulPopup.value = "hide";
      await login.handleRefresh();
      await updateReview(reviewId);
    } else if (status == 404) {
      successfulPopup.value = "hide";
      router.push("/PageNotFound/");
    }
  }

  //Delete review
  async function deleteReview(reviewId, bookId) {
    successfulPopup.value = "load";
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/review/${reviewId}`,
      {
        onRequest({ request, options }) {
          options.method = "Delete";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      successfulPopup.value = "show";
      reviewPage.value = 0;
      getReview(bookId);
      // console.log("delete review completed");
    } else if (status == 404) {
      successfulPopup.value = "hide";
      // console.log("delete review uncompleted");
    } else if (status == 401) {
      successfulPopup.value = "hide";
      await login.handleRefresh();
      await deleteReview(reviewId, bookId);
    } else if (status == 404) {
      successfulPopup.value = "hide";
      router.push("/PageNotFound/");
    }
  }

  //Create Like
  async function createLike(statusDetail) {
    let status = 0;

    await $fetch(`${import.meta.env.VITE_BASE_URL}/likeStatus`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${login.accessToken}`,
      },
      body: {
        userId: statusDetail.userId,
        reviewId: statusDetail.reviewId,
        likeStatus: statusDetail.likeStatus,
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          // console.log("like this review uncompleted");
        } else if (status == 401) {
          login.handleRefresh();
          createLike();
        }
      },
    });
    if (status == 201) {
      // console.log("like this review completed");
    }
  }

  //Update Like
  async function updateLike(statusDetail) {
    let status = 0;

    await $fetch(
      `${import.meta.env.VITE_BASE_URL}/likeStatus/${
        statusDetail.likeStatusId
      }`,
      {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${login.accessToken}`,
        },
        body: {
          userId: statusDetail.userId,
          reviewId: statusDetail.reviewId,
          likeStatus: statusDetail.likeStatus,
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
          if (status == 400) {
            // console.log("like this review uncompleted");
          } else if (status == 401) {
            login.handleRefresh();
            updateLike();
          }
        },
      }
    );
    if (status == 200) {
      // console.log("like this review completed");
    }
  }

  //Set edit review
  function setEditReview(bookId) {
    editReview.value = {
      reviewId: reviewDetail.value.data.reviewId,
      rating: reviewDetail.value.data.reviewRating,
      detail: reviewDetail.value.data.reviewDetail,
      title: reviewDetail.value.data.reviewTitle,
      spoileFlag: reviewDetail.value.data.spoileFlag == 0 ? false : true,
      bookId: reviewDetail.value.data.book.bookId,
    };
    console.log();
  }

  //Close successful popup
  function closeSuccessfulPopup() {
    successfulPopup.value = "hide";
    leavePopup.value = false;
    backPage().then(() => {
      clearNewReview();
    });
  }

  //Clear review list
  function clearReviewList() {
    reviewList.value = {
      data: {
        content: [],
        pageable: {
          totalPages: 1,
        },
      },
    };
  }

  //Clear new review
  function clearNewReview() {
    newReview.value = {
      rating: 1,
      detail: "",
      title: "",
      spoileFlag: false,
    };
  }

  //Clear my review list
  function clearMyReviewList() {
    myReviewList.value = {
      data: {
        content: [],
        pageable: {
          totalPages: 1,
        },
      },
    };
  }

  //Clear review list
  function clearNewReviewList() {
    newReviewList.value = {
      data: [],
    };
  }

  // function countUpdateTime(dateTime,dateValue,dateUnit){
  function countUpdateTime(seconds) {
    let interval = Math.floor(seconds / 31536000);
    if (interval >= 1) {
      return interval + " years ago";
    }

    interval = Math.floor(seconds / 2592000);
    if (interval >= 1) {
      return interval + " months ago";
    }

    interval = Math.floor(seconds / 86400);
    if (interval >= 1) {
      return interval + " days ago";
    }

    interval = Math.floor(seconds / 3600);
    if (interval >= 1) {
      return interval + " hours ago";
    }

    interval = Math.floor(seconds / 60);
    if (interval >= 1) {
      return interval + " minutes ago";
    }

    if (seconds < 10) return "just now";

    return Math.floor(seconds) + " seconds ago";
  }

  //Change review page
  function changeReviewPage(bookId, page) {
    reviewPage.value = page - 1;
    if (roleToken.value == "GUEST") {
      getReviewByGuest(bookId);
    } else {
      getReview(bookId);
    }
  }

  //Change my review page
  function changeMyReviewPage(page, userId) {
    myReviewPage.value = page - 1;
    getMyReview(userId);
  }

  //Back Page
  function backPage() {
    window.history.back();
  }

  return {
    reviewList,
    newReviewList,
    reviewDetail,
    newReview,
    editReview,
    reviewPage,
    myReviewPage,
    successfulPopup,
    leavePopup,
    myReviewList,
    sortReview,
    filterReview,
    loadSection,
    getReview,
    getReviewByGuest,
    getMyReview,
    getMyReviewByGuest,
    getNewReviewList,
    getNewReviewListByGuest,
    getReviewDetail,
    getReviewDetailByGuest,
    createReview,
    updateReview,
    deleteReview,
    createLike,
    updateLike,
    changeReviewPage,
    changeMyReviewPage,
    setEditReview,
    clearReviewList,
    clearMyReviewList,
    clearNewReviewList,
    clearNewReview,
    closeSuccessfulPopup,
    countUpdateTime,
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useReviews, import.meta.hot));
}
