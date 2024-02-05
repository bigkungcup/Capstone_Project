import { defineStore, acceptHMRUpdate } from "pinia";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useLogin } from "./login";

export const useUsers = defineStore("Users", () => {
  const accessToken = useCookie("accessToken");
  const login = useLogin();
  const router = useRouter();
  // const userList = ref();

  //-------------------------------------------------
//Test Data
const userList = ref({
  "data": {
        "content": [
            {
                "userId": 1,
                "displayName": "Test2",
                "email": "Test2@mail.com",
                "password": "$2a$10$j0TkBGjgAGeTV598646Dcu62UQJWXVuO1cfFw3FEP5xhKX69.JuvG",
                "role": "USER",
                "followers": 0,
                "follows": 0,
                "totalReview": 0,
                "totalFavoriteReview": 0,
                "totalLike": 0,
                "bio": null
            },
            {
                "userId": 2,
                "displayName": "User Test",
                "email": "TEST@mail.kmutt.ac.th",
                "password": "$2a$10$x3abwpouwyjWGfiYm0/fxe0N4TIS12uduIXqBWUj9v9dBGh3jq.Y.",
                "role": "USER",
                "followers": 0,
                "follows": 0,
                "totalReview": 0,
                "totalFavoriteReview": 0,
                "totalLike": 0,
                "bio": "TEST CREATE"
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 10,
            "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "pageNumber": 0,
        "size": 10,
        "totalPages": 1,
        "numberOfElements": 2,
        "totalElements": 2,
        "last": true,
        "first": true,
        "empty": false
    }
})
// -----------------------------------------------------

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
      router.push("/login");
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

  function changeUserPage(page) {
    userPage.value = page - 1;
    getUserList();
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
    toggleUserFailPopup,
    changeUserPage
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUsers, import.meta.hot));
}
