<template>
    <div class="historyAll">
        <div class="historyTitle">
            <h1 class="mb-1"><i class="bi bi-brightness-high fs-3"></i>&nbsp;휴가 보유 정보</h1>
        </div>
        <div class="vacations">
            <div class="annual" v-if="userRole === 'ROLE_USER'">
                <div class="card ">
                    <div class="card-body">
                        <h3 class="card-title">휴가</h3>
                        <p class="card-text">{{ annualVacationNum }}</p>
                    </div>
                </div>
            </div>
            <div class="month" v-if="userRole === 'ROLE_USER'">
                <div class="card ">
                    <div class="card-body">
                        <h3 class="card-title">정기 휴가</h3>
                        <p class="card-text">{{ monthVacationNum }}</p>
                    </div>
                </div>
            </div>
            <div class="diretly" v-if="userRole === 'ROLE_USER'">
                <div class="card ">
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
            <b-table empty-html hover small :fields="fields" :items="paginatedInfo">
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
</template>

<script setup>
import { onBeforeMount, ref, computed } from 'vue';
import axios from "axios";

const searchType = ref('name');
const searchQuery = ref('');
const allInfo = ref([]);
const vacationNum = ref('');
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
    { key: 'changeTime', label: '휴가 사용기간' },
    { key: 'vacationNum', label: '보유개수' }
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

const goToFirstPage = () => {
    currentPage.value = 1;
};

const goToLastPage = () => {
    currentPage.value = totalPages.value;
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
    grid-template-rows: 18% 13% 4% 50% 10% 5%;
    grid-template-columns: 10% 80% 10%;
    height: 100%;
}

.historyTitle {
    grid-column-start: 2;
    align-items: center;
    margin-top: 2%;
    display: grid;
    grid-template-columns: 100%;
    color: #000000;
}

.historyTitle h1 {
    margin-bottom: 0;
    font-size: 25px;
    font-weight: 600;
    font-family: 'IBMPlexSansKR-Regular', sans-serif;
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
    margin-bottom: 1%;
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
    font-weight: 600;
    color: black;
}

.annual p {
    color: black;
}

.month {
    width: calc(100% - 20px);
    background-color: #F2F2F2;
    grid-column-start: 3;
}

.month h3 {
    font-size: 15px;
    font-weight: 600;
    color: black;
}

.month p {
    color: black;
}

.diretly {
    width: calc(100% - 20px);
    grid-column-start: 5;

}

.diretly h3 {
    font-size: 15px;
    font-weight: 600;
    color: black;
}

.diretly p {
    color: black;
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

th,
td {
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

.vacationsNum {
    grid-column-start: 1;
    display: grid;
    grid-template-columns: 90% 10%;
    font-size: 10px;
}

.vacationsNum h3 {
    font-size: 13px;
    font-weight: 600;
}

.no-data {
    text-align: center;
    vertical-align: middle;
}

.card-text {
    font-size: 13px;
    font-weight: 600;
    color: black;
    margin: 0;
    grid-row-start: 3;
}

.card-body {
    width: 100%;
    padding: 0px 0px;
    margin: 0;
    display: grid;
    grid-template-rows: 1fr 1fr 1fr;
}

.card-title {
    margin: 0;
}

.card {
    padding: 10px 10px;
    border: 2px solid #a0a0a0;
    margin-bottom: 0;
    height: 100%;
}
</style>