<template>
  <div class="historyAll">
    <div class="historyTitle">
        <h1 class="mb-1"><i class="bi bi-brightness-high fs-3"></i>&nbsp;휴가 변화 이력&nbsp;&nbsp;<i class="bi bi-info-circle fs-5 custom-icon" v-if="userRole === 'ROLE_ADMIN'" id="popover-target-1" style="color: darkgoldenrod" @click="showModal('guideModal')"></i> </h1>
    </div>
    <div class="vacations">
      <div class="annual" v-if="userRole === 'ROLE_ADMIN'">
        <div class="card">
          <div class="card-body">
            <h3 class="card-title">연 단위 휴가 지급<br> (연차, 정기휴가)</h3>
            <button href="#" @click="giveAnnual()" class="btn btn-custom-1">
              <span>지급하기</span></button>
          </div>
        </div>
      </div>
      <div class="month" v-if="userRole === 'ROLE_ADMIN'">
        <div class="card">
          <div class="card-body">
            <h3 class="card-title">월 단위 휴가 지급 <br> (월차, 보건휴가)</h3>
            <button href="#" @click="giveMonth()" class="btn btn-custom-1"><span>지급하기</span></button>
          </div>
        </div>
      </div>
      <div class="diretly" v-if="userRole === 'ROLE_ADMIN'">
        <div class="card">
          <div class="card-body">
            <h3 class="card-title">휴가 직접 지급 <br> (관리자)</h3>
            <button href="#" @click="showModal('giveVacation')" class="btn btn-custom-1"><span>지급하기</span></button>
          </div>
        </div>
      </div>
    </div>
    <div class="search">
      <select v-model="searchType" class="searchType">
        <option value="name">이름</option>
        <option value="employeeNumber">사번</option>
      </select>
      <input v-model="searchQuery" class="sortBox" type="text" placeholder="검색">
      <button @click="search" class="printBtn">검색</button>
    </div>
    <div class="tableContainer">
      <b-table hover small :fields="fields" :items="paginatedHistories">
        <template #cell(index)="data">
          {{ (currentPage - 1) * itemsPerPage + data.index + 1 }}
        </template>
        <template #cell(employeeName)="data">
          <span>{{ data.item.employeeName }}</span>
        </template>
        <template #cell(employeeNumber)="data">
          <span>{{ data.item.employeeNumber }}</span>
        </template>
        <template #cell(changeTypeName)="data">
          <span>{{ data.item.changeTypeName }}</span>
        </template>
        <template #cell(typeName)="data">
          <span>{{ data.item.typeName }}</span>
        </template>
        <template #cell(changeTime)="data">
          <span>{{ data.item.changeTime }}</span>
        </template>
        <template #cell(changeNum)="data">
          <span>{{ data.item.changeNum }}</span>
        </template>
        <template #cell(changeReason)="data">
          <span>{{ data.item.changeReason }}</span>
        </template>
      </b-table>
    </div>
    <nav class="pg" aria-label="Page navigation example">
      <ul class="pagination">
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <a class="page-link" href="#" aria-label="First" @click.prevent="goToFirstPage">
            <span aria-hidden="true">&laquo;&laquo;</span>
          </a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <a class="page-link" href="#" aria-label="Previous" @click.prevent="prevPage">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li v-for="page in filteredPages" :key="page" class="page-item"
            :class="{ active: page === currentPage }">
          <a class="page-link" @click.prevent="goToPage(page)">{{ page }}</a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <a class="page-link" aria-label="Next" @click.prevent="nextPage">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <a class="page-link" href="#" aria-label="Last" @click.prevent="goToLastPage">
            <span aria-hidden="true">&raquo;&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>

  </div>
  <!-- 가이드 모달 -->
  <div class="modal fade" id="guideModal" tabindex="-1" aria-labelledby="guideModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title" id="guideModalLabel">휴가 변화 이력 가이드</h4>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <div class="example-content">
            <p>휴가 변화이력 확인 및 휴가를 지급할 수 있는 페이지입니다.</p>
            <p>지급버튼은 관리자에게만 보입니다.</p>
            <hr>
            <p>1. 전직원의 휴가 변화이력을 확인할 수 있습니다.</p>
            <p>2. 연 단위 휴가 지급 버튼을 누르면 연차와 정기휴가가 지급됩니다.</p>
            <p>2-1. 1년 이상의 직원들에게는 15개부터 20개까지 계산되어 연차가 지급됩니다.</p>
            <p>2-2. 1년 미만의 직원들에게는 입사일 이후부터 다음 년도 1월1일까지 몇개월 근무했는지를 <br> &nbsp; &nbsp; &nbsp; 계산하여 근무한 달만큼
              지급됩니다.
            </p>
            <p>2-3. 정기휴가는 4개씩 전직원에게 지급됩니다.</p>
            <p>3. 월 단위 휴가 지급 버튼을 누르면 월차와 보건휴가가 지급됩니다.</p>
            <p>3-1. 1년 미만의 직원들은 한달에 한개씩 월차가 지급됩니다.</p>
            <p>3-2. 여성직원들에게는 한달에 한개씩 보건휴가가 지급됩니다.</p>
            <p>4. 직접지급 버튼을 누르면 원하는 휴가타입과 휴가일수, 사용기한을 입력한 후에 사번을 통해 <br> &nbsp; &nbsp; &nbsp; 해당직원에게 직접 휴가를 지급할 수 있습니다.</p>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
        </div>

      </div>
    </div>
  </div>

  <!-- 휴가지급 모달 -->
  <div class="modal fade" id="giveVacation" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">휴가 직접 지급</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"
                  @click="closeModal('giveVacation')"></button>
        </div>
        <div class="modal-body">
          <form class="needs-validation" @submit.prevent="validateAndRegistContent" novalidate>
            <div class="mb-3">
              <label for="vacationType" class="form-label">휴가 타입</label>
              <select class="form-select" v-model="selectedType" id="vacationType" required>
                <option value="" disabled selected>휴가 타입을 선택해주세요</option>
                <option v-for="type in filteredTypes" :key="type.id" :value="type.id">{{
                    type.typeName
                  }}
                </option>
              </select>
              <div class="invalid-feedback">
                휴가 타입을 선택해주세요.
              </div>
            </div>
            <div class="mb-3">
              <label for="vacationNum" class="form-label">휴가 일수</label>
              <input class="form-control" id="vacationNum" placeholder="내용을 입력해주세요." type="number"
                     v-model="vacationNum" required>
              <div class="invalid-feedback">
                휴가 일수를 입력해주세요.
              </div>
            </div>
            <div class="mb-3">
              <label for="employeeNum" class="form-label">직원 사번</label>
              <input class="form-control" id="employeeNum" placeholder="내용을 입력해주세요." v-model="employeeNum"
                     required>
              <div class="invalid-feedback">
                직원 사번을 입력해주세요.
              </div>
            </div>
            <div class="mb-3">
              <label for="dayOfUsing" class="form-label">휴가 사용기한</label>
              <b-form-input type="date" id="start" :min="new Date().toISOString().split('T')[0]"
                            v-model="date" required></b-form-input>
              <div class="invalid-feedback">
                휴가 사용기한을 입력해주세요.
              </div>
            </div>
            <div class="button-container">
              <button type="submit" class="btn btn-primary">지급</button>
            </div>
          </form>
        </div>
      </div>
    </div>


  </div>

</template>

<script setup>
import {onBeforeMount, ref, computed, onMounted} from 'vue';
import axios from "axios";
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';
import {BTable} from 'bootstrap-vue-3';

const histories = ref([]);
const searchType = ref('name'); // 검색 유형을 위한 기본값 설정
const searchQuery = ref(''); // 검색어를 위한 변수
const filteredHistories = ref([]); // 필터링된 기록
const currentPage = ref(1);
const itemsPerPage = 10;
const userRole = ref('');
const userId = ref('');
const showRegistModal = ref(false);
const types = ref([]);
const vacationNum = ref('');
const employeeNum = ref('');
const date = ref('');
const selectedType = ref('');
const users = ref([]);
const filteredTypes = ref([]);


const fields = [
  {key: 'index', label: '번호'},
  {key: 'employeeName', label: '이름'},
  {key: 'employeeNumber', label: '사번'},
  {key: 'changeTypeName', label: '지급종류'},
  {key: 'typeName', label: '휴가종류'},
  {key: 'changeTime', label: '변경일'},
  {key: 'changeNum', label: '변화개수'},
  {key: 'changeReason', label: '사유'}
];

const showModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.show();
};

const totalPages = computed(() => {
  return Math.ceil(filteredHistories.value.length / itemsPerPage);
});

const paginatedHistories = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredHistories.value.slice(start, end);
});

const filteredPages = computed(() => {
  const maxPages = 5; // 페이지당 최대 표시할 페이지 수
  const startPage = Math.max(1, currentPage.value - Math.floor(maxPages / 2));
  const endPage = Math.min(totalPages.value, startPage + maxPages - 1);

  const pages = [];
  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }
  return pages;
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

// 처음 페이지로 이동
const goToFirstPage = () => {
  currentPage.value = 1;
};

// 마지막 페이지로 이동
const goToLastPage = () => {
  currentPage.value = totalPages.value;
};

const getAllVacationHistory = async () => {
  try {
    const response = await axios.get("/api/vacation/details");
    histories.value = response.data.result;
    filteredHistories.value = histories.value; // 처음에 모든 기록을 보여줌
  } catch (error) {
    console.error("Error fetching vacation details:", error);
  }
};

const getUserVacationHistory = async () => {
  try {
    const response = await axios.get(`/api/vacation/details/${userId.value}`);

    histories.value = response.data.result;
    filteredHistories.value = histories.value; // 처음에 모든 기록을 보여줌
  } catch (error) {
    console.error("Error fetching vacation details:", error);
  }
};

const search = () => {
  if (searchQuery.value) {
    filteredHistories.value = histories.value.filter(history => {
      if (searchType.value === 'name') {
        return history.employeeName.includes(searchQuery.value);
      } else if (searchType.value === 'employeeNumber') {
        return history.employeeNumber.includes(searchQuery.value);
      }
    });
  } else {
    filteredHistories.value = histories.value; // 검색어가 없으면 모든 기록을 보여줌
  }
  currentPage.value = 1; // 새 검색 시 첫 페이지로 이동
};

const giveAnnualVacation = async () => {
  try {
    const response = await axios.get("/api/vacation/annual_vacation");
  } catch (error) {
    console.error("Error fetching vacation details:", error);
  }
}

const giveRegularVacation = async () => {
  try {
    const response = await axios.get("/api/vacation/regular_vacation");
  } catch (error) {
    console.error("Error fetching vacation details:", error);
  }
}

const giveHealthVacation = async () => {
  try {
    const response = await axios.get("/api/vacation/health_vacation");
  } catch (error) {
    console.error("Error fetching vacation details:", error);
  }
}

const giveMonthVacation = async () => {
  try {
    const response = await axios.get("/api/vacation/month_vacation");
  } catch (error) {
    console.error("Error fetching vacation details:", error);
  }
}

const getVacationType = async () => {
  try {
    const response = await axios.get('/api/vacation/type');
    types.value = response.data.result;
    filteredTypes.value = types.value.filter(type => type.typeName !== '반차' && type.typeName !== '반반차');
  } catch (error) {
    console.error('Error:', error);
  }
};

const validateAndRegistContent = () => {
  const form = document.querySelector('#giveVacation form');
  if (!form.checkValidity()) {
    form.classList.add('was-validated');

  } else {
    giveVacationDirectly();
  }

};

const closeModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.hide();
  if (modalId === 'giveVacation') {
    vacationNum.value = '';
    date.value = '';
    employeeNum.value = '';
    selectedType.value = '';
    const form = document.querySelector(`#${modalId} form`);
    if (form) {
      form.classList.remove('was-validated');
    }
  }
};

const allUsers = async () => {
  try {
    const response = await axios.get('/api/users/list/all');
    users.value = response.data.result;

  } catch (error) {
    console.error('Error:', error);
  }
}

const giveVacationDirectly = async () => {
  if (!date.value) {
    alert('휴가 사용기한을 선택해주세요.')
    return;
  }

  // 입력받은 사번을 users.value에서 찾기
  const employee = users.value.find(user => user.employeeNumber === employeeNum.value);

  if (!employee) {
    alert('해당 사번의 직원이 없습니다.');
    return;
  }

  try {
    const confirmed = window.confirm('지급하시겠습니까?');
    if (confirmed) {
      const formattedDate = date.value;
      const response = await axios.post('/api/vacation/payments', {
        vacationNum: vacationNum.value,
        endTime: formattedDate,
        employeeNum: employeeNum.value,
        typeId: selectedType.value
      });

      if (response.status === 200) {
        alert('지급 완료되었습니다!');
        showRegistModal.value = false; // 모달 닫기
        window.location.reload();
      } else {
        alert('지급에 실패했습니다.');
      }
    }
  } catch (error) {
    if (error.response && error.response.status === 404) {
      alert('지급 엔드포인트를 찾을 수 없습니다.');
    } else {
      console.error("Error:", error);
      alert('지급 중 오류가 발생했습니다.');
    }
  }
}


function giveAnnual() {
  try {
    const confirmed = window.confirm('지급하시겠습니까?');
    if (confirmed) {
      giveAnnualVacation();
      giveRegularVacation();
      alert('지급 완료되었습니다!')
      window.location.reload();
    }
  } catch (error) {
    alert(error.message)
  }
}

function giveMonth() {
  try {
    const confirmed = window.confirm('지급하시겠습니까?');
    if (confirmed) {
      giveHealthVacation();
      giveMonthVacation();
      alert('지급 완료되었습니다!')
      window.location.reload();
    }
  } catch (error) {
    alert(error.message)
  }
}


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


onBeforeMount(() => {
  const token = localStorage.getItem('access');
  if (token) {
    const decodedToken = parseJwt(token);
    userRole.value = decodedToken?.auth || '';
    userId.value = decodedToken?.id || '';
  }

  getVacationType();
  allUsers();

  if (userRole.value === 'ROLE_ADMIN') {
    getAllVacationHistory();
  } else if (userRole.value === 'ROLE_USER') {
    getUserVacationHistory();
  }
});
</script>

<style scoped>
.historyAll {
  display: grid;
  grid-template-rows: 18% 13% 4% 50% 10% 5%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.historyTitle {
  grid-column-start: 2;
  margin-top: 2%;
  display: grid;
  grid-template-columns: 23% 77%;
  align-items: center;
  color: #000000;
}

.historyTitle h1 {
  margin-bottom: 0;
  font-size: 25px;
  font-weight: 600;
  margin: 0;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.guide {
  width: 30px;
  height: 30px;
  grid-column: 3;
  margin-bottom: 5px;
  cursor: pointer;
}

.vacations {
  grid-column-start: 2;
  grid-column-end: 3;
  display: grid;
  grid-template-columns: 20% 5% 20% 5% 20% 30%;
  align-items: center;
  margin-bottom: 5%;
}

.plusBtn {
  width: 100%;
  cursor: pointer;
}

.annual {
  width: calc(100% - 20px);
  background-color: #F2F2F2;
  font-size: 15px;
}

.month {
  width: calc(100% - 20px);
  background-color: #F2F2F2;
  grid-column-start: 3;
  font-size: 15px;
}

.diretly {
  width: calc(100% - 20px);
  background-color: #F2F2F2;
  grid-column-start: 5;
  font-size: 15px;
}

.search {
  grid-row-start: 3;
  grid-column-start: 2;
  display: grid;
  grid-template-columns: 74% 5% 1% 15% 1% 4%;
  font-size: 12px;
}

.searchType {
  grid-column-start: 2;
}

.sortBox {
  grid-column-start: 4;
  margin-left: 2%;
  padding: 5px 5px;
  border-radius: 4px;
  font-size: 12px;
  font-style: bold;
}

.printBtn {
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

.tableContainer {
  grid-column-start: 2;
  grid-column-end: 3;
  margin-top: 20px;
  font-size: 12px;
}

.custom-icon {
    cursor: pointer;
}


.pg {
  grid-row-start: 5;
  grid-column-start: 2;
  grid-column-end: 3;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
}

.pagination .page-item.active .page-link {
  background-color: #088A85;
  /* 원하는 배경색 */
  border-color: #088A85;
  /* 원하는 테두리 색 */
  color: white;
  /* 원하는 텍스트 색 */
}

.pagination .page-item .page-link {
  color: #088A85;
  /* 기본 텍스트 색 */
}

.pagination .page-item.disabled .page-link {
  color: #088A85;
  /* 비활성화된 페이지 색 */
}

.pagination a {
  cursor: pointer;
}

.registMain {
  height: 100%;
  width: 100%;
  padding: 10px;
  background-color: #F2F2F2;
}

.registMain h3 {
  font-size: 15px;
  margin: 0;
  font-weight: 600;
}

.registTitle {
  margin-top: 2%;
  display: grid;
  grid-template-columns: 10% 20% 30% 10%;
  font-size: 14px;
  align-items: center;
}

.registTitle h3 {
  grid-column-start: 2;
}

.registContent {
  margin-top: 2%;
  display: grid;
  grid-template-columns: 10% 20% 50% 10%;
  font-size: 14px;
  align-items: center;
}

.employeeNum {
  margin-top: 2%;
  display: grid;
  grid-template-columns: 10% 20% 50% 10%;
  font-size: 14px;
  align-items: center;
}

.employeeNum h3 {
  grid-column-start: 2;
}


.vacationNum {
  margin-top: 2%;
  display: grid;
  grid-template-columns: 10% 20% 50% 10%;
  font-size: 14px;
  align-items: center;
}

.vacationNum h3 {
  grid-column-start: 2;
}

.registContent h3 {
  grid-column-start: 2;
}


.registBtn {
  width: 100%;
  background-color: #088A85;
  color: white;
  padding: 5px 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-style: bold;
  grid-column-start: 2;
}

.registBtnArea {
  display: grid;
  grid-template-columns: 40% 20% 40%;
  place-items: center;
  grid-row-start: 3;
  grid-column-start: 2;
  margin-top: 5%;
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
  margin-top: 10px;
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

.vacations h3 {
  font-size: 14px;
  font-weight: 600;
  color: black;
  margin: 0;
}

.card-body {
  width: 100%;
  padding: 0px 0px;
  display: grid;
  grid-template-rows: 1fr 1fr;
  margin: 0;
}

.card {
  padding: 10px 10px;
  border: 2px solid #a0a0a0;
}

.button-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
