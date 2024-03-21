<script setup>
import { useLogin } from "../stores/login";
import { useBooks } from "~/stores/book";
import { useReviews } from "~/stores/review";
import Recommend from "~/components/home/recommend.vue";
import MostView from "~/components/home/mostView.vue";
import NewBook from "~/components/home/newBook.vue";
import NewReview from "~/components/home/newReview.vue";
import Other from "~/components/home/other.vue";

const accessToken = ref(useCookie("accessToken"));
const login = useLogin();
const book = useBooks();
const reviews = useReviews();
const roleToken = ref(localStorage.getItem("role"));
const idToken = ref(localStorage.getItem("id"));

const  slides = [
        '/image/bookbanner1.jpg',
        '/image/bookbanner2.png',
        '/image/bookbanner3.png',
      ]
const path = '/ej2'

if(accessToken.value == undefined){
  login.resetToken();
}else{
  login.getProfile();
}

async function likeReviews(reviewId, likeStatus) {
  let status = {
    userId: idToken.value,
    reviewId: reviewId,
    likeStatus: likeStatus,
  };
  await reviews.createLike(status);
  await reviews.getNewReviewList();
}

async function updatelikeReviews(reviewId, likeStatus, likeStatusId) {
  let status = {
    userId: idToken.value,
    reviewId: reviewId,
    likeStatusId: likeStatusId,
    likeStatus: likeStatus,
  };
  await reviews.updateLike(status);
  await reviews.getNewReviewList();
}

onBeforeMount(async () => {
  await book.getRecommendBookList();
  await book.getMostviewBookList();
  await book.getNewBookList();
  if(roleToken.value == "GUEST"){
    await reviews.getNewReviewListByGuest();
  }else{
    reviews.clearNewReviewList();
    await reviews.getNewReviewList();
  }
  await book.getOtherBookList();
});
</script>

<template>
  <div>
    <v-card width="100%" height="100%" class="mx-auto">
      <v-carousel :continuous="false" :show-arrows="false" hide-delimiter-background delimiter-icon="mdi-square" cycle
        :interval="4000">
        <v-carousel-item v-for="(slide, index) in slides" :key="index">
          <v-img :src="`${path}${slide}`" height="100%" cover></v-img>
        </v-carousel-item>
      </v-carousel>
    </v-card>
  </div>
  <div>
    
  <div class="ma-8">
    <div v-if="book.recommendBookList.data.length !== 0">
      <Recommend :recommendBookList="book.recommendBookList.data"/>
    </div>
    <div v-if="book.mostviewBookList.data.length !== 0">
      <MostView :mostviewBookList="book.mostviewBookList.data"/>
    </div>
    <div v-if="book.newBookList.data.length !== 0">
      <NewBook :newBookList="book.newBookList.data"/>
    </div>
    <div v-if="reviews.newReviewList.data.length !== 0">
      <NewReview :newReviewList="reviews.newReviewList.data" 
                @like="likeReviews($event.reviewId, $event.likeStatus)"                   
                @update="updatelikeReviews(
                      $event.reviewId,
                      $event.likeStatus,
                      $event.likeStatusId
                    )
                  "/>
    </div>
    <div v-if="book.otherBookList.data.length !== 0">
      <Other :otherBookList="book.otherBookList.data"/>
    </div>
  </div>
  </div>
</template>

<style scoped>

@import url('https://fonts.googleapis.com/css2?family=Rampart+One&display=swap');
</style>
  