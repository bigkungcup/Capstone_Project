<script setup>
defineEmits(["like", "update"]);
defineProps({
  newReviewList: {
    type: Array,
    require: true,
  },
});

const roleToken = ref(localStorage.getItem('role'));
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
            <nuxt-link :to="`/book/${review.bookDetail.bookId}/`">
              <span
                class="tw-bg-[#1646C4] web-text-rv-head tw-p-3"
                style="border-radius: 18px 0 0 0"
              >
                {{ review.bookDetail.bookName }}
              </span>
              <div class="tw-mx-6 tw-my-4">
                <p class="web-text-title">"{{ review.reviewTitle }}"</p>
                <p class="web-text-sub tw-min-h-[7rem]">
                  {{ review.reviewDetail }}
                </p>
                <div class="web-text-sub tw-my-1">
                  <v-row no-gutters>
                    <v-col cols="2">
                      <div class="d-flex tw-space-x-1" v-if="roleToken == 'USER'">
                        <!-- Start (create) -->
                        <v-btn prepend-icon="mdi mdi-thumb-up-outline" variant="text" v-show="review.likeStatus == null" @click="$emit('like', { reviewId: review.reviewId, likeStatus: 1 })">{{ review.reviewTotalLike }}</v-btn>
                        <v-btn prepend-icon="mdi mdi-thumb-down-outline" variant="text" v-show="review.likeStatus == null" @click="$emit('like', { reviewId: review.reviewId, likeStatus: 2 })">{{ review.reviewTotalDisLike }}</v-btn>
                        <!-- Like/Dislike (update) -->
                        <div class="d-flex tw-space-x-1" v-if="review.likeStatus != null">
                        <v-btn prepend-icon="mdi mdi-thumb-up-outline" variant="text" v-show="review.likeStatus.likeStatus != 1 && review.likeStatus != null" @click="$emit('update', { reviewId: review.reviewId, likeStatus: 1, likeStatusId: review.likeStatus.likeStatusId })">{{ review.reviewTotalLike }}</v-btn>
                        <v-btn prepend-icon="mdi mdi-thumb-up" variant="text" v-show="review.likeStatus.likeStatus == 1" @click="$emit('update', { reviewId: review.reviewId, likeStatus: 3, likeStatusId: review.likeStatus.likeStatusId })">{{ review.reviewTotalLike }}</v-btn>
                        <v-btn prepend-icon="mdi mdi-thumb-down-outline" variant="text" v-show="review.likeStatus.likeStatus != 2 && review.likeStatus != null" @click="$emit('update', { reviewId: review.reviewId, likeStatus: 2, likeStatusId: review.likeStatus.likeStatusId })">{{ review.reviewTotalDisLike }}</v-btn>
                        <v-btn prepend-icon="mdi mdi-thumb-down" variant="text" v-show="review.likeStatus.likeStatus == 2" @click="$emit('update', { reviewId: review.reviewId, likeStatus: 3, likeStatusId: review.likeStatus.likeStatusId })">{{review.likeStatus.likeStatus}}{{ review.reviewTotalDisLike }}</v-btn>
                        </div>
                    </div>
                    <div class="d-flex tw-space-x-1" v-if="roleToken == 'GUEST'">
                      <v-btn prepend-icon="mdi mdi-thumb-up-outline" variant="text" v-show="review.likeStatus == null">{{ review.reviewTotalLike }}</v-btn>
                        <v-btn prepend-icon="mdi mdi-thumb-down-outline" variant="text" v-show="review.likeStatus == null">{{ review.reviewTotalDisLike }}</v-btn>
                    </div>
                    </v-col>
                    <v-col cols="3"></v-col>
                    <v-col cols="6" align="end" class="px-2"
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
            </nuxt-link>
            </div>
          </v-slide-group-item>
        </v-slide-group>
      </v-sheet>
    </div>
  </div>
</template>

<style></style>
