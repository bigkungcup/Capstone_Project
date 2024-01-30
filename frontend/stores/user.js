import { defineStore, acceptHMRUpdate } from "pinia";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useLogin } from "./login";

export const useUsers = defineStore("Users", () => {
  const accessToken = useCookie("accessToken");
  const login = useLogin();
  const router = useRouter();
  const userList = ref();
  const newUser = ref({
    displayName: "",
    email: "",
    password: "",
    role: "USER",
  });
  const userPage = ref(0);
  const failPopup = ref(false);

  //Get user list
  async function getUserList() {
    let status = 0;
    const { data } = await useFetch(`${import.meta.env.VITE_BASE_URL}/user`, {
      onRequest({ request, options }) {
        options.method = "GET";
        options.headers = {
          "Content-Type": "application/json",
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
      router.push("/");
      console.log("get user list completed");
    } else if (status == 400) {
      clearUserList();
      console.log("get user list uncompleted");
    }
  }

  //Register user
  async function registerUser() {
    let status = 0;
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
          console.log("register user uncompleted");
        } else if(status == 500) {
          failPopup.value = true;
          console.log("register user uncompleted");
        }
      },
    });
    if (status == 201) {
      login.loginAccount.email = newUser.value.email;
      login.loginAccount.password = newUser.value.password;
      login.handleLogin();
      console.log("register user completed");
    }
  }

  //Clear user list
  function clearUserList() {
    userList.value = {
      data: {
        content: [],
        pageable: {
          totalPages: 1,
        },
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

  return {
    userList,
    newUser,
    userPage,
    failPopup,
    getUserList,
    registerUser,
    clearUserList,
    clearNewUser,
    toggleUserFailPopup
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUsers, import.meta.hot));
}
