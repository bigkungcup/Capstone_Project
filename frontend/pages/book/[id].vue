<script setup>
import deleteReviewConfirmPopup from "~/components/reviews/popups/deleteReviewConfirmPopup.vue";
import deleteBookConfirmPopup from "~/components/books/popups/deleteBookConfirmPopup.vue";
import reviewCard from "~/components/reviews/reviewCard.vue";
import { useBooks } from "~/stores/book";
import { useReviews } from "~/stores/review";
import { ref } from "vue";
import similarBook from "~/components/similarBook.vue";
import { mergeProps } from "vue";
import deleteBookSuccessPopup from "~/components/books/popups/createBookSuccessPopup.vue";
import deleteReviewSuccessPopup from "~/components/reviews/popups/deleteReviewSuccessPopup.vue";
import deleteBookFailPopup from "~/components/books/popups/deleteBookFailPopup.vue";

const library = useBooks();
const reviews = useReviews();
const route = useRoute();
const page = ref(1);
const deleteId = ref(0);
const bookConfirmPopup = ref(false);
const reviewConfirmPopup = ref(false);

function setDeleteId(id) {
  deleteId.value = id;
}

function toggleBookConfirmPopup() {
  bookConfirmPopup.value = !bookConfirmPopup.value;
}

function toggleBookSuccessfulPopup() {
  library.successfulPopup = !library.successfulPopup;
}

function toggleReviewSuccessfulPopup() {
  reviews.successfulPopup = !reviews.successfulPopup;
}

function toggleBookFailPopup() {
  library.failPopup = !library.failPopup;
}

function toggleReviewConfirmPopup() {
  reviewConfirmPopup.value = !reviewConfirmPopup.value;
}

function bookCoverPath(filePath) {
  return (filePath = `../../_nuxt/@fs/${filePath}`);
}

await library.getBookDetail(route.params.id);
await reviews.getReview(route.params.id, 0);

</script>

<template>
  <div class="web-grey-color">
    <div class="tw-bg-white tw-space-y-1 tw-pt-1 tw-pb-10 tw-drop-shadow-lg">
      <div class="tw-mx-36 tw-mt-5">
        <v-btn
          prepend-icon="mdi mdi-chevron-left"
          variant="text"
          @click="$router.go(-1)"
          width="8%"
          color="#082250"
        >
          <p class="tw-font-bold">Back</p>
        </v-btn>
      </div>
      <div
        class="tw-flex tw-justify-center tw-h-3/5 tw-max-h-[30rem] tw-min-h-[30rem]"
      >
        <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
          <v-row no-gutters>
            <v-col cols="4" class="tw-my-10 web-text-detail" align="center">
              <v-img
                src="/image/cover_not_available.jpg"
                v-show="library.bookDetail.data.file == null"
                height="360"
                width="240"
              ></v-img>
              <v-img
                class="tw-drop-shadow-xl mb-2"
                :src="bookCoverPath(library.bookDetail.data.file)"
                v-show="library.bookDetail.data.file != null"
                height="360"
                width="240"
                cover
              ></v-img>
              <div class="tw-space-x-1 tw-inline-flex tw-items-center">
                <v-rating
                  :model-value="
                    library.getStarRating(library.bookDetail.data.bookRating)
                  "
                  color="#FFB703"
                  density="compact"
                  size="large"
                  half-increments
                  readonly
                ></v-rating>
                <p class="web-text-rate">
                  {{ library.bookDetail.data.bookRating }}
                </p>
              </div>
            </v-col>
            <v-col cols="7" class="tw-my-16 web-text-detail tw-space-y-12">
              <div class="tw-space-y-6">
                <p class="web-text-title">
                  {{ library.bookDetail.data.bookName }}
                </p>
                <p><span>Author:</span> {{ library.bookDetail.data.author }}</p>
                <p>
                  Bookmarked by:
                  <span
                    v-show="library.bookDetail.data.bookTotalBookmarked == null"
                    >0</span
                  >
                  <span
                    v-show="library.bookDetail.data.bookTotalBookmarked != null"
                    >{{ library.bookDetail.data.bookTotalBookmarked }}</span
                  >
                  people
                </p>
                <p>
                  Booktype:
                  <v-btn
                    color="#1D419F"
                    v-show="library.bookDetail.bookType != null"
                    >{{ library.bookDetail.data.bookType }}</v-btn
                  >
                </p>
                <p>Genre:</p>
                <v-btn color="#1D419F">{{
                  library.bookDetail.data.bookGenre
                }}</v-btn>
              </div>

              <div class="tw-flex tw-justify-center tw-gap-x-12">
                <v-btn class="text-none" color="#1D419F"
                  ><v-icon start icon="mdi mdi-bookmark"></v-icon
                  >Bookmark</v-btn
                >
                <NuxtLink
                  :to="`../../review/create_${library.bookDetail.data.bookId}/`"
                  ><v-btn class="text-none" color="#1D419F"
                    ><v-icon start icon="mdi mdi-pencil-plus"></v-icon
                    >Review</v-btn
                  ></NuxtLink
                >
              </div>
            </v-col>
            <v-col cols="1" class="tw-flex tw-justify-center my-2">
              <span class="text-center">
                <v-menu>
                  <template v-slot:activator="{ props: menu }">
                    <v-tooltip location="top">
                      <template v-slot:activator="{ props: tooltip }">
                        <v-icon
                          icon="mdi mdi-dots-horizontal"
                          style="font-size: 42px"
                          v-bind="mergeProps(menu, tooltip)"
                        ></v-icon>
                      </template>
                      <span>More</span>
                    </v-tooltip>
                  </template>
                  <v-list>
                    <v-list-item
                      :to="`../../book/update_${library.bookDetail.data.bookId}/`"
                    >
                      <v-list-item-title class="web-text-detail tw-space-x-2"
                        ><v-icon icon="mdi mdi-pencil-outline"></v-icon
                        ><span>Edit this book</span></v-list-item-title
                      >
                    </v-list-item>
                    <v-list-item
                      class="hover:tw-bg-zinc-300/20 tw-cursor-pointer"
                    >
                      <v-list-item-title
                        class="web-text-detail"
                        @click="toggleBookConfirmPopup()"
                      >
                        <v-list-item-title class="web-text-detail tw-space-x-2"
                          ><v-icon icon="mdi mdi-trash-can-outline"></v-icon
                          ><span>Delete this book</span></v-list-item-title
                        >
                      </v-list-item-title>
                    </v-list-item>
                    <v-list-item>
                      <v-list-item-title class="web-text-detail tw-space-x-2"
                        ><v-icon icon="mdi mdi-flag-variant-outline"></v-icon
                        ><span>Report this book</span></v-list-item-title
                      >
                    </v-list-item>
                  </v-list>
                </v-menu>
              </span>
            </v-col>
          </v-row>
        </v-card>
      </div>

      <div class="tw-flex tw-justify-center my-4">
        <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
          <div
            class="web-text-detail tw-indent-8 tw-m-4 tw-p-10 tw-bg-white tw-rounded-md tw-min-h-[30rem]"
          >
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
          <p class="web-text-header tw-inline-block tw-align-middle">
            Review ({{
              reviews.reviewList.data.totalElements
                ? reviews.reviewList.data.totalElements
                : 0
            }}):
          </p>
        </div>
        <div
          class="tw-bg-white tw-mx-5 tw-mb-5 tw-p-4 tw-max-h-[50rem] tw-rounded-lg"
        >
          <v-container>
            <v-row no-gutters>
              <v-col cols="10">
                <v-text-field label="Search" variant="solo-filled">
                </v-text-field>
              </v-col>
              <v-col cols="1"
                ><v-btn height="58" class="pa-5" color="#082266" rounded="lg">
                  Search
                </v-btn></v-col
              >
              <v-col cols="1"
                ><v-btn
                  height="58"
                  class="mx-5 pa-5"
                  color="#082266"
                  rounded="lg"
                >
                  <v-icon icon="mdi mdi-filter-variant"></v-icon>
                  Filter
                </v-btn></v-col
              >
            </v-row>
            <v-row
              no-gutters
              v-show="reviews.reviewList.data.content.length == 0"
            >
              <v-col cols="12" align="center">
                <v-img
                  src="/image/rvnotfound.png"
                  width="40%"
                  class="tw-opacity-50"
                ></v-img>
              </v-col>
            </v-row>
            <v-row v-show="reviews.reviewList.data.content.length !== 0">
              <v-virtual-scroll :items="['']" max-height="35rem">
                <reviewCard
                  :reviewList="reviews.reviewList.data.content"
                  :bookId="library.bookDetail.data.bookId"
                  @toggle="toggleReviewConfirmPopup()"
                  @set="setDeleteId($event)"
                />
              </v-virtual-scroll>
            </v-row>
          </v-container>
          <div v-show="reviews.reviewList.data.content.length !== 0">
            <v-pagination
              v-model="page"
              class="my-4"
              :length="reviews.reviewList.data.totalPages"
              :total-visible="7"
              rounded="20"
              @update:model-value="
                reviews.changeReviewPage(route.params.id, page)
              "
            >
            </v-pagination>
          </div>
        </div>
      </div>
      <deleteReviewConfirmPopup
        class="delete-popup"
        :dialog="reviewConfirmPopup"
        @toggle="toggleReviewConfirmPopup()"
        @delete="reviews.deleteReview(deleteId, library.bookDetail.data.bookId)"
      />
      <deleteBookConfirmPopup
        class="delete-popup"
        :dialog="bookConfirmPopup"
        @toggle="toggleBookConfirmPopup()"
        @delete="library.deleteBook(route.params.id)"
      />
      <deleteBookFailPopup
        class="delete-popup"
        :dialog="library.failPopup"
        @close="toggleBookFailPopup()"
      />
      <deleteBookSuccessPopup
        class="delete-popup"
        :dialog="library.successfulPopup"
        @close="toggleBookSuccessfulPopup()"
      />
      <deleteReviewSuccessPopup 
        class="delete-popup"
        :dialog="reviews.successfulPopup"
        @close="toggleReviewSuccessfulPopup()"
      />
    </div>
    <footer class="tw-bg-white tw-py-5"></footer>
  </div>
</template>

<style scoped>
.delete-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
