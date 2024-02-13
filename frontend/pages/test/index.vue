<script setup>
import { useBooks } from '~/stores/book'

const library = useBooks();
const page = ref(1)

onBeforeMount( async () => {
  await library.getLibrary();
});

</script>
 
<template>
<div>
    {{ library.bookList.data.content }}
</div>
</template>
 
<style>

</style>