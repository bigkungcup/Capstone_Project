<script setup>
import HistoryCard from '~/components/history/historyCard.vue';
import { useRouter } from "vue-router";

const router = useRouter();
const profileToken = ref(useCookie("profileToken"))

onBeforeMount(() => {
  if (profileToken.value.role == 'GUEST') {
    router.push(`/UnauthenPage/`)
  }
});
</script>
 
<template>
    <div v-show="profileToken.role !== 'GUEST'">
        <v-container>
            <v-row>
                <v-col cols="12" align="right">
                    <v-btn color="#1D419F" rounded variant="flat" size="large"
                        prepend-icon="mdi mdi-trash-can-outline">Clear all</v-btn>
                </v-col>
            </v-row>
            <v-row >
                <v-col cols="6" class="">
                    <HistoryCard />
                </v-col>
                <v-col cols="6">
                    <HistoryCard />
                </v-col>
            </v-row>
        </v-container>
    </div>
</template>
 
<style></style>