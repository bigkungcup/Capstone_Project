<script setup>
import { useNotifications } from "~/stores/notification";
defineProps({
  notiList: {
    type: Array,
    require: true,
  },
});

const noti = useNotifications();

function handleImage(data) {
  if (data.notificationType == "USER") {
    if (data.user.file != null) {
      return data.user.file;
    } else {
      return "/image/guest_icon.png";
    }
  } else if (data.notificationType == "BOOK") {
    if (data.book.file != null) {
      return data.book.file;
    } else {
      return "/image/cover_not_available.jpg";
    }
  } else {
    return "/image/foryou3.jpg";
  }
}
</script>

<template>
  <div class="">
    <v-list-item v-for="noti in notiList" >
      <v-row no-gutters>
        <v-col cols="2" align="center" class="py-2">
          <v-img :src="handleImage(noti)" width="40" class="" />
        </v-col>
        <v-col cols="10" class="px-2">
          <div class="web-text-sub-bold">
        <p>{{ noti.notificationTitle }}</p>
      </div>
      <v-list-item-subtitle class="mb-2">
        <p>2 hours ago</p>
      </v-list-item-subtitle>
        </v-col>
      </v-row>
      <hr />
    </v-list-item>
  </div>
</template>

<style></style>
