<script setup>
defineEmits(["rating"]);
defineProps({
    newBookList: {
        type: Array,
        require: true,
  }
});
</script>

<template>
  <div>
    <div class="tw-text-4xl tw-text-[#082266] tw-font-extrabold tw-py-8">
      New Book !
    </div>
    <div class="">
      <v-sheet width="100%" height="100%">
        <v-slide-group show-arrows >
          <v-slide-group-item >
            <div class="tw-h-[15rem] tw-w-[30rem] tw-mx-4 tw-border-4 tw-border-[#3157BB] tw-rounded-xl" v-for="book in newBookList">
              <nuxt-link :to="`/book/${book.bookId}/`">
              <v-row no-gutters align="center" justify="center" class="py-3">
                <v-col cols="3">
                  <v-img
                    src="/image/cover_not_available.jpg"
                    v-if="book.file == null"
                    width="120"
                    height="190"
                    cover
                    class="tw-mx-2"
                  ></v-img>
                  <v-img
                    :src="book.file"
                    v-if="book.file != null"
                    width="120"
                    height="190"
                    cover
                    class="tw-mx-2"
                  ></v-img>
                </v-col>
                <v-col cols="9" class="tw-my-3">
                  <div class="tw-px-5">
                    <p class="web-text-title tw-truncate">
                      {{ book.bookName }}
                    </p>
                    <p
                      class="web-text-sub tw-italic tw-min-h-[6rem] tw-max-h-[6rem] tw-py-2 tw-overflow-clip"
                    >
                    {{ book.bookDetail }}
                    </p>
                    <div>
                      <v-rating
                        :model-value="0.5 * Math.floor(2 * book.bookRating)"
                        color="#FFB703"
                        density="compact"
                        size="meduim"
                        half-increments
                        readonly
                      ></v-rating>
                      <p>({{ book.bookTotalReview }} reviews)</p>
                    </div>
                  </div>
                </v-col>
              </v-row></nuxt-link>
              <!-- <v-img src="/image/foryou3.jpg" width="100%" height="100%" cover></v-img> -->
            </div>
          </v-slide-group-item>
        </v-slide-group>
      </v-sheet>
    </div>
  </div>
</template>

<style></style>
