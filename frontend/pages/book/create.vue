<script setup>
definePageMeta({
    layout: false,
})

</script>
 
<template>
    <div class="tw-bg-[#3157BB] tw-h-[4rem] d-flex justify-center align-center tw-drop-shadow-xl">
        <p class="lily tw-text-3xl tw-text-white">Bannarug</p>
    </div>
    <div class="tw-pt-1 tw-pb-10 tw-drop-shadow-lg tw-space-y-1">
        <div class="tw-mx-36 tw-mt-5">
            <v-btn prepend-icon="mdi mdi-chevron-left" variant="text" width="8%" color="#082250">
                <p class="tw-font-bold">Back</p>
            </v-btn>
        </div>
        <div class="tw-flex tw-justify-center tw-min-h-[25rem] tw-pb-2">
            <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
                <v-row no-gutters="">
                    <v-col cols="2">
                        <v-avatar class="my-6" size="200" rounded="0">
                            <v-img src="/image/upload_book_cover.png"></v-img>
                        </v-avatar>
                        <div class="mx-2">
                            <v-btn> 
                                <p class="tw-font-bold tw-text-[#1D419F] tw-text-xs">Select Book type </p>
                                <span class="mdi mdi-chevron-right"></span>
                            </v-btn>
                        </div>
                    </v-col>
                    <v-col cols="10">
                        <div class="tw-mx-8 tw-my-6">
                            <v-text-field label="Book Name" variant="solo"></v-text-field>
                            <v-text-field label="Author" variant="solo"></v-text-field>
                            <v-textarea label="Book Detail" variant="solo" rows="5"></v-textarea>
                            <div class="inline-elements tw-space-x-2">
                                <span class="tw-font-bold tw-text-[#1D419F] tw-text-lg">Genre:</span>
                                <v-text-field label="Genre" variant="solo" class="inline-field"></v-text-field>
                            </div>
                        </div>
                    </v-col>

                </v-row>

            </v-card>
        </div>

        <div class="d-flex justify-end tw-mx-[9rem] tw-space-x-4">
            <v-btn color="#1D419F" variant="outlined" @click="">clear</v-btn>
            <v-btn color="#1D419F" variant="flat" @click="" >submit</v-btn>
        </div>

    </div>
</template>
 
<style scoped>
.inline-elements {
    display: flex;
    align-items: center;
}
</style>