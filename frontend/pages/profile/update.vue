<script setup>
import { ref } from "vue";
import { useLogin } from "../../stores/login";
import updateProfileSuccessPopup from "../../components/profiles/popups/updateProfileSuccessPopup.vue";
import LoadingPopup from "~/components/popups/loadingPopup.vue";

const login = useLogin();
const route = useRoute();
const selectedImage = ref(null);
const validateSize = ref(false);
const validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

function showValidateSize() {
    validateSize.value = true;
}

function handleFileChange(event) {
    if (login.editProfileFile[0]) {
      selectedImage.value = URL.createObjectURL(login.editProfileFile[0])
    }
}

const rules = {
    required: (value) => !!value || "Field is required",
    email: (value) => value.match(validEmail) || "Please enter a valid email address",
    limited: (value) => value.length <= 255 || "Max 255 characters",
    size: (value) => !!value || value[0].size <= 50000000 || showValidateSize(),
};

function setSelectedImage() {
    selectedImage.value = login.editProfile.file == null ? null : login.editProfile.file;
    login.editProfileFile = undefined;
}

onBeforeMount(() => {
    login.leavePopup = true;
    setSelectedImage();
});

onBeforeRouteLeave(() => {
    const coverCheck =
        selectedImage.value == null
            ? selectedImage.value != login.profile.file
            : selectedImage.value != login.profile.file;
    if (
        login.editProfile.displayName !== login.profile.displayName ||
        login.editProfile.email !== login.profile.email ||
        login.editProfile.bio !== login.profile.bio ||
        coverCheck
    ) {
        if (login.leavePopup) {
            const shouldShowPopup = confirm("Do you really want to leave?");
            if (shouldShowPopup) {
                return null;
            } else {
                next(false); // Prevent leaving the page
            }
        }
    }
});

await login.getProfile();
login.setEditProfile();

</script>
 
<template>
    <div class="tw-pt-1 tw-pb-10 tw-drop-shadow-lg tw-space-y-1">
        <div class="tw-mx-36 tw-mt-5">
            <v-btn prepend-icon="mdi mdi-chevron-left" variant="text" width="8%" color="#082250" @click="$router.go(-1)">
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
                  v-show="login.editProfile.file == null && login.editProfileFile == undefined"
                  width="200"
                  height="200"
                  cover
                ></v-img>
                <v-img
                  :src="selectedImage"
                  class="tw-rounded-full"
                  v-show="login.editProfile.file != null || login.editProfileFile != null"
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
                  v-model="login.editProfileFile"
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
            <div v-show="login.editProfile.file != null || login.editProfileFile != null">
              <v-btn @click="login.editProfile.file = null, login.editProfileFile = null" class="d-flex justify-center px-12" >
                  <p class="tw-font-bold tw-text-[#1D419F] tw-text-xs">
                    cancel
                  </p>
              </v-btn>
            </div>
          </div>
          </v-col>
                    <v-col cols="9">
                        <div class="tw-mx-8 tw-my-6">
                            <div class="">
                                <v-card :text="`${login.updateFailedError}`" variant="tonal" class="my-5" color="red-lighten-1" v-show="login.updateFailed"></v-card>
                                <p class="web-text-sub">Username</p>
                                <v-text-field 
                                label="Username" 
                                variant="solo" 
                                :rules="[rules.required,rules.limited]"
                                v-model="login.editProfile.displayName" disabled></v-text-field>
                                <p class="web-text-sub">Email</p>
                                <v-text-field 
                                label="Email" 
                                variant="solo" 
                                :rules="[rules.required,rules.limited,rules.email]"
                                v-model="login.editProfile.email" disabled></v-text-field>
                                <p class="web-text-sub">Bio</p>
                                <v-textarea label="Bio" 
                                variant="solo" rows="3" 
                                :rules="[rules.limited]"
                                v-model="login.editProfile.bio"></v-textarea>
                            </div>
                        </div>

                    </v-col>
                </v-row>
            </v-card>
        </div>

        <div class="d-flex justify-end tw-mx-[9rem] tw-space-x-4">
            <v-btn color="#1D419F" variant="outlined" @click="login.setEditProfile().then(setSelectedImage())">reset</v-btn>
            <v-btn color="#1D419F" variant="flat" @click="login.updateProfile()"
            :disabled="
            login.editProfile.displayName == '' ||
            login.editProfile.email == '' ||
            !login.editProfile.email.match(validEmail) ||
            login.editProfile.displayName.length > 255 ||
            (login.editProfileFile == null ? false : login.editProfileFile[0].size > 50000000) ||
            login.editProfile.email.length > 255">submit</v-btn>
        </div>
    </div>
    <updateProfileSuccessPopup
      :dialog="login.successfulPopup == 'show'"
      @close="login.closeSuccessfulPopup()"/>
    <div v-if="login.successfulPopup == 'load'">
    <LoadingPopup />
  </div>
</template>
 
<style></style>