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
const idToken = ref(localStorage.getItem("id"));

const  slides = [
        '/image/bookbanner1.jpg',
        '/image/bookbanner2.png',
        '/image/bookbanner3.jpg',
      ]
const path = '/ej2'

if(accessToken.value == undefined || accessToken.value == null){
  login.resetToken();
  login.roleToken = localStorage.getItem("role");
}else{
  await login.getProfile();
  login.roleToken = localStorage.getItem("role");
}

async function likeReviews(reviewId, likeStatus) {
  let status = {
    userId: login.getIdToken(),
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
  await book.getMostviewBookList();
  await book.getNewBookList();
  if(login.roleToken == "GUEST"){
    await reviews.getNewReviewListByGuest();
  }else{
    await book.getRecommendBookList();
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
    <div v-if="login.roleToken == 'USER'">
      <Recommend :recommendBookList="book.recommendBookList.data"/>
    </div>
    <div>
      <MostView :mostviewBookList="book.mostviewBookList.data"/>
    </div>
    <div>
      <NewBook :newBookList="book.newBookList.data"/>
    </div>
    <div>
      <NewReview :newReviewList="reviews.newReviewList.data" 
                @like="likeReviews($event.reviewId, $event.likeStatus)"                   
                @update="updatelikeReviews(
                      $event.reviewId,
                      $event.likeStatus,
                      $event.likeStatusId
                    )
                  "/>
    </div>
    <div>
      <Other :otherBookList="book.otherBookList.data"/>
    </div>
  </div>
  </div>
</template>

<style scoped>

@import url('https://fonts.googleapis.com/css2?family=Rampart+One&display=swap');
</style>
  