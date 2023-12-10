<script setup>
import { ref } from "vue";
import BookCard from '~/components/books/bookCard.vue';
import BookNotFound from "~/components/books/bookNotFound.vue";
import { useBooks } from '~/stores/book'

const library = useBooks();
const page = ref(1)

await library.getLibrary();

</script>

<template>
  <div class="tw-min-h-[80%]">
    <v-container>
      <v-row no-gutters>
        <v-col cols="10">
          <v-text-field label="Search" variant="solo-filled"> </v-text-field>
        </v-col>
        <v-col cols="1"><v-btn size="auto" class="pa-5" color="#082266" rounded="lg"> Search </v-btn></v-col>
        <v-col cols="1"><v-btn size="auto" class="ml-10 pa-5" color="#082266" rounded="lg"> <v-icon end
              icon="mdi mdi-filter-variant"></v-icon>
            Filter
          </v-btn></v-col>
      </v-row>

      <v-row no-gutters>
        <v-col cols="5">
          <v-btn size="auto" class="pa-5" color="#082266" rounded="lg"> Result - {{ library.bookList.data.totalElements }} </v-btn>
        </v-col>
        <v-col cols="4"></v-col>
        <v-col cols="3"> <v-btn size="auto" class="pa-5 ml-12" color="#082266" rounded="lg"> Sort By: Result - {{ library.bookList.data.totalElements }}
            <v-icon end icon="mdi mdi-menu-down"></v-icon></v-btn>
        </v-col>

      </v-row>
    </v-container>
    <BookNotFound v-show="library.bookList.data.content.length == 0" />
    <div v-show="library.bookList.data.content.length !== 0" >
      <BookCard :bookList="library.bookList.data.content" />
    </div>

    </div>
    <div v-show="library.bookList.data.content.length !== 0">
    <v-pagination v-model="page" :length="library.bookList.data.totalPages" :total-visible="7"
        rounded="20" @update:model-value="library.changeLibraryPage(page)">
    </v-pagination></div>
</template>

<style scoped></style>