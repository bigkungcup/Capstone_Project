<script setup>
import { useBooks } from '~/stores/book'

const library = useBooks();
defineProps({
    historyList: {
        type: Array,
        require: true,
    }
})

function bookCoverPath(filePath) {
   return filePath = (`../_nuxt/@fs/${filePath}`)
}

</script>
 
<template>
    <!-- <div  class="tw-px-36 tw-space-y-4"> -->
        <v-row >
                <v-col cols="6" v-for="book in historyList"  class="">
    <v-card color="rgb(217, 217, 217, 0.6)" class="tw-h-[12rem]" :to="`/book/${book.historyId}/`">
        <v-row no-gutters>
            <v-col cols="4" class="tw-my-4" align="center">
                <!-- <v-img class="tw-drop-shadow-lg " src="/image/foryou1.png" width="100" height="160" cover /> -->
                <v-img class="tw-drop-shadow-lg " src="/image/cover_not_available.jpg" width="100" height="160" cover  v-show="book.bookData.file == null"/>
                <v-img class="tw-drop-shadow-lg" :src="book.bookData.file" width="100" height="160" cover v-show="book.bookData.file != null" />
            </v-col>
            <v-col cols="8" class="tw-my-4">
                <div class="">
                        <p  class="web-text-title tw-truncate" >{{ book.bookData.bookName }}</p>
                    <div class="tw-space-x-2">
                        <v-icon icon="mdi mdi-account" color="#082266"></v-icon>
                        <span class="web-text-sub">{{ book.bookData.author }}</span>
                    </div>
                    <v-row no-gutters>
                        <v-col cols="10">
                            <!-- <p class=" tw-my-16 web-text-sub"> About {{ library.countUpdateTime(book.historyUpdateDateTime) }}</p> -->
                        </v-col>
                        <v-col cols="2">
                            <v-icon icon="mdi mdi-trash-can-outline" color="#8B8B8B" class="tw-my-16" style="font-size: 2rem;" @click="library.deleteHistoryById(book.historyId)"></v-icon>
                        </v-col>
                    </v-row>
                </div>
            </v-col>
        </v-row>
    </v-card></v-col></v-row>
    <!-- </div> -->
</template>
 
<style></style>