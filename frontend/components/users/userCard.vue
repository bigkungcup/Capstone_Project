<script setup>
defineEmits(["toggle", "set"]);

defineProps({
  userList: {
    type: Array,
    require: true,
  },
});

function bookCoverPath(filePath) {
  return (filePath = `../../ej2/_nuxt/@fs/${filePath}`);
}

</script>

<template>
  <div class="tw-px-36 tw-space-y-4">
    <v-card
      v-for="user in userList"
      class="border-md rounded-pill"
      :to="`/user/${user.userId}`+'/'"
    >
      <v-row no-gutters class="my-5">
        <v-col cols="2" align="center">
          <v-avatar size="80px" class="border-md">
            <v-img alt="Avatar" src="/image/guest_icon.png" v-show="user.file == null" ></v-img>
            <v-img alt="Avatar" :src="bookCoverPath(user.file)" cover v-show="user.file != null" />
          </v-avatar>
        </v-col>
        <v-col
          cols="3"
          class="web-text-detail d-flex align-center justify-center"
        >
          <p>{{ user.displayName }}</p>
        </v-col>
        <v-col
          cols="3"
          class="web-text-detail d-flex align-center justify-center"
        >
          <p>{{ user.email }}</p>
        </v-col>
        <v-col
          cols="2"
          class="web-text-detail d-flex align-center justify-center"
        >
          <p>{{ user.role }}</p>
        </v-col>
        <v-col cols="2" class="d-flex align-center justify-center web-text-detail">
          <v-icon
            icon="mdi mdi-chevron-right-circle-outline"
            size="x-large"
          ></v-icon>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<style></style>
