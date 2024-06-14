<script setup>
  import {onMounted, reactive} from "vue";
  import {useRoute} from "vue-router";
  import axios from "axios";

  import ApprovalCard from "@/components/Approval/ApprovalCard.vue";
  import ApprovalChain from "@/components/Approval/ApprovalChain.vue";
  import defaultProfileImage from '@/assets/defaultProfile.jpg';

  const route = useRoute();
  const typeId = route.params.typeId;
  const approvalId = route.params.approvalId;

  const state = reactive({
    approval: [],
    user: []
  })

  const fetchApproval = async(typeId, approvalId) => {
    try {
      const response = await axios.get(`/api/approval/detail/${typeId}/${approvalId}`);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.approval = response.data.approvalEmpResult;
      state.user = state.approval.user;

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async() => {
    await fetchApproval(typeId, approvalId);
  })
</script>

<template>
  <div class="all">
  <div class="detail">
    <div>
      <h3 class="fw-bolder mb-3" style="font-family: 'IBMPlexSansKR-Regular', sans-serif;"><i class="bi bi-file-earmark-text"></i>&nbsp; 결재 문서</h3>
      <b-card class="container shadow" style="font-family: 'IBMPlexSansKR-Regular', sans-serif;">
        <div class="mx-1 my-3">
          <div class="text-success" v-if="state.approval['approvalStatus'] === 'A'"> <h5 style="font-family: 'IBMPlexSansKR-Regular', sans-serif;">승인됨</h5></div>
          <div class="text-danger" v-else-if="state.approval['approvalStatus'] === 'D'"> <h5 style="font-family: 'IBMPlexSansKR-Regular', sans-serif;">반려됨</h5></div>
          <div class="text-warning" v-else-if="state.approval['cancelYn'] === 'Y'"> <h5 style="font-family: 'IBMPlexSansKR-Regular', sans-serif;">취소</h5></div>
          <div class="text-muted" v-else> <h5 style="font-family: 'IBMPlexSansKR-Regular', sans-serif;">결재 대기중</h5></div>
        </div>
        <div class="title">
          <h1 v-if="typeId === '1'">출장 신청서</h1>
          <h1 v-if="typeId === '2'">시간 외 근무 신청서</h1>
          <h1 v-if="typeId === '3'">단축 근무 신청서</h1>
          <h1 v-if="typeId === '4'">휴가 신청서</h1>
        </div>
        <div class="text-secondary mb-4">{{ state.approval['writeTime'] }} &nbsp;작성</div>
        <div v-if="state.approval && state.approval.user && state.approval.user.duties" style="font-family: 'IBMPlexSansKR-Regular', sans-serif;">
          <div href="#" class="list-group-item list-group-item-action d-flex gap-3 py-3" aria-current="true" style="margin-bottom: -10px; font-family: 'IBMPlexSansKR-Regular', sans-serif;">
            <template v-if="state.user['profilePath'] === null"><img :src="defaultProfileImage" alt="profile" width="50" height="50" class="rounded-circle flex-shrink-0"></template>
            <template v-else><img :src="state.user['profilePath']" alt="profile" width="50" height="50" class="rounded-circle flex-shrink-0"/></template>
            <div class="d-flex gap-2 w-100 justify-content-between">
              <div class="mt-1">
                <h5 class="fw-bolder mb-0" style="font-family: 'IBMPlexSansKR-Regular', sans-serif;">&nbsp;&nbsp;&nbsp;{{ state.user['name'] }}</h5>
                <p class="mb-0 opacity-75">&nbsp;&nbsp;&nbsp;{{ state.user['team'].teamName }} / {{ state.user['duties'].dutiesName }}</p>
              </div>
            </div>
          </div>
          <hr style="border-top: 2px dotted #000; margin-bottom: 20px;">
          <div style="margin-left: 10px;">
            <h6 class="mb-3"><strong style="font-family: 'IBMPlexSansKR-Regular', sans-serif;"><i class="bi fs-5 bi-envelope"></i>&nbsp; &nbsp;</strong> {{ state.user['email'] }}</h6>
            <h6><strong style="font-family: 'IBMPlexSansKR-Regular', sans-serif;"><i class="bi fs-5 bi-telephone"></i>&nbsp; &nbsp; </strong> {{ state.user['callNumber'] }}</h6>
          </div>
          <hr style="border-top: 2px dotted #000; margin-top: 20px; margin-bottom: 30px;">
        </div>
        <ApprovalCard :approval="state.approval" :typeId="typeId"/>
      </b-card>
    </div>
    <ApprovalChain :typeId="typeId" :approvalId="approvalId" :approvalStatus="state.approval['approvalStatus']" :requesterId="state.approval['employeeId']" :cancelStatus="state.approval['cancelYn']" :cancelDoc="state.approval['cancelDocId']"/>
  </div>
</div>
</template>

<style scoped>
.all {
  display:grid;
  grid-template-columns: 10% 80% 10%;
  grid-template-rows: 13% 80% 7%;
}

.all h3 {
  margin-left: 0.5%;
  margin-bottom: 0;
  font-size: 25px;
  font-weight: 600;
}

.detail {
  grid-column-start: 2;
  grid-row-start: 2;
  display: grid;
  grid-template-columns: 3.5fr 2fr;
  gap: 20px;
}

.title h1 {
  margin-left: 0.5%;
  margin-bottom: 0;
  font-size: 23px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}
</style>
