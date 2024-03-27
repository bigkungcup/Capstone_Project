<script setup></script>

<template>
  <div
    class="tw-grid tw-h-100% tw-min-h-screen tw-w-screen tw-absolute tw-inset-0 tw--top-[5.5rem] tw-bg-black/50"
  >
    <v-card height="auto" width="36rem" class="rounded-xl tw-place-self-center">
      <v-card-title style="height: 5rem; width: 40rem">
        <p class="action-popUp-header tw-flex tw-justify-center tw-my-4">
          Report Detail
        </p>
      </v-card-title>
      <v-row>
        <v-col cols="8">
          <div class="web-text-sub tw-mx-8 tw-space-y-2">
            <div><v-chip>ID</v-chip><span class=""> 1</span></div>
            <div><v-chip>Category</v-chip><span class=""> Book report</span></div>
            <div><v-chip>Book</v-chip><span class=""> XXXName</span></div>
            <div>
              <v-chip>Type</v-chip
              ><span class=""> This book is duplicated.</span>
            </div>
            <div>
              <v-chip>Description</v-chip
              ><span class="">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit Ut enim
                ad Excepteur</span
              >
            </div>
            <div>
              <v-chip>Report by</v-chip><span class=""> Cat the reviewer</span>
            </div>
          </div>
        </v-col>
        <v-col cols="4" align="left">
          <!-- for book cover image -->
          <v-img src="/image/foryou2.jpg" width="140"></v-img>

         <!-- for profile image -->
          <!-- <v-img
            src="/image/cat.jpg"
            width="150"
            height="150"
            class="tw-rounded-full"
            cover
          ></v-img>  -->
        </v-col>
      </v-row>

      <v-card-actions class="tw-my-2">
        <v-spacer></v-spacer>
        <v-btn
          class="tw-mx-2"
          style="height: 3rem; width: 7rem"
          color="#727272"
          variant="tonal"
          size="large"
          rounded="xl"
          >Close</v-btn
        >
        
      </v-card-actions>
    </v-card>
  </div>
</template>

<style></style>
