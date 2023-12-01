<script setup>
defineProps({
    popupDetail: {
        type: Object,
        require: true,
    },
})
import { ref } from 'vue';

const operation = '';
const dialog = ref(false);
</script>

<template>
  <v-row v-show="true">
    <v-dialog v-model="dialog" persistent width="auto">
      <template v-slot:activator="{ props }">
        <v-btn color="primary" v-bind="props">{{ popupDetail.buttonName }}</v-btn>
      </template>
      <v-card justify="center" style="height: 20rem; width: 40rem;" >
        <v-card-title class="text-h5">{{ popupDetail.title }}</v-card-title>
        <v-card-text style="height: 15rem; width: 15rem;">{{ popupDetail.detail }}</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green-darken-1" variant="text" @click="dialog = false">Cancle</v-btn>
          <v-btn color="green-darken-1" variant="text" @click="dialog = false">{{ popupDetail.buttonName }}</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>

  <!-- <v-row v-show="false">
    <v-dialog v-model="dialog" persistent width="auto">
      <template v-slot:activator="{ props }">
        <v-btn color="primary" v-bind="props">Open Dialog</v-btn>
      </template>
      <v-card justify="center">
        <v-card-title class="text-h5">Use Google's location service?</v-card-title>
        <v-card-text style="height: 15rem;">Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green-darken-1" variant="text" @click="dialog = false">Disagree</v-btn>
          <v-btn color="green-darken-1" variant="text" @click="dialog = false">Agree</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>

  <v-row v-show="false">
    <v-dialog v-model="dialog" persistent width="auto">
      <template v-slot:activator="{ props }">
        <v-btn color="primary" v-bind="props">Open Dialog</v-btn>
      </template>
      <v-card justify="center">
        <v-card-title class="text-h5">Use Google's location service?</v-card-title>
        <v-card-text style="height: 15rem;">Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green-darken-1" variant="text" @click="dialog = false">Disagree</v-btn>
          <v-btn color="green-darken-1" variant="text" @click="dialog = false">Agree</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row> -->
</template>

<style scoped></style>
  