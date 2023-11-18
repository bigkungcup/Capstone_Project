import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";

export const useBooks = defineStore("Books", () => {
  const bookList = ref();

//Get Library
async function getLibrary() {
  const { data } = await useFetch(
    `http://localhost:8080/api/book`,
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
    bookList.value = data.value
    // console.log(bookList.value);
}


function getStarRating(number){
  console.log(number);
  return number = 0.5 * Math.floor(2 * number);
}
  return { bookList, getLibrary, getStarRating };

});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useBooks, import.meta.hot));
  }