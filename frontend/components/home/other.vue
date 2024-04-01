<script setup>
defineProps({
    otherBookList: {
        type: Array,
        require: true,
  }
});
</script>

<template>
  <div>
    <div class="tw-text-4xl tw-text-[#082266] tw-font-extrabold tw-py-8">
      Other...
    </div>
    <div v-if="otherBookList.length != 0">
      <v-row>
        <v-col cols="4" class="" v-for="book in otherBookList">
          <nuxt-link :to="`/book/${book.bookId}/`">
          <div class="tw-h-min-[11rem] tw-h-max-[11rem]">
            <v-row no-gutters>
              <v-col cols="4">
                <v-img src="/image/cover_not_available.jpg" width="100" height="160" v-if="book.file == null" cover/>
                <v-img :src="book.file" width="100" height="160" v-if="book.file != null" cover/>
              </v-col>
              <v-col
                cols="8"
                class="tw-flex tw-flex-col tw-justify-center tw-space-y-2"
              >
                <p class="web-text-title">{{ book.bookName }}</p>
                <p class="web-text-detail">{{ book.author }}</p>
              </v-col>
            </v-row>
          </div>
        </nuxt-link>
        </v-col>
      </v-row>
    </div>
    <div class="tw-h-[15rem] tw-grid tw-place-content-center tw-gap-y-2" v-if="otherBookList.length == 0">
        <img src="/image/book_not_found.png" width="240" />        
          <v-btn variant="outlined" size="large" rounded="xl" v-show="roleToken !== 'GUEST'"
                class="custom-border tw-flex  tw-justify-self-center" color="#082266">
                <NuxtLink to="/book/create/"><p class="tw-font-bold">Create Book</p></NuxtLink>
            </v-btn>       
      </div>
  </div>
</template>

<style></style>
