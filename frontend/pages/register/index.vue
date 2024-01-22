<script setup>
import bgimage from '~/image/login.png'
definePageMeta({
    layout: false,
});
</script>
 
<template>
    <v-row no-gutters>
        <v-col cols="6" class="tw-bg-[#3157BB] tw-max-h-screen" >
                <v-img src="/image/login.png"  :aspect-ratio="1" cover class="tw-opacity-40"></v-img>
            <!-- <div :style="{ backgroundImage: `url(${bgimage})` }" class="tw-min-h-full tw-bg-cover tw-opacity-40" ></div> -->
            <!-- <v-img src="/image/login.png" height="65%" cover></v-img> -->
        </v-col>
        <v-col cols="6">
           <NuxtLink to="/" class="tw-flex tw-space-x-2 tw-m-5 tw-justify-center">
                <img src="/image/logo.png" style="height: 40px;" />
                <p class="lily app-name-color tw-text-3xl tw-self-center">Bannarug</p>
            </NuxtLink>
            <div class="tw-flex tw-justify-center">
                <p class="web-text-header"> Register </p>
            </div>

            <div class="tw-mx-10 tw-my-1 tw-space-y-1">
                <p class="web-text-sub">Email*</p>
                <v-text-field label="Enter your email" variant="solo" style="height: 60px;"></v-text-field>
                <p class="web-text-sub">Username*</p>
                <v-text-field label="Enter your username" variant="solo" style="height: 60px;"></v-text-field>
                <p class="web-text-sub">Password*</p>
                <v-text-field label="Enter your password" variant="solo" style="height: 60px;"></v-text-field>
                <p class="web-text-sub">Confirm password*</p>
                <v-text-field label="Enter confirm password" variant="solo" style="height: 70px;"></v-text-field>
            </div>

            <div class="tw-flex tw-justify-center tw-my-2">
                <v-btn color="#1D419F" rounded variant="flat" size="large">Register</v-btn>
            </div>
            <div class="web-text-sub tw-flex tw-justify-center ">
            Already have an account? 
            <NuxtLink to="/login/"><span class="tw-px-2 tw-underline">Log in</span></NuxtLink>
            </div>
        </v-col>
    </v-row>
</template>
 
<style scoped>
</style>