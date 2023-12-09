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
const path = "/ej2"
</script>
 
<template>
  <!-- <v-sheet class="mx-auto tw-p-4" elevation="2" width="100%" height="100%"  style="border-radius:20px;" > -->
      <v-slide-group  show-arrows>
        <v-slide-group-item>
          <v-card color="white" class="mx-4 my-2" height="20rem" width="12rem" v-for="(cover, index) in bookcovers" :key="index" >
            <div class="d-flex fill-height " >
              <v-img :src="`${path}${cover}`" height="100%" cover></v-img>
            </div>
          </v-card>
        </v-slide-group-item>
      </v-slide-group>
    <!-- </v-sheet> -->
</template>
 
<style>

</style>