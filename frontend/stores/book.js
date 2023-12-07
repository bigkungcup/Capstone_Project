import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";

export const useBooks = defineStore("Books", () => {
  const bookList = ref({
    data: {
      content: [],
      pageable: {
        totalPages: 1
      }
    }
});
  const bookDetail = ref();
  const bookPage = ref(0);
  const runtimeConfig = useRuntimeConfig();

//Get Library
async function getLibrary() {
  let status = 0;
  const { data } = await useFetch(
    `${import.meta.env.VITE_BASE_URL}/book`,
    {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
        };
        options.params = {
          page: bookPage.value,
        }
      },onResponse({ request, response, options }) {
        status = response._data.response_code
      }
    },
  );
  if(status == 200){
    if(data.value){
      bookList.value = data.value
    }
    console.log('get library completed');
  }else if(status == 404){
    console.log('get library uncompleted')
  }
}

//Get Book Detail
let status = 0;
async function getBookDetail(bookId) {
  const { data } = await useFetch(
    `${import.meta.env.VITE_BASE_URL}/book/${bookId}`,
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
    bookDetail.value = data.value
    console.log('get book detail  completed');
  }else if(status == 404){
    console.log('get book detail uncompleted')
  }
}

// function countUpdateTime(dateTime,dateValue,dateUnit){
  function countUpdateTime(seconds){
    let interval = Math.floor(seconds / 31536000);
    if (interval > 1) {
      return interval + ' years ago';
    }
  
    interval = Math.floor(seconds / 2592000);
    if (interval > 1) {
      return interval + ' months ago';
    }
  
    interval = Math.floor(seconds / 86400);
    if (interval > 1) {
      return interval + ' days ago';
    }
  
    interval = Math.floor(seconds / 3600);
    if (interval > 1) {
      return interval + ' hours ago';
    }
  
    interval = Math.floor(seconds / 60);
    if (interval > 1) {
      return interval + ' minutes ago';
    }
  
    if(seconds < 10) return 'just now';
  
    return Math.floor(seconds) + ' seconds ago';
  }

function changeLibraryPage(page) {
  reviewPage.value = page-1;
  getLibrary();
}

function getStarRating(number){
  return number = 0.5 * Math.floor(2 * number);
}
  return { bookList,bookDetail, bookPage, getLibrary, getBookDetail, changeLibraryPage, getStarRating, countUpdateTime };

});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useBooks, import.meta.hot));
  }