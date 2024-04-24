<script setup>
import Bookmarks from "~/components/profiles/bookmark.vue";
import BookmarkNotFound from "~/components/profiles/bookmarkNotFound.vue";
import MyReviews from "~/components/profiles/myReviews.vue";
import Followings from "~/components/profiles/following.vue";
import Followers from "~/components/profiles/followers.vue";
import ReviewNotFound from "~/components/reviews/reviewNotFound.vue";
import UserNotFound from "~/components/users/userNotFound.vue";
import { useUsers } from "~/stores/user";
import { useBooks } from "~/stores/book";
import { useReviews } from "~/stores/review";
import { useNotifications } from "~/stores/notification";
import { useRoute, useRouter } from "vue-router";
import { mergeProps } from "vue";
import deleteUserConfirmPopup from "~/components/users/popups/deleteUserConfirmPopup.vue";
import deleteUserSuccessPopup from "~/components/users/popups/deleteUserSuccessPopup.vue";
import CreateReportPopup from "~/components/reports/createReportPopup.vue";
import ReportSuccessPopup from "~/components/reports/popups/reportSuccessPopup.vue";
import LoadingPopup from "~/components/popups/loadingPopup.vue";

const user = useUsers();
const book = useBooks();
const review = useReviews();
const noti = useNotifications();
const router = useRouter();
const route = useRoute();
const profileSection = ref("bookmark");
const bookmarkPage = ref(1);
const reviewPage = ref(1);
const followingPage = ref(1);
const followerPage = ref(1);
const result = ref(0);
const idToken = ref(localStorage.getItem('id'));
const roleToken = ref(localStorage.getItem('role'));

function toggleUserConfirmPopup() {
  user.confirmPopup = !user.confirmPopup;
}

async function handleFollow(userId) {
  await user.createFollower(userId)
  await user.getUserDetail(route.params.id);
}

async function handleUnfollow(userId) {
  await user.deleteFollower(userId)
  await user.getUserDetail(route.params.id);
}

async function handleGetUserDetail() {
  if(roleToken.value == "GUEST"){
    await user.getUserDetailByGuest(route.params.id);
  }else{
    await user.getUserDetail(route.params.id);
  }
}

async function selectSection(section) {
  if (section == "bookmark") {
    profileSection.value = "bookmark";
    await book.getBookmarkList(route.params.id);
    result.value = book.bookmarkList.data.totalElements
      ? book.bookmarkList.data.totalElements
      : 0;
  } else if (section == "review") {
    profileSection.value = "review";
    review.clearMyReviewList();
    await review.getMyReview(route.params.id);
    console.log(review.myReviewList.data.content.length);
    result.value = review.myReviewList.data.totalElements
      ? review.myReviewList.data.totalElements
      : 0;
  } else if (section == "following") {
    profileSection.value = "following";
    user.clearFollowingList();
    await user.getFollowingList(route.params.id);
    result.value = user.followingList.data.totalElements
      ? user.followingList.data.totalElements
      : 0;
  } else if (section == "follower") {
    profileSection.value = "follower";
    user.clearFollowerList();
    await user.getFollowerList(route.params.id);
    result.value = user.followerList.data.totalElements
      ? user.followerList.data.totalElements
      : 0;
  }
}

function closeUserSuccessfulPopup() {
  user.successfulPopup = 'hide';
  window.history.back();
}

function bookCoverPath(filePath) {
  return (filePath = `../../_nuxt/@fs/${filePath}`);
}

onBeforeMount(async () => {
  if(route.params.id != idToken.value){
    await selectSection(profileSection.value);
    noti.clearReportProblem();
    handleGetUserDetail()
    if (roleToken.value == "GUEST") {
      await book.getBookmarkListByGuest(route.params.id);
    }else{
      await book.getBookmarkList(route.params.id);
    }}else{
      router.push(`/Profile/`)
    }
//   if (roleToken.value == 'ADMIN') {
//   await user.getUserDetail(route.params.id);
// }else{
//   router.push(`/UnauthenPage/`)
// }
});
</script>

<template>
  <div class="tw-bg-[#D9D9D9] tw-h-full">
    <div class="tw-flex tw-place-content-center">
      <div class="tw-w-[70rem] tw-max-h-[16rem]">
        <v-img src="/image/profile_banner.jpg" v-show="user.userDetail.data.file == null" cover></v-img>
        <v-img
          class="tw-blur-[2px]"
          v-show="user.userDetail.data.file != null"  
          :src="user.userDetail.data.file"
          cover
        ></v-img>
        <!-- <v-img src="/image/cat.jpg" width="120" height="120"
                    class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-5" cover /> -->
      </div>
    </div>

    <div class="tw-flex tw-place-content-center">
      <div class="tw-bg-white tw-w-[70rem] tw-h-[9rem]">
        <v-row class="tw-py-2">
          <v-col cols="2">
            <v-img
              src="/image/guest_icon.png"
              v-show="user.userDetail.data.file == null"
              width="140"
              height="140"
              class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-6"
              cover
            />
            <v-img
              class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-6"
              :src="user.userDetail.data.file"
              v-show="user.userDetail.data.file != null"
              height="140"
              width="140"
              cover
            ></v-img>
          </v-col>

          <v-col cols="5">
            <div class="tw-space-y-2">
              <p class="web-text-header">{{ user.userDetail.data.displayName }}</p>
              <p class="web-text-sub">{{ user.userDetail.data.email }}</p>
              <div class="tw-min-h-[4rem] tw-break-words">
              <p class="web-text-sub">{{ user.userDetail.data.bio }}</p>
              </div>
            </div>
          </v-col>

          <v-col cols="2">
            <div class="py-1">
              <!-- <p class="web-text-sub tw-flex tw-place-content-end">
                {{ user.userDetail.data.followings }} Following
                {{ user.userDetail.data.followers }} Followers
              </p> -->
            </div>
          </v-col>

          <v-col cols="3" class="tw-grid tw-content-between">
            <!-- <v-btn color="#1D419F" variant="outlined" rounded="lg" elevation="2" :to="`/profile/update_${user.userDetail.data.userId}/`">Edit profile</v-btn> -->
            <div align="end">
            <span v-if="roleToken == 'USER'">
              <v-btn
                  height="auto"
                  variant="outlined"
                  color="#3157BB"
                  class="px-8 py-2"
                  rounded="lg"
                  v-if="user.userDetail.data.follow == null"
                  @click="handleFollow(user.userDetail.data.userId)"
                >Follow</v-btn>
                <v-btn
                  height="auto"
                  class="px-5 py-2"
                  color="#3157BB"
                  rounded="lg"
                  v-if="user.userDetail.data.follow !== null"
                  @click="handleUnfollow(user.userDetail.data.userId)"
                >Following</v-btn>
            </span>  

            <span class="tw-px-4 text-center web-text-detail" v-if="roleToken != 'GUEST'">
              <v-menu>
                <template v-slot:activator="{ props: menu }">
                  <v-tooltip location="top">
                    <template v-slot:activator="{ props: tooltip }">
                      <v-icon
                        icon="mdi mdi-dots-vertical-circle-outline"
                        size="x-large"
                        v-bind="mergeProps(menu, tooltip)"
                      ></v-icon>
                    </template>
                    <span>More</span>
                  </v-tooltip>
                </template>
                <v-list v-if="roleToken == 'ADMIN'">
                  <v-list-item :to="`/user/update_${route.params.id}`+'/'">
                    <v-list-item-title class="web-text-detail tw-space-x-2"
                      ><v-icon icon="mdi mdi-pencil-outline"></v-icon
                      ><span>Edit this user</span></v-list-item-title
                    >
                  </v-list-item>
                  <v-list-item
                    class="hover:tw-bg-zinc-300/20 tw-cursor-pointer"
                  >
                    <v-list-item-title class="web-text-detail">
                      <v-list-item-title
                        class="web-text-detail tw-space-x-2"
                        @click="toggleUserConfirmPopup()"
                        ><v-icon icon="mdi mdi-trash-can-outline"></v-icon
                        ><span>Delete this user</span></v-list-item-title
                      >
                    </v-list-item-title>
                  </v-list-item>
                </v-list>
                <v-list v-if="roleToken == 'USER'">
                  <v-list-item class="hover:tw-bg-zinc-300/20 tw-cursor-pointer">
                    <v-list-item-title class="web-text-detail tw-space-x-2" @click="noti.handleReportUser(user.userDetail.data.userId)"
                      ><v-icon icon="mdi mdi-flag-variant-outline"></v-icon
                      ><span>Report this user</span></v-list-item-title
                    >
                  </v-list-item>
                </v-list>
              </v-menu>
            </span>
            </div>

            <div class="py-4">
              <p class="tw-px-4 web-text-sub-thin tw-flex tw-place-content-end">
                {{ user.userDetail.data.followings }} Following
                {{ user.userDetail.data.followers }} Followers
              </p>
            </div>
          </v-col>
        </v-row>
      </div>
    </div>

    <!----------------------- new section -------------------------->
    <div class="tw-flex tw-place-content-center tw-my-6" v-if="user.userDetail.data.role == 'USER'">
      <div class="tw-bg-white tw-w-[70rem] tw-h-full">
        <div class="tw-border-y-4">
          <v-row class="">
            <v-col cols="3" class="tw-grid tw-content-center">
              <div
                class="tw-flex tw-justify-center web-text-sub-pf"
                v-if="profileSection != 'bookmark'"
                @click="
                  (profileSection = 'bookmark'), selectSection(profileSection)
                "
              >
                Bookmarks
              </div>
              <div
                class="web-text-sub-pf-white tw-bg-[#082266] tw-py-3"
                v-if="profileSection == 'bookmark'"
              >
                <p class="tw-flex tw-justify-center">Bookmarks</p>
                <p class="tw-flex tw-justify-center">({{ result }})</p>
              </div>
            </v-col>
            <v-col cols="3" class="tw-grid tw-content-center">
              <div
                class="tw-flex tw-justify-center web-text-sub-pf"
                v-if="profileSection != 'review'"
                @click="
                  (profileSection = 'review'), selectSection(profileSection)
                "
              >
                My reviews
              </div>
              <div
                class="web-text-sub-pf-white tw-bg-[#082266] tw-py-3"
                v-if="profileSection == 'review'"
              >
                <p class="tw-flex tw-justify-center">My reviews</p>
                <p class="tw-flex tw-justify-center">({{ result }})</p>
              </div>
            </v-col>
            <v-col cols="3" class="tw-grid tw-content-center">
              <div
                class="tw-flex tw-justify-center web-text-sub-pf"
                v-if="profileSection != 'following'"
                @click="
                  (profileSection = 'following'), selectSection(profileSection)
                "
              >
                Followings
              </div>
              <div
                class="web-text-sub-pf-white tw-bg-[#082266] tw-py-3"
                v-if="profileSection == 'following'"
              >
                <p class="tw-flex tw-justify-center">Followings</p>
                <p class="tw-flex tw-justify-center">({{ result }})</p>
              </div>
            </v-col>
            <v-col cols="3" class="tw-grid tw-content-center">
              <div
                class="tw-flex tw-justify-center web-text-sub-pf"
                v-if="profileSection != 'follower'"
                @click="
                  (profileSection = 'follower'), selectSection(profileSection)
                "
              >
                Followers
              </div>
              <div
                class="web-text-sub-pf-white tw-bg-[#082266] tw-py-3"
                v-if="profileSection == 'follower'"
              >
                <p class="tw-flex tw-justify-center">Followers</p>
                <p class="tw-flex tw-justify-center">({{ result }})</p>
              </div>
            </v-col>
          </v-row>
        </div>

        <!-------- insert component here ---------->
     <!-- Bookmark /> -->
     <div v-if="profileSection == 'bookmark'">
          <div v-if="book.bookmarkList.data.content.length !== 0">
            <Bookmarks :bookmarkList="book.bookmarkList.data.content" />
            <div class="py-1">
              <v-pagination
                v-model="bookmarkPage"
                :length="book.bookmarkList.data.totalPages"
                :total-visible="7"
                rounded="20"
                @update:model-value="book.changeBookmarkPage(bookmarkPage)"
              >
              </v-pagination>
            </div>
          </div>
          <div v-if="book.bookmarkList.data.content.length == 0">
            <BookmarkNotFound />
          </div>
        </div>
        <!-- <Reviews /> -->
        <div v-if="profileSection == 'review'">
          <div v-if="review.myReviewList.data.content.length !== 0">
          <MyReviews :reviewList="review.myReviewList.data.content" 
                      @like="likeReviews($event.reviewId, $event.likeStatus)"
                      @update="
                    updatelikeReviews(
                      $event.reviewId,
                      $event.likeStatus,
                      $event.likeStatusId
                    )
                  "/>
          <div
            class="py-1"
          >
            <v-pagination
              v-model="reviewPage"
              :length="review.myReviewList.data.totalPages"
              :total-visible="7"
              rounded="20"
              @update:model-value="review.changeMyReviewPage(reviewPage)"
            >
            </v-pagination>
          </div></div>
          <div v-if="review.myReviewList.data.content.length == 0">
          <ReviewNotFound />
        </div>
        </div>
        <!-- <Followings /> -->
        <div v-if="profileSection == 'following'">
          <div v-if="user.followingList.data.content.length !== 0">
          <Followings 
            :followingList="user.followingList.data.content" 
            @follow="user.createFollower($event)"
            @unfollow="user.deleteFollower($event)"/>
          <div class="py-1">
            <v-pagination
              v-model="followingPage"
              :length="user.followingList.data.totalPages"
              :total-visible="7"
              rounded="20"
              @update:model-value="user.changeFolloingPage(followingPage)"
            >
            </v-pagination>
          </div></div>
          <div v-if="user.followingList.data.content.length == 0">
            <UserNotFound />
          </div>        
        </div>
        <!-- <Followers /> -->
        <div v-if="profileSection == 'follower'">
          <div v-if="user.followerList.data.content.length !== 0">
          <Followers 
            :followerList="user.followerList.data.content" 
            @follow="user.createFollower($event)"
            @unfollow="user.deleteFollower($event)"/>
          <div class="py-1">
            <v-pagination
              v-model="followerPage"
              :length="user.followerList.data.totalPages"
              :total-visible="7"
              rounded="20"
              @update:model-value="user.changeFollowerPage(followerPage)"
            >
            </v-pagination>
          </div></div>
          <div v-if="user.followerList.data.content.length == 0">
            <UserNotFound />
          </div>
        </div>

        <!-- Admin popup -->
        <deleteUserConfirmPopup
          class="delete-popup"
          :dialog="user.confirmPopup"
          @toggle="toggleUserConfirmPopup()"
          @delete="user.deleteUser(user.userDetail.data.userId)"
        />
        <deleteUserSuccessPopup
          class="delete-popup"
          :dialog="user.successfulPopup == 'show'"
          @close="closeUserSuccessfulPopup()"
        />

      <!-- Create Report Popup -->
      <div v-if="noti.reportStatus">
      <CreateReportPopup class="report-popup" 
      :title="noti.reportUserList" 
      :report="noti.reportProblem" 
      @cancel="noti.reportStatus = false, noti.clearReportProblem()" 
      @submit="noti.createReport()"/>
      </div>
      <div v-if="noti.successfulPopup == 'show' ">
      <ReportSuccessPopup class="report-popup" @close="noti.successfulPopup = 'hide'"/>
    </div>  
    <div v-if="noti.successfulPopup == 'load' || user.successfulPopup == 'load'">
    <LoadingPopup />
  </div>
    </div>
    </div>
  </div>
</template>

<style scoped>
.report-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
