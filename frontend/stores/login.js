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
  const profileToken = ref(useCookie("profileToken", cookieOptions));
  // const channel1 = new BroadcastChannel("accessToken");
  // const channel2 = new BroadcastChannel("refreshToken");  
  // const channel3 = new BroadcastChannel("profileToken");  
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
  const profileData = ref({
    id: 0,
    role: 'GUEST',
    file: null
  }
  );
  profileToken.value = profileData.value;

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
      accessToken.value = data.access_token;
      refreshToken.value = data.refresh_token;
      getProfile();
      router.push("/");
      console.log("login completed");
    }
  }

  //Refresh token
  async function handleRefresh(event=null ) {
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
          }
        },
      }
    );
    if (status == 200) {
      accessToken.value = data.access_token;
      refreshToken.value = data.refresh_token;
      event;
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
            handleRefresh(getProfile());
          }
        },
      }
    );
    if (status == 200) {
      profile.value = data;
      if(profileToken.value.role == 'GUEST'){
        setProfileData();
        profileToken.value = profileData.value;
      }
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
      profileToken.value = profileData.value;
      console.log("update user completed");
    } else if (status == 401) {
      handleRefresh(updateProfile);
    }
  }

  //Change password
  async function changePassword(oldPassword,newPassword) {
    console.log(oldPassword,newPassword);
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
      handleRefresh(changePassword());
    }
  }

  //Log out
  function logOut() {
    accessToken.value = null;
    refreshToken.value = null;
    // resetProfileData();
    // profileToken.value = profileData.value;
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

    //Set profile data
    function setProfileData() {
      profileData.value = {
        id: profile.value.userId,
        role: profile.value.role,
        file: profile.value.file
      }
    }

    //Resset profile data
    function resetProfileData() {
      profileData.value = {
        userId: 0,
        role: 'GUEST',
        file: null
      }
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
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useLogin, import.meta.hot));
}
