<script setup>
  import {onMounted, reactive} from "vue";
  import {useRoute} from "vue-router";
  import axios from "axios";

  import ApprovalCard from "@/components/Approval/ApprovalCard.vue";
  import ApprovalChain from "@/components/Approval/ApprovalChain.vue";

  const route = useRoute();
  const typeId = route.params.typeId;
  const approvalId = route.params.approvalId;

  const state = reactive({
    approval: [],
    user: []
  })

  const fetchApproval = async(typeId, approvalId) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval/detail/${typeId}/${approvalId}`);

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
  <div class="detail">
    <div>
      <h3 class="fw-bolder"><i class="bi bi-file-earmark-text"></i>&nbsp; 결재 문서</h3>
      <b-card class="container">
        <div>
          <h1 v-if="typeId === '1'">출장 신청서</h1>
          <h1 v-if="typeId === '2'">시간 외 근무 신청서</h1>
          <h1 v-if="typeId === '3'">단축 근무 신청서</h1>
          <h1 v-if="typeId === '4'">휴가 신청서</h1>
        </div>
        <div v-if="state.approval && state.approval.user && state.approval.user.duties">
          <img :src="state.user['profilePath']" alt="profile">
          작성자: {{ state.user['name'] }}<br>
          {{ state.approval.user.duties.dutiesName }} / {{ state.user['team'].teamName }}
          <hr>
          {{state.user['email']}}
          {{state.user['callNumber']}}
          <hr>
        </div>
        <ApprovalCard :approval="state.approval"/>
      </b-card>
    </div>
    <ApprovalChain :typeId="typeId" :approvalId="approvalId" :approvalStatus="state.approval['approvalStatus']" :requesterId="state.approval['employeeId']" :cancelStatus="state.approval['cancelYN']"/>
  </div>
</template>

<style scoped>
.detail {
  display: grid;
  grid-template-columns: 3.5fr 2fr;
  gap: 20px;
}
</style>