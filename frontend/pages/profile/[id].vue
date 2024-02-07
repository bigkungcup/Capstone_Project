<script setup>

</script>
 
<template>
    <div class="tw-pt-1 tw-pb-10 tw-drop-shadow-lg tw-space-y-1">
        <div class="tw-mx-36 tw-mt-5">
            <v-btn prepend-icon="mdi mdi-chevron-left" variant="text" width="8%" color="#082250" @click="$router.go(-1)">
                <p class="tw-font-bold">Back</p>
            </v-btn>
        </div>

        <div class="tw-flex tw-justify-center tw-min-h-[32rem] tw-pb-2">
            <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
                <v-row no-gutters>
                    <v-col cols="3" align="center">
                        <v-img src="/image/cat.jpg" width="200" height="200" class="tw-rounded-full tw-m-5" cover />
                        <div class="d-flex justify-center">
                            <v-btn>
                                <p class="tw-font-bold tw-text-[#1D419F] tw-text-xs">
                                    Select image
                                </p>
                                <span class="mdi mdi-chevron-right"></span>
                            </v-btn>
                        </div>
                    </v-col>
                    <v-col cols="9">
                        <div class="tw-mx-8 tw-my-6">
                            <div class="tw-space-y-2">
                                <p class="web-text-sub">Username</p>
                                <v-text-field label="Username" variant="solo" hide-details></v-text-field>
                                <p class="web-text-sub">Email</p>
                                <v-text-field label="Email" variant="solo" hide-details></v-text-field>
                                <p class="web-text-sub">Bio</p>
                                <v-textarea label="Bio" variant="solo" rows="3" hide-details></v-textarea>
                                <div>
                                    <span class="web-text-sub">Password</span>
                                    <span class="tw-text-cyan-500 tw-underline tw-mx-4" @click="">Change password</span>
                                </div>
                            </div>

                            <div class="tw-space-y-4">
                                <v-row class="tw-flex tw-items-center" no-gutters>
                                    <v-col cols="1"></v-col>
                                    <v-col cols="2" class="d-flex align-center justify-center">
                                        <span class="web-text-sub ">Old password</span>
                                    </v-col>
                                    <v-col cols="8">
                                        <v-text-field label="Enter old password" variant="solo" hide-details></v-text-field>
                                    </v-col>
                                </v-row>
                                <v-row no-gutters class="tw-flex tw-items-center">
                                    <v-col cols="1"></v-col>
                                    <v-col cols="2" class="d-flex align-center justify-center">
                                        <span class="web-text-sub">New password</span>
                                    </v-col>
                                    <v-col cols="8">
                                        <v-text-field label="Enter new password" variant="solo" hide-details></v-text-field>
                                    </v-col>
                                </v-row>
                                <v-row no-gutters>
                                    <v-col cols="3" class="d-flex align-center justify-center">
                                        <span class="web-text-sub">Confirm new password</span>
                                    </v-col>
                                    <v-col cols="8">
                                        <v-text-field label="Enter confirm password" variant="solo"
                                            hide-details></v-text-field>
                                    </v-col>
                                </v-row>
                            </div>
                        </div>

                    </v-col>
                </v-row>
            </v-card>
        </div>



        <div class="d-flex justify-end tw-mx-[9rem] tw-space-x-4">
            <v-btn color="#1D419F" variant="outlined" @click="">reset</v-btn>
            <v-btn color="#1D419F" variant="flat" @click="">submit</v-btn>
        </div>
    </div>
</template>
 
<style></style>