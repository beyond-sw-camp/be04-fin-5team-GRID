<template>
  <div class="container">
    <div class="header">
      <div class="header-title">
        <h1>팀 정보</h1>
      </div>
    </div>

    <div class="search">
      <div class="search-group">
        <input type="text" class="searchBox" v-model="searchQuery" placeholder="이름 검색" />
        <button @click="search" class="searchBtn">검색</button>
      </div>
      <button @click="showModal('addNewTeamModal')" class="addTeamBtn">팀 추가</button>
    </div>

    <table class="teamTable">
      <thead>
        <tr>
          <th>팀명</th>
          <th>인원</th>
          <th>시작 일</th>
          <th>종료일</th>
          <th>상위 부서명</th>
          <th>책임자 명</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="team in filteredTeams" :key="team.id">
          <td>{{ team.teamName }}</td>
          <td>{{ team.memberCnt }}</td>
          <td>{{ formatDate(team.startTime) }}</td>
          <td>{{ formatDate(team.endTime) }}</td>
          <td>{{ team.departmentName }}</td>
          <td>{{ team.leaderName }}</td>
          <td>
            <button class="view-details-btn" @click="goToTeamMembers(team.id)">자세히 보기</button>
          </td>
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

    <!-- Add Team Modal -->
    <div class="modal fade" id="addNewTeamModal" tabindex="-1" aria-labelledby="addNewTeamModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="addNewTeamModalLabel">팀 추가</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="addNewTeam">
              <div class="mb-3">
                <label for="teamName" class="form-label">팀명</label>
                <input type="text" class="form-control" id="teamName" v-model="newTeam.teamName" required>
              </div>
              <div class="mb-3">
                <label for="departmentName" class="form-label">상위 부서명</label>
                <div class="input-group">
                  <input type="text" class="form-control" id="departmentName" v-model="newTeam.departmentName" readonly>
                  <button type="button" class="btn btn-secondary" @click="showModal('selectDepartmentModal')">조회</button>
                </div>
              </div>
              <div class="mb-3">
                <label for="leaderName" class="form-label">책임자 명</label>
                <div class="input-group">
                  <input type="text" class="form-control" id="leaderName" v-model="newTeam.leaderName" readonly>
                  <button type="button" class="btn btn-secondary" @click="showModal('selectLeaderModal')">조회</button>
                </div>
              </div>
              <button type="submit" class="btn btn-primary">추가</button>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- 부서 선택 Modal -->
    <div class="modal fade" id="selectDepartmentModal" tabindex="-1" aria-labelledby="selectDepartmentModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="selectDepartmentModalLabel">부서 선택</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <table class="table">
              <thead>
                <tr>
                  <th>부서명</th>
                  <th>선택</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="department in departments" :key="department.id">
                  <td>{{ department.departmentName }}</td>
                  <td><button type="button" class="btn btn-primary" @click="selectDepartment(department)">선택</button></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- 책임자 선택 Modal -->
    <div class="modal fade" id="selectLeaderModal" tabindex="-1" aria-labelledby="selectLeaderModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="selectLeaderModalLabel">책임자 선택</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <table class="table">
              <thead>
                <tr>
                  <th>책임자 명</th>
                  <th>선택</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="leader in leaders" :key="leader.id">
                  <td>{{ leader.name }}</td>
                  <td><button type="button" class="btn btn-primary" @click="selectLeader(leader)">선택</button></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

const route = useRoute();
const departmentId = ref(route.params.id);

const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = 5;
const teams = ref([]);
const allTeams = ref([]); // 전체 팀 정보를 저장할 변수
const departments = ref([]);
const leaders = ref([]);
const newTeam = ref({
  teamName: '',
  departmentId: null,
  departmentName: '',
  leaderId: null,
  leaderName: ''
});

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

const fetchDepartments = async () => {
  try {
    const response = await axios.get('http://localhost:8080/department/find-all');
    departments.value = response.data.result;
  } catch (error) {
    console.error('부서 정보를 가져오는 중 오류 발생:', error);
  }
};

const fetchLeaders = async () => {
  try {
    const response = await axios.get('http://localhost:8080/users/list');
    leaders.value = response.data.result;
  } catch (error) {
    console.error('책임자 정보를 가져오는 중 오류 발생:', error);
  }
};

onMounted(() => {
  fetchTeams();
  fetchDepartments();
  fetchLeaders();
});

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

const formatDate = (dateString) => {
  if (!dateString) return '-';
  const date = new Date(dateString);
  return date.toISOString().split('T')[0];
};

const goToTeamMembers = (id) => {
  window.location.href = `/team/member-list/${id}`;
};

const showModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.show();
};

const addNewTeam = async () => {
  try {
    // 팀 추가 API 호출
    await axios.post('http://localhost:8080/team', {
      teamName: newTeam.value.teamName,
      departmentId: newTeam.value.departmentId,
      leaderId: newTeam.value.leaderId
    });

    // 팀 목록을 다시 불러오기
    await fetchTeams();

    // 모달 닫기 및 초기화
    const modal = bootstrap.Modal.getInstance(document.getElementById('addNewTeamModal'));
    modal.hide();
    newTeam.value = {
      teamName: '',
      departmentId: null,
      departmentName: '',
      leaderId: null,
      leaderName: ''
    };
  } catch (error) {
    console.error('팀을 추가하는 중 오류 발생:', error);
  }
};

const selectDepartment = (department) => {
  newTeam.value.departmentId = department.id;
  newTeam.value.departmentName = department.departmentName;
  const modal = bootstrap.Modal.getInstance(document.getElementById('selectDepartmentModal'));
  modal.hide();
};

const selectLeader = (leader) => {
  newTeam.value.leaderId = leader.id;
  newTeam.value.leaderName = leader.name;
  const modal = bootstrap.Modal.getInstance(document.getElementById('selectLeaderModal'));
  modal.hide();
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
  grid-template-rows: 18% 3% 2% 65% 2% 5% 5%;
  width: 80%;
  font-family: 'IBMPlexSansKR-Regular';
}

.search {
  grid-row-start: 2;
  grid-column-start: 2;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 12px;
}

.search-group {
  display: flex;
  align-items: center;
}

.searchBox {
  width: 200px;
  padding: 5px;
  border-radius: 4px;
  font-size: 12px;
  margin-right: 5px;
}

.searchBtn {
  background-color: #088A85;
  color: white;
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.addTeamBtn {
  background-color: #088A85;
  color: white;
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.searchBtn:hover,
.addTeamBtn:hover {
  background-color: #065f5b;
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

.header-title h1 {
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

.view-details-btn {
  padding: 5px 10px;
  background-color: #088A85;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.view-details-btn:hover {
  background-color: #065f5b;
}
</style>
