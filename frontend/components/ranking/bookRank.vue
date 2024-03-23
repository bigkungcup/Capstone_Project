<script setup>
defineProps({
  bookList: {
    type: Array,
    require: true,
  },
});
</script>

<template>
  <div>
    <!-- section 1 -->
    <div class="tw-my-8">
      <v-row no-gutters>
        <v-col cols="2"></v-col>
        <v-col cols="2" align="center">
          <div class="tw-pt-24" v-for="(book, index) in bookList.slice(1, 2)">
            <nuxt-link :to="`/book/${book.bookId}/`">
              <v-img
                :src="book.file"
                width="150"
                height="250"
                v-if="book.file != null"
                cover
              />
              <v-img
                src="/image/cover_not_available.jpg"
                width="150"
                height="250"
                v-if="book.file == null"
                cover
              />
            </nuxt-link>
          </div>
        </v-col>
        <v-col cols="4" align="center">
          <div class="" v-for="(book, index) in bookList.slice(0, 1)">
            <nuxt-link :to="`/book/${book.bookId}/`">
              <v-img
                :src="book.file"
                width="220"
                height="360"
                v-if="book.file != null"
                cover
              />
              <v-img
                src="/image/cover_not_available.jpg"
                width="220"
                height="360"
                v-if="book.file == null"
                cover
              />
            </nuxt-link>
          </div>
        </v-col>
        <v-col cols="2" align="center">
          <div class="tw-pt-24" v-for="(book, index) in bookList.slice(2, 3)">
            <nuxt-link :to="`/book/${book.bookId}/`">
              <v-img
                :src="book.file"
                width="150"
                height="250"
                v-if="book.file != null"
                cover
              />
              <v-img
                src="/image/cover_not_available.jpg"
                width="150"
                height="250"
                v-if="book.file == null"
                cover
              />
            </nuxt-link>
          </div>
        </v-col>
        <v-col cols="2"></v-col>
      </v-row>
      <div>
        <v-row no-gutters> 
          <v-col cols="1"></v-col>
          <v-col cols="10" class="d-inline-flex tw-my-[-6rem]">
            <v-img
              src="/image/rankbanner.png"
              class="tw-absolute tw-bottom-12"
            ></v-img>
          </v-col>
          <v-col cols="1"></v-col>
        </v-row>
      </div>
      <v-row no-gutters class="web-text-title">
        <v-col cols="2"></v-col>
        <v-col cols="2" align="center">
          <div
            class="tw-pt-16 tw-overflow-clip"
            v-for="(book, index) in bookList.slice(1, 2)"
          >
            <p>{{ book.bookName }}</p>
            <v-rating
              :model-value="0.5 * Math.floor(2 * book.bookRating)"
              color="#FFB703"
              density="compact"
              size="small"
              half-increments
              readonly
            ></v-rating>
            <p class="web-text-sub text-subtitle-2">
              ({{ book.bookTotalReview }} reviews)
            </p>
          </div>
        </v-col>
        <v-col cols="4" align="center">
          <div
            class="tw-pt-14 tw-mx-8 tw-overflow-clip"
            v-for="(book, index) in bookList.slice(0, 1)"
          >
            <p>{{ book.bookName }}</p>
            <v-rating
              :model-value="0.5 * Math.floor(2 * book.bookRating)"
              color="#FFB703"
              density="compact"
              size="small"
              half-increments
              readonly
            ></v-rating>
            <p class="web-text-sub text-subtitle-2">
              ({{ book.bookTotalReview }} reviews)
            </p>
          </div>
        </v-col>
        <v-col cols="2" align="center">
          <div
            class="tw-pt-16 tw-overflow-clip"
            v-for="(book, index) in bookList.slice(2, 3)"
          >
            <p>{{ book.bookName }}</p>
            <v-rating
              :model-value="0.5 * Math.floor(2 * book.bookRating)"
              color="#FFB703"
              density="compact"
              size="small"
              half-increments
              readonly
            ></v-rating>
            <p class="web-text-sub text-subtitle-2">
              ({{ book.bookTotalReview }} reviews)
            </p>
          </div>
        </v-col>
        <v-col cols="2"></v-col>
      </v-row>
    </div>

    <!-- section 2 -->
    <div class="tw-my-16">
      <div
        class="tw-h-min-[11rem] tw-h-max-[11rem]"
        v-for="(book, index) in bookList.slice(3)"
      >
        <nuxt-link :to="`/book/${book.bookId}/`">
          <hr class="tw-my-4" />
          <v-row no-gutters>
            <v-col cols="1" align="left" class="tw-my-5">
              <p class="ranking-num-header">{{ index + 4 }}</p>
            </v-col>
            <v-col
              cols="8"
              class="tw-flex tw-flex-col tw-justify-center tw-space-y-2"
            >
              <p class="web-text-header">{{ book.bookName }}</p>
              <p
                class="web-text-sub tw-min-h-[5rem] tw-max-h-[5rem] tw-py-1 tw-overflow-clip"
              >
                {{ book.bookDetail }}
              </p>
              <div
                class="tw-space-x-1 tw-inline-flex tw-items-center tw-w-4/6 tw-py-1"
              >
                <v-rating
                  :model-value="0.5 * Math.floor(2 * book.bookRating)"
                  color="#FFB703"
                  density="compact"
                  size="large"
                  half-increments
                  readonly
                ></v-rating>
                <span class="web-text-sub"
                  >({{ book.bookTotalReview }} reviews)</span
                >
              </div>
            </v-col>
            <v-col cols="1"></v-col>
            <v-col cols="2" align="right"
              ><v-img
                :src="book.file"
                v-if="book.file != null"
                width="120"
                height="200"
                cover
              ></v-img
              ><v-img
                src="/image/cover_not_available.jpg"
                v-if="book.file == null"
                width="120"
                height="200"
                cover
              ></v-img
            ></v-col>
          </v-row>
        </nuxt-link>
      </div>
    </div>
  </div>
</template>

<style></style>
