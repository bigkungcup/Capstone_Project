<script setup>
defineEmits(["like", "update"]);
defineProps({
  newReviewList: {
    type: Array,
    require: true,
  },
});
</script>

<template>
  <div>
    <div class="tw-text-4xl tw-text-[#082266] tw-font-extrabold tw-py-8">
      New Review !
    </div>
    <div class="">
      <v-sheet width="100%" height="100%">
        <v-slide-group show-arrows>
          <v-slide-group-item>
            <div
              class="tw-h-[16rem] tw-w-[30rem] tw-mx-4 tw-border-4 tw-border-[#3157BB] tw-rounded-xl"
              v-for="review in newReviewList"
            >
              <span
                class="tw-bg-[#1646C4] web-text-rv-head tw-p-3"
                style="border-radius: 18px 0 0 0"
              >
                And Then There Were None
              </span>
              <div class="tw-mx-6 tw-my-4">
                <p class="web-text-title">"{{ review.reviewTitle }}"</p>
                <p class="web-text-sub tw-min-h-[7rem]">
                  {{ review.reviewDetail }}
                </p>
                <div class="web-text-sub tw-my-1">
                  <v-row no-gutters>
                    <v-col cols="2" class="tw-space-x-2">
                      <!-- Start (create)
                        <v-btn prepend-icon="mdi mdi-thumb-up-outline" variant="text" v-show="review.likeStatus.likeStatus == 0" @click="review.likeStatus.likeStatus = 1, $emit('like', { reviewId: review.reviewId, likeStatus: 1 })">Likes</v-btn>
                        <v-btn prepend-icon="mdi mdi-thumb-up-outline" variant="text" v-show="review.likeStatus.likeStatus == 0" @click="review.likeStatus.likeStatus = 2, $emit('like', { reviewId: review.reviewId, likeStatus: 2 })">Dislikes</v-btn>
                        Like/Dislike (update)
                        <v-btn prepend-icon="mdi mdi-thumb-up-outline" variant="text" v-show="review.likeStatus.likeStatus != 1 && review.likeStatus.likeStatus != 0" @click="review.likeStatus.likeStatus = 1, $emit('update', { reviewId: review.reviewId, likeStatus: 1, likeStatusId: review.likeStatus.likeStatusId })">Likes</v-btn>
                        <v-btn prepend-icon="mdi mdi-thumb-up" variant="text" v-show="review.likeStatus.likeStatus == 1" @click="review.likeStatus.likeStatus = 0, $emit('update', { reviewId: review.reviewId, likeStatus: 3, likeStatusId: review.likeStatus.likeStatusId })">Likes</v-btn>
                        <v-btn prepend-icon="mdi mdi-thumb-down-outline" variant="text" v-show="review.likeStatus.likeStatus != 2 && review.likeStatus.likeStatus != 0" @click="review.likeStatus.likeStatus = 2, $emit('update', { reviewId: review.reviewId, likeStatus: 2, likeStatusId: review.likeStatus.likeStatusId })">Dislikes</v-btn>
                        <v-btn prepend-icon="mdi mdi-thumb-down" variant="text" v-show="review.likeStatus.likeStatus == 2" @click="review.likeStatus.likeStatus = 0, $emit('update', { reviewId: review.reviewId, likeStatus: 3, likeStatusId: review.likeStatus.likeStatusId })">Dislikes</v-btn> -->

                      <!-- <v-icon icon="mdi mdi-thumb-up-outline" /><span>10</span> -->
                      <v-btn
                        prepend-icon="mdi mdi-thumb-up-outline"
                        variant="text"
                        >10</v-btn
                      >
                    </v-col>
                    <v-col cols="2" class="tw-space-x-2">
                      <v-btn
                        prepend-icon="mdi mdi-thumb-down-outline"
                        variant="text"
                        >0</v-btn
                      >
                    </v-col>
                    <v-col cols="3"></v-col>
                    <v-col cols="4" align="center"
                      ><div class="web-text-sub tw-truncate">
                        {{ review.userDetail.displayName }}
                      </div></v-col
                    >
                    <v-col cols="1" align="center"
                      ><v-img
                        src="/image/guest_icon.png"
                        v-if="review.userDetail.file == null"
                        width="40"
                        class=""
                      /><v-img
                        :src="review.userDetail.file"
                        v-if="review.userDetail.file != null"
                        width="40"
                        class="tw-rounded-full tw-border-[#082266] tw-border-2"
                        cover
                      />
                    </v-col>
                  </v-row>
                </div>
              </div>
            </div>

            <!--             
            <div
              class="tw-h-[16rem] tw-w-[30rem] tw-mx-4 tw-border-4 tw-border-[#3157BB] tw-rounded-xl"
            >
              <span
                class="tw-bg-[#1646C4] web-text-rv-head tw-p-3"
                style="border-radius: 18px 0 0 0"
              >
                And Then There Were None
              </span>
              <div class="tw-mx-6 tw-my-4">
                <p class="web-text-title">"Awesome"</p>
                <p class="web-text-sub">
                  I really enjoyed this book. It was required reading for a
                  University course I took on Adolescent Literature.This book
                  has been placed on banned book lists by needlessly...
                </p>
                <div class="web-text-sub tw-my-1">
                  <v-row no-gutters>
                    <v-col cols="2" class="tw-space-x-2">
                      <v-btn
                        prepend-icon="mdi mdi-thumb-up-outline"
                        variant="text"
                        >10</v-btn
                      >
                    </v-col>
                    <v-col cols="2" class="tw-space-x-2">
                      <v-btn
                        prepend-icon="mdi mdi-thumb-down-outline"
                        variant="text"
                        >0</v-btn
                      >
                    </v-col>
                    <v-col cols="3"></v-col>
                    <v-col cols="4" align="center"
                      ><div class="web-text-sub tw-truncate">
                        Papada Keawchaon
                      </div></v-col
                    >
                    <v-col cols="1" align="center"
                      ><v-img
                        src="/image/guest_icon.png"
                        width="40"
                        class=""
                      ></v-img
                    ></v-col>
                  </v-row>
                </div>
              </div>
            </div> -->

            <div
              class="tw-h-[16rem] tw-w-[30rem] tw-mx-4 tw-border-4 tw-border-[#3157BB] tw-rounded-xl"
            >
              <span
                class="tw-bg-[#1646C4] web-text-rv-head tw-p-3"
                style="border-radius: 18px 0 0 0"
              >
                And Then There Were None
              </span>
              <div class="tw-mx-6 tw-my-4">
                <p class="web-text-title">"Awesome"</p>
                <p class="web-text-sub">
                  I really enjoyed this book. It was required reading for a
                  University course I took on Adolescent Literature.This book
                  has been placed on banned book lists by needlessly...
                </p>
                <div class="web-text-sub tw-my-1">
                  <v-row no-gutters>
                    <v-col cols="2" class="tw-space-x-2">
                      <!-- <v-icon icon="mdi mdi-thumb-up-outline" /><span>10</span> -->
                      <v-btn
                        prepend-icon="mdi mdi-thumb-up-outline"
                        variant="text"
                        >10</v-btn
                      >
                    </v-col>
                    <v-col cols="2" class="tw-space-x-2">
                      <v-btn
                        prepend-icon="mdi mdi-thumb-down-outline"
                        variant="text"
                        >0</v-btn
                      >
                    </v-col>
                    <v-col cols="3"></v-col>
                    <v-col cols="4" align="center"
                      ><div class="web-text-sub tw-truncate">
                        Papada Keawchaon
                      </div></v-col
                    >
                    <v-col cols="1" align="center"
                      ><v-img
                        src="/image/guest_icon.png"
                        width="40"
                        class=""
                      ></v-img
                    ></v-col>
                  </v-row>
                </div>
              </div>
            </div>
          </v-slide-group-item>
        </v-slide-group>
      </v-sheet>
    </div>
  </div>
</template>

<style></style>
