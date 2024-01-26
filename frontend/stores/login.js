import { defineStore, acceptHMRUpdate } from "pinia";
import { useRouter } from "vue-router";
import { ref } from "vue";

export const useLogin = defineStore("Login", () => {
  const router = useRouter();
  const loginAccount = ref({
    email: "",
    password: "",
  });

  //Register user
  async function handleLogin() {
    let status = 0;
    await $fetch(`${import.meta.env.VITE_BASE_URL}/auth/login`, {
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
      router.push("/");
      //   successfulPopup.value = true;
      console.log("login completed");
    }
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
    clearLoginAccount,
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useLogin, import.meta.hot));
}
