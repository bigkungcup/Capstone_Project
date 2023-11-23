<script setup>

</script>
 
<template>
    <div>
        <div class="tw-mx-36 tw-mt-2">
            <v-btn prepend-icon="mdi mdi-chevron-left" variant="text" @click="$router.go(-1)" width="8%" class=""> Back
            </v-btn>
        </div>

        <!-- <div class="tw-flex tw-justify-center tw-min-h-[25rem]">
            <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
            <v-row no-gutters>
                    <v-col cols="3" class="tw-my-4" align="center">
                        <v-img src="/image/cover_not_available.jpg" height="35%" width="100%"></v-img>

                    </v-col>
                    <v-col cols="9" class="tw-my-4 tw-space-y-5">
                        <p class="text-h5 tw-text-[#082266]">
                          This is a Name of Book.
                        </p>

                        <p class="text-h6 tw-text-[#1D419F]">
                            by Edogawa Conan
                        </p>
                        <p class="text-h6 tw-text-[#1D419F]">
                            My rating: 
                        </p>
                    </v-col>
                    <v-col cols="12" class="tw-my-4" align="center">
                        <p class="">jeelofjodsspf</p>
                    </v-col>
                </v-row>

            </v-card>
        </div> -->
        <div class="tw-flex tw-justify-center tw-min-h-[25rem]">
            <v-card color="rgb(217, 217, 217, 0.6)" width="80%">
                <v-row>
                    <v-col cols="2">
                        <v-avatar class="ma-3" size="150" rounded="0">
                            <v-img src="/image/bookcover.png"></v-img>
                        </v-avatar>
                    </v-col>
                    <v-col cols="10">
                        <div class="tw-my-2 web-text-title">This is a name of the book</div>
                        <div class="web-text-sub">by: Author's name</div>
                        <div class="tw-my-6 web-text-sub">My rating:</div>
                    </v-col>

                </v-row>
                <div class="tw-mx-8 tw-space-y-4">
                <v-text-field label="Review Header" variant="solo" height="100px"  hide-details></v-text-field>
                <v-textarea label="Review Detail" variant="solo" rows="3" hide-details></v-textarea>
                 <v-checkbox label="Hide entire review because of spoilers" hide-details></v-checkbox>

            </div>

            </v-card>
        </div>

        <div class="tw-flex tw-justify-end  tw-mx-[9rem] tw-my-5 tw-space-x-4">
            <v-btn color="#1D419F" variant="outlined">clear</v-btn> 
            <v-btn color="#1D419F" variant="flat">submit</v-btn>
        </div>
    </div>
</template>
 
<style></style>