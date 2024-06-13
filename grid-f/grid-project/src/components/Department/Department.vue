<template>
  <div class="container">
    <div class="header">
      <div class="header-title">
        <h1 class="mb-1"><i class="bi bi-diagram-2 fs-3"></i>&nbsp; 부서 정보</h1>
      </div>
      <button v-if="userRole === 'ROLE_ADMIN'" class="updateLeaderBtn btn-custom-1" @click="prepareUpdateLeaderModal"><span>부서장 수정</span></button>
      <button v-if="userRole === 'ROLE_ADMIN'" class="addbtn btn-custom-1" @click="showModal('addNewModal')"><span>추가하기</span></button>
      <button v-if="userRole === 'ROLE_ADMIN'" class="modifybtn btn-custom-1" @click="modifyDepartmentsStatus"><span>활성/비활성</span></button>
    </div>
    <div class="search">
      <input type="text" class="searchBox" placeholder="부서명 검색" v-model="searchQuery">
      <button @click="search" class="searchBtn">검색</button>
    </div>
    <table class="deptTable" style="background-color: white;">
      <thead>
        <tr>
          <th style="width: 10%;"></th>
          <th style="width: 10%;">부서코드</th>
          <th style="width: 20%;">부서명</th>
          <th style="width: 20%;">시작일</th>
          <th style="width: 20%;">종료일</th>
          <th style="width: 10%;">부서장</th>
          <th style="width: 10%;"></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(department) in paginatedDepartments" :key="department.sequence">
          <td>
            <input v-if="userRole === 'ROLE_ADMIN'" type="checkbox" @change="selectDepartment(department)" :checked="isDepartmentSelected(department)">
          </td>
          <td>{{ department.departmentCode }}</td>
          <td>{{ department.departmentName }}</td>
          <td>{{ formatDate(department.startTime) }}</td>
          <td>{{ formatDate(department.endTime) }}</td>
          <td>{{ department.leaderName }}</td>
          <td>
            <button class="view-details-btn btn-custom-1" @click="goToDepartmentTeams(department.id)"><span>소속팀 목록</span></button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- 부서 추가 Modal -->
    <div class="modal fade" id="addNewModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">부서 정보 추가</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="addNewDepartment">
              <div class="mb-3">
                <label for="departmentName" class="form-label">부서명</label>
                <input type="text" class="form-control" id="departmentName" v-model="newDepartment.departmentName" required>
              </div>
              <div class="mb-3">
                <label for="departmentCode" class="form-label">부서코드</label>
                <input type="text" class="form-control" id="departmentCode" v-model="newDepartment.departmentCode" required>
              </div>
              <div class="mb-3">
                <label for="leaderName" class="form-label">부서장</label>
                <div class="input-group">
                  <input type="text" class="form-control" id="leaderName" v-model="newDepartment.leaderName" readonly required>
                  <button type="button" class="btn btn-secondary" @click="showModal('selectLeaderModal')">조회</button>
                </div>
              </div>
              <div class="button-container">
                <button type="submit" class="btn btn-primary">등록</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>


    <!-- 부서장 선택 모달 -->
    <div class="modal fade" id="selectLeaderModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">부서장 선택</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <input type="text" class="form-control mb-3" placeholder="이름, 사원번호 또는 직책 검색" v-model="leaderSearchQuery" @input="searchLeaders">
            <table class="table">
              <thead>
                <tr>
                  <th>사원번호</th>
                  <th>이름</th>
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

    <div class="modal fade" id="updateLeaderModal" tabindex="-1" aria-labelledby="updateLeaderModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="updateLeaderModalLabel">부서장 수정</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="updateLeader">
              <div class="mb-3">
                <label for="selectDepartment" class="form-label">부서 선택</label>
                <select class="form-select" v-model="selectedDepartmentId" id="selectDepartment" required>
                  <option v-for="department in departments" :key="department.id" :value="department.id">{{ department.departmentName }}</option>
                </select>
              </div>
              <div class="mb-3">
                <label for="leaderName" class="form-label">부서장 명</label>
                <div class="input-group">
                  <input type="text" class="form-control" id="newLeaderName" v-model="newLeaderName" readonly>
                  <button type="button" class="btn btn-secondary" @click="showModal('selectLeaderModalForUpdate')">조회</button>
                </div>
              </div>
              <div class="button-container">
                <button type="submit" class="btn btn-primary">수정</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- 부서장 선택 Modal for Update -->
    <div class="modal fade" id="selectLeaderModalForUpdate" tabindex="-1" aria-labelledby="selectLeaderModalForUpdateLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="selectLeaderModalForUpdateLabel">부서장 선택</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <input type="text" class="form-control mb-3" placeholder="이름, 사원번호 또는 직책 검색" v-model="leaderSearchQuery" @input="searchLeaders">
            <table class="table">
              <thead>
                <tr>
                  <th>사원번호</th>
                  <th>이름</th>
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
                  <td><button type="button" class="btn btn-primary" @click="selectLeaderForUpdate(leader)">선택</button></td>
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
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

const departments = ref([]);
const leaders = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;
const searchQuery = ref('');
const selectedDepartment = ref(null);
const userId = ref('');
const userRole = ref('');
const selectedDepartmentId = ref(null);
const newLeaderName = ref('');
const newLeaderId = ref(null);
const leaderSearchQuery = ref('');

const newDepartment = ref({
  departmentName: '',
  departmentCode: '',
  startTime: '',
  endTime: '',
  leaderId: '',
  leaderName: ''
});

const fetchLeaders = async () => {
  try {
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/list/all`);
    leaders.value = response.data.result;
    searchLeaders();
  } catch (error) {
    console.error('직원 정보를 불러오는 중 에러 발생:', error);
  }
};

// Fetch department list
const fetchDepartments = async () => {
  try {
    const response = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/department/find-all');
    const departmentsData = response.data.result;
    for (const department of departmentsData) {
      department.leaderName = await fetchLeaderName(department.leaderId);
    }
    departments.value = departmentsData.sort((a, b) => a.sequence - b.sequence); // sequence 순으로 정렬
    search(); 
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

// Fetch leader name by ID
const fetchLeaderName = async (leaderId) => {
  try {
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/id/${leaderId}`);
    return response.data.result.name;
  } catch (error) {
    console.error('부서장 이름을 불러오는 중 에러 발생:', error);
    return 'Unknown';
  }
};

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

onMounted(async () => {
  const token = localStorage.getItem('access');
  if (token) {
    const decodedToken = parseJwt(token);
    userRole.value = decodedToken?.auth || '';
    userId.value = decodedToken?.id || '';
  }
  await fetchDepartments();
  await fetchLeaders();
  search(); // 초기 데이터 로드 후 필터링 적용
});

const filteredDepartments = ref([]);

const search = () => {
  // 검색어에 맞는 부서 필터링
  let departmentsFiltered = departments.value.filter(department =>
    department.departmentName.includes(searchQuery.value)
  );

  // 부서 상태와 사용자 권한에 따른 필터링 추가
  filteredDepartments.value = departmentsFiltered.filter(department => {
    // 부서 상태가 'N'인 경우 ROLE_ADMIN만 접근 가능
    if (department.departmentStatus === 'N') {
      return userRole.value === 'ROLE_ADMIN';
    }
    return true; // 다른 경우는 모두 접근 가능
  });

  currentPage.value = 1;
};

const totalPages = computed(() => {
  return Math.ceil(filteredDepartments.value.length / itemsPerPage);
});

const paginatedDepartments = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredDepartments.value.slice(start, start + itemsPerPage);
});

const changePage = (page) => {
  if (page > 0 && page <= totalPages.value) {
    currentPage.value = page;
  }
};

const formatDate = (dateString) => {
  if (!dateString) return '-';
  const date = new Date(dateString);
  return date.toISOString().split('T')[0];
};

const showModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.show();
};

const leaderIdsInDepartments = computed(() => {
  return departments.value.map(department => department.leaderId);
});

const addNewDepartment = async () => {
  if (!newDepartment.value.leaderId) {
    alert('부서장을 선택해야 합니다.');
    return;
  }

  if (leaderIdsInDepartments.value.includes(newDepartment.value.leaderId)) {
    alert('이미 다른 부서의 부서장으로 등록된 사람입니다.');
    return;
  }

  // 부서명과 부서코드 중복 확인
  const isDepartmentNameDuplicate = departments.value.some(dept => dept.departmentName === newDepartment.value.departmentName);
  const isDepartmentCodeDuplicate = departments.value.some(dept => dept.departmentCode === newDepartment.value.departmentCode);

  if (isDepartmentNameDuplicate) {
    alert('부서명이 중복됩니다.');
    return;
  }

  if (isDepartmentCodeDuplicate) {
    alert('부서코드가 중복됩니다.');
    return;
  }

  try {
    await axios.post('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/department', newDepartment.value);
    await fetchDepartments();
    const modal = bootstrap.Modal.getInstance(document.getElementById('addNewModal'));
    modal.hide();
    newDepartment.value = {
      departmentName: '',
      departmentCode: '',
      startTime: '',
      endTime: '',
      leaderId: '',
      leaderName: ''
    };
    alert("추가되었습니다.");
  } catch (error) { 
      if (error.response && error.response.data) {
        alert(error.response.data.message);
      } else {
      alert('부서 등록 중 오류가 발생했습니다.');
    }
  }
};

const selectLeader = (leader) => {
  if (leaderIdsInDepartments.value.includes(leader.id)) {
    alert('이미 다른 부서의 부서장으로 등록된 사람입니다.');
    return;
  }
  newDepartment.value.leaderId = leader.id;
  newDepartment.value.leaderName = leader.name;
  const modal = bootstrap.Modal.getInstance(document.getElementById('selectLeaderModal'));
  modal.hide();
};

const selectLeaderForUpdate = (leader) => {
  if (leaderIdsInDepartments.value.includes(leader.id)) {
    alert('이미 다른 부서의 부서장으로 등록된 사람입니다.');
    return;
  }
  newLeaderId.value = leader.id;
  newLeaderName.value = leader.name;
  const modal = bootstrap.Modal.getInstance(document.getElementById('selectLeaderModalForUpdate'));
  modal.hide();
};

const goToDepartmentTeams = (id) => {
  window.location.href = `/team/${id}`;
};

const modifyDepartmentsStatus = async () => {
  if (!selectedDepartment.value) {
    alert('수정할 부서를 선택하세요.');
    return;
  }

  try {
    const response = await axios.put('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/department/status', [selectedDepartment.value]);
    if (response.status === 200) {
      alert('수정되었습니다.');
      await fetchDepartments();
      selectedDepartment.value = null;
      window.location.reload(); 
    }
  } catch (error) {
    console.error('부서 상태를 수정하는 중 에러 발생:', error);
    alert('부서 상태 수정 중 에러가 발생했습니다.');
  }
};

const selectDepartment = (department) => {
  selectedDepartment.value = department;
};

const isDepartmentSelected = (department) => {
  return selectedDepartment.value && selectedDepartment.value.id === department.id;
};

const updateLeader = async () => {
  if (!selectedDepartmentId.value || !newLeaderId.value) {
    alert('부서와 부서장을 선택해야 합니다.');
    return;
  }

  try {
    const response = await axios.put('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/department/department-leader', {
      id: selectedDepartmentId.value,
      leaderId: newLeaderId.value
    });

    if (response.status === 200) {
      alert('부서장이 수정되었습니다.');
      await fetchDepartments();
      const modal = bootstrap.Modal.getInstance(document.getElementById('updateLeaderModal'));
      modal.hide();
      selectedDepartmentId.value = null;
      newLeaderName.value = '';
      newLeaderId.value = null;
    }
  } catch (error){ 
      if (error.response && error.response.data) {
        alert(error.response.data.message);
      } else {
      alert('부서장 수정 중 오류가 발생했습니다.');
    }
  }
};

const prepareUpdateLeaderModal = () => {
  if (selectedDepartment.value) {
    selectedDepartmentId.value = selectedDepartment.value.id;
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
  display: grid;
  grid-template-columns: 10% 80% 10%;
  grid-template-rows: 18% 3% 2% 65% 2% 5% 5%;
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
  border: 0.5px solid #ddd;
}
  
.searchBtn {
  grid-column-start: 6;
  margin-left: 2%;
  width: 100%;
  background-color: #088A85;
  color: white;
  padding: 5px 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-style: bold;
}

.deptTable {
  grid-column-start: 2;
  grid-row-start: 4;
}

.header {
  grid-column-start: 2;
  display: grid;
  grid-template-columns: 78.5% 6.5% 1% 6.5% 1% 6.5%;
  align-items: center;
}

.header-title {
  align-items: center;
  display: grid;
  margin-top: 2%;
  margin-left: -0.5%;
  color: #000000;
}

.header-title h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.addbtn {
  padding: 5px 5px;
  background-color: #088A85;
  border: none;
  color: white;
  border-radius: 4px;
  font-size: 11px;
  cursor: pointer;
  grid-column-start: 4;
}

.modifybtn {
  grid-column-start: 6;
  padding: 5px 5px;
  background-color: #088A85;
  border: none;
  color: white;
  border-radius: 4px;
  font-size: 11px;
  cursor: pointer;
}

.updateLeaderBtn {
  padding: 5px 5px;
  background-color: #088A85;
  border: none;
  color: white;
  border-radius: 4px;
  font-size: 11px;
  cursor: pointer;
  grid-column-start: 2;
}

.search-bar {
  grid-row-start: 2;
  grid-column-start: 2;
  display: grid;
  grid-template-columns: 90% 10%;
}

.search-bar input {
  grid-column-start: 2;
  width: 100%;
  border: 1px solid #ddd;
  padding: 5px;
  border-radius: 5px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
  table-layout: fixed;
}

th,
td {
  padding: 6px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  word-wrap: break-word;
}

th {
  background-color: white;
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
}

.pagination a {
  margin: 0 5px;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
  text-decoration: none;
  color: #333;
}

.pagination a.active {
  background-color: #088A85;
  color: white;
  border: none;
}

.department-pic {
  width: 80%;
  grid-column-start: 1;
}

.modal-header {
  margin-top: 10px;
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

td, th { text-align: center;}

.button-container {
  display: flex;
  justify-content: center;
  width: 100%;
  padding-top: 10px;
}
</style>
