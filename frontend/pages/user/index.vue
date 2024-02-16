<script setup>
import { ref } from "vue";
import userCard from "~/components/users/userCard.vue";
// import BookNotFound from "~/components/books/bookNotFound.vue";
import { useUsers } from "~/stores/user";

const user = useUsers();
const page = ref(1);

onBeforeMount(async () => {
  user.clearUserList();
  await user.getUserList();
});
</script>

<template>
  <div class="tw-min-h-[80%]">
    <v-container>
      <v-row no-gutters>
        <v-col cols="9">
          <v-text-field label="Search" variant="solo-filled"> </v-text-field>
        </v-col>
        <v-col cols="1"
          ><v-btn size="auto" class="pa-5" color="#082266" rounded="lg">
            Search
          </v-btn></v-col
        >
        <v-col cols="1"
          ><v-btn size="auto" class="tw-mx-8 pa-5" color="#082266" rounded="lg">
            <v-icon icon="mdi mdi-filter-variant"></v-icon> </v-btn
        ></v-col>
        <v-col cols="1">
          <!-- <NuxtLink to="/user/create/"
            ><v-btn
              size="auto"
              class="tw-mx-7 pa-5"
              color="#082266"
              rounded="lg"
            >
              <v-icon icon="mdi mdi-plus"></v-icon> </v-btn></NuxtLink> -->
            </v-col>
      </v-row>

      <v-row no-gutters>
        <v-col cols="5">
          <v-btn size="auto" class="pa-5" color="#082266" rounded="lg">
            Result - {{ user.userList.data.totalElements }}
          </v-btn>
        </v-col>
        <v-col cols="4"></v-col>
        <v-col cols="3">
          <v-btn size="auto" class="pa-5 ml-12" color="#082266" rounded="lg">
            Sort By: Result - {{ user.userList.data.totalElements }}
            <v-icon end icon="mdi mdi-menu-down"></v-icon
          ></v-btn>
        </v-col>
      </v-row>
    </v-container>

    <div class="tw-px-36 tw-space-y-4 tw-my-4">
        <v-card class="border-md rounded-pill" color="#082266">
          <v-row no-gutters class="my-5">
            <v-col cols="2" class="user-text-title d-flex align-center justify-center">
              <p>Profile</p>
            </v-col>
            <v-col cols="3" class="user-text-title d-flex align-center justify-center">
              <p>Display Name</p>
            </v-col>
            <v-col cols="3" class="user-text-title d-flex align-center justify-center">
              <p>Email</p>
            </v-col>
            <v-col cols="2" class="user-text-title d-flex align-center justify-center">
              <p>Role</p>
            </v-col>
            <v-col cols="2"></v-col>
          </v-row>
        </v-card>
      </div>

    <!-- <BookNotFound v-show="user.userList.data.content.length == 0" /> -->
    <div v-show="user.userList.data.content.length !== 0" class="py-2">
      <userCard :userList="user.userList.data.content" />
    </div>
  </div>
  <div v-show="user.userList.data.content.length !== 0">
    <v-pagination
      v-model="page"
      :length="user.userList.data.totalPages"
      :total-visible="7"
      rounded="20"
      @update:model-value="user.changeUserPage(page)"
    >
    </v-pagination>
  </div>
</template>

<style scoped></style>
