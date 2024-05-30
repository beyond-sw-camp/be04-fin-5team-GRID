<template>
  <div class="container">
    <div class="header">
      <div class="header-title">
        <img src="@/assets/department_icon.png" alt="department_icon" class="department-pic">
        <h1>부서 정보</h1>
      </div>
      <button class="addbtn" @click="showModal('addNewModal')">추가하기</button>
      <button class="modifybtn" @click="">수정하기</button>
    </div>
    <div class="search">
      <input type="text" class="searchBox" placeholder="부서명 검색" v-model="searchQuery">
      <button @click="search" class="searchBtn">검색</button>
    </div>
    <table class="deptTable">
      <thead>
        <tr>
          <th style="width: 10%;"></th>
          <th style="width: 10%;">부서코드</th>
          <th style="width: 20%;">부서명</th>
          <th style="width: 20%;">시작일</th>
          <th style="width: 20%;">종료일</th>
          <th style="width: 10%;">책임자</th>
          <th style="width: 10%;">상위 부서</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(department, index) in paginatedDepartments" :key="department.departmentCode">
          <td><input type="checkbox"></td>
          <td>{{ department.departmentCode }}</td>
          <td><a @click.prevent="goToDepartmentTeams(department.id)" href="#">{{ department.departmentName }}</a></td>
          <td>{{ formatDate(department.startTime) }}</td>
          <td>{{ formatDate(department.endTime) }}</td>
          <td>{{ department.leaderName }}</td>
          <td>{{ department.highDepartment }}</td>
        </tr>
      </tbody>
    </table>
    <div class="pagination">
      <a href="#" @click.prevent="changePage(page)" :class="{ active: page === currentPage }" v-for="page in totalPages" :key="page">{{ page }}</a>
    </div>

    <!-- Add New Department Modal -->
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
                <label for="leaderId" class="form-label">책임자</label>
                <div class="input-group">
                  <input type="text" class="form-control" id="leaderId" v-model="newDepartment.leaderId" readonly>
                  <button type="button" class="btn btn-secondary" @click="showModal('selectLeaderModal')">조회</button>
                </div>
              </div>
              <div class="mb-3">
                <label for="highDepartment" class="form-label">상위 부서</label>
                <div class="input-group">
                  <input type="text" class="form-control" id="highDepartment" v-model="newDepartment.highDepartment" readonly>
                  <button type="button" class="btn btn-secondary" @click="showModal('selectDepartmentModal')">조회</button>
                </div>
              </div>
              <button type="submit" class="btn btn-primary">등록</button>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- Select Leader Modal -->
    <div class="modal fade" id="selectLeaderModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">책임자 선택</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <table>
              <thead>
                <tr>
                  <th>책임자 ID</th>
                  <th>책임자 이름</th>
                  <th>선택</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(leader, index) in leaders" :key="leader.leaderId">
                  <td>{{ leader.leaderId }}</td>
                  <td>{{ leader.leaderName }}</td>
                  <td><button type="button" class="btn btn-primary" @click="selectLeader(leader)">선택</button></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- Select High Department Modal -->
    <div class="modal fade" id="selectDepartmentModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">상위 부서 선택</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <table>
              <thead>
                <tr>
                  <th>부서코드</th>
                  <th>부서명</th>
                  <th>선택</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(department, index) in departments" :key="department.departmentCode">
                  <td>{{ department.departmentCode }}</td>
                  <td>{{ department.departmentName }}</td>
                  <td><button type="button" class="btn btn-primary" @click="selectDepartment(department)">선택</button></td>
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
const newDepartment = ref({
  departmentName: '',
  departmentCode: '',
  startTime: '',
  endTime: '',
  leaderId: '',
  highDepartment: ''
});

// Fetch leader names
const fetchLeaders = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/users/${id}`);
    leaders.value = response.data.result;
  } catch (error) {
    console.error('책임자 정보를 불러오는 중 에러 발생:', error);
  }
};

// Fetch department list
const fetchDepartments = async () => {
  try {
    const response = await axios.get('http://localhost:8080/department/find-all');
    const departmentsData = response.data.result;
    console.log(departmentsData)
    for (const department of departmentsData) {
      department.leaderName = await fetchLeaderName(department.leaderId);
    }
    departments.value = departmentsData;
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

// Fetch leader name by ID
const fetchLeaderName = async (leaderId) => {
  try {
    const response = await axios.get(`http://localhost:8080/users/${leaderId}/leaders`);
    return response.data.result.depLeaderName;
  } catch (error) {
    console.error('책임자 이름을 불러오는 중 에러 발생:', error);
    return 'Unknown';
  }
};

onMounted(() => {
  fetchDepartments();
  fetchLeaders();
});

const totalPages = computed(() => {
  return Math.ceil(departments.value.length / itemsPerPage);
});

const paginatedDepartments = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return departments.value.slice(start, start + itemsPerPage);
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

const addNewDepartment = async () => {
  try {
    await axios.post('http://localhost:8080/department', newDepartment.value);
    await fetchDepartments();
    const modal = bootstrap.Modal.getInstance(document.getElementById('addNewModal'));
    modal.hide();
    newDepartment.value = {
      departmentName: '',
      departmentCode: '',
      startTime: '',
      endTime: '',
      leaderId: '',
      highDepartment: ''
    };
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

const selectLeader = (leader) => {
  newDepartment.value.leaderId = leader.leaderId;
  const modal = bootstrap.Modal.getInstance(document.getElementById('selectLeaderModal'));
  modal.hide();
};

const selectDepartment = (department) => {
  newDepartment.value.highDepartment = department.departmentName;
  const modal = bootstrap.Modal.getInstance(document.getElementById('selectDepartmentModal'));
  modal.hide();
};

const search = () => {
  currentPage.value = 1;
};

const goToDepartmentTeams = (id) => {
  window.location.href = `/team/${id}`;
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
    font-style: bold;
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
}

.header-title {

  grid-column-start: 2;
  display: grid;
  grid-template-columns: 90% 4.5% 1% 4.5%;
  align-items: center;
}

.header-title {
  align-items: center;
  display: grid; 
  grid-template-columns: 3% 97%;

}

.header-title h1 {
  font-size: 25px;
  font-weight: 600;
  margin-left: 0.5%;
  margin-bottom: 0;
}

.addbtn {
  padding: 5px 5px;
  background-color: #088A85;
  border: none;
  color: white;
  border-radius: 4px;
  font-size: 11px;
  cursor: pointer;
  grid-column-start: 2;
}

.modifybtn {
  grid-column-start:4 ;
  padding: 5px 5px;
  background-color: #088A85;
  border: none;
  color: white;
  border-radius: 4px;
  font-size: 11px;
  cursor: pointer;
}

.search-bar {
  grid-row-start:2;
  grid-column-start: 2;
  display: grid;
  grid-template-columns: 90% 10%;
}

.search-bar input {
  grid-column-start:2 ;
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
  grid-row-start:6;
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
</style>
