<script setup>
import { useBooks } from "~/stores/book";
import { ref } from 'vue';

definePageMeta({
    layout: false,
})

const book = useBooks();
const route = useRoute()
const selectedImage = ref();

function handleFileChange(event) {
      if (book.editBookFile[0]) {
        // Convert the selected image to a data URL
        const reader = new FileReader();
        reader.onload = (e) => {
          this.selectedImage = e.target.result
        };
        reader.readAsDataURL(book.editBookFile[0]);
      }
}

const rules = {
    required: value => !!value || 'Field is required',
    limited: value => value.length <= 255 || 'Max 255 characters',
    size: value => !value || value[0].size <= 64000000 || showValidateSize()
}

onBeforeMount(() => {
    selectedImage.value = book.editBook.file == null ? null : `../../_nuxt/@fs/${book.editBook.file}`
})

await book.getBookDetail(route.params.id)
book.setEditBook()

</script>
 
<template>
    <div class="tw-bg-[#3157BB] tw-h-[4rem] d-flex justify-center align-center tw-drop-shadow-xl">
        <p class="lily tw-text-3xl tw-text-white">Bannarug</p>
    </div>
    <div class="tw-pt-1 tw-pb-10 tw-drop-shadow-lg tw-space-y-1">
        <div class="tw-mx-36 tw-mt-5">
            <v-btn prepend-icon="mdi mdi-chevron-left" variant="text" width="8%" color="#082250" @click="$router.go(-1)">
                <p class="tw-font-bold">Back</p>
            </v-btn>
        </div>
        <div class="tw-flex tw-justify-center tw-min-h-[32rem] tw-pb-2">
            <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
                <v-row no-gutters="">
                    <v-col cols="3">
                        <div class="my-8">
                        <div rounded="0" class="d-flex justify-center px-10" @click="$refs.fileInput.click()">
                            <v-img src="/image/upload_book_cover.png" v-show="book.editBook.file == null" width="250" height="320" cover></v-img>
                            <v-img :src="selectedImage" v-show="book.editBook.file != null" width="250" height="320" cover></v-img>
                        </div>
                        <v-responsive class="mx-auto my-2" max-width="200">
                            <v-file-input ref="fileInput" v-model="book.editBookFile" @change="handleFileChange()" accept="image/*" style="display: none;" :rules="[rules.size]">
                        </v-file-input>
                        <p v-show="validateSize" class="validate-text">Image size should be less than 64 MB!</p>
                        </v-responsive>
                        <div class="d-flex justify-center">
                            <v-btn> 
                                <p class="tw-font-bold tw-text-[#1D419F] tw-text-xs">Select Book type </p>
                                <span class="mdi mdi-chevron-right"></span>
                            </v-btn>
                        </div>
                        </div>
                    </v-col>
                    <v-col cols="9">
                        <div class="tw-mr-8 tw-my-8">
                            <v-text-field label="Book Name" variant="solo" v-model="book.editBook.bookName" :rules="[rules.required,rules.limited]" counter></v-text-field>
                            <v-text-field label="Author" variant="solo" v-model="book.editBook.author" :rules="[rules.required,rules.limited]" counter></v-text-field>
                            <v-textarea label="Book Detail" variant="solo" rows="6" v-model="book.editBook.bookDetail" :rules="[rules.required]"></v-textarea>
                            <div class="tw-space-x-2 tw-flex tw-items-center">
                                <span class="tw-font-bold tw-text-[#1D419F] tw-text-lg">Genre:</span>
                                <v-text-field label="Genre" variant="solo" class="inline-field" v-model="book.editBook.bookGenre" :rules="[rules.required]"></v-text-field>
                            </div>
                        </div>
                    </v-col>

                </v-row>

            </v-card>
        </div>

        <div class="d-flex justify-end tw-mx-[9rem] tw-space-x-4">
            <v-btn color="#1D419F" variant="outlined" @click="book.setEditBook()">reset</v-btn>
            <v-btn color="#1D419F" variant="flat" @click="book.updateBook(route.params.id)" >submit</v-btn>
        </div>

    </div>
</template>
 
<style scoped>
.inline-elements {
    display: flex;
    align-items: center;
}
</style>