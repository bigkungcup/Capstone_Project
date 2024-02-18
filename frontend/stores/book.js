import { defineStore, acceptHMRUpdate } from "pinia";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useLogin } from "./login";

export const useBooks = defineStore("Books", () => {
  const accessToken = useCookie("accessToken");
  const refreshToken = useCookie("refreshToken");
  const router = useRouter();
  const login = useLogin();
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
    booktypeId: null,
    bookTag: null,
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
  const bookType = ref();
  const runtimeConfig = useRuntimeConfig();

  //Get Library
  async function getLibrary() {
    let status = 0;
    const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/book`, {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken.value}`,
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
    } else if (status == 404) {
      clearBookList();
      console.log("get library uncompleted");
    } else if (status == 401) {
      await login.handleRefresh(getLibrary());
    }
  }

    //Get Library by guest
    async function getLibraryByGuest() {
      let status = 0;
      const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/book/guest`, {
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
      } else if (status == 404) {
        clearBookList();
        console.log("get library uncompleted");
      } else if (status == 401) {
        await login.handleRefresh(getLibraryByGuest());
      }
    }  


  //Get Book Detail
  async function getBookDetail(bookId) {
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/book/${bookId}`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken.value}`,
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
    } else if (status == 401) {
      if(refreshToken.value !== null && refreshToken.value !== undefined){
        login.handleRefresh(getBookDetail(bookId));
      }
    }else if (status == 404) {
      router.push("/PageNotFound/");
    }
  }

    //Get Book Detail by guest
    async function getBookDetailByGuest(bookId) {
      let status = 0;
      const { data } = await useFetch(
        `${import.meta.env.VITE_BASE_URL}/book/guest/${bookId}`,
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
      } else if (status == 404) {
          router.push("/PageNotFound/");
      }
    }
  

  //Create Book
  async function createBook() {
    let status = 0;
    const book = {
      bookName: newBook.value.bookName,
      author: newBook.value.author,
      booktypeId: newBook.value.booktypeId,
      bookTag: newBook.value.bookTag == null ? null : newBook.value.bookTag.join(','),
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
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
      },
      body: formData,
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          failPopup.value = true;
          console.log("upload book uncompleted");
        }else if (status == 404) {
          router.push("/PageNotFound/");
        }
      },
    });
    if (status == 201) {
      successfulPopup.value = true;
      console.log("upload book completed");
    } else if (status == 401) {
      login.handleRefresh(createBook);
    }
  }

  //Update Book
  async function updateBook(bookId) {
    let status = 0;
    let book = {};
    if (editBookFile.value === null && bookDetail.value.data.file !== null) {
      book = {
        bookName: editBook.value.bookName,
        author: editBook.value.author,
        booktypeId: editBook.value.booktypeId,
        bookTag: editBook.value.bookTag == null ? null : editBook.value.bookTag.join(','),
        bookDetail: editBook.value.bookDetail,
      };
    } else {
      book = {
        bookName: editBook.value.bookName,
        author: editBook.value.author,
        booktypeId: editBook.value.booktypeId,
        bookTag: editBook.value.bookTag.join(','),
        bookDetail: editBook.value.bookDetail,
        status: "edit",
      };
    }

    const formData = new FormData();
    formData.append(
      "book",
      new Blob([JSON.stringify(book)], { type: "application/json" })
    );
    if (bookDetail.value.data.file === null && editBookFile.value !== null && editBookFile.value !== undefined ) {
      //Add cover case
      formData.append("file", editBookFile.value[0]);
    } else if (
      bookDetail.value.data.file !== null &&
      editBookFile.value !== undefined &&
      editBookFile.value !== null
    ) {
      //Update cover case
      formData.append("file", editBookFile.value[0]);
    }

    await $fetch(`${import.meta.env.VITE_BASE_URL}/book/${bookId}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
      },
      body: formData,
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          console.log("update book uncompleted");
        }else if (status == 404) {
          router.push("/PageNotFound/");
        }
      },
    });
    if (status == 200) {
      successfulPopup.value = true;
      console.log("update book completed");
    } else if (status == 401) {
      login.handleRefresh(updateBook(bookId));
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
            Authorization: `Bearer ${accessToken.value}`,
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
    } else if (status == 400) {
      failPopup.value = true;
      console.log("delete book uncompleted");
    } else if (status == 404) {
      router.push("/PageNotFound/");
    } else if (status == 401) {
      login.handleRefresh(deleteBook(bookId));
    }
  }

  //Get book type
  async function getBookType() {
    let status = 0;
    let content = [];
    const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/booktype`, {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken.value}`,
        };
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
      },
    });
    if (status == 200) {
      if (data.value) {
        // bookType.value = data.value
        // console.log(bookType.value.data[0].booktypeId);
        content = data.value
        bookType.value = content.data
      }
      console.log("get library completed");
    } else if (status == 400) {
      clearBookList();
      console.log("get library uncompleted");
    } else if (status == 401) {
      login.handleRefresh(getLibrary);
    }else if (status == 404) {
      router.push("/PageNotFound/");
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
    (newBook.value = {
      bookName: "",
      author: "",
      bookTypeId: null,
      bookTag: null,
      bookDetail: "",
    }),
      (newBookFile.value = null);
  }

  //Clear book list
  function clearBookList() {
    bookList.value = {
      data: {
        content: [],
        pageable: {
          totalPages: 1,
        },
      },
    };
  }

  //set edit book
  async function setEditBook() {
    (editBook.value = {
      bookName: bookDetail.value.data.bookName,
      author: bookDetail.value.data.author,
      booktypeId: bookDetail.value.data.booktype.booktypeId,
      bookTag: bookDetail.value.data.bookTagList,
      bookDetail: bookDetail.value.data.bookDetail,
      file: bookDetail.value.data.file,
    }),
      (editBookFile.value = bookDetail.value.data.file);
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
    bookType,
    bookPage,
    successfulPopup,
    failPopup,
    leavePopup,
    getLibrary,
    getLibraryByGuest,
    getBookDetail,
    getBookDetailByGuest,
    createBook,
    updateBook,
    deleteBook,
    changeLibraryPage,
    getStarRating,
    getBookType,
    countUpdateTime,
    clearNewBook,
    setEditBook,
    closeSuccessfulPopup,
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useBooks, import.meta.hot));
}
