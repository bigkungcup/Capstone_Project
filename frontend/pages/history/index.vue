<script setup>
import { useBooks } from "~/stores/book";
import HistoryCard from '~/components/history/historyCard.vue';
import historyNotFound from "~/components/history/historyNotFound.vue";
import { useRouter } from "vue-router";

const book = useBooks();
const page = ref(1);
const router = useRouter();
const profileToken = ref(useCookie("profileToken"));

onBeforeMount(async () => {
  if (profileToken.value.role == 'USER') {
    await book.getHistoryList();
    console.log(book.historyList);
  }else{
    router.push(`/UnauthenPage/`)
  }
});
</script>
 
<template>
    <div class="tw-min-h-[80%]" v-show="profileToken.role == 'USER'">
        <v-container>
            <v-row>
                <v-col cols="12" align="right">
                    <v-btn color="#1D419F" rounded variant="flat" size="large"
                        prepend-icon="mdi mdi-trash-can-outline" @click="book.deleteHistoryAll()">Clear all</v-btn>
                </v-col>
            </v-row>
    <historyNotFound v-show="book.historyList.data.content.length == 0" />
    <div v-show="book.historyList.data.content.length !== 0" >
        <HistoryCard :historyList="book.historyList.data.content"/>
    </div>

        </v-container>
    </div>
    <div v-show="book.historyList.data.content.length !== 0">
    <v-pagination v-model="page" :length="book.historyList.data.totalPages" :total-visible="7"
        rounded="20" @update:model-value="book.changeHistoryPage(page)">
    </v-pagination></div>
</template>
 
<style></style>