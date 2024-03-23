<script setup>
import { ref } from "vue";
defineEmits(["cancel", "submit"]);
defineProps({
  dialog: {
    type: Boolean,
    require: true,
  },
  title: {
    type: Array,
    require: true,
  },
  report: {
    type: Object,
    require: true,
  },
});

const problemId = ref('');

function handleProblemTitle(report,title) {
    report.problemId = problemId.value;
    report.reportTitle = title.find((element) => element.id == problemId.value);
}
</script>

<template>
  <div
    class="tw-grid tw-h-100% tw-min-h-screen tw-w-screen tw-absolute tw-inset-0 tw--top-[5.5rem] tw-bg-black/50"
  >
    <v-card
      style="min-height: 30rem; width: 50rem"
      class="rounded-xl tw-place-self-center"
    >
      <v-card-title class="tw-bg-[#1D419F]" style="height: 5rem; width: 50rem">
        <p
          class="action-popUp-header !tw-text-white tw-flex tw-justify-center tw-my-4"
        >
        What’s happening?
        </p>
      </v-card-title>
      <v-card-text class="py-6" style="height: 20rem; width: 50rem">
        <div class="tw-px-10 web-text-detail">
            <div class="tw-space-y-1">
            <p>What is the problem?</p>
            <v-select
          label="Please select problem : "
          class="tw-font-bold tw-text-white tw-text-xs " 
          v-model="problemId"
          :items="title"
          item-title="Name"
          item-value="id"
          variant="solo-filled" 
          bg-color="white" 
          rounded="lg"
          @update:model-value="handleProblemTitle(report,title)"
></v-select>
        </div>
        <div class="tw-space-y-1">
            <p>Description : </p>
            <v-textarea
            v-model="report.reportDetail"
            label="Report Detail"
            variant="solo"
            rows="5"
            clearable
          ></v-textarea>
        </div>
          <!-- <v-row class="tw-flex tw-items-center tw-space-x-6" no-gutters>
            <v-col cols="4" class="d-flex tw-place-content-end">
              <span class="web-text-detail">Old password</span>
            </v-col>
            <v-col cols="7">
              <v-text-field
                label="Enter old password"
                :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
                :type="visible ? 'text' : 'password'"
                density="compact"
                variant="solo"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row no-gutters class="tw-flex tw-items-center tw-space-x-6">
            <v-col cols="4" class="d-flex tw-place-content-end">
              <span class="web-text-detail">New password</span>
            </v-col>
            <v-col cols="7">
              <v-text-field
                label="Enter new password"
                :append-inner-icon="newVisible ? 'mdi-eye-off' : 'mdi-eye'"
                :type="newVisible ? 'text' : 'password'"
                density="compact"
                variant="solo"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row no-gutters class="tw-flex tw-items-center tw-space-x-6">
            <v-col cols="4" class="d-flex tw-place-content-end">
              <span class="web-text-detail">Confirm new password</span>
            </v-col>
            <v-col cols="7">
              <v-text-field
                label="Enter confirm password"
                :append-inner-icon="confirmVisible ? 'mdi-eye-off' : 'mdi-eye'"
                :type="confirmVisible ? 'text' : 'password'"
                density="compact"
                variant="solo"
              ></v-text-field>
            </v-col>
          </v-row> -->
        </div>
      </v-card-text>
      <v-card-actions style="height: 4em; width: 50rem">
        <v-spacer></v-spacer>
        <v-btn
          class="tw-mx-2"
          style="height: 3rem; width: 7rem"
          color="#727272"
          variant="tonal"
          size="large"
          rounded="xl"
          @click="$emit('cancel')"
          >Cancel</v-btn
        >
        <v-btn
          class="tw-mx-4"
          style="height: 3rem; width: 7rem"
          color="#1646C4"
          variant="flat"
          size="large"
          rounded="xl"
          >Submit</v-btn
        >
      </v-card-actions>
    </v-card>
  </div>
</template>

<style></style>
