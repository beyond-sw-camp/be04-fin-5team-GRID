<template>
    <div class="historyAll">
        <div class="historyTitle">
            <i class="bi bi-brightness-high fs-3"></i>
            <h1 class="mb-1">휴가 보유 정보/변화 이력</h1>
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
        <div class="tableContainer">
            <b-table empty-html hover small :fields="fields" :items="paginatedInfo">
                <template #cell(index)="data">
                    {{ (currentPageInfo - 1) * itemsPerPage + data.index + 1 }}
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
                <li class="page-item" :class="{ disabled: currentPageInfo === 1 }">
                    <a class="page-link" href="#" aria-label="First" @click.prevent="goToFirstPageInfo">
                        <span aria-hidden="true">&laquo;&laquo;</span>
                    </a>
                </li>
                <li class="page-item" :class="{ disabled: currentPageInfo === 1 }">
                    <a class="page-link" href="#" aria-label="Previous" @click.prevent="prevPageInfo">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                <li v-for="page in filteredPagesInfo" :key="page" class="page-item"
                    :class="{ active: page === currentPageInfo }">
                    <a class="page-link" @click.prevent="goToPageInfo(page)">{{ page }}</a>
                </li>
                <li class="page-item" :class="{ disabled: currentPageInfo === totalPagesInfo }">
                    <a class="page-link" aria-label="Next" @click.prevent="nextPageInfo">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
                <li class="page-item" :class="{ disabled: currentPageInfo === totalPagesInfo }">
                    <a class="page-link" href="#" aria-label="Last" @click.prevent="goToLastPageInfo">
                        <span aria-hidden="true">&raquo;&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
        <div class="tableContainer-changeInfo">
            <b-table hover small :fields="changeInfofields" :items="paginatedHistories">
                <template #cell(index)="data">
                    {{ (currentPageHistories - 1) * itemsPerPage + data.index + 1 }}
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
        <nav class="changeInfopg" aria-label="Page navigation example">
            <ul class="pagination">
                <li class="page-item" :class="{ disabled: currentPageHistories === 1 }">
                    <a class="page-link" href="#" aria-label="First" @click.prevent="goToFirstPageHistories">
                        <span aria-hidden="true">&laquo;&laquo;</span>
                    </a>
                </li>
                <li class="page-item" :class="{ disabled: currentPageHistories === 1 }">
                    <a class="page-link" href="#" aria-label="Previous" @click.prevent="prevPageHistories">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                <li v-for="page in filteredPagesHistories" :key="page" class="page-item"
                    :class="{ active: page === currentPageHistories }">
                    <a class="page-link" @click.prevent="goToPageHistories(page)">{{ page }}</a>
                </li>
                <li class="page-item" :class="{ disabled: currentPageHistories === totalPagesHistories }">
                    <a class="page-link" aria-label="Next" @click.prevent="nextPageHistories">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
                <li class="page-item" :class="{ disabled: currentPageHistories === totalPagesHistories }">
                    <a class="page-link" href="#" aria-label="Last" @click.prevent="goToLastPageHistories">
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

const allInfo = ref([]);
const vacationNum = ref('');
const currentPageInfo = ref(1); // 변경된 부분: vacation info의 현재 페이지
const currentPageHistories = ref(1); // 변경된 부분: vacation histories의 현재 페이지
const itemsPerPage = 5;
const filteredInfo = ref([]);
const userRole = ref('');
const userId = ref('');
const histories = ref([]);
const filteredHistories = ref([]); // 필터링된 기록

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

const changeInfofields = [
    {key: 'index', label: '번호'},
    {key: 'employeeName', label: '이름'},
    {key: 'employeeNumber', label: '사번'},
    {key: 'changeTypeName', label: '지급종류'},
    {key: 'typeName', label: '휴가종류'},
    {key: 'changeTime', label: '변경일'},
    {key: 'changeNum', label: '변화개수'},
    {key: 'changeReason', label: '사유'}
];

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

const getUserVacationHistory = async () => {
  try {
    const response = await axios.get(`/api/vacation/details/${userId.value}`);
    histories.value = response.data.result;
    filteredHistories.value = histories.value; // 처음에 모든 기록을 보여줌
  } catch (error) {
    console.error("Error fetching vacation details:", error);
  }
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
    const start = (currentPageInfo.value - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    return filteredInfo.value.slice(start, end);
});

const paginatedHistories = computed(() => {
    const start = (currentPageHistories.value - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    return filteredHistories.value.slice(start, end);
});


const filteredPagesInfo = computed(() => {
    const maxPages = 5; // 페이지당 최대 표시할 페이지 수
    const startPage = Math.max(1, currentPageInfo.value - Math.floor(maxPages / 2));
    const endPage = Math.min(totalPagesInfo.value, startPage + maxPages - 1);

    const pages = [];
    for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
    }
    return pages;
});

const filteredPagesHistories = computed(() => {
    const maxPages = 5; // 페이지당 최대 표시할 페이지 수
    const startPage = Math.max(1, currentPageHistories.value - Math.floor(maxPages / 2));
    const endPage = Math.min(totalPagesHistories.value, startPage + maxPages - 1);

    const pages = [];
    for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
    }
    return pages;
});

const totalPagesInfo = computed(() => {
    return Math.ceil(filteredInfo.value.length / itemsPerPage);
});

const totalPagesHistories = computed(() => {
    return Math.ceil(filteredHistories.value.length / itemsPerPage);
});

const goToPageInfo = (page) => {
    if (page >= 1 && page <= totalPagesInfo.value) {
        currentPageInfo.value = page;
    }
};

const goToPageHistories = (page) => {
    if (page >= 1 && page <= totalPagesHistories.value) {
        currentPageHistories.value = page;
    }
};

const prevPageInfo = () => {
    if (currentPageInfo.value > 1) {
        currentPageInfo.value--;
    }
};

const prevPageHistories = () => {
    if (currentPageHistories.value > 1) {
        currentPageHistories.value--;
    }
};

const nextPageInfo = () => {
    if (currentPageInfo.value < totalPagesInfo.value) {
        currentPageInfo.value++;
    }
};

const nextPageHistories = () => {
    if (currentPageHistories.value < totalPagesHistories.value) {
        currentPageHistories.value++;
    }
};

const goToFirstPageInfo = () => {
    currentPageInfo.value = 1;
};

const goToFirstPageHistories = () => {
    currentPageHistories.value = 1;
};

const goToLastPageInfo = () => {
    currentPageInfo.value = totalPagesInfo.value;
};

const goToLastPageHistories = () => {
    currentPageHistories.value = totalPagesHistories.value;
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

        getUserVacationInfo();
        getUserVacationHistory();
});
</script>

<style scoped>
.historyAll {
    display: grid;
    grid-template-rows: 18% 13% 25% 8% 25% 8% 3%;
    grid-template-columns: 10% 80% 10%;
    height: 100%;
}

.historyTitle {
  grid-column-start: 2;
  align-items: center;
  margin-top: 2%;
  display: grid;
  grid-template-columns: 3% 97%;
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


.tableContainer {
    grid-row-start: 3;
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
    grid-row-start: 4;
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

.tableContainer-changeInfo {
    grid-row-start: 5;
    grid-column-start: 2;
    margin-top: 20px;
    font-size: 12px; 
}

.changeInfopg {
    grid-row-start: 6;
    grid-column-start: 2;
    grid-column-end: 3;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 10px;
}
</style>