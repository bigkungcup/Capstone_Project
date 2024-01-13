<script setup>
import deleteConfirmPopup from "~/components/reviews/popups/deleteConfirmPopup.vue";
import reviewCard from "~/components/reviews/reviewCard.vue";
import { useBooks } from "~/stores/book";
import { useReviews } from "~/stores/review";
import { ref } from "vue";
import similarBook from "~/components/similarBook.vue";


const library = useBooks();
const reviews = useReviews();
const route = useRoute()
const page = ref(1)
const deleteId = ref(0)
const confirmPopup = ref(false);

function setDeleteId(id) {
    deleteId.value = id;
}

function togglePopup() {
    confirmPopup.value = !confirmPopup.value
}

await library.getBookDetail(route.params.id);
await reviews.getReview(route.params.id, 0);

function bookCoverPath(filePath) {
    console.log(filePath);
   return filePath = (`../../_nuxt/@fs/${filePath}`)
}

</script>
 
<template>
    <div class="web-grey-color">
        <div class="tw-bg-white tw-space-y-1 tw-pt-1 tw-pb-10 tw-drop-shadow-lg">
            <div class="tw-mx-36 tw-mt-5">
                <v-btn prepend-icon="mdi mdi-chevron-left" variant="text" @click="$router.go(-1)" width="8%"
                    color="#082250">
                    <p class="tw-font-bold">Back</p>
                </v-btn>
            </div>
            <div class="tw-flex tw-justify-center tw-h-3/5 tw-max-h-[30rem] tw-min-h-[30rem]">
                <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
                <!-- <v-row no-gutters>
                    <v-col cols="10"></v-col>
                    <v-col cols="2" class="tw-flex tw-justify-center"><v-btn >Report</v-btn></v-col>
                </v-row> -->
                    <v-row no-gutters>
                        <v-col cols="4" class="tw-my-10 web-text-detail" align="center">
                            <v-img src="/image/cover_not_available.jpg" v-show="library.bookDetail.data.file == null" height="360" width="240"></v-img>
                            <v-img class="tw-drop-shadow-xl mb-2" :src="bookCoverPath(library.bookDetail.data.file)" v-show="library.bookDetail.data.file != null" height="360" width="240" cover></v-img>
                            <div class="tw-space-x-1 tw-inline-flex tw-items-center">
                                <v-rating :model-value="library.getStarRating(library.bookDetail.data.bookRating)"
                                    color="#FFB703" density="compact" size="large" half-increments readonly></v-rating>
                                <p class="web-text-rate">{{ library.bookDetail.data.bookRating }}</p>
                            </div>
                        </v-col>
                        <v-col cols="6" class="tw-my-16 web-text-detail tw-space-y-12 ">
                            <div class="tw-space-y-6 ">
                                <p class="web-text-title">{{ library.bookDetail.data.bookName }}</p>
                                <p><span>Author:</span> {{ library.bookDetail.data.author }}</p>
                                <p>Bookmarked by:
                                    <span v-show="library.bookDetail.data.bookTotalBookmarked == null">0</span>
                                    <span v-show="library.bookDetail.data.bookTotalBookmarked != null">{{
                                        library.bookDetail.data.bookTotalBookmarked
                                    }}</span> people
                                </p>
                                <p>Booktype: <v-btn color="#1D419F" v-show="library.bookDetail.bookType != null">{{
                                    library.bookDetail.data.bookType }}</v-btn></p>
                                <p>Genre: </p>
                                <v-btn color="#1D419F">{{ library.bookDetail.data.bookGenre }}</v-btn>
                            </div>

                            <div class="tw-flex tw-justify-center tw-gap-x-12">
                                <v-btn class="text-none" color="#1D419F"><v-icon start
                                        icon="mdi mdi-bookmark"></v-icon>Bookmark</v-btn>
                                <NuxtLink :to="`../../review/create_${library.bookDetail.data.bookId}/`"><v-btn
                                        class="text-none" color="#1D419F"><v-icon start
                                            icon="mdi mdi-pencil-plus"></v-icon>Review</v-btn></NuxtLink>
                            </div>
                        </v-col>
                        <v-col cols="2" class="tw-flex tw-justify-center my-2"><v-btn>Report</v-btn></v-col>
                    </v-row>
                </v-card>
            </div>

            <div class="tw-flex tw-justify-center my-4">
                <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
                    <div class="web-text-detail tw-indent-8 tw-m-4 tw-p-10 tw-bg-white tw-rounded-md tw-min-h-[30rem]">
                        {{ library.bookDetail.data.bookDetail }}
                    </div>
                </v-card>
            </div>
        </div>

        <div class="tw-mt-5 tw-min-h-[24rem]">
            <p class="web-text-header tw-mx-16">Similar Book</p>
            <similarBook />
        </div>

        <div class="tw-flex tw-justify-center tw-bg-white tw-py-10">
            <div class="web-grey-color tw-w-10/12 tw-rounded-lg tw-drop-shadow-lg">
                <div class="tw-px-6 tw-py-8">
                    <p class="web-text-header tw-inline-block tw-align-middle"> Review
                        ({{ reviews.reviewList.data.totalElements ? reviews.reviewList.data.totalElements : 0 }}): </p>
                </div>
                <div class="tw-bg-white tw-mx-5 tw-mb-5 tw-p-4 tw-max-h-[50rem] tw-rounded-lg">
                    <v-container>
                        <v-row no-gutters>
                            <v-col cols="10">
                                <v-text-field label="Search" variant="solo-filled"> </v-text-field>
                            </v-col>
                            <v-col cols="1"><v-btn height="58" class="pa-5" color="#082266" rounded="lg"> Search
                                </v-btn></v-col>
                            <v-col cols="1"><v-btn height="58" class="mx-5 pa-5" color="#082266" rounded="lg"> <v-icon
                                        icon="mdi mdi-filter-variant"></v-icon>
                                    Filter
                                </v-btn></v-col>
                        </v-row>
                        <v-row no-gutters v-show="reviews.reviewList.data.content.length == 0">
                            <v-col cols="12" align="center">
                                <v-img src="/image/rvnotfound.png" width="40%" class="tw-opacity-50"></v-img>
                            </v-col>
                        </v-row>
                        <v-row v-show="reviews.reviewList.data.content.length !== 0">
                            <v-virtual-scroll :items="['']" max-height="35rem">
                                <reviewCard :reviewList="reviews.reviewList.data.content"
                                    :bookId="library.bookDetail.data.bookId" @toggle="togglePopup()"
                                    @set="setDeleteId($event)" />
                            </v-virtual-scroll>
                        </v-row>
                    </v-container>
                    <div v-show="reviews.reviewList.data.content.length !== 0">
                        <v-pagination v-model="page" class="my-4" :length="reviews.reviewList.data.totalPages"
                            :total-visible="7" rounded="20"
                            @update:model-value="reviews.changeReviewPage(route.params.id, page)">
                        </v-pagination>
                    </div>
                </div>
            </div>
            <deleteConfirmPopup class="delete-popup" :dialog="confirmPopup" 
            @toggle="togglePopup()" @delete="reviews.deleteReview(deleteId, library.bookDetail.data.bookId)" />
        </div>
        <footer class="tw-bg-white tw-py-5"></footer>
    </div>
</template>
 
<style scoped>.delete-popup {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}</style>