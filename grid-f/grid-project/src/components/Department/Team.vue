<template>
  <div>
    <div>
      <h1>팀 정보</h1>
    </div>

    <div>
      <input type="text" v-model="searchQuery" placeholder="이름 검색" />
      <button @click="search">검색</button>
    </div>

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
        <tr v-for="team in filteredTeams" :key="team.id">
          <td>
            <router-link :to="`/team/member-list/${team.id}`">{{ team.teamName }}</router-link>
          </td>
          <td>{{ team.memberCnt }}</td>
          <td>{{ team.startTime }}</td>
          <td>{{ team.endTime }}</td>
          <td>{{ team.departmentName }}</td>
          <td>{{ team.leaderName }}</td>
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
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const departmentId = ref(route.params.id);

const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = 5;
const teams = ref([]);
const allTeams = ref([]); // 전체 팀 정보를 저장할 변수

// API에서 팀 정보를 가져오는 함수
const fetchTeams = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/team/sub-department/${departmentId.value}`); 
    const teamData = await Promise.all(response.data.result.map(async team => {
      const leaderResponse = await axios.get(`http://localhost:8080/users/id/${team.leaderId}`);
      team.leaderName = leaderResponse.data.result.name;
      
      const departmentResponse = await axios.get(`http://localhost:8080/department/${team.departmentId}`);
      team.departmentName = departmentResponse.data.result.departmentName;
      
      return team;
    }));
    teams.value = teamData;
    allTeams.value = teamData;
  } catch (error) {
    console.error('팀 정보를 가져오는 중 오류 발생:', error);
  }
};

onMounted(fetchTeams);

const filteredTeams = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return teams.value.slice(start, end);
});

const totalPages = computed(() => {
  return Math.ceil(teams.value.length / itemsPerPage);
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

const search = () => {
  teams.value = allTeams.value.filter(team => team.leaderName.includes(searchQuery.value));
  currentPage.value = 1;
};
</script>

<style scoped>
.pagination {
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
  background-color: #ff7f50;
  color: white;
}

.pagination button:disabled {
  background-color: #ddd;
  cursor: not-allowed;
}
</style>
