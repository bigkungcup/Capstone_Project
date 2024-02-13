<script setup>
import { useBooks } from '~/stores/book'
import BookCard from '~/components/books/bookCard.vue';


const library = useBooks();
const page = ref(1)

onBeforeMount(async () => {
    await library.getLibrary();
});

</script>
 
<template>
    <!-- <div>
        {{ library.bookList.data.content }}
    </div> -->
    <div v-show="library.bookList.data.content.length !== 0">
        <BookCard :bookList="library.bookList.data.content" />
    </div>
</template>
 
<style></style>