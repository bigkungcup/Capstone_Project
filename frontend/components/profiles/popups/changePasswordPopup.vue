<script setup>
import { useLogin } from '~/stores/login'

defineEmits(["toggle","update"]);
defineProps({
    dialog: {
        type: Boolean,
        require: true,
    },
})

const login = useLogin();
const newPassword = ref();
const confirmPassword = ref();
const oldPassword = ref();
const visible = ref(false);
const confirmVisible = ref(false);
const newVisible = ref(false);
const checkPassword = ref(true);

function handleCheckPassword() {
  if (newPassword.value === confirmPassword.value) {
    checkPassword.value = false;
    // user.newUser.password = password.value;
  } else {
    checkPassword.value = true;
  }
}

function clearConfirmPassword() {
  confirmPassword.value = '';
}

function clearAll() {
    oldPassword.value = '';
    newPassword.value = '';
    confirmPassword.value = '';
    checkPassword.value = true;
    visible.value = false;
    newVisible.value = false;
    confirmVisible.value = false;
}

const rules = {
  required: (value) => !!value || "Field is required",
  same: () => password.value === confirmPassword.value || 'Password did not match',
  limited: (value) => value.length <= 16 && value.length >= 8 || "Password must be 8-16 characters",
};

</script>
 
<template>
    <div v-show="dialog" class="tw-grid tw-h-100% tw-min-h-screen tw-w-screen tw-absolute tw-inset-0 tw--top-[5.5rem] tw-bg-black/50">

      <!-- Change password popup -->
        <v-card style="min-height: 25rem; width: 50rem;" class="rounded-xl tw-place-self-center" v-show="!login.updateFailed && !login.successfulPopup">
        <v-card-title class="tw-bg-[#1D419F]" style="height: 5rem; width: 50rem;">
          <p class="action-popUp-header !tw-text-white tw-flex tw-justify-center tw-my-4 ">Change Password</p>
        </v-card-title>
        <v-card-text class="py-8 " style="height: 15rem; width: 50rem;">
                            <div class="tw-space-y-1">     
                                <v-row class="tw-flex tw-items-center tw-space-x-6" no-gutters>
                                    <v-col cols="4" class="d-flex tw-place-content-end">
                                        <span class="web-text-detail">Old password</span>
                                    </v-col>
                                    <v-col cols="7">
                                        <v-text-field 
                                        label="Enter old password"             
                                        :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
                                        :type="visible ? 'text' : 'password'" 
                                        density="compact"
                                        variant="solo"
                                        v-model="oldPassword"
                                        :rules="[rules.required,rules.limited]"
                                        @click:append-inner="visible = !visible"></v-text-field>
                                    </v-col>
                                </v-row>
                                <v-row no-gutters class="tw-flex tw-items-center tw-space-x-6">
                                    <v-col cols="4" class="d-flex tw-place-content-end">
                                        <span class="web-text-detail">New password</span>
                                    </v-col>
                                    <v-col cols="7">
                                        <v-text-field 
                                        label="Enter new password" 
                                        :append-inner-icon="newVisible ? 'mdi-eye-off' : 'mdi-eye'"
                                        :type="newVisible ? 'text' : 'password'"
                                        density="compact"
                                        variant="solo"
                                        v-model="newPassword"
                                        :rules="[rules.required,rules.limited]"
                                        @input="clearConfirmPassword()"
                                        @click:append-inner="newVisible = !newVisible"></v-text-field>
                                    </v-col>
                                </v-row>
                                <v-row no-gutters class="tw-flex tw-items-center tw-space-x-6">
                                    <v-col cols="4" class="d-flex tw-place-content-end">
                                        <span class="web-text-detail">Confirm new password</span>
                                    </v-col>
                                    <v-col cols="7">
                                        <v-text-field 
                                        label="Enter confirm password" 
                                        :append-inner-icon="confirmVisible ? 'mdi-eye-off' : 'mdi-eye'"
                                        :type="confirmVisible ? 'text' : 'password'"
                                        density="compact"
                                        variant="solo"
                                        v-model="confirmPassword"
                                        :rules="[rules.required,rules.limited]"
                                        @input="handleCheckPassword()"
                                        @click:append-inner="confirmVisible = !confirmVisible"></v-text-field>
                                    </v-col>
                                </v-row>
                            </div>
        </v-card-text>
        <v-card-actions style="height: 4em; width: 50rem;">
          <v-spacer></v-spacer>
          <v-btn class="tw-mx-2" style="height: 3rem; width: 7rem;" color="#727272" variant="tonal" size="large" rounded="xl" @click="$emit('toggle'),clearAll()">Cancel</v-btn>
          <v-btn class="tw-mx-4" 
          style="height: 3rem; width: 7rem;" 
          color="#1646C4" 
          variant="flat" 
          size="large" 
          rounded="xl" 
          :disabled="
          checkPassword ||
          newPassword.length < 8 || newPassword.length > 16"
          @click="login.changePassword(oldPassword,newPassword)">Submit</v-btn>
        </v-card-actions>
      </v-card>

      <!-- Successful popup -->
      <v-card style="height: 20rem; width: 40rem;" class="rounded-xl tw-place-self-center" v-show="login.successfulPopup">
          <div class="tw-flex tw-justify-center tw-my-3">
          <v-icon icon="mdi mdi-check-circle-outline" color="#1D419F" style="font-size: 8rem;" ></v-icon>
        </div>
          <p class="confirm-popUp-header tw-flex tw-justify-center tw-my-3">Success !</p>
        <v-card-text class="d-flex align-content-center justify-center flex-wrap" style="height: 2rem; width: 40rem;">
            <div class="confirm-popUp-detail">Your password has been updated successfully.</div></v-card-text>
        <v-card-actions style="height: 5rem; width: 40rem;"  class="tw-flex tw-justify-center">
          <v-btn class=" tw-mx-2" style="height: 3rem; width: 7rem; color: white;" color="#1D419F" variant="flat" size="large" rounded="xl" @click="$emit('toggle'),clearAll(),login.successfulPopup = false">OK</v-btn>
        </v-card-actions>
      </v-card>

      <!-- Failed popup -->
      <v-card style="height: 20rem; width: 40rem;" class="rounded-xl tw-place-self-center" v-show="login.updateFailed">
            <div class="tw-flex tw-justify-center">
                <v-icon color="#BB0000" style="font-size: 8rem;">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                        <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                        <g id="SVGRepo_iconCarrier">
                            <path
                                d="M16 8L8 16M8.00001 8L16 16M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z"
                                stroke="#BB0000" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                        </g>
                    </svg>
                </v-icon>
            </div>
            <p class="warning-popUp-header tw-flex tw-justify-center tw-my-3">Warning !</p>
            <v-card-text class="d-flex align-content-center justify-center flex-wrap tw-text-center tw-my-1"
                style="height: 3rem; width: 40rem;">
                <div class="warning-popUp-detail tw-px-8">Old password is incorrect</div>
            </v-card-text>
            <v-card-actions style="height: 5rem; width: 40rem;" class="tw-flex tw-justify-center">
            <v-btn class=" tw-mx-2" style="height: 3rem; width: 7rem; color: white;" color="#BB0000" variant="flat"
                size="large" rounded="xl" @click="login.updateFailed = false">Got it!</v-btn>
        </v-card-actions>
    </v-card>
    </div>
</template>
 
<style>

</style>