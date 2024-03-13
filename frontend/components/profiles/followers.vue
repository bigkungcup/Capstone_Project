<script setup>
defineEmits(["follow","unfollow"]);
defineProps({
    followerList: {
        type: Array,
        require: true,
    }
})
</script>

<template>
  <v-container>
    <v-row no-gutters>
      <v-col cols="4">
        <v-card class="tw-w-[20rem] tw-h-[20rem]" v-for="user in followerList">
          <div class="tw-h-[8rem]">
            <v-img src="/image/profile_banner.jpg" v-show="user.userFollowers.file == null" cover></v-img>
            <v-img
          class="tw-blur-[2px]"
          v-show="user.userFollowers.file != null"  
          :src="user.userFollowers.file"
          cover
        ></v-img>
          </div>

          <div class="tw-flex tw-place-content-center">
            <div class="tw-w-full tw-h-full">
              <v-img
                src="/image/guest_icon.png"
                width="120"
                height="120"
                class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-6"
                v-show="user.userFollowers.file == null"
                cover
              />
              <v-img
                :src="user.userFollowers.file"
                width="120"
                height="120"
                class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-6"
                v-show="user.userFollowers.file !== null"
                cover
              />
            </div>
            <v-btn color="#1D419F" variant="elevated" class="tw-m-2" v-if="user.followingStatus == 1"
            @click="user.followingStatus = 0, $emit('unfollow', user.userFollowers.userId)"
              >Following</v-btn
            >
            <v-btn color="#1D419F" variant="outlined"  class="tw-m-2" v-if="user.followingStatus !== 1"
            @click="user.followingStatus = 1, $emit('follow', user.userFollowers.userId)"
              >Follow</v-btn
            >
          </div>
          <div class="tw-my-[1rem] tw-mx-4">
            <p class="web-text-title tw-truncate">{{ user.userFollowers.displayName }}</p>
            <p class="web-text-pf-email tw-truncate">{{ user.userFollowers.email }}</p>
            <p class="web-text-detail tw-my-2 tw-truncate">{{ user.userFollowers.bio }}</p>
          </div>
        </v-card>
      </v-col>
      <!-- <v-col cols="4">
        <v-card class="tw-w-[20rem] tw-h-[20rem]">
          <div class="tw-h-[8rem]">
            <v-img src="/image/profile_banner.jpg" cover></v-img>
          </div>

          <div class="tw-flex tw-place-content-center">
            <div class="tw-w-full tw-h-full">
              <v-img
                src="/image/guest_icon.png"
                width="120"
                height="120"
                class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-6"
                cover
              />
            </div>
            <v-btn color="#1D419F" variant="outlined" class="tw-m-2"
              >Follow</v-btn
            >
          </div>
          <div class="tw-my-[1rem] tw-mx-4">
            <p class="web-text-title">Cat the reviewer</p>
            <p class="web-text-pf-email">cat@gmail.com</p>
            <p class="web-text-detail tw-my-2">I am God</p>
          </div>
        </v-card>
      </v-col>
      <v-col cols="4">
        <v-card class="tw-w-[20rem] tw-h-[20rem]">
          <div class="tw-h-[8rem]">
            <v-img src="/image/profile_banner.jpg" cover></v-img>
          </div>

          <div class="tw-flex tw-place-content-center">
            <div class="tw-w-full tw-h-full">
              <v-img
                src="/image/guest_icon.png"
                width="120"
                height="120"
                class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-6"
                cover
              />
            </div>
            <v-btn color="#1D419F" variant="outlined" class="tw-m-2"
              >Follow</v-btn>
          </div>
          <div class="tw-my-[1rem] tw-mx-4">
            <p class="web-text-title">Cat the reviewer</p>
            <p class="web-text-pf-email">cat@gmail.com</p>
            <p class="web-text-detail tw-my-2">I am God</p>
          </div>
        </v-card>
      </v-col> -->
    </v-row>
  </v-container>
</template>

<style scoped></style>
