<script setup>
import Bookmarks from '~/components/profiles/bookmark.vue';
// import Reviews from '~/components/profiles/reviews.vue';

</script>
 
<template>
    <div class="tw-bg-[#D9D9D9] tw-h-full">
        <div class="tw-flex tw-place-content-center ">
            <div class="tw-w-[70rem] tw-max-h-[16rem]">
                <v-img src="/image/bookbanner3.png" cover class=""></v-img>
                <!-- <v-img src="/image/cat.jpg" width="120" height="120"
                    class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-5" cover /> -->
            </div>
        </div>


        <div class="tw-flex tw-place-content-center ">
            <div class="tw-bg-white tw-w-[70rem] tw-h-[9rem]">
                <v-row class="tw-py-2" >
                    <v-col cols="2" >
                        <v-img src="/image/cat.jpg" width="140" height="140" 
                    class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-8" cover />
                    </v-col>

                    <v-col cols="4">
                        <div>
                            <p class="web-text-header">Cat the Reviewer</p>
                            <p class="web-text-sub">mistercat@gmail.com</p>
                            <p class="web-text-sub tw-py-6">I want to be a kamen rider.</p>
                        </div>
                    </v-col>

                    <v-col cols="4">
                        <div>
                            <p class="web-text-sub tw-flex tw-place-content-end">120 Following 30 Followers</p>
                        </div>
                    </v-col>

                    <v-col cols="2">
                        <v-btn color="#1D419F" variant="outlined" rounded="lg" elevation="2">Edit profile</v-btn>
                    </v-col>
                </v-row>
            </div>
        </div>

        <!----------------------- new section -------------------------->
        <div class="tw-flex tw-place-content-center tw-my-6">
            <div class="tw-bg-white tw-w-[70rem] tw-h-full">
                <div class="tw-border-y-4">
                    <v-row class="tw-py-3">
                        <v-col cols="2">
                            <div class="tw-flex tw-justify-center web-text-sub-pf"> Bookmarks </div>
                        </v-col>
                        <v-col cols="2">
                            <div class="tw-flex tw-justify-center web-text-sub-pf"> My reviews </div>
                        </v-col>
                        <v-col cols="2">
                            <div class="tw-flex tw-justify-center web-text-sub-pf"> Followings </div>
                        </v-col>
                        <v-col cols="2">
                            <div class="tw-flex tw-justify-center web-text-sub-pf"> Followers </div>
                        </v-col>
                    </v-row>
                </div>

                <!-------- insert component here ---------->

                <Bookmarks />
                <!-- <Reviews /> -->

            </div>
        </div>
    </div>
</template>
 
<style scoped>
</style>