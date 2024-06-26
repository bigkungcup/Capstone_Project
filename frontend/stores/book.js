import { defineStore, acceptHMRUpdate } from "pinia";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useLogin } from "./login";

export const useBooks = defineStore("Books", () => {
  const roleToken = ref(localStorage.getItem("role"));
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
  const bookDetail = ref({
    data: {
      bookName: "",
      author: "",
      booktype: {
        booktypeId: 0,
      },
      bookTag: "",
      bookDetail: "",
      file: null,
    },
  });
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
  const successfulPopup = ref("hide");
  const failPopup = ref(false);
  const leavePopup = ref(true);
  const bookType = ref();
  const sortBook = ref("desc");
  const filterBook = ref(0);
  const searchBook = ref("");
  const bookmarkedStatus = ref(0); // 0 = unbookmarked, 1 = bookmarked
  const similarBookList = ref({
    data: [],
  });
  const recommendBookList = ref({
    data: [],
  });
  const mostviewBookList = ref({
    data: [],
  });
  const newBookList = ref({
    data: [],
  });
  const otherBookList = ref({
    data: [],
  });
  const rankingBookList = ref({
    data: [],
  });
  const rankingFilter = ref(0);
  const rankingSort = ref("totalView");
  const loadPage = ref("not");
  const loadSection = ref("not");

  //Get Library
  async function getLibrary() {
    loadPage.value = "load";
    let status = 0;
    let sortBybook = "";
    let sortTypebook = "";
    if (sortBook.value == "desc" || sortBook.value == "asc") {
      sortBybook = sortBook.value;
    } else {
      sortBybook = "desc";
      sortTypebook = sortBook.value;
    }

    const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/book`, {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${login.accessToken}`,
        };
        options.params = {
          page: bookPage.value,
          size: 10,
          sortType: sortBybook,
          sortBy: sortTypebook,
          booktypeId: filterBook.value == 0 ? "" : filterBook.value,
          search: searchBook.value,
        };
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
      },
    });
    if (status == 200) {
      if (data.value) {
        loadPage.value = "done";
        bookList.value = data.value;
      }
      // console.log("get library completed");
    } else if (status == 404) {
      loadPage.value = "done";
      clearBookList();
      // console.log("get library uncompleted");
    } else if (status == 401) {
      loadPage.value = "not";
      await login.handleRefresh();
      await getLibrary();
    }
  }

  //Get Library by guest
  async function getLibraryByGuest() {
    let status = 0;
    loadPage.value = "load";
    let sortBybook = "";
    let sortTypebook = "";
    if (sortBook.value == "desc" || sortBook.value == "asc") {
      sortBybook = sortBook.value;
    } else {
      sortBybook = "desc";
      sortTypebook = sortBook.value;
    }

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/book/guest`,
      {
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
            booktypeId: filterBook.value == 0 ? "" : filterBook.value,
            search: searchBook.value,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        loadPage.value = "done";
        bookList.value = data.value;
      }
      // console.log("get library completed");
    } else if (status == 404) {
      loadPage.value = "done";
      clearBookList();
      // console.log("get library uncompleted");
    }
  }

  //Get all book recommend
  async function getRecommendBookList() {
    let status = 0;
    clearRecommendBookList();

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/book/recommend`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        recommendBookList.value = data.value;
      }
      // console.log("get recommend book list completed");
    } else if (status == 404) {
      clearRecommendBookList();
      // console.log("get recommend book list uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getRecommendBookList();
    }
  }

  //Get all book most view
  async function getMostviewBookList() {
    let status = 0;
    clearMostviewBookList();

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/book/mostview`,
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
      if (data.value) {
        mostviewBookList.value = data.value;
      }
      // console.log("get most view book list completed");
    } else if (status == 404) {
      clearMostviewBookList();
      // console.log("get most view book list uncompleted");
    }
  }

  //Get all new book
  async function getNewBookList() {
    let status = 0;
    clearNewBookList();

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/book/newBook`,
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
      if (data.value) {
        newBookList.value = data.value;
      }
      // console.log("get new book list completed");
    } else if (status == 404) {
      clearNewBookList();
      // console.log("get new book list uncompleted");
    }
  }

  //Get all other book
  async function getOtherBookList() {
    let status = 0;
    clearOtherBookList();

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/book/random`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        otherBookList.value = data.value;
      }
      // console.log("get other book list completed");
    } else if (status == 404) {
      clearOtherBookList();
      // console.log("get other book list uncompleted");
    }
  }

  //Get ranking book
  async function getRankingBookList() {
    let status = 0;
    loadPage.value = "load";
    clearRankingBookList();

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/book/ranking`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
          };
          options.params = {
            bookTypeId: rankingFilter.value == 0 ? "" : rankingFilter.value,
            sortBy: rankingSort.value,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        loadPage.value = "done";
        rankingBookList.value = data.value;
      }
      // console.log("get ranking book list completed");
    } else {
      loadPage.value = "done";
      clearRankingBookList();
      // console.log("get ranking book list uncompleted");
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
            Authorization: `Bearer ${login.accessToken}`,
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
      // console.log("get book detail completed");
    } else if (status == 400) {
      // console.log("get book detail uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getBookDetail(bookId);
    } else if (status == 404) {
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
      // console.log("get book detail  completed");
    } else if (status == 400) {
      // console.log("get book detail uncompleted");
    } else if (status == 404) {
      router.push("/PageNotFound/");
    }
  }

  //Create Book
  async function createBook() {
    successfulPopup.value = "load";
    let status = 0;
    const book = {
      bookName: newBook.value.bookName,
      author: newBook.value.author,
      booktypeId: newBook.value.booktypeId,
      bookTag:
        newBook.value.bookTag == null ? "" : newBook.value.bookTag.join(","),
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
        Authorization: `Bearer ${login.accessToken}`,
      },
      body: formData,
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          successfulPopup.value = "hide";
          failPopup.value = true;
          // console.log("upload book uncompleted");
        } else if (status == 404) {
          successfulPopup.value = "hide";
          router.push("/PageNotFound/");
        } else if (status == 401) {
          successfulPopup.value = "hide";
          login.handleRefresh();
          createBook();
        }
      },
    });
    if (status == 201) {
      successfulPopup.value = "show";
      // console.log("upload book completed");
    }
  }

  //Update Book
  async function updateBook(bookId) {
    let status = 0;
    let book = {};
    successfulPopup.value = "load";
    if (editBookFile.value === null && bookDetail.value.data.file !== null) {
      book = {
        bookName: editBook.value.bookName,
        author: editBook.value.author,
        booktypeId: editBook.value.booktypeId,
        bookTag:
          editBook.value.bookTag == null
            ? ""
            : editBook.value.bookTag.join(","),
        bookDetail: editBook.value.bookDetail,
      };
    } else {
      book = {
        bookName: editBook.value.bookName,
        author: editBook.value.author,
        booktypeId: editBook.value.booktypeId,
        bookTag:
          editBook.value.bookTag == null
            ? ""
            : editBook.value.bookTag.join(","),
        bookDetail: editBook.value.bookDetail,
        status: "edit",
      };
    }

    const formData = new FormData();
    formData.append(
      "book",
      new Blob([JSON.stringify(book)], { type: "application/json" })
    );
    if (
      bookDetail.value.data.file === null &&
      editBookFile.value !== null &&
      editBookFile.value !== undefined
    ) {
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
        Authorization: `Bearer ${login.accessToken}`,
      },
      body: formData,
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          successfulPopup.value = "hide";
          failPopup.value = true;
          // console.log("update book uncompleted");
        } else if (status == 404) {
          successfulPopup.value = "hide";
          router.push("/PageNotFound/");
        }
      },
    });
    if (status == 200) {
      successfulPopup.value = "show";
      // console.log("update book completed");
    } else if (status == 401) {
      successfulPopup.value = "hide";
      await login.handleRefresh();
      await updateBook(bookId);
    }
  }

  //Delete book
  async function deleteBook(bookId) {
    let status = 0;
    successfulPopup.value = "load";

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/book/${bookId}`,
      {
        onRequest({ request, options }) {
          options.method = "Delete";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      successfulPopup.value = "show";
      // console.log("delete book completed");
    } else if (status == 400) {
      successfulPopup.value = "hide";
      failPopup.value = true;
      // console.log("delete book uncompleted");
    } else if (status == 404) {
      successfulPopup.value = "hide";
      router.push("/PageNotFound/");
    } else if (status == 401) {
      successfulPopup.value = "hide";
      await login.handleRefresh();
      await deleteBook(bookId);
    }
  }

  //Get Bookmark
  async function getBookmarkList(userId) {
    let status = 0;
    loadSection.value = "load";

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/bookmark`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
          options.params = {
            userId: userId,
            page: bookmarkPage.value,
            size: 20,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        loadSection.value = "done";
        bookmarkList.value = data.value;
      }
      // console.log("get bookmark list completed");
    } else if (status == 401) {
      loadSection.value = "not";
      await login.handleRefresh();
      await getBookmarkList(userId);
    } else {
      loadSection.value = "done";
      clearBookmarkList();
      // console.log("get library uncompleted");
    }
  }

  //Get Bookmark by guest
  async function getBookmarkListByGuest(userId) {
    let status = 0;
    loadSection.value = "load";

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/bookmark`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
          };
          options.params = {
            userId: userId,
            page: bookmarkPage.value,
            size: 20,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        loadSection.value = "done";
        bookmarkList.value = data.value;
      }
      // console.log("get bookmark list completed");
    } else {
      loadSection.value = "done";
      clearBookmarkList();
      // console.log("get library uncompleted");
    }
  }

  //Create Bookmark
  async function createBookmark(bookId) {
    let status = 0;

    await $fetch(`${import.meta.env.VITE_BASE_URL}/bookmark`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${login.accessToken}`,
      },
      params: {
        bookId: bookId,
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          // console.log("bookmark uncompleted");
        } else if (status == 401) {
          login.handleRefresh();
          createBookmark(bookId);
        }
      },
    });
    if (status == 201) {
      getBookDetail(bookId);
      // console.log("bookmark completed");
    }
  }

  //Delete bookmark by bookId
  async function deleteBookmarkByBookId(bookId) {
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/bookmark`,
      {
        onRequest({ request, options }) {
          options.method = "Delete";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
          options.params = {
            bookId: bookId,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      getBookDetail(bookId);
      // console.log("delete bookmark completed");
    } else if (status == 400) {
      // console.log("delete bookmark uncompleted");
    } else if (status == 404) {
    } else if (status == 401) {
      await login.handleRefresh();
      await deleteBookmarkByBookId(bookId);
    }
  }

  //Delete bookmark by bookmarkId
  async function deleteBookmark(bookmarkId) {
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/bookmark/${bookId}`,
      {
        onRequest({ request, options }) {
          options.method = "Delete";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      // console.log("delete bookmark completed");
    } else if (status == 400) {
      failPopup.value = true;
      // console.log("delete bookmark uncompleted");
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
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/booktype`,
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
      if (data.value) {
        content = data.value;
        bookType.value = content.data;
      }
      // console.log("get library completed");
    } else if (status == 400) {
      clearBookList();
      // console.log("get library uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getBookType();
    } else if (status == 404) {
      router.push("/PageNotFound/");
    }
  }

  //Get History
  async function getHistoryList() {
    loadPage.value = "load";
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/history`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
          options.params = {
            page: historyPage.value,
            size: 10,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        loadPage.value = "done";
        historyList.value = data.value;
      }
      // console.log("get library completed");
    } else if (status == 404) {
      loadPage.value = "done";
      clearHistoryList();
      // console.log("get library uncompleted");
    } else if (status == 401) {
      loadPage.value = "not";
      await login.handleRefresh();
      await getHistoryList();
    }
  }

  //Delete history by ID
  async function deleteHistoryById(bookId) {
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/history/${bookId}`,
      {
        onRequest({ request, options }) {
          options.method = "Delete";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      getHistoryList();
    } else if (status == 400) {
      failPopup.value = true;
    } else if (status == 404) {
      // router.push("/PageNotFound/");
    } else if (status == 401) {
      await login.handleRefresh();
      await deleteHistoryById(bookId);
    }
  }

  //Delete history all
  async function deleteHistoryAll() {
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/history/all`,
      {
        onRequest({ request, options }) {
          options.method = "Delete";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      getHistoryList();
    } else if (status == 400) {
      failPopup.value = true;
    } else if (status == 404) {
      // router.push("/PageNotFound/");
    } else if (status == 401) {
      await login.handleRefresh();
      await deleteHistoryAll();
    }
  }

  //Get Similar Book
  async function getSimilarBook(bookTypeId, bookId) {
    let status = 0;
    clearSimilarBookList();
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/book/similar/${bookTypeId}`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
          };
          options.params = {
            bookId: bookId,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      similarBookList.value = data.value;
      // console.log("get similar book completed");
    } else if (status == 400) {
      // console.log("get similar book uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getSimilarBook(bookTypeId, bookId);
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
    if (roleToken.value == "GUEST") {
      getLibraryByGuest();
    } else {
      getLibrary();
    }
  }

  function changeHistoryPage(page) {
    historyPage.value = page - 1;
    getHistoryList();
  }

  function changeBookmarkPage(page, userId) {
    bookmarkPage.value = page - 1;
    if (roleToken.value == "GUEST") {
      getBookmarkListByGuest(userId);
    } else {
      getBookmarkList(userId);
    }
  }

  //Rating Star
  function getStarRating(number) {
    return (number = 0.5 * Math.floor(2 * number));
  }

  //Format Total 
  function formatTotalNumber(totalNumber) {
    if (totalNumber >= 1000 && totalNumber < 1000000) {
      return (totalNumber / 1000).toFixed(1) + "K";
    } else if (totalNumber >= 1000000) {
      return (totalNumber / 1000000).toFixed(1) + "M";
    } else {
      return totalNumber.toString();
    }
  }

  //Close successful popup
  function closeSuccessfulPopup() {
    successfulPopup.value = "hide";
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

  //Clear recommend book list
  function clearRecommendBookList() {
    recommendBookList.value = {
      data: [],
    };
  }

  //Clear most review book list
  function clearMostviewBookList() {
    mostviewBookList.value = {
      data: [],
    };
  }

  //Clear new book list
  function clearNewBookList() {
    newBookList.value = {
      data: [],
    };
  }

  //Clear other book list
  function clearOtherBookList() {
    otherBookList.value = {
      data: [],
    };
  }

  //Clear ranking book list
  function clearRankingBookList() {
    rankingBookList.value = {
      data: [],
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

  //Clear similar book list
  function clearSimilarBookList() {
    similarBookList.value = {
      data: [],
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
    recommendBookList,
    mostviewBookList,
    newBookList,
    otherBookList,
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
    similarBookList,
    rankingBookList,
    rankingFilter,
    rankingSort,
    loadPage,
    loadSection,
    getLibrary,
    getLibraryByGuest,
    getBookDetail,
    getBookDetailByGuest,
    getHistoryList,
    getSimilarBook,
    getRecommendBookList,
    getMostviewBookList,
    getNewBookList,
    getOtherBookList,
    getRankingBookList,
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
    getBookmarkListByGuest,
    createBookmark,
    deleteBookmark,
    deleteBookmarkByBookId,
    getBookType,
    countUpdateTime,
    formatTotalNumber,
    clearNewBook,
    clearHistoryList,
    clearSimilarBookList,
    clearRecommendBookList,
    clearMostviewBookList,
    clearNewBookList,
    clearOtherBookList,
    clearRankingBookList,
    setEditBook,
    closeSuccessfulPopup,
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useBooks, import.meta.hot));
}
