<script setup>
import { useUsers } from "~/stores/user";
import { ref } from "vue";
// import bgimage from '~/image/login.png'

const user = useUsers();
const visible = ref(false);

definePageMeta({
  layout: false,
});
</script>

<template>
  <!-- <div class="tw-max-h-[20rem]">  -->
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
          <p class="web-text-header">Login Account</p>
        </div>
        <div class="tw-mx-10 tw-my-10">
          <v-text-field
            prepend-inner-icon="mdi-email-outline"
            density="compact"
            label="Email address"
            variant="solo"
            v-model="user.userLogin.email"
          ></v-text-field>
          <v-text-field
            :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
            :type="visible ? 'text' : 'password'"
            density="compact"
            label="Password"
            variant="solo"
            prepend-inner-icon="mdi-lock-outline"
            @click:append-inner="visible = !visible"
            style="height: 40px"
            v-model="user.userLogin.password"
          ></v-text-field>
          <div class="">
            <!-- <v-checkbox label="Remember me" hide-details="auto"> </v-checkbox> -->
          </div>
          <div class="d-flex justify-end tw-space-x-1 my-5">
            <NuxtLink to="/register/" class="login-text-sub">
              Register
            </NuxtLink>
            <span>|</span>
            <span class="login-text-sub">Forgot password ?</span>
          </div>
        </div>

        <div class="tw-flex tw-justify-center">
          <v-btn class="px-14" color="#1D419F" rounded variant="flat" size="large"
            >Login</v-btn
          >
        </div>
        <NuxtLink to="/" class="login-text-sub tw-flex tw-justify-center my-3">
          Continue as guest
        </NuxtLink>
      </v-col>
    </v-row>
  </v-row>

  <!-- </div> -->
</template>

<style></style>
