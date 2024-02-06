import { defineStore, acceptHMRUpdate } from "pinia";
import { useRouter } from "vue-router";
import { ref } from "vue";

export const useLogin = defineStore("Login", () => {
  const cookieOptions = {
    maxAge: 60 * 60 * 24, // 1 day
    path: '/',
  }
  const router = useRouter();
  const loginAccount = ref({
    email: "",
    password: "",
  });

  const accessToken = ref(useCookie('accessToken', cookieOptions))
  const refreshToken = ref(useCookie('refreshToken', cookieOptions))
  const profile = ref(useCookie('profile', cookieOptions))
  const loginFailed = ref(false);

  //Login
  async function handleLogin() {
    let status = 0;
    const { data } = await $fetch(`${import.meta.env.VITE_BASE_URL}/auth/login`, {
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
    });
    if (status == 200) {
      loginFailed.value = false;
      accessToken.value = data.access_token;
      refreshToken.value = data.refresh_token;
      // getProfile();
      router.push("/");
      console.log("login completed");
    }
  }

  //Refresh token
  async function handleRefresh(event) {
    let status = 0;
    console.log(refreshToken.value);
      const { data } = await $fetch(`${import.meta.env.VITE_BASE_URL}/auth/refresh`, {
        method: "GET",
        headers: {
          'Refresh-Token': refreshToken.value,
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
          if (status == 401) {
            logOut();
          }
        },
      });
      if (status == 200) {
        accessToken.value = data.access_token;
        refreshToken.value = data.refresh_token;
        event;
      }
    }

    //Profile
    async function getProfile() {
      let status = 0;
      console.log(accessToken.value);
      const { data } = await $fetch(`${import.meta.env.VITE_BASE_URL}/auth/profile`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken.value}`,
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
          if (status == 400) {
            console.log("get ptofile uncompleted");
          }
        },
      });
      if (status == 200) {
        profile.value = data.role;
      }
    }

  function logOut() {
    const channel1 = new BroadcastChannel('accessToken');
    const channel2 = new BroadcastChannel('refreshToken');
    accessToken.value = null;
    refreshToken.value = null;
    channel1.close();
    channel2.close();
    // refreshToken.value = null;
    // profile.value = null;
    router.push("/login");
  }


  const delete_cookie = (name) => {
    document.cookie =
      name + "=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;";
  };

  //Clear login account
  function clearLoginAccount() {
    loginAccount.value = {
      email: "",
      password: "",
    };
  }

  return {
    loginAccount,
    loginFailed,
    handleLogin,
    handleRefresh,
    logOut,
    clearLoginAccount,
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useLogin, import.meta.hot));
}
