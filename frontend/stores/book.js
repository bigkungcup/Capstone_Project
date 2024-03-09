import { defineStore, acceptHMRUpdate } from "pinia";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useLogin } from "./login";

export const useBooks = defineStore("Books", () => {
  const refreshToken = useCookie("refreshToken");
  const roleToken = ref(localStorage.getItem('role'));
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
    bookTag: "",
    bookDetail: "",
  });
  const editBook = ref();
  const newBookFile = ref();
  const editBookFile = ref();
  const bookDetail = ref();
  const bookPage = ref(0);
  const historyList = ref({
    data: {
      content: [],
      pageable: {
        totalPages: 1,
      },
    },
  });
  const historyPage = ref(0);
  const bookmarkList = ref({
    data: {
      content: [],
      pageable: {
        totalPages: 1,
      },
    },
  });
  const bookmarkPage = ref(0);
  const successfulPopup = ref(false);
  const failPopup = ref(false);
  const leavePopup = ref(true);
  const bookType = ref();
  const sortBook = ref('desc')
  const filterBook = ref(0)
  const searchBook = ref('')
  const bookmarkedStatus = ref(0); // 0 = unbookmarked, 1 = bookmarked
  const runtimeConfig = useRuntimeConfig();

  //Get Library
  async function getLibrary() {
    let accessToken = useCookie("accessToken");
    let status = 0;
    let sortBybook = ''
    let sortTypebook = ''
      if(sortBook.value == 'desc' || sortBook.value == 'asc' ){
          sortBybook = sortBook.value;
      }
      else{
          sortBybook = 'desc';
          sortTypebook = sortBook.value;
      }

    const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/book`, {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken.value}`,
        };
        options.params = {
          page: bookPage.value,
          size: 10,
          sortType: sortBybook,
          sortBy: sortTypebook,
          booktypeId: filterBook.value == 0 ? '' : filterBook.value,
          search: searchBook.value
        }
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
      await login.handleRefresh();
      await getLibrary();
    }
  }

    //Get Library by guest
    async function getLibraryByGuest() {
      let status = 0;
      let sortBybook = ''
    let sortTypebook = ''
      if(sortBook.value == 'desc' || sortBook.value == 'asc' ){
          sortBybook = sortBook.value;
      }
      else{
          sortBybook = 'desc';
          sortTypebook = sortBook.value;
      }

      const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/book/guest`, {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
          };
          options.params = {
            page: bookPage.value,
            size: 10,
            sortType: sortBybook,
            sortBy: sortTypebook,
            booktypeId: filterBook.value == 0 ? '' : filterBook.value 
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
      } 
    }  


  //Get Book Detail
  async function getBookDetail(bookId) {
    let status = 0;
    let accessToken = useCookie("accessToken");
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
      bookmarkedStatus.value = bookDetail.value.data.bookmark;
      console.log("get book detail  completed");
    } else if (status == 400) {
      console.log("get book detail uncompleted");
    } else if (status == 401) {
        await login.handleRefresh();
        await getBookDetail(bookId);
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
        bookmarkedStatus.value = bookDetail.value.data.bookmark;
        console.log("get book detail  completed");
      } else if (status == 400) {
        console.log("get book detail uncompleted");
      } else if (status == 404) {
          router.push("/PageNotFound/");
      }
    }
  

  //Create Book
  async function createBook() {
    let accessToken = useCookie("accessToken");
    let status = 0;
    const book = {
      bookName: newBook.value.bookName,
      author: newBook.value.author,
      booktypeId: newBook.value.booktypeId,
      bookTag: newBook.value.bookTag == null ? "" : newBook.value.bookTag.join(','),
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
        }else if (status == 401) {
          login.handleRefresh();
          createBook();
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
    let accessToken = useCookie("accessToken");
    let book = {};
    if (editBookFile.value === null && bookDetail.value.data.file !== null) {
      book = {
        bookName: editBook.value.bookName,
        author: editBook.value.author,
        booktypeId: editBook.value.booktypeId,
        bookTag: editBook.value.bookTag == null ? "" : editBook.value.bookTag.join(','),
        bookDetail: editBook.value.bookDetail,
      };
    } else {
      book = {
        bookName: editBook.value.bookName,
        author: editBook.value.author,
        booktypeId: editBook.value.booktypeId,
        bookTag: editBook.value.bookTag == null ? "" : editBook.value.bookTag.join(','),
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
          failPopup.value = true;
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
      await login.handleRefresh();
      await updateBook(bookId);
    }
  }

  //Delete book
  async function deleteBook(bookId) {
    let accessToken = useCookie("accessToken");
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
      await login.handleRefresh();
      await deleteBook(bookId);
    }
  }


  //Get Bookmark
  async function getBookmarkList() {
    let accessToken = useCookie("accessToken");
    let status = 0;

    const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/bookmark`, {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken.value}`,
        };
        options.params = {
          page: bookmarkPage.value,
          size: 8,
        }
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
      },
    });
    if (status == 200) {
      if (data.value) {
        bookmarkList.value = data.value;
      }
      console.log("get bookmark list completed");
    } else if (status == 404) {
      clearBookmarkList();
      console.log("get library uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getBookmarkList();
    }
  }


  //Create Bookmark
    async function createBookmark(bookId) {
      let accessToken = useCookie("accessToken");
      let status = 0;

      await $fetch(`${import.meta.env.VITE_BASE_URL}/bookmark`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${accessToken.value}`,
        },
        params: {
          bookId: bookId
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
          if (status == 400) {
            console.log("bookmark uncompleted");
          } else if (status == 401) {
            login.handleRefresh();
            createBookmark(bookId);
          }
        },
      });
      if (status == 201) {
        console.log("bookmark completed");
      } 
    }

  //Delete bookmark by bookId
  async function deleteBookmarkByBookId(bookId) {
    let accessToken = useCookie("accessToken");
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/bookmark`,
      {
        onRequest({ request, options }) {
          options.method = "Delete";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken.value}`,
          };
          options.params = {
            bookId: bookId
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      console.log("delete bookmark completed");
    } else if (status == 400) {
      console.log("delete bookmark uncompleted");
    } else if (status == 404) {
    } else if (status == 401) {
      await login.handleRefresh();
      await deleteBookmarkByBookId(bookId);
    }
  }

    //Delete bookmark by bookmarkId
    async function deleteBookmark(bookmarkId) {
      let accessToken = useCookie("accessToken");
      let status = 0;
      const { data } = await useFetch(
        `${import.meta.env.VITE_BASE_URL}/bookmark/${bookId}`,
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
        console.log("delete bookmark completed");
      } else if (status == 400) {
        failPopup.value = true;
        console.log("delete bookmark uncompleted");
      } else if (status == 404) {
      } else if (status == 401) {
        await login.handleRefresh();
        await deleteBookmark(bookmarkId);
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
      await login.handleRefresh();
      await getBookType();
    }else if (status == 404) {
      router.push("/PageNotFound/");
    }
  }

  //Get History
  async function getHistoryList() {
    let accessToken = useCookie("accessToken");
    let status = 0;
    const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/history`, {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken.value}`,
        };
        options.params = {
          page: historyPage.value,
          size: 10
        };
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
      },
    });
    if (status == 200) {
      if (data.value) {
        historyList.value = data.value;
      }
      console.log("get library completed");
    } else if (status == 404) {
      clearHistoryList();
      console.log("get library uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getHistoryList();
    }
  }

  //Delete history by ID
  async function deleteHistoryById(bookId) {
    let accessToken = useCookie("accessToken");
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/history/${bookId}`,
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
      getHistoryList();
      // successfulPopup.value = true;
      // console.log("delete book completed");
    } else if (status == 400) {
      failPopup.value = true;
      // console.log("delete book uncompleted");
    } else if (status == 404) {
      // router.push("/PageNotFound/");
    } else if (status == 401) {
      await login.handleRefresh();
      await deleteHistoryById(bookId);
    }
  }

  //Delete history all
  async function deleteHistoryAll() {
    let accessToken = useCookie("accessToken");
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/history/all`,
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
      getHistoryList()
      // successfulPopup.value = true;
      // console.log("delete book completed");
    } else if (status == 400) {
      failPopup.value = true;
      // console.log("delete book uncompleted");
    } else if (status == 404) {
      // router.push("/PageNotFound/");
    } else if (status == 401) {
      await login.handleRefresh();
      await deleteHistoryAll();
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
    if(roleToken.value == 'GUEST'){
      getLibraryByGuest();
    }else{
      getLibrary();
    }
  }

  function changeHistoryPage(page) {
    historyPage.value = page - 1;
    getHistoryList();
  }

  function changeBookmarkPage(page) {
    bookmarkPage.value = page - 1;
    getBookmark();
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

    //Clear history list
    function clearHistoryList() {
      historyList.value = {
        data: {
          content: [],
          pageable: {
            totalPages: 1,
          },
        },
      };
    }

    //Clear bookmark list
    function clearBookmarkList() {
      bookmarkList.value = {
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
    historyList,
    bookmarkList,
    sortBook,
    filterBook,
    searchBook,
    bookPage,
    historyPage,
    bookmarkPage,
    successfulPopup,
    failPopup,
    leavePopup,
    bookmarkedStatus,
    getLibrary,
    getLibraryByGuest,
    getBookDetail,
    getBookDetailByGuest,
    getHistoryList,
    deleteHistoryAll,
    deleteHistoryById,
    createBook,
    updateBook,
    deleteBook,
    changeLibraryPage,
    changeHistoryPage,
    changeBookmarkPage,
    getStarRating,
    getBookmarkList,
    createBookmark,
    deleteBookmark,
    deleteBookmarkByBookId,
    getBookType,
    countUpdateTime,
    clearNewBook,
    clearHistoryList,
    setEditBook,
    closeSuccessfulPopup,
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useBooks, import.meta.hot));
}
