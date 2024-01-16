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
    <div v-show="dialog" class="tw-grid tw-h-100% tw-min-h-screen tw-w-screen tw-absolute tw-inset-0 tw--top-[5.5rem] tw-bg-black/50">
        <v-card style="height: 20rem; width: 40rem;" class="rounded-xl tw-place-self-center">
        <v-card-title style="height: 5rem; width: 40rem;">
          <p class="confirm-popUp-header tw-flex tw-justify-center tw-my-4">Success !</p>
          <div class="popup-horizontal-line"></div>
        </v-card-title>
        <v-card-text class="d-flex align-content-center justify-center flex-wrap" style="height: 10rem; width: 40rem;">
            <div class="confirm-popUp-detail">Your book has been updated successfully.</div></v-card-text>
        <v-card-actions style="height: 5rem; width: 40rem;">
          <v-spacer></v-spacer>
          <v-btn class="tw-mx-2" style="height: 3rem; width: 7rem; color: white;" color="#28E546" variant="flat" size="large" rounded="xl" @click="$emit('close')">OK</v-btn>
        </v-card-actions>
      </v-card>
    </div>
</template>
 
<style>

</style>