<template>
  <div class="allWork">
    <div class="allWorkTitle">
      <h1 class="mb-1"><i class="bi bi-clipboard-check"></i>&nbsp; 근무 목록</h1>
    </div>
    <div class="cards">
      <div v-if="isLoading">로딩 중</div>
      <div v-else>
        <div v-if="userRole !== 'ROLE_ADMIN'">
          <div v-if="state.rwApprovalList.length > 0">
            <div class="text-center mb-5"> <!-- 가운데 정렬 스타일 -->
              <h5 class="text-primary fw-bolder" style="font-family: 'IBMPlexSansKR-Regular', sans-serif;"> <!-- 타이틀 스타일 -->
                <span><i class="bi bi-heart-pulse"></i>&nbsp; 단축 근무 기간 &nbsp;</span>
                <span class="text-secondary">( {{ state.startTime }} ~ {{ state.endTime }} )</span>
              </h5>
            </div>
          </div>
        </div>
        <b-card class="cardTitle shadow">
          <h1>출장</h1>
          <div class="text-end">
            <h6 class="text-muted" style="margin-bottom: 10px; margin-top: -30px;" @click="navigateTo('/bt')">상세 <i
                class="bi bi-chevron-right"></i></h6>
          </div>
          <br>
          <ApprovalList :approvalList="state.btApprovalList" :short="1"/>
        </b-card>
        <b-card class="cardTitle shadow">
          <h1>시간 외 근무</h1>
          <div class="text-end">
            <h6 class="text-muted" style="margin-bottom: 10px; margin-top: -30px;" @click="navigateTo('/overtime')">상세
              <i class="bi bi-chevron-right"></i></h6>
          </div>
          <br>
          <ApprovalList :approvalList="state.oApprovalList" :short="1"/>
        </b-card>

        <!-- 관리자 -->
        <div v-if="userRole === 'ROLE_ADMIN'">
          <b-card class="cardTitle shadow">
            <h1>단축 근무</h1>
            <div class="text-end">
              <h6 class="text-muted" style="margin-bottom: 10px; margin-top: -30px;" @click="navigateTo('/rw')">상세 <i
                  class="bi bi-chevron-right"></i></h6>
            </div>
            <br>
            <ApprovalList :approvalList="state.rwApprovalList" :short="1"/>
          </b-card>
        </div>
      </div>
    </div>
  </div>

</template>

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
  rwApprovalList: [],

  startTime: '',
  endTime: ''
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

const fetchApprovalList = async (typeId, approvalStatus, employeeId) => {

  try {
    let url = `/api/approval/all/${typeId}/${approvalStatus}`;

    if (userRole.value !== 'ROLE_ADMIN') {
      url = `/api/approval/list/${typeId}/${approvalStatus}/${employeeId}`;
    }

    const response = await axios.get(url);

    if (response === null || response.status !== 200) {
      throw new Error("response is not ok");
    }

    switch (typeId) {
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

onMounted(async () => {
  const token = localStorage.getItem('access');

  if (token) {
    const decodedToken = parseJwt(token);

    userRole.value = decodedToken.auth || '';
    userId.value = decodedToken.id || '';
  }

  // 버튼 클릭에 따라 다르게 실행

  // 0: 전체, 1: 승인, 2: 반려, 3: 대기
  await fetchApprovalList(1, 5, userId.value);
  await fetchApprovalList(2, 5, userId.value);
  await fetchApprovalList(3, 5, userId.value);

  if (state.approvalList.length > 0) {
    state.length = state.approvalList.length - 1;
    state.startTime = state.approvalList[state.length]['startTime'];
    state.endTime = state.approvalList[state.length]['endTime'];
  }

  isLoading.value = false;
})
</script>

<style scoped>
.allWork {
  display: grid;
  grid-template-rows: 18% 74% 8%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.allWorkTitle {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
  margin-left: -0.5%;
  color: #000000;
}

.cards {
  grid-column-start: 2;
  grid-row-start: 2;
}

.allWorkTitle h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.cardTitle {
  font-size: 12px;
  margin-bottom: 3%;
  font-weight: 300;
}

.card-title {
  font-size: 10px;
}

.cardTitle h1 {
  margin-left: 0.5%;
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.cardTitle h6 {
  font-size: 12px;
  font-weight: 300;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.text-muted {
  cursor: pointer;
}
</style>