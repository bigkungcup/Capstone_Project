<script setup>
const roleToken = ref(localStorage.getItem('role'));
</script>

<template>
    <v-container class="tw-grid tw-place-content-center tw-gap-y-2">
        <img src="/image/book_not_found.png" style="height: 360px;" />        
            <v-btn variant="outlined" size="large" rounded="xl" v-show="roleToken !== 'GUEST'"
                class="custom-border tw-flex tw-w-2/5 tw-justify-self-center" color="#082266">
                <NuxtLink to="/book/create/"><p class="tw-font-bold">Create Book</p></NuxtLink>
            </v-btn>       
    </v-container>
</template>

<style scoped>
.custom-border {
    border-width: 3px;
}
</style>
