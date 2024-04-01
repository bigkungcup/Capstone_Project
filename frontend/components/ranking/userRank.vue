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

</script>

<template>
  <div>
    <!-- section 1 -->
    <div v-if="userList.length != 0">
      <v-row no-gutters>
      <v-col cols="4" align="right">
        <div class="tw-pt-28" v-for="(user, index) in userList.slice(1,2)">
              <nuxt-link :to="user.userId == idToken ? `/profile/`: `/user/${user.userId}/`">
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
        <div class="tw-pt-20" v-for="(user, index) in userList.slice(0,1)">
          <nuxt-link :to="user.userId == idToken ? `/profile/`: `/user/${user.userId}/`">
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
        <div class="tw-pt-28" v-for="(user, index) in userList.slice(2,3)">
              <nuxt-link :to="user.userId == idToken ? `/profile/`: `/user/${user.userId}/`">
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
            <v-img src="/image/rankbanner.png" class="tw-absolute tw-bottom-12"></v-img>
          </v-col>
          <v-col cols="1"></v-col>
        </v-row>
      </div>
      <v-row no-gutters class="web-text-title">
        <v-col cols="2"></v-col>
        <v-col cols="2" align="center">
          <div class="tw-pt-16 tw-overflow-clip" v-for="(user, index) in userList.slice(1,2)">
            <p>{{ user.displayName }}</p>
            <p class="web-text-sub" v-if="sort == 'followers'">({{ user.followers }} Followers)</p>
            <p class="web-text-sub" v-if="sort == 'totalLike'">({{ user.totalLike }} Likes)</p>
          </div>
        </v-col>
        <v-col cols="4" align="center">
          <div class="tw-pt-14 tw-mx-8 tw-overflow-clip" v-for="(user, index) in userList.slice(0,1)">
            <p>{{ user.displayName }}</p>
            <p class="web-text-sub" v-if="sort == 'followers'">({{ user.followers }} Followers)</p>
            <p class="web-text-sub" v-if="sort == 'totalLike'">({{ user.totalLike }} Likes)</p>
        </div>
        </v-col>
        <v-col cols="2" align="center">
          <div class="tw-pt-16 tw-overflow-clip" v-for="(user, index) in userList.slice(2,3)">
            <p>{{ user.displayName }}</p>
            <p class="web-text-sub" v-if="sort == 'followers'">({{ user.followers }} Followers)</p>
            <p class="web-text-sub" v-if="sort == 'totalLike'">({{ user.totalLike }} Likes)</p>
          </div>
        </v-col>
        <v-col cols="2"></v-col>
      </v-row> 
    </div>

    <!-- section 2 -->
    <div class="tw-my-24" v-if="userList.length != 0">
      <div class="tw-h-min-[11rem] tw-h-max-[11rem]" v-for="(user, index) in userList.slice(3)">
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
                  v-if="user.follow == null"
                  @click="$emit('follow',user.userId)"
                >Follow</v-btn>
                <v-btn
                  height="auto"
                  class="px-5 py-2"
                  color="#3157BB"
                  rounded="lg"
                  v-if="user.follow !== null"
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
            <nuxt-link :to="user.userId == idToken ? `/profile/`: `/user/${user.userId}/`">
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
          </nuxt-link>
          </v-col>
        </v-row>
      </div>
    </div>

    <div
      class="tw-h-[20rem] tw-place-content-center tw-space-y-4"
      v-if="userList.length == 0"
    >
      <div align="center">
        <svg
          width="180px"
          height="180px"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          stroke="#000000"
        >
          <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
          <g
            id="SVGRepo_tracerCarrier"
            stroke-linecap="round"
            stroke-linejoin="round"
          ></g>
          <g id="SVGRepo_iconCarrier">
            <path
              d="M16 22V13C16 11.5858 16 10.8787 15.5607 10.4393C15.1213 10 14.4142 10 13 10H11C9.58579 10 8.87868 10 8.43934 10.4393C8 10.8787 8 11.5858 8 13M8 22V17"
              stroke="#082266"
              stroke-width="1.5"
              stroke-linecap="round"
            ></path>
            <path
              d="M8 22C8 20.5858 8 19.8787 7.56066 19.4393C7.12132 19 6.41421 19 5 19C3.58579 19 2.87868 19 2.43934 19.4393C2 19.8787 2 20.5858 2 22"
              stroke="#082266"
              stroke-width="1.5"
              stroke-linecap="round"
            ></path>
            <path
              d="M22 22V19C22 17.5858 22 16.8787 21.5607 16.4393C21.1213 16 20.4142 16 19 16C17.5858 16 16.8787 16 16.4393 16.4393C16 16.8787 16 17.5858 16 19V22"
              stroke="#082266"
              stroke-width="1.5"
              stroke-linecap="round"
            ></path>
            <path
              d="M11.1459 3.02251C11.5259 2.34084 11.7159 2 12 2C12.2841 2 12.4741 2.34084 12.8541 3.02251L12.9524 3.19887C13.0603 3.39258 13.1143 3.48944 13.1985 3.55334C13.2827 3.61725 13.3875 3.64097 13.5972 3.68841L13.7881 3.73161C14.526 3.89857 14.895 3.98205 14.9828 4.26432C15.0706 4.54659 14.819 4.84072 14.316 5.42898L14.1858 5.58117C14.0429 5.74833 13.9714 5.83191 13.9392 5.93531C13.9071 6.03872 13.9179 6.15023 13.9395 6.37327L13.9592 6.57632C14.0352 7.36118 14.0733 7.75361 13.8435 7.92807C13.6136 8.10252 13.2682 7.94346 12.5773 7.62535L12.3986 7.54305C12.2022 7.45265 12.1041 7.40745 12 7.40745C11.8959 7.40745 11.7978 7.45265 11.6014 7.54305L11.4227 7.62535C10.7318 7.94346 10.3864 8.10252 10.1565 7.92807C9.92674 7.75361 9.96476 7.36118 10.0408 6.57632L10.0605 6.37327C10.0821 6.15023 10.0929 6.03872 10.0608 5.93531C10.0286 5.83191 9.95713 5.74833 9.81418 5.58117L9.68403 5.42898C9.18097 4.84072 8.92945 4.54659 9.01723 4.26432C9.10501 3.98205 9.47396 3.89857 10.2119 3.73161L10.4028 3.68841C10.6125 3.64097 10.7173 3.61725 10.8015 3.55334C10.8857 3.48944 10.9397 3.39258 11.0476 3.19887L11.1459 3.02251Z"
              stroke="#082266"
              stroke-width="1.5"
            ></path>
          </g>
        </svg>
      </div>
      <div class="tw-text-center web-text-header">
        <p>No ranking user now.</p>
      </div>
    </div>
  </div>
</template>

<style></style>
