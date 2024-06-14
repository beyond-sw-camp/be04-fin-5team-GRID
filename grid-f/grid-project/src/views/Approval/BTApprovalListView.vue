<template>
  <div class="btAll">
    <div class="btheader">
      <nav style="--bs-breadcrumb-divider: '>'; margin-top: -35px; margin-bottom: -7px;" aria-label="breadcrumb">
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="http://www.gridhr.site/work" style="text-decoration: none; color: grey; font-size: 17px;"><i class="bi bi-list-check"></i>&nbsp; 근무 목록</a></li>
          <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i class="bi bi-briefcase"></i>&nbsp; 출장</span></li>
        </ol>
      </nav>
      <h1 class="fw-bolder "><i class="bi bi-briefcase"></i>&nbsp; 출장</h1>
    </div>
    <div class="btContent">
      <ApprovalList :approvalList="state.approvalList"/>
    </div>
  </div>
</template>

<script setup>
  import {onMounted, reactive, ref} from "vue";
  import axios from "axios";

  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const userRole = ref('');
  const userId = ref();

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
      let url = `/api/approval/all/1/5`;

      if (userRole.value !== 'ROLE_ADMIN') {
        url = `/api/approval/list/1/5/${id}`;
      }

      const response = await axios.get(url);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.approvalList = response.data.approvalEmpResultList;
      state.approvalList.type = "bt";

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
  })
</script>

<style>
.btAll {
  display: grid;
  grid-template-rows: 18% 74% 8%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.btheader {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
  margin-left: -0.5%;
  color: #000000;
}

.btContent {
  grid-column-start: 2;
  grid-row-start: 2;
}

.btheader h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}
</style>