<template>
  <div class="container">
    <div class="header">
      <div class="header-title">
        <h2>팀 정보 > {{ teamName }}</h2>
      </div>
    </div>

    <div class="search">
      <input type="text" class="searchBox" placeholder="검색" v-model="searchQuery" @input="filterBySearch" />
    </div>

    <table class="teamTable">
      <thead>
        <tr>
          <th>이름</th>
          <th>사원 번호</th>
          <th>이메일</th>
          <th>업무용 번호</th>
          <th>직급</th>
          <th>직책</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="employee in paginatedEmployees" :key="employee.employeeId">
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
import { useRoute } from 'vue-router';

const route = useRoute();
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
    const response = await axios.get(`http://localhost:8080/users/team-list/${teamId}`);
    const employeeIds = response.data.result.map(emp => emp.id);
    
    // 모든 직원의 상세 정보를 가져옴
    const employeeDetailsPromises = employeeIds.map(id => axios.get(`http://localhost:8080/users/id/${id}`));
    const employeeDetailsResponses = await Promise.all(employeeDetailsPromises);
    
    // 상세 정보를 employees에 저장
    employees.value = employeeDetailsResponses.map(response => response.data.result);
    filteredEmployees.value = employees.value;
  } catch (error) {
    console.error('직원 데이터를 가져오는 중 오류 발생:', error);
  }
};

const getTeamName = async (teamId) => {
  try {
    const response = await axios.get(`http://localhost:8080/team/${teamId}`);
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

const filterBySearch = () => {
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
</script>

<style scoped>
@font-face {
  font-family: 'IBMPlexSansKR-Regular';
  src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

.container {
  display: grid;
  grid-template-columns: 10% 80% 10%;
  grid-template-rows: 10% 3% 2% 65% 2% 5% 5%;
  width: 80%;
  font-family: 'IBMPlexSansKR-Regular';
}

.search {
  grid-row-start: 2;
  grid-column-start: 2;
  display: grid;
  grid-template-columns: 74% 5% 1% 15% 1% 4%;
  font-size: 12px;
}

.searchBox {
    grid-column-start: 4;
    margin-left: 2%;
    padding: 5px 5px;
    border-radius: 4px;
    font-size: 12px;
    font-style: bold;
}

.teamTable {
  grid-column-start: 2;
  grid-row-start: 4;
}

.header {
  grid-column-start: 2;
  display: grid;
  align-items: center;
  margin-bottom: 15px;
}

.header-title {
  display: grid; 
  grid-template-columns: 1fr;
}

.header-title h2 {
  font-size: 25px;
  font-weight: 600;
  margin-left: 0.5%;
  margin-bottom: 0;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
  table-layout: fixed;
}

th, td {
  padding: 6px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  word-wrap: break-word;
}

th {
  background-color: #f8f8f8;
}

td {
  font-size: 14px;
}

tr:hover {
  background-color: #f1f1f1;
}

.pagination {
  grid-row-start: 6;
  grid-column-start: 2;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
  margin-top: 20px;
}

.pagination button {
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 5px 10px;
  cursor: pointer;
  font-size: 14px;
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
</style>
