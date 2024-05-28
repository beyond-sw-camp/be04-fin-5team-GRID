<script setup>
  import { onMounted, reactive } from "vue";
  import axios from "axios";

  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const typeId = 2;

  const admin = 1;

  const state = reactive({
    approvalList:[]
  });

  const fetchApprovalList = async(id) => {
    try {
      let url = `http://localhost:8080/approval/all/${typeId}/5`;

      if (admin !== 1) {
        url = `http://localhost:8080/approval/list/${typeId}/5/${id}`;
      }

      const response = await axios.get(url);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.approvalList = response.data.approvalEmpResultList;
      state.approvalList.type = "o";

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  const countOvertimeInWeek = async(id) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval/overtime-count/${id}`);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.overtimeInWeek = response.data;

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async() => {
    await fetchApprovalList(2);
    await countOvertimeInWeek(2);
  })

</script>

<template>
  <div v-if="admin !== 1">{{ state.overtimeInWeek }}</div>
  <ApprovalList :approvalList="state.approvalList"/>
</template>

<style scoped>

</style>