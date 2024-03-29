<script setup>
defineEmits(["close"]);
defineProps({

})
</script>
 
<template>
    <div 
        class="tw-grid tw-h-100% tw-min-h-screen tw-w-screen tw-absolute tw-inset-0 tw--top-[5.5rem] tw-bg-black/50">
        <v-card style="height: 20rem; width: 40rem;" class="rounded-xl tw-place-self-center">
            <div class="tw-flex tw-justify-center">
                <v-icon color="#BB0000" style="font-size: 8rem;" class="tw-mt-2">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path d="M8.5 12.5L10.5 14.5L15.5 9.5" stroke="#082266" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path> <path d="M7 3.33782C8.47087 2.48697 10.1786 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12C2 10.1786 2.48697 8.47087 3.33782 7" stroke="#082266" stroke-width="1.5" stroke-linecap="round"></path> </g></svg>
                </v-icon>
            </div>
            <!-- <v-card-title style="height: 5rem; width: 40rem;"> -->
            <p class="web-text-header tw-flex tw-justify-center tw-my-3">Success !</p>
            <!-- </v-card-title> -->
            <v-card-text class="d-flex align-content-center justify-center flex-wrap tw-text-center tw-my-1"
                style="height: 3rem; width: 40rem;">
                <div class="warning-popUp-detail tw-px-8">Your report has been sent successfully.</div>
            </v-card-text>
            <v-card-actions style="height: 4rem; width: 40rem;" class="tw-flex tw-justify-center">
            <v-btn class=" tw-mx-2" style="height: 3rem; width: 7rem; color: white;" color="#1D419F" variant="flat"
                size="large" rounded="xl" @click="$emit('close')">OK</v-btn>
        </v-card-actions>
    </v-card>
</div></template>
 
<style></style>