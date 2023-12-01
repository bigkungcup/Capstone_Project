<script setup>
defineEmits(["delete"]);

defineProps({
    reviewList: {
        type: Array,
        require: true,
    },
    bookId: {
        type: String,
        require: true
    }
})

import { mergeProps } from 'vue'
</script>
 
<template>
    <div class="tw-flex tw-flex-col tw-justify-center tw-space-y-5 tw-min-w-full tw-min-h-[16rem]">
        <v-card v-for="review in reviewList" width="100%">
            <v-row>
                <v-col cols="3" class="web-text-detail tw-my-5 tw-space-x-5 tw-space-y-2" align="left">
                    <v-img src="/image/cat.jpg" width="40%" height="40%" class="tw-rounded-full tw-mx-5" cover />
                    <!-- <p class="tw-font-bold">{{ review.reviewUsername }}</p>
                    <h3>{{ review.reviewAmount }} reviews</h3>
                    <h3>{{ review.followers }} followers</h3> -->
                    <p class="tw-font-bold">Cat The Reviewer</p>
                    <p class="web-text-sub">250 reviews</p>
                    <p class="web-text-sub">1231 followers</p>
                </v-col>
                <!-- <v-col cols="1"></v-col> -->
                <v-col cols="9" class="web-text-detail tw-my-5 tw-space-y-3">
                    <v-rating :model-value="review.reviewRating" color="#FFB703" density="compact" size="meduim"
                        half-increments readonly></v-rating>
                    <p class="tw-font-bold">{{ review.reviewTitle }}</p>
                    <!-- <div class="tw-h-[4rem] tw-overflow-hidden" v-show="review.spoileFlag == 0">
                        <p class="tw-text-ellipsis">
                            {{ review.reviewDetail }}
                        </p>
                    </div> -->
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
                    <div class="web-text-sub"><span> {{ review.reviewTotalLike }} likes </span> <span> 10 dislikes</span>
                    </div>
                    <div class="tw-space-x-3">
                        <v-icon icon="mdi mdi-thumb-up-outline"></v-icon><span>Likes</span>
                        <v-icon icon="mdi mdi-thumb-down-outline"></v-icon><span>Dislikes</span>
                        <!-- <v-icon icon="mdi mdi-dots-horizontal"></v-icon> -->
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
                                    <v-list-item>
                                        <v-list-item-title class="web-text-detail tw-space-x-2"><v-icon icon="mdi mdi-pencil-outline"></v-icon><span>Edit this review</span></v-list-item-title>
                                    </v-list-item>
                                    <v-list-item>
                                        <v-list-item-title class="web-text-detail tw-space-x-2" ><v-icon icon="mdi mdi-trash-can-outline"></v-icon><span>Delete this review</span></v-list-item-title>
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