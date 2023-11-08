<script setup></script>

<template>
    <div class="tw-grid tw-grid-cols-3 tw-py-4 tw-border-b-gray-300 tw-border-2 tw-font-bold">
        <div class="tw-flex tw-space-x-2 tw-pl-16">
            <img src="../public/image/logo.png" style="height: 40px;"/>
            <p class="lily app-name-color tw-text-3xl tw-self-center">Bannarug</p>
        </div>
        <div class="nav-text-before-click tw-flex tw-space-x-6 tw-place-self-center tw-text-xl ">
            <NuxtLink to="/">Home</NuxtLink>
            <NuxtLink to="/bookDetail">Ranking</NuxtLink>
            <NuxtLink to="/library">Library</NuxtLink>
            <NuxtLink to="">History</NuxtLink>
        </div>
        <div class="nav-icon-color tw-flex tw-space-x-6 tw-place-self-end tw-pr-16">
            <v-icon icon="mdi-magnify" style="font-size: 50px"></v-icon>
            <v-icon icon="mdi-account-circle-outline" style="font-size: 50px"></v-icon>
        </div>
    </div>
</template>
