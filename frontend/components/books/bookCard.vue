<script setup>
import { useBooks } from '~/stores/book'

const library = useBooks();
defineProps({
    bookList: {
        type: Array,
        require: true,
    }
})

function bookCoverPath(filePath) {
   return filePath = (`/ej2/_nuxt/@fs/${filePath}`)
}

</script>
 
<template>
    <div class="tw-px-36 tw-space-y-4">
        <v-card v-for="book in bookList" :search="library.searchBook" color="rgb(217, 217, 217, 0.6)" class="tw-min-h-[19rem] tw-max-h-[19rem]" :to="`/book/${book.bookId}/`">
            <v-row no-gutters>
                <v-col cols="3" class="my-3" align="center">
                    <v-img class="tw-drop-shadow-lg " src="/image/cover_not_available.jpg" width="180" height="280" cover v-show="book.file == null"/>
                    <v-img class="tw-drop-shadow-lg" :src="book.file" width="180" height="280" cover v-show="book.file != null" />
                    <!-- <v-img class="tw-drop-shadow-lg" :src="book.file" width="180" height="280" cover v-show="book.file != null" /> -->
                    <!-- <v-img src="~\Files\Uploads\TEST1\foryou2.jpg" width="180" v-show="book.file !== null"/> -->
                </v-col>
                <v-col cols="5" class="web-text-detail tw-my-10 tw-mx-2 tw-space-y-0.5">
                    <div>
                        <p class="web-text-title">{{ book.bookName }}</p>
                        <!-- <p class="tw-opacity-60">Update about {{ library.countUpdateTime(book.countDateTime) }}</p> -->
                    </div>
                    <div class="tw-min-h-[9rem] tw-max-h-[9rem] tw-py-2 tw-overflow-clip">
                        <p class="tw-indent-8">{{ book.bookDetail }}</p>
                    </div>
                    <div class="tw-space-x-1 tw-inline-flex tw-items-center tw-w-4/6 tw-py-2">
                        <v-rating :model-value="0.5 * Math.floor(2 * book.bookRating)" color="#FFB703"
                            density="compact" size="meduim" half-increments readonly></v-rating>
                        <p class="web-text-rate">{{0.5 * Math.floor(2 * book.bookRating)}}</p>
                        <p v-show="book.bookTotalReview == null">(0 review)</p>
                        <p v-show="book.bookTotalReview != null">({{ book.bookTotalReview }} reviews)</p>
                    </div>
                </v-col>
                <v-col class="tw-grid tw-grid-cols-8 tw-gap-2">
                    <div class="vertical-line tw-my-5"></div>
                    <div class="web-text-detail tw-col-span-7 tw-py-14">
                        <div class="tw-flex tw-gap-x-2 tw-py-2 tw-items-center">
                            <p class="tw-font-bold">Book Type : </p>
                            <v-btn color="#1D419F">{{ book.booktype.booktypeName }}</v-btn>
                        </div>
                        <div class="tw-flex tw-gap-x-2 tw-py-2 tw-items-center">
                            <p class="tw-font-bold">Tags : </p>
                            <v-chip variant="elevated" color="#1D419F" v-show="book.bookTagList[0] != ''" v-for="tag in book.bookTagList">{{ tag }}</v-chip>
                    </div>
                </div>
                </v-col>
            </v-row>
        </v-card>
    </div>
</template>
 
<style scoped>
.vertical-line{
    border-left: 4px solid #082266;
    opacity: 50%;
    height: 80%;
    justify-self: center;
}
</style>