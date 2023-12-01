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

//Get Library
async function getLibrary() {
  const { data, status } = await useFetch(
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
      },
    },
  );
  if(!data.value){
    console.log('get library uncompleted')
  }else if(data.value.response_code == 200){
    bookList.value = data.value
    console.log('get library completed');
  }
}

//Get Book Detail
async function getBookDetail(bookId) {
  const { data , pending, error, refresh } = await useFetch(
    `${import.meta.env.VITE_BASE_URL}/book/${bookId}`,
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
  // console.log(dateValue);
  // console.log(dateUnit);
  // String[] parts = string.split("-");
  // String part1 = parts[0]; // 004
  // String part2 = parts[1]; // 034556
  // if(){

  // }
// }

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