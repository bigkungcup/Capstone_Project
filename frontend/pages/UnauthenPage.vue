<script setup>

definePageMeta({
    layout: false,
});

</script>
 
<template>
    <div class="tw-flex tw-items-center tw-my-24">
        <v-row align="center" justify="center" no-gutters>
            <v-col cols="2"></v-col>
            <v-col cols="4">
                <v-img src="/image/warning.png" width="450" height="450"></v-img>
            </v-col>
            <v-col cols="5">
                <p class="tw-flex tw-justify-right tw-text-[#8D8D8D] tw-text-6xl tw-font-bold">Sorry!</p>
                <p class="tw-flex tw-justify-right tw-text-[#8D8D8D] tw-text-4xl tw-font-bold">You are not authorized to
                    access.</p>
                <div class="tw-text-[#8D8D8D] tw-text-xl tw-flex tw-justify-right my-3">
                    Click to go
                    <NuxtLink to="/"><span class="tw-px-2 tw-underline">Home</span></NuxtLink>
                </div>
            </v-col>
            <v-col cols="1"></v-col>
        </v-row>
    </div>
</template>
 
<style></style>