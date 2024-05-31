<script setup>
  import {onMounted, reactive, ref} from "vue";
  import {useRouter} from "vue-router";
  import axios from "axios";

  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const router = useRouter();

  const userRole = ref('');
  const userId = ref();

  const isLoading = ref(true);


  const state = reactive({
    approvalList: [],
    btApprovalList: [],
    oApprovalList: [],
    rwApprovalList: []
  });

  const navigateTo = (path) => {
    router.push(path);
  }

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
      let url = `http://localhost:8080/approval/all/${typeId}/${approvalStatus}`;

      if (userRole.value !== 'ROLE_ADMIN') {
        url = `http://localhost:8080/approval/list/${typeId}/${approvalStatus}/${employeeId}`;
      }

      const response = await axios.get(url);

      if (response === null || response.status !== 200) {
        throw new Error("response is not ok");
      }

      console.log(url)

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
    const token = localStorage.getItem('access');

    if (token) {
      const decodedToken = parseJwt(token);

      userRole.value = decodedToken.auth || '';
      userId.value = decodedToken.id || '';
    }

    // 버튼 클릭에 따라 다르게 실행

    // 0: 전체, 1: 승인, 2: 반려, 3: 대기
    await fetchApprovalList(1,5,  userId.value);
    await fetchApprovalList(2,5,  userId.value);
    await fetchApprovalList(3,5,  userId.value);

    isLoading.value = false;

    console.log(userRole.value)
  })
</script>
<template>
  <div>근무 목록</div>
  <div v-if="isLoading">로딩 중</div>
  <div v-else>
    <b-button @click="navigateTo('/bt')">상세</b-button>
    <b-card title="출장">
      <br>
      <ApprovalList :approvalList="state.btApprovalList" :short="1"/>
    </b-card>
    <b-button @click="navigateTo('/overtime')">상세</b-button>
    <b-card title="시간 외 근무">
      <br>
      <ApprovalList :approvalList="state.oApprovalList" :short="1"/>
    </b-card>
    <!-- 관리자 -->
    <div v-if="userRole === 'ROLE_ADMIN'">
      <b-button @click="navigateTo('/rw')">상세</b-button>
      <b-card title="단축 근무">
        <br>
        <ApprovalList :approvalList="state.rwApprovalList" :short="1"/>
      </b-card>
    </div>
  </div>
</template>
<style scoped>

</style>