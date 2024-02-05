<script setup>
import { mergeProps } from "vue";
defineEmits(["toggle", "set"]);

defineProps({
  userList: {
    type: Array,
    require: true,
  },
});
</script>

<template>
  <div class="tw-px-36 tw-space-y-4">
    <v-card
      v-for="user in userList"
      class="border-md rounded-pill"
      :to="`/user/${user.userId}/`"
    >
      <v-row no-gutters class="my-5" >
        <v-col cols="2" align="center">
          <v-avatar size="80px" class="border-md">
            <v-img alt="Avatar" src="/image/guest_icon.png"></v-img
          ></v-avatar>
        </v-col>
        <v-col cols="3" class="web-text-detail d-flex align-center justify-center">
          <p>{{ user.displayName }}</p>
        </v-col>
        <v-col cols="3" class="web-text-detail d-flex align-center justify-center">
          <p>{{ user.email }}</p>
        </v-col>
        <v-col cols="2" class="web-text-detail d-flex align-center justify-center">
          <p>{{ user.role }}</p>
        </v-col>
        <v-col cols="2" class="d-flex align-center justify-center">
          <span class="text-center">
            <v-menu>
              <template v-slot:activator="{ props: menu }">
                <v-tooltip location="top">
                  <template v-slot:activator="{ props: tooltip }"> 
                    <v-icon
                      icon="mdi mdi-dots-horizontal-circle-outline"
                      size="x-large"
                      v-bind="mergeProps(menu, tooltip)"
                    ></v-icon>
                  </template>
                  <span>More</span>
                </v-tooltip>
              </template>
              <v-list>
                <v-list-item :to="`../../user/update_${user.userId}/`">
                  <v-list-item-title class="web-text-detail tw-space-x-2"
                    ><v-icon icon="mdi mdi-pencil-outline"></v-icon
                    ><span>Edit this user</span></v-list-item-title
                  >
                </v-list-item>
                <v-list-item class="hover:tw-bg-zinc-300/20 tw-cursor-pointer">
                  <v-list-item-title class="web-text-detail">
                    <v-list-item-title
                      class="web-text-detail tw-space-x-2"
                      @click="$emit('toggle'), $emit('set', user.userId)"
                      ><v-icon icon="mdi mdi-trash-can-outline"></v-icon
                      ><span>Delete this user</span></v-list-item-title
                    >
                  </v-list-item-title>
                </v-list-item>
              </v-list>
            </v-menu>
          </span>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<style></style>
