<script setup>
  import {onMounted, reactive} from "vue";
  import axios from "axios";

  import ApprovalList from "@/components/Approval/ApprovalList.vue";
  import {useRoute} from "vue-router";

  const route = useRoute();
  const employeeId = route.params.employeeId;

  const state = reactive({
    approvalList: []
  });

  const fetchApprovalList = async(typeId, employeeId, approvalStatus) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval/list/${typeId}/${employeeId}/${approvalStatus}`);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.approvalList = response.data.approvalEmpResultList;
      state.approvalList.type = typeId;

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async() => {
    // 버튼 클릭에 따라 다르게 실행
    // 0: 전체, 1: 승인, 2: 반려, 3: 대기
    await fetchApprovalList(1, employeeId, 0);
  })
</script>

<template>
  <ApprovalList :approvalList="state.approvalList"/>
</template>

<style scoped>

</style>