import { defineStore, acceptHMRUpdate } from "pinia";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useLogin } from "./login";

export const useNotifications = defineStore("Notification", () => {
  const router = useRouter();
  const idToken = ref(localStorage.getItem("id"));
  const login = useLogin();
  const roleToken = ref(localStorage.getItem("role"));
  const successfulPopup = ref(false);
  const notificationList = ref({
    data: [],
  });
  const countAllNotification = ref({
    data: [],
  });
  const countUserNotification = ref({
    data: [],
  });
  const countSystemNotification = ref({
    data: [],
  });
  const newNotification = ref({
    title: '',
    detail: '',
    link: null
  });
  const reportList = ref({
    data: {
      content: [],
    },
  });
  const reportHistoryList = ref({
    data: [],
  });
  const reportStatus = ref(false);
  const reportProblem = ref({
    reportTitle: "",
    reportDetail: "",
    reportType: "",
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
      Name: "This review is inappropriate.",
    },
    {
      id: 2,
      Name: "This review does not hide a spoil.",
    },
    {
      id: 3,
      Name: "This review is spam.",
    },
  ]);
  const reportUserList = ref([
    {
      id: 1,
      Name: "This user posts inappropriate thing in the public.",
    },
    {
      id: 2,
      Name: "This user is spam.",
    },
  ]);

  // --------------- Notification Function ---------------
  //Get Notification List
  async function getNotificationList(notiLevel) {
    let accessToken = useCookie("accessToken");
    let status = 0;

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/notification`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken.value}`,
          };
          options.params = {
            notificationLevel: notiLevel,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        notificationList.value = data.value;
      }
      console.log("get notification list completed");
    } else if (status == 404) {
      clearNotificationList();
      console.log("get notification list uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getNotificationList(notiLevel);
    }
  }

  //Get all notification number
  async function getCountAllNotification() {
    let accessToken = useCookie("accessToken");
    let status = 0;

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/notification/count`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken.value}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        countAllNotification.value = data.value;
      }
      console.log("get all notification number completed");
    } else if (status == 404) {
      clearCountAllNotification();
      console.log("get all notification number uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getCountAllNotification();
    }
  }

  //Get user notification number
  async function getCountUserNotification() {
    let accessToken = useCookie("accessToken");
    let status = 0;

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/notification/countNormal`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken.value}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        countUserNotification.value = data.value;
      }
      console.log("get user notification number completed");
    } else if (status == 404) {
      clearCountUserNotification();
      console.log("get user notification number uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getCountUserNotification();
    }
  }

  //Get system notification number
  async function getCountSystemNotification() {
    let accessToken = useCookie("accessToken");
    let status = 0;

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/notification/countSystem`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken.value}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        countSystemNotification.value = data.value;
      }
      console.log("get system notification number completed");
    } else if (status == 404) {
      clearCountSystemNotification();
      console.log("get system notification number uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getCountSystemNotification();
    }
  }

      //Create notification
      async function createNotification() {
        let accessToken = useCookie("accessToken");
        let status = 0;
  
        await $fetch(`${import.meta.env.VITE_BASE_URL}/notification`, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${accessToken.value}`,
          },
          body: {
            notificationTitle: newNotification.value.title,
            notificationDetail: newNotification.value.detail,
            notificationType: "ADMIN",
            notificationLink: newNotification.value.link 
          },
          onResponse({ request, response, options }) {
            status = response._data.response_code;
            if (status == 400) {
              console.log("create notification uncompleted");
            } else if (status == 401) {
              login.handleRefresh();
              createNotification();
            }
          },
        });
        if (status == 201) {
          clearNewNotification();
          console.log("create notification completed");
        } 
      }

      //Update notification status by Id (read by id)
  async function clearNotificationById(notiId,notiLevel) {
    let accessToken = useCookie("accessToken");
    let status = 0;

    await $fetch(`${import.meta.env.VITE_BASE_URL}/notification/${notiId}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          console.log("read this notification uncompleted");
        } else if (status == 401) {
          login.handleRefresh();
          clearNotificationById(notiId,notiLevel);
        }
      },
    });
    if (status == 200) {
      getNotificationList(notiLevel);
      getCountAllNotification();
      if(notiLevel == 1){
        getCountSystemNotification();
      } else {
        getCountUserNotification();
      }
      console.log("read this notification completed");
    }
  }

        //Update all notification status (read all)
        async function clearAllNotification(notiLevel) {
          let accessToken = useCookie("accessToken");
          let status = 0;
      
          await $fetch(`${import.meta.env.VITE_BASE_URL}/notification/updateAll`, {
            method: "PUT",
            headers: {
              Authorization: `Bearer ${accessToken.value}`,
            },
            onResponse({ request, response, options }) {
              status = response._data.response_code;
              if (status == 400) {
                console.log("read all notification uncompleted");
              } else if (status == 401) {
                login.handleRefresh();
                clearAllNotification(notiLevel);
              }
            },
          });
          if (status == 200) {
            getNotificationList(notiLevel);
            getCountAllNotification();
            getCountSystemNotification();
            getCountUserNotification();
            console.log("read all notification completed");
          }
        }

  //Clear notification list
  function clearNotificationList() {
    notificationList.value = {
      data: [],
    };
  }

  //Clear all notification number
  function clearCountAllNotification() {
    countAllNotification.value = {
      data: [],
    };
  }

  //Clear all notification number
  function clearCountUserNotification() {
    countUserNotification.value = {
      data: [],
    };
  }

  //Clear all notification number
  function clearCountSystemNotification() {
    countSystemNotification.value = {
      data: [],
    };
  }

    //Clear new notification 
    function clearNewNotification() {
      newNotification.value = {
        title: '',
        detail: '',
        link: null
      };
    }

  // --------------- Report Function ---------------
  //Get Report List
  async function getReportList() {
    let accessToken = useCookie("accessToken");
    let status = 0;
    clearReportList();

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/report/notfix`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken.value}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        reportList.value = data.value;
      }
      console.log("get report list completed");
    } else if (status == 404) {
      clearReportList();
      console.log("get report list uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getReportList();
    }
  }

  //Get Report History List
  async function getReportHistoryList() {
    let accessToken = useCookie("accessToken");
    let status = 0;
    clearReportList();

    const { data } = await useFetch(
      `${import.meta.env.VITE_BASE_URL}/report/fix`,
      {
        onRequest({ request, options }) {
          options.method = "GET";
          options.headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken.value}`,
          };
        },
        onResponse({ request, response, options }) {
          status = response._data.response_code;
        },
      }
    );
    if (status == 200) {
      if (data.value) {
        reportHistoryList.value = data.value;
      }
      console.log("get report history list completed");
    } else if (status == 404) {
      clearReportList();
      console.log("get report history list uncompleted");
    } else if (status == 401) {
      await login.handleRefresh();
      await getReportHistoryList();
    }
  }

  //Create Report Book
  async function createReport() {
    let accessToken = useCookie("accessToken");
    let status = 0;

    await $fetch(`${import.meta.env.VITE_BASE_URL}/report`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
      },
      body: {
        reportTitle: reportProblem.value.reportTitle,
        reportDetail: reportProblem.value.reportDetail,
        problemId: reportProblem.value.problemId,
        reportType: reportProblem.value.reportType,
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          console.log("report uncompleted");
        } else if (status == 401) {
          login.handleRefresh();
          createReport();
        }
      },
    });
    if (status == 201) {
      reportStatus.value = false;
      successfulPopup.value = true;
      clearReportProblem();
      console.log("report completed");
    }
  }

  //Update report status (report done)
  async function updateReportDone(reportId) {
    let accessToken = useCookie("accessToken");
    let status = 0;

    await $fetch(`${import.meta.env.VITE_BASE_URL}/report/${reportId}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${accessToken.value}`,
      },
      onResponse({ request, response, options }) {
        status = response._data.response_code;
        if (status == 400) {
          console.log("this revport uncompleted");
        } else if (status == 401) {
          login.handleRefresh();
          updateReportDone(reportId);
        }
      },
    });
    if (status == 200) {
      getReportList();
      console.log("this report completed");
    }
  }

  function handleReportReview(reviewId) {
    reportStatus.value = true;
    reportProblem.value.reportType = 'review';
    reportProblem.value.problemId = reviewId;
  }
  
  function handleReportBook(bookId) {
    reportStatus.value = true;
    reportProblem.value.reportType = 'book';
    reportProblem.value.problemId = bookId;
  }

  function handleReportUser(userId) {
    reportStatus.value = true;
    reportProblem.value.reportType = 'user';
    reportProblem.value.problemId = userId;
  }

  //Clear report list
  function clearReportList() {
    reportList.value = {
      data: {
        content: [],
      },
    };
  }

  //Clear report history list
  function clearReportHistoryList() {
    reportHistoryList.value = {
      data: [],
    };
  }

  //Clear report problem
  function clearReportProblem() {
    reportProblem.value = {
      reportTitle: "",
      reportDetail: "",
      reportType: "",
      problemId: 0,
    };
  }

  //Close successful popup
  //   function closeSuccessfulPopup() {
  //     successfulPopup.value = false;
  //   }

  // --------------- etc. Function ---------------
  // function countUpdateTime(dateTime,dateValue,dateUnit){
  function countUpdateTime(seconds) {
    let interval = Math.floor(seconds / 31536000);
    if (interval >= 1) {
      return interval + " years ago";
    }

    interval = Math.floor(seconds / 2592000);
    if (interval >= 1) {
      return interval + " months ago";
    }

    interval = Math.floor(seconds / 86400);
    if (interval >= 1) {
      return interval + " days ago";
    }

    interval = Math.floor(seconds / 3600);
    if (interval >= 1) {
      return interval + " hours ago";
    }

    interval = Math.floor(seconds / 60);
    if (interval >= 1) {
      return interval + " minutes ago";
    }

    if (seconds < 10) return "just now";

    return Math.floor(seconds) + " seconds ago";
  }

  //Back Page
  function backPage() {
    window.history.back();
  }

  return {
    notificationList,
    countAllNotification,
    countUserNotification,
    countSystemNotification,
    newNotification,
    reportList,
    reportHistoryList,
    reportStatus,
    reportProblem,
    reportBookList,
    reportReviewList,
    reportUserList,
    successfulPopup,
    getNotificationList,
    getCountAllNotification,
    getCountUserNotification,
    getCountSystemNotification,
    createNotification,
    clearNotificationById,
    clearAllNotification,
    clearNotificationList,
    clearCountAllNotification,
    clearCountUserNotification,
    clearCountSystemNotification,
    clearNewNotification,
    getReportList,
    getReportHistoryList,
    createReport,
    updateReportDone,
    handleReportBook,
    handleReportReview,
    handleReportUser,
    clearReportList,
    clearReportHistoryList,
    clearReportProblem,
    countUpdateTime,
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useNotifications, import.meta.hot));
}
