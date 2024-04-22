<script setup>
import { useReviews } from "~/stores/review";
import { useBooks } from "~/stores/book";
import { useRouter } from "vue-router";
import { ref } from "vue";
// import leaveConfirmPopup from "~/components/popups/leaveConfirmPopup.vue";
import UpdateReviewSuccessPopup from "~/components/reviews/popups/updateReviewSuccessPopup.vue";
import DeleteReviewConfirmPopup from "~/components/reviews/popups/deleteReviewConfirmPopup.vue";
import DeleteReviewSuccessPopup from "~/components/reviews/popups/deleteReviewSuccessPopup.vue";
import LoadingPopup from "~/components/popups/loadingPopup.vue";

const book = useBooks();
const reviews = useReviews();
const route = useRoute();
const router = useRouter();
const roleToken = ref(localStorage.getItem('role'));
const popupStatus = ref('');
const reviewConfirmPopup = ref(false);
// const confirmLeavePopup = ref(false);

// function toggleLeavePopup() {
//   confirmLeavePopup.value = !confirmLeavePopup.value;
// }

// function toggleValidatePopup() {
//   reviews.validate = !reviews.validate;
// }

const rules = {
  required: (value) => !!value || "Field is required",
  limited: (value) => value.length <= 255 || "Max 255 characters",
};

function bookCoverPath(filePath) {
   return filePath = (`../../_nuxt/@fs/${filePath}`)
}

onBeforeRouteLeave(() => {
  if (roleToken.value !== 'GUEST') {
  const spoileFlag = reviews.reviewDetail.data.spoileFlag == 0 ? false : true;
  if (
    reviews.editReview.title !== reviews.reviewDetail.data.reviewTitle ||
    reviews.editReview.detail !== reviews.reviewDetail.data.reviewDetail ||
    reviews.editReview.rating !== reviews.reviewDetail.data.reviewRating ||
    reviews.editReview.spoileFlag !== spoileFlag
  ) {
    if(reviews.leavePopup){
    const shouldShowPopup = confirm("Do you really want to leave?");
    if (shouldShowPopup) {
      return null
    } else {
      next(false); // Prevent leaving the page
    }
  }
}}
});

onBeforeMount(() => {
  if (roleToken.value == 'GUEST') {
    reviews.leavePopup = true;
    router.push(`/UnauthenPage/`)
  }else{
    reviews.leavePopup = true;
}
});

if (roleToken.value == 'GUEST') {
  await reviews.getReviewDetailByGuest(route.params.id);
  reviews.setEditReview();
  await book.getBookDetailByGuest(reviews.editReview.bookId);
  }else{
  // await reviews.setEditReview(route.params.id);
  await reviews.getReviewDetail(route.params.id);
  reviews.setEditReview();
  await book.getBookDetail(reviews.editReview.bookId);
}

</script>

<template>
  <div class="tw-pt-1 tw-pb-10 tw-drop-shadow-lg tw-space-y-1" v-if="roleToken !== 'GUEST'">
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
    <div class="tw-flex tw-justify-center tw-min-h-[25rem] tw-pb-2">
      <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
        <v-row>
          <v-col cols="2">
            <div class="d-flex px-8 my-4" rounded="0">
              <v-img src="/image/cover_not_available.jpg" v-show="book.bookDetail.data.file == null" height="150" width="100"></v-img>
              <v-img class="tw-drop-shadow-xl mb-2" :src="book.bookDetail.data.file" v-show="book.bookDetail.data.file != null" height="150" width="100" cover></v-img>
            </div>
          </v-col>
          <v-col cols="10">
            <div class="web-text-sub tw-my-8 tw-space-y-3">
              <p class="web-text-title">{{ book.bookDetail.data.bookName }}</p>
              <p class="">by {{ book.bookDetail.data.author }}</p>
              <div
                class="tw-space-x-1 tw-inline-flex tw-items-center tw-font-bold"
              >
                My rating:
                <v-rating
                  hover
                  :length="5"
                  :size="32"
                  :model-value="0"
                  color="orange-lighten-1"
                  active-color="#FFBB11"
                  v-model="reviews.editReview.rating"
                />
              </div>
            </div>
          </v-col>
        </v-row>
        <div class="tw-mx-8 tw-space-y-4">
          <v-text-field
            v-model="reviews.editReview.title"
            label="Review Header"
            variant="solo"
            height="100px"
            :rules="[rules.required, rules.limited]"
            counter
          ></v-text-field>
          <v-textarea
            v-model="reviews.editReview.detail"
            label="Review Detail"
            variant="solo"
            rows="5"
            :rules="[rules.required]"
          ></v-textarea>
        </div>
        <div class="tw-mx-8 tw-pb-4">
          <v-checkbox
            label="Hide entire review because of spoilers"
            hide-details
            v-model="reviews.editReview.spoileFlag"
          ></v-checkbox>
        </div>
      </v-card>
    </div>

    <div class="d-flex justify-space-between">
      <div class="justify-start tw-mx-[9rem]">
        <v-btn
        color="red"
        variant="flat"
        @click="reviewConfirmPopup = true,popupStatus = 'delete'"
        >Delete</v-btn>
      </div>
      <div class="justify-end tw-mx-[9rem] tw-space-x-4">
      <v-btn
        color="#1D419F"
        variant="outlined"
        @click="reviews.setEditReview(route.params.id)"
        >reset</v-btn
      >
      <v-btn
        color="#1D419F"
        variant="flat"
        @click="reviews.updateReview(route.params.id),popupStatus = 'update'"
        :disabled="
          reviews.editReview.title == '' ||
          reviews.editReview.detail == '' ||
          reviews.editReview.title.length > 255
        "
        >submit</v-btn
      ></div>
    </div>
    <UpdateReviewSuccessPopup
      v-if="popupStatus == 'update'"
      :dialog="reviews.successfulPopup == 'show'"
      @close="reviews.closeSuccessfulPopup()"
    />
  </div>
  <div v-if="popupStatus == 'delete'">
      <DeleteReviewConfirmPopup
        class="delete-popup"
        :dialog="reviewConfirmPopup"
        @toggle="reviewConfirmPopup = !reviewConfirmPopup,popupStatus == ''"
        @delete="reviews.deleteReview(route.params.id)"
      />
      <DeleteReviewSuccessPopup
        class="delete-popup"
        :dialog="reviews.successfulPopup == 'show'"
        @close="reviews.successfulPopup = 'hide',popupStatus == '',router.go(-1)"
      />
    </div>
    <div v-if="reviews.successfulPopup == 'load'">
    <LoadingPopup />
  </div>
</template>

<style>
.delete-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
