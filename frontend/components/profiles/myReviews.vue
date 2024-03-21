<script setup>
import { useReviews } from '~/stores/review'
defineEmits(["like", "update"]);
defineProps({
    reviewList: {
        type: Array,
        require: true,
    }
})

const reviews = useReviews();
const roleToken = ref(localStorage.getItem("role"));
</script>
 
<template>
    <div class="">
    <v-card 
    v-for="review in reviewList" 
    style="box-shadow: none;" 
    >
        <v-row class="tw-py-6">
            <v-col cols="3" class="tw-space-y-2" align="center">
                <nuxt-link :to="`/book/${review.bookDetail.bookId}/`">
                <v-img class="tw-drop-shadow-lg" src="/image/cover_not_available.jpg" width="150px" height="250px" cover v-if="review.bookDetail.file == null"></v-img>
                <v-img class="tw-drop-shadow-lg tw-border-2 tw-border-[#082266]" :src="review.bookDetail.file" width="150px" height="250px" cover v-if="review.bookDetail.file !== null"></v-img>
                <p class="web-text-detail tw-truncate">{{ review.bookDetail.bookName }}</p>
            </nuxt-link>    
            </v-col>
            <v-col cols="7" class="tw-space-y-4" >
                <div class="tw-flex tw-justify-start">
                    <v-rating :model-value="review.reviewRating" color="#FFB703" density="compact" size="meduim"
                        half-increments readonly></v-rating>
                </div>
                <p class="tw-flex tw-justify-start web-text-title">{{ review.reviewTitle }}</p>
                <p class="tw-flex tw-justify-start web-text-sub tw-min-h-[6rem]" v-if="review.spoileFlag == 0">
                   {{ review.reviewDetail }}
                </p>
                <div class="web-text-sub tw-min-h-[6rem]" v-if="review.spoileFlag == 1">
                    <v-expansion-panels variant="inset">
                        <v-expansion-panel>
                            <v-expansion-panel-title color="#082266" class="tw-font-bold" expand-icon="mdi-plus"
                                collapse-icon="mdi-minus">
                                Spoil
                            </v-expansion-panel-title>
                            <v-expansion-panel-text class="tw-indent-8 web-text-detail">
                                {{ review.reviewDetail }}
                            </v-expansion-panel-text>
                        </v-expansion-panel>
                    </v-expansion-panels>
                </div>
                <p class="tw-flex tw-justify-start web-text-sub tw-py-4">
                    <div v-if="roleToken != 'USER'">
                    {{ review.reviewTotalLike }} likes {{ review.reviewTotalDisLike }} dislikes
                    </div>
                    <div v-if="roleToken == 'USER'">
                        <!-- Start (create) -->
                        <v-btn
                          prepend-icon="mdi mdi-thumb-up-outline"
                          variant="text"
                          v-show="review.likeStatus == null"
                          @click="
                            $emit('like', {
                              reviewId: review.reviewId,
                              likeStatus: 1,
                            })
                          "
                          >{{ review.reviewTotalLike }}</v-btn
                        >
                        <v-btn
                          prepend-icon="mdi mdi-thumb-down-outline"
                          variant="text"
                          v-show="review.likeStatus == null"
                          @click="
                            $emit('like', {
                              reviewId: review.reviewId,
                              likeStatus: 2,
                            })
                          "
                          >{{ review.reviewTotalDisLike }}</v-btn
                        >
                        <!-- Like/Dislike (update) -->
                        <div
                          class="d-flex tw-space-x-1"
                          v-if="review.likeStatus != null"
                        >
                          <v-btn
                            prepend-icon="mdi mdi-thumb-up-outline"
                            variant="text"
                            v-show="
                              review.likeStatus.likeStatus != 1 &&
                              review.likeStatus != null
                            "
                            @click="
                              $emit('update', {
                                reviewId: review.reviewId,
                                likeStatus: 1,
                                likeStatusId: review.likeStatus.likeStatusId,
                              })
                            "
                            >{{ review.reviewTotalLike }}</v-btn
                          >
                          <v-btn
                            prepend-icon="mdi mdi-thumb-up"
                            variant="text"
                            v-show="review.likeStatus.likeStatus == 1"
                            @click="
                              $emit('update', {
                                reviewId: review.reviewId,
                                likeStatus: 3,
                                likeStatusId: review.likeStatus.likeStatusId,
                              })
                            "
                            >{{ review.reviewTotalLike }}</v-btn
                          >
                          <v-btn
                            prepend-icon="mdi mdi-thumb-down-outline"
                            variant="text"
                            v-show="
                              review.likeStatus.likeStatus != 2 &&
                              review.likeStatus != null
                            "
                            @click="
                              $emit('update', {
                                reviewId: review.reviewId,
                                likeStatus: 2,
                                likeStatusId: review.likeStatus.likeStatusId,
                              })
                            "
                            >{{ review.reviewTotalDisLike }}</v-btn
                          >
                          <v-btn
                            prepend-icon="mdi mdi-thumb-down"
                            variant="text"
                            v-show="review.likeStatus.likeStatus == 2"
                            @click="
                              $emit('update', {
                                reviewId: review.reviewId,
                                likeStatus: 3,
                                likeStatusId: review.likeStatus.likeStatusId,
                              })
                            "
                            >{{ review.reviewTotalDisLike }}</v-btn
                          >
                        </div>
                    </div>
                </p>
            </v-col>
            <v-col cols="2">
                <div class="tw-flex tw-justify-end web-text-sub tw-px-5">
                    {{ reviews.countUpdateTime(review.countDateTime) }}
                </div>
            </v-col>
        </v-row>
    </v-card>
    <hr />
</div>
</template>
 
<style scoped>

</style>