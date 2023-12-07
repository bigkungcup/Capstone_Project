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
    title:  '',
    userId: '1',
    bookId: '',
    spoileFlag: 0
  })
  const createConfirmPopup = {
    operation: 'create',
    buttonName: 'Upload',
    title: 'Confirm Create',
    detail: 'Are you sure to create this review?'
  }
  const updateConfirmPopup = {
    operation: 'update',
    buttonName: 'Submit',
    title: 'Confirm Update',
    detail: 'Are you sure to update this review?'
  }
  const leaveConfirmPopup = {
    operation: 'leave',
    buttonName: 'Back',
    title: 'Do you want to leave this site?',
    detail: 'Changes you made may not be saved.'
  }
  const deleteConfirmPopup = {
    operation: 'delete',
    buttonName: 'Delete',
    title: 'Confirm Delete',
    detail: 'Are you sure to delete this review?'
  }
  const validatePopup = {
    operation: 'validate',
    title: 'Error',
    detail: 'Please fill in required fields.'
  }
  const validate = ref(false);

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
      },onResponse({ request, response, options }) {
        status = response._data.response_code
      }
    },
  );
  if(status == 200){
    if(data.value){
      reviewList.value = data.value
    }
    console.log('get review list completed');
  }else if(status == 404){
    console.log('get review list uncompleted')
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
      },onResponse({ request, response, options }) {
        status = response._data.response_code
      }
    },
  );
  if(status == 200){
    newReview.value = data.value
    setNewReview();
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
            if(status == 400){
              validate.value = true;
              console.log(validate.value);
              console.log('upload review uncompleted')
            }
          }
      }
  );
  if(status == 201){
    validate.value = false;
    clearNewReview();
    backPage();
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
          title:  newReview.value.title,
          bookId: newReview.value.bookId,
          spoileFlag: newReview.value.spoileFlag
        }
        ,onResponse({ request, response, options }) {
          status = response._data.response_code
          if(status == 400){
            validate.value = true;
            console.log('update review uncompleted')
          }
        }
    }
);
if(status == 200){
  validate.value = false;
  clearNewReview();
  backPage();
  console.log('update review completed');
}
}

//Delete review
async function deleteReview(reviewId,bookId) {
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
        status = response._data.response_code
        console.log(status);
      }
    },
  );
  if(status == 200){
    reviewPage.value = 0;
    getReview(bookId);
    console.log('delete review completed');
  }else if(status == 404){
    console.log('delete review uncompleted')
  }
}

function setNewReview() {
  newReview.value = {
    reviewId: newReview.value.data.reviewId,
    rating: newReview.value.data.reviewRating,
    detail: newReview.value.data.reviewDetail,
    title:  newReview.value.data.reviewTitle,
    spoileFlag: newReview.value.data.spoileFlag,
    bookId: newReview.value.data.book.bookId
  }
}

//Clear new review
function clearNewReview() {
  newReview.value = {
    rating: 1,
    detail: "",
    title:  "",
    spoileFlag: 0
  }
}

// function clearEditReview() {
//   newReview.value = {
//     data: {
//       reviewRating: 1,
//       reviewDetail: '',
//       reviewTitle:  '',
//       spoileFlag: 0
//     }
//   }
// }

//Change review page
function changeReviewPage(bookId,page) {
  reviewPage.value = page-1;
  getReview(bookId);
}

//Back Page
function backPage() {
  window.history.back()
}

  return { reviewList, 
    newReview, 
    reviewPage, 
    createConfirmPopup, 
    updateConfirmPopup,
    leaveConfirmPopup,
    deleteConfirmPopup,
    validatePopup,
    validate,
    getReview, 
    getReviewDetail, 
    createReview, 
    updateReview, 
    deleteReview, 
    changeReviewPage,
    setNewReview, 
    clearNewReview };

});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useReviews, import.meta.hot));
  }