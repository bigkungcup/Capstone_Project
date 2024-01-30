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

  const accessToken = useCookie('accessToken', cookieOptions)
  const refreshToken = useCookie('refreshToken', cookieOptions)

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
        if (status == 400) {
          console.log("login uncompleted");
        }
      },
    });
    if (status == 200) {
      accessToken.value = data.access_token;
      refreshToken.value = data.refresh_token;
      router.push("/");
      console.log("login completed");
    }
  }

  //Refresh token
  async function handleRefresh(event) {
    let status = 0;
    const { data } = await $fetch(`${import.meta.env.VITE_BASE_URL}/auth/refresh`, {
      method: "GET",
      headers: {
        'Refresh-Token': refreshToken.value,
      },
      onResponse({ request, response, options }) {
        status = response.status;
        if (status == 401) {
          logOut();
        }
      },
    });
    console.log(status);
    if (status == 200) {
      accessToken.value = data.access_token;
      refreshToken.value = data.refresh_token;
      event();
    }
  }

  function logOut() {
    accessToken.value = null;
    refreshToken.value = null;
    router.push("/login");
  }

  //Clear login account
  function clearLoginAccount() {
    loginAccount.value = {
      email: "",
      password: "",
    };
  }

  return {
    loginAccount,
    handleLogin,
    handleRefresh,
    clearLoginAccount,
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useLogin, import.meta.hot));
}
