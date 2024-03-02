<script setup>
import Bookmarks from '~/components/profiles/bookmark.vue';
import Reviews from '~/components/profiles/reviews.vue';
import changePasswordPopup from "~/components/profiles/popups/changePasswordPopup.vue";
// import Reviews from '~/components/profiles/reviews.vue';
import { useLogin } from '~/stores/login'
import { useBooks } from '~/stores/book'
import { mergeProps } from "vue";

const login = useLogin();
const book = useBooks();
const changePassword = ref(false);
const profileSection = ref('bookmark');
const result = ref(0);

function handleChangePassword() {
    changePassword.value = !changePassword.value;
}

function bookCoverPath(filePath) {
  return (filePath = `../../ej2/_nuxt/@fs/${filePath}`);
}

function setResult() {
  if(profileSection.value == 'bookmark'){
    result.value = book.bookmarkList.data.totalElements ? book.bookmarkList.data.totalElements : 0
  }
  // }else if(profileSection.value == 'reviews'){

  // }
}

function selectBookmark() {
  book.getBookmarkList();
  setResult();
}

onBeforeMount(async () => { 
  await login.getProfile();
  await book.getBookmarkList();
});


</script>
 
<template>
    <div class="tw-bg-[#D9D9D9] tw-h-full">
        <div class="tw-flex tw-place-content-center ">
            <div class="tw-w-[70rem] tw-max-h-[16rem]">
                <v-img src="/image/profile_banner.jpg" v-show="login.profile.file == null" cover></v-img>
        <v-img
          class="tw-blur-[2px]"
          v-show="login.profile.file != null"  
          :src="login.profile.file"
          cover
        ></v-img>
                <!-- <v-img src="/image/cat.jpg" width="120" height="120"
                    class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-5" cover /> -->
            </div>
        </div>


        <div class="tw-flex tw-place-content-center ">
            <div class="tw-bg-white tw-w-[70rem] tw-h-[9rem]">
                <v-row class="tw-py-2" >
                    <v-col cols="2" >
                        <v-img
              src="/image/guest_icon.png"
              v-show="login.profile.file == null"
              width="140"
              height="140"
              class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-6"
              cover
            />
            <v-img
              class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-6"
              :src="login.profile.file"
              v-show="login.profile.file != null"
              height="140"
              width="140"
              cover
            ></v-img>
                    </v-col>

                    <v-col cols="4">
                        <div>
                            <p class="web-text-header">{{ login.profile.displayName }}</p>
                            <p class="web-text-sub">{{ login.profile.email }}</p>
                            <p class="web-text-sub tw-py-6">{{ login.profile.bio }}</p>
                        </div>
                    </v-col>

                    <v-col cols="5">
                        <div class="py-1">
                            <p class="web-text-sub tw-flex tw-place-content-end">
                                {{ login.profile.follows }} Following 
                                {{ login.profile.followers }} Followers
                            </p>
                        </div>
                    </v-col>

                    <v-col cols="1">
                        <!-- <v-btn color="#1D419F" variant="outlined" rounded="lg" elevation="2" :to="`/profile/update_${login.profile.userId}/`">Edit profile</v-btn> -->
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
                  <v-list-item :to="`/profile/update/`">
                    <v-list-item-title class="web-text-detail tw-space-x-2"
                      ><v-icon icon="mdi mdi-pencil-outline"></v-icon
                      ><span>Edit profile</span></v-list-item-title
                    >
                  </v-list-item>
                  <v-list-item
                    class="hover:tw-bg-zinc-300/20 tw-cursor-pointer"
                  >
                    <v-list-item-title class="web-text-detail">
                      <v-list-item-title
                        class="web-text-detail tw-space-x-2"
                        @click="handleChangePassword()"
                        ><v-icon icon="mdi mdi-key"></v-icon
                        ><span>Change password</span></v-list-item-title
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
                    <v-row class="">
                        <v-col cols="3" class="tw-grid tw-content-center ">   
                            <div class="tw-flex tw-justify-center web-text-sub-pf" v-if="profileSection != 'bookmark'" @click="profileSection = 'bookmark', selectBookmark()"> Bookmarks </div>
                            <div class="web-text-sub-pf-white tw-bg-[#082266] tw-py-3" v-if="profileSection == 'bookmark'"> 
                              <p class="tw-flex tw-justify-center">Bookmarks</p> 
                              <p class="tw-flex tw-justify-center">({{ result }})</p> 
                            </div>
                        </v-col>
                        <v-col cols="3" class="tw-grid tw-content-center">
                            <div class="tw-flex tw-justify-center web-text-sub-pf" v-if="profileSection != 'review'" @click="profileSection = 'review'"> My reviews </div>
                            <div class="web-text-sub-pf-white tw-bg-[#082266] tw-py-3" v-if="profileSection == 'review'"> 
                              <p class="tw-flex tw-justify-center"> My reviews </p> 
                              <p class="tw-flex tw-justify-center">({{ result }})</p> 
                            </div>
                        </v-col>
                        <v-col cols="3" class="tw-grid tw-content-center">
                            <div class="tw-flex tw-justify-center web-text-sub-pf" v-if="profileSection != 'following'" @click="profileSection = 'following'"> Followings </div>
                            <div class="web-text-sub-pf-white tw-bg-[#082266] tw-py-3" v-if="profileSection == 'following'"> 
                              <p class="tw-flex tw-justify-center"> Followings </p> 
                              <p class="tw-flex tw-justify-center">({{ result }})</p> 
                            </div>
                        </v-col>
                        <v-col cols="3" class="tw-grid tw-content-center">
                            <div class="tw-flex tw-justify-center web-text-sub-pf" v-if="profileSection != 'follower'" @click="profileSection = 'follower'"> Followers </div>
                            <div class="web-text-sub-pf-white tw-bg-[#082266] tw-py-3" v-if="profileSection == 'follower'"> 
                              <p class="tw-flex tw-justify-center"> Followers </p> 
                              <p class="tw-flex tw-justify-center">({{ result }})</p> 
                            </div>
                        </v-col>
                    </v-row>
                </div>

                <!-------- insert component here ---------->
                <!-- Bookmark /> -->
                <div v-if="profileSection == 'bookmark'">
                <Bookmarks :bookmarkList="book.bookmarkList.data.content"/>
                <div v-show="book.bookmarkList.data.content.length !== 0" class="py-1">
                <v-pagination v-model="page" :length="book.bookmarkList.data.totalPages" :total-visible="7"
                  rounded="20" @update:model-value="book.changeBookmarkPage(page)">
                </v-pagination></div></div>
                <!-- <Reviews /> -->
                <div v-if="profileSection == 'review'">
                <Reviews />
              </div>
            </div>
        </div>
        <!-- Popup -->
        <changePasswordPopup :dialog="changePassword" @toggle="handleChangePassword()" @update="login.changePassword()"/>
    </div>
</template>
 
<style scoped>
</style>