<script setup>
import Bookmarks from "~/components/profiles/bookmark.vue";
import BookmarkNotFound from "~/components/profiles/bookmarkNotFound.vue";
import MyReviews from "~/components/profiles/myReviews.vue";
import Followings from "~/components/profiles/following.vue";
import Followers from "~/components/profiles/followers.vue";
import ReviewNotFound from "~/components/reviews/reviewNotFound.vue";
import UserNotFound from "~/components/users/userNotFound.vue";
import changePasswordPopup from "~/components/profiles/popups/changePasswordPopup.vue";
import { useLogin } from "~/stores/login";
import { useBooks } from "~/stores/book";
import { useReviews } from "~/stores/review";
import { useUsers } from "~/stores/user";
import { mergeProps } from "vue";

const login = useLogin();
const book = useBooks();
const review = useReviews();
const user = useUsers();
const changePassword = ref(false);
const profileSection = ref("bookmark");
const bookmarkPage = ref(1);
const reviewPage = ref(1);
const followingPage = ref(1);
const followerPage = ref(1);
const result = ref(0);
const idToken = ref(localStorage.getItem('id'));

function handleChangePassword() {
  changePassword.value = !changePassword.value;
}

async function likeReviews(reviewId, likeStatus) {
  let status = {
    userId: idToken.value,
    reviewId: reviewId,
    likeStatus: likeStatus,
  };
  await review.createLike(status);
  await review.getMyReview(idToken.value);
}

async function updatelikeReviews(reviewId, likeStatus, likeStatusId) {
  let status = {
    userId: idToken.value,
    reviewId: reviewId,
    likeStatusId: likeStatusId,
    likeStatus: likeStatus,
  };
  await review.updateLike(status);
  await review.getMyReview(idToken.value);
}

async function selectSection(section) {
  if (section == "bookmark") {
    profileSection.value = "bookmark";
    await book.getBookmarkList(idToken.value);
    result.value = book.bookmarkList.data.totalElements
      ? book.bookmarkList.data.totalElements
      : 0;
  } else if (section == "review") {
    profileSection.value = "review";
    review.clearMyReviewList();
    await review.getMyReview(idToken.value);
    console.log(review.myReviewList.data.content.length);
    result.value = review.myReviewList.data.totalElements
      ? review.myReviewList.data.totalElements
      : 0;
  } else if (section == "following") {
    profileSection.value = "following";
    user.clearFollowingList();
    await user.getFollowingList(idToken.value);
    result.value = user.followingList.data.totalElements
      ? user.followingList.data.totalElements
      : 0;
  } else if (section == "follower") {
    profileSection.value = "follower";
    user.clearFollowerList();
    await user.getFollowerList(idToken.value);
    result.value = user.followerList.data.totalElements
      ? user.followerList.data.totalElements
      : 0;
  }
}

onBeforeMount(async () => {
  await login.getProfile();
  await book.getBookmarkList();
  await selectSection(profileSection.value);
});
</script>

<template>
  <div class="tw-bg-[#D9D9D9] tw-h-full">
    <div class="tw-flex tw-place-content-center">
      <div class="tw-w-[70rem] tw-max-h-[16rem]">
        <v-img
          src="/image/profile_banner.jpg"
          v-show="login.profile.file == null"
          cover
        ></v-img>
        <v-img
          class="tw-blur-[2px]"
          v-show="login.profile.file != null"
          :src="login.profile.file"
          cover
        ></v-img>
      </div>
    </div>

    <div class="tw-flex tw-place-content-center">
      <div class="tw-bg-white tw-w-[70rem]">
        <v-row class="tw-py-2">
          <v-col cols="2">
            <v-img
              src="/image/guest_icon.png"
              v-show="login.profile.file == null"
              width="140"
              height="140"
              class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-6"
              cover
            />
            <v-img
              class="tw-rounded-full tw-border-white tw-border-8 tw-my-[-4rem] tw-mx-6"
              :src="login.profile.file"
              v-show="login.profile.file != null"
              height="140"
              width="140"
              cover
            ></v-img>
          </v-col>

          <v-col cols="5">
            <div class="tw-space-y-2">
              <p class="web-text-header">{{ login.profile.displayName }}</p>
              <p class="web-text-sub">{{ login.profile.email }}</p>
              <div class="tw-min-h-[4rem] tw-break-words">
              <p class="web-text-sub">{{ login.profile.bio }}</p>
              </div>
            </div>
          </v-col>

          <!-- <v-col cols="5">
            <div class="py-1">
              <p class="web-text-sub tw-flex tw-place-content-end">
                {{ login.profile.followings }} Following
                {{ login.profile.followers }} Followers
              </p>
            </div>
          </v-col> -->

          <v-col cols="5" class="tw-grid tw-content-between">
            <!-- <v-btn color="#1D419F" variant="outlined" rounded="lg" elevation="2" :to="`/profile/update_${login.profile.userId}/`">Edit profile</v-btn> -->
            <div align="end">
            <span class="tw-px-4 text-center web-text-detail">
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
                <v-list>
                  <v-list-item :to="`/profile/update/`">
                    <v-list-item-title class="web-text-detail tw-space-x-2"
                      ><v-icon icon="mdi mdi-pencil-outline"></v-icon
                      ><span>Edit profile</span></v-list-item-title
                    >
                  </v-list-item>
                  <v-list-item
                    class="hover:tw-bg-zinc-300/20 tw-cursor-pointer"
                  >
                    <v-list-item-title class="web-text-detail">
                      <v-list-item-title
                        class="web-text-detail tw-space-x-2"
                        @click="handleChangePassword()"
                        ><v-icon icon="mdi mdi-key"></v-icon
                        ><span>Change password</span></v-list-item-title
                      >
                    </v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </span>
          </div>

          <div class="py-4" v-if="login.profile.role == 'USER'">
              <p class="tw-px-4 web-text-sub-thin tw-flex tw-place-content-end">
                {{ login.profile.followings }} Following
                {{ login.profile.followers }} Followers
              </p>
            </div>
          </v-col>
        </v-row>
      </div>
    </div>

    <!----------------------- new section -------------------------->
    <div class="tw-flex tw-place-content-center tw-my-6" v-if="login.profile.role == 'USER'">
      <div class="tw-bg-white tw-w-[70rem] tw-h-full">
        <div class="tw-border-y-4">
          <v-row class="">
            <v-col cols="3" class="tw-grid tw-content-center">
              <div
                class="tw-cursor-pointer tw-flex tw-justify-center web-text-sub-pf "
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
                class="tw-flex tw-justify-center web-text-sub-pf tw-cursor-pointer"
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
                class="tw-flex tw-justify-center web-text-sub-pf tw-cursor-pointer"
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
                class="tw-flex tw-justify-center web-text-sub-pf tw-cursor-pointer"
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
      </div>
    </div>
    <!-- Popup -->
    <changePasswordPopup
      :dialog="changePassword"
      @toggle="handleChangePassword()"
      @update="login.changePassword()"
    />
  </div>
</template>

<style scoped></style>
