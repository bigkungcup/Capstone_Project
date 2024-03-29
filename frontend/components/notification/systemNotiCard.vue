<script setup>
import { useNotifications } from "~/stores/notification";
defineEmits(['clear'])
defineProps({
    notiList: {
        type: Array,
        require: true,
    },
})
const notification = useNotifications();
</script>
 
<template>
<div class="">
    <v-list-item v-for="noti in notiList" >
      <v-card :color="noti.notificationStatus == 0 ? '#3157BB' : 'white'" variant="tonal" class="tw-cursor-pointer">
      <v-row no-gutters  @click="$emit('clear', noti.notificationId),noti.notificationStatus = 1">
        <v-col cols="2" align="center" class="py-1">
          <v-img src="/image/system_noti.png" width="40" class="" />
        </v-col>
        <v-col cols="10" class="px-2">
          <div class="web-text-sub-bold">
        <p>{{ noti.notificationTitle }}</p>
      </div>
      <v-list-item-subtitle class="mb-4">
        <p>{{ notification.countUpdateTime(noti.countDateTime) }}</p>
      </v-list-item-subtitle>
        </v-col>
      </v-row>
    </v-card>
      <hr />
    </v-list-item>
  </div>
</template>
 
<style>

</style>