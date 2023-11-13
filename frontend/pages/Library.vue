<script setup>

const library = {
  data: [
    {
      bookId: '01',
      bookName: 'I Know Why the Caged Bird Sings',
      bookDetail: 'Maya Angelou’s debut memoir is a modern American classic beloved worldwide. Her life story is told in the documentary film And Still I Rise, as seen on PBS’s American Masters.Here is a book as joyous and painful, as mysterious and memorable, as childhood itself. I Know Why the Caged Bird Sings captures the longing of lonely children, the brute insult of bigotry, and the wonder of words that can make the world right. Maya Angelou’s debut memoir is a modern American classic beloved worldwide. Sent by their mother to live with their devout, self-sufficient grandmother in a small Southern town, Maya and her brother, Bailey, endure the ache of abandonment and the prejudice of the local “powhitetrash.” At eight years old and back at her mother’s side in St. Louis, Maya is attacked by a man many times her age—and has to live with the consequences for a lifetime.',
      author: 'Maya Angelou',
      bookType: 'Autobiography',
      bookGenre: 'Biography',
      bookTotalBookmarked: 124,
      bookTotalReview: 3,
      bookRating: 5.0,
      updateDate: '',
      bookCover: null
    },
    // {
    //   bookId: '02',
    //   bookName: 'I Know Why the Caged Bird Sings',
    //   bookDetail: 'Maya Angelou’s debut memoir is a modern American classic beloved worldwide. Her life story is told in the documentary film And Still I Rise, as seen on PBS’s American Masters.Here is a book as joyous and painful, as mysterious and memorable, as childhood itself. I Know Why the Caged Bird Sings captures the longing of lonely children, the brute insult of bigotry, and the wonder of words that can make the world right. Maya Angelou’s debut memoir is a modern American classic beloved worldwide. Sent by their mother to live with their devout, self-sufficient grandmother in a small Southern town, Maya and her brother, Bailey, endure the ache of abandonment and the prejudice of the local “powhitetrash.” At eight years old and back at her mother’s side in St. Louis, Maya is attacked by a man many times her age—and has to live with the consequences for a lifetime.',
    //   author: 'Maya Angelou',
    //   bookType: 'Autobiography',
    //   bookGenre: 'Biography',
    //   bookTotalBookmarked: 124,
    //   bookTotalReview: 3,
    //   bookRating: 5.0,
    //   updateDate: '',
    //   bookCover: null
    // }
  ],
  paginate: {
    pageNo: 1,
    pageSize: 10,
    totalElements: 1,
    totalPages: 1,
    last: true
  }
};
// const library = [];
</script>

<template>
  <div>
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
    <BookNotFound v-show="library.data.length == 0" />
    <!-- <div v-show="library.data.length !== 0"> -->
      <BookCard :bookList="library.data" />
      <v-pagination v-model="page" class="my-4" :length=library.paginate.totalPages :total-visible="7"
        rounded="20"></v-pagination>
    <!-- </div> -->
  </div>
</template>

<style scoped></style>