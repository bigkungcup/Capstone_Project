<script setup>
import { useUsers } from "~/stores/user";
import { ref } from "vue";
import RegisterFailPopup from "~/components/users/popups/registerFailPopup.vue";
// import bgimage from "~/image/login.png";

const user = useUsers();
const visible = ref(false);
const confirmVisible = ref(false);
const password = ref();
const confirmPassword = ref();
const checkPassword = ref(true);
const validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

function handleCheckPassword() {
  if (password.value === confirmPassword.value) {
    checkPassword.value = true;
    user.newUser.password = password.value;
  } else {
    checkPassword.value = false;
  }
}

function clearConfirmPassword() {
  confirmPassword.value = '';
}

const rules = {
  required: (value) => !!value || "Field is required",
  email: (value) => value.match(validEmail) || "Please enter a valid email address",
  same: () => password.value === confirmPassword.value || 'Password did not match',
  limited: (value) => value.length <= 16 && value.length >= 8 || "Password must be 8-16 characters",
};

definePageMeta({
  layout: false,
});

onBeforeMount(() => {
  user.registerFailed = false;
  user.clearNewUser();
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
        <NuxtLink to="/" class="tw-flex tw-space-x-2 tw-m-5 tw-justify-center">
          <img src="/image/logo.png" style="height: 40px" />
          <p class="lily app-name-color tw-text-3xl tw-self-center">Bannarug</p>
        </NuxtLink>
        <div class="tw-flex tw-justify-center">
          <p class="web-text-header">Create Account</p>
        </div>

        <div class="tw-mx-10 tw-my-10 tw-space-y-1">
          <v-card :text="`${user.registerFailedError}`" variant="tonal" class="my-5" color="red-lighten-1" v-show="user.registerFailed"></v-card>
          <v-text-field
            prepend-inner-icon="mdi-account-outline"
            density="compact"
            label="Username*"
            variant="solo"
            :rules="[rules.required]"
            v-model="user.newUser.displayName"
          ></v-text-field>
          <v-text-field
            prepend-inner-icon="mdi-email-outline"
            density="compact"
            label="Email*"
            type="email"
            variant="solo"
            :rules="[rules.required,rules.email]"
            v-model="user.newUser.email"
          ></v-text-field>
          <v-text-field
            :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
            :type="visible ? 'text' : 'password'"
            density="compact"
            label="Password*"
            variant="solo"
            prepend-inner-icon="mdi-lock-outline"
            :rules="[rules.required,rules.limited]"
            v-model="password"
            @change="clearConfirmPassword()"
            @click:append-inner="visible = !visible"
          ></v-text-field>
          <v-text-field
            :append-inner-icon="confirmVisible ? 'mdi-eye-off' : 'mdi-eye'"
            :type="confirmVisible ? 'text' : 'password'"
            density="compact"
            label="Confirm Password*"
            variant="solo"
            prepend-inner-icon="mdi-lock-outline"
            :rules="[rules.required,rules.same]"
            v-model="confirmPassword"
            @input="handleCheckPassword()"
            @click:append-inner="confirmVisible = !confirmVisible"
          ></v-text-field>
        </div>

        <div class="tw-flex tw-justify-center">
          <v-btn
            class="px-14"
            color="#1D419F"
            rounded
            variant="flat"
            size="large"
            :disabled="
              user.newUser.displayName == '' ||
              user.newUser.email == '' ||
              !user.newUser.email.match(validEmail) ||
              user.newUser.displayName.length > 255 ||
              user.newUser.email.length > 255 ||
              user.newUser.password == '' ||
              password.length < 8 || password.length > 16 ||
              password !== confirmPassword
            "
            @click="user.registerUser()"
            >Register</v-btn
          >
        </div>
        <div class="login-text-sub tw-flex tw-justify-center my-3">
          Already have an account?
          <NuxtLink to="/login/"
            ><span class="tw-px-2 tw-underline">Log in</span></NuxtLink
          >
        </div>
      </v-col>
    </v-row>
    <RegisterFailPopup 
    :dialog="user.failPopup"
    @close="user.toggleUserFailPopup()"/>
  </v-row>
</template>

<style scoped></style>
