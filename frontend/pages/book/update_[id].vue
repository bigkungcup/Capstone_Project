<script setup>
import { useRouter } from "vue-router";
import { useBooks } from "~/stores/book";
import { ref } from "vue";
import UpdateBookSuccessPopup from "~/components/books/popups/updateBookSuccessPopup.vue";
import DuplicateBookPopup from "~/components/books/popups/duplicateBookPopup.vue";
import DeleteBookConfirmPopup from "~/components/books/popups/deleteBookConfirmPopup.vue";
import DeleteBookFailPopup from "~/components/books/popups/deleteBookFailPopup.vue";
import DeleteBookSuccessPopup from "~/components/books/popups/deleteBookSuccessPopup.vue";
import LoadingPopup from "~/components/popups/loadingPopup.vue";

const book = useBooks();
const route = useRoute();
const selectedImage = ref(null);
const validateSize = ref(false);
const roleToken = ref(localStorage.getItem('role'));
const router = useRouter();
const popupStatus = ref('');
const bookConfirmPopup = ref(false);

function showValidateSize() {
  validateSize.value = true;
}

function toggleBookFailPopup() {
  book.failPopup = !book.failPopup;
}

function handleFileChange() {
  if (book.editBookFile[0]) {
    selectedImage.value = URL.createObjectURL(book.editBookFile[0])
  }
}

const rules = {
  required: (value) => !!value || "Field is required",
  limited: (value) => value.length <= 255 || "Max 255 characters",
  size: (value) => !!value || value[0].size <= 50000000 || showValidateSize(),
};

function setSelectedImage() {
  selectedImage.value = book.editBook.file == null ? null : book.editBook.file
  book.editBookFile = undefined;
}

onBeforeRouteLeave(() => {
  if (roleToken.value == 'ADMIN' && book.bookDetail.data.bookName != '') {
  const coverCheck = selectedImage.value == null ? selectedImage.value != book.bookDetail.data.file : selectedImage.value != book.bookDetail.data.file;
  if (
    book.editBook.bookName !== book.bookDetail.data.bookName ||
    book.editBook.author !== book.bookDetail.data.author ||
    book.editBook.booktypeId !== book.bookDetail.data.booktype.booktypeId || 
    (Object.values(book.editBook.bookTag).join(', ') !== book.bookDetail.data.bookTag) || 
    book.editBook.bookDetail !== book.bookDetail.data.bookDetail ||
    coverCheck
  ) {
    if(book.leavePopup){
    const shouldShowPopup = confirm("Do you really want to leave?");
    if (shouldShowPopup) {
      return null
    } else {
      next(false); // Prevent leaving the page
    }
  }
}  }
});


if (roleToken.value == 'ADMIN') {
  await book.getBookDetail(route.params.id);
  book.setEditBook();
  book.getBookType();
}else{
  await book.getBookDetailByGuest(route.params.id);
  book.setEditBook();
}

onBeforeMount(() => {
  if (roleToken.value == 'ADMIN') {
    book.leavePopup = true;
    setSelectedImage();
  }else{
    book.leavePopup = true;
    setSelectedImage();
    router.push(`/UnauthenPage/`)
}
});

</script>

<template>
  <div class="tw-pt-1 tw-pb-10 tw-drop-shadow-lg tw-space-y-1" v-show="roleToken == 'ADMIN' && book.bookDetail.data.bookName != ''">
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
                  v-show="book.editBook.file == null && book.editBookFile == undefined"
                  width="250"
                  height="320"
                  cover
                ></v-img>
                <v-img
                  :src="selectedImage"
                  v-show="book.editBook.file != null || book.editBookFile != null"
                  width="250"
                  height="320"
                  cover
                ></v-img>
              </div>
              <div class="px-10" v-show="book.editBook.file != null || book.editBookFile != null">
              <v-btn block @click="book.editBook.file = null, book.editBookFile = null">
                  <p class="tw-font-bold tw-text-[#1D419F] tw-text-xs">
                    cancel
                  </p>
              </v-btn>
            </div>
              <v-responsive class="mx-auto my-2" max-width="200">
                <v-file-input
                  ref="fileInput"
                  v-model="book.editBookFile"
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
              <div class="d-flex justify-center">
                <v-autocomplete
                class="tw-font-bold tw-text-[#1D419F] tw-text-xs px-8"
                v-model="book.editBook.booktypeId"
                :items="book.bookType"
                item-title="booktypeName"
                item-value="booktypeId"
                label="Select Book type"
                variant="solo-filled"
                ></v-autocomplete>
              </div>
            </div>
          </v-col>
          <v-col cols="9">
            <div class="tw-mr-8 tw-my-8">
              <v-text-field
                label="Book Name"
                variant="solo"
                v-model="book.editBook.bookName"
                :rules="[rules.required, rules.limited]"
                counter
              ></v-text-field>
              <v-text-field
                label="Author"
                variant="solo"
                v-model="book.editBook.author"
                :rules="[rules.required, rules.limited]"
                counter
              ></v-text-field>
              <v-textarea
                label="Book Detail"
                variant="solo"
                rows="6"
                v-model="book.editBook.bookDetail"
                :rules="[rules.required]"
              ></v-textarea>
              <div class="tw-space-x-2 tw-flex tw-items-center">
                <span class="tw-font-bold tw-text-[#1D419F] tw-text-lg"
                  >Tags:</span
                >
                <v-combobox
          v-model="book.editBook.bookTag"
          label="Enter your tag"
          variant="solo-filled"
          multiple
          chips
          clearable
          :readonly="book.editBook.bookTag != null ? book.editBook.bookTag.length >= 5: false"
        ></v-combobox>
              </div>
            </div>
          </v-col>
        </v-row>
      </v-card>
    </div>

    <div class="d-flex justify-space-between">
      <div class="justify-start tw-mx-[9rem]">
        <v-btn
        color="red"
        variant="flat"
        @click="bookConfirmPopup = true,popupStatus = 'delete'"
        >Delete</v-btn>
      </div>
      <div class="justify-end tw-mx-[9rem] tw-space-x-4">
      <v-btn color="#1D419F" variant="outlined" @click="book.setEditBook(route.params.id).then(setSelectedImage())"
        >Reset</v-btn
      >
      <v-btn
        color="#1D419F"
        variant="flat"
        @click="book.updateBook(route.params.id),popupStatus = 'update'"
        :disabled="
          book.editBook.bookName == '' ||
          book.editBook.author == '' ||
          book.editBook.booktypeId == null ||
          book.editBook.bookName.length > 255 ||
          book.editBook.author.length > 255 ||
          (book.editBookFile == null ? false : book.editBookFile[0].size > 50000000)
        "
        >submit</v-btn
      ></div>
    </div>
    <UpdateBookSuccessPopup
      v-if="popupStatus == 'update'"
      :dialog="book.successfulPopup == 'show'"
      @close="book.closeSuccessfulPopup()"
    />
    <DuplicateBookPopup 
      :dialog="book.failPopup"
      @close="toggleBookFailPopup()"
    />
  </div>
  <div v-if="popupStatus == 'delete'">
      <DeleteBookConfirmPopup
        class="delete-popup"
        :dialog="bookConfirmPopup"
        @toggle="bookConfirmPopup = !bookConfirmPopup,popupStatus == ''"
        @delete="book.deleteBook(route.params.id)"
      />
      <DeleteBookFailPopup
        class="delete-popup"
        :dialog="book.failPopup"
        @close="book.failPopup = !book.failPopup,popupStatus == ''"
      />
      <DeleteBookSuccessPopup
        class="delete-popup"
        :dialog="book.successfulPopup == 'show'"
        @close="book.successfulPopup = 'hide',popupStatus == '',router.push(`/library/`)"
      />
    </div>
    <div v-if="book.successfulPopup == 'load'">
    <LoadingPopup />
  </div>
</template>

<style scoped>
.inline-elements {
  display: flex;
  align-items: center;
}
.delete-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
