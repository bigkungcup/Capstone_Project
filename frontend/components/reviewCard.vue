<script setup>
defineProps({
    reviewList: {
        type: Array,
        require: true,
    }
})
</script>
 
<template>
    <div class="tw-flex tw-flex-col tw-justify-center tw-space-y-5 tw-min-w-full tw-min-h-[16rem]">
        <v-card v-for="review in reviewList" width="100%" class="">
            <v-row>
                <v-col cols="3" class="tw-my-5 tw-space-x-5 tw-space-y-2" align="left">
                    <v-img src="/image/cat.jpg" width="40%" height="40%" class="tw-rounded-full tw-mx-5" cover />
                    <p class="tw-font-bold">{{ review.reviewUsername }}</p>
                    <h3>{{ review.reviewAmount }} reviews</h3>
                    <h3>{{ review.followers }} followers</h3>
                </v-col>
                <!-- <v-col cols="1"></v-col> -->
                <v-col cols="9" class="tw-my-5 tw-space-y-3">
                    <v-img src="/image/star.png" width="20%" />
                    <p class="tw-font-bold">{{ review.reviewTitle }}</p>
                    <div class="tw-h-[4rem] tw-overflow-hidden">
                        <p class="tw-text-ellipsis">
                            {{ review.reviewDetail }}
                        </p>
                    </div>
                    <div class=""><span> {{ review.reviewTotalLike }} likes </span> <span> 10 dislikes</span></div>
                    <div class=" tw-space-x-3">
                        <v-icon icon="mdi mdi-thumb-up-outline"></v-icon><span>Likes</span>
                        <v-icon icon="mdi mdi-thumb-down-outline"></v-icon><span>Dislikes</span>
                        <v-icon icon="mdi mdi-dots-horizontal"></v-icon>
                    </div>
                </v-col>
            </v-row>
        </v-card>
    </div>
</template>
 
<style></style>