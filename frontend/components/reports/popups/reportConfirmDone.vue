<script setup>
defineEmits(["toggle",""]);
defineProps({
    dialog: {
        type: Boolean,
        require: true,
    },
})
</script>
 
<template>
    <div class="tw-grid tw-h-100% tw-min-h-screen tw-w-screen tw-absolute tw-inset-0 tw--top-[5.5rem] tw-bg-black/50">
        <v-card style="height: 20rem; width: 40rem;" class="rounded-xl tw-place-self-center">
        <v-card-title style="height: 5rem; width: 40rem;">
          <p class="confirm-popUp-header tw-flex tw-justify-center tw-my-4">Confirm Done</p>
          <div class="popup-horizontal-line"></div>
        </v-card-title>
        <v-card-text class="d-flex align-content-center justify-center flex-wrap" style="height: 10rem; width: 40rem;"><div class="confirm-popUp-detail">Are you sure to complete this report ?</div></v-card-text>
        <v-card-actions style="height: 5rem; width: 40rem;">
          <v-spacer></v-spacer>
          <v-btn class="tw-mx-2" style="height: 3rem; width: 7rem;" color="#727272" variant="tonal" size="large" rounded="xl" @click="$emit('toggle')">Cancel</v-btn>
          <v-btn class="tw-mx-4" style="height: 3rem; width: 7rem;" color="#1D419F" variant="flat" size="large" rounded="xl" @click="">Confirm</v-btn>
        </v-card-actions>
      </v-card>
    </div>
</template>
 
<style>

</style>