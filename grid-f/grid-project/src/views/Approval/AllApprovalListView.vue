<script setup>
  import {onMounted, reactive} from "vue";
  import axios from "axios";

  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const admin = 0;
  const employeeId = 1;

  const state = reactive({
    approvalList: [],
    reqApprovalList: []
  });

  const fetchApprovalList = async(typeId, approvalStatus, employeeId) => {

    try {
      let url = `http://localhost:8080/approval/all/${typeId}/${approvalStatus}`;

      if (admin !== 1) {
        url = `http://localhost:8080/approval/list/${typeId}/${approvalStatus}/${employeeId}`;
      }

      const response = await axios.get(url);

      if (response === null || response.status !== 200) {
        throw new Error("response is not ok");
      }

      // 상태 업데이트
      state.approvalList = response.data.approvalEmpResultList;
      state.approvalList.type = typeId;

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  const fetchReqApprovalList = async(typeId, approvalStatus, approverId) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval/approver/${typeId}/${approvalStatus}/${approverId}`);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.reqApprovalList = response.data.approvalEmpResultList;
      state.reqApprovalList.type = typeId;

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async() => {
    // 버튼 클릭에 따라 다르게 실행
    // 0: 전체, 1: 승인, 2: 반려, 3: 대기
    await fetchApprovalList(1,0,  employeeId);
    await fetchReqApprovalList(1, 0, employeeId);
  })
</script>

<template>
  <ApprovalList :approvalList="state.approvalList"/>
  <ApprovalList v-if="admin !== 1" :approvalList="state.reqApprovalList"/>
</template>

<style scoped>

</style>