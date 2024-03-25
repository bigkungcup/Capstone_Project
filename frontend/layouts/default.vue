<script setup>
import { mergeProps } from "vue";
import { useLogin } from "~/stores/login";
import userNotiCard from "~/components/notification/userNotiCard.vue";
import systemNotiCard from "~/components/notification/systemNotiCard.vue";

const accessToken = ref(useCookie("accessToken"));
const loginStatus = ref(false);
const login = useLogin();
const notiSection = ref('user');

loginStatus.value = accessToken.value == undefined ? false : true;

function profileCoverPath(filePath) {
  return (filePath = `/ej2/_nuxt/@fs/${filePath}`);
}

//test
const notiList = ref({
    "response_code": 200,
    "response_status": "OK",
    "response_message": "All Notification",
    "response_datetime": "2024-03-23 17:57:01",
    "data": [
        {
            "notificationId": 1,
            "notificationTitle": "Bannarug: Scheduled Maintenance - Saturday 23/3/2024 12:00 AM (GMT+7)",
            "notificationDetail": "This is an announcement of upcoming maintenance. We will be shutting down our website for scheduled maintenance on Saturday the 23rd of March between 12:00AM and 13:00AM (GMT+7).",
            "notificationLevel": 1,
            "notificationLink": null,
            "notificationType": "ADMIN",
            "notificationStatus": 0,
            "notificationCreateDateTime": "2024-03-23T17:46:58",
            "notificationUpdateDateTime": "2024-03-23T17:46:58",
            "user": {
                "userId": 2,
                "displayName": "Admin Test",
                "email": "admin@mail.kmutt.ac.th",
                "role": "ADMIN",
                "followers": 0,
                "followings": 0,
                "totalReview": 0,
                "totalFavoriteReview": 0,
                "totalLike": 0,
                "bio": "TEST CREATE",
                "file": null
            },
            "book": null
        },{
            "notificationId": 2,
            "notificationTitle": "Bella (followed) has created a new review.",
            "notificationDetail": "Bella (followed) has created a new review.",
            "notificationLevel": 0,
            "notificationLink": null,
            "notificationType": "USER",
            "notificationStatus": 0,
            "notificationCreateDateTime": "2024-03-23T17:46:58",
            "notificationUpdateDateTime": "2024-03-23T17:46:58",
            "user": {
                "userId": 2,
                "displayName": "User Test2",
                "email": "TEST2@mail.kmutt.ac.th",
                "role": "USER",
                "followers": 0,
                "followings": 0,
                "totalReview": 0,
                "totalFavoriteReview": 0,
                "totalLike": 0,
                "bio": "TEST CREATE",
                "file": null
            },
            "book": null
        },
      ]})


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
      <NuxtLink to="/history/" v-show="login.roleToken == 'USER'"
        >History</NuxtLink
      >
      <NuxtLink to="/user/" v-show="login.roleToken == 'ADMIN'">User</NuxtLink>
      <NuxtLink to="/report/">Report</NuxtLink>
    </div>
    <div class="nav-icon-color tw-flex tw-space-x-6 tw-place-self-end tw-pr-16">
      <span
        class="d-flex align-center justify-center"
        v-if="login.roleToken != 'GUEST'"
      >
        <v-menu transition="slide-y-transition" :close-on-content-click="false">
          <template v-slot:activator="{ props: menu }">
            <v-icon
              icon="mdi-bell"
              v-bind="mergeProps(menu)"
              style="font-size: 40px"
              v-if="false"
            ></v-icon>
            <v-badge color="error" :content="2" v-if="true">
              <v-icon
                icon="mdi-bell"
                v-bind="mergeProps(menu)"
                style="font-size: 40px"
              ></v-icon>
            </v-badge>
          </template>
          <v-card class="tw-min-w-[20rem] tw-max-w-[25rem]">
            <v-list>
              <v-list-item>
                <v-list-item-title class=""
                  ><div class="d-flex justify-space-between">
                    <span class="web-text-title">Activity</span>
                    <span class="web-text-sub-grey py-1">Mark all as read</span>
                  </div>
                </v-list-item-title>
                <div class="web-text-sub-bold tw-space-x-5 px-3">
                  <span :class="notiSection == 'user' ? 'tw-text-[#3157BB] tw-cursor-pointer' : 'tw-cursor-pointer'" @click="notiSection = 'user'"
                    >Notification</span
                  >
                  <span :class="notiSection == 'system' ? 'tw-text-[#3157BB] tw-cursor-pointer' : 'tw-cursor-pointer'" @click="notiSection = 'system'"
                    >System Announcement</span
                  >
                </div>
              </v-list-item>
              <hr />
            </v-list>
            <v-list v-if="notiSection == 'user'">
              <userNotiCard :notiList="notiList.data.filter((element) => element.notificationType != 'ADMIN')"/>
            </v-list>
            <v-list v-if="notiSection == 'system'">
              <systemNotiCard :notiList="notiList.data.filter((element) => element.notificationType == 'ADMIN')"/>
            </v-list>
          </v-card>
        </v-menu>
      </span>

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
            <v-list-item to="/login/">
              <v-list-item-title class="web-text-detail tw-space-x-2 px-3"
                ><v-icon icon="mdi mdi-login"></v-icon
                ><span>Log-in</span></v-list-item-title
              >
            </v-list-item>
            <v-list-item
              class="hover:tw-bg-zinc-300/20 tw-cursor-pointer"
              to="/register/"
            >
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
            <v-list-item :to="`/profile/`">
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

<style></style>
