<script setup>
  import { onMounted, reactive, ref } from "vue";
  import { useRouter } from "vue-router";
  import axios from "axios";

  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const router = useRouter();

  const userRole = ref('');
  const userId = ref();

  const isLoading = ref(true);

  const state = reactive({
    approvalList: [],
    reqApprovalList: [],
    sApprovalList: [],
    sReqApprovalList: []
  });

  const navigateTo = (path) => {
    router.push(path);
  };

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

  const fetchApprovalList = async (typeId, approvalStatus, employeeId) => {
    try {
      let url = `http://localhost:8080/approval/all/${typeId}/${approvalStatus}`;
      if (userRole.value !== 'ROLE_ADMIN') {
        url = `http://localhost:8080/approval/list/${typeId}/${approvalStatus}/${employeeId}`;
      }
      const response = await axios.get(url);
      if (response === null || response.status !== 200) {
        throw new Error("response is not ok");
      }
      state.approvalList = response.data.approvalEmpResultList;
      state.sApprovalList = response.data.approvalEmpResultList.slice(0, 5);
      state.approvalList.type = typeId;
      console.log(url);
    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  const fetchReqApprovalList = async (typeId, approvalStatus, approverId) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval/approver/${typeId}/${approvalStatus}/${approverId}`);
      if (response.status !== 200) {
        throw new Error("response is not ok");
      }
      state.reqApprovalList = response.data.approvalEmpResultList;
      state.sReqApprovalList = response.data.approvalEmpResultList.slice(0, 5);
      state.reqApprovalList.type = typeId;
    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async () => {
    const token = localStorage.getItem('access');
    if (token) {
      const decodedToken = parseJwt(token);

      userRole.value = decodedToken.auth || '';
      userId.value = decodedToken.id || '';
    }

    await fetchApprovalList(1, 0, userId.value);
    await fetchReqApprovalList(1, 5, userId.value);

    isLoading.value = false;
  })
</script>

<template>
  <div>결재 목록</div>
  <div v-if="isLoading">
    로딩 중
  </div>
  <div v-else>
    <div v-if="userRole === 'ROLE_ADMIN'">
      <!-- 관리자 -->
      <div>
        <b-card no-body>
          <b-tabs card>
            <b-tab title="출장" @click="fetchApprovalList(1, 0, userId.value)" active>
              <b-card-text><ApprovalList :approvalList="state.approvalList"/></b-card-text>
            </b-tab>
            <b-tab title="시간 외 근무" @click="fetchApprovalList(2, 0, userId.value)">
              <ApprovalList :approvalList="state.approvalList"/>
            </b-tab>
            <b-tab title="단축 근무" @click="fetchApprovalList(3, 0, userId.value)">
              <ApprovalList :approvalList="state.approvalList"/>
            </b-tab>
            <b-tab title="휴가" @click="fetchApprovalList(4, 0, userId.value)">
              <ApprovalList :approvalList="state.approvalList"/>
            </b-tab>
          </b-tabs>
        </b-card>
      </div>
    </div>
    <div v-else>
      <b-card title="내가 작성한 문서">
        <br>
        <ApprovalList :approvalList="state.sApprovalList" :short="1"/>
      </b-card>
      <br>
      <b-card title="결재 필요 문서">
        <div @click="navigateTo('/bt')">상세</div>
        <br>
        <ApprovalList :approvalList="state.sReqApprovalList" :short="1"/>
      </b-card>
    </div>
  </div>
</template>

<style scoped>
</style>