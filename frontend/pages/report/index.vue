<script setup>
import CreateNoti from "~/components/notification/createNoti.vue";
import CreateNotiSuccess from "~/components/notification/popups/createNotiSuccess.vue";
import ReportConfirmDone from "~/components/reports/popups/reportConfirmDone.vue";
import ReportDetail from "~/components/reports/reportDetail.vue";
import ReportList from "~/components/reports/reportList.vue";
import ReportNotFound from "~/components/reports/reportNotFound.vue";
import { useNotifications } from "~/stores/notification";

const noti = useNotifications();
const roleToken = ref(localStorage.getItem('role'));
const reportData = ref([]);
const reportDetailStatus = ref(false);
const reportDoneId = ref(0);
const reportDoneCheck = ref(false);
const notiCheck = ref(false);
const page = ref(noti.reportPage+1)

function handleGetReportDetail(reportId) {
  reportData.value = [noti.reportList.data.content.find((element) => element.reportId == reportId)];
  console.log(reportData.value);
  reportDetailStatus.value = true;
}

function handleDoneCheck(reportId) {
  reportDoneId.value = reportId;
  reportDoneCheck.value = true;
}

onBeforeMount(async () => {
  if (roleToken.value == 'ADMIN') {
  await noti.getReportList();
}else{
  router.push(`/UnauthenPage/`)
}
});
</script>

<template>
  <div>
    <p class="web-text-header tw-flex tw-justify-center tw-my-4">Report</p>
    <div v-if="noti.reportList.data.content.length != 0">
    <ReportList :reportList="noti.reportList.data.content" 
    @get="handleGetReportDetail($event)"
    @done="handleDoneCheck($event)"/>
    </div>
    <div v-show="noti.reportList.data.content.length != 0">
    <v-pagination v-model="page" :length="noti.reportList.data.totalPages" :total-visible="7"
        rounded="20" @update:model-value="noti.changeReportPage(page)">
    </v-pagination></div>
    <div v-if="noti.reportList.data.content.length == 0">
    <ReportNotFound />
    </div>
    <div class="tw-absolute tw-bottom-0 tw-right-0 tw-m-5">
      <v-btn icon="mdi-bullhorn-variant-outline" size="x-large" color="#3157BB" @click="notiCheck = true"></v-btn>
    </div>
  </div>
  <div v-if="reportDetailStatus">
    <ReportDetail :reportDetail="reportData" @close="reportDetailStatus = false"/>
  </div>
  <div v-if="reportDoneCheck">
    <ReportConfirmDone @cancel="reportDoneCheck = false" @submit="noti.updateReportDone(reportDoneId),reportDoneCheck = false"/>
  </div>
  <div v-if="notiCheck">
    <CreateNoti @cancel="notiCheck = false,noti.clearNewNotification()" @submit="noti.createNotification()" />
  </div>
  <div v-if="noti.successfulPopup">
    <CreateNotiSuccess @close="noti.successfulPopup = false,notiCheck = false2"/>
  </div>
</template>

<style></style>
