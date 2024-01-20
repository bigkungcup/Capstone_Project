<script setup>
import bgimage from '~/image/login.png'
definePageMeta({
    layout: false,
});
</script>
 
<template>
    <!-- <div class="tw-max-h-[20rem]">  -->
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
            <div class="tw-flex tw-justify-center tw-my-10">
                <p class="web-text-header"> Login </p>
            </div>
            <div class="tw-mx-10 tw-my-10">
                <v-text-field label="Email" variant="solo" ></v-text-field>
                <v-text-field label="Password" variant="solo" style="height: 40px;"></v-text-field>
            </div>
            <v-row>
                <v-col cols="7" class="">
                    <!-- <v-checkbox label="Remember me" hide-details="auto"> </v-checkbox> -->
                </v-col>
                <v-col cols="5" >
                    <NuxtLink to="/register/" class="web-text-sub"> Register | </NuxtLink>
                    <span class="web-text-sub"> Forgot password ?</span>
                </v-col>
            </v-row>
            <div class="tw-flex tw-justify-center tw-my-3">
                <v-btn color="#1D419F" rounded variant="flat" size="large">Login</v-btn>
            </div>
            <NuxtLink to="/" class="web-text-sub tw-flex tw-justify-center tw-my-3">
            Continue as guest
            </NuxtLink>
        </v-col>
    </v-row>

    <!-- </div> -->
</template>
 
<style></style>