import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";

export const useBooks = defineStore("Books", () => {
  const bookList = ref();
  const bookDetail = ref();
  const bookPage = ref(0);

//Get Library
async function getLibrary() {
  const { data , pending, error, refresh } = await useFetch(
    `http://localhost:8080/api/book`,
    {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
        };
        options.params = {
          page: bookPage.value,
        }
      },
    },
  );
    bookList.value = data.value
    console.log(bookList.value);
}

//Get Book Detail
async function getBookDetail(bookId) {
  const { data , pending, error, refresh } = await useFetch(
    `http://localhost:8080/api/book/${bookId}`,
    {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
        };
      },
    },
  );
    bookDetail.value = data.value
    console.log(bookDetail.value);
}

function getStarRating(number){
  console.log(number);
  return number = 0.5 * Math.floor(2 * number);
}
  return { bookList,bookDetail, bookPage, getLibrary, getBookDetail, getStarRating };

});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useBooks, import.meta.hot));
  }