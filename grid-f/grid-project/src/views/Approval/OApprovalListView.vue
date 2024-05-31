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

  const fetchApprovalList = async(id) => {
    try {
      let url = `http://localhost:8080/approval/all/2/5`;

      if (userRole.value !== 'ROLE_ADMIN') {
        url = `http://localhost:8080/approval/list/2/5/${id}`;
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

      console.log(userRole.value)

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async() => {
    const token = localStorage.getItem('access');

    if (token) {
      const decodedToken = parseJwt(token);

      userRole.value = decodedToken.auth || '';
      userId.value = decodedToken.id || '';
    }

    await fetchApprovalList(userId.value);
    await countOvertimeInWeek(userId.value);

    isLoading.value = false;
  })

</script>

<template>
  <div v-if="isLoading">로딩 중</div>
  <div v-else>
    <div v-if="userRole !== 'ROLE_ADMIN'">{{ state.overtimeInWeek }}</div>
    <ApprovalList :approvalList="state.approvalList"/>
  </div>
</template>

<style scoped>

</style>