import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";

export const useReviews = defineStore("Reviews", () => {
  const reviewList = ref(
    {
      data: {
        content: [],
        pageable: {
          totalPages: 1
        }
      }
    }
  );
  const reviewPage = ref(0);
  const newReview = ref({
    rating: 1,
    detail: '',
    title: '',
    userId: '1',
    bookId: '',
    spoileFlag: false
  })

  //Get reviews
  async function getReview(bookId) {
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/review`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
          };
          options.params = {
            bookId: bookId,
            page: reviewPage.value,
            size: 10
          }
        }, onResponse({ request, response, options }) {
          status = response._data.response_code
          if (status == 400) {
            reviewList.value = {
              data: {
                content: [],
                pageable: {
                  totalPages: 1
                }
              }
            }
            console.log('get review list uncompleted')
          }
        }
      },
    );
    if (status == 200) {
      reviewList.value = data.value
      console.log('get review list completed');
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
          };
        }, onResponse({ request, response, options }) {
          status = response._data.response_code
        }
      },
    );
    if (status == 200) {
      newReview.value = data.value
      setNewReview();
      console.log('get review detail completed');
    } else if (status == 400) {
      console.log('get review detail uncompleted')
    }
  }


  //Create review
  async function createReview() {
    let status = 0;
    await $fetch(
      `${import.meta.env.VITE_BASE_URL}/review`,
      {
        method: "POST",
        body: {
          rating: newReview.value.rating,
          detail: newReview.value.detail,
          title: newReview.value.title,
          userId: '1',
          bookId: newReview.value.bookId,
          spoileFlag: newReview.value.spoileFlag == false ?  0 : 1
        }
        , onResponse({ request, response, options }) {
          status = response._data.response_code
          if (status == 400) {
            console.log('upload review uncompleted')
          }
        }
      }
    );
    if (status == 201) { 
      backPage().then(() => {
        clearNewReview();
      })
      console.log('upload review completed');
    }
  }

  //Update review
  async function updateReview(reviewId) {
    let status = 0;
    await $fetch(
      `${import.meta.env.VITE_BASE_URL}/review/${reviewId}`,
      {
        method: "PUT",
        body: {
          rating: newReview.value.rating,
          detail: newReview.value.detail,
          title: newReview.value.title,
          bookId: newReview.value.bookId,
          spoileFlag: newReview.value.spoileFlag == false ?  0 : 1
        }
        , onResponse({ request, response, options }) {
          status = response._data.response_code
          if (status == 400) {
            console.log('update review uncompleted')
          }
        }
      }
    );
    if (status == 200) {
      backPage().then(() => {
        clearNewReview();
      })
      console.log('update review completed');
    }
  }

  //Delete review
  async function deleteReview(reviewId, bookId) {
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/review/${reviewId}`,
      {
        onRequest({ request, options }) {
          options.method = "Delete";
          options.headers = {
            "Content-Type": "application/json",
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        }
      },
    );
    if (status == 200) {
      reviewPage.value = 0;
      getReview(bookId);
      console.log('delete review completed');
    } else if (status == 404) {
      console.log('delete review uncompleted')
    }
  }

  function setNewReview() {
    newReview.value = {
      reviewId: newReview.value.data.reviewId,
      rating: newReview.value.data.reviewRating,
      detail: newReview.value.data.reviewDetail,
      title: newReview.value.data.reviewTitle,
      spoileFlag: newReview.value.data.spoileFlag == 0 ? false : true,
      bookId: newReview.value.data.book.bookId
    }
  }

  //Clear new review
  function clearNewReview() {
    newReview.value = {
      rating: 1,
      detail: "",
      title: "",
      spoileFlag: false
    }
  }

  //Change review page
  function changeReviewPage(bookId, page) {
    reviewPage.value = page - 1;
    getReview(bookId);
  }

  //Back Page
  function backPage() {
    window.history.back()
  }

  return {
    reviewList,
    newReview,
    reviewPage,
    getReview,
    getReviewDetail,
    createReview,
    updateReview,
    deleteReview,
    changeReviewPage,
    setNewReview,
    clearNewReview,
  };

});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useReviews, import.meta.hot));
}