<script setup>
defineEmits(["get","done"]);
defineProps({
    reportList: {
        type: Array,
        require: true,
    },
})
</script>
 
<template>
    <v-container>
      <v-card>
        <v-table>
          <tbody class="tw-text-2xl tw-text-white tw-bg-[#1646C4]">
            <tr>
              <th>Category</th>
              <th>No.</th>
              <th>Type</th>
              <th>Link</th>
              <th></th>
            </tr>
          </tbody>
          <tbody >
            <tr v-for="report in reportList" >
              <td class="tw-cursor-pointer" @click="$emit('get',report.reportId)">
                <v-icon style="font-size: 2rem" class="tw-mx-6" v-if="report.reportType == 'book'">
                  <svg
                    viewBox="0 0 24 24"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                    <g
                      id="SVGRepo_tracerCarrier"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                    ></g>
                    <g id="SVGRepo_iconCarrier">
                      <path
                        fill-rule="evenodd"
                        clip-rule="evenodd"
                        d="M23 4C23 3.11596 21.855 2.80151 21.0975 2.59348C21.0279 2.57437 20.9616 2.55615 20.8997 2.53848C19.9537 2.26818 18.6102 2 17 2C15.2762 2 13.8549 2.574 12.8789 3.13176C12.7296 3.21707 12.5726 3.33492 12.4307 3.44143C12.2433 3.58215 12.0823 3.70308 12 3.70308C11.9177 3.70308 11.7567 3.58215 11.5693 3.44143C11.4274 3.33492 11.2704 3.21707 11.1211 3.13176C10.1451 2.574 8.72378 2 7 2C5.38978 2 4.0463 2.26818 3.10028 2.53848C3.04079 2.55547 2.97705 2.57302 2.91016 2.59144C2.156 2.79911 1 3.11742 1 4V17C1 17.3466 1.17945 17.6684 1.47427 17.8507C1.94329 18.1405 2.56224 17.8868 3.11074 17.662C3.30209 17.5835 3.48487 17.5086 3.64972 17.4615C4.4537 17.2318 5.61022 17 7 17C8.2613 17 9.20554 17.4161 9.9134 17.8517C10.0952 17.9636 10.279 18.1063 10.4676 18.2527C10.9338 18.6148 11.4298 19 12 19C12.5718 19 13.0653 18.6162 13.5307 18.2543C13.7195 18.1074 13.9037 17.9642 14.0866 17.8517C14.7945 17.4161 15.7387 17 17 17C18.3898 17 19.5463 17.2318 20.3503 17.4615C20.5227 17.5108 20.7099 17.5898 20.9042 17.6719C21.4443 17.9 22.0393 18.1513 22.5257 17.8507C22.8205 17.6684 23 17.3466 23 17V4ZM3.33252 4.55749C3.13163 4.62161 3 4.81078 3 5.02166V14.8991C3 15.233 3.32089 15.4733 3.64547 15.3951C4.53577 15.1807 5.67777 15 7 15C8.76309 15 10.0794 15.5994 11 16.1721V5.45567C10.7989 5.29593 10.5037 5.08245 10.1289 4.86824C9.35493 4.426 8.27622 4 7 4C5.41509 4 4.12989 4.30297 3.33252 4.55749ZM17 15C15.2369 15 13.9206 15.5994 13 16.1721V5.45567C13.2011 5.29593 13.4963 5.08245 13.8711 4.86824C14.6451 4.426 15.7238 4 17 4C18.5849 4 19.8701 4.30297 20.6675 4.55749C20.8684 4.62161 21 4.81078 21 5.02166V14.8991C21 15.233 20.6791 15.4733 20.3545 15.3951C19.4642 15.1807 18.3222 15 17 15Z"
                        fill="#082266"
                      ></path>
                      <path
                        d="M2.08735 20.4087C1.86161 19.9047 2.08723 19.3131 2.59127 19.0873C3.05951 18.8792 3.54426 18.7043 4.0318 18.5478C4.84068 18.2883 5.95911 18 7 18C8.16689 18 9.16285 18.6289 9.88469 19.0847C9.92174 19.1081 9.95807 19.131 9.99366 19.1534C10.8347 19.6821 11.4004 20 12 20C12.5989 20 13.1612 19.6829 14.0012 19.1538C14.0357 19.1321 14.0708 19.1099 14.1066 19.0872C14.8291 18.6303 15.8257 18 17 18C18.0465 18 19.1647 18.2881 19.9732 18.548C20.6992 18.7814 21.2378 19.0122 21.3762 19.073C21.8822 19.2968 22.1443 19.8943 21.9118 20.4105C21.6867 20.9106 21.0859 21.1325 20.5874 20.9109C20.1883 20.7349 19.7761 20.5855 19.361 20.452C18.6142 20.2119 17.7324 20 17 20C16.4409 20 15.9037 20.3186 15.0069 20.8841C14.2635 21.3529 13.2373 22 12 22C10.7619 22 9.73236 21.3521 8.98685 20.8829C8.08824 20.3173 7.55225 20 7 20C6.27378 20 5.39222 20.2117 4.64287 20.4522C4.22538 20.5861 3.80974 20.7351 3.4085 20.9128C2.9045 21.1383 2.31305 20.9127 2.08735 20.4087Z"
                        fill="#082266"
                      ></path>
                    </g>
                  </svg>
                </v-icon>
                <v-icon style="font-size: 2rem" class="tw-mx-6" v-if="report.reportType == 'review'">
                  <svg
                    viewBox="0 0 24 24"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                    <g
                      id="SVGRepo_tracerCarrier"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                    ></g>
                    <g id="SVGRepo_iconCarrier">
                      <path
                        d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 14.663 3.04094 17.0829 4.73812 18.875L2.72681 21.1705C2.44361 21.4937 2.67314 22 3.10288 22H12Z"
                        stroke="#082266"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      ></path>
                      <path
                        d="M7 9H17"
                        stroke="#082266"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      ></path>
                      <path
                        d="M7 13H11"
                        stroke="#082266"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      ></path>
                    </g>
                  </svg>
                </v-icon>
                <v-icon style="font-size: 2rem" class="tw-mx-6" v-if="report.reportType == 'user'">
                  <svg
                    viewBox="0 0 24 24"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                    <g
                      id="SVGRepo_tracerCarrier"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                    ></g>
                    <g id="SVGRepo_iconCarrier">
                      <path
                        d="M16 7C16 9.20914 14.2091 11 12 11C9.79086 11 8 9.20914 8 7C8 4.79086 9.79086 3 12 3C14.2091 3 16 4.79086 16 7Z"
                        stroke="#082266"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      ></path>
                      <path
                        d="M12 14C8.13401 14 5 17.134 5 21H19C19 17.134 15.866 14 12 14Z"
                        stroke="#082266"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      ></path>
                    </g>
                  </svg>
                </v-icon>
              </td>
              <td class="tw-text-xl tw-text-[#082266] tw-cursor-pointer" @click="$emit('get',report.reportId)">#{{ report.reportId }}</td>
              <td class="tw-text-xl tw-text-[#082266] tw-cursor-pointer" @click="$emit('get',report.reportId)">
                {{ report.reportTitle }}
              </td>
              <td>
                <NuxtLink :to="report.reportType == 'user' ? `/user/${report.data.userId}/` : report.reportType == 'book' ? `/book/update_${report.data.bookId}/` : `/review/update_${report.data.reviewId}/`">
                  <v-icon style="font-size: 2rem"
                    ><svg
                      viewBox="0 0 24 24"
                      fill="none"
                      xmlns="http://www.w3.org/2000/svg"
                    >
                      <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                      <g
                        id="SVGRepo_tracerCarrier"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      ></g>
                      <g id="SVGRepo_iconCarrier">
                        <line
                          x1="10.8492"
                          y1="13.0606"
                          x2="19.435"
                          y2="4.47485"
                          stroke="#082266"
                          stroke-width="2"
                          stroke-linecap="round"
                          stroke-linejoin="round"
                        ></line>
                        <path
                          d="M19.7886 4.12134L20.1421 8.01042"
                          stroke="#082266"
                          stroke-width="2"
                          stroke-linecap="round"
                          stroke-linejoin="round"
                        ></path>
                        <path
                          d="M19.7886 4.12134L15.8995 3.76778"
                          stroke="#082266"
                          stroke-width="2"
                          stroke-linecap="round"
                          stroke-linejoin="round"
                        ></path>
                        <path
                          d="M18 13.1465V17.6465C18 19.3033 16.6569 20.6465 15 20.6465H6C4.34315 20.6465 3 19.3033 3 17.6465V8.64648C3 6.98963 4.34315 5.64648 6 5.64648H10.5"
                          stroke="#082266"
                          stroke-width="2"
                          stroke-linecap="round"
                          stroke-linejoin="round"
                        ></path>
                      </g></svg
                  ></v-icon>
                </NuxtLink>
              </td>
              <td><v-btn color="#1D419F" @click="$emit('done',report.reportId)">Done</v-btn></td>
            </tr>
          </tbody>
        </v-table>
      </v-card>
    </v-container>
</template>
 
<style>

</style>