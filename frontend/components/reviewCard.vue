<script setup>
import { mergeProps } from 'vue'
import { useReviews } from "~/stores/review";
const reviews = useReviews();
defineEmits(["toggle","set"]);

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

</script>
 
<template>
    <div class="tw-flex tw-flex-col tw-justify-center tw-space-y-5 tw-min-w-full tw-min-h-[16rem]">
        <v-card v-for="review in reviewList" width="100%">
            <v-row>
                <v-col cols="3" class="web-text-detail tw-my-5 tw-space-x-5 tw-space-y-2" align="left">
                    <v-img src="/image/cat.jpg" width="40%" height="40%" class="tw-rounded-full tw-mx-5" cover />
                    <p class="tw-font-bold">Guest</p>
                    <p class="web-text-sub">0 reviews</p>
                    <p class="web-text-sub">0 followers</p>
                </v-col>
                <v-col cols="9" class="web-text-detail tw-my-5 tw-space-y-3">
                    <v-rating :model-value="review.reviewRating" color="#FFB703" density="compact" size="meduim"
                        half-increments readonly></v-rating>
                    <p class="tw-font-bold">{{ review.reviewTitle }}</p>
                    <div class="tw-min-h-[4rem]" v-show="review.spoileFlag == 0">
                        <p>{{ review.reviewDetail }}</p>
                    </div>
                    <v-expansion-panels variant="inset" v-show="review.spoileFlag == 1">
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
                    <div class="web-text-sub"><span> {{ review.reviewTotalLike }} likes </span> <span> {{ review.reviewTotalDisLike }} dislikes</span>
                    </div>
                    <div class="tw-space-x-3">
                        <v-icon icon="mdi mdi-thumb-up-outline"></v-icon><span>Likes</span>
                        <v-icon icon="mdi mdi-thumb-down-outline"></v-icon><span>Dislikes</span>
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
                                    <v-list-item :to="`../review/update_${review.reviewId}`">
                                        <v-list-item-title class="web-text-detail tw-space-x-2"><v-icon icon="mdi mdi-pencil-outline"></v-icon><span>Edit this review</span></v-list-item-title>
                                    </v-list-item>
                                    <v-list-item class="hover:tw-bg-zinc-300/20 tw-cursor-pointer">
                                        <v-list-item-title class="web-text-detail">
                                            <v-list-item-title class="web-text-detail tw-space-x-2" @click="$emit('toggle'),$emit('set',review.reviewId)"><v-icon icon="mdi mdi-trash-can-outline"></v-icon><span>Delete this review</span></v-list-item-title>
                                        </v-list-item-title>
                                    </v-list-item>
                                </v-list>
                            </v-menu>
                        </span>
                    </div>
                </v-col>
            </v-row>
        </v-card>
    </div>
</template>
 
<style></style>