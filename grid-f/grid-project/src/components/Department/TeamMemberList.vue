<template>
  <div class="container">
    <div class="header">
      <div class="header-title">
        <nav style="--bs-breadcrumb-divider: '>'; margin-top: -7px; margin-bottom: -7px;" aria-label="breadcrumb">
          <ol class="breadcrumb">
            <li class="breadcrumb-item">
              <a href="#" @click.prevent="goBack" style="text-decoration: none; color: grey; font-size: 17px;">
                <i class="bi bi-person"></i>&nbsp; 팀 정보
              </a>
            </li>
            <li class="breadcrumb-item active" aria-current="page">
              <span class="fw-bolder">
                <i class="bi bi-briefcase"></i>&nbsp; {{ teamName }}
              </span>
            </li>
          </ol>
        </nav>
        <div><h1 class="fw-bolder"><i class="bi bi-person"></i>&nbsp; 팀원 정보</h1></div>
      </div>
    </div>

    <div class="search-and-add">
      <div class="search-group">
        <input type="text" class="searchBox" placeholder="이름 검색" v-model="searchQuery" />
        <button @click="search" class="searchBtn">검색</button>
      </div>
    </div>

    <table class="teamTable">
      <thead>
        <tr>
          <th style="width: 10%;">이름</th>
          <th style="width: 10%;">사원 번호</th>
          <th style="width: 20%;">이메일</th>
          <th style="width: 20%;">업무용 번호</th>
          <th style="width: 10%;">직급</th>
          <th style="width: 10%;">직책</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="employee in paginatedEmployees" :key="employee.duties.id">
          <td>{{ employee.name }}</td>
          <td>{{ employee.employeeNumber }}</td>
          <td>{{ employee.email }}</td>
          <td>{{ employee.phoneNumber }}</td>
          <td>{{ employee.duties.dutiesName }}</td>
          <td>{{ employee.position.positionName }}</td>
        </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">❮</button>
      <button
        v-for="page in totalPages"
        :key="page"
        @click="goToPage(page)"
        :class="{ active: page === currentPage }"
      >
        {{ page }}
      </button>
      <button @click="nextPage" :disabled="currentPage === totalPages">❯</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();
const teamId = ref(route.params.teamId); // 라우트에서 teamId를 가져옴

const teamName = ref('');
const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = 5;
const employees = ref([]);
const filteredEmployees = ref([]);
const employeeDetails = ref([]); // 직원 상세 정보 저장용
const teams = ref([]); // API로부터 가져올 팀 목록

const fetchEmployees = async (teamId) => {
  try {
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/team-list/${teamId}`);
    const employeeIds = response.data.result.map(emp => emp.id);
    
    // 모든 직원의 상세 정보를 가져옴
    const employeeDetailsPromises = employeeIds.map(id => axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/id/${id}`));
    const employeeDetailsResponses = await Promise.all(employeeDetailsPromises);
    
    // 상세 정보를 employees에 저장하고 duties.id를 기준으로 정렬
    employees.value = employeeDetailsResponses.map(response => response.data.result);
    employees.value.sort((a, b) => a.duties.id - b.duties.id); // duties.id를 기준으로 정렬
    filteredEmployees.value = employees.value;
  } catch (error) {
    console.error('직원 데이터를 가져오는 중 오류 발생:', error);
  }
};

const getTeamName = async (teamId) => {
  try {
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/team/${teamId}`);
    teamName.value = response.data.result.teamName;
  } catch (error) {
    console.error('팀 이름을 가져오는 중 오류 발생:', error);
  }
};

onMounted(() => {
  fetchEmployees(teamId.value);
  getTeamName(teamId.value);
});

watch(
  () => route.params.teamId,
  (newId) => {
    teamId.value = newId;
    fetchEmployees(newId);
    getTeamName(newId);
  },
  { immediate: true }
);

const search = () => {
  filteredEmployees.value = employees.value.filter(emp =>
    emp.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
  currentPage.value = 1;
};

const paginatedEmployees = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredEmployees.value.slice(start, end);
});

const totalPages = computed(() => {
  return Math.ceil(filteredEmployees.value.length / itemsPerPage);
});

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value -= 1;
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value += 1;
  }
};

const goToPage = (page) => {
  currentPage.value = page;
};

const goBack = () => {
  router.go(-1);
};
</script>

<style scoped>
@font-face {
  font-family: 'IBMPlexSansKR-Regular';
  src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

.container {
  display: flex;
  flex-direction: column;
  padding: 0 10%;
  margin-top: 5%;
  font-family: 'IBMPlexSansKR-Regular';
}

.header {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 20px;
}

.header-title {
  display: flex;
  flex-direction: column;
}

.header-title h2 {
  font-size: 25px;
  font-weight: 600;
  margin-left: 10px;
}

.search-and-add {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.search-group {
  display: flex;
  align-items: center;
  flex-grow: 1;
  justify-content: right;
}

.searchBox {
  padding: 5px;
  font-size: 14px;
  border-radius: 4px;
  border: 1px solid #ddd;
  width: 200px;
  max-width: 200px;
}

.searchBtn {
  background-color: #088A85;
  color: white;
  padding: 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-left: 10px;
}

.searchBtn:hover {
  background-color: #065f5b;
}

/* 테이블 스타일 */
.teamTable {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
  table-layout: fixed;
}

/* 테이블 헤더와 셀 스타일 */
th, td {
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

/* 테이블 헤더 스타일 */
th {
  background-color: white;
  color: #333;
}

/* 테이블 데이터 셀 스타일 */
td {
  font-size: 14px;
}

/* 테이블 행 호버 스타일 */
tr:hover {
  background-color: #f1f1f1;
}

/* 페이지네이션 스타일 */
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination button {
  margin: 0 5px;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: white;
  cursor: pointer;
}

.pagination button.active {
  background-color: #088A85;
  color: white;
  border: none;
}

.pagination button:disabled {
  background-color: #ddd;
  cursor: not-allowed;
}

.pagination button:hover:not(.active):not(:disabled) {
  background-color: #f1f1f1;
}
h1 {
  font-size: 25px;
}
</style>
