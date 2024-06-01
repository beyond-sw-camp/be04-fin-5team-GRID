<script setup>
  import {onMounted, reactive, ref} from "vue";
  import axios from "axios";
  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const userId = ref();

  const isLoading = ref(true);

  const state = reactive({
    approvalList: []
  })

  function parseJwt(token) {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      return JSON.parse(jsonPayload);
    } catch (error) {
      console.error('Invalid token', error);
      return null;
    }
  }

  const fetchApprovalList = async(typeId, approvalStatus, employeeId) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval/list/${typeId}/${approvalStatus}/${employeeId}`);

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
    const token = localStorage.getItem('access');

    if (token) {
      const decodedToken = parseJwt(token);

      userId.value = decodedToken.id || '';
    }

    await fetchApprovalList(1, 0, userId.value);

    isLoading.value = false;
  })
</script>

<template>
  <ApprovalList :approvalList="state.approvalList"/>
</template>

<style scoped>

</style>