<script setup>
import { ref } from "vue";
import BookCard from '~/components/books/bookCard.vue';
import BookNotFound from "~/components/books/bookNotFound.vue";
import { useBooks } from '~/stores/book'

const library = useBooks();
const roleToken = ref(localStorage.getItem('role'));
const page = ref(1)
const result = ref(0);
const dialog = ref(false);
const booktype = ref('');
const sortList = [
  {
    id: 1,
    Name: 'Newest' ,
    value: 'desc'
  },
  {
    id: 2,
    Name: 'Oldest' ,
    value: 'asc'
  },  
  {
    id: 3,
    Name: 'Total Reviews' ,
    value: 'bookTotalReview'
  },  
  {
    id: 4,
    Name: 'Total Rating' ,
    value: 'bookRating'
  },
];

function handleSelectionChange() {  
  booktype.value = library.filterBook != 0 ? library.bookType.find(({ booktypeId }) => booktypeId ===  library.filterBook) : ''

  if (roleToken.value == 'GUEST') {
    library.getLibraryByGuest();
    result.value = library.bookList.data.totalElements ? library.bookList.data.totalElements : 0
  }else{
    library.getLibrary();
    result.value = library.bookList.data.totalElements ? library.bookList.data.totalElements : 0
  }
}

onBeforeMount( async () => {
  if (roleToken.value == 'GUEST') {
    await library.getLibraryByGuest();
    library.getBookType();
    result.value = library.bookList.data.totalElements ? library.bookList.data.totalElements : 0
  }else{
    await library.getLibrary();
    library.getBookType();
    result.value = library.bookList.data.totalElements ? library.bookList.data.totalElements : 0
  }
  booktype.value = library.filterBook != 0 ? library.bookType.find(({ booktypeId }) => booktypeId ===  library.filterBook) : ''
});


</script>

<template>
  <div class="tw-min-h-[80%]">
    <v-container>
      <v-row no-gutters>
        <v-col cols="9">
          <v-text-field
          placeholder="Search your book or auther name"
          prepend-inner-icon="mdi-magnify"
          v-model="library.searchBook"
          clearable
          >
        </v-text-field>
        </v-col>
        <v-col cols="1" v-if="roleToken === 'GUEST'"><v-btn size="auto" class="pa-5" color="#082266" rounded="lg" @click="library.getLibraryByGuest()"> Search </v-btn></v-col>
        <v-col cols="1" v-if="roleToken !== 'GUEST'"><v-btn size="auto" class="pa-5" color="#082266" rounded="lg" @click="library.getLibrary()"> Search </v-btn></v-col>
        <v-col cols="1"><v-btn size="auto" class="tw-mx-8 pa-5" color="#082266" rounded="lg" @click="dialog = true"> 
          <v-icon icon="mdi mdi-filter-variant"></v-icon>
          </v-btn></v-col>
          <v-col cols="1"> 
          <NuxtLink to="/book/create/" v-show="roleToken !== 'GUEST'"><v-btn size="auto" class="tw-mx-7 pa-5" color="#082266" rounded="lg"> 
          <v-icon icon="mdi mdi-plus"></v-icon>
          </v-btn></NuxtLink>
        </v-col>
      </v-row>

      <v-row no-gutters>
        <v-col cols="5" class="tw-space-x-5">
          <v-btn size="auto" class="pa-5" color="#082266" rounded="lg"> Result - {{ result }} </v-btn>
          <v-chip
          v-if="booktype != ''"           
          variant="elevated" 
          color="#1D419F"
          size="large" 
          >{{ booktype.booktypeName }}</v-chip>
        </v-col>
        <v-col cols="4">
          
        </v-col>
        <v-col cols="3"> 
          <!-- <v-btn size="auto" class="pa-5 ml-12" color="#082266" rounded="lg"> Sort By: Result - {{ result }}
            <v-icon end icon="mdi mdi-menu-down"></v-icon>
          </v-btn> -->
          <v-select
          label="SORT BY: "
          class="tw-font-bold tw-text-white tw-text-xs " 
          v-model="library.sortBook"
          :items="sortList"
          item-title="Name"
          item-value="value"
          variant="solo-filled" 
          bg-color="#082266" 
          rounded="lg"
          @update:model-value="handleSelectionChange()"
></v-select>
        </v-col>

      </v-row>
    </v-container>
    
    <v-dialog v-model="dialog">
      <v-row justify="center">
        <v-col cols="12" sm="7" md="6" lg="5">
      <v-sheet elevation="10" rounded="xl">
        <v-sheet class="d-flex pa-3 justify-center" rounded="t-xl" color="#082266">
          <v-card-title>SELECT BOOK TYPE</v-card-title>
        </v-sheet>
    <v-card>
        <v-card-text style="height: 300px; overflow-y: auto;">
          <div class="pa-4">
          <v-chip-group selected-class="bg-blue-darken-2" column v-model="library.filterBook">
            <v-chip :key="0" >
              All
            </v-chip>
            <v-chip v-for="tag in library.bookType" :key="tag.booktypeId">
              {{ tag.booktypeName }}
            </v-chip>
          </v-chip-group>
        </div>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions class="d-flex justify-end">
          <v-spacer></v-spacer>
          <v-btn color="blue-darken-1" variant="text" @click="dialog = false,library.filterBook = 0">
            Close
          </v-btn>
          <v-btn color="blue-darken-1" variant="text" @click="dialog = false,handleSelectionChange()">
            Save
          </v-btn>
        </v-card-actions>
      </v-card></v-sheet></v-col>
    </v-row>
  </v-dialog>
  
    <BookNotFound v-show="library.bookList.data.content.length == 0" />
    <div v-show="library.bookList.data.content.length !== 0" >
      <BookCard :bookList="library.bookList.data.content"/>
    </div>

    </div>
    <div v-show="library.bookList.data.content.length !== 0">
    <v-pagination v-model="page" :length="library.bookList.data.totalPages" :total-visible="7"
        rounded="20" @update:model-value="library.changeLibraryPage(page)">
    </v-pagination></div>
</template>

<style scoped></style>