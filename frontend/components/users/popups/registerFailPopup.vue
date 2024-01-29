<script setup>
defineEmits(["close"]);
defineProps({
  dialog: {
    type: Boolean,
    require: true,
  },
})
</script>
 
<template>
  <div v-show="dialog"
    class="tw-grid tw-h-100% tw-min-h-screen tw-w-screen tw-absolute tw-inset-0 tw--top-[5.5rem] tw-bg-black/50">
    <v-card style="height: 20rem; width: 40rem;" class="rounded-xl tw-place-self-center">
      <div class="tw-flex tw-justify-center tw-my-3">
        <v-icon color="#BB0000" style="font-size: 8rem;">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
            <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
            <g id="SVGRepo_iconCarrier">
              <path
                d="M16 8L8 16M8.00001 8L16 16M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z"
                stroke="#BB0000" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
            </g>
          </svg>
        </v-icon>
      </div>
      <p class="warning-popUp-header tw-flex tw-justify-center tw-my-3">Warning !</p>
      <div class="confirm-popUp-detail tw-flex tw-justify-center">Username or email is already in use
        reviews.</div>
      <v-card-actions style="height: 5rem; width: 40rem;" class="tw-flex tw-justify-center">
      <v-btn class=" tw-mx-2" style="height: 3rem; width: 7rem; color: white;" color="#BB0000" variant="flat"
        size="large" rounded="xl" @click="$emit('close')">Got it!</v-btn>
    </v-card-actions>
  </v-card>
</div></template>
 
<style></style>