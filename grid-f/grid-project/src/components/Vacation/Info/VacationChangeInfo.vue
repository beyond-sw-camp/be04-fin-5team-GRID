<template>
    <div class="historyAll">
        <div class="historyTitle">
            <img class="historyIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 변화이력</h1>
        </div>
        <div class="vacations">
            <div class="annual">
                <div class="vacationsTitle">
                    <h3>연 단위 휴가 지급<br> (연차, 정기휴가)</h3>
                    <img class="plusBtn" @click=giveAnnual() src="@/assets/buttons/plus.png">
                </div>
            </div>
            <div class="month">
                <div class="vacationsTitle">
                    <h3>월 단위 휴가 지급 <br> (월차, 보건휴가)</h3>
                    <img class="plusBtn" @click=giveMonth() src="@/assets/buttons/plus.png">
                </div>
            </div>
            <div class="diretly">
                <div class="vacationsTitle">
                    <h3>휴가 직접 지급 <br> (관리자)</h3>
                    <img class="plusBtn" src="@/assets/buttons/plus.png">
                </div>
            </div>
        </div>
        <div class="search">  
            <input class="sortBox" type="text" placeholder="검색">
            <button class="printBtn">검색</button>
        </div> 
        <div class="tableContainer">
            <table>
                <thead>
                    <tr>
                        <th>번호</th>
                        <th>이름</th>
                        <th>지급종류</th>
                        <th>휴가종류</th>
                        <th>변경일</th>
                        <th>사유</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(history, index) in paginatedHistories" :key="history.id">
                        <td>{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
                        <td>{{ history.employeeName }}</td>
                        <td>{{ history.changeTypeName }}</td>
                        <td>{{ history.typeName }}</td>
                        <td>{{ history.changeTime }}</td>
                        <td>{{ history.changeReason }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="pagination">
            <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
            <button 
                v-for="page in totalPages" 
                :key="page" 
                @click="goToPage(page)" 
                :class="{ active: page === currentPage }"
            >{{ page }}</button>
            <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
        </div>
    </div>
</template>



<script setup>
import { onBeforeMount, ref, computed } from 'vue';
import axios from "axios";
import router from '@/router/router';

const histories = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;

const totalPages = computed(() => {
    return Math.ceil(histories.value.length / itemsPerPage);
});

const paginatedHistories = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    return histories.value.slice(start, end);
});

const getAllVacationHistory = async () => {
    try {
        const response = await axios.get("/api/vacation/details");
        histories.value = response.data.result;
        console.log(response.data.result);
    } catch (error) {
        console.error("Error fetching vacation details:", error);
    }
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

function giveAnnual() {
    try {
        giveAnnualVacation();
        giveRegularVacation();
        alert('지급 완료되었습니다!')
    } catch (error) {
        alert(error.message)
    }
}

function giveMonth() {
    try {
        giveHealthVacation();
        giveMonthVacation();
        alert('지급 완료되었습니다!')
    } catch (error) {
        alert(error.message)
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
    getAllVacationHistory();
});
</script>



<style scoped>
    .historyAll {
        display: grid;
        grid-template-rows: 18% 15% 4% 43% 5% 13%;
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
        grid-template-columns: 80% 15% 1% 4%;
    }

    .sortBox {
        grid-column-start: 2;
        margin-left: 2%;
        padding: 5px 5px;
        border-radius: 4px;
        font-size: 12px;
        font-style: bold;
    }

    .printBtn {
        grid-column-start: 4;
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

    th, td {
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
        background-color: darkorange;
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
</style>


