<script setup>
  import { onMounted, reactive } from "vue";
  import axios from "axios";
  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const typeId = 1;
  const state = reactive({
    approvalList:[]
  });

  const fetchApprovalList = async(id) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval/list/${typeId}/${id}`);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.approvalList = response.data.approvalEmpResultList;
      state.approvalList.type = "bt";

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async() => {
    await fetchApprovalList(1);
  })
</script>

<template>
  <ApprovalList :approvalList="state.approvalList"/>
</template>

<style>

</style>