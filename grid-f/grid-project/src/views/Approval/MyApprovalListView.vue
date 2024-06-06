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
      const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/approval/list/${typeId}/${approvalStatus}/${employeeId}`);

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
  <div class="myAll">
    <div class="myHeader">
      <nav style="--bs-breadcrumb-divider: '>'; margin-top: -35px; margin-bottom: -7px;" aria-label="breadcrumb">
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="http://localhost:5173/approval" style="text-decoration: none; color: grey; font-size: 17px;"><i class="bi bi-collection"></i>&nbsp; 결재 목록</a></li>
          <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i class="bi bi-briefcase"></i>&nbsp; 내가 작성한 문서</span></li>
        </ol>
      </nav>
      <h1 class="fw-bolder pb-5"><i class="bi bi-briefcase"></i>&nbsp; 내가 작성한 문서</h1>
    </div>
    <div class="myContent">
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
</template>

<style scoped>
.myAll {
  display: grid;
  grid-template-rows: 18% 75% 7%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.myHeader {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
}

.myHeader h1 {
  margin-left: 0.5%;
  margin: 0;
  font-size: 25px;
  font-weight: 600;
}

.myContent {
  grid-column-start: 2;
  grid-row-start: 2;
}
</style>