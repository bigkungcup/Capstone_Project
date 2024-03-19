<script setup>
defineEmits(["follow","unfollow"]);
defineProps({
  userList: {
        type: Array,
        require: true,
    },
    sort:{
      type: String,
      require: true,
    }
})

const roleToken = ref(localStorage.getItem('role'));
const idToken = ref(localStorage.getItem('id'));

const testList = ref({
  data: [
        {
            userId: 2,
            displayName: "User Test",
            email: "TEST@mail.kmutt.ac.th",
            password: "$2a$10$2rbccrQoxynuu41qaWbXBuXdxgx4HyulU/Eu26qTVd1o.NFQcKfB6",
            role: "USER",
            followers: 4,
            followings: 0,
            totalReview: 7,
            totalFavoriteReview: 0,
            totalLike: 0,
            bio: "TEST CREATE",
            file: "http://localhost:8080/api/files/filesUser/2",
            followingReview: null
        },
        {
            "userId": 1,
            "displayName": "User Test2",
            "email": "TEST2@mail.kmutt.ac.th",
            "password": "$2a$10$5HODHNTCkWMqMJW8QDW22.l7A6KpxafYY91Pt0H2.NyMBjt3KxU1C",
            "role": "USER",
            "followers": 3,
            "followings": 1,
            "totalReview": 1,
            "totalFavoriteReview": 0,
            "totalLike": 0,
            "bio": "TEST CREATE",
            "file": "http://localhost:8080/api/files/filesUser/1",
            "followingReview": null
        },
        {
            "userId": 3,
            "displayName": "User Test3",
            "email": "TEST3@mail.kmutt.ac.th",
            "password": "$2a$10$V8qF8dcWsF1edFsgalRkg.08TwxqXXrrO6j4VzscYKkqJLrzF2jDy",
            "role": "USER",
            "followers": 2,
            "followings": 2,
            "totalReview": 1,
            "totalFavoriteReview": 0,
            "totalLike": 0,
            "bio": "TEST CREATE",
            "file": "http://localhost:8080/api/files/filesUser/3",
            "followingReview": null
        },
        {
            "userId": 4,
            "displayName": "User Test4",
            "email": "TEST4@mail.kmutt.ac.th",
            "password": "$2a$10$.UgvRWJOJywfHaoUKlD9eOZav1UTv26Do.I6ReQX.OMeasvgP5AVK",
            "role": "USER",
            "followers": 1,
            "followings": 3,
            "totalReview": 1,
            "totalFavoriteReview": 0,
            "totalLike": 0,
            "bio": "TEST CREATE",
            "file": "http://localhost:8080/api/files/filesUser/4",
            "followingReview": null
        },
        {
            "userId": 5,
            "displayName": "User Test5",
            "email": "TEST5@mail.kmutt.ac.th",
            "password": "$2a$10$9nloStyAasKyfVdeqTNEpeAOCa8y365b9X9tNebMP0HrokhsQmm82",
            "role": "USER",
            "followers": 0,
            "followings": 4,
            "totalReview": 1,
            "totalFavoriteReview": 0,
            "totalLike": 0,
            "bio": "TEST CREATE",
            "file": "http://localhost:8080/api/files/filesUser/5",
            "followingReview": null
        }
    ]
})

</script>

<template>
  <div>
    <!-- section 1 -->
    <div class="">
      <v-row no-gutters>
      <v-col cols="4" align="right">
        <div class="tw-pt-28" v-for="(user, index) in testList.data.slice(1,2)">
              <nuxt-link :to="`/user/${user.userId}/`">
            <v-img
              :src="user.file"
              v-if="user.file != null"
              width="180"
              height="180"
              class="tw-rounded-full tw-border-white tw-border-8"
              cover
            />
          <v-img
              src="/image/guest_icon.png"
              v-if="user.file == null"
              width="180"
              height="180"
              class="tw-rounded-full tw-border-white tw-border-8"
              cover
            />
          </nuxt-link>
        </div>
      </v-col>
      <v-col cols="4" align="center">
        <div class="tw-pt-20" v-for="(user, index) in testList.data.slice(0,1)">
          <nuxt-link :to="`/user/${user.userId}/`">
            <v-img
              :src="user.file"
              v-if="user.file != null"
              width="250"
              height="250"
              class="tw-rounded-full tw-border-white tw-border-8"
              cover
            />
          <v-img
              src="/image/guest_icon.png"
              v-if="user.file == null"
              width="250"
              height="250"
              class="tw-rounded-full tw-border-white tw-border-8"
              cover
            />
        </nuxt-link>
        </div>
      </v-col>
      <v-col cols="4" align="left">
        <div class="tw-pt-28" v-for="(user, index) in testList.data.slice(2,3)">
              <nuxt-link :to="`/user/${user.userId}/`">
            <v-img
              :src="user.file"
              v-if="user.file != null"
              width="180"
              height="180"
              class="tw-rounded-full tw-border-white tw-border-8"
              cover
            />
          <v-img
              src="/image/guest_icon.png"
              v-if="user.file == null"
              width="180"
              height="180"
              class="tw-rounded-full tw-border-white tw-border-8"
              cover
            />
          </nuxt-link>
            </div>
        </v-col>
      </v-row>
      <div>
        <v-row no-gutters>
          <v-col cols="1"></v-col>
          <v-col cols="10" class="d-inline-flex tw-my-[-5rem]">
            <v-img src="/image/rankbanner2.png" class="tw-absolute tw-bottom-12"></v-img>
          </v-col>
          <v-col cols="1"></v-col>
        </v-row>
      </div>
      <v-row no-gutters class="web-text-title">
        <v-col cols="2"></v-col>
        <v-col cols="2" align="center">
          <div class="tw-pt-16 tw-overflow-clip" v-for="(user, index) in testList.data.slice(1,2)">
            <p>{{ user.displayName }}</p>
            <p class="web-text-sub" v-if="sort == 'followers'">({{ user.followers }} Followers)</p>
            <p class="web-text-sub" v-if="sort == 'totalLike'">({{ user.totalLike }} Likes)</p>
          </div>
        </v-col>
        <v-col cols="4" align="center">
          <div class="tw-pt-14 tw-mx-8 tw-overflow-clip" v-for="(user, index) in testList.data.slice(0,1)">
            <p>{{ user.displayName }}</p>
            <p class="web-text-sub" v-if="sort == 'followers'">({{ user.followers }} Followers)</p>
            <p class="web-text-sub" v-if="sort == 'totalLike'">({{ user.totalLike }} Likes)</p>
        </div>
        </v-col>
        <v-col cols="2" align="center">
          <div class="tw-pt-16 tw-overflow-clip" v-for="(user, index) in testList.data.slice(2,3)">
            <p>{{ user.displayName }}</p>
            <p class="web-text-sub" v-if="sort == 'followers'">({{ user.followers }} Followers)</p>
            <p class="web-text-sub" v-if="sort == 'totalLike'">({{ user.totalLike }} Likes)</p>
          </div>
        </v-col>
        <v-col cols="2"></v-col>
      </v-row> 
    </div>

    <!-- section 2 -->
    <div class="tw-my-24">
      <div class="tw-h-min-[11rem] tw-h-max-[11rem]" v-for="(user, index) in testList.data.slice(3)">
        <nuxt-link :to="`/user/${user.userId}/`">
        <hr class="tw-my-4" />
        <v-row no-gutters>
          <v-col cols="1" align="left" class="tw-my-8">
            <p class="ranking-num-header">{{ index+4 }}</p>
          </v-col>
          <v-col
            cols="8"
            class="tw-flex tw-flex-col tw-justify-center tw-space-y-2"
          >
            <div class="tw-space-x-3 tw-inline-flex tw-items-center">
              <span class="web-text-header">{{ user.displayName }}</span>
              <div v-if="user.userId != idToken && roleToken == 'USER'">    
                <v-btn
                  height="auto"
                  variant="outlined"
                  color="#3157BB"
                  class="px-8 py-2"
                  rounded="lg"
                  v-if="user.followingReview == null"
                  @click="$emit('follow',user.userId)"
                >Follow</v-btn>
                <v-btn
                  height="auto"
                  class="px-5 py-2"
                  color="#3157BB"
                  rounded="lg"
                  v-if="user.followingReview !== null"
                  @click="$emit('unfollow',user.userId)"
                >Following</v-btn>
                </div>
            </div>
            <p class="web-text-sub web-text-pf-email">{{ user.email }}</p>
            <div
              class="tw-space-x-1 tw-inline-flex tw-items-center tw-w-4/6 tw-py-1"
            >
              <span class="web-text-sub">{{ user.followers }} followers</span>
              <!-- <span class="web-text-sub">121 followings</span> -->
            </div>
          </v-col>
          <v-col cols="1"></v-col>
          <v-col cols="2" align="right">
            <v-img
              :src="user.file"
              v-if="user.file != null"
              width="150"
              height="150"
              class="tw-rounded-full tw-border-white tw-border-8"
              cover
            />
            <v-img
              src="/image/guest_icon.png"
              v-if="user.file == null"
              width="150"
              height="150"
              class="tw-rounded-full tw-border-white tw-border-8"
              cover
            />
          </v-col>
        </v-row>
      </nuxt-link>
      </div>
    </div>
    
  </div>
</template>

<style></style>
