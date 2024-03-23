<script setup>
import { useReviews } from '~/stores/review'
import { mergeProps } from 'vue'
defineEmits(["toggle","set","like","follow","unfollow","update","report"]);

defineProps({
    reviewList: {
        type: Array,
        require: true,
    },
    bookId: {
        type: Number,
        require: true
    },
})

const reviews = useReviews();
const roleToken = ref(localStorage.getItem('role'));
const idToken = ref(localStorage.getItem('id'));

function userCoverPath(filePath) {
   return filePath = (`../../_nuxt/@fs/${filePath}`)
}

const follow = ref(true)

</script>
 
<template>
    <div class="tw-flex tw-flex-col tw-justify-center tw-space-y-5 tw-min-w-full tw-min-h-[16rem]">
        <v-card v-for="review in reviewList" width="100%" >
            <v-row>
                <v-col cols="3" class="web-text-detail tw-my-5 tw-space-x-5 tw-space-y-1" align="left">
                    <nuxt-link :to="`/user/${review.userDetail.userId}`+'/'">
                    <v-img src="/image/guest_icon.png" width="120" height="120" class="tw-rounded-full tw-border-black tw-border-2 tw-mx-5" cover  v-show="review.userDetail.file == null"/>
                    <v-img :src="review.userDetail.file" width="120" height="120" class="tw-rounded-full tw-border-black tw-border-2  tw-mx-5" cover v-show="review.userDetail.file !== null"/>
                    </nuxt-link>
                    <p class="tw-font-bold">{{review.userDetail.displayName}}</p>
                    <p class="web-text-sub">{{review.userDetail.totalReview}} reviews</p>
                    <p class="web-text-sub">{{review.userDetail.followers}} followers</p>
                <div v-if="review.userDetail.userId != idToken && roleToken == 'USER'">    
                <v-btn
                  height="auto"
                  variant="outlined"
                  color="#3157BB"
                  class="px-8 py-2"
                  rounded="lg"
                  v-if="review.userDetail.followingReview == null"
                  @click="$emit('follow',review.userDetail.userId)"
                >Follow</v-btn>
                <v-btn
                  height="auto"
                  class="px-5 py-2"
                  color="#3157BB"
                  rounded="lg"
                  v-if="review.userDetail.followingReview !== null"
                  @click="$emit('unfollow',review.userDetail.userId)"
                >Following</v-btn>

                </div>
                </v-col>
                <v-col cols="7" class="web-text-detail tw-my-5 tw-space-y-3">
                    <v-rating :model-value="review.reviewRating" color="#FFB703" density="compact" size="meduim"
                        half-increments readonly></v-rating>
                    <p class="tw-font-bold tw-mr-8">{{ review.reviewTitle }}</p>
                    <div class="tw-min-h-[4rem] tw-mr-8" v-show="review.spoileFlag == 0">
                        <p>{{ review.reviewDetail }}</p>
                    </div>
                    <div class="tw-mr-8"  v-show="review.spoileFlag == 1">
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
                    </v-expansion-panels></div>
                    <div class="web-text-sub"><span> {{ review.reviewTotalLike }} likes </span> <span> {{ review.reviewTotalDisLike }} dislikes</span>
                    </div>
                    <div class="d-flex tw-space-x-3" v-if="roleToken == 'USER'">
                        <!-- Start (create) -->
                        <v-btn prepend-icon="mdi mdi-thumb-up-outline" variant="text" v-show="review.likeStatus == null" @click="$emit('like', { reviewId: review.reviewId, likeStatus: 1 })">Likes</v-btn>
                        <v-btn prepend-icon="mdi mdi-thumb-down-outline" variant="text" v-show="review.likeStatus == null" @click="$emit('like', { reviewId: review.reviewId, likeStatus: 2 })">Dislikes</v-btn>
                        <!-- Like/Dislike (update) -->
                        <div v-if="review.likeStatus != null">
                        <v-btn prepend-icon="mdi mdi-thumb-up-outline" variant="text" v-show="review.likeStatus.likeStatus != 1 && review.likeStatus.likeStatus != 0" @click="$emit('update', { reviewId: review.reviewId, likeStatus: 1, likeStatusId: review.likeStatus.likeStatusId })">Likes</v-btn>
                        <v-btn prepend-icon="mdi mdi-thumb-up" variant="text" v-show="review.likeStatus.likeStatus == 1" @click="$emit('update', { reviewId: review.reviewId, likeStatus: 3, likeStatusId: review.likeStatus.likeStatusId })">Likes</v-btn>
                        <v-btn prepend-icon="mdi mdi-thumb-down-outline" variant="text" v-show="review.likeStatus.likeStatus != 2 && review.likeStatus.likeStatus != 0" @click="$emit('update', { reviewId: review.reviewId, likeStatus: 2, likeStatusId: review.likeStatus.likeStatusId })">Dislikes</v-btn>
                        <v-btn prepend-icon="mdi mdi-thumb-down" variant="text" v-show="review.likeStatus.likeStatus == 2" @click="$emit('update', { reviewId: review.reviewId, likeStatus: 3, likeStatusId: review.likeStatus.likeStatusId })">Dislikes</v-btn>
                        </div>
                        <span class="text-center">
                            <v-menu>
                                <template v-slot:activator="{ props: menu }">
                                    <v-tooltip location="top">
                                        <template v-slot:activator="{ props: tooltip }">
                                            <v-icon icon="mdi mdi-dots-horizontal" v-bind="mergeProps(menu, tooltip)"></v-icon>
                                        </template>
                                        <span>More</span>
                                    </v-tooltip>
                                </template>
                                <v-list>
                                    <v-list-item :to="`../../review/update_${review.reviewId}/`" v-show="roleToken == 'ADMIN' || idToken == review.userDetail.userId">
                                        <v-list-item-title class="web-text-detail tw-space-x-2"><v-icon icon="mdi mdi-pencil-outline"></v-icon><span>Edit this review</span></v-list-item-title>
                                    </v-list-item>
                                    <v-list-item class="hover:tw-bg-zinc-300/20 tw-cursor-pointer" v-show="roleToken == 'ADMIN' || idToken == review.userDetail.userId">
                                        <v-list-item-title class="web-text-detail">
                                            <v-list-item-title class="web-text-detail tw-space-x-2" @click="$emit('toggle'),$emit('set',review.reviewId)"><v-icon icon="mdi mdi-trash-can-outline"></v-icon><span>Delete this review</span></v-list-item-title>
                                        </v-list-item-title>
                                    </v-list-item>
                                    <v-list-item v-show="roleToken == 'USER' && review.userDetail.userId == idToken" class="hover:tw-bg-zinc-300/20 tw-cursor-pointer">
                                        <v-list-item-title class="web-text-detail tw-space-x-2 " @click="$emit('report')"
                                            ><v-icon icon="mdi mdi-flag-variant-outline"></v-icon
                                            ><span>Report this review</span></v-list-item-title
                                        >
                                    </v-list-item>
                                </v-list>
                            </v-menu>
                        </span>
                    </div>
                </v-col>
                <v-col cols="2">
                <div class="tw-flex tw-justify-end web-text-sub tw-px-5">
                    {{ reviews.countUpdateTime(review.countDateTime) }}
                </div>
            </v-col>
            </v-row>
        </v-card>
    </div>
</template>
 
<style></style>