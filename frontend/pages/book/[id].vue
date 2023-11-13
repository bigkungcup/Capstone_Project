<script setup>

const library = {
  data: [    
    {
      bookId: '01',
      bookName: 'I Know Why the Caged Bird Sings',
      bookDetail: 'Maya Angelou’s debut memoir is a modern American classic beloved worldwide. Her life story is told in the documentary film And Still I Rise, as seen on PBS’s American Masters.Here is a book as joyous and painful, as mysterious and memorable, as childhood itself. I Know Why the Caged Bird Sings captures the longing of lonely children, the brute insult of bigotry, and the wonder of words that can make the world right. Maya Angelou’s debut memoir is a modern American classic beloved worldwide. Sent by their mother to live with their devout, self-sufficient grandmother in a small Southern town, Maya and her brother, Bailey, endure the ache of abandonment and the prejudice of the local “powhitetrash.” At eight years old and back at her mother’s side in St. Louis, Maya is attacked by a man many times her age—and has to live with the consequences for a lifetime.',
      author: 'Maya Angelou',
      bookType: 'Autobiography',
      bookGenre: 'Biography',
      bookTotalBookmarked: 124,
      bookTotalReview: 3,
      bookRating: 5.0,
      updateDate: '',
      bookCover: null
    }
  ]
}
</script>
 
<template>
    <div class="ma-2"><v-icon end icon="mdi mdi-chevron-left"></v-icon> Back </div>
    <div class="tw-flex tw-justify-center tw-h-3/5 tw-max-h-[30rem] tw-min-h-[30rem]">
        <v-card class="bg-opct" color="#D9D9D9"  width="80%">
            <!-- <v-row no-gutters>
                <v-col cols="10"></v-col>
                <v-col cols="2" class="tw-flex tw-justify-center"><v-btn >Report</v-btn></v-col>
            </v-row> -->
            <v-row no-gutters>
                <v-col cols="4" class="tw-my-4" align="center">
                    <v-img src="/image/bookcover.png" height="60%" width="100%"></v-img>
                    <v-img src="/image/star.png" width="50%"></v-img>
                </v-col>
                <v-col cols="6" class="tw-my-4 tw-space-y-10">
                    <p class="text-h4 tw-text-[#082266]">
                        {{library.data[0].bookName}}
                    </p>

                    <p class="text-h5 tw-text-[#1D419F]">
                        Author: {{library.data[0].author}}
                    </p>
                    <p class="text-h6 tw-text-[#1D419F]">
                        Bookmarked by: {{library.data[0].bookTotalBookmarked}} people
                    </p>
                    <p class="text-h6 tw-text-[#1D419F]">
                        Booktype: <v-btn color="#1D419F">{{library.data[0].bookType}}</v-btn>
                    </p>
                    <p class="text-h6 tw-text-[#1D419F]">
                        Genre: <v-btn color="#1D419F">{{library.data[0].bookGenre}}</v-btn>
                    </p>
                    <div class="tw-flex tw-justify-center tw-gap-x-12">
                        <v-btn class="text-none" color="#1D419F"><v-icon start
                                icon="mdi mdi-bookmark"></v-icon>Bookmark</v-btn>
                        <v-btn class="text-none" color="#1D419F"><v-icon start
                                icon="mdi mdi-pencil-plus"></v-icon>Review</v-btn>
                    </div>
                </v-col>
                <v-col cols="2" class="tw-flex tw-justify-center my-2"><v-btn >Report</v-btn></v-col>
            </v-row>

        </v-card>

    </div>

    <div class="tw-flex tw-justify-center my-4">
        <v-card class="bg-opct" color="#D9D9D9" width="80%">
            <div class="tw-bg-white tw-flex tw-justify-center tw-m-5 tw-p-4 tw-text-xl tw-rounded-md">
                <!-- Maya Angelou’s debut memoir is a modern American classic beloved worldwide.
                Her life story is told in the documentary film And Still I Rise, as seen on PBS’s American Masters.Here is a
                book as joyous and painful,
                as mysterious and memorable, as childhood itself. I Know Why the Caged Bird Sings captures the longing of
                lonely children, the brute insult of bigotry, and the wonder of words that can make the world right. Maya
                Angelou’s
                debut memoir is a modern American classic beloved worldwide. Sent by their mother to live with their devout,
                self-sufficient grandmother in a small Southern town, Maya and her brother, Bailey, endure the ache of
                abandonment
                and the prejudice of the local “powhitetrash.” At eight years old and back at her mother’s side in St.
                Louis, Maya is attacked by a man many times her age—and has to live with the consequences for a lifetime. -->
                {{library.data[0].bookDetail}}
            </div>
        </v-card>
    </div>

    <div class="tw-flex tw-justify-left my-2 tw-bg-[#D9D9D9] tw-min-h-[24rem] bg-opct">
        <p class="tw-text-4xl tw-text-[#082266] tw-px-6 tw-py-2">Similar Book</p>
    </div>

    <div class="tw-flex tw-justify-center my-2 ">
        <div class="tw-bg-[#D9D9D9] tw-w-10/12 tw-min-h-[60rem] tw-rounded-lg bg-opct">
            <p class="tw-text-3xl tw-text-[#082266] tw-px-6 tw-py-2"> Review (0): </p>
            <div class="tw-bg-white tw-flex tw-justify-center tw-m-5 tw-p-4 tw-min-h-[56rem] tw-rounded-lg">
                <v-container>
                    <v-row no-gutters>
                        <v-col cols="10">
                            <v-text-field label="Search" variant="solo-filled"> </v-text-field>
                        </v-col>
                        <v-col cols="1"><v-btn  height="58" class="pa-5" color="#082266" rounded="lg"> Search
                            </v-btn></v-col>
                        <v-col cols="1"><v-btn height="58" class="mx-5 pa-5" color="#082266" rounded="lg"> <v-icon 
                                    icon="mdi mdi-filter-variant"></v-icon>
                                Filter
                            </v-btn></v-col>
                    </v-row>
                    <v-row> <v-img src="/image/rvnotfound.png" width="40%" class="tw-opacity-50"></v-img></v-row>
                </v-container>
               
            </div>
        </div>
    </div>
</template>
 
<style scoped>

.bg-opct {
    background-color: rgb(217, 217, 217, 0.6) !important;
    color: rgb(0, 0, 0) !important;
    caret-color: rgb(0, 0, 0) !important;
    /* width: 80% !important; */
}

</style>