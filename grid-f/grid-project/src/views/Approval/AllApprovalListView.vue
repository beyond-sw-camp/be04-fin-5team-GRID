<script setup>
  import {onMounted, reactive} from "vue";
  import axios from "axios";

  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const admin = 0;
  const employeeId = 2;

  const state = reactive({
    approvalList: []
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

  onMounted(async() => {
    // 버튼 클릭에 따라 다르게 실행
    // 0: 전체, 1: 승인, 2: 반려, 3: 대기
    await fetchApprovalList(2,0,  employeeId);
  })
</script>

<template>
  <ApprovalList :approvalList="state.approvalList"/>
</template>

<style scoped>

</style>