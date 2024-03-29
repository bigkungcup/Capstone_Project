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

// const problemId = ref('');
// const idToken = ref(localStorage.getItem("id"));

// function handleProblemTitle(report,title) {
//     report.problemId = problemId.value;
//     let problem = title.find((element) => element.id == problemId.value);
//     report.reportTitle = problem.Name;
// }
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
          v-model="report.reportTitle"
          :items="title"
          item-title="Name"
          item-value="Name"
          variant="solo-filled" 
          bg-color="white" 
          rounded="lg"
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
          @click="$emit('submit')"
          >Submit</v-btn
        >
      </v-card-actions>
    </v-card>
  </div>
</template>

<style></style>
