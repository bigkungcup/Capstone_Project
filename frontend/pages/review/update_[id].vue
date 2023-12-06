<script setup>
import { useReviews } from "~/stores/review";
import { useBooks } from "~/stores/book";
import { ref } from 'vue';

const book = useBooks();
const reviews = useReviews();
const route = useRoute()
const confirmPopup = ref(false);

function togglePopup() {
    confirmPopup.value = !confirmPopup.value
}

await reviews.getReviewDetail(route.params.id)
await book.getBookDetail(reviews.newReview.data.book.bookId)


</script>
 
<template>
    <div class=" tw-pt-1 tw-pb-10 tw-drop-shadow-lg">
        <div class="tw-mx-36 tw-mt-5">
            <v-btn prepend-icon="mdi mdi-chevron-left" variant="text" @click="$router.go(-1)" width="8%" color="#082250">
                <p class="tw-font-bold">Back</p>
            </v-btn>
        </div>
        <div class="tw-flex tw-justify-center tw-min-h-[25rem]">
            <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
                <v-row>
                    <v-col cols="2">
                        <v-avatar class="mx-8 my-4" size="150" rounded="0">
                            <v-img src="/image/bookcover.png"></v-img>
                        </v-avatar>
                    </v-col>
                    <v-col cols="10">
                        <div class="web-text-sub tw-my-8 tw-space-y-3">
                            <p class="web-text-title">{{ book.bookDetail.data.bookName }}</p>
                            <p class="">by {{ book.bookDetail.data.author }}</p>
                            <div class="tw-space-x-1 tw-inline-flex tw-items-center tw-font-bold">My rating:
                                <v-rating hover :length="5" :size="32" :model-value="0" color="orange-lighten-1"
                                    active-color="#FFBB11" v-model="reviews.newReview.data.reviewRating"/>
                            </div>
                        </div>
                    </v-col>

                </v-row>
                <div class="tw-mx-8 tw-space-y-4">
                    <v-text-field v-model="reviews.newReview.data.reviewTitle" label="Review Header" variant="solo" height="100px" hide-details></v-text-field>
                    <v-textarea v-model="reviews.newReview.data.reviewDetail" label="Review Detail" variant="solo" rows="5" hide-details></v-textarea>
                </div>
                <div class="tw-mx-8 tw-pb-4">
                    <v-checkbox label="Hide entire review because of spoilers" hide-details v-model="reviews.newReview.data.spoileFlag" value='1'></v-checkbox>
                </div>
            </v-card>
        </div>

        <div class="d-flex justify-end tw-mx-[10rem] tw-mt-5 tw-space-x-4">
            <v-btn color="#1D419F" variant="outlined" @click="reviews.clearEditReview()">clear</v-btn>
            <v-btn color="#1D419F" variant="flat" @click="togglePopup()">submit</v-btn>
        </div>
        <ConfirmPopupCard :popupDetail="reviews.updateConfirmPopup" :dialog="confirmPopup" @toggle="togglePopup()" @update="reviews.updateReview(reviews.newReview.data.reviewId)"/>
    </div>
</template>
 
<style></style>