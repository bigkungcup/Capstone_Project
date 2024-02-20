import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useLogin } from "./login";

export const useReviews = defineStore("Reviews", () => {
  // const accessToken = ref(useCookie("accessToken"));
  const refreshToken = useCookie("refreshToken");
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
  const reviewDetail = ref();
  const newReview = ref({
    rating: 1,
    detail: "",
    title: "",
    userId: "1",
    bookId: "",
    spoileFlag: false,
  });
  const editReview = ref();
  const successfulPopup = ref(false);
  const leavePopup = ref(true);

  //Get reviews
  async function getReview(bookId) {
    let accessToken = useCookie("accessToken");
    let status = 0;
    const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/review`, {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken.value}`,
        };
        options.params = {
          bookId: bookId,
          page: reviewPage.value,
          size: 10,
        };
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
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
      reviewList.value = data.value;
      console.log("get review list completed");
    } else if (status == 401) {
      if(refreshToken.value !== null && refreshToken.value !== undefined){
        await login.handleRefresh();
        await getReview(bookId)
      }
    } else if (status == 404) {
      clearReviewList();
      console.log("get review list uncompleted");
    } 
  }

    //Get reviews by guest
    async function getReviewByGuest(bookId) {
      let status = 0;
      const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/review/guest`, {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
          };
          options.params = {
            bookId: bookId,
            page: reviewPage.value,
            size: 10,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
          if (status == 400) {
            reviewList.value = {
              data: {
                content: [],
                pageable: {
                  totalPages: 1,
                },
              },
            };
            console.log("get review list uncompleted");
          }
        },
      });
      if (status == 200) {
        reviewList.value = data.value;
        console.log("get review list completed");
      }
    }

  //Get review detail
  async function getReviewDetail(reviewId) {
    let status = 0;
    let accessToken = useCookie("accessToken")
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/review/${reviewId}`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken.value}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      reviewDetail.value = data.value;
      console.log("get review detail completed");
    } else if (status == 400) {
      console.log("get review detail uncompleted");
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
        console.log("get review detail completed");
      } else if (status == 400) {
        console.log("get review detail uncompleted");
      } else if (status == 404) {
        router.push("/PageNotFound/");
      }
    }

  //Create review
  async function createReview() {
    let accessToken = useCookie("accessToken");
    let status = 0;
    await $fetch(`${import.meta.env.VITE_BASE_URL}/review`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
      },
      body: {
        rating: newReview.value.rating,
        detail: newReview.value.detail,
        title: newReview.value.title,
        userId: localStorage.getItem('id'),
        bookId: newReview.value.bookId,
        spoileFlag: newReview.value.spoileFlag == false ? 0 : 1,
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          console.log("upload review uncompleted");
        }
      },
    });
    if (status == 201) {
      successfulPopup.value = true;
      console.log("upload review completed");
    } else if (status == 401) {
      await login.handleRefresh();
      await createReview();
    } else if (status == 404) {
      router.push("/PageNotFound/");
    }
  }

  //Update review
  async function updateReview(reviewId) {
    let accessToken = useCookie("accessToken");
    let status = 0;
    await $fetch(`${import.meta.env.VITE_BASE_URL}/review/${reviewId}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
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
          console.log("update review uncompleted");
        }
      },
    });
    if (status == 200) {
      successfulPopup.value = true;
      console.log("update review completed");
    } else if (status == 401) {
      await login.handleRefresh();
      await updateReview(reviewId);
    } else if (status == 404) {
      router.push("/PageNotFound/");
    }
  }

  //Delete review
  async function deleteReview(reviewId, bookId) {
    let accessToken = useCookie("accessToken");
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/review/${reviewId}`,
      {
        onRequest({ request, options }) {
          options.method = "Delete";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken.value}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      successfulPopup.value = true;
      reviewPage.value = 0;
      getReview(bookId);
      console.log("delete review completed");
    } else if (status == 404) {
      console.log("delete review uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await deleteReview(reviewId, bookId);
    } else if (status == 404) {
      router.push("/PageNotFound/");
    }
  }

  //Set edit review
  function setEditReview(bookId) {
    // await getReviewDetail(bookId);
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
    successfulPopup.value = false;
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

  //Change review page
  function changeReviewPage(bookId, page) {
    reviewPage.value = page - 1;
    getReview(bookId);
  }

  //Back Page
  function backPage() {
    window.history.back();
  }

  return {
    reviewList,
    reviewDetail,
    newReview,
    editReview,
    reviewPage,
    successfulPopup,
    leavePopup,
    getReview,
    getReviewByGuest,
    getReviewDetail,
    getReviewDetailByGuest,
    createReview,
    updateReview,
    deleteReview,
    changeReviewPage,
    setEditReview,
    clearReviewList,
    clearNewReview,
    closeSuccessfulPopup,
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useReviews, import.meta.hot));
}
