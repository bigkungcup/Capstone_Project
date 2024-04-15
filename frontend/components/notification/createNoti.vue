<script setup>
import { useNotifications } from "~/stores/notification";
defineEmits(["cancel", "submit"]);
const noti = useNotifications();

const rules = {
  required: (value) => !!value || "Field is required",
};
</script>

<template>
  <div
    class="tw-grid tw-h-100% tw-min-h-screen tw-w-screen tw-absolute tw-inset-0 tw-bg-black/50"
  >
    <v-card
      style="min-height: 32rem; width: 50rem"
      class="rounded-xl tw-place-self-center"
    >
      <v-card-title class="tw-bg-[#1D419F]" style="height: 5rem; width: 50rem">
        <p
          class="action-popUp-header !tw-text-white tw-flex tw-justify-center tw-my-4"
        >
        Notification
        </p>
      </v-card-title>
      <v-card-text class="py-6" style="height: 22rem; width: 50rem">
        <div class="tw-px-10 web-text-detail">
            <div class="tw-space-y-1">
            <p>Title :</p>
            <v-textarea
            label=""
            v-model="noti.newNotification.title"
            variant="solo"
            rows="2"
            :rules="[rules.required]"
            clearable
          ></v-textarea>
        </div>
        <div class="tw-space-y-1">
            <p>Detaile : </p>
            <v-textarea
            label=""
            v-model="noti.newNotification.detail"
            variant="solo"
            rows="4"
            :rules="[rules.required]"
            clearable
          ></v-textarea>
        </div>
        <p>Sent to: Everyone</p>
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
          :disabled="noti.newNotification.title == '' || noti.newNotification.detail == ''"
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
