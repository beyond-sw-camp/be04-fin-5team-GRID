<template>
  <div class="adTimeListContainer">
    <div class="adTimeListTitle">
      <img class="adTimeIcon" src="@/assets/icons/goal_icon.png">
      <h1>출퇴근 조회</h1>
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
      <table class="table">
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
          <td>{{ item.attendanceStatus }}</td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
      <button v-for="page in totalPages" :key="page" @click="goToPage(page)"
              :class="{ active: page === currentPage }">{{ page }}</button>
      <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
    </div>
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
    const response = await axios.get('http://localhost:8080/ad-time/all')
    adTimeList.value = response.data.adTimeDTOList;

    // 오늘 지각 인원 조회
    const currentTime = getCurrentDateTimeString();
    const today = currentTime.slice(0, 10)

    const responseTodayAd = await axios.get(`http://localhost:8080/ad-time/date/${today}`)
    todayList.value = responseTodayAd.data.adTimeDTOList;

    const lateAttendanceList = todayList.value.filter(item => item.attendanceStatus === "지각");
    cntLate.value = lateAttendanceList.length;

    //오늘 출장 인원 조회
    const responseTodayBt = await axios.get(`http://localhost:8080/approval/today/bt`)
    cntBt.value = responseTodayBt.data.approvalEmpResultList.length;


    //오늘 휴가 인원 조회
    const responseTodayVacation = await axios.get(`http://localhost:8080/approval/today/v`)
    cntVacation.value = responseTodayVacation.data.approvalEmpResultList.length;

  } catch (error) {
    console.error('에러 발생:', error);
  }
};

//직원용
const fetchEmployeeAdTime = async () => {
  try{
    const response = await axios.get(`http://localhost:8080/ad-time/${userId.value}`);
    console.log(response.data.adTimeDTOList);
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
  grid-column-end: 3;
  font-size: 12px;
  font-weight: 0;
  margin-top: 2%;
  color: #000000;
  display: grid;
  grid-template-columns: 3% 97%;
  align-items: center;
}

.adTimeListTitle h1 {
  margin-left: 0.5%;
  font-weight: 600;
  font-size: 25px;
}

.adTimeIcon {
  width: 80%;
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

.pagination {
  grid-row-start: 6;
  grid-column-start: 2;
  grid-column-end: 3;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
}

.pagination button {
  background-color: white;
  color: black;
  padding: 5px 10px;
  border: 1px solid #dddddd;
  border-radius: 4px;
  cursor: pointer;
  margin: 0 5px;
}

.pagination button.active {
  background-color: darkorange;
  font-weight: bold;
  color: white;
}

.pagination button:disabled {
  background-color: #dddddd;
  cursor: not-allowed;
}

.pagination span {
  display: flex;
  align-items: center;
}
</style>