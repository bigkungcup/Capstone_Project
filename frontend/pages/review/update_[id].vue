<script setup>
import { useReviews } from "~/stores/review";
import { useBooks } from "~/stores/book";
import { ref } from 'vue';
import updateConfirmPopup from "~/components/reviews/popups/updateConfirmPopup.vue";
import leaveConfirmPopup from "~/components/reviews/popups/leaveConfirmPopup.vue";
import validatePopup from "~/components/reviews/popups/validatePopup.vue";

const book = useBooks();
const reviews = useReviews();
const route = useRoute()
const confirmLeavePopup = ref(false);
const confirmUpdatePopup = ref(false);

definePageMeta({
    layout: false,
})

function toggleLeavePopup() {
    confirmLeavePopup.value = !confirmLeavePopup.value
}

function toggleUpdatePopup() {
    confirmUpdatePopup.value = !confirmUpdatePopup.value
}

function toggleValidatePopup() {
    reviews.validate = !reviews.validate
}

const rules = {
    required: value => !!value || 'Field is required',
}

await reviews.getReviewDetail(route.params.id)
await book.getBookDetail(reviews.newReview.bookId)

</script>
 
<template>
    <div class="tw-bg-[#3157BB] tw-h-[4rem] d-flex justify-center align-center tw-drop-shadow-xl">
        <p class="lily tw-text-3xl tw-text-white">Bannarug</p>
    </div>
    <div class=" tw-pt-1 tw-pb-10 tw-drop-shadow-lg tw-space-y-1">
        <div class="tw-mx-36 tw-mt-5">
            <v-btn prepend-icon="mdi mdi-chevron-left" variant="text" @click="toggleLeavePopup()" width="8%"
                color="#082250">
                <p class="tw-font-bold">Back</p>
            </v-btn>
        </div>
        <div class="tw-flex tw-justify-center tw-min-h-[25rem] tw-pb-2">
            <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
                <v-row>
                    <v-col cols="2">
                        <v-avatar class="mx-8 my-4" size="150" rounded="0">
                            <v-img src="/image/cover_not_available.jpg"></v-img>
                        </v-avatar>
                    </v-col>
                    <v-col cols="10">
                        <div class="web-text-sub tw-my-8 tw-space-y-3">
                            <p class="web-text-title">{{ book.bookDetail.data.bookName }}</p>
                            <p class="">by {{ book.bookDetail.data.author }}</p>
                            <div class="tw-space-x-1 tw-inline-flex tw-items-center tw-font-bold">My rating:
                                <v-rating hover :length="5" :size="32" :model-value="0" color="orange-lighten-1"
                                    active-color="#FFBB11" v-model="reviews.newReview.rating" />
                            </div>
                        </div>
                    </v-col>

                </v-row>
                <div class="tw-mx-8 tw-space-y-4">
                    <v-text-field v-model="reviews.newReview.title" label="Review Header" variant="solo" height="100px"
                        :rules="[rules.required]"></v-text-field>
                    <v-textarea v-model="reviews.newReview.detail" label="Review Detail" variant="solo" rows="5"
                        :rules="[rules.required]"></v-textarea>
                </div>
                <div class="tw-mx-8 tw-pb-4">
                    <v-checkbox label="Hide entire review because of spoilers" hide-details
                        v-model="reviews.newReview.spoileFlag"></v-checkbox>
                </div>
            </v-card>
        </div>

        <div class="d-flex justify-end tw-mx-[10rem] tw-mt-5 tw-space-x-4">
            <v-btn color="#1D419F" variant="outlined" @click="reviews.clearNewReview()">clear</v-btn>
            <v-btn color="#1D419F" variant="flat" @click="toggleUpdatePopup()" :disabled="reviews.newReview.title == '' || reviews.newReview.detail == ''">submit</v-btn>
        </div>
        <updateConfirmPopup :dialog="confirmUpdatePopup"  @toggle="toggleUpdatePopup()" @update="reviews.updateReview(route.params.id)"/>
        <leaveConfirmPopup :dialog="confirmLeavePopup" @toggle="toggleLeavePopup()"
            @back="$router.go(-1)"/>
</div></template>
 
<style></style>