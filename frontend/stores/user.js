import { defineStore, acceptHMRUpdate } from "pinia";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useLogin } from "./login";

export const useUsers = defineStore("Users", () => {
  // const accessToken = useCookie("accessToken");
  const refreshToken = useCookie("refreshToken");
  const login = useLogin();
  const router = useRouter();
  const registerFailed = ref(false);
  const registerFailedError = ref();
  const userList = ref({
    data: {
      content: [],
      pageable: {},
      totalPages: 1,
    },
  });
  const userDetail = ref({
    data: {},
  });
  const newUser = ref({
    displayName: "",
    email: "",
    password: "",
    role: "USER",
  });
  const editUser = ref();
  const editUserFile = ref();
  const userPage = ref(0);
  const failPopup = ref(false);
  const successfulPopup = ref(false);
  const confirmPopup = ref(false);
  const leavePopup = ref(true);
  const updateFailed = ref(false);
  const updateFailedError = ref();
  const followStatus = ref(0); //0 = unfollow, 1 = follow
  const followingList = ref({
    data: {
      content: [],
      pageable: {},
      totalPages: 1,
    },
  });
  const followerList = ref({
    data: {
      content: [],
      pageable: {},
      totalPages: 1,
    },
  });
  const followingPage = ref(0);
  const followerPage = ref(0);

  //Get user list
  async function getUserList() {
    let accessToken = useCookie("accessToken");
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/user/all`,
      {
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
      }
    );
    if (status == 200) {
      if (data.value) {
        userList.value = data.value;
      }
      console.log("get user list completed");
    } else if (status == 400) {
      clearUserList();
      console.log("get user list uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getUserList();
    } else if (status == 404) {
      router.push("/PageNotFound/");
    }
  }

  //Get User Detail
  let status = 0;
  async function getUserDetail(userId) {
    let accessToken = useCookie("accessToken");
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
      await login.handleRefresh();
      await getUserDetail(userId);
    } else if (status == 404) {
      router.push("/PageNotFound/");
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
        } else if (status == 500) {
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
    let accessToken = useCookie("accessToken");
    let status = 0;
    let user = {};
    if (editUserFile.value === null && userDetail.value.data.file !== null) {
      user = {
        displayName: editUser.value.displayName,
        email: editUser.value.email,
        password: editUser.value.password,
        bio: editUser.value.bio,
        role: editUser.value.role,
      };
    } else {
      user = {
        displayName: editUser.value.displayName,
        email: editUser.value.email,
        password: editUser.value.password,
        bio: editUser.value.bio,
        role: editUser.value.role,
        status: "edit",
      };
    }

    const formData = new FormData();
    formData.append(
      "user",
      new Blob([JSON.stringify(user)], { type: "application/json" })
    );
    if (
      userDetail.value.data.file === null &&
      editUserFile.value !== null &&
      editUserFile.value !== undefined
    ) {
      //Add cover case
      formData.append("file", editUserFile.value[0]);
    } else if (
      userDetail.value.data.file !== null &&
      editUserFile.value !== undefined &&
      editUserFile.value !== null
    ) {
      //Update cover case
      formData.append("file", editUserFile.value[0]);
    }

    await $fetch(`${import.meta.env.VITE_BASE_URL}/user/admin/${userId}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
      },
      body: formData,
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          updateFailed.value = true;
          updateFailedError.value = Object.values(response._data.filedErrors);
          console.log("update user uncompleted");
        } else if (status == 404) {
          router.push("/PageNotFound/");
        }
      },
    });
    if (status == 200) {
      successfulPopup.value = true;
      updateFailed.value = false;
      console.log("update user completed");
    } else if (status == 401) {
      await login.handleRefresh();
      await updateUser(userId);
    }
  }

  //Delete User by Admin
  async function deleteUser(userId) {
    let accessToken = useCookie("accessToken");
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
      router.push("/PageNotFound/");
    } else if (status == 401) {
      await login.handleRefresh();
      await deleteUser(userId);
    }
  }

    //Get Following list
    async function getFollowingList() {
      let accessToken = useCookie("accessToken");
      let status = 0;
      const { data } = await useFetch(
        `${import.meta.env.VITE_BASE_URL}/follow/following`,
        {
          onRequest({ request, options }) {
            options.method = "GET";
            options.headers = {
              "Content-Type": "application/json",
              Authorization: `Bearer ${accessToken.value}`,
            };
            options.params = {
              page: followingPage.value,
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
          followingList.value = data.value;
        }
        console.log("get user list completed");
      } else if (status == 400) {
        clearFollowingList();
        console.log("get user list uncompleted");
      } else if (status == 401) {
        await login.handleRefresh();
        await getFollowingList();
      }
    }
  
        //Get Follower list
        async function getFollowerList() {
          let accessToken = useCookie("accessToken");
          let status = 0;
          const { data } = await useFetch(
            `${import.meta.env.VITE_BASE_URL}/follow/follower`,
            {
              onRequest({ request, options }) {
                options.method = "GET";
                options.headers = {
                  "Content-Type": "application/json",
                  Authorization: `Bearer ${accessToken.value}`,
                };
                options.params = {
                  page: followerPage.value,
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
              followerList.value = data.value;
            }
            console.log("get user list completed");
          } else if (status == 400) {
            clearFollowerList();
            console.log("get user list uncompleted");
          } else if (status == 401) {
            await login.handleRefresh();
            await getFollowerList();
          }
        }    

  //Create Follower
  async function createFollower(userId) {
    let accessToken = useCookie("accessToken");
    let status = 0;

    await $fetch(`${import.meta.env.VITE_BASE_URL}/follow`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
      },
      params: {
        userFollowingId: userId,
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          console.log("follow uncompleted");
        } else if (status == 401) {
          login.handleRefresh();
          createFollower(userId)
        }
      },
    });
    if (status == 201) {
      console.log("follow completed");
    }
  }

  //Delete Follow
  async function deleteFollower(userId) {
    let accessToken = useCookie("accessToken");
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/follow/${userId}`,
      {
        onRequest({ request, options }) {
          options.method = "Delete";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken.value}`,
          }
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      console.log("unfollow completed");
    } else if (status == 400) {
      console.log("unfollow uncompleted");
    } else if (status == 404) {
    } else if (status == 401) {
      await login.handleRefresh();
      await deleteFollower(userId);
    }
  }

  //Clear user list
  function clearUserList() {
    userList.value = {
      data: {
        content: [],
        pageable: {},
        totalPages: 1,
      },
    };
  }

    //Clear following list
    function clearFollowingList() {
      followingList.value = {
        data: {
          content: [],
          pageable: {},
          totalPages: 1,
        },
      };
    }

      //Clear follower list
  function clearFollowerList() {
    followerList.value = {
      data: {
        content: [],
        pageable: {},
        totalPages: 1,
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
    };
  }

  function toggleUserFailPopup() {
    failPopup.value = !failPopup.value;
  }

  function changeUserPage(page) {
    userPage.value = page - 1;
    getUserList();
  }

  function changeFolloingPage(page) {
   followingPage.value = page - 1;
    getFollowingList();
  }

  function changeFollowerPage(page) {
    followerPage.value = page - 1;
    getFollowerList();
  }

  //set edit user
  async function setEditUser() {
    (editUser.value = {
      displayName: userDetail.value.data.displayName,
      email: userDetail.value.data.email,
      password: "",
      bio: userDetail.value.data.bio,
      role: userDetail.value.data.role,
      file: userDetail.value.data.file,
    }),
      (editUserFile.value = userDetail.value.data.file);
  }

  //Close successful popup
  function closeSuccessfulPopup() {
    successfulPopup.value = false;
    leavePopup.value = false;
    backPage();
  }

  //Back Page
  function backPage() {
    window.history.back();
  }

  return {
    userList,
    followingList,
    followerList,
    userDetail,
    newUser,
    editUser,
    editUserFile,
    userPage,
    followingPage,
    followerPage,
    failPopup,
    confirmPopup,
    successfulPopup,
    leavePopup,
    registerFailed,
    registerFailedError,
    updateFailed,
    updateFailedError,
    followStatus,
    getUserList,
    getUserDetail,
    updateUser,
    deleteUser,
    registerUser,
    getFollowingList,
    getFollowerList,
    createFollower,
    deleteFollower,
    clearUserList,
    clearNewUser,
    clearFollowingList,
    clearFollowerList,
    toggleUserFailPopup,
    changeUserPage,
    changeFolloingPage,
    changeFollowerPage,
    setEditUser,
    closeSuccessfulPopup,
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUsers, import.meta.hot));
}
