<script setup>
const bookcovers = [
      '/image/foryou1.png',
      '/image/foryou2.jpg',
      '/image/foryou3.jpg',
      '/image/foryou4.jpg',
      '/image/foryou5.png',
      '/image/foryou6.png',
      '/image/foryou7.png',
      '/image/foryou8.jpg',
      '/image/foryou9.jpg',
      '/image/foryou9.png',

      ]
</script>

<template>
  <div>
    <div class="tw-text-4xl tw-text-[#082266] tw-font-extrabold tw-py-8">
      Recommend
    </div>
    <!-- <v-img :src="bookPath" width="180" alt="aaa" /> -->
    <v-sheet class="mx-auto tw-p-4" elevation="2" width="100%" height="100%" color="#3157BB" style="border-radius:20px;" >
      <v-slide-group  show-arrows>
        <v-slide-group-item>
          <div class="tw-flex tw-items-center tw-text-7xl" style="font-family: 'Rampart One';"> For You </div>
          <v-card color="white" class="mx-4 " height="100%" width="20rem" v-for="(cover, index) in bookcovers" :key="index" >
            <div class="d-flex fill-height align-center justify-center tw-p-8" >
              <v-img :src="cover" height="100%" cover></v-img>
            </div>
          </v-card>
        </v-slide-group-item>
      </v-slide-group>
    </v-sheet>
  </div>
</template>

<style></style>
