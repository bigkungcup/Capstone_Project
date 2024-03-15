<script setup>
defineProps({
    mostviewBookList: {
        type: Array,
        require: true,
  }
});

function formatTotalview(totalview) {
    if (totalview >= 1000 && totalview < 1000000) {
          return (totalview / 1000).toFixed(1) + 'K';
        } else if (totalview >= 1000000) {
          return (totalview / 1000000).toFixed(1) + 'M';
        } else {
          return totalview.toString();
        }
}

</script>

<template>
  <div>
    <div class="tw-text-4xl tw-text-[#082266] tw-font-extrabold tw-py-8">
      Top 5 Most Viewed !
    </div>
    <div class="tw-space-y-4">
      <!--------- No.1 ------------>
      <v-card class="tw-h-min-[11rem] tw-h-max-[11rem]" v-for="(book, index) in mostviewBookList" :to="`/book/${book.bookId}/`">
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
              <p>{{ formatTotalview(book.bookTotalView) }}</p>
            </div>
          </v-col>
          <v-col cols="1"
            ><v-img
              src="/image/cover_not_available.jpg"
              v-if="book.file == null"
              width="100%"
              height="100%"
              cover
            /><v-img
              :src="book.file"
              v-if="book.file != null"
              width="100%"
              height="100%"
              cover
            />
          </v-col>
        </v-row>
      </v-card>
    </div>
  </div>
</template>

<style></style>
