
<template>
    <div class="historyAll">
        <div class="historyTitle">
            <img class="historyIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 변화 이력</h1>
        </div>
        <div class="vacations">
            <div class="annual" v-if="userRole === 'ROLE_ADMIN'">
                <div class="card" >
                    <div class="card-body">
                        <h3 class="card-title">연 단위 휴가 지급<br> (연차, 정기휴가)</h3>
                        <button href="#" @click="giveAnnual()" class="btn btn-custom">지급하기</button>
                    </div>
                </div>
            </div>
            <div class="month"  v-if="userRole === 'ROLE_ADMIN'">
                <div class="card" >
                    <div class="card-body">
                        <h3 class="card-title">월 단위 휴가 지급 <br> (월차, 보건휴가)</h3>
                        <button href="#" @click="giveMonth()" class="btn btn-custom">지급하기</button>
                    </div>
                </div>
            </div>
            <div class="diretly"  v-if="userRole === 'ROLE_ADMIN'">
                <div class="card" >
                    <div class="card-body">
                        <h3 class="card-title">휴가 직접 지급 <br> (관리자)</h3>
                        <button href="#" @click="showModal('giveVacation')" class="btn btn-custom">지급하기</button>
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
            <b-table hover small :fields="fields" :items="paginatedHistories" >
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
                <li v-for="page in filteredPages" :key="page" class="page-item" :class="{ active: page === currentPage }">
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

      <!-- 휴가지급 모달 -->
      <div class="modal fade" id="giveVacation" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">휴가 정책 등록</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" @click="closeModal('giveVacation')"></button>
            </div>
            <div class="modal-body">
                <form class="needs-validation" @submit.prevent="validateAndRegistContent" novalidate>
                    <div class="mb-3">
                        <label for="vacationType" class="form-label">휴가 타입</label>
                        <select class="form-select" v-model="selectedType" id="vacationType" required>
                            <option value="" disabled selected>휴가 타입을 선택해주세요</option>
                            <option v-for="type in filteredTypes" :key="type.id" :value="type.id">{{ type.typeName }}</option>
                        </select>
                        <div class="invalid-feedback">
                            휴가 타입을 선택해주세요.
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="vacationNum" class="form-label">휴가 일수</label>
                        <input class="form-control" id="vacationNum" placeholder="내용을 입력해주세요." type="number" v-model="vacationNum" required>
                        <div class="invalid-feedback">
                            휴가 일수를 입력해주세요.
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="employeeNum" class="form-label">직원 사번</label>
                        <input class="form-control" id="employeeNum" placeholder="내용을 입력해주세요." v-model="employeeNum" required>
                        <div class="invalid-feedback">
                            직원 사번을 입력해주세요.
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="dayOfUsing" class="form-label">휴가 사용기한</label>
                        <VueDatePicker locale="ko" :enable-time-picker="false" :min-date="new Date()" v-model="date" class="inputField" />
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
import { onBeforeMount, ref, computed, onMounted } from 'vue';
import axios from "axios";
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';
import { BTable } from 'bootstrap-vue-3';

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
    { key: 'index', label: '번호' },
    { key: 'employeeName', label: '이름' },
    { key: 'employeeNumber', label: '사번' },
    { key: 'changeTypeName', label: '지급종류' },
    { key: 'typeName', label: '휴가종류' },
    { key: 'changeTime', label: '변경일' },
    { key: 'changeReason', label: '사유' }
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
        console.log(response.value)
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
        console.log(users.value);
    } catch (error) {
        console.error('Error:', error);
    }
}

  const giveVacationDirectly = async () => {
    if(!date.value) {
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
            const formattedDate = date.value.toISOString().split('T')[0];
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
        if(confirmed) {
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
        if(confirmed) {
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
        grid-template-rows: 18% 13% 4% 43% 10% 8%;
        grid-template-columns: 10% 80% 10%;
        height: 100%;
    }

    .historyTitle {
        grid-column-start: 2;
        grid-column-end: 3;
        font-size: 12px;
        font-weight: 600;
        margin-top: 2%;
        color: #000000;
        display: grid;
        grid-template-columns: 3% 97%;
        align-items: center;
    }

    .historyTitle h1 {
        margin-left: 0.5%;
        margin-bottom: 0;
        font-size: 25px;
        font-weight: 600;
    }

    .historyIcon {
        width: 80%;
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
    background-color: #088A85; /* 원하는 배경색 */
    border-color: #088A85; /* 원하는 테두리 색 */
    color: white; /* 원하는 텍스트 색 */
    }

    .pagination .page-item .page-link {
        color: #088A85; /* 기본 텍스트 색 */
    }

    .pagination .page-item.disabled .page-link {
        color: #088A85; /* 비활성화된 페이지 색 */
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


  .registBtn{
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

.btn-custom {
    font-size:11px;
    font-weight: 600;
    color:white;
    background-color: #088A85;
    border-color:#088A85 ;
    margin-top: 10px;
  }

  .vacations h3 {
      font-size: 14px;
      font-weight: 600;
      color:black;
      margin: 0;
    }

  .card-body {
    width:100%;
    padding: 0px 0px;
    display:grid;
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
