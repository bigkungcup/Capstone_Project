<script setup>
defineEmits(["toggle","upload","update","delete","back"]);
defineProps({
    popupDetail: {
        type: Object,
        require: true,
    },
    dialog: {
        type: Boolean,
        require: true,
    },
})
</script>
 
<template>
    <div v-show="dialog" class="tw-grid tw-h-100% tw-min-h-screen tw-w-screen tw-absolute tw-inset-0 tw--top-[5.5rem] tw-bg-black/50">
    <v-card style="height: 20rem; width: 40rem;" class="rounded-xl tw-place-self-center" v-show="popupDetail.operation == 'create'">
        <v-card-title style="height: 5rem; width: 40rem;">
          <p class="action-popUp-header tw-flex tw-justify-center tw-my-4">{{ popupDetail.title }}</p>
          <div class="horizontal-line"></div>
        </v-card-title>
        <v-card-text class="d-flex align-content-center justify-center flex-wrap" style="height: 10rem; width: 40rem;"><div class="action-popUp-detail">{{ popupDetail.detail }}</div></v-card-text>
        <v-card-actions style="height: 5rem; width: 40rem;">
          <v-spacer></v-spacer>
          <v-btn class="tw-mx-2" style="height: 3rem; width: 7rem;" color="#727272" variant="tonal" size="large" rounded="xl" @click="$emit('toggle')">Cancle</v-btn>
          <v-btn class="tw-mx-4" style="height: 3rem; width: 7rem;" color="#1646C4" variant="flat" size="large" rounded="xl" @click="$emit('upload'),$emit('toggle')">{{ popupDetail.buttonName }}</v-btn>
        </v-card-actions>
      </v-card>

      <v-card style="height: 20rem; width: 40rem;" class="rounded-xl tw-place-self-center" v-show="popupDetail.operation == 'update'">
        <v-card-title style="height: 5rem; width: 40rem;">
          <p class="action-popUp-header tw-flex tw-justify-center tw-my-4">{{ popupDetail.title }}</p>
          <div class="horizontal-line"></div>
        </v-card-title>
        <v-card-text class="d-flex align-content-center justify-center flex-wrap" style="height: 10rem; width: 40rem;"><div class="action-popUp-detail">{{ popupDetail.detail }}</div></v-card-text>
        <v-card-actions style="height: 5rem; width: 40rem;">
          <v-spacer></v-spacer>
          <v-btn class="tw-mx-2" style="height: 3rem; width: 7rem;" color="#727272" variant="tonal" size="large" rounded="xl" @click="$emit('toggle')">Cancle</v-btn>
          <v-btn class="tw-mx-4" style="height: 3rem; width: 7rem;" color="#1646C4" variant="flat" size="large" rounded="xl" @click="$emit('update'),$emit('toggle')">{{ popupDetail.buttonName }}</v-btn>
        </v-card-actions>
      </v-card>

      <v-card style="height: 20rem; width: 40rem;" class="rounded-xl tw-place-self-center" v-show="popupDetail.operation == 'leave'">
        <v-card-title style="height: 5rem; width: 40rem;">
          <p class="action-popUp-header tw-flex tw-justify-center tw-my-4">{{ popupDetail.title }}</p>
          <div class="horizontal-line"></div>
        </v-card-title>
        <v-card-text class="d-flex align-content-center justify-center flex-wrap" style="height: 10rem; width: 40rem;"><div class="action-popUp-detail">{{ popupDetail.detail }}</div></v-card-text>
        <v-card-actions style="height: 5rem; width: 40rem;">
          <v-spacer></v-spacer>
          <v-btn class="tw-mx-2" style="height: 3rem; width: 7rem;" color="#727272" variant="tonal" size="large" rounded="xl" @click="$emit('toggle')">Cancle</v-btn>
          <v-btn class="tw-mx-4" style="height: 3rem; width: 7rem;" color="#1646C4" variant="flat" size="large" rounded="xl" @click="$emit('toggle'),$emit('back')">{{ popupDetail.buttonName }}</v-btn>
        </v-card-actions>
      </v-card>

      <v-card style="height: 20rem; width: 40rem;" class="rounded-xl tw-place-self-center" v-show="popupDetail.operation == 'delete'">
        <v-card-title style="height: 5rem; width: 40rem;">
          <p class="delete-popUp-header tw-flex tw-justify-center tw-my-4">{{ popupDetail.title }}</p>
          <div class="horizontal-line"></div>
        </v-card-title>
        <v-card-text class="d-flex align-content-center justify-center flex-wrap" style="height: 10rem; width: 40rem;"><div class="delete-popUp-detail">{{ popupDetail.detail }}</div></v-card-text>
        <v-card-actions style="height: 5rem; width: 40rem;">
          <v-spacer></v-spacer>
          <v-btn class="tw-mx-2" style="height: 3rem; width: 7rem;" color="#727272" variant="tonal" size="large" rounded="xl" @click="$emit('toggle')">Cancle</v-btn>
          <v-btn class="tw-mx-4" style="height: 3rem; width: 7rem;" color="#DC0000" variant="flat" size="large" rounded="xl" @click="$emit('delete'),$emit('toggle')">{{ popupDetail.buttonName }}</v-btn>
        </v-card-actions>
      </v-card>

      <v-card style="height: 20rem; width: 40rem;" class="rounded-xl tw-place-self-center" v-show="popupDetail.operation == 'validate'">
        <v-card-title style="height: 5rem; width: 40rem;">
          <p class="delete-popUp-header tw-flex tw-justify-center tw-my-4">{{ popupDetail.title }}</p>
          <div class="horizontal-line"></div>
        </v-card-title>
        <v-card-text class="d-flex align-content-center justify-center flex-wrap" style="height: 10rem; width: 40rem;"><div class="delete-popUp-detail">{{ popupDetail.detail }}</div></v-card-text>
        <v-card-actions style="height: 5rem; width: 40rem;">
          <v-spacer></v-spacer>
          <v-btn class="tw-mx-2" style="height: 3rem; width: 7rem;" color="#DC0000" variant="flat" size="large" rounded="xl" @click="$emit('toggle')">OK</v-btn>
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </div>
</template>
 
<style>
.horizontal-line{
    border-bottom: 2px solid #B8B4B4;
}
</style>