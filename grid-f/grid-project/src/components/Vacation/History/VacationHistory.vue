<template>
    <div class="historyAll">
        <div class="historyTitle">
            <img class="historyIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 기록</h1>
        </div>
        <div class="vacations">
            <div class="today">
                <div class="vacationsTitle">
                    <h3>오늘 휴가 인원</h3>
                    <img class="plusBtn" src="@/assets/buttons/plus.png">
                </div>
                <div class="vacationsNum">
                    <h3>10</h3>
                </div>
            </div>
            <div class="tomorrow">
                <div class="vacationsTitle">
                    <h3>내일 휴가 인원</h3>
                    <img class="plusBtn" src="@/assets/buttons/plus.png">
                </div>
                <div class="vacationsNum">
                    <h3>10</h3>
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
                        <th>휴가종류</th>
                        <th>신청날짜</th>
                        <th>휴가 사용기간</th>
                        <th>사용개수</th>
                        <th>결재상태</th>
                        <th>세부정보</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="index in 10" :key="index">
                        <td>{{ index }}</td>
                        <td>이름 {{ index }}</td>
                        <td>사번</td>
                        <td>연차</td>
                        <td>2024-05-01</td>
                        <td>2024-05-02 ~ 2024-05-10</td>
                        <td>5</td>
                        <td>완료</td>
                        <td><img class="more" src="@/assets/buttons/zoom.png"></td>
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
import { onBeforeMount, ref } from 'vue';
import axios from "axios";
import router from '@/router/router';

const searchType = ref('name'); // 검색 유형을 위한 기본값 설정
const searchQuery = ref(''); // 검색어를 위한 변수
const histories = ref({
    result: {}
});

function selectAllCheckbox(event) {
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach(checkbox => {
        if (checkbox !== event.target) {
            checkbox.checked = event.target.checked;
        }
    });
}

const getAllVacationHistory = async () => {
    await axios.get("/api/vacation/details")
    .then(response => {
        histories.value = response.data;
    })

}

onBeforeMount(() => {
    getAllVacationHistory();
})
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
        grid-template-columns: 20% 5% 20% 5% 50%;
        align-items: center;
        margin-bottom: 5%;
    }

    .vacationsTitle {
        display: grid;
        grid-template-columns: 85% 10% 5%;
        align-items: center;
        font-size: 12px;
        height: 50%;
    }

    .vacationsNum {
        grid-column-start: 1;
        display: grid;
        grid-template-columns: 15% 85%;
        font-size: 10px;
    }

    .plusBtn {
        width: 100%;
    }

    .today {
        padding: 10px;
        background-color: #F2F2F2;
    }

    .tomorrow {
        padding: 10px;
        background-color: #F2F2F2;
        grid-column-start: 3;
    }

    .search {
        grid-row-start: 3;
        grid-column-start: 2;
        display: grid;
        grid-template-columns: 74% 5% 1% 15% 1% 4%;
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

    th, td {
        border: 1px solid #dddddd;
        text-align: left;
        padding: 6px;
        vertical-align: middle;
    }

    th {
        background-color: #f2f2f2;
    }

    .more {
        width: 12px;
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
