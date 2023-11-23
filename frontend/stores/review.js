import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";

export const useReviews = defineStore("Reviews", () => {
  const reviewList = ref();
  const reviewPage = ref(0);

//Get Library
async function getReview(bookId) {
  const { data } = await useFetch(
    `http://localhost:8080/api/review`,
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
      },
    },
  );
    reviewList.value = data.value
    console.log(reviewList.value);
}

  return { reviewList, reviewPage, getReview};

});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useReviews, import.meta.hot));
  }