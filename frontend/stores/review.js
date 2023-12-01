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
    rating: 0,
    detail: '',
    title:  '',
    // userId: '',
    userId: '2',
    bookId: '',
    spoileFlag: 0
  })
  const createConfirmPopup = {
    operation: 'action',
    buttonName: 'Upload',
    title: 'Confirm Create',
    datail: 'Are you sure to create this review?'
  }
  const editConfirmPopup = {
    operation: 'action',
    buttonName: 'Submit',
    title: 'Confirm Edit',
    datail: 'Are you sure to edit this review?'
  }
  const leaveConfirmPopup = {
    operation: 'leave',
    buttonName: 'Back',
    title: 'Do you want to leave this site?',
    datail: 'Changes you made may not be saved.'
  }
  const deleteConfirmPopup = {
    operation: 'dalete',
    buttonName: 'Delete',
    title: 'Confirm Delete',
    datail: 'Are you sure to delete this review?'
  }

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
          size: 1
        }
      },onResponse({ request, response, options }) {
        status = response._data.response_code
      }
    },
  );
  if(status == 200){
    reviewList.value = data.value
    console.log('get review list completed');
  }else if(status == 404){
    console.log('get review list uncompleted')
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
            title:  newReview.value.title,
            userId: '1',
            bookId: newReview.value.bookId,
            spoileFlag: newReview.value.spoileFlag
          }
          ,onResponse({ request, response, options }) {
            status = response._data.response_code
            console.log(status);
          }
      }
  );
}

//Edit review


//Delete review
async function deleteReview(reviewId,bookId) {
  const { data } = await useFetch(
    `${import.meta.env.VITE_BASE_URL}/book/${reviewId}`,
    {
      onRequest({ request, options }) {
        options.method = "Delete";
        options.headers = {
          "Content-Type": "application/json",
        };
      },
      onRequestError({ request, options, error }) {
        console.log("ERROR", error);
      },
      onResponseError({ request, response, options }) {
        console.log("RESPONE ERROR", response);
      },
    },
  );
  console.log(data);
  if(!data.value){
    console.log('delete review uncompleted')
  }else if(data.value.response_code == 200){
  getReview(bookId)
  console.log('delete review completed');
}
}

//Clear new review
function clearNewReview() {
  newReview.value = {
    rating: '',
    detail: '',
    title:  ''
  }
}

//Change review page
function changeReviewPage(bookId,page) {
  reviewPage.value = page-1;
  getReview(bookId);
}

  return { reviewList, newReview, reviewPage, createConfirmPopup, getReview, createReview,deleteReview, changeReviewPage, clearNewReview };

});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useReviews, import.meta.hot));
  }