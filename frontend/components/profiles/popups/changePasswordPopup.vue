<script setup>
defineEmits(["toggle","update"]);
defineProps({
    dialog: {
        type: Boolean,
        require: true,
    },
})

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
        <v-card style="height: 25rem; width: 50rem;" class="rounded-xl tw-place-self-center">
        <v-card-title style="height: 5rem; width: 50rem;">
          <p class="action-popUp-header tw-flex tw-justify-center tw-my-4">Change Password</p>
          <div class="popup-horizontal-line"></div>
        </v-card-title>
        <v-card-text class="py-8" style="height: 15rem; width: 50rem;">
                            <div class="tw-space-y-1">
                                <v-row class="tw-flex tw-items-center tw-space-x-6" no-gutters>
                                    <v-col cols="4" class="d-flex tw-place-content-end">
                                        <span class="web-text-sub ">Old password</span>
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
                                        <span class="web-text-sub">New password</span>
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
                                        @change="clearConfirmPassword()"
                                        @click:append-inner="newVisible = !newVisible"></v-text-field>
                                    </v-col>
                                </v-row>
                                <v-row no-gutters class="tw-flex tw-items-center tw-space-x-6">
                                    <v-col cols="4" class="d-flex tw-place-content-end">
                                        <span class="web-text-sub">Confirm new password</span>
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
          <v-btn class="tw-mx-2" style="height: 3rem; width: 7rem;" color="#727272" variant="tonal" size="large" rounded="xl" @click="$emit('toggle'),clearAll()">Cancle</v-btn>
          <v-btn class="tw-mx-4" 
          style="height: 3rem; width: 7rem;" 
          color="#1646C4" 
          variant="flat" 
          size="large" 
          rounded="xl" 
          :disabled="
          checkPassword ||
          newPassword.length < 8 || newPassword.length > 16"
          @click="$emit('update')">Submit</v-btn>
        </v-card-actions>
      </v-card>
    </div>
</template>
 
<style>

</style>