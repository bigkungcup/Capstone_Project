<script setup>
import { ref } from "vue";
import { useUsers } from "../../stores/user";
import { useRoute, useRouter } from "vue-router";
import updateUserSuccessPopup from "../../components/users/popups/updateUserSuccessPopup.vue";
import DeleteUserConfirmPopup from "~/components/users/popups/deleteUserConfirmPopup.vue";
import DeleteUserSuccessPopup from "~/components/users/popups/deleteUserSuccessPopup.vue";
import LoadingPopup from "~/components/popups/loadingPopup.vue";

const user = useUsers();
const route = useRoute();
const router = useRouter();
const selectedImage = ref(null);
const validateSize = ref(false);
const validatePassword = ref(false);
const validEmail =
  /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
const roleToken = ref(localStorage.getItem('role'));
const popupStatus = ref('');
const userConfirmPopup = ref(false);
const visible = ref(false);

function showValidateSize() {
  validateSize.value = true;
}

function handleFileChange(event) {
  if (user.editUserFile[0]) {
    selectedImage.value = URL.createObjectURL(user.editUserFile[0])
  }
}

const rules = {
  required: (value) => !!value || "Field is required",
  email: (value) => value.match(validEmail) || "Please enter a valid email address",
  limitedPass: (value) => value.length <= 16 && value.length >= 8 || "Password must be 8-16 characters",
  limited: (value) => value.length <= 255 || "Max 255 characters",
  size: (value) => !!value || value[0].size <= 50000000 || showValidateSize(),
};

function setSelectedImage() {
  selectedImage.value = user.editUser.file == null ? null : user.editUser.file;
  user.editUserFile = undefined;
}

function handleCheckPassword() {
  if (user.editUser.password !== "") {
    if (
      user.editUser.password.length < 8 ||
      user.editUser.password.length > 16
    ) {
      validatePassword.value = true;
    }else{
      validatePassword.value = false;
    }
  }else{
    validatePassword.value = false;
  }
}

onBeforeMount(() => {
  if (roleToken.value == 'ADMIN') {
  user.updateFailed = false;
  user.leavePopup = true;
  setSelectedImage();
}else{
  router.push(`/UnauthenPage/`)
}
});

if (roleToken.value == 'ADMIN') {
  await user.getUserDetail(route.params.id);
  if(user.userDetail.data.displayName == ''){
    router.push('/PageNotFound/')
  }
  user.setEditUser();
}

onBeforeRouteLeave(() => {
  if (roleToken.value == 'ADMIN') {
  const coverCheck =
    selectedImage.value == null
      ? selectedImage.value != user.userDetail.data.file
      : selectedImage.value != user.userDetail.data.file;
  if (
    user.editUser.displayName !== user.userDetail.data.displayName ||
    user.editUser.email !== user.userDetail.data.email ||
    user.editUser.password !== '' ||
    user.editUser.bio !== user.userDetail.data.bio ||
    user.editUser.role !== user.userDetail.data.role ||
    coverCheck
  ) {
    if (user.leavePopup) {
      const shouldShowPopup = confirm("Do you really want to leave?");
      if (shouldShowPopup) {
        return null;
      } else {
        next(false); // Prevent leaving the page
      }
    }
  }
}
});

</script>

<template>
  <div class="tw-pt-1 tw-pb-5 tw-drop-shadow-lg tw-space-y-1" v-if="roleToken == 'ADMIN' && user.userDetail.data.displayName != ''">
    <div class="tw-mx-36 tw-mt-5">
      <v-btn
        prepend-icon="mdi mdi-chevron-left"
        variant="text"
        width="8%"
        color="#082250"
        @click="$router.go(-1)"
      >
        <p class="tw-font-bold">Back</p>
      </v-btn>
    </div>

    <div class="tw-flex tw-justify-center tw-min-h-[32rem] tw-pb-2">
      <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
        <v-row no-gutters>
          <v-col cols="3" align="center" class="tw-my-3">
            <div>
            <div
                class="tw-m-5"
                @click="$refs.fileInput.click()"
              >
                <v-img
                  src="/image/upload_photo.png"
                  class="tw-rounded-full"
                  v-show="user.editUser.file == null && user.editUserFile == undefined"
                  width="200"
                  height="200"
                  cover
                ></v-img>
                <v-img
                  :src="selectedImage"
                  class="tw-rounded-full"
                  v-show="user.editUser.file != null || user.editUserFile != null"
                  width="200"
                  height="200"
                  cover
                ></v-img>
              </div>
            <div>
              <v-btn @click="$refs.fileInput.click()"  class="d-flex justify-center">
                <p class="tw-font-bold tw-text-[#1D419F] tw-text-xs">
                  Select image
                </p>
                <span class="mdi mdi-chevron-right"></span>
              </v-btn>
              <v-responsive class="mx-auto my-2" max-width="200">
                <v-file-input
                  ref="fileInput"
                  v-model="user.editUserFile"
                  @change="handleFileChange()"
                  accept="image/*"
                  style="display: none"
                  :rules="[rules.size]"
                >
                </v-file-input>
                <p v-show="validateSize" class="validate-text">
                  Image size should be less than 50 MB!
                </p>
              </v-responsive>
            </div>
            <div v-show="user.editUser.file != null || user.editUserFile != null">
              <v-btn @click="user.editUser.file = null, user.editUserFile = null" class="d-flex justify-center px-12" >
                  <p class="tw-font-bold tw-text-[#1D419F] tw-text-xs">
                    cancel
                  </p>
              </v-btn>
            </div>
          </div>
          </v-col>

          <v-col cols="9">
            <div class="tw-mx-8 tw-my-6">
              <div class="web-text-detail">
                <v-card :text="`${user.updateFailedError}`" variant="tonal" class="my-5" color="red-lighten-1" v-show="user.updateFailed"></v-card>
                <div>
                  <p>Username</p>
                  <v-text-field
                    label="Username"
                    variant="solo"
                    :rules="[rules.required,rules.limited]"
                    v-model="user.editUser.displayName"
                    disabled
                  ></v-text-field>
                </div>
                <div>
                  <p>Email</p>
                  <v-text-field
                    label="Email"
                    variant="solo"
                    :rules="[rules.required,rules.limited,rules.email]"
                    v-model="user.editUser.email"
                    disabled
                  ></v-text-field>
                </div>
                <div>
                  <p>Password</p>
                  <v-text-field
                    :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
                    :type="visible ? 'text' : 'password'"
                    density="compact"
                    label="Password"
                    variant="solo"
                    @click:append-inner="visible = !visible"
                    :rules="[rules.limitedPass]"
                    @input="handleCheckPassword()"
                    v-model="user.editUser.password"
                  ></v-text-field>
                </div>
                <div>
                  <p>Bio</p>
                  <v-textarea
                    label="Bio"
                    variant="solo"
                    rows="3"
                    :rules="[rules.limited]"
                    v-model="user.editUser.bio"
                  ></v-textarea>
                </div>
                <div>
                </div>
              </div>
            </div>
          </v-col>
        </v-row>
      </v-card>
    </div>

    <div class="d-flex justify-space-between">
      <div class="justify-start tw-mx-[9rem]" v-if="roleToken == 'ADMIN'">
        <v-btn
        color="red"
        variant="flat"
        @click="userConfirmPopup = true,popupStatus = 'delete'"
        >Delete</v-btn>
      </div>
      <div class="justify-end tw-mx-[9rem] tw-space-x-4">
      <v-btn
        color="#1D419F"
        variant="outlined"
        @click="user.setEditUser(route.params.id).then(setSelectedImage()),handleCheckPassword()"
        >reset</v-btn
      >
      <v-btn
        color="#1D419F"
        variant="flat"
        @click="user.updateUser(route.params.id),popupStatus = 'update'"
        :disabled="
          user.editUser.displayName == '' ||
          user.editUser.email == '' ||
          !user.editUser.email.match(validEmail) ||
          user.editUser.displayName.length > 255 ||
          user.editUser.email.length > 255 ||
          (user.editUserFile == null ? false : user.editUserFile[0].size > 50000000) ||
          validatePassword 
          // user.editUserFile == null
          //   ? false
          //   : user.editUserFile[0].size > 64000000
        "
        >submit</v-btn
      >
      </div>
    </div>
    <updateUserSuccessPopup
      :dialog="user.successfulPopup == 'show'"
      v-if="popupStatus == 'update'"
      @close="user.closeSuccessfulPopup()"/>
  </div>
  <div v-if="popupStatus == 'delete'">
      <DeleteUserConfirmPopup
        class="delete-popup"
        :dialog="userConfirmPopup"
        @toggle="userConfirmPopup = !userConfirmPopup,popupStatus == ''"
        @delete="user.deleteUser(route.params.id)"
      />
      <DeleteUserSuccessPopup
        class="delete-popup"
        :dialog="user.successfulPopup == 'show'"
        @close="user.successfulPopup = 'hide',popupStatus == '',router.push(`/user/`)"
      />
    </div>
    <div v-if="user.successfulPopup == 'load'">
    <LoadingPopup />
  </div>
</template>

<style>
.delete-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
