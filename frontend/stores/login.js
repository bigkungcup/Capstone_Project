import { defineStore, acceptHMRUpdate } from "pinia";
import { useRouter } from "vue-router";
import { ref } from "vue";

export const useLogin = defineStore("Login", () => {
  const cookieOptions = {
    maxAge: 60 * 60 * 24, // 1 day
    path: "/",
  };
  const router = useRouter();
  const loginAccount = ref({
    email: "",
    password: "",
  });

  const accessToken = ref(useCookie("accessToken", cookieOptions));
  const refreshToken = ref(useCookie("refreshToken", cookieOptions));
  const idToken = ref(localStorage.getItem("id"));
  const roleToken = ref(localStorage.getItem("role"));
  const fileToken = ref(localStorage.getItem("file"));

  const setToken = (token) => {
    localStorage.setItem("id", token.userId);
    localStorage.setItem("role", token.role);
    localStorage.setItem("file", token.file);
  };

  const resetToken = () => {
    localStorage.removeItem("id");
    localStorage.removeItem("file");
    localStorage.setItem("role", 'GUEST');
  };

  const getIdToken = () => {
    return localStorage.getItem("id");
  };

  const getRoleToken = () => {
    return localStorage.getItem("role");
  };

  const getFileToken = () => {
    return localStorage.getItem("file");
  };

  const delete_cookie = (name) => {
    document.cookie =
      name + "=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;";
  };
  
  const profile = ref({
    data: {},
  });
  
  const editProfile = ref();
  const editProfileFile = ref();
  const failPopup = ref(false);
  const successfulPopup = ref(false);
  const loginFailed = ref(false);
  const leavePopup = ref(true);
  const updateFailed = ref(false);
  const updateFailedError = ref()
  const profilePic = ref(localStorage.getItem('file'))

  //Login
  async function handleLogin() {
    let status = 0;
    const { data } = await $fetch(
      `${import.meta.env.VITE_BASE_URL}/auth/login`,
      {
        method: "POST",
        body: {
          email: loginAccount.value.email,
          password: loginAccount.value.password,
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
          if (status == 401) {
            loginFailed.value = true;
            console.log("login uncompleted");
          }
        },
      }
    );
    if (status == 200) {
      loginFailed.value = false;
      accessToken.value = useCookie("accessToken", cookieOptions);
      refreshToken.value = useCookie("refreshToken", cookieOptions);
      accessToken.value = data.access_token;
      refreshToken.value = data.refresh_token;
      getProfile();
      router.push("/");
      console.log("login completed");
    }
  }

  //Refresh token
  async function handleRefresh() {
    let status = 0;
    const { data } = await $fetch(
      `${import.meta.env.VITE_BASE_URL}/auth/refresh`,
      {
        method: "GET",
        headers: {
          "Refresh-Token": refreshToken.value,
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
          if (status == 401) {
            logOut();
          };
        },
      }
    );
    if (status == 200) {
      accessToken.value = null;
      accessToken.value = data.access_token;
    }
  }

  //Profile
  async function getProfile() {
    let status = 0;
    const { data } = await $fetch(
      `${import.meta.env.VITE_BASE_URL}/auth/profile`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken.value}`,
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
          if (status == 400) {
            console.log("get ptofile uncompleted");
          }else if (status == 401) {
            handleRefresh();
            getProfile();
          }
        },
      }
    );
    if (status == 200) {
      profile.value = data;
      resetToken();
      setToken(profile.value);
      roleToken.value = getRoleToken();
      fileToken.value = getFileToken();
    }
  }

  //Update Profile
  async function updateProfile() {
    let status = 0;
    let user = {};
    if (editProfileFile.value === null && profile.value.file !== null) {
      user = {
        displayName: editProfile.value.displayName,
        email: editProfile.value.email,
        bio: editProfile.value.bio,
        role: editProfile.value.role,
      };
    } else {
      user = {
        displayName: editProfile.value.displayName,
        email: editProfile.value.email,
        bio: editProfile.value.bio,
        role: editProfile.value.role,
        status: "edit",
      };
    }

    const formData = new FormData();
    formData.append(
      "user",
      new Blob([JSON.stringify(user)], { type: "application/json" })
    );
    if (
      profile.value.file === null &&
      editProfileFile.value !== null &&
      editProfileFile.value !== undefined
    ) {
      //Add cover case
      formData.append("file", editProfileFile.value[0]);
    } else if (
      profile.value.file !== null &&
      editProfileFile.value !== undefined &&
      editProfileFile.value !== null
    ) {
      //Update cover case
      formData.append("file", editProfileFile.value[0]);
    }

    await $fetch(`${import.meta.env.VITE_BASE_URL}/user/${profile.value.userId}`, {
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
        }
      },
    });
    if (status == 200) {
      updateFailed.value = false;
      successfulPopup.value = true;
      fileToken.value = editProfileFile.value[0]
      console.log("update user completed");
    } else if (status == 401) {
      await handleRefresh();
      await updateProfile();
    }
  }

  //Change password
  async function changePassword(oldPassword,newPassword) {
    let status = 0;
    await $fetch(`${import.meta.env.VITE_BASE_URL}/user/resetPassword`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
      },
      body: {
        password: oldPassword,
        newPassword: newPassword
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          updateFailed.value = true;
          updateFailedError.value = Object.values(response._data.filedErrors);
          console.log("change password uncompleted");
        }
      },
    });
    if (status == 200) {
      updateFailed.value = false;
      successfulPopup.value = true;
      console.log("change password completed");
    } else if (status == 401) {
      await handleRefresh();
      await changePassword();
    }
  }

  //Log out
  function logOut() {
    // const myBroadcastChannel1 = new BroadcastChannel('accessToken');
    // const myBroadcastChannel2 = new BroadcastChannel('refreshToken');
    accessToken.value = null;
    delete_cookie("accessToken");
    refreshToken.value = null;
    delete_cookie("refreshToken");
    resetToken();
    // myBroadcastChannel1.close();
    // myBroadcastChannel2.close();
    router.push("/login");
  }

  //Clear login account
  function clearLoginAccount() {
    loginAccount.value = {
      email: "",
      password: "",
    };
  }

  function toggleProfileFailPopup() {
    failPopup.value = !failPopup.value;
  }

    //set edit profile
    async function setEditProfile() {
      (editProfile.value = {
        displayName: profile.value.displayName,
        email: profile.value.email,
        oldPassword: '',
        newPassword: '',
        bio: profile.value.bio,
        role: profile.value.role,
        file: profile.value.file,
      }),
        (editProfileFile.value = profile.value.file);
    }

  //Close successful popup
  function closeSuccessfulPopup() {
    successfulPopup.value = false;
    leavePopup.value = false;
    backPage()
  }

  //Back Page
  function backPage() {
    window.history.back();
  }


  return {
    loginAccount,
    loginFailed,
    profile,
    editProfile,
    editProfileFile,
    failPopup,
    // confirmPopup,
    successfulPopup,
    leavePopup,
    updateFailed,
    updateFailedError,
    profilePic,
    idToken,
    roleToken,
    fileToken,
    getProfile,
    updateProfile,
    handleLogin,
    handleRefresh,
    logOut,
    clearLoginAccount,
    toggleProfileFailPopup,
    setEditProfile,
    closeSuccessfulPopup,
    changePassword,
    resetToken,
    getIdToken,
    getRoleToken,
    getFileToken
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useLogin, import.meta.hot));
}
