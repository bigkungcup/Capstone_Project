<script setup>
import { useReviews } from "~/stores/review";
import { useBooks } from "~/stores/book";
import { ref } from 'vue';

const book = useBooks();
const reviews = useReviews();
const route = useRoute()
// const dialog = ref(false);
await book.getBookDetail(route.params.id)
reviews.newReview.bookId = route.params.id;

// async function createReview() {
//     console.log(reviews.newReview);
//   await $fetch(
//     `${import.meta.env.VITE_BASE_URL}/review`,
//     {
//         method: "POST",
//         // options.headers = {
//         //   "Content-Type": "application/json",
//         //   },
//         body: {
//             rating: reviews.newReview.rating,
//             detail: reviews.newReview.detail,
//             title:  reviews.newReview.title,
//             userId: '2',
//             bookId: reviews.newReview.bookId
//       },
//     },
//   );
// }


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
                                    active-color="#FFBB11" v-model="reviews.newReview.rating"/>
                            </div>
                        </div>
                    </v-col>

                </v-row>
                <div class="tw-mx-8 tw-space-y-4">
                    <v-text-field v-model="reviews.newReview.title" label="Review Header" variant="solo" height="100px" hide-details></v-text-field>
                    <v-textarea v-model="reviews.newReview.detail" label="Review Detail" variant="solo" rows="5" hide-details></v-textarea>
                </div>
                <div class="tw-mx-8 tw-pb-4">
                    <v-checkbox label="Hide entire review because of spoilers" hide-details v-model="reviews.newReview.spoileFlag" value='1'></v-checkbox>
                </div>
            </v-card>
        </div>

        <div class="tw-flex tw-justify-end  tw-mx-[9rem] tw-mt-5 tw-space-x-4">
            <v-btn color="#1D419F" variant="outlined" @click="reviews.clearNewReview()">clear</v-btn>
            <!-- <confirmPopup :popupDetail="reviews.createConfirmPopup"/> -->
            <v-btn color="#1D419F" variant="flat" @click="reviews.createReview()">submit</v-btn>
        </div>
        <!-- <v-dialog v-model="dialog" persistent width="auto">
            <template v-slot:activator="{ props }">
                <v-btn color="primary" v-bind="props">
                    Open Dialog
                </v-btn>
            </template>
            <confirmPopup :dialog="dialog"/>
        </v-dialog> -->
        <!-- <confirmPopup :dialog="dialog"/> -->
    </div>
</template>
 
<style></style>