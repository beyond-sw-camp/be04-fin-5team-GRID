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
  <nav style="--bs-breadcrumb-divider: '>'; margin-top: -35px; margin-bottom: -7px;" aria-label="breadcrumb">
    <ol class="breadcrumb">
      <li class="breadcrumb-item"><a href="http://localhost:5173/work" style="text-decoration: none; color: grey; font-size: 17px;"><i class="bi bi-list-check"></i>&nbsp; 근무 목록</a></li>
      <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i class="bi bi-clock"></i>&nbsp; 시간 외 근무</span></li>
    </ol>
  </nav>
  <h3 class="fw-bolder pb-5"><i class="bi bi-clock"></i>&nbsp; 시간 외 근무</h3>
  <div v-if="isLoading">로딩 중</div>
  <div v-else>
    <div v-if="userRole !== 'ROLE_ADMIN'">
      <div class="mb-3">이번 주 시간 외 근무 시간 합계: </div>
      <b-progress :max="12" height="2rem" show-progress class="mb-5">
        <b-progress-bar variant="info" :value="state.overtimeInWeek">
          <span>{{ state.overtimeInWeek }}시간 / 12시간</span>
        </b-progress-bar>
      </b-progress>
    </div>
    <ApprovalList :approvalList="state.approvalList"/>
  </div>
</template>

<style scoped>

</style>