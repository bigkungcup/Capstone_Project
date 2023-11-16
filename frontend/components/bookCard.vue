<script setup>
defineProps({
    bookList: {
        type: Array,
        require: true,
    }
})
</script>
 
<template>
    <div class="tw-px-24">
        <v-card v-for="book in bookList" color="#D9D9D9" class="tw-mb-4 tw-h-[19rem]" :to="`/book/${book.bookId}`">
            <v-row no-gutters>
                <v-col cols="2" class="tw-my-4" align="center">
                    <v-img src="/image/cover_not_available.jpg" width="80%" />
                </v-col>
                <v-col cols="7" class="web-text-detail tw-my-8 tw-mx-2 tw-space-y-0.5">
                    <div>
                        <p class="web-text-title">{{ book.bookName }}</p>
                        <p class="tw-opacity-60">Update about {{ }}</p>
                    </div>
                    <div class="tw-h-2/6 tw-py-2 tw-overflow-hidden">
                        <p class="tw-text-ellipsis tw-indent-8">{{ book.bookDetail }}</p>
                    </div>
                    <div class="tw-space-x-1 tw-inline-flex tw-items-center tw-w-1/6">
                        <v-img  src="/image/star_icon.png"/>
                        <p class="web-text-rate">{{book.bookRating}}</p>
                        <p v-show="book.bookTotalReview == null">(0 review)</p>
                        <p v-show="book.bookTotalReview != null">({{ book.bookTotalReview }} reviews)</p>
                    </div>
                </v-col>
                <v-col class="tw-grid tw-grid-cols-8 tw-gap-2">
                    <div class="vertical-line tw-my-8"></div>
                    <div class="web-text-detail tw-col-span-7 tw-py-12">
                        <div class="tw-flex tw-gap-x-2 tw-py-2 tw-items-center">
                            <p class="tw-font-bold">Book Type : </p>
                            <span class="tag-color" v-show="book.bookType == ''">{{ book.bookType }}</span>
                        </div>
                        <div class="tw-flex tw-gap-x-4 tw-py-2 tw-items-center">
                            <p class="tw-font-bold">Genre : </p>
                            <span class="tag-color">{{ book.bookGenre }}</span>
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
    height: 50%;
    justify-self: center;
}
</style>