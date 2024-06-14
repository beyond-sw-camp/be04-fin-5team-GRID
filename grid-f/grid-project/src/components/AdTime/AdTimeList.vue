<template>
  <div class="adTimeListContainer">
    <div class="adTimeListTitle">
      <h1 class="mb-1"><i class="bi bi-person-add fs-3"></i>&nbsp; 출퇴근 조회 &nbsp;&nbsp;<i class="bi bi-info-circle fs-5 mt-5" id="popover-target-1" style="color: darkgoldenrod; cursor: pointer;" @click="showModal('guideAdTime')"></i></h1>
    </div>
    <div class="workerContainer" v-if="userRole === 'ROLE_ADMIN'">
      <div class="card-deck cnt">
        <div class="card cntI">
          <div class="card-body">
            <h3 class="card-title">오늘 지각 인원</h3>
            <p class="card-text">{{ cntLate ? cntLate : 0 }}</p>
          </div>
        </div>
        <div class="card cntI">
          <div class="card-body">
            <h3 class="card-title">오늘 출장 인원</h3>
            <p class="card-text">{{ cntBt ? cntBt : 0 }}</p>
          </div>
        </div>
        <div class="card cntI">
          <div class="card-body">
            <h3 class="card-title">오늘 휴가 인원</h3>
            <p class="card-text">{{ cntVacation ? cntVacation : 0 }}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="adTableContainer">
      <table class="table table-hover">
        <thead>
        <tr>
          <th>No</th>
          <th>사번</th>
          <th>직원명</th>
          <th>출근 시간</th>
          <th>퇴근 시간</th>
          <th>출근 상태</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in paginatedAdTimes" :key="item.id">
          <td>{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
          <td>{{ item.employeeNumber}}</td>
          <td>{{ item.employeeName }}</td>
          <td>{{ item.startTime }}</td>
          <td>{{ item.endTime }}</td>
          <td>
            <b-badge variant="success" v-if="item.attendanceStatus === '정시 출근'">{{ item.attendanceStatus }}</b-badge>
            <b-badge variant="danger" v-else>{{ item.attendanceStatus }}</b-badge>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- 관리자용 가이드 모달 -->
    <div class="modal fade" id="guideAdTime" tabindex="-1" aria-labelledby="guideManageLabel" aria-hidden="true"
         v-if="userRole === 'ROLE_ADMIN'">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title" id="guideManageLabel">출퇴근 조회 가이드</h4>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div class="example-content">
              <p>전체 직원 출퇴근 목록을 볼 수 있는 페이지 입니다. </p>
              <hr>
              <p>1. 전체 직원의 출근, 퇴근 시간을 볼 있습니다.</p>
              <p>2. 출근 상태로 직원의 출결을 확인 할 수 있습니다.</p>
              <p>2-1. 오전 9시 이전 출근 시 정시 출근, 이후엔 지각으로 표시됩니다.</p>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 직원용 가이드 모달 -->
    <div class="modal fade" id="guideAdTime" tabindex="-1" aria-labelledby="guideManageLabel" aria-hidden="true"
         v-if="userRole === 'ROLE_USER'">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title" id="guideManageLabel">출퇴근 조회 가이드</h4>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div class="example-content">
              <p>사용자의 출퇴근 목록을 볼 수 있는 페이지 입니다. </p>
              <hr>
              <p>1. 본인의 출근, 퇴근 시간을 볼 수 있습니다.</p>
              <p>2. 출근 상태로 출결을 확인 할 수 있습니다.</p>
              <p>2-1. 오전 9시 이전 출근 시 정시 출근, 이후엔 지각으로 표시됩니다.</p>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>
    <nav class="pg" aria-label="Page navigation example">
      <ul class="pagination">
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <a class="page-link" href="#" aria-label="First" @click.prevent="goToFirstPage">
            <span aria-hidden="true">&laquo;&laquo;</span>
          </a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <a class="page-link" href="#" aria-label="Previous" @click.prevent="prevPage">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li v-for="page in filteredPages" :key="page" class="page-item" :class="{ active: page === currentPage }">
          <a class="page-link" @click.prevent="goToPage(page)">{{ page }}</a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <a class="page-link" aria-label="Next" @click.prevent="nextPage">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <a class="page-link" href="#" aria-label="Last" @click.prevent="goToLastPage">
            <span aria-hidden="true">&raquo;&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';

const router = useRouter();

const adTimeList = ref([]);
const todayList = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;

const userRole = ref('');
const userId = ref('');


const cntLate = ref();
const cntBt = ref();
const cntVacation = ref();

// 유저 확인
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

const showModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.show();
};

// 현재 시간
function getCurrentDateTimeString() {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// 관리자용
const fetchAllAdTime = async () => {
  try {
    const response = await axios.get('/api/ad-time/all')
    adTimeList.value = response.data.adTimeDTOList;

    // 오늘 지각 인원 조회
    const currentTime = getCurrentDateTimeString();
    const today = currentTime.slice(0, 10)

    const responseTodayAd = await axios.get(`/api/ad-time/date/${today}`)
    todayList.value = responseTodayAd.data.adTimeDTOList;

    const lateAttendanceList = todayList.value.filter(item => item.attendanceStatus === "지각");
    cntLate.value = lateAttendanceList.length;

    //오늘 출장 인원 조회
    const responseTodayBt = await axios.get(`/api/approval/today/bt`)
    cntBt.value = responseTodayBt.data.approvalEmpResultList.length;


    //오늘 휴가 인원 조회
    const responseTodayVacation = await axios.get(`/api/approval/today/v`)
    cntVacation.value = responseTodayVacation.data.approvalEmpResultList.length;

  } catch (error) {
    console.error('에러 발생:', error);
  }
};

//직원용
const fetchEmployeeAdTime = async () => {
  try{
    const response = await axios.get(`/api/ad-time/${userId.value}`);

    adTimeList.value = response.data.adTimeDTOList;

  } catch (error) {
    console.error('에러 발생:', error);
  }
};


onMounted(() => {
  // 유저 확인
  const token = localStorage.getItem('access');
  if (token) {
    const decodedToken = parseJwt(token);
    userRole.value = decodedToken?.auth || '';
    userId.value = decodedToken?.id || '';
  }

  if (userRole.value === 'ROLE_ADMIN') {
    fetchAllAdTime();
  } else if (userRole.value === 'ROLE_USER') {
    fetchEmployeeAdTime();
  }
});

const paginatedAdTimes = computed(() => {
  if (!adTimeList.value || adTimeList.value.length === 0) {
    return [];
  }

  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return adTimeList.value.slice(start, end);
});

const totalPages = computed(() => {
  if (!adTimeList.value || adTimeList.value.length === 0) {
    return 1;
  }

  return Math.ceil(adTimeList.value.length / itemsPerPage);
});

const filteredPages = computed(() => {
  const maxPages = 5; // 페이지당 최대 표시할 페이지 수
  const startPage = Math.max(1, currentPage.value - Math.floor(maxPages / 2));
  const endPage = Math.min(totalPages.value, startPage + maxPages - 1);

  const pages = [];
  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }
  return pages;
});

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};

const goToPage = (page) => {
  currentPage.value = page;
};

// 처음 페이지로 이동
const goToFirstPage = () => {
  currentPage.value = 1;
};

// 마지막 페이지로 이동
const goToLastPage = () => {
  currentPage.value = totalPages.value;
};

</script>

<style scoped>
.adTimeListContainer {
  display: grid;
  grid-template-rows: 18% 10% auto 5% 13%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.workerContainer {
  grid-row-start: 2;
  grid-column-start: 2;
}

.adTimeListTitle {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
  margin-left: -0.5%;
  color: #000000;
  display: grid;
  grid-template-columns: 16% 4%;
  align-items: center;
  min-width: 1300px;
}

.adTimeListTitle h1 {
  margin-left: 0.5%;
  margin-bottom: 0;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.adTimeIcon {
  width: 80%;
}

.guide {
  width: 60%;
  height: 25px;
  grid-column: 2;
  margin: 0;
  cursor: pointer;
}

.cnt {
  width: 45%;
}

.cnt h3 {
  font-size: 15px;
}

.cntI {
  align-items: center;
  background-color: #F2F2F2;
}

.workerContainer p {
  text-align: center;
}

.search {
  grid-row-start: 2;
  grid-column-start: 2;
  display: grid;
  grid-template-columns: 80% 15% 1% 4%;
}

.sortBox {
  grid-column-start: 2;
  margin-left: 2%;
  padding: 5px 5px;
  border-radius: 4px;
  font-size: 12px;
}

.printBtn {
  grid-column-start: 4;
  margin-left: 2%;
  width: 100%;
  background-color: #088A85;
  color: white;
  padding: 5px 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.adTableContainer {
  grid-row-start: 3;
  grid-column-start: 2;
  grid-column-end: 3;
  margin-top: 20px;
  font-size: 12px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  //border: 1px solid #dddddd;
  text-align: left;
  padding: 6px;
  vertical-align: middle;
}

th {
  //background-color: #f2f2f2;
}


.pg {
  grid-row-start: 4;
  grid-column-start: 2;
  grid-column-end: 3;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
}

.pagination .page-item.active .page-link {
  background-color: #088A85; /* 원하는 배경색 */
  border-color: #088A85; /* 원하는 테두리 색 */
  color: white; /* 원하는 텍스트 색 */
}

.pagination .page-item .page-link {
  color: #088A85; /* 기본 텍스트 색 */
}

.pagination .page-item.disabled .page-link {
  color: #088A85; /* 비활성화된 페이지 색 */
}
</style>