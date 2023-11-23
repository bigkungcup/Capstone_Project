<script setup>
import { useBooks } from '~/stores/book'


const library = useBooks();

await library.getLibrary();

</script>

<template>
  <div class="">
    <!-- <v-container class="ma-0 ml-10"> -->
    <v-container>
      <v-row no-gutters>
        <v-col cols="10">
          <v-text-field label="Search" variant="solo-filled"> </v-text-field>
        </v-col>
        <!-- <v-text-field label="Search" variant="solo-filled">      </v-text-field> -->
        <v-col cols="1"><v-btn size="auto" class="pa-5" color="#082266" rounded="lg"> Search </v-btn></v-col>
        <v-col cols="1"><v-btn size="auto" class="ml-10 pa-5" color="#082266" rounded="lg"> <v-icon end
              icon="mdi mdi-filter-variant"></v-icon>
            Filter
          </v-btn></v-col>
      </v-row>

      <v-row no-gutters>
        <v-col cols="5">
          <v-btn size="auto" class="pa-5" color="#082266" rounded="lg"> Result - 3200 </v-btn>
        </v-col>
        <v-col cols="4"></v-col>
        <v-col cols="3"> <v-btn size="auto" class="pa-5 ml-12" color="#082266" rounded="lg"> Sort By: Result - 3200
            <v-icon end icon="mdi mdi-menu-down"></v-icon></v-btn>
        </v-col>

      </v-row>
    </v-container>
    <BookNotFound v-show="library.bookList.data.length == 0" />
    <div v-show="library.bookList.data.length !== 0" class="tw-min-h-[70%]">
      <BookCard :bookList="library.bookList.data"/>
    </div>
    <div v-show="library.bookList.data.length !== 0">
    <v-pagination v-model="page" class="my-4" :length="library.bookList.paginate.totalPages" :total-visible="7"
        rounded="20">
    </v-pagination></div>
    </div>
</template>

<style scoped></style>