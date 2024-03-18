<script setup>
import Bookmarks from "~/components/profiles/bookmark.vue";
// import Reviews from '~/components/profiles/reviews.vue';
import { useUsers } from "~/stores/user";
import { useRoute, useRouter } from "vue-router";
import { mergeProps } from "vue";
import deleteUserConfirmPopup from "~/components/users/popups/deleteUserConfirmPopup.vue";
import deleteUserSuccessPopup from "~/components/users/popups/deleteUserSuccessPopup.vue";

const user = useUsers();
const router = useRouter();
const route = useRoute();
const roleToken = ref(localStorage.getItem('role'));

function toggleUserConfirmPopup() {
  user.confirmPopup = !user.confirmPopup;
}

function closeUserSuccessfulPopup() {
  user.successfulPopup = !user.successfulPopup;
  window.history.back();
}

function bookCoverPath(filePath) {
  return (filePath = `../../_nuxt/@fs/${filePath}`);
}

onBeforeMount(async () => {
  if (roleToken.value == 'ADMIN') {
  await user.getUserDetail(route.params.id);
}else{
  router.push(`/UnauthenPage/`)
}
});
</script>

<template>
  <div class="tw-bg-[#D9D9D9] tw-h-full" v-show="roleToken == 'ADMIN'">
    <div class="tw-flex tw-place-content-center">
      <div class="tw-w-[70rem] tw-max-h-[16rem]">
        <v-img src="/image/profile_banner.jpg" v-show="user.userDetail.data.file == null" cover></v-img>
        <v-img
          class="tw-blur-[2px]"
          v-show="user.userDetail.data.file != null"  
          :src="user.userDetail.data.file"
          cover
        ></v-img>
        <!-- <v-img src="/image/cat.jpg" width="120" height="120"
                    class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-5" cover /> -->
      </div>
    </div>

    <div class="tw-flex tw-place-content-center">
      <div class="tw-bg-white tw-w-[70rem] tw-h-[9rem]">
        <v-row class="tw-py-2">
          <v-col cols="2">
            <v-img
              src="/image/guest_icon.png"
              v-show="user.userDetail.data.file == null"
              width="140"
              height="140"
              class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-6"
              cover
            />
            <v-img
              class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-6"
              :src="user.userDetail.data.file"
              v-show="user.userDetail.data.file != null"
              height="140"
              width="140"
              cover
            ></v-img>
          </v-col>

          <v-col cols="4">
            <div class="tw-space-y-1">
              <p class="web-text-header">
                {{ user.userDetail.data.displayName }}
              </p>
              <p class="web-text-sub">{{ user.userDetail.data.email }}</p>
              <p class="web-text-sub tw-py-5">{{ user.userDetail.data.bio }}</p>
            </div>
          </v-col>

          <v-col cols="5">
            <div class="py-1">
              <p class="web-text-sub tw-flex tw-place-content-end">
                {{ user.userDetail.data.follows }} Following
                {{ user.userDetail.data.followers }} Followers
              </p>
            </div>
          </v-col>

          <v-col cols="1">
            <!-- <v-btn color="#1D419F" variant="outlined" rounded="lg" elevation="2" :to="`/profile/update_${user.userDetail.data.userId}/`">Edit profile</v-btn> -->
            <span class="text-center web-text-detail">
              <v-menu>
                <template v-slot:activator="{ props: menu }">
                  <v-tooltip location="top">
                    <template v-slot:activator="{ props: tooltip }">
                      <v-icon
                        icon="mdi mdi-dots-vertical-circle-outline"
                        size="x-large"
                        v-bind="mergeProps(menu, tooltip)"
                      ></v-icon>
                    </template>
                    <span>More</span>
                  </v-tooltip>
                </template>
                <v-list>
                  <v-list-item :to="`/user/update_${route.params.id}`+'/'">
                    <v-list-item-title class="web-text-detail tw-space-x-2"
                      ><v-icon icon="mdi mdi-pencil-outline"></v-icon
                      ><span>Edit this user</span></v-list-item-title
                    >
                  </v-list-item>
                  <v-list-item
                    class="hover:tw-bg-zinc-300/20 tw-cursor-pointer"
                  >
                    <v-list-item-title class="web-text-detail">
                      <v-list-item-title
                        class="web-text-detail tw-space-x-2"
                        @click="toggleUserConfirmPopup()"
                        ><v-icon icon="mdi mdi-trash-can-outline"></v-icon
                        ><span>Delete this user</span></v-list-item-title
                      >
                    </v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </span>
          </v-col>
        </v-row>
      </div>
    </div>

    <!----------------------- new section -------------------------->
    <div class="tw-flex tw-place-content-center tw-my-6">
      <div class="tw-bg-white tw-w-[70rem] tw-h-full">
        <div class="tw-border-y-4">
          <v-row class="tw-py-3">
            <v-col cols="2">
              <div class="tw-flex tw-justify-center web-text-sub-pf">
                Bookmarks
              </div>
            </v-col>
            <v-col cols="2">
              <div class="tw-flex tw-justify-center web-text-sub-pf">
                My reviews
              </div>
            </v-col>
            <v-col cols="2">
              <div class="tw-flex tw-justify-center web-text-sub-pf">
                Followings
              </div>
            </v-col>
            <v-col cols="2">
              <div class="tw-flex tw-justify-center web-text-sub-pf">
                Followers
              </div>
            </v-col>
          </v-row>
        </div>

        <!-------- insert component here ---------->
        <Bookmarks />
        <!-- <Reviews /> -->
        <deleteUserConfirmPopup
          class="delete-popup"
          :dialog="user.confirmPopup"
          @toggle="toggleUserConfirmPopup()"
          @delete="user.deleteUser(user.userDetail.data.userId)"
        />
        <deleteUserSuccessPopup
          class="delete-popup"
          :dialog="user.successfulPopup"
          @close="closeUserSuccessfulPopup()"
        />
      </div>
    </div>
  </div>
</template>

<style scoped></style>
