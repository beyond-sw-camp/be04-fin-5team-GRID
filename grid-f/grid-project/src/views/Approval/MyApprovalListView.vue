<template>
  <div class="myAll">
    <div class="myHeader">
      <nav style="--bs-breadcrumb-divider: '>'; margin-top: -35px; margin-bottom: -7px;" aria-label="breadcrumb">
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="http://www.gridhr.site/approval" style="text-decoration: none; color: grey; font-size: 17px;"><i class="bi bi-collection"></i>&nbsp; 결재 목록</a></li>
          <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i class="bi bi-briefcase"></i>&nbsp; 내가 작성한 문서</span></li>
        </ol>
      </nav>
      <h1 class="fw-bolder "><i class="bi bi-briefcase"></i>&nbsp; 내가 작성한 문서</h1>
    </div>
    <div class="myContent">
      <b-card no-body>
        <b-tabs card>
          <b-tab title="출장" @click="setTab(1)" active>
            <b-form-group v-slot="{ ariaDescribedby }" class="d-flex justify-content-end my-3 pb-3">
              <b-form-radio-group
                  id="btn-radios-1"
                  v-model="filterStatus"
                  :options="statusOptions"
                  :aria-describedby="ariaDescribedby"
                  button-variant="outline-secondary"
                  name="radios-btn-default"
                  buttons
              ></b-form-radio-group>
            </b-form-group>
            <b-card-text><ApprovalList :approvalList="filteredItems"/></b-card-text>
          </b-tab>
          <b-tab title="시간 외 근무" @click="setTab(2)">
            <b-form-group v-slot="{ ariaDescribedby }" class="d-flex justify-content-end my-3 pb-3">
              <b-form-radio-group
                  id="btn-radios-2"
                  v-model="filterStatus"
                  :options="statusOptions"
                  :aria-describedby="ariaDescribedby"
                  button-variant="outline-secondary"
                  name="radios-btn-default"
                  buttons
              ></b-form-radio-group>
            </b-form-group>
            <ApprovalList :approvalList="filteredItems"/>
          </b-tab>
          <b-tab title="단축 근무" @click="setTab(3)">
            <b-form-group v-slot="{ ariaDescribedby }" class="d-flex justify-content-end my-3 pb-3">
              <b-form-radio-group
                  id="btn-radios-3"
                  v-model="filterStatus"
                  :options="statusOptions"
                  :aria-describedby="ariaDescribedby"
                  button-variant="outline-secondary"
                  name="radios-btn-default"
                  buttons
              ></b-form-radio-group>
            </b-form-group>
            <ApprovalList :approvalList="filteredItems"/>
          </b-tab>
          <b-tab title="휴가" @click="setTab(4)">
            <b-form-group v-slot="{ ariaDescribedby }" class="d-flex justify-content-end my-3 pb-3">
              <b-form-radio-group
                  id="btn-radios-4"
                  v-model="filterStatus"
                  :options="statusOptions"
                  :aria-describedby="ariaDescribedby"
                  button-variant="outline-secondary"
                  name="radios-btn-default"
                  buttons
              ></b-form-radio-group>
            </b-form-group>
            <ApprovalList :approvalList="filteredItems"/>
          </b-tab>
        </b-tabs>
      </b-card>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from "vue";
  import axios from "axios";
  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const userId = ref();

  const isLoading = ref(true);

  const currentTab = ref(1);
  const filterStatus = ref(null);

  const state = reactive({
    approvalList: []
  })

  const statusOptions = [
    { value: null, text: '전체' },
    { value: 'A', text: '승인' },
    { value: 'D', text: '반려' },
    { value: 'V', text: '열람' },
    { value: 'N', text: '미열람/취소' }
  ];

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
      const response = await axios.get(`/api/approval/list/${typeId}/${approvalStatus}/${employeeId}`);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.approvalList = response.data.approvalEmpResultList;
      state.approvalList.type = typeId;

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

let items;

const filteredItems = computed(() => {
  if (!filterStatus.value) {
    items =  state.approvalList;
  } else {

    items= state.approvalList.filter(item => item.approvalStatus === filterStatus.value);
  }

  items.type = state.approvalList.type;
  return items;
});

  const setTab = async (tabIndex) => {
    currentTab.value = tabIndex;
    filterStatus.value = null; // 필터 상태 초기화
    await fetchApprovalList(currentTab.value, 0, userId.value); // 해당 탭의 데이터를 불러오기
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
  margin-left: -0.5%;
  color: #000000;
}

.myHeader h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.myContent {
  grid-column-start: 2;
  grid-row-start: 2;
}
</style>