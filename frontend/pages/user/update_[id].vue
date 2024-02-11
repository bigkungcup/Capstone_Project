<script setup>
import { ref } from "vue";
import { useUsers } from "../../stores/user";
import updateUserSuccessPopup from "../../components/users/popups/updateUserSuccessPopup.vue";

const user = useUsers();
const route = useRoute();
const selectedImage = ref();
const validateSize = ref(false);
const validatePassword = ref(false);
const validEmail =
  /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

function showValidateSize() {
  validateSize.value = true;
}

function handleFileChange(event) {
  if (user.editUserFile[0]) {
    // Convert the selected image to a data URL
    const reader = new FileReader();
    reader.onload = (e) => {
      this.selectedImage = e.target.result;
    };
    reader.readAsDataURL(user.editUserFile[0]);
  }
}

const rules = {
  required: (value) => !!value || "Field is required",
  email: (value) => value.match(validEmail) || "Please enter a valid email address",
  limitedPass: (value) => value.length <= 16 && value.length >= 8 || "Password must be 8-16 characters",
  limited: (value) => value.length <= 255 || "Max 255 characters",
  size: (value) => !!value || value[0].size <= 64000000 || showValidateSize(),
};

function setSelectedImage() {
  // book.editBook.file == null ? null : `../../_nuxt/@fs/${book.editBook.file}`;
  selectedImage.value = user.editUser.file == null ? null : `../../ej2/_nuxt/@fs/${user.editUser.file}`;
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
  user.leavePopup = true;
  setSelectedImage();
});

onBeforeRouteLeave(() => {
  const coverCheck =
    selectedImage.value == null
      ? selectedImage.value != user.userDetail.data.file
      : selectedImage.value != `../../_nuxt/@fs/${user.userDetail.data.file}`;
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
});

await user.getUserDetail(route.params.id);
user.setEditUser();
</script>

<template>
  <div class="tw-pt-1 tw-pb-5 tw-drop-shadow-lg tw-space-y-1">
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
            <!-- <v-img
              src="/image/cat.jpg"
              width="200"
              height="200"
              class="tw-rounded-full tw-m-5"
              cover
            /> -->
            <div>
            <div
                class="tw-m-5"
                @click="$refs.fileInput.click()"
              >
                <v-img
                  src="/image/upload_book_cover.png"
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
                  Image size should be less than 64 MB!
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
            <div class="web-text-detail my-5">
                <v-radio-group inline v-model="user.editUser.role" class="d-flex justify-center" hide-details="auto">
                  <v-radio label="User" value="USER"></v-radio>
                  <v-radio label="Admin" value="ADMIN"></v-radio>
                </v-radio-group>
            </div>
          </v-col>

          <v-col cols="9">
            <div class="tw-mx-8 tw-my-6">
              <div class="web-text-detail">
                <div>
                  <p>Username</p>
                  <v-text-field
                    label="Username"
                    variant="solo"
                    
                    :rules="[rules.required,rules.limited]"
                    v-model="user.editUser.displayName"
                  ></v-text-field>
                </div>
                <div>
                  <p>Email</p>
                  <v-text-field
                    label="Email"
                    variant="solo"
                    :rules="[rules.required,rules.limited,rules.email]"
                    v-model="user.editUser.email"
                  ></v-text-field>
                </div>
                <div>
                  <p>Password</p>
                  <v-text-field
                    label="Password"
                    variant="solo"
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

    <div class="d-flex justify-end tw-mx-[9rem] tw-space-x-4">
      <v-btn
        color="#1D419F"
        variant="outlined"
        @click="user.setEditUser(route.params.id).then(setSelectedImage()),handleCheckPassword()"
        >reset</v-btn
      >
      <v-btn
        color="#1D419F"
        variant="flat"
        @click="user.updateUser(route.params.id)"
        :disabled="
          user.editUser.displayName == '' ||
          user.editUser.email == '' ||
          !user.editUser.email.match(validEmail) ||
          user.editUser.displayName.length > 255 ||
          user.editUser.email.length > 255 ||
          validatePassword 
          // user.editUserFile == null
          //   ? false
          //   : user.editUserFile[0].size > 64000000
        "
        >submit</v-btn
      >
    </div>
    <updateUserSuccessPopup
      :dialog="user.successfulPopup"
      @close="user.closeSuccessfulPopup()"/>
  </div>
</template>

<style></style>
