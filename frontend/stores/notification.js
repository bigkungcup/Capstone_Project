import { defineStore, acceptHMRUpdate } from "pinia";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useLogin } from "./login";

export const useNotifications = defineStore("Notification", () => {
  const router = useRouter();
//   const accessToken = ref(useCookie("accessToken"));
  const idToken = ref(localStorage.getItem("id"));
  const login = useLogin();
  const roleToken = ref(localStorage.getItem("role"));
  const successfulPopup = ref(false);
  const reportStatus = ref(
    {
        type: '',
        show: false
    }
);
  const reportProblem = ref({
    reportTitle: "",
    reportDetail: "",
    problemId: 0,
  });
  const reportBookList = ref([
    {
      id: 1,
      Name: "This book has an incorrect information.",
    },
    {
      id: 2,
      Name: "This book is duplicated.",
    },
    {
      id: 3,
      Name: "This book does not exist.",
    },
  ]);
  const reportReviewList = ref([
    {
      id: 1,
      Name: "This review has an incorrect information.",
    },
    {
      id: 2,
      Name: "This review is spam.",
    },
  ]);
  const reportUserList = ref([
    {
      id: 1,
      Name: "This user has an incorrect information.",
    },
    {
      id: 2,
      Name: "This user is spam.",
    },
  ]);

// --------------- Notification Function ---------------


// --------------- Report Function ---------------
//Get Report List

  //Create Report Book
  async function createReportBook(bookId) {
    let accessToken = useCookie("accessToken");
    let status = 0;

    await $fetch(`${import.meta.env.VITE_BASE_URL}/report`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
      },
      params: {
        reportTitle: reportProblem.value.reportTitle,
        reportDetail: reportProblem.value.reportDetail,
        problemId: reportProblem.value.problemId,
        reportType: 'book',
        bookId : bookId
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          console.log("report uncompleted");
        } else if (status == 401) {
          login.handleRefresh();
          createReportBook(bookId);
        }
      },
    });
    if (status == 201) {
      reportStatus.value.show = false;
      successfulPopup.value = true;
      console.log("report completed");
    }
  }

  
  //Create Report Review
  async function createReportReview(reviewId) {
    let accessToken = useCookie("accessToken");
    let status = 0;

    await $fetch(`${import.meta.env.VITE_BASE_URL}/report`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
      },
      params: {
        reportTitle: reportProblem.value.reportTitle,
        reportDetail: reportProblem.value.reportDetail,
        problemId: reportProblem.value.problemId,
        reportType: 'review',
        reviewId : reviewId
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          console.log("report uncompleted");
        } else if (status == 401) {
          login.handleRefresh();
          createReportReview(reviewId);
        }
      },
    });
    if (status == 201) {
      reportStatus.value.show = false;
      successfulPopup.value = true;
      console.log("report completed");
    }
  }

  
  //Create Report User
  async function createReportUser(userId) {
    let accessToken = useCookie("accessToken");
    let status = 0;

    await $fetch(`${import.meta.env.VITE_BASE_URL}/report`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
      },
      params: {
        reportTitle: reportProblem.value.reportTitle,
        reportDetail: reportProblem.value.reportDetail,
        problemId: reportProblem.value.problemId,
        reportType: 'user',
        userId : userId
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          console.log("report uncompleted");
        } else if (status == 401) {
          login.handleRefresh();
          createReportUser(userId);
        }
      },
    });
    if (status == 201) {
      reportStatus.value.show = false;
      successfulPopup.value = true;
      console.log("report completed");
    }
  }



  //Clear report problem
  function clearReportProblem() {
    reportProblem.value = {
      reportTitle: "",
      reportDetail: "",
      problemId: 0,
    };
  }

  //Close successful popup
//   function closeSuccessfulPopup() {
//     successfulPopup.value = false;
//   }

  //Back Page
  function backPage() {
    window.history.back();
  }

  return {
    reportStatus,
    reportProblem,
    reportBookList,
    reportReviewList,
    reportUserList,
    createReportBook,
    createReportReview,
    createReportUser,
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useNotifications, import.meta.hot));
}
