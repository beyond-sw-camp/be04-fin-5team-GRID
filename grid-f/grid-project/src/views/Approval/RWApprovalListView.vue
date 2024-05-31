<script setup>
  import {onMounted, reactive, ref} from "vue";
    import axios from "axios";

    import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const userRole = ref('');
  const userId = ref();

  const isLoading = ref(true);

  const state = reactive({
    approvalList:[]
  });

  const fetchApprovalList = async(id) => {
    try {
      const url = `http://localhost:8080/approval/all/${typeId}/5`;

      const response = await axios.get(url);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.approvalList = response.data.approvalEmpResultList;
      state.approvalList.type = "rw";

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async() => {
    await fetchApprovalList(userId.value);
  })
</script>

<template>
  <ApprovalList :approvalList="state.approvalList"/>
</template>

<style scoped>

</style>