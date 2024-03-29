<script setup>
import { ref } from "vue";
import { useBooks } from "~/stores/book";
import { useUsers } from "~/stores/user";
import BookRank from "~/components/ranking/bookRank.vue";
import UserRank from "~/components/ranking/userRank.vue";

const book = useBooks();
const user = useUsers();
const section = ref("book");
const roleToken = ref(localStorage.getItem('role'));

const sortList = [
  {
    id: 1,
    Name: "Total Followers",
    value: "followers",
  },
  {
    id: 2,
    Name: "Total Likes",
    value: "totalLike",
  },
];

async function handleFollow(userId) {
  await user.createFollower(userId)
  await user.getRankingUserList();
}

async function handleUnfollow(userId) {
  await user.deleteFollower(userId)
  await user.getRankingUserList();
}

async function handleGetRankingUser() {
  if(roleToken.value == "GUEST"){
    await user.getRankingUserListByGuest();
  }else{
    user.clearRankingUserList();
    await user.getRankingUserList();
  }
}

onBeforeMount(async () => {
  await book.getBookType();
  book.bookType.push({ booktypeId: 0, booktypeName: "All" });
  await book.getRankingBookList();
});
</script>

<template>
  <div class="tw-bg-[#D9D9D9]">
    <div class="tw-flex tw-place-content-center">
      <div class="tw-bg-white tw-w-[70rem]">
        <div class="tw-space-x-8 tw-mx-8 tw-my-8">
          <v-btn
            :variant="section == 'book' ? 'elevated' : 'outlined'"
            color="#1D419F"
            @click="section = 'book',book.getRankingBookList()"
            >BOOK</v-btn
          >
          <v-btn
            :variant="section == 'user' ? 'elevated' : 'outlined'"
            color="#1D419F"
            @click="section = 'user',handleGetRankingUser()"
            >USER</v-btn
          >
        </div>
        <hr />
        <div class="tw-mx-8 tw-mt-6">
          <v-row>
            <v-col cols="3">
              <v-select
                label=""
                class="tw-font-bold tw-text-white tw-text-xs"
                v-if="section == 'book'"
                v-model="book.rankingFilter"
                :items="book.bookType"
                item-title="booktypeName"
                item-value="booktypeId"
                variant="solo-filled"
                bg-color="#082266"
                rounded="lg"
                @update:model-value="book.getRankingBookList()"
              ></v-select>

              <v-select
                label="SORT BY: "
                class="tw-font-bold tw-text-white tw-text-xs"
                v-if="section == 'user'"
                v-model="user.rankingSort"
                :items="sortList"
                item-title="Name"
                item-value="value"
                variant="solo-filled"
                bg-color="#082266"
                rounded="lg"
                @update:model-value="handleGetRankingUser()"
              ></v-select>
            </v-col>
            <v-col cols="9"></v-col>
          </v-row>
        </div>

        <div class="tw-flex tw-justify-center">
          <v-icon style="font-size: 5rem">
            <svg
              fill="#FFC73A"
              viewBox="0 0 36 36"
              xmlns="http://www.w3.org/2000/svg"
            >
              <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
              <g
                id="SVGRepo_tracerCarrier"
                stroke-linecap="round"
                stroke-linejoin="round"
              ></g>
              <g id="SVGRepo_iconCarrier">
                <title>crown-solid</title>
                <g
                  id="b4dd88e3-aa37-48df-864c-a0d0dc35fb0b"
                  data-name="Layer 3"
                >
                  <path
                    d="M2.6,11.93A1.4,1.4,0,1,0,4,13.33,1.4,1.4,0,0,0,2.6,11.93Z"
                  ></path>
                  <ellipse cx="33.83" cy="13.33" rx="1.39" ry="1.39"></ellipse>
                  <path
                    d="M18.22,6.39A1.39,1.39,0,1,0,16.84,5,1.39,1.39,0,0,0,18.22,6.39Z"
                  ></path>
                  <path
                    d="M31.63,16.1A18.61,18.61,0,0,0,28,17.34a21.57,21.57,0,0,0-4,2.49,19.2,19.2,0,0,1-2.26-3.49,48.92,48.92,0,0,1-2.52-6.58,1,1,0,0,0-1-.71h0a1,1,0,0,0-1,.71,48.42,48.42,0,0,1-2.52,6.58,18.69,18.69,0,0,1-2.26,3.48,22.81,22.81,0,0,0-4-2.48A18.83,18.83,0,0,0,4.9,16.1a1,1,0,0,0-1,.33,1,1,0,0,0-.13,1.07,55.9,55.9,0,0,1,4,13.5,1,1,0,0,0,1,.83h19a1,1,0,0,0,1-.83,55.9,55.9,0,0,1,4-13.5,1,1,0,0,0-.13-1.07A1,1,0,0,0,31.63,16.1ZM11.08,28.55a1.11,1.11,0,1,1,1.1-1.11A1.11,1.11,0,0,1,11.08,28.55Zm7.15,0a1.11,1.11,0,0,1,0-2.21,1.11,1.11,0,0,1,0,2.21Zm7.16,0a1.11,1.11,0,1,1,1.1-1.11A1.11,1.11,0,0,1,25.39,28.55Z"
                  ></path>
                </g>
              </g>
            </svg>
          </v-icon>
        </div>

        <div class="tw-flex tw-justify-center ranking-text-header">
          Top 10 All time
        </div>
        <div class="tw-mx-8" v-if="section == 'book'">
          <BookRank :bookList="book.rankingBookList.data" />
        </div>
        <div class="tw-mx-8" v-if="section == 'user'">
          <UserRank :userList="user.rankingUserList.data" :sort="user.rankingSort" @follow="handleFollow($event)" @unfollow="handleUnfollow($event)"/>
        </div>
      </div>
    </div>
  </div>
</template>

<style></style>
