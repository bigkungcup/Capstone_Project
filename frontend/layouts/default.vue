<script setup>
import { mergeProps } from "vue";
import { useLogin } from "~/stores/login";
import { useNotifications } from "~/stores/notification";
import userNotiCard from "~/components/notification/userNotiCard.vue";
import systemNotiCard from "~/components/notification/systemNotiCard.vue";
import NotiNotFound from "~/components/notification/notiNotFound.vue";

const accessToken = ref(useCookie("accessToken"));
const loginStatus = ref(false);
const login = useLogin();
const noti = useNotifications();
const notiSection = ref('user');

loginStatus.value = accessToken.value == undefined ? false : true;

function handleGetNotification(section) {
  noti.getCountAllNotification();
  if(section == 'user'){
    noti.getCountUserNotification();
    noti.getNotificationList(0)
  }else{
    noti.getCountSystemNotification();
    noti.getNotificationList(1)
  }
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
      <NuxtLink to="/history/" v-show="login.roleToken == 'USER'"
        >History</NuxtLink
      >
      <NuxtLink to="/user/" v-show="login.roleToken == 'ADMIN'">User</NuxtLink>
      <NuxtLink to="/report/" v-show="login.roleToken == 'ADMIN'">Report</NuxtLink>
    </div>
    <div class="nav-icon-color tw-flex tw-space-x-6 tw-place-self-end tw-pr-16">
      <span
        class="d-flex align-center justify-center"
        v-if="login.roleToken == 'USER'"
      >
        <v-menu transition="slide-y-transition" :close-on-content-click="false">
          <template v-slot:activator="{ props: menu }">
            <v-icon
              icon="mdi-bell"
              v-bind="mergeProps(menu)"
              style="font-size: 40px"
              @click="handleGetNotification(notiSection)"
              v-show="noti.countAllNotification.data.countNotification == 0 || noti.countAllNotification.data.countNotification == null"
            ></v-icon>
            <v-badge color="error" :content="noti.countAllNotification.data.countNotification" v-if="noti.countAllNotification.data.countNotification > 0">
              <v-icon
                icon="mdi-bell"
                v-bind="mergeProps(menu)"
                style="font-size: 40px"
                @click="handleGetNotification(notiSection)"
              ></v-icon>
            </v-badge>
          </template>
          <v-card class="tw-min-w-[25rem] tw-max-w-[25rem]">
            <v-list>
              <v-list-item>
                <v-list-item-title class=""
                  ><div class="d-flex justify-space-between">
                    <span class="web-text-title">Activity</span>
                    <span class="web-text-sub-grey py-1 tw-cursor-pointer" @click="noti.clearAllNotification(notiSection == 'user' ? 0 : 1)">Mark all as read</span>
                  </div>
                </v-list-item-title>
                <div class="d-flex web-text-sub-bold tw-space-x-5 px-3">
                  <div class="d-flex tw-cursor-pointer">
                  <p :class="notiSection == 'user' ? 'tw-text-[#3157BB]' : ''" @click="notiSection = 'user',handleGetNotification(notiSection)">Notification</p>
                    <v-badge class="px-4 py-3" color="error" :content="noti.countUserNotification.data.countNotificationNormal" v-if="noti.countUserNotification.data.countNotificationNormal > 0"></v-badge></div>
                  <div class="d-flex tw-cursor-pointer">
                  <p v-if="true" :class="notiSection == 'system' ? 'tw-text-[#3157BB] tw-cursor-pointer' : 'tw-cursor-pointer'" @click="notiSection = 'system',handleGetNotification(notiSection)"
                    >System Announcement</p>
                    <v-badge class="px-4 py-3" color="error" :content="noti.countSystemNotification.data.countNotificationSystem" v-if="noti.countSystemNotification.data.countNotificationSystem > 0"></v-badge></div>

                </div>
              </v-list-item>
              <hr />
            </v-list>
            <v-list v-if="notiSection == 'user'">
              <div v-if="noti.notificationList.data.length != 0">
              <v-virtual-scroll :items="['']" max-height="35rem">
              <userNotiCard :notiList="noti.notificationList.data" @clear="noti.clearNotificationById($event, 0)"/>
              </v-virtual-scroll>
              </div>
              <div v-if="noti.notificationList.data.length == 0">
                <NotiNotFound />
              </div>
              <!-- <userNotiCard :notiList="notiList.data.filter((element) => element.notificationLevel == 0)"/> -->
            </v-list>
            <v-list v-if="notiSection == 'system'">
              <div v-if="noti.notificationList.data.length != 0">
              <v-virtual-scroll :items="['']" max-height="35rem">
              <systemNotiCard :notiList="noti.notificationList.data" @clear="noti.clearNotificationById($event, 1)"/>
              </v-virtual-scroll>
            </div>
            <div v-if="noti.notificationList.data.length == 0">
                <NotiNotFound />
            </div>
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
