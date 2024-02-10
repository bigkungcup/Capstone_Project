import { defineStore, acceptHMRUpdate } from "pinia";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useLogin } from "./login";

export const useUsers = defineStore("Users", () => {
  const accessToken = useCookie("accessToken");
  const login = useLogin();
  const router = useRouter();
  const registerFailed = ref(false);
  const registerFailedError = ref();
  const userList = ref({
    data: {
      content: [],
      pageable: {},
      totalPages: 1,
    }
  });
  const userDetail = ref({
    data: {}
  });
  const newUser = ref({
    displayName: "",
    email: "",
    password: "",
    role: "USER",
  });
  const userPage = ref(0);
  const failPopup = ref(false);
  const successfulPopup = ref(false);
  const confirmPopup = ref(false);

  //Get user list
  async function getUserList() {
    let status = 0;
    const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/user/all`, {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken.value}`,
        };
        options.params = {
          page: userPage.value,
          size: 10,
        };
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
      },
    });
    if (status == 200) {
      if (data.value) {
        userList.value = data.value;
      }
      console.log("get user list completed");
    } else if (status == 400) {
      clearUserList();
      console.log("get user list uncompleted");
    } else if (status == 401) {
      login.handleRefresh(getUserList);
    }
  }

  //Get User Detail
  let status = 0;
  async function getUserDetail(userId) {
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/user/${userId}`,
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
      userDetail.value = data.value;
      console.log("get user detail completed");
    } else if (status == 400) {
      console.log("get user detail uncompleted");
    } else if (status == 401) {
      if(refreshToken.value !== null && refreshToken.value !== undefined){
        login.handleRefresh(getUserDetail(bookId));
      }
    }
  }

  //Register user
  async function registerUser() {
    let status = 0;
    registerFailed.value = false;
    registerFailedError.value = null;
    await $fetch(`${import.meta.env.VITE_BASE_URL}/user`, {
      method: "POST",
      body: {
        displayName: newUser.value.displayName,
        email: newUser.value.email,
        password: newUser.value.password,
        role: newUser.value.role,
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          registerFailed.value = true;
          registerFailedError.value = Object.values(response._data.filedErrors);
          console.log("register user uncompleted");
        } else if(status == 500) {
          failPopup.value = true;
          console.log("register user uncompleted");
        }
      },
    });
    if (status == 201) {
      router.push("/login");
      console.log("register user completed");
    }
  }

  //Update User by Admin
  async function updateUser(userId) {
    let status = 0;
    let user = {};
    // if (editBookFile.value === null && bookDetail.value.data.file !== null) {
    //   book = {
    //     bookName: editBook.value.bookName,
    //     author: editBook.value.author,
    //     bookGenre: editBook.value.bookGenre,
    //     bookDetail: editBook.value.bookDetail,
    //   };
    // } else {
    //   book = {
    //     bookName: editBook.value.bookName,
    //     author: editBook.value.author,
    //     bookGenre: editBook.value.bookGenre,
    //     bookDetail: editBook.value.bookDetail,
    //     status: "edit",
    //   };
    // }

    const formData = new FormData();
    formData.append(
      "user",
      new Blob([JSON.stringify(user)], { type: "application/json" })
    );
    // if (bookDetail.value.data.file === null && editBookFile.value !== null && editBookFile.value !== undefined ) {
    //   //Add cover case
    //   formData.append("file", editBookFile.value[0]);
    // } else if (
    //   bookDetail.value.data.file !== null &&
    //   editBookFile.value !== undefined &&
    //   editBookFile.value !== null
    // ) {
    //   //Update cover case
    //   formData.append("file", editBookFile.value[0]);
    // }

    await $fetch(`${import.meta.env.VITE_BASE_URL}/user/admin/${userId}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
      },
      body: formData,
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          console.log("update user uncompleted");
        }
      },
    });
    if (status == 200) {
      successfulPopup.value = true;
      console.log("update user completed");
    } else if (status == 401) {
      login.handleRefresh(updateUser(userId));
    }
  }

  //Delete User by Admin
    async function deleteUser(userId) {
      let status = 0;
      const { data } = await useFetch(
        `${import.meta.env.VITE_BASE_URL}/user/${userId}`,
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
        userPage.value = 0;
        // getReview(bookId);
        console.log("delete user completed");
      } else if (status == 404) {
        console.log("delete user uncompleted");
      } else if (status == 401) {
        login.handleRefresh(deleteUser(userId));
      }
    }

  //Clear user list
  function clearUserList() {
    userList.value = {
      data: {
        content: [],
        pageable: {},
        totalPages: 1
      },
    };
  }

  //Clear new book
  function clearNewUser() {
    newUser.value = {
        displayName: "",
        email: "",
        password: "",
        role: "USER",
    }
  }

  function toggleUserFailPopup() {
    failPopup.value = !failPopup.value;
  }

  function changeUserPage(page) {
    userPage.value = page - 1;
    getUserList();
  }

  return {
    userList,
    userDetail,
    newUser,
    userPage,
    failPopup,
    confirmPopup,
    successfulPopup,
    registerFailed,
    registerFailedError,
    getUserList,
    getUserDetail,
    updateUser,
    deleteUser,
    registerUser,
    clearUserList,
    clearNewUser,
    toggleUserFailPopup,
    changeUserPage
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUsers, import.meta.hot));
}
