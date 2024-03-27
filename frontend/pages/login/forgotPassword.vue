<script setup>
definePageMeta({
  layout: false,
});

</script>
 
<template>
  <v-row no-gutters>
    <v-col cols="6" class="tw-bg-[#3157BB] tw-max-h-screen">
      <v-img
        src="/image/login.png"
        :aspect-ratio="1"
        cover
        class="tw-opacity-40"
      ></v-img>
      <!-- <div :style="{ backgroundImage: `url(${bgimage})` }" class="tw-min-h-full tw-bg-cover tw-opacity-40" ></div> -->
      <!-- <v-img src="/image/login.png" height="65%" cover></v-img> -->
    </v-col>

    <v-row align="center" justify="center" no-gutters>
      <v-col cols="9">
        <NuxtLink to="/" class="d-flex tw-space-x-2 my-5 justify-center">
          <img src="/image/logo.png" style="height: 40px" />
          <p class="lily app-name-color tw-text-3xl align-self-center">
            Bannarug
          </p>
        </NuxtLink>
        <div class="d-flex justify-center my-5">
          <p class="web-text-header">Forgot Password</p>
        </div>
        <p class="web-text-detail tw-mx-4">*Enter your email address and we’ll send you new password to reset your password.</p>
        <div class="tw-mx-10 tw-my-8">
          <v-text-field
            prepend-inner-icon="mdi-email-outline"
            density="compact"
            label="Enter your email"
            variant="solo"
          ></v-text-field>

        </div>

        <div class="tw-flex tw-justify-center">
          <v-btn class="px-14" color="#1D419F" rounded variant="flat" size="large"
            >send</v-btn
          >
        </div>
        <NuxtLink to="/login/" class="login-text-sub tw-flex tw-justify-center my-3">
          <span>Back to login</span>
        </NuxtLink>
      </v-col>
    </v-row>
  </v-row>
</template>
 
<style>

</style>