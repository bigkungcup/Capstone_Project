import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";

export const useReviews = defineStore("Reviews", () => {
  const reviewList = ref();
  const reviewPage = ref(0);
  const newReview = ref({
    rating: 0,
    detail: '',
    title:  '',
    // userId: '',
    userId: '2',
    bookId: ''
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

//Get Library
async function getReview(bookId) {
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
      },
    },
  );
    reviewList.value = data.value
    console.log(reviewList.value);
}

async function createReview() {
  await $fetch(
    `${import.meta.env.VITE_BASE_URL}/review`,
    {
        method: "POST",
        body: {
            rating: newReview.value.rating,
            detail: newReview.value.detail,
            title:  newReview.value.title,
            userId: '2',
            bookId: newReview.value.bookId
          }
      },
  );
}

function clearNewReview() {
  newReview.value = {
    rating: '',
    detail: '',
    title:  ''
  }
}

function changeReviewPage(bookId,page) {
  reviewPage.value = page-1;
  getReview(bookId);
}

  return { reviewList, newReview, reviewPage, createConfirmPopup, getReview, createReview, changeReviewPage, clearNewReview };

});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useReviews, import.meta.hot));
  }