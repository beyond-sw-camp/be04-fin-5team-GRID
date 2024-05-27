<script setup>
  import {onMounted, reactive} from "vue";
  import {useRoute} from "vue-router";
  import axios from "axios";

  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const typeId = 1;

  const route = useRoute();
  const employeeId = route.params.employeeId;

  const state = reactive({
    approvalList:[]
  });

  const fetchApprovalList = async(id) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval/list/${typeId}/${id}/0`);

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
    await fetchApprovalList(employeeId);
  })
</script>

<template>
  <ApprovalList :approvalList="state.approvalList"/>
</template>

<style>

</style>