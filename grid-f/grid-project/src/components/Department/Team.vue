<template>
  <div class="container">
    <div class="header">
      <div class="header-title">
        <img class="reviewIcon" src="@/assets/list-check.png" alt="list-check" />
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
                <input type="text" class="form-control" id="departmentName" v-model="newTeam.departmentName" readonly>
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
import { ref, computed, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

const route = useRoute();
const departmentId = ref(route.params.id);
const departmentName = ref('');

const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = 5;
const teams = ref([]);
const allTeams = ref([]); // 전체 팀 정보를 저장할 변수
const departments = ref([]);
const leaders = ref([]);
const newTeam = ref({
  teamName: '',
  departmentId: departmentId.value,
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

const fetchDepartmentName = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/department/${departmentId.value}`);
    departmentName.value = response.data.result.departmentName;
    newTeam.value.departmentName = departmentName.value; // 부서명 설정
  } catch (error) {
    console.error('부서 정보를 가져오는 중 오류 발생:', error);
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

// departmentId가 변경될 때마다 부서명 업데이트
watch(departmentId, (newId) => {
  const department = departments.value.find(dep => dep.id === newId);
  if (department) {
    newTeam.value.departmentName = department.departmentName;
  }
});

onMounted(() => {
  fetchDepartmentName(); // 부서 이름 가져오기
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
  if (!newTeam.value.leaderId) {
    alert('책임자를 선택해야 합니다.');
    return;
  }
  try {
    await axios.post('http://localhost:8080/team', {
      teamName: newTeam.value.teamName,
      departmentId: newTeam.value.departmentId,
      leaderId: newTeam.value.leaderId
    });

    await fetchTeams();

    const modal = bootstrap.Modal.getInstance(document.getElementById('addNewTeamModal'));
    modal.hide();
    newTeam.value = {
      teamName: '',
      departmentId: departmentId.value,
      departmentName: departmentName.value, // 부서명 초기화
      leaderId: null,
      leaderName: ''
    };
  } catch (error) {
    console.error('팀을 추가하는 중 오류 발생:', error);
  }
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
  display: flex;
  flex-direction: column;
  padding: 0 10%;
  margin-top: 5%;
  font-family: 'IBMPlexSansKR-Regular';
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-title {
  display: flex;
  align-items: center;
}

.header-title h1 {
  font-size: 25px;
  font-weight: 600;
  margin-left: 10px;
}

.reviewIcon {
  width: 30px; /* 이미지 크기 유지 */
}

.addNewBtn {
  background-color: #088A85;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.addNewBtn:hover {
  background-color: #065f5b;
}

.search {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-group {
  display: flex;
  align-items: center;
}

.searchBox {
  padding: 10px;
  font-size: 14px;
  border-radius: 4px;
  border: 1px solid #ddd;
  width: 200px;
  max-width: 200px;
}

.searchBtn {
  background-color: #088A85;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-left: 10px;
}

.searchBtn:hover {
  background-color: #065f5b;
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

.addTeamBtn:hover {
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
  border-top: 1px solid #ddd;
  border-bottom: 1px solid #ddd;
}

/* 테이블 헤더 스타일 */
th {
  background-color: #f2f2f2;
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
