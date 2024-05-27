<template>
  <div class="container">
    <header class="header">
      <div class="logo">부서정보</div>
      <div class="title">영업</div>
    </header>
    <div class="toolbar">
      <input type="text" v-model="searchQuery" placeholder="Search..." class="mb-2" />
    </div>
    <div class="content">
      <table>
        <thead>
          <tr>
            <th>팀명</th>
            <th>인원</th>
            <th>시작 일</th>
            <th>종료일</th>
            <th>상위 부서명</th>
            <th>책임자 명</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="team in filteredTeams" :key="team.teamId">
            <td>{{ team.teamName }}</td>
            <td>{{ team.memberCnt }}</td>
            <td>{{ team.startTime }}</td>
            <td>{{ team.endTime }}</td>
            <td>{{ team.departmentId }}</td>
            <td>{{ team.leaderId }}</td>
          </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button @click="handlePageChange(currentPage - 1)" :disabled="currentPage === 1">Previous</button>
        <span>{{ currentPage }}</span>
        <button @click="handlePageChange(currentPage + 1)" :disabled="currentPage === totalPages">Next</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const searchQuery = ref('');
const teams = ref([]);
const currentPage = ref(1);
const itemsPerPage = ref(7);

const fetchTeams = async () => {
  try {
    const response = await axios.get('http://localhost:8080/team/find-all'); // 실제 API 엔드포인트로 변경하세요
    teams.value = response.data.result;
    console.log(teams.value);
  } catch (error) {
    console.error('팀 데이터를 가져오는 중 오류 발생:', error);
  }
};

onMounted(fetchTeams);

const filteredTeams = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return teams.value.filter(team => {
    return (
      (searchQuery.value === '' || team.teamName.toLowerCase().includes(searchQuery.value.toLowerCase()))
    );
  }).slice(start, end);
});

const handlePageChange = (newPage) => {
  if (newPage > 0 && newPage <= totalPages.value) {
    currentPage.value = newPage;
  }
};

const totalPages = computed(() => {
  return Math.ceil(
    teams.value.filter(team => {
      return (
        (searchQuery.value === '' || team.teamName.toLowerCase().includes(searchQuery.value.toLowerCase()))
      );
    }).length / itemsPerPage.value
  );
});
</script>

<style scoped>
.container {
  display: grid;
  grid-template-rows: auto auto 1fr auto;
  gap: 10px;
  padding: 10px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #f0f0f0;
}

.logo {
  font-size: 1.5em;
}

.title {
  font-size: 1.2em;
}

.toolbar {
  display: flex;
  gap: 10px;
}

.mb-2 {
  padding: 5px;
  border-radius: 4px;
  font-size: 12px;
  border: 1px solid #ccc;
}

.content {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

th, td {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 10px;
  vertical-align: middle;
}

th {
  background-color: #f2f2f2;
}

tr:hover {
  background-color: #f1f1f1;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}

.pagination button {
  background-color: white;
  color: black;
  padding: 5px 10px;
  border: 1px solid #dddddd;
  border-radius: 4px;
  cursor: pointer;
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
