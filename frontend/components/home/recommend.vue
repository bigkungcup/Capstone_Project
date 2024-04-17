<script setup>
defineProps({
    recommendBookList: {
        type: Array,
        require: true,
  }
});
</script>

<template>
  <div>
    <div class="tw-text-4xl tw-text-[#082266] tw-font-extrabold tw-py-8">
      Recommend
    </div>
    <!-- <v-img :src="bookPath" width="180" alt="aaa" /> -->
    <v-sheet class="mx-auto tw-p-4" elevation="2" width="100%" height="100%" color="#3157BB" style="border-radius:20px;">
      <v-row no-gutters>
      <v-col cols="3" class="tw-flex tw-items-center tw-justify-center" align="center">
      <div class="tw-text-7xl" style="font-family: 'Rampart One';"> For You </div>
      </v-col>
      <v-col cols="9" class="" align="center">
      <v-slide-group  show-arrows v-if="recommendBookList.length != 0">
        <v-slide-group-item>
          <v-card color="white" class="mx-4" height="100%" width="18rem" rounded="xl" v-for="book in recommendBookList" :to="`/book/${book.bookId}/`">
            <div class="align-center justify-center tw-px-6 tw-pt-4 tw-border-8 tw-border-[#A5BEFF] tw-rounded-3xl" >
              <v-img class="tw-drop-shadow-lg tw-border-2 tw-border-[#082266]" :src="book.file" height="320" width="180" cover></v-img>
              <div class="tw-space-x-1 tw-inline-flex tw-items-center">
              <v-rating
                  :model-value="0.5 * Math.floor(2 * book.bookRating)"
                  color="#FFB703"
                  density="compact"
                  size="large"
                  half-increments
                  readonly
                ></v-rating>
                <p class="web-text-rate">{{ book.bookRating }}</p>
              </div>
            </div>
          </v-card>
        </v-slide-group-item>
      </v-slide-group>
      <div class="tw-h-[20rem] tw-space-y-1 py-8" v-if="recommendBookList.length == 0">
        <v-img src="/image/recommend_not_found.png" height="180" width="160" cover></v-img>
        <div class="tw-text-center user-text-title">
        <p>No recommend book now.</p>
        <p>Let’s explore some more book!</p>
        </div>
      </div>
    </v-col>
    </v-row>
    </v-sheet>
  </div>
</template>

<style></style>
