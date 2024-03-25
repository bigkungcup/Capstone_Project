<script setup>
defineProps({
    notiList: {
        type: Array,
        require: true,
    },
})
</script>
 
<template>
<div class="">
    <v-list-item v-for="noti in notiList" >
      <v-row no-gutters>
        <v-col cols="2" align="center" class="py-2">
          <v-img src="/image/system_noti.png" width="40" class="" />
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
 
<style>

</style>