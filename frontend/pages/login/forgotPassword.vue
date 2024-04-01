<script setup>
import { useLogin } from "~/stores/login";
import { ref } from "vue";
import forgotPasswordSuccessPopup from "~/components/users/popups/forgotPasswordSuccessPopup.vue";

definePageMeta({
  layout: false,
});

const login = useLogin();
const validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

const rules = {
  required: (value) => !!value || "Field is required",
  email: (value) => value.match(validEmail) || "Please enter a valid email address",
};

onBeforeMount(() => {
  login.forgetEmail = '';
  login.successfulPopup = false;
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
    </v-col>

    <v-col cols="6" class="">
    <v-row align="center" justify="center" no-gutters class="tw-min-h-screen tw-max-h-screen">
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
        <div class="web-text-detail tw-mx-4">
          <p>*Enter your email address and we’ll send you new password to reset your password.</p>
        </div>
        <div class="tw-mx-10 tw-my-8">
          <v-text-field
            prepend-inner-icon="mdi-email-outline"
            density="compact"
            label="Enter your email"
            variant="solo"
            v-model="login.forgetEmail"
            :rules="[rules.required,rules.email]"
          ></v-text-field>
        </div>

        <div class="tw-flex tw-justify-center">
          <v-btn class="px-14" color="#1D419F" rounded variant="flat" size="large" 
          :disabled="login.forgetEmail == '' || !login.forgetEmail.match(validEmail)"
          @click="login.forgetPassword()">
            send
          </v-btn>
        </div>
        <NuxtLink to="/login/" class="login-text-sub tw-flex tw-justify-center my-3">
          <span>Back to login</span>
        </NuxtLink>
      </v-col>
    </v-row></v-col>
    <forgotPasswordSuccessPopup
    :dialog="login.successfulPopup"
    @close="login.successfulPopup = false"/>
  </v-row>
</template>
 
<style>

</style>