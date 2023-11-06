<script setup></script>

<template>
  <div>
    <!-- <v-container class="ma-0 ml-10"> -->
    <v-container>
      <v-row no-gutters>
        <v-col cols="10">
          <v-text-field label="Search" variant="solo-filled"> </v-text-field>
        </v-col>
        <!-- <v-text-field label="Search" variant="solo-filled">      </v-text-field> -->
        <v-col cols="1"><v-btn size="auto" class="pa-5" color="#082266" rounded="lg"> Search </v-btn></v-col>
        <v-col cols="1"><v-btn size="auto" class="ml-10 pa-5" color="#082266" rounded="lg"> <v-icon end
              icon="mdi mdi-filter-variant"></v-icon>
            Filter
          </v-btn></v-col>
      </v-row>

      <v-row no-gutters>
        <v-col cols="5">
          <v-btn size="auto" class="pa-5" color="#082266" rounded="lg"> Result - 3200 </v-btn>
        </v-col>
        <v-col cols="4"></v-col>
        <v-col cols="3"> <v-btn size="auto" class="pa-5 ml-12" color="#082266" rounded="lg"> Sort By: Result - 3200
            <v-icon end icon="mdi mdi-menu-down"></v-icon></v-btn>
        </v-col>

      </v-row>
    </v-container>


  </div>
</template>

<style scoped></style>