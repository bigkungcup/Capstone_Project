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
          <div class="tw-flex tw-justify-center tw-my-3">
          <v-icon icon="mdi mdi-exclamation" color="#BB0000" style="font-size: 8rem;" ></v-icon>
        </div>
        <!-- <v-card-title style="height: 5rem; width: 40rem;"> -->
          <p class="warning-popUp-header tw-flex tw-justify-center tw-my-3">Warning !</p>
        <!-- </v-card-title> -->
        <!-- <v-card-text class="d-flex align-content-center justify-center flex-wrap" style="height: 3rem; width: 40rem;"> -->
            <div class="confirm-popUp-detail tw-flex tw-justify-center">You can not delete this book because it contains reviews.</div>
          <!-- </v-card-text> -->
        <v-card-actions style="height: 5rem; width: 40rem;"  class="tw-flex tw-justify-center">
          <v-btn class=" tw-mx-2" style="height: 3rem; width: 7rem; color: white;" color="#BB0000" variant="flat" size="large" rounded="xl" @click="$emit('close')">Got it!</v-btn>
        </v-card-actions>

      </v-card>
    </div>
</template>
 
<style>

</style>