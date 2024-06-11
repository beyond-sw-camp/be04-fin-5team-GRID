<template>
  <div class="container">
    <div class="header">
      <div class="header-title">
        <nav style="--bs-breadcrumb-divider: '>'; margin-top: -35px; margin-bottom: -7px;" aria-label="breadcrumb">
          <ol class="breadcrumb">
            <li class="breadcrumb-item">
              <a href="#" @click.prevent="goBack" style="text-decoration: none; color: grey; font-size: 17px;">
                <i class="bi bi-person"></i>&nbsp; 팀 정보
              </a>
            </li>
            <li class="breadcrumb-item active" aria-current="page">
              <span class="fw-bolder">
                <i class="bi bi-briefcase"></i>&nbsp; {{ departmentName }}
              </span>
            </li>
          </ol>
        </nav>
        <div><h1 class="fw-bolder"><i class="bi bi-person"></i>&nbsp; 소속 팀 정보</h1></div>
      </div>
      <div class="buttons">
        <button @click="prepareUpdateLeaderModal" class="updateLeaderBtn btn-custom-1" v-if="userRole === 'ROLE_ADMIN'"><span>팀장 수정</span></button>
        <button @click="showModal('addNewTeamModal')" class="addTeamBtn btn-custom-1" v-if="userRole === 'ROLE_ADMIN'"><span>팀 추가</span></button>
        <button @click="toggleTeamStatus" class="toggleStatusBtn btn-custom-1" v-if="userRole === 'ROLE_ADMIN'"><span>활성/비활성화</span></button>
      </div>
    </div>

    <div class="search">
      <div class="search-group">
        <input type="text" class="searchBox" v-model="searchQuery" placeholder="팀명 검색" />
        <button @click="search" class="searchBtn">검색</button>
      </div>
    </div>

    <b-table hover :items="filteredTeams" :fields="fields" responsive>
      <template #cell(selected)="data">
        <input type="checkbox" :value="data.item" v-model="selectedTeams" @change="checkSelectionLimit" />
      </template>
      <template #cell(startTime)="data">
        {{ formatDate(data.item.startTime) }}
      </template>
      <template #cell(endTime)="data">
        {{ formatDate(data.item.endTime) }}
      </template>
      <template #cell(actions)="data">
        <button class="view-details-btn btn-custom-1" id="listBtn" @click="goToTeamMembers(data.item.id)"><span>팀원 목록</span></button>
      </template>
    </b-table>

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
                <input type="text" class="form-control" id="teamName" v-model="newTeam.teamName" required />
              </div>
              <div class="mb-3">
                <label for="departmentName" class="form-label">상위 부서</label>
                <input type="text" class="form-control" id="departmentName" v-model="newTeam.departmentName" readonly />
              </div>
              <div class="mb-3">
                <label for="leaderName" class="form-label">팀장</label>
                <div class="input-group">
                  <input type="text" class="form-control" id="leaderName" v-model="newTeam.leaderName" readonly />
                  <button type="button" class="btn btn-secondary" @click="showModal('selectLeaderModal')">조회</button>
                </div>
              </div>
              <div class="modal-footer">
                <div class="button-container">
                  <button type="submit" class="btn btn-primary">추가</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- 팀장 선택 Modal -->
    <div class="modal fade" id="selectLeaderModal" tabindex="-1" aria-labelledby="selectLeaderModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="selectLeaderModalLabel">팀장 선택</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <input type="text" class="form-control mb-3" placeholder="이름, 사원번호 또는 직책 검색" v-model="leaderSearchQuery" @input="searchLeaders">
            <table class="table">
              <thead>
                <tr>
                  <th>사원번호</th>
                  <th>팀장</th>
                  <th>직책</th>
                  <th>직급</th>
                  <th>선택</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="leader in filteredLeaders" :key="leader.id">
                  <td>{{ leader.employeeNumber }}</td>
                  <td>{{ leader.name }}</td>
                  <td>{{ leader.position.positionName }}</td>
                  <td>{{ leader.duties.dutiesName }}</td>
                  <td><button type="button" class="btn btn-primary" @click="selectLeader(leader)">선택</button></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- 팀장 수정 Modal -->
    <div class="modal fade" id="updateLeaderModal" tabindex="-1" aria-labelledby="updateLeaderModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="updateLeaderModalLabel">팀장 수정</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="updateLeader">
              <div class="mb-3">
                <label for="selectTeam" class="form-label">팀 선택</label>
                <select class="form-select" v-model="selectedTeamId" id="selectTeam" required>
                  <option v-for="team in teams" :key="team.id" :value="team.id">{{ team.teamName }}</option>
                </select>
              </div>
              <div class="mb-3">
                <label for="leaderName" class="form-label">팀장</label>
                <div class="input-group">
                  <input type="text" class="form-control" id="newLeaderName" v-model="newLeaderName" readonly />
                  <button type="button" class="btn btn-secondary" @click="showModal('selectLeaderModalForUpdate')">조회</button>
                </div>
              </div>
              <div class="modal-footer">
                <div class="button-container">
                  <button type="submit" class="btn btn-primary">수정</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- 팀장 선택 Modal for Update -->
    <div class="modal fade" id="selectLeaderModalForUpdate" tabindex="-1" aria-labelledby="selectLeaderModalForUpdateLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="selectLeaderModalForUpdateLabel">팀장 선택</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <input type="text" class="form-control mb-3" placeholder="이름, 사원번호 또는 직책 검색" v-model="leaderSearchQuery" @input="searchLeaders">
            <table class="table">
              <thead>
                <tr>
                  <th>사원번호</th>
                  <th>팀장</th>
                  <th>직책</th>
                  <th>직급</th>
                  <th>선택</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="leader in filteredLeaders" :key="leader.id">
                  <td>{{ leader.employeeNumber }}</td>
                  <td>{{ leader.name }}</td>
                  <td>{{ leader.position.positionName }}</td>
                  <td>{{ leader.duties.dutiesName }}</td>
                  <td><button type="button" class="btn-primary" @click="selectLeaderForUpdate(leader)">선택</button></td>
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
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';
import { BTable } from 'bootstrap-vue-3';

const route = useRoute();
const router = useRouter();
const departmentId = ref(route.params.id);
const departmentName = ref('');
const userRole = ref('');
const userId = ref('');

const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = 10;
const teams = ref([]);
const allTeams = ref([]); // 전체 팀 정보를 저장할 변수
const departments = ref([]);
const leaders = ref([]);
const selectedTeams = ref([]); // 선택된 팀 정보를 저장할 변수
const newTeam = ref({
  teamName: '',
  departmentId: departmentId.value,
  departmentName: '',
  leaderId: null,
  leaderName: ''
});
const selectedTeamId = ref(null);
const newLeaderName = ref('');
const newLeaderId = ref(null);
const leaderSearchQuery = ref('');

const fetchTeams = async () => {
  try {
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/team/sub-department/${departmentId.value}`);
    const teamData = await Promise.all(response.data.result.map(async team => {
      const leaderResponse = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/id/${team.leaderId}`);
      team.leaderName = leaderResponse.data.result.name;
      
      const departmentResponse = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/department/${team.departmentId}`);
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
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/department/${departmentId.value}`);
    departmentName.value = response.data.result.departmentName;
    newTeam.value.departmentName = departmentName.value; // 부서명 설정
  } catch (error) {
    console.error('부서 정보를 가져오는 중 오류 발생:', error);
  }
};

const fetchDepartments = async () => {
  try {
    const response = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/department/find-all');
    departments.value = response.data.result;
  } catch (error) {
    console.error('부서 정보를 가져오는 중 오류 발생:', error);
  }
};

const fetchLeaders = async () => {
  try {
    const response = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/list/all');
    leaders.value = response.data.result;
  } catch (error) {
    console.error('직원 정보를 가져오는 중 오류 발생:', error);
  }
};

// departmentId가 변경될 때마다 부서명 업데이트
watch(departmentId, (newId) => {
  const department = departments.value.find(dep => dep.id === newId);
  if (department) {
    newTeam.value.departmentName = department.departmentName;
  }
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

onMounted(() => {
  fetchDepartmentName(); 
  fetchTeams();
  fetchDepartments();
  fetchLeaders();

  const token = localStorage.getItem('access');
  if (token) {
    const decodedToken = parseJwt(token);
    userRole.value = decodedToken?.auth || '';
    userId.value = decodedToken?.id || '';
  }
});


const fields = [
  { key: 'selected', label: '', thStyle: { width: '5%' } },
  { key: 'teamName', label: '팀명', thStyle: { width: '10%' } },
  { key: 'startTime', label: '시작 일', thStyle: { width: '20%' } },
  { key: 'endTime', label: '종료일', thStyle: { width: '20%' } },
  { key: 'departmentName', label: '상위 부서명', thStyle: { width: '10%' } },
  { key: 'leaderName', label: '팀장', thStyle: { width: '10%' } },
  { key: 'actions', label: '', thStyle: { width: '10%' } }
];

const filteredTeams = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  
  return teams.value
    .filter(team => {
      if (userRole.value !== 'ROLE_ADMIN' && team.teamStatus === 'N') {
        return false; 
      }
      return true;
    })
    .slice(start, end);
});

const totalPages = computed(() => {
  return Math.ceil(teams.value.filter(team => {
    if (userRole.value !== 'ROLE_ADMIN' && team.teamStatus === 'N') {
      return false;
    }
    return true;
  }).length / itemsPerPage);
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
  
  teams.value = allTeams.value.filter(team => team.teamName.includes(searchQuery.value));
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

// Computed property to get list of leader IDs already assigned as team leaders
const leaderIdsInTeams = computed(() => {
  return teams.value.map(team => team.leaderId);
});

const addNewTeam = async () => {
  if (!newTeam.value.leaderId) {
    alert('팀장을 선택해야 합니다.');
    return;
  }

  if (leaderIdsInTeams.value.includes(newTeam.value.leaderId)) {
    alert('이미 다른 팀의 팀장으로 등록된 사람입니다.');
    return;
  }

  try {
    await axios.post('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/team', {
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
  } catch (error){ 
      if (error.response && error.response.data) {
        alert(error.response.data.message);
      } else {
      alert('팀 등록 중 오류가 발생했습니다.');
    }
  }
};

const selectLeader = (leader) => {
  if (leaderIdsInTeams.value.includes(leader.id)) {
    alert('이미 다른 팀의 팀장으로 등록된 사람입니다.');
    return;
  }

  newTeam.value.leaderId = leader.id;
  newTeam.value.leaderName = leader.name;
  const modal = bootstrap.Modal.getInstance(document.getElementById('selectLeaderModal'));
  modal.hide();
};

const updateLeader = async () => {
  if (!newLeaderId.value || !selectedTeamId.value) {
    alert('팀장과 팀을 선택해야 합니다.');
    return;
  }

  if (leaderIdsInTeams.value.includes(newLeaderId.value)) {
    alert('이미 다른 팀의 팀장으로 등록된 사람입니다.');
    return;
  }

  try {
    await axios.put(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/team/team-leader`, {
      id: selectedTeamId.value,
      leaderId: newLeaderId.value
    });

    await fetchTeams();

    const modal = bootstrap.Modal.getInstance(document.getElementById('updateLeaderModal'));
    modal.hide();
    newLeaderName.value = '';
    newLeaderId.value = null;
    selectedTeamId.value = null;
  } catch (error){ 
      if (error.response && error.response.data) {
        alert(error.response.data.message);
      } else {
      alert('팀장 수정 중 오류가 발생했습니다.');
    }
  }
};

const selectLeaderForUpdate = (leader) => {
  if (leaderIdsInTeams.value.includes(leader.id)) {
    alert('이미 다른 팀의 팀장으로 등록된 사람입니다.');
    return;
  }

  newLeaderId.value = leader.id;
  newLeaderName.value = leader.name;
  const modal = bootstrap.Modal.getInstance(document.getElementById('selectLeaderModalForUpdate'));
  modal.hide();
};

const toggleTeamStatus = async () => {
  if (selectedTeams.value.length === 0) {
    alert('활성화 또는 비활성화할 팀을 선택하세요.');
    return;
  }

  try {
    const response = await axios.put('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/team/status', selectedTeams.value);

    if (response.status === 200) {
      alert('팀 상태가 수정되었습니다.');
      await fetchTeams();
      selectedTeams.value = [];
    }
  } catch (error) {
    console.error('팀 상태를 수정하는 중 오류 발생:', error);
    alert('팀 상태 수정 중 오류가 발생했습니다.');
  }
};

const goBack = () => {
  router.go(-1);
};

const checkSelectionLimit = () => {
  if (selectedTeams.value.length > 1) {
    alert('한 개의 팀만 선택할 수 있습니다.');
  }
};

const prepareUpdateLeaderModal = () => {
  if (selectedTeams.value.length === 1) {
    selectedTeamId.value = selectedTeams.value[0].id;
  } else {
    selectedTeamId.value = null;
  }
  showModal('updateLeaderModal');
};

const filteredLeaders = computed(() => {
  return leaders.value.filter(leader => {
    return leader.name.includes(leaderSearchQuery.value) || 
           leader.employeeNumber.includes(leaderSearchQuery.value) || 
           leader.position.positionName.includes(leaderSearchQuery.value);
  });
});

const searchLeaders = () => {
  // 필터링된 leaders를 재계산합니다.
  filteredLeaders.value = leaders.value.filter(leader => {
    return leader.name.includes(leaderSearchQuery.value) || 
           leader.employeeNumber.includes(leaderSearchQuery.value) || 
           leader.position.positionName.includes(leaderSearchQuery.value);
  });
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
  flex-direction: column;
}


.reviewIcon {
  width: 30px; /* 이미지 크기 유지 */
}

.department-name {
  
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
  justify-content: right;
  align-items: center;
  margin-bottom: 20px;
}

.search-group {
  display: flex;
  align-items: center;
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

.toggleStatusBtn {
  background-color: #088A85;
  color: white;
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  margin-left: 10px;
}

.toggleStatusBtn:hover {
  background-color: #065f5b;
}

.updateLeaderBtn {
  background-color: #088A85;
  color: white;
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  margin-right: 10px;
}

.updateLeaderBtn:hover {
  background-color: #065f5b;
}

/* 테이블 스타일 */
.teamTable {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
  table-layout: fixed;
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

.btn-custom-1 {
    background-color: white;
    color: #088A85;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.4s, color 0.4s;
    position: relative;
    overflow: hidden;
    font-size: 11px;
    font-weight: bold;
}

.btn-custom-1::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background-color: #088A85;
    transition: left 0.4s;
    z-index: 1;
}

.btn-custom-1:hover::before {
    left: 0;
}

.btn-custom-1 span {
    position: relative;
    z-index: 2;
    color: #088A85;
}

.btn-custom-1:hover span {
    color: white;
}

.listBtn {
  margin-top: 0;
}
h1 {
  font-size: 25px;
  margin: 0; /* 세로 높이를 맞추기 위해 margin 제거 */
}


.buttons {
  display: flex;
  align-items: center; /* 세로 높이를 맞추기 위해 추가 */
  justify-content: right;
}

.button-container {
  display: flex;
  justify-content: center;
  width: 100%;
  padding-top: 10px;
}


</style>

