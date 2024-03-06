<script setup>
defineProps({
    reviewList: {
        type: Array,
        require: true,
    }
})
</script>
 
<template>
    <div class="">
    <v-card 
    v-for="review in reviewList" 
    style="box-shadow: none;" 
    :to="`/book/1/`">
        <v-row class="tw-py-6">
            <v-col cols="3" class="tw-space-y-2" align="center">
                <v-img class="tw-drop-shadow-lg" src="/image/cover_not_available.jpg" width="150px" height="250px" cover v-if="review.bookDetail.file == null"></v-img>
                <v-img class="tw-drop-shadow-lg tw-border-2 tw-border-[#082266]" :src="review.bookDetail.file" width="150px" height="250px" cover v-if="review.bookDetail.file !== null"></v-img>
                <p class="web-text-detail tw-truncate">{{ review.bookDetail.bookName }}</p>
            </v-col>
            <v-col cols="7" class="tw-space-y-4" >
                <div class="tw-flex tw-justify-start">
                    <v-rating :model-value="review.reviewRating" color="#FFB703" density="compact" size="meduim"
                        half-increments readonly></v-rating>
                </div>
                <p class="tw-flex tw-justify-start web-text-title">{{ review.reviewTitle }}</p>
                <p class="tw-flex tw-justify-start web-text-sub tw-min-h-[6rem]">
                    {{ review.reviewDetail }}
                </p>
                <p class="tw-flex tw-justify-start web-text-sub tw-py-4">
                    {{ review.reviewTotalLike }} likes {{ review.reviewTotalDisLike }} dislikes
                </p>
            </v-col>
            <v-col cols="2">
                <div class="tw-flex tw-justify-end web-text-sub tw-px-5">
                    30 minutes ago
                </div>
            </v-col>
        </v-row>
    </v-card>
    <hr />
</div>
</template>
 
<style scoped>

</style>