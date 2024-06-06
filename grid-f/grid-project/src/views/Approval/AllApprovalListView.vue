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
      let url = `https://api.gridhr.site/approval/all/${typeId}/${approvalStatus}`;
      if (userRole.value !== 'ROLE_ADMIN') {
        url = `https://api.gridhr.site/approval/list/${typeId}/${approvalStatus}/${employeeId}`;
      }
      const response = await axios.get(url);
      if (response === null || response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.approvalList = response.data.approvalEmpResultList;
      state.sApprovalList = response.data.approvalEmpResultList.slice(0, 5);

      state.approvalList.type = typeId;
      state.sApprovalList.type = typeId;

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  const fetchReqApprovalList = async (typeId, approvalStatus, approverId) => {
    try {
      const response = await axios.get(`https://api.gridhr.site/approval/approver/${typeId}/${approvalStatus}/${approverId}`);
      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.reqApprovalList = response.data.approvalEmpResultList;
      state.sReqApprovalList = response.data.approvalEmpResultList.slice(0, 5);

      state.reqApprovalList.type = typeId;
      state.sReqApprovalList.type = typeId;

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async () => {
    const token = localStorage.getItem('access');
    if (token) {
      const decodedToken = parseJwt(token);

      userId.value = decodedToken.id || '';
      userRole.value = decodedToken.auth || '';
    }

    if (userRole.value === 'ROLE_ADMIN') {
      await fetchApprovalList(1, 0, userId.value);
    } else {
      await fetchApprovalList(0, 0, userId.value);
      await fetchReqApprovalList(0, 0, userId.value);
    }

    isLoading.value = false;
  })
</script>

<template>
  <div class="approvalAll"> 
    <div class="approvalHeader">
      <div><h1 class="fw-bolder "><i class="bi bi-collection"></i>&nbsp; 결재 목록</h1></div>
        <div v-if="isLoading">
          로딩 중
        </div>
      <div v-else></div>
    </div>
    <div class="approvalContent">
        <div v-if="userRole === 'ROLE_ADMIN'">
          <!-- 관리자 -->
          <div>
            <b-card no-body>
              <b-tabs card>
                <b-tab title="출장" @click="fetchApprovalList(1, 0, userId)" active>
                  <b-card-text><ApprovalList :approvalList="state.approvalList"/></b-card-text>
                </b-tab>
                <b-tab title="시간 외 근무" @click="fetchApprovalList(2, 0, userId)">
                  <ApprovalList :approvalList="state.approvalList"/>
                </b-tab>
                <b-tab title="단축 근무" @click="fetchApprovalList(3, 0, userId)">
                  <ApprovalList :approvalList="state.approvalList"/>
                </b-tab>
                <b-tab title="휴가" @click="fetchApprovalList(4, 0, userId)">
                  <ApprovalList :approvalList="state.approvalList"/>
                </b-tab>
              </b-tabs>
            </b-card>
          </div>
        </div>
        <div v-else>
          <b-card title="내가 작성한 문서">
            <div class="text-end">
              <h6 class="text-muted" style="margin-bottom: 10px; margin-top: -30px;" @click="navigateTo('/my')">상세 <i class="bi bi-chevron-right"></i></h6>
            </div>
            <br>
            <ApprovalList :approvalList="state.sApprovalList" :short="1"/>
          </b-card>
          <br>
          <b-card title="나의 결재 문서">
            <div class="text-end">
              <h6 class="text-muted" style="margin-bottom: 10px; margin-top: -30px;" @click="navigateTo('/required')">상세 <i class="bi bi-chevron-right"></i></h6>
            </div>
            <br>
            <ApprovalList :approvalList="state.sReqApprovalList" :short="1"/>
          </b-card>
        </div>
      </div>
    </div>
</template>

<style scoped>
.approvalAll {
  display: grid;
  grid-template-rows: 18% 75% 7%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.approvalHeader {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
}

.approvalHeader h1 {
  margin-left: 0.5%;
  margin: 0;
  font-size: 25px;
  font-weight: 600;
}

.approvalContent {
  grid-column-start: 2;
  grid-row-start: 2;
}
</style>