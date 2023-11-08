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
        <v-card v-for="book in bookList" height="30%" color="#D9D9D9">          
            <v-row no-gutters>
                <v-col class="tw-grid tw-place-items-center">
                <img src="/image/cover_not_available.jpg" width="50%" spect-ratio="16/9" cover />
                </v-col>
                <v-col class="tw-place-content-center">
                    <div>
                        <p>{{book.bookName}}</p>
                        <p>Update about {{ }}</p>
                    </div>
                    <div class="tw-h-3/5 tw-py-2 tw-overflow-hidden">
                        <p class="tw-text-ellipsis">{{book.bookDetail}}</p>
                    </div>
                    <div>{{book.bookTotalReview}} reviews</div>
                </v-col>
                <v-col>
                    <div class="tw-flex">
                        <p>Book Type : </p>
                        <span>{{book.bookType}}</span>
                    </div>
                    <div class="tw-flex">
                        <p>Genre : </p>
                        <span>{{book.bookGenre}}</span>
                    </div>
                </v-col>
            </v-row>
        </v-card>
    </div>
</template>
 
<style>

</style>