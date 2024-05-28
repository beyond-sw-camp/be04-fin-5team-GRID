<script setup>
  import {onMounted, reactive} from "vue";
  import axios from "axios";
  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const state = reactive({
    approvalList: []
  })

  const fetchReqApprovalList = async(typeId, approverId, approvalStatus) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval/approver/${typeId}/${approverId}/${approvalStatus}`);

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
    await fetchReqApprovalList(1, 1, 0);
  })
</script>

<template>
  <ApprovalList :approvalList="state.approvalList"/>
</template>

<style scoped>

</style>