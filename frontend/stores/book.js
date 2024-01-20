import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";

export const useBooks = defineStore("Books", () => {
  const bookList = ref({
    data: {
      content: [],
      pageable: {
        totalPages: 1,
      },
    },
  });
  const newBook = ref({
    bookName: "",
    author: "",
    bookGenre: "",
    bookDetail: "",
  });
  const editBook = ref();
  const newBookFile = ref();
  const editBookFile = ref();
  const bookDetail = ref();
  const bookPage = ref(0);
  const successfulPopup = ref(false);
  const failPopup = ref(false);
  const leavePopup = ref(true);
  const runtimeConfig = useRuntimeConfig();

  //Get Library
  async function getLibrary() {
    let status = 0;
    const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/book`, {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
        };
        options.params = {
          page: bookPage.value,
        };
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
      },
    });
    if (status == 200) {
      if (data.value) {
        bookList.value = data.value;
      }
      console.log("get library completed");
    } else if (status == 400) {
      console.log("get library uncompleted");
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
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      bookDetail.value = data.value;
      console.log("get book detail  completed");
    } else if (status == 400) {
      console.log("get book detail uncompleted");
    }
  }

  //Create Book
  async function createBook() {
    let status = 0;
    const book = {
      bookName: newBook.value.bookName,
      author: newBook.value.author,
      bookGenre: newBook.value.bookGenre,
      bookDetail: newBook.value.bookDetail,
    };

    const formData = new FormData();
    formData.append(
      "book",
      new Blob([JSON.stringify(book)], { type: "application/json" })
    );
    formData.append(
      "file",
      newBookFile.value != undefined ? newBookFile.value[0] : null
    );
    console.log(formData);
    await $fetch(`${import.meta.env.VITE_BASE_URL}/book`, {
      method: "POST",
      body: formData,
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          console.log("upload book uncompleted");
        }
      },
    });
    if (status == 201) {
      successfulPopup.value = true;
      console.log("upload book completed");
    }
  }

  //Update Book
  async function updateBook(bookId) {
    let status = 0;
    const book = {
      bookName: editBook.value.bookName,
      author: editBook.value.author,
      bookGenre: editBook.value.bookGenre,
      bookDetail: editBook.value.bookDetail,
      bookTotalView: 5,
      bookRating: 4,
    };

    const formData = new FormData();
    formData.append(
      "book",
      new Blob([JSON.stringify(book)], { type: "application/json" })
    );
    formData.append(
      "file",
      editBookFile.value != undefined ? editBookFile.value[0] : null
    );
    console.log(formData);
    await $fetch(`${import.meta.env.VITE_BASE_URL}/book/${bookId}`, {
      method: "PUT",
      body: formData,
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          console.log("update book uncompleted");
        }
      },
    });
    if (status == 200) {
      successfulPopup.value = true;
      console.log("update book completed");
    }
  }

  //Delete book
  async function deleteBook(bookId) {
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/book/${bookId}`,
      {
        onRequest({ request, options }) {
          options.method = "Delete";
          options.headers = {
            "Content-Type": "application/json",
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      successfulPopup.value = true;
      console.log("delete book completed");
      }else if (status == 400) {
        failPopup.value = true;
        console.log("delete book uncompleted");
    } else if (status == 404) {
      console.log("delete book uncompleted");
    }
  }

  // function countUpdateTime(dateTime,dateValue,dateUnit){
  function countUpdateTime(seconds) {
    let interval = Math.floor(seconds / 31536000);
    if (interval >= 1) {
      return interval + " years ago";
    }

    interval = Math.floor(seconds / 2592000);
    if (interval >= 1) {
      return interval + " months ago";
    }

    interval = Math.floor(seconds / 86400);
    if (interval >= 1) {
      return interval + " days ago";
    }

    interval = Math.floor(seconds / 3600);
    if (interval >= 1) {
      return interval + " hours ago";
    }

    interval = Math.floor(seconds / 60);
    if (interval >= 1) {
      return interval + " minutes ago";
    }

    if (seconds < 10) return "just now";

    return Math.floor(seconds) + " seconds ago";
  }

  function changeLibraryPage(page) {
    bookPage.value = page - 1;
    getLibrary();
  }

  function getStarRating(number) {
    return (number = 0.5 * Math.floor(2 * number));
  }

  //Close successful popup
  function closeSuccessfulPopup() {
    successfulPopup.value = false;
    leavePopup.value = false;
    backPage().then(() => {
      clearNewBook();
    });
  }

  //Clear new book
  function clearNewBook() {
    newBook.value = {
      bookName: "",
      author: "",
      bookGenre: "",
      bookDetail: "",
    },
      newBookFile.value = null;
  }

  //set edit book
  async function setEditBook(bookId) {
    editBook.value = {
      bookName: bookDetail.value.data.bookName,
      author: bookDetail.value.data.author,
      bookGenre: bookDetail.value.data.bookGenre,
      bookDetail: bookDetail.value.data.bookDetail,
      file: bookDetail.value.data.file
    },
      editBookFile.value = bookDetail.value.data.file;
  }

  //Back Page
  function backPage() {
    window.history.back();
  }

  return {
    bookList,
    bookDetail,
    newBook,
    newBookFile,
    editBook,
    editBookFile,
    bookPage,
    successfulPopup,
    failPopup,
    leavePopup,
    getLibrary,
    getBookDetail,
    createBook,
    updateBook,
    deleteBook,
    changeLibraryPage,
    getStarRating,
    countUpdateTime,
    clearNewBook,
    setEditBook,
    closeSuccessfulPopup
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useBooks, import.meta.hot));
}
