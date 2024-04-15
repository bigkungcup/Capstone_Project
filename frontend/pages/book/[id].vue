<script setup>
import { ref } from "vue";
import { mergeProps } from "vue";
import { useBooks } from "~/stores/book";
import { useUsers } from "~/stores/user";
import { useReviews } from "~/stores/review";
import { useNotifications } from "~/stores/notification";
import SimilarBook from "~/components/books/similarBook.vue";
import SimilarBookNotFound from "~/components/books/similarBookNotFound.vue";
import reviewCard from "~/components/reviews/reviewCard.vue";
import ReviewNotFound from "~/components/reviews/reviewNotFound.vue";
import deleteBookSuccessPopup from "~/components/books/popups/deleteBookSuccessPopup.vue";
import deleteReviewSuccessPopup from "~/components/reviews/popups/deleteReviewSuccessPopup.vue";
import deleteBookFailPopup from "~/components/books/popups/deleteBookFailPopup.vue";
import deleteReviewConfirmPopup from "~/components/reviews/popups/deleteReviewConfirmPopup.vue";
import deleteBookConfirmPopup from "~/components/books/popups/deleteBookConfirmPopup.vue";
import CreateReportPopup from "~/components/reports/createReportPopup.vue";
import ReportSuccessPopup from "~/components/reports/popups/reportSuccessPopup.vue";

const library = useBooks();
const reviews = useReviews();
const user = useUsers();
const noti = useNotifications();
const route = useRoute();
const page = ref(reviews.reviewPage+1);
const deleteId = ref(0);
const bookConfirmPopup = ref(false);
const reviewConfirmPopup = ref(false);

const roleToken = ref(localStorage.getItem("role"));
const idToken = ref(localStorage.getItem("id"));
const sortList = [
  {
    id: 1,
    Name: "Newest",
    value: "desc",
  },
  {
    id: 2,
    Name: "Oldest",
    value: "asc",
  },
  {
    id: 3,
    Name: "Total Likes",
    value: "reviewTotalLike",
  },
];
const starRate = [{
  title: 'All',
  value: null
},{
  title: '5',
  value: 5
},{
  title: '4',
  value: 4
},{
  title: '3',
  value: 3
},{
  title: '2',
  value: 2
},{
  title: '1',
  value: 1
}];

function setDeleteId(id) {
  deleteId.value = id;
}

function toggleBookConfirmPopup() {
  bookConfirmPopup.value = !bookConfirmPopup.value;
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

async function likeReviews(reviewId, likeStatus) {
  let status = {
    userId: idToken.value,
    reviewId: reviewId,
    likeStatus: likeStatus,
  };
  await reviews.createLike(status);
  await reviews.getReview(route.params.id);
}

async function updatelikeReviews(reviewId, likeStatus, likeStatusId) {
  let status = {
    userId: idToken.value,
    reviewId: reviewId,
    likeStatusId: likeStatusId,
    likeStatus: likeStatus,
  };
  await reviews.updateLike(status);
  await reviews.getReview(route.params.id);
}

function handleSelectionChange() {

  if (roleToken.value == "GUEST") {
    reviews.getReviewByGuest(route.params.id);
    // result.value = library.bookList.data.totalElements ? library.bookList.data.totalElements : 0
  } else {
    reviews.getReview(route.params.id);
    // result.value = library.bookList.data.totalElements ? library.bookList.data.totalElements : 0
  }
}

function handleStarRate(value) {
  reviews.filterReview = value
  page.value = 1;
  reviews.reviewPage = 0;
  if(roleToken.value == "GUEST"){
    reviews.getReviewByGuest(route.params.id);
  }else{
    reviews.getReview(route.params.id);
  }
}

async function handleFollow(userId) {
  await user.createFollower(userId)
  reviews.getReview(route.params.id)
}

async function handleUnfollow(userId) {
  await user.deleteFollower(userId)
  reviews.getReview(route.params.id)
}


if (roleToken.value == "GUEST") {
  await library.getBookDetailByGuest(route.params.id);
  library.getSimilarBook(library.bookDetail.data.booktype.booktypeId, route.params.id);
  reviews.clearReviewList();
  await reviews.getReviewByGuest(route.params.id);
} else {
  await library.getBookDetail(route.params.id);
  library.getSimilarBook(library.bookDetail.data.booktype.booktypeId, route.params.id);
  reviews.clearReviewList();
  noti.clearReportProblem();
  await reviews.getReview(route.params.id);
}

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
                :src="library.bookDetail.data.file"
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
                  {{ 0.5 * (2 * library.bookDetail.data.bookRating).toFixed(1) }}
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
                  <!-- <span
                    v-show="library.bookDetail.data.bookTotalBookmarked == null"
                    >0</span
                  > -->
                  <span
                    
                    >{{ library.bookDetail.data.bookTotalBookmark }}</span
                  >
                  people
                </p>
                <p>
                  Booktype:
                  <v-btn color="#1D419F">{{
                    library.bookDetail.data.booktype.booktypeName
                  }}</v-btn>
                </p>
                <div class="tw-space-x-2 tw-space-y-2 tw-h-[5rem]">
                  <span>Tags:</span>
                  <v-chip
                    variant="elevated"
                    color="#1D419F"
                    v-show="library.bookDetail.data.bookTagList[0] != ''"
                    v-for="tag in library.bookDetail.data.bookTagList"
                    >{{ tag }}</v-chip
                  >
                </div>
              </div>

              <div
                class="tw-flex tw-justify-center tw-gap-x-12"
                v-show="roleToken == 'USER'"
              >
                <v-btn
                  class="text-none"
                  color="#1D419F"
                  v-if="library.bookmarkedStatus == null"
                  @click="
                    library.createBookmark(library.bookDetail.data.bookId),
                      (library.bookmarkedStatus = 1)
                  "
                >
                  <v-icon start icon="mdi mdi-bookmark-outline"></v-icon>
                  Bookmark
                </v-btn>
                <v-btn
                  class="text-none"
                  color="#3157BB"
                  v-if="library.bookmarkedStatus != null"
                  @click="
                    library.deleteBookmarkByBookId(
                      library.bookDetail.data.bookId
                    ),
                      (library.bookmarkedStatus = null)
                  "
                >
                  <v-icon start icon="mdi mdi-bookmark-check"></v-icon>
                  Bookmarked
                </v-btn>
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
              <span class="text-center" v-show="roleToken !== 'GUEST'">
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
                      v-show="roleToken == 'ADMIN'"
                    >
                      <v-list-item-title class="web-text-detail tw-space-x-2"
                        ><v-icon icon="mdi mdi-pencil-outline"></v-icon
                        ><span>Edit this book</span></v-list-item-title
                      >
                    </v-list-item>
                    <v-list-item
                      class="hover:tw-bg-zinc-300/20 tw-cursor-pointer"
                      v-show="roleToken == 'ADMIN'"
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
                    <v-list-item class="hover:tw-bg-zinc-300/20 tw-cursor-pointer" v-show="roleToken == 'USER'">
                      <v-list-item-title class="web-text-detail tw-space-x-2"
                      @click="noti.handleReportBook(library.bookDetail.data.bookId)"
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
      <div class="tw-mx-16" v-if="library.similarBookList.data.length != 0"><SimilarBook :similarBookList="library.similarBookList.data"/></div>
      <div class="tw-mx-16" v-if="library.similarBookList.data.length == 0"><SimilarBookNotFound /></div>
    </div>

    <div class="tw-flex tw-justify-center tw-bg-white tw-py-10">
      <div class="web-grey-color tw-w-10/12 tw-rounded-lg tw-drop-shadow-lg">
        <div class="tw-px-6 tw-py-8">
          <p class="web-text-header tw-inline-block tw-align-middle">
            <!-- Review ({{
              reviews.reviewList.data.totalElements
                ? reviews.reviewList.data.totalElements
                : 0
            }}):  -->
            Reviews :
          </p>
        </div>
        <div
          class="tw-bg-white tw-mx-5 tw-mb-5 tw-p-4 tw-max-h-[50rem] tw-rounded-lg"
        >
          <v-container>
            <v-row>
              <v-col cols="10">
                <v-item-group mandatory v-model="reviews.filterReview">
                  <v-row no-gutters>
                    <v-col v-for="star in starRate" cols="12" md="2" class="py-1">
                      <v-item>
                        <v-btn
                          color="#1D419F"
                          height="50"
                          width="95%"
                          :variant="reviews.filterReview == star.value ? 'elevated' : 'outlined'"
                          @click="handleStarRate(star.value)"
                        >
                          <v-icon icon="mdi mdi-star" color="#FFBB11"></v-icon>
                          <span v-if="reviews.filterReview != star.value">{{ star.title }}</span>
                          <span v-if="reviews.filterReview == star.value">{{ star.title }} ({{ reviews.reviewList.data.totalElements ? reviews.reviewList.data.totalElements : 0 }})</span>
                        </v-btn>
                      </v-item>
                    </v-col>
                  </v-row>
                </v-item-group>
              </v-col>
              <v-col cols="2">
                <v-select
                  label="SORT BY: "
                  class="tw-font-bold tw-text-white tw-text-xs"
                  v-model="reviews.sortReview"
                  :items="sortList"
                  item-title="Name"
                  item-value="value"
                  variant="solo-filled"
                  bg-color="#082266"
                  rounded="lg"
                  @update:model-value="handleSelectionChange()"
                ></v-select>
              </v-col>
            </v-row>
            <v-row
              no-gutters
              v-show="reviews.reviewList.data.content.length == 0"
            >
            <ReviewNotFound />
            </v-row>
            <v-row v-show="reviews.reviewList.data.content.length !== 0">
              <v-virtual-scroll :items="['']" max-height="35rem">
                <reviewCard
                  :reviewList="reviews.reviewList.data.content"
                  :bookId="library.bookDetail.data.bookId"
                  @toggle="toggleReviewConfirmPopup()"
                  @set="setDeleteId($event)"
                  @like="likeReviews($event.reviewId, $event.likeStatus)"
                  @follow="handleFollow($event)"
                  @unfollow="handleUnfollow($event)"
                  @update="
                    updatelikeReviews(
                      $event.reviewId,
                      $event.likeStatus,
                      $event.likeStatusId
                    )
                  "
                  @report="noti.handleReportReview($event)"
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
        @close="library.closeSuccessfulPopup()"
      />
      <deleteReviewSuccessPopup
        class="delete-popup"
        :dialog="reviews.successfulPopup"
        @close="toggleReviewSuccessfulPopup()"
      />
  
      <!-- Create Report Popup -->
      <div v-if="noti.reportStatus">
      <CreateReportPopup class="report-popup" 
      :title="noti.reportProblem.reportType == 'book' ? noti.reportBookList : noti.reportReviewList" 
      :report="noti.reportProblem" 
      @cancel="noti.reportStatus = false,noti.clearReportProblem()" 
      @submit="noti.createReport()" />
      </div>
      <div v-if="noti.successfulPopup">
      <ReportSuccessPopup class="report-popup" @close="noti.successfulPopup = false"/>
    </div>
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
.report-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
