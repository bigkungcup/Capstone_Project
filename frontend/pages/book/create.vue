<script setup>
import { useRouter } from "vue-router";
import { useBooks } from "~/stores/book";
import { ref } from "vue";
import CreateBookSuccessPopup from "~/components/books/popups/createBookSuccessPopup.vue";
import DuplicateBookPopup from "~/components/books/popups/duplicateBookPopup.vue";
import LoadingPopup from "~/components/popups/loadingPopup.vue";

const book = useBooks();
const selectedImage = ref(null);
const router = useRouter();
const roleToken = ref(localStorage.getItem('role'));

function handleFileChange() {
  if (book.newBookFile[0]) {
    selectedImage.value = URL.createObjectURL(book.newBookFile[0])
  }
}

const validateSize = ref(false);

function showValidateSize() {
  validateSize.value = true;
}

function toggleBookFailPopup() {
  book.failPopup = !book.failPopup;
}

const rules = {
  required: (value) => !!value || "Field is required",
  limited: (value) => value.length <= 255 || "Max 255 characters",
  size: (value) => !value || value[0].size <= 50000000 || showValidateSize(),
  selected: (value) => value != null || 'Please select type'
};

onBeforeRouteLeave(() => {
  if (roleToken.value !== 'GUEST') {
  if (
    book.newBook.bookName !== "" ||
    book.newBook.author !== "" ||
    book.newBook.bookDetail !== "" ||
    book.newBook.booktypeId !== undefined ||
    (book.newBook.bookTag !== null ? book.newBook.bookTag.length !== 0 : false) ||
    book.newBookFile !== null 
  ) {
    if(book.leavePopup){
    const shouldShowPopup = confirm("Do you really want to leave?");
    if (shouldShowPopup) {
      return null
    } else {
      next(false); // Prevent leaving the page
    }
  }
}}
});

onBeforeMount(() => {
  if (roleToken.value == 'GUEST') {
    book.leavePopup = true;
    book.newBookFile = null;
    router.push(`/UnauthenPage/`)
  }else{
    book.clearNewBook();
    book.getBookType();
    book.leavePopup = true;
    book.newBookFile = null;
}
});

</script>

<template>
  <div class="tw-pt-1 tw-pb-10 tw-drop-shadow-lg tw-space-y-1" v-show="roleToken !== 'GUEST'">
    <div class="tw-mx-36 tw-mt-5">
      <v-btn
        prepend-icon="mdi mdi-chevron-left"
        variant="text"
        width="8%"
        color="#082250"
        @click="$router.go(-1)"
      >
        <p class="tw-font-bold">Back</p>
      </v-btn>
    </div>
    <div class="tw-flex tw-justify-center tw-min-h-[32rem] tw-pb-2">
      <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
        <v-row no-gutters="">
          <v-col cols="3">
            <div class="my-8">
              <div
                rounded="0"
                class="d-flex justify-center px-10"
                @click="$refs.fileInput.click()"
              >
                <v-img
                  src="/image/upload_book_cover.png"
                  v-show="book.newBookFile == null"
                  width="250"
                  height="320"
                ></v-img>
                <v-img
                  :src="selectedImage"
                  v-show="book.newBookFile != null"
                  width="250"
                  height="320"
                  cover
                ></v-img>
              </div>
              <div class="px-10" v-show="book.newBookFile != null">
              <v-btn block @click="book.newBookFile = null">
                  <p class="tw-font-bold tw-text-[#1D419F] tw-text-xs">
                    cancel
                  </p>
              </v-btn>
            </div>
              <v-responsive class="mx-auto my-2 justify-center" max-width="200">
                <v-file-input
                  ref="fileInput"
                  v-model="book.newBookFile"
                  @change="handleFileChange()"
                  accept="image/*"
                  style="display: none"
                  :rules="[rules.size]"
                >
                </v-file-input>
                <p v-show="validateSize" class="validate-text">
                  Image size should be less than 50 MB!
                </p>
              </v-responsive>
              <div class="d-flex justify-center px-8">
                <v-autocomplete
                class="tw-font-bold tw-text-[#1D419F] tw-text-xs "
                v-model="book.newBook.booktypeId"
                :items="book.bookType"
                item-title="booktypeName"
                item-value="booktypeId"
                label="Select Book type"
                variant="solo-filled"
                :rules="[rules.selected]"
                ></v-autocomplete>
              </div>
            </div>
          </v-col>
          <v-col cols="9">
            <div class="tw-mr-8 tw-my-8">
              <v-text-field
                label="Book Name"
                variant="solo"
                v-model="book.newBook.bookName"
                :rules="[rules.required, rules.limited]"
                counter
              ></v-text-field>
              <v-text-field
                label="Author"
                variant="solo"
                v-model="book.newBook.author"
                :rules="[rules.required, rules.limited]"
                counter
              ></v-text-field>
              <v-textarea
                label="Book Detail"
                variant="solo"
                rows="6"
                v-model="book.newBook.bookDetail"
                :rules="[rules.required]"
              ></v-textarea>
              <div class="tw-space-x-2 tw-flex tw-items-center">
                <span class="tw-font-bold tw-text-[#1D419F] tw-text-lg"
                  >Tags:</span
                >
                <v-combobox
          v-model="book.newBook.bookTag"
          label="Enter your tag"
          variant="solo-filled"
          multiple
          chips
          clearable
        ></v-combobox>
              </div>
            </div>
          </v-col>
        </v-row>
      </v-card>
    </div>

    <div class="d-flex justify-end tw-mx-[9rem] tw-space-x-4">
      <v-btn color="#1D419F" variant="outlined" @click="book.clearNewBook()"
        >clear</v-btn
      >
      <v-btn
        color="#1D419F"
        variant="flat"
        @click="book.createBook()"
        :disabled="
          book.newBook.bookName == '' ||
          book.newBook.author == '' ||
          book.newBook.bookName.length > 255 ||
          book.newBook.author.length > 255 ||
          book.newBook.booktypeId == null ||
          (book.newBookFile == null ? false : book.newBookFile[0].size > 50000000)
        "
        >submit</v-btn
      >
    </div>
    <CreateBookSuccessPopup
      :dialog="book.successfulPopup == 'show'"
      @close="book.closeSuccessfulPopup()"
    />
    <DuplicateBookPopup 
      :dialog="book.failPopup"
      @close="toggleBookFailPopup()"
    />
    <div v-if="book.successfulPopup == 'load'">
    <LoadingPopup />
  </div>
  </div>
</template>

<style scoped>
.inline-elements {
  display: flex;
  align-items: center;
}
</style>
