<script setup>
import { useBooks } from '~/stores/book'
defineProps({
    mostviewBookList: {
        type: Array,
        require: true,
  }
});

const library = useBooks();
const roleToken = ref(localStorage.getItem("role"));

</script>

<template>
  <div>
    <div class="tw-text-4xl tw-text-[#082266] tw-font-extrabold tw-py-8">
      Top 5 Most Viewed !
    </div>
    <div class="tw-space-y-4">
      <!--------- No.1 ------------>
      <v-card class="tw-h-min-[11rem] tw-h-max-[11rem]" v-for="(book, index) in mostviewBookList" :to="`/book/${book.bookId}/`" v-if="mostviewBookList.length != 0">
        <v-row no-gutters>
          <v-col cols="1" align="center" class="tw-my-5"
            ><v-chip variant="elevated" color="#1D419F" size="x-large"
              ># {{ index+1 }}</v-chip
            ></v-col
          >
          <v-col
            cols="8"
            class="tw-flex tw-flex-col tw-justify-center tw-space-y-2"
          >
            <p class="web-text-header">{{ book.bookName }}</p>
            <p
              class="web-text-sub tw-italic tw-min-h-[4rem] tw-max-h-[4rem] tw-py-2 tw-overflow-clip"
            >
            {{ book.bookDetail }}
            </p>
            <p class="web-text-title">by {{ book.author }}</p>
          </v-col>
          <v-col cols="2" align="center" class="tw-my-4 tw-space-y-16">
            <v-chip variant="elevated" color="#1D419F" size="x-large"
              >{{ book.booktype.booktypeName }}</v-chip
            >
            <div class="web-text-title d-flex justify-center">
              <v-icon icon="mdi mdi-eye-outline" class="tw-mx-2" />
              <p>{{ library.formatTotalNumber(book.bookTotalView) }}</p>
            </div>
          </v-col>
          <v-col cols="1"
            ><v-img
            class="tw-drop-shadow-lg tw-border-2 tw-border-[#082266]"
              src="/image/cover_not_available.jpg"
              v-if="book.file == null"
              width="100%"
              height="100%"
              cover
            /><v-img
            class="tw-drop-shadow-lg tw-border-2 tw-border-[#082266]"
              :src="book.file"
              v-if="book.file != null"
              width="100%"
              height="100%"
              cover
            />
          </v-col>
        </v-row>
      </v-card>
      <div class="tw-h-[15rem] tw-grid tw-place-content-center tw-gap-y-2" v-if="mostviewBookList.length == 0">
        <img src="/image/book_not_found.png" width="240" />        
          <v-btn variant="outlined" size="large" rounded="xl" v-show="roleToken !== 'GUEST'"
                class="custom-border tw-flex  tw-justify-self-center" color="#082266">
                <NuxtLink to="/book/create/"><p class="tw-font-bold">Create Book</p></NuxtLink>
            </v-btn>       
      </div>
    </div>
  </div>
</template>

<style></style>
