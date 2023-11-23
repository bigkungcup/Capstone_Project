import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";

export const useReviews = defineStore("Reviews", () => {
  const reviewList = ref();
  const reviewPage = ref(1);

//Get Library
async function getReview() {
  const { data } = await useFetch(
    `http://localhost:8080/api/review`,
    {
      // onRequest({ request, options }) {
      //   options.method = "GET";
      //   options.headers = {
      //     "Content-Type": "application/json",
      //   };
      // },
      // onResponse({ request, response, options }) {
      //   // bookList.value = response._data
      //   // console.log(bookList.value);
      //   // console.log(response._data);
      //   // console.log("RESPONSE", response._data);
      // }
    }
  );
    // console.log(bookList.value);
    reviewList.value = data.value
    // console.log(bookList.value);
}

  return { reviewList, reviewPage, getReview};

});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useReviews, import.meta.hot));
  }