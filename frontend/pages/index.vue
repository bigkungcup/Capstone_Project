<script setup>
import { useLogin } from "../stores/login";
import { useBooks } from "~/stores/book";
// import { useUsers } from "~/stores/user";
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
// const user = useUsers();

// const colors = [
//         'green',
//         'secondary',
//         'yellow darken-4',
//         'red lighten-2',
//         'orange darken-1',
//       ]
const  slides = [
        '/image/bookbanner1.jpg',
        '/image/bookbanner2.png',
        '/image/bookbanner3.png',
      ]
// const bookcovers = [
//       '/image/foryou1.png',
//       '/image/foryou2.jpg',
//       '/image/foryou3.jpg',
//       '/image/foryou4.jpg',
//       '/image/foryou5.png',
//       '/image/foryou6.png',
//       '/image/foryou7.png',
//       '/image/foryou8.jpg',
//       '/image/foryou9.jpg',
//       '/image/foryou9.png',

//       ]
const path = '/ej2'

// const bookPath = ref('_nuxt/@fs\\Files\\Uploads\\TEST\\foryou2.jpg')

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


onBeforeMount(async () => {
  await book.getRecommendBookList();
  await book.getMostviewBookList();
  await book.getNewBookList();
  await reviews.getNewReviewList();
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
    <!-- <div class="tw-text-4xl tw-text-[#082266] tw-font-extrabold tw-py-8"> Recommend </div> -->
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
    <!-- <v-img :src="bookPath" width="180" alt="aaa"/> -->
  <!-- <v-sheet class="mx-auto tw-p-4" elevation="2" width="100%" height="100%" color="blue" style="border-radius:20px;" >
      <v-slide-group  show-arrows>
        <v-slide-group-item>
          <div class="tw-flex tw-items-center tw-text-7xl" style="font-family: 'Rampart One';"> For You </div>
          <v-card color="white" class="mx-4 " height="100%" width="20rem" v-for="(cover, index) in bookcovers" :key="index" >
            <div class="d-flex fill-height align-center justify-center tw-p-8" >
              <v-img :src="`${path}${cover}`" height="100%" cover></v-img>
            </div>
          </v-card>
        </v-slide-group-item>
      </v-slide-group>
    </v-sheet> -->
  </div>

  </div>

<!-- <div class="my-5">
  <v-sheet class="mx-auto" elevation="2" width="100%" height="100%" >
      <v-slide-group class="pa-4" show-arrows cycle :interval="4000">
        <v-slide-group-item>
          <v-card color="grey-lighten-1" class="ma-4" height="100%" width="40rem" v-for="(cover, index) in slides" :key="index" >
            <div class="d-flex fill-height align-center justify-center" >
              <v-img :src="cover" height="100%" cover></v-img>
            </div>
          </v-card>
        </v-slide-group-item>
      </v-slide-group>
    </v-sheet>
</div> -->

</template>

<style scoped>

@import url('https://fonts.googleapis.com/css2?family=Rampart+One&display=swap');
</style>
  