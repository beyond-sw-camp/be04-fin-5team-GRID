<template>
    <div class="historyAll">
        <div class="historyTitle">
            <img class="historyIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 보유 정보</h1>
        </div>
        <div class="vacations">
            <div class="annual" v-if="userRole === 'ROLE_USER'">
                <div class="card " >
                    <div class="card-body">
                        <h3 class="card-title">휴가</h3>
                        <p class="card-text">{{ annualVacationNum }}</p>
                    </div>
                </div>
            </div>
            <div class="month" v-if="userRole === 'ROLE_USER'">
                <div class="card " >
                    <div class="card-body">
                        <h3 class="card-title">정기 휴가</h3>
                        <p class="card-text">{{ monthVacationNum }}</p>
                    </div>
                </div>
            </div>
            <div class="diretly" v-if="userRole === 'ROLE_USER'">
                <div class="card " >
                    <div class="card-body">
                        <h3 class="card-title">기타 휴가</h3>
                        <p class="card-text">{{ directlyVacationNum }}</p>
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
            <!-- <table>
                <thead>
                    <tr>
                        <th>번호</th>
                        <th>이름</th>
                        <th>사번</th>
                        <th>휴가종류</th>
                        <th>지급날짜</th>
                        <th>휴가 사용기간</th>
                        <th>지급개수</th>
                    </tr>
                </thead>
                <tbody>
                    <template v-if="paginatedInfo.length === 0">
                        <tr>
                            <td colspan="7" class="no-data">휴가 보유 정보가 없습니다.</td>
                        </tr>
                    </template>
                    <tr v-for="(info, index) in paginatedInfo" :key="info.id">
                        <td>{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
                        <td>{{ info.employeeName }}</td>
                        <td>{{ info.employeeNumber }}</td>
                        <td>{{ info.typeName }}</td>
                        <td>{{ info.addTime }}</td>
                        <td>{{ info.addTime + "~" +info.endTime }}</td>
                        <td>{{ info.vacationNum }}</td>
                    </tr>
                </tbody>
            </table> -->
            <b-table hover small :fields="fields" :items="paginatedInfo" >
                <template #cell(index)="data">
                    {{ (currentPage - 1) * itemsPerPage + data.index + 1 }}
                </template>
                <template #cell(employeeName)="data">
                    <span>{{ data.item.employeeName }}</span>
                </template>
                <template #cell(employeeNumber)="data">
                    <span>{{ data.item.employeeNumber }}</span>
                </template>
                <template #cell(typeName)="data">
                    <span>{{ data.item.typeName }}</span>
                </template>
                <template #cell(addTime)="data">
                    <span>{{ data.item.addTime }}</span>
                </template>
                <template #cell(changeTime)="data">
                    <span>{{ data.item.addTime + "~" + data.item.endTime }}</span>
                </template>
                <template #cell(vacationNum)="data">
                    <span>{{ data.item.vacationNum }}</span>
                </template>
            </b-table>
        </div>
        <nav class="pg" aria-label="Page navigation example" v-if="totalPages > 1">
            <ul class="pagination">
                <li class="page-item" :class="{ disabled: currentPage === 1 }">
                    <a class="page-link" href="#" aria-label="Previous" @click.prevent="prevPage">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: page === currentPage }">
                    <a class="page-link" @click.prevent="goToPage(page)">{{ page }}</a>
                </li>
                <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                    <a class="page-link" aria-label="Next" @click.prevent="nextPage">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
    </div>
</template>

<script setup>
import { onBeforeMount, ref, computed } from 'vue';
import axios from "axios";

const searchType = ref('name');
const searchQuery = ref('');
const allInfo = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;
const filteredInfo = ref([]);
const userRole = ref('');
const userId = ref('');

const annualVacationNum = ref(0);
const monthVacationNum = ref(0);
const directlyVacationNum = ref(0);

const fields = [
    { key: 'index', label: '번호' },
    { key: 'employeeName', label: '이름' },
    { key: 'employeeNumber', label: '사번' },
    { key: 'typeName', label: '휴가종류' },
    { key: 'addTime', label: '지급날짜' },
    { key: 'changeTime', label: '휴가 사용기간' },
    { key: 'vacationNum', label: '지급개수' }
];

const getAllVacationInfo = async () => {
    try {
        const response = await axios.get("/api/vacation/info");
        allInfo.value = response.data.result;
        filteredInfo.value = allInfo.value; // 초기화 시 전체 데이터를 필터링된 데이터에 할당
        calculateVacationNums();
    } catch (error) {
        console.error("Error:", error);
    }
};

const getUserVacationInfo = async () => {
    try {
        const response = await axios.get(`/api/vacation/info/${userId.value}`);
        allInfo.value = response.data.result;
        filteredInfo.value = allInfo.value; // 초기화 시 전체 데이터를 필터링된 데이터에 할당
        calculateVacationNums();
        console.log(allInfo.value)
    } catch (error) {
        console.error("Error:", error);
    }
};

const search = () => {
    if (searchType.value === 'name') {
        filteredInfo.value = allInfo.value.filter(info => info.employeeName.includes(searchQuery.value));
    } else if (searchType.value === 'employeeNumber') {
        filteredInfo.value = allInfo.value.filter(info => info.employeeNumber.includes(searchQuery.value));
    }
    currentPage.value = 1; // 검색 후 페이지를 1로 초기화
};

const calculateVacationNums = () => {
    annualVacationNum.value = allInfo.value
        .filter(info => info.typeId === 1 || info.typeId === 2)
        .reduce((acc, info) => acc + info.vacationNum, 0);

    monthVacationNum.value = allInfo.value
        .filter(info => info.typeId === 4)
        .reduce((acc, info) => acc + info.vacationNum, 0);

    directlyVacationNum.value = allInfo.value
        .filter(info => info.typeId === 3)
        .reduce((acc, info) => acc + info.vacationNum, 0);
};

const paginatedInfo = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    return filteredInfo.value.slice(start, end);
});

const totalPages = computed(() => {
    return Math.ceil(filteredInfo.value.length / itemsPerPage);
});

const goToPage = (page) => {
    if (page >= 1 && page <= totalPages.value) {
        currentPage.value = page;
    }
};

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

    if (userRole.value === 'ROLE_ADMIN') {
        getAllVacationInfo();
    } else if (userRole.value === 'ROLE_USER') {
        getUserVacationInfo();
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

.vacations {
    grid-row-start: 2;
    grid-column-start: 2;
    display: grid;
    grid-template-columns: 20% 5% 20% 5% 20% 5% 25%;
    margin-bottom:1%;
}

.vacationsTitle {
    display: grid;
    grid-template-columns: 85% 10% 5%;
    align-items: center;
    font-size: 12px;
    height: 50%;
}

.annual {
    width: calc(100% - 20px);
    background-color: #F2F2F2;
}

.annual h3 {
    font-size: 15px;
}

.month {
    width: calc(100% - 20px);
    background-color: #F2F2F2;
    grid-column-start: 3;
}

.month h3 {
    font-size: 15px;
}

.diretly {
    width: calc(100% - 20px);
    grid-column-start: 5;
    
}

.diretly h3 {
    font-size: 15px;
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
    grid-row-start: 4;
    grid-column-start: 2;
    grid-column-end: 3;
    margin-top: 20px;
    font-size: 12px;
}

table {
    width: 100%;
    border-collapse: collapse;
}

th, td {
    border: 1px solid #dddddd;
    text-align: left;
    padding: 6px;
    vertical-align: middle;
}

th {
    background-color: #f2f2f2;
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

.pagination button {
    background-color: white;
    color: black;
    padding: 5px 10px;
    border: 1px solid #dddddd;
    border-radius: 4px;
    cursor: pointer;
    margin: 0 5px;
}

.pagination button.active {
    background-color: #088A85;
    font-weight: bold;
    color: white;
}

.pagination button:disabled {
    background-color: #dddddd;
    cursor: not-allowed;
}

.vacationsNum {
    grid-column-start: 1;
    display: grid;
    grid-template-columns:90% 10%;
    font-size: 10px;
}

.vacationsNum h3 {
    font-size:13px;
    font-weight: 600;
}

.no-data {
    text-align: center;
    vertical-align: middle;
}  

.card-text {
        font-size: 13px;
      font-weight: 600;
      color:black;
      margin: 0;
      grid-row-start: 3;
    }

  .card-body {
    width:100%;
    padding: 0px 0px;
    margin: 0;
    display:grid;
    grid-template-rows: 1fr 1fr 1fr;
  }

  .card-title {
    margin:0;
  }

  .card {
    padding: 10px 10px;
    background-color: #088A85;
    margin-bottom:0;
    height: 100%;
  }
</style>

