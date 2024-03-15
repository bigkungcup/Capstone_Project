<script setup>
import { mergeProps } from "vue";
import { useLogin } from "../stores/login";

const accessToken = ref(useCookie("accessToken"));
const loginStatus = ref(false);
const login = useLogin();

loginStatus.value = accessToken.value == undefined ? false : true;

function profileCoverPath(filePath) {
  return (filePath = `/ej2/_nuxt/@fs/${filePath}`);
}

</script>

<template>
  <div
    class="tw-grid tw-grid-cols-3 tw-py-4 tw-border-b-gray-300 tw-border-2 tw-font-bold"
  >
    <div class="tw-flex tw-space-x-2 tw-pl-16">
      <img src="/image/logo.png" style="height: 40px" />
      <p class="lily app-name-color tw-text-3xl tw-self-center">Bannarug</p>
    </div>
    <div
      class="nav-text-before-click tw-flex tw-space-x-6 tw-place-self-center tw-text-xl"
    >
      <NuxtLink to="/">Home</NuxtLink>
      <NuxtLink to="/ranking/">Ranking</NuxtLink>
      <NuxtLink to="/library/">Library</NuxtLink>
      <NuxtLink to="/history/" v-show="login.roleToken == 'USER'">History</NuxtLink>
      <NuxtLink to="/user/" v-show="login.roleToken == 'ADMIN'">User</NuxtLink>
    </div>
    <div class="nav-icon-color tw-flex tw-space-x-6 tw-place-self-end tw-pr-16">
      <span class="d-flex align-center justify-center"><v-icon icon="mdi-bell" style="font-size: 40px" v-if="login.roleToken != 'GUEST'"></v-icon></span>
      <span class="text-center" v-show="!loginStatus">
        <v-menu transition="slide-y-transition">
          <template v-slot:activator="{ props: menu }">
            <v-icon
              v-bind="mergeProps(menu)"
              icon="mdi-account-circle-outline"
              style="font-size: 50px"
            ></v-icon>
          </template>
          <v-list>
            <v-list-item to="/login/" >
              <v-list-item-title class="web-text-detail tw-space-x-2 px-3"
                ><v-icon icon="mdi mdi-login"></v-icon
                ><span>Log-in</span></v-list-item-title
              >
            </v-list-item>
            <v-list-item class="hover:tw-bg-zinc-300/20 tw-cursor-pointer" to="/register/">
              <v-list-item-title class="web-text-detail">
                <v-list-item-title
                  class="web-text-detail tw-space-x-2 px-3"
                  @click=""
                  ><v-icon icon="mdi mdi-account-plus"></v-icon
                  ><span>Register</span></v-list-item-title
                >
              </v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu></span
      >
      <span class="text-center" v-show="loginStatus">
        <v-menu transition="slide-y-transition">
          <template v-slot:activator="{ props: menu }">
            <v-icon
              v-if="login.fileToken == 'null'"
              v-bind="mergeProps(menu)"
              icon="mdi-account-circle"
              style="font-size: 50px"
            ></v-icon>
            <v-img
              class="tw-rounded-full tw-border-[#082266] tw-border-2 tw-cursor-pointer"
              :src="login.fileToken"
              v-if="login.fileToken !== 'null'"
              height="40"
              width="40"
              cover
              v-bind="mergeProps(menu)"
            ></v-img>
          </template>

          <v-list>
            <v-list-item :to="`/profile/`" >
              <v-list-item-title class="web-text-detail tw-space-x-2 px-3"
                ><v-icon icon="mdi mdi-account"></v-icon
                ><span>Profile</span></v-list-item-title
              >
            </v-list-item>
            <v-list-item class="hover:tw-bg-zinc-300/20 tw-cursor-pointer">
              <v-list-item-title class="web-text-detail">
                <v-list-item-title
                  class="web-text-detail tw-space-x-2 px-3"
                  @click="login.logOut()"
                  ><v-icon icon="mdi mdi-logout"></v-icon
                  ><span>Logout</span></v-list-item-title
                >
              </v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu></span
      >
      <!-- </NuxtLink> -->
    </div>
  </div>
</template>
