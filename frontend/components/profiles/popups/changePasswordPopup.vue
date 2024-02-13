<script setup>
defineEmits(["toggle","back"]);
defineProps({
    dialog: {
        type: Boolean,
        require: true,
    },
})
</script>
 
<template>
    <div v-show="dialog" class="tw-grid tw-h-100% tw-min-h-screen tw-w-screen tw-absolute tw-inset-0 tw--top-[5.5rem] tw-bg-black/50">
        <v-card style="height: 25rem; width: 50rem;" class="rounded-xl tw-place-self-center">
        <v-card-title style="height: 5rem; width: 50rem;">
          <p class="action-popUp-header tw-flex tw-justify-center tw-my-4">Change Password</p>
          <div class="popup-horizontal-line"></div>
        </v-card-title>
        <v-card-text class="py-8" style="height: 15rem; width: 50rem;">
                            <div class="tw-space-y-4">
                                <v-row class="tw-flex tw-items-center tw-space-x-6" no-gutters>
                                    <v-col cols="4" class="d-flex tw-place-content-end">
                                        <span class="web-text-sub ">Old password</span>
                                    </v-col>
                                    <v-col cols="7">
                                        <v-text-field label="Enter old password" variant="solo" hide-details></v-text-field>
                                    </v-col>
                                </v-row>
                                <v-row no-gutters class="tw-flex tw-items-center tw-space-x-6">
                                    <v-col cols="4" class="d-flex tw-place-content-end">
                                        <span class="web-text-sub">New password</span>
                                    </v-col>
                                    <v-col cols="7">
                                        <v-text-field label="Enter new password" variant="solo" hide-details></v-text-field>
                                    </v-col>
                                </v-row>
                                <v-row no-gutters class="tw-flex tw-items-center tw-space-x-6">
                                    <v-col cols="4" class="d-flex tw-place-content-end">
                                        <span class="web-text-sub">Confirm new password</span>
                                    </v-col>
                                    <v-col cols="7">
                                        <v-text-field label="Enter confirm password" variant="solo"
                                            hide-details></v-text-field>
                                    </v-col>
                                </v-row>
                            </div>
        </v-card-text>
        <v-card-actions style="height: 5rem; width: 50rem;">
          <v-spacer></v-spacer>
          <v-btn class="tw-mx-2" style="height: 3rem; width: 7rem;" color="#727272" variant="tonal" size="large" rounded="xl" @click="$emit('toggle')">Cancle</v-btn>
          <v-btn class="tw-mx-4" style="height: 3rem; width: 7rem;" color="#1646C4" variant="flat" size="large" rounded="xl" @click="$emit('toggle'),$emit('back')">Submit</v-btn>
        </v-card-actions>
      </v-card>
    </div>
</template>
 
<style>

</style>