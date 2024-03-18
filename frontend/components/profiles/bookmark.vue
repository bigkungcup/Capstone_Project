<script setup>
defineProps({
    bookmarkList: {
        type: Array,
        require: true,
    }
})

</script>
 
<template>
    <div class="py-4 px-10">
        <v-row no-gutters>
            <v-col cols="10">
                <v-text-field label="Search" variant="solo-filled">
                </v-text-field>
            </v-col>
            <v-col cols="1"><v-btn height="58" class="pa-5" color="#082266" rounded="lg">
                    Search
                </v-btn></v-col>
            <v-col cols="1"><v-btn height="58" class="mx-5 pa-5" color="#082266" rounded="lg">
                    <v-icon icon="mdi mdi-filter-variant"></v-icon>
                    Filter
                </v-btn></v-col>
        </v-row>
        <v-row no-gutters="">
            <v-col cols="3" class="" align="center" v-for="book in bookmarkList" >
                <nuxt-link :to="`/book/${book.book.bookId}/`">
                <v-img class="tw-drop-shadow-lg " src="/image/cover_not_available.jpg" width="200px" height="300px" cover  v-show="book.book.file == null"/>
                <v-img class="tw-drop-shadow-lg tw-border-2 tw-border-[#082266]" :src="book.book.file" width="200px" height="300px" cover v-show="book.book.file != null" />
                </nuxt-link>
                <p class="my-2 web-text-detail tw-truncate">{{book.book.bookName}}</p>
            </v-col>
        </v-row>
    </div>
</template>
 
<style></style>