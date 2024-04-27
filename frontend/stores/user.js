import { defineStore, acceptHMRUpdate } from "pinia";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useLogin } from "./login";

export const useUsers = defineStore("Users", () => {
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
    data: {
      displayName: "",
      email: "",
      password: "",
      bio: "",
      role: "",
      file: null,
    },
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
  const successfulPopup = ref("hide");
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
  const rankingUserList = ref({
    data: [],
  });
  const rankingSort = ref("followers");
  const filterUser = ref("All");
  const searchUser = ref("");
  const loadPage = ref("not");
  const loadSection = ref("not");

  //Get user list
  async function getUserList() {
    let status = 0;
    loadPage.value = "load";
    let detail = ref({});
    if (filterUser.value == "All" || filterUser.value == "") {
      detail.value = {
        page: userPage.value,
        size: 10,
        search: searchUser.value,
      };
    } else {
      detail.value = {
        page: userPage.value,
        size: 10,
        role: filterUser.value.toUpperCase(),
        search: searchUser.value,
      };
    }

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/user/all`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
          options.params = detail.value;
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        loadPage.value = "done";
        userList.value = data.value;
      }
      // console.log("get user list completed");
    } else if (status == 400) {
      loadPage.value = "done";
      clearUserList();
      // console.log("get user list uncompleted");
    } else if (status == 401) {
      loadPage.value = "not";
      await login.handleRefresh();
      await getUserList();
    } else if (status == 404) {
      loadPage.value = "done";
      clearUserList();
      // console.log("get user list uncompleted");
    }
  }

  //Get ranking user
  async function getRankingUserList() {
    let status = 0;
    loadPage.value = "load";

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/user/ranking`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
          options.params = {
            sort: rankingSort.value,
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
        rankingUserList.value = data.value;
      }
      // console.log("get ranking user list completed");
    } else if (status == 401) {
      loadPage.value = "not";
      await login.handleRefresh();
      await getRankingUserList();
    } else if (status == 404) {
      loadPage.value = "done";
      clearRankingUserList();
      // console.log("get ranking user list uncompleted");
    }
  }

  //Get ranking user
  async function getRankingUserListByGuest() {
    let status = 0;
    loadPage.value = "load";
    clearRankingUserList();

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/user/ranking`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
          };
          options.params = {
            sort: rankingSort.value,
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
        rankingUserList.value = data.value;
      }
      // console.log("get ranking user list completed");
    } else if (status == 404) {
      loadPage.value = "done";
      clearRankingUserList();
      // console.log("get ranking user list uncompleted");
    }
  }

  //Get User Detail
  async function getUserDetail(userId) {
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/user/${userId}`,
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
      userDetail.value = data.value;
      // console.log("get user detail completed");
    } else if (status == 400) {
      // console.log("get user detail uncompleted");
    } else if (status == 403) {
      router.push("/UnauthenPage/");
    } else if (status == 404) {
      router.push("/PageNotFound/");
    }
  }

  //Get User Detail
  async function getUserDetailByGuest(userId) {
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/user/${userId}`,
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
      userDetail.value = data.value;
      // console.log("get user detail completed");
    } else if (status == 400) {
      // console.log("get user detail uncompleted");
    } else if (status == 403) {
      router.push("/UnauthenPage/");
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
          // console.log("register user uncompleted");
        } else if (status == 500) {
          failPopup.value = true;
          // console.log("register user uncompleted");
        }
      },
    });
    if (status == 201) {
      router.push("/login");
      // console.log("register user completed");
    }
  }

  //Update User by Admin
  async function updateUser(userId) {
    let status = 0;
    let user = {};
    successfulPopup.value = "load";
    if (editUserFile.value === null && userDetail.value.data.file !== null) {
      user = {
        displayName: editUser.value.displayName,
        password:
          editUser.value.password == ""
            ? userDetail.value.data.password
            : editUser.value.password,
        bio: editUser.value.bio,
      };
    } else {
      user = {
        displayName: editUser.value.displayName,
        password:
          editUser.value.password == ""
            ? userDetail.value.data.password
            : editUser.value.password,
        bio: editUser.value.bio,
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
        Authorization: `Bearer ${login.accessToken}`,
      },
      body: formData,
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          updateFailed.value = true;
          successfulPopup.value = "hide";
          updateFailedError.value = Object.values(response._data.filedErrors);
          // console.log("update user uncompleted");
        } else if (status == 404) {
          successfulPopup.value = "hide";
          router.push("/PageNotFound/");
        }
      },
    });
    if (status == 200) {
      successfulPopup.value = "show";
      updateFailed.value = false;
      // console.log("update user completed");
    } else if (status == 401) {
      successfulPopup.value = "hide";
      await login.handleRefresh();
      await updateUser(userId);
    }
  }

  //Delete User by Admin
  async function deleteUser(userId) {
    let status = 0;
    successfulPopup.value = "load";
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/user/${userId}`,
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
      userPage.value = 0;
      // console.log("delete user completed");
    } else if (status == 404) {
      successfulPopup.value = "hide";
      router.push("/PageNotFound/");
    } else if (status == 401) {
      successfulPopup.value = "hide";
      await login.handleRefresh();
      await deleteUser(userId);
    }
  }

  //Get Following list
  async function getFollowingList(userId) {
    let status = 0;
    loadSection.value = "load";

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/follow/following`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
          options.params = {
            userId: userId,
            page: followingPage.value,
            size: 15,
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
        followingList.value = data.value;
      }
      // console.log("get user list completed");
    } else if (status == 401) {
      loadSection.value = "not";
      await login.handleRefresh();
      await getFollowingList(userId);
    } else {
      loadSection.value = "done";
      clearFollowerList();
      // console.log("get user list uncompleted");
    }
  }

  //Get Following list
  async function getFollowingListByGuest(userId) {
    let status = 0;
    loadSection.value = "load";

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/follow/following`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
          };
          options.params = {
            userId: userId,
            page: followingPage.value,
            size: 15,
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
        followingList.value = data.value;
      }
      // console.log("get user list completed");
    } else {
      loadSection.value = "done";
      clearFollowerList();
      // console.log("get user list uncompleted");
    }
  }

  //Get Follower list
  async function getFollowerList(userId) {
    let status = 0;
    loadSection.value = "load";

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/follow/follower`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${login.accessToken}`,
          };
          options.params = {
            userId: userId,
            page: followerPage.value,
            size: 15,
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
        followerList.value = data.value;
      }
      // console.log("get user list completed");
    } else if (status == 401) {
      loadSection.value = "not";
      await login.handleRefresh();
      await getFollowerList(userId);
    } else {
      loadSection.value = "done";
      clearFollowerList();
      // console.log("get user list uncompleted");
    }
  }

  //Get Follower list by guest
  async function getFollowerListByGuest(userId) {
    let status = 0;
    loadSection.value = "load";

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/follow/follower`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
          };
          options.params = {
            userId: userId,
            page: followerPage.value,
            size: 15,
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
        followerList.value = data.value;
      }
      // console.log("get user list completed");
    } else {
      loadSection.value = "done";
      clearFollowerList();
      // console.log("get user list uncompleted");
    }
  }

  //Create Follower
  async function createFollower(userId) {
    let status = 0;

    await $fetch(`${import.meta.env.VITE_BASE_URL}/follow`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${login.accessToken}`,
      },
      params: {
        userFollowingId: userId,
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          // console.log("follow uncompleted");
        } else if (status == 401) {
          login.handleRefresh();
          createFollower(userId);
        }
      },
    });
    if (status == 201) {
      // console.log("follow completed");
    }
  }

  //Delete Follow
  async function deleteFollower(userId) {
    let status = 0;
    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/follow/${userId}`,
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
      // console.log("unfollow completed");
    } else if (status == 400) {
      // console.log("unfollow uncompleted");
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

  //Clear ranking user list
  function clearRankingUserList() {
    rankingUserList.value = {
      data: [],
    };
  }

  function toggleUserFailPopup() {
    failPopup.value = !failPopup.value;
  }

  function changeUserPage(page) {
    userPage.value = page - 1;
    getUserList();
  }

  function changeFolloingPage(page, userId) {
    followingPage.value = page - 1;
    getFollowingList(userId);
  }

  function changeFollowerPage(page, userId) {
    followerPage.value = page - 1;
    getFollowerList(userId);
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
    successfulPopup.value = "hide";
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
    rankingUserList,
    rankingSort,
    filterUser,
    searchUser,
    loadPage,
    loadSection,
    getUserList,
    getRankingUserList,
    getRankingUserListByGuest,
    getUserDetail,
    getUserDetailByGuest,
    updateUser,
    deleteUser,
    registerUser,
    getFollowingList,
    getFollowingListByGuest,
    getFollowerList,
    getFollowerListByGuest,
    createFollower,
    deleteFollower,
    clearUserList,
    clearNewUser,
    clearFollowingList,
    clearFollowerList,
    clearRankingUserList,
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
