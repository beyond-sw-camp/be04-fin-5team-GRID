<template>
    <div class="historyAll">
        <div class="historyTitle">
            <img class="historyIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 변화 이력</h1>
        </div>
        <div class="vacations">
            <div class="annual" v-if="userRole === 'ROLE_ADMIN'">
                <div class="vacationsTitle">
                    <h3>연 단위 휴가 지급<br> (연차, 정기휴가)</h3>
                    <img class="plusBtn" @click=giveAnnual() src="@/assets/buttons/plus.png">
                </div>
            </div>
            <div class="month" v-if="userRole === 'ROLE_ADMIN'">
                <div class="vacationsTitle">
                    <h3>월 단위 휴가 지급 <br> (월차, 보건휴가)</h3>
                    <img class="plusBtn" @click=giveMonth() src="@/assets/buttons/plus.png">
                </div>
            </div>
            <div class="diretly" v-if="userRole === 'ROLE_ADMIN'">
                <div class="vacationsTitle">
                    <h3>휴가 직접 지급 <br> (관리자)</h3>
                    <img class="plusBtn" @click="showRegistModal = true" src="@/assets/buttons/plus.png">
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
            <table>
                <thead>
                    <tr>
                        <th>번호</th>
                        <th>이름</th>
                        <th>사번</th>
                        <th>지급종류</th>
                        <th>휴가종류</th>
                        <th>변경일</th>
                        <th>사유</th>
                    </tr>
                </thead>
                <tbody>
                    <template v-if="paginatedHistories.length === 0">
                        <tr>
                            <td colspan="7" class="no-data">휴가 변화 이력이 없습니다.</td>
                        </tr>
                    </template>
                    <template v-else>
                        <tr v-for="(history, index) in paginatedHistories" :key="history.id">
                            <td>{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
                            <td>{{ history.employeeName }}</td>
                            <td>{{ history.employeeNumber }}</td>
                            <td>{{ history.changeTypeName }}</td>
                            <td>{{ history.typeName }}</td>
                            <td>{{ history.changeTime }}</td>
                            <td>{{ history.changeReason }}</td>
                        </tr>
                    </template>
                </tbody>
            </table>
        </div>
        <div class="pagination">
            <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
            <button v-for="page in totalPages" :key="page" @click="goToPage(page)"
                :class="{ active: page === currentPage }">{{
                page }}</button>
            <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
        </div>
    </div>

    <Modal v-if="showRegistModal" @close="showRegistModal = false">
        <div class="registMain">
            <div class="registTitle">
                <h3 for="type">타입</h3>
                <select id="type" v-model="selectedType" class="selectField">
                    <option value="">선택해주세요.</option>
                    <option v-for="type in types" :key="type.id" :value="type.id">{{ type.typeName }}</option>
                </select>
            </div>
            <div class="vacationNum">
                <h3 for="VacationNum">휴가일수</h3>
                <input v-model="vacationNum" type="number" class="numInputField" placeholder="휴가일수를 입력해주세요.">
            </div>
            <div class="employeeNum">
                <h3 for="employeeNum">직원사번</h3>
                <input v-model="employeeNum" class="empInputField" placeholder="직원사번을 입력해주세요.">
            </div>
            <div class="registContent">
                <h3>휴가 사용기한</h3>
                <VueDatePicker locale="ko" :enable-time-picker="false" v-model="date" class="inputField" />
            </div>
            <div class="registBtnArea">
                <button class="registBtn" @click="giveVacationDirectly">지급하기</button>
            </div>
        </div>
    </Modal>
</template>

<script setup>
import { onBeforeMount, ref, computed } from 'vue';
import axios from "axios";
import router from '@/router/router';
import Modal from '@/components/Vacation/Info/VacationInfoModal.vue';
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

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
const date = ref(null);
const selectedType = ref('');

const totalPages = computed(() => {
    return Math.ceil(filteredHistories.value.length / itemsPerPage);
});

const paginatedHistories = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    return filteredHistories.value.slice(start, end);
});

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
        console.log(response.data.result);
    } catch (error) {
        console.error('Error:', error);
    }
};

const giveVacationDirectly = async () => {
    // 입력 값 검증 추가
    if (!selectedType.value) {
        alert('휴가타입을 선택해주세요.');
        return;
    }
    if (!vacationNum.value) {
        alert('지급일수를 입력해주세요.');
        return;
    }
    if (!employeeNum.value) {
        alert('사번을 입력해주세요.');
        return;
    }
    if (!date.value) {
        alert('사용기한을 선택해주세요.');
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

onBeforeMount(() => {
    const token = localStorage.getItem('access');
    if (token) {
        const decodedToken = parseJwt(token);
        userRole.value = decodedToken?.auth || '';
        userId.value = decodedToken?.id || '';
    }

    getVacationType();

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
    grid-template-rows: 18% 10% 4% 43% 10% 11%;
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

.vacationsTitle {
    margin-left: 5%;
    display: grid;
    grid-template-columns: 85% 10% 5%;
    align-items: center;
    font-size: 12px;
    height: 10vh;
}

.vacations h3 {
    font-size: 15px;
    font-weight: 600;
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

.pagination {
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


.pagination span {
    display: flex;
    align-items: center;
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

.no-data {
    text-align: center;
    vertical-align: middle;
}
</style>
