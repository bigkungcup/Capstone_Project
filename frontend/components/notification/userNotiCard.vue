<script setup>
import { useNotifications } from "~/stores/notification";
defineEmits(['clear'])
defineProps({
  notiList: {
    type: Array,
    require: true,
  },
});
const notification = useNotifications();

</script>

<template>
  <div class="">
    <v-list-item v-for="noti in notiList">
      <v-card :color="noti.notificationStatus == 0 ? '#3157BB' : 'white'" variant="tonal" :to="noti.notificationLink">
      <v-row no-gutters @click="$emit('clear', noti.notificationId),noti.notificationStatus = 1">
        <v-col cols="2" align="center" class="py-1">
          <v-img v-if="noti.notificationType == 'Bookmark'" :src="noti.book.file != null ? noti.book.file: '/image/cover_not_available.jpg'" height="80" width="40" cover/>
          <v-avatar v-if="noti.notificationType != 'Bookmark'" > 
            <v-img :src="noti.user.file != null ? noti.user.file : '/image/guest_icon.png'" height="80" width="40" cover/>
          </v-avatar>
        </v-col>
        <v-col cols="10" class="px-2">
      <div class="">
        <p class="web-text-sub-bold">{{ noti.notificationTitle }}</p>
        <p class="web-text-sub-thin">{{ noti.notificationDetail }}</p>
      </div>
      <v-list-item-subtitle class="mb-2 web-text-sub-thin">
        <p class="py-1">{{ notification.countUpdateTime(noti.countDateTime) }}</p>
      </v-list-item-subtitle>
        </v-col>
      </v-row></v-card>
      <hr />
    </v-list-item>
  </div>
</template>

<style></style>
