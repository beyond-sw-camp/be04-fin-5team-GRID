<script setup>
  import {onMounted, reactive} from "vue";
  import axios from "axios";

  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const typeId = 1;

  // admin 체크 필요
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
      state.approvalList.type = "bt";

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async() => {
    await fetchApprovalList(2);
  })
</script>

<template>
  <ApprovalList :approvalList="state.approvalList"/>
</template>

<style>

</style>