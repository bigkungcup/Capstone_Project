<script setup>
import { useReviews } from "~/stores/review";
import { useBooks } from "~/stores/book";
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import leaveConfirmPopup from "~/components/popups/leaveConfirmPopup.vue";
import CreateReviewSuccessPopup from "~/components/reviews/popups/createReviewSuccessPopup.vue";
import LoadingPopup from "~/components/popups/loadingPopup.vue";

const book = useBooks();
const reviews = useReviews();
const route = useRoute();
const confirmLeavePopup = ref(false);
const roleToken = ref(localStorage.getItem('role'));
const router = useRouter();

function toggleLeavePopup() {
  confirmLeavePopup.value = !confirmLeavePopup.value;
}

// function toggleValidatePopup() {
//   reviews.validate = !reviews.validate;
// }

const rules = {
  required: (value) => !!value || "Field is required",
  limited: (value) => value.length <= 255 || "Max 255 characters",
};

function setBookId() {
  reviews.newReview.bookId = route.params.id;
}

function bookCoverPath(filePath) {
   return filePath = (`../../_nuxt/@fs/${filePath}`)
}

onBeforeRouteLeave(() => {
  if (
    reviews.newReview.title !== "" ||
    reviews.newReview.detail !== "" 
  ) {
    if(reviews.leavePopup){
    const shouldShowPopup = confirm("Do you really want to leave?");
    if (shouldShowPopup) {
      return null
    } else {
      next(false); // Prevent leaving the page
    }
  }
}});

onBeforeMount(() => {
if (roleToken.value == 'USER') {
  reviews.clearNewReview();
  reviews.leavePopup = true;
  setBookId();
}else{
  reviews.clearNewReview();
  reviews.leavePopup = true;
  router.push(`/UnauthenPage/`)
}
});

if (roleToken.value == 'USER') {
  await book.getBookDetail(route.params.id);
}else{
  await book.getBookDetailByGuest(route.params.id);
}



</script>

<template>
  <div class="tw-pt-1 tw-pb-10 tw-drop-shadow-lg tw-space-y-1" v-show="roleToken == 'USER'">
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
                  v-model="reviews.newReview.rating"
                />
              </div>
            </div>
          </v-col>
        </v-row>
        <div class="tw-mx-8 tw-space-y-4">
          <v-text-field
            v-model="reviews.newReview.title"
            label="Review Title"
            variant="solo"
            height="100px"
            clearable
            :rules="[rules.required, rules.limited]"
            counter
          ></v-text-field>
          <v-textarea
            v-model="reviews.newReview.detail"
            label="Review Detail"
            variant="solo"
            rows="5"
            clearable
            :rules="[rules.required]"
          ></v-textarea>
        </div>
        <div class="tw-mx-8 tw-pb-4">
          <v-checkbox
            label="Hide entire review because of spoilers"
            hide-details
            v-model="reviews.newReview.spoileFlag"
          ></v-checkbox>
        </div>
      </v-card>
    </div>

    <div class="d-flex justify-end tw-mx-[10rem] tw-space-x-4">
      <v-btn
        color="#1D419F"
        variant="outlined"
        @click="reviews.clearNewReview(), setBookId()"
        >clear</v-btn
      >
      <v-btn
        color="#1D419F"
        variant="flat"
        @click="reviews.createReview(reviews.newReview)"
        :disabled="
          reviews.newReview.title == '' ||
          reviews.newReview.detail == '' ||
          reviews.newReview.title.length > 255
        "
        >submit</v-btn
      >
    </div>
    <CreateReviewSuccessPopup
      :dialog="reviews.successfulPopup == 'show'"
      @close="reviews.closeSuccessfulPopup()"
    />
    <leaveConfirmPopup
      :dialog="confirmLeavePopup"
      @toggle="toggleLeavePopup()"
      @back="$router.go(-1)"
    />
    <div v-if="reviews.successfulPopup == 'load'">
    <LoadingPopup />
  </div>
  </div>
</template>

<style></style>
