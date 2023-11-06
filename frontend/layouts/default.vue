<script setup>
</script>

<template>
    <div class="tw-grid tw-grid-cols-3 tw-py-4 tw-border-b-gray-300 tw-border-2">
        <div class="tw-place-self-start tw-pl-16 tw-self-center tw-text-3xl">Bannarug</div>
        <div class="tw-flex tw-space-x-6 tw-place-self-center tw-text-xl tw-font-bold ">
            <NuxtLink to="/">Home</NuxtLink>
            <NuxtLink to="">Ranking</NuxtLink>
            <NuxtLink to="/library">Library</NuxtLink>
            <NuxtLink to="">Hirstory</NuxtLink>
        </div>
        <div class="tw-flex tw-space-x-6 tw-place-self-end tw-pr-16">
            <v-icon icon="mdi-magnify" style="font-size: 50px;"></v-icon>
            <v-icon icon="mdi-account-circle-outline" style="font-size: 50px;"></v-icon>
        </div>
    </div>
</template>
