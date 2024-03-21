<script setup>
defineEmits(["follow","unfollow"]);
defineProps({
    followingList: {
        type: Array,
        require: true,
    }
})
</script>

<template>
  <v-container>
    <v-row no-gutters  >
      <v-col cols="4" v-for="user in followingList">
        <v-card class="tw-w-[20rem] tw-h-[20rem] tw-my-2 tw-mx-6" >
          <div class="tw-h-[8rem]">
            <v-img src="/image/profile_banner.jpg" v-show="user.userFollowings.file == null" cover></v-img>
            <v-img
          class="tw-blur-[2px]"
          v-show="user.userFollowings.file != null"  
          :src="user.userFollowings.file"
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
                v-show="user.userFollowings.file == null"
                cover
              />
              <v-img
                :src="user.userFollowings.file"
                width="120"
                height="120"
                class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-6"
                v-show="user.userFollowings.file !== null"
                cover
              />
            </div>
            <v-btn color="#1D419F" variant="elevated" class="tw-m-2" v-if="user.followStatus == 1"
            @click="user.followStatus = 0, $emit('unfollow', user.userFollowings.userId)"
              >Following</v-btn
            >
            <v-btn color="#1D419F" variant="outlined"  class="tw-m-2" v-if="user.followStatus !== 1"
            @click="user.followStatus = 1, $emit('follow', user.userFollowings.userId)"
              >Follow</v-btn
            >
          </div>
          <div class="tw-my-[1rem] tw-mx-4">
            <p class="web-text-title tw-truncate">{{ user.userFollowings.displayName }}</p>
            <p class="web-text-pf-email tw-truncate">{{ user.userFollowings.email }}</p>
            <p class="web-text-detail tw-my-2 tw-truncate">{{ user.userFollowings.bio }}</p>
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped></style>
