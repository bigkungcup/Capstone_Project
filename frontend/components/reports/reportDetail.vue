<script setup>
defineEmits(["close"]);
defineProps({
    reportDetail: {
        type: Array,
        require: true,
    },
})
</script>

<template>
  <div
    class="tw-grid tw-h-100% tw-min-h-screen tw-w-screen tw-absolute tw-inset-0 tw--top-[5.5rem] tw-bg-black/50"
  >
    <v-card height="auto" width="40rem" class="rounded-xl tw-place-self-center">
      <v-card-title style="height: 5rem; width: 40rem">
        <p class="action-popUp-header tw-flex tw-justify-center tw-my-4">
          Report Detail
        </p>
      </v-card-title>
      <v-row v-for="report in reportDetail">
        <v-col cols="8">
          <div class="web-text-sub tw-mx-8 tw-space-y-2">
            <div>
              <v-row no-gutters>
                <v-col cols="2">
                  <v-chip>No.</v-chip>
                </v-col>
                <v-col cols="10">
                  <div class="px-2">#{{ report.reportId }}</div>
                </v-col>
              </v-row>  
            </div>
            <div>
              <v-row no-gutters>
                <v-col cols="3">
                  <v-chip>Category</v-chip>
                </v-col>
                <v-col cols="9">
                  <div class="px-2"><span class="tw-capitalize">{{ report.reportType }}</span> report</div>
                </v-col>
              </v-row>          
            </div>
            <div>
              <v-row no-gutters>
                <v-col :cols="report.reportType == 'review' ? '3' : '2'">
                  <v-chip v-if="report.reportType == 'book'">Book</v-chip>
                  <v-chip v-if="report.reportType == 'user'">User</v-chip>
                  <v-chip v-if="report.reportType == 'review'">Reviewer</v-chip>
                </v-col>
                <v-col :cols="report.reportType == 'review' ? '9' : '10'">
                  <div class="px-2" v-if="report.reportType == 'book'">{{ report.data.bookName }}</div>
                  <div class="px-2" v-if="report.reportType == 'user'">{{ report.data.displayName }}</div>
                  <div class="px-2" v-if="report.reportType == 'review'">{{ report.data.userDetail.displayName }}</div>
                </v-col>
              </v-row>
            </div>
            <div v-if="report.reportType == 'review'">
              <v-row no-gutters>
                <v-col cols="2">
                  <v-chip>Book</v-chip>
                </v-col>
                <v-col cols="10">
                  <div class="px-2">{{ report.data.book.bookName }}</div>
                </v-col>
              </v-row>
            </div>
            <div>
              <v-row no-gutters>
                <v-col cols="2">
              <v-chip>Type</v-chip>
                </v-col>
                <v-col cols="10">
                  <div class="px-2">{{ report.reportTitle }}</div>
                </v-col>
              </v-row>
            </div>
            <div>
              <v-row no-gutters>
                <v-col cols="4">
                  <v-chip>Description</v-chip>
                </v-col>
                <v-col cols="8">
                  <div class="px-2">{{ report.reportDetail == '' ? '-' : report.reportDetail }}</div>
                </v-col>
              </v-row>
            </div>
            <div>
              <v-row no-gutters>
                <v-col cols="4">
                  <v-chip class="px-3">Reported by</v-chip>
                </v-col>
                <v-col cols="8">
                  <div class="px-2"> {{ report.reportBy.displayName }} </div>
                </v-col>
              </v-row>
            </div>
          </div>
        </v-col>
        <v-col cols="4" align="left">
          <div v-if="report.reportType == 'book'">
            <v-img src="/image/cover_not_available.jpg" width="140" class="tw-border-[#082266] tw-border-2" v-if="report.data.file == null" cover></v-img>
            <v-img :src="report.data.file" width="160" height="240" class="tw-border-[#082266] tw-border-2" v-if="report.data.file != null" cover></v-img>
          </div>
          <div v-if="report.reportType == 'user'" align="left">
            <v-img src="/image/guest_icon.png" width="160" height="160" class="tw-rounded-full tw-border-[#082266] tw-border-2" cover  v-show="report.data.file == null"/>
            <v-img :src="report.data.file" width="160" height="160" class="tw-rounded-full tw-border-[#082266] tw-border-2" cover v-show="report.data.file !== null"/>
          </div>
          <div v-if="report.reportType == 'review'" align="left">
            <v-img src="/image/guest_icon.png" width="160" height="160" class="tw-rounded-full tw-border-[#082266] tw-border-2" cover  v-show="report.data.userDetail.file == null"/>
            <v-img :src="report.data.userDetail.file" width="160" height="160" class="tw-rounded-full tw-border-[#082266] tw-border-2" cover v-show="report.data.userDetail.file !== null"/>
          </div>
        </v-col>
      </v-row>

      <v-card-actions class="tw-my-2">
        <v-spacer></v-spacer>
        <v-btn
          class="tw-mx-2"
          style="height: 3rem; width: 7rem"
          color="#727272"
          variant="tonal"
          size="large"
          rounded="xl"
          @click="$emit('close')"
          >Close</v-btn
        >
        
      </v-card-actions>
    </v-card>
  </div>
</template>

<style></style>
