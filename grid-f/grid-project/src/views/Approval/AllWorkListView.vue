<script setup>
  import {onMounted, reactive} from "vue";
  import {useRouter} from "vue-router";
  import axios from "axios";

  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const admin = 0;
  const employeeId = 2;

  const router = useRouter();

  const state = reactive({
    approvalList: [],
    btApprovalList: [],
    oApprovalList: [],
    rwApprovalList: []
  });

  const navigateTo = (path) => {
    router.push(path);
  }

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

      switch(typeId) {
        case 1:
          state.btApprovalList = response.data.approvalEmpResultList.slice(0, 3);
          state.approvalList = response.data.approvalEmpResultList;
          state.btApprovalList.type = "bt";
          break;

        case 2:
          state.oApprovalList = response.data.approvalEmpResultList.slice(0, 3);
          state.approvalList = response.data.approvalEmpResultList;
          state.oApprovalList.type = "o";
          break;

        case 3:
          state.rwApprovalList = response.data.approvalEmpResultList.slice(0, 3);
          state.approvalList = response.data.approvalEmpResultList;
          state.rwApprovalList.type = "rw";
          break;
      }

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }

  }

  onMounted(async() => {
    // 버튼 클릭에 따라 다르게 실행
    // 0: 전체, 1: 승인, 2: 반려, 3: 대기
    await fetchApprovalList(1,1,  employeeId);
    await fetchApprovalList(2,1,  employeeId);
    await fetchApprovalList(3,1,  employeeId);
  })
</script>

<template>
  <b-button @click="navigateTo('/bt')">상세</b-button>
  <b-card title="출장">
    <br>
    <ApprovalList :approvalList="state.btApprovalList" :short="1"/>
  </b-card>
  <b-button @click="navigateTo('/overtime')">상세</b-button>
  <ApprovalList :approvalList="state.oApprovalList"/>
  <b-button @click="navigateTo('/rw')">상세</b-button>
  <ApprovalList :approvalList="state.rwApprovalList"/>
</template>

<style scoped>

</style>