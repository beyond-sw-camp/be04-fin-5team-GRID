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

  const fetchReqApprovalList = async(typeId, approvalStatus, approverId) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval/approver/${typeId}/${approvalStatus}/${approverId}`);

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

    await fetchReqApprovalList(1, 5, userId.value);

    isLoading.value = false;
  })
</script>

<template>
  <nav style="--bs-breadcrumb-divider: '>'; margin-top: -35px; margin-bottom: -7px;" aria-label="breadcrumb">
    <ol class="breadcrumb">
      <li class="breadcrumb-item"><a href="http://localhost:5173/approval" style="text-decoration: none; color: grey; font-size: 17px;"><i class="bi bi-collection"></i>&nbsp; 결재 목록</a></li>
      <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i class="bi bi-briefcase"></i>&nbsp; 결재 필요 문서</span></li>
    </ol>
  </nav>
  <h3 class="fw-bolder pb-5"><i class="bi bi-briefcase"></i>&nbsp; 결재 필요 문서</h3>
  <b-card no-body>
    <b-tabs card>
      <b-tab title="출장" @click="fetchReqApprovalList(1, 5, userId)" active>
        <b-card-text><ApprovalList :approvalList="state.approvalList"/></b-card-text>
      </b-tab>
      <b-tab title="시간 외 근무" @click="fetchReqApprovalList(2, 5, userId)">
        <ApprovalList :approvalList="state.approvalList"/>
      </b-tab>
      <b-tab title="단축 근무" @click="fetchReqApprovalList(3, 5, userId)">
        <ApprovalList :approvalList="state.approvalList"/>
      </b-tab>
      <b-tab title="휴가" @click="fetchReqApprovalList(4, 5, userId)">
        <ApprovalList :approvalList="state.approvalList"/>
      </b-tab>
    </b-tabs>
  </b-card>
</template>

<style scoped>

</style>