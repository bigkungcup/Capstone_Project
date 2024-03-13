<script setup>
defineProps({
    similarBookList: {
        type: Array,
        require: true,
  }
});

</script>
 
<template>
    <v-slide-group>
        <v-slide-group-item>
          <v-card color="white" class="mx-4 my-2" height="20rem" width="12rem" v-for="book in similarBookList" :to="`/book/${book.bookId}/`">
            <div class="d-flex fill-height " >
              <v-img :src="book.file" height="100%" cover></v-img>
            </div>
          </v-card>
        </v-slide-group-item>
      </v-slide-group>
</template>
 
<style>

</style>