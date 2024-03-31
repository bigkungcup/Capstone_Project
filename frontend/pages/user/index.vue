<script setup>
import { ref } from "vue";
import userCard from "~/components/users/userCard.vue";
// import BookNotFound from "~/components/books/bookNotFound.vue";
import { useUsers } from "~/stores/user";
import { useRouter } from "vue-router";
import UserNotFound from "~/components/users/userNotFound.vue";

const user = useUsers();
const page = ref(1);
const router = useRouter();
const roleToken = ref(localStorage.getItem('role'));

onBeforeMount(async () => {
  if (roleToken.value == 'ADMIN') {
  user.clearUserList();
  await user.getUserList();
}else{
  router.push(`/UnauthenPage/`)
}
});
</script>

<template>
  <div class="tw-min-h-[80%]">
    <v-container>
      <v-row no-gutters>
        <v-col cols="9">
          <v-text-field 
          label="Search username or email" 
          variant="solo-filled"  
          v-model="user.searchUser"
          clearable> 
        </v-text-field>
        </v-col>
        <v-col cols="1"
          ><v-btn size="auto" class="pa-5" color="#082266" rounded="lg" @click="user.getUserList()">
            Search
          </v-btn></v-col
        >
        <v-col cols="2"
          >
          <!-- <v-btn size="auto" class="tw-mx-8 pa-5" color="#082266" rounded="lg">
            <v-icon icon="mdi mdi-filter-variant"></v-icon> </v-btn> -->
        <v-select
          label="Filter BY: "
          class="tw-font-bold tw-text-white tw-text-xs mx-3 my-1" 
          v-model="user.filterUser"
          :items="['All','Admin','User']"
          variant="solo-filled" 
          bg-color="#082266" 
          rounded="lg"
          @update:model-value="user.getUserList()"
></v-select>
      </v-col>
      </v-row>

      <v-row no-gutters>
        <v-col cols="5">
          <v-btn size="auto" class="pa-5" color="#082266" rounded="lg">
            Result - {{ user.userList.data.totalElements ? user.userList.data.totalElements : 0 }}
          </v-btn>
        </v-col>
      </v-row>
    </v-container>

    <div class="tw-px-36 tw-space-y-4 tw-my-4" v-if="user.userList.data.content.length !== 0">
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
    <div v-if="user.userList.data.content.length !== 0" class="py-2">
      <userCard :userList="user.userList.data.content" />
    </div>
    <div v-if="user.userList.data.content.length == 0">
    <UserNotFound />
  </div>
  </div>
  <div v-if="user.userList.data.content.length !== 0"> 
    <v-pagination
      v-model="page"
      :length="user.userList.data.totalPages"
      :total-visible="7"
      rounded="20"
      @update:model-value="user.changeUserPage(page)"
    >
    </v-pagination>{{}}
  </div>
</template>

<style scoped></style>
