import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";

export const useBooks = defineStore("Books", () => {
  const bookList = ref();
  const bookDetail = ref();
  const bookPage = ref(0);

//Get Library
async function getLibrary() {
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
      },
    },
  );
    bookList.value = data.value
    console.log(bookList.value);
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
  function countUpdateTime(countDateTime,dateTime){
    let newCountDateTime = '';
    let dateTimeArray = countDateTime.split(' ');
    let dateValue = dateTimeArray[0];
    let dateUnit = dateTimeArray[1];
    if(dateValue > 365 && dateUnit == 'days'){
      newCountDateTime = dateTime;
    }else if(dateValue > 365 && dateUnit == 'days'){

    }
  console.log(dateValue);
  console.log(dateUnit);
  // String[] parts = string.split("-");
  // String part1 = parts[0]; // 004
  // String part2 = parts[1]; // 034556
  // if(){

  // }
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