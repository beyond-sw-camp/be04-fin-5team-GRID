<template>
    <div class="hr-main">
        <div class="hr-title">
            <img class="hr-icon" src="@/assets/icon2.png" alt="인사 정보 메인 이미지">
            <h1>인사 정보</h1>
        </div>
        <div class="search">
            <button class="printBtn" @click="downloadCSV">
                <img src="@/assets/buttons/download.png" alt="download button">
                Download
            </button>
            <button type="button" class="modifyBtn" @click="toModify">
                <img src="@/assets/buttons/modify-btn.png" alt="modify button">
                Modify
            </button>
            <div class="dropdown-container">
                <button type="button" class="addBtn" @click="toggleAddDropdown">
                    <img src="@/assets/buttons/plus.png" :class="{ rotated: isAddDropdownOpen }" alt="add button">
                    Add new
                </button>
                <ul class="dropdown-menu" ref="dropdownMenu" :class="{ show: isAddDropdownOpen }">
                    <li style="margin-bottom: 5%;"><a class="dropdown-item" href="#" @click="toAdd"><img
                                src="@/assets/buttons/add-one.png" alt="유저 추가 버튼">신규 직원 등록</a></li>
                    <li><a class="dropdown-item" href="#" @click="toAddMulti"><img src="@/assets/buttons/add-multi.png"
                                alt="다중 유저 추가 버튼">일괄
                            등록</a></li>
                </ul>
            </div>
            <input class="sortBox" v-model="searchCondition" type="text" placeholder="이름" @keyup.enter="findUser">
            <button class="searchBtn" @click="findUser">검색</button>
        </div>
        <div class="tableContainer">
            <table>
                <thead>
                    <tr>
                        <th></th>
                        <th>이름</th>
                        <th>사원 번호</th>
                        <th>부서</th>
                        <th>팀</th>
                        <th>직위</th>
                        <th>직책</th>
                        <th>부재중</th>
                        <th>비고</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="employee in employeeList" :key="employee.id" @click="toInfo(employee.employeeNumber)">
                        <td><img :src="getProfileUrl(employee.profilePath)" alt="profile" class="profile-image"></td>
                        <td>{{ employee.name }}</td>
                        <td>{{ employee.employeeNumber }}</td>
                        <td>{{ employee.department }}</td>
                        <td>{{ employee.team }}</td>
                        <td>{{ employee.position }}</td>
                        <td>{{ employee.duties }}</td>
                        <td>{{ employee.absenceYn === 'Y' ? 'Yes' : 'No' }}</td>
                        <td>{{ employee.absenceContent }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import defaultProfileImage from '@/assets/defaultProfile.jpg';
import axios from 'axios';
import router from '@/router/router';
import { saveAs } from 'file-saver';
import { Parser } from '@json2csv/plainjs';

const isAddDropdownOpen = ref(false);
const employeeList = ref([]);
const searchCondition = ref('');

const toggleAddDropdown = () => {
    isAddDropdownOpen.value = !isAddDropdownOpen.value;
};

const getProfileUrl = (profilePath) => {
    return profilePath ? profilePath : defaultProfileImage;
};

const findUser = async () => {
    let response = null;
    const url = searchCondition.value.trim() === ''
        ? 'http://localhost:8080/users/list'
        : `http://localhost:8080/users/list/${encodeURIComponent(searchCondition.value)}`;

    response = await axios.get(url);
    employeeList.value = response.data.result;
    console.log("사원리스트 조회 결과: ", employeeList.value);
};

const downloadCSV = () => {
    const csvData = employeeList.value.map(
        item => ({
            name: item.name,
            employeeNumber: item.employeeNumber,
            department: item.department,
            team: item.team,
            position: item.position,
            duties: item.duties,
            absenceYn: item.absenceYn,
            absenceContent: item.absenceContent
        })
    );

    const fields = ['name', 'employeeNumber', 'department', 'team', 'position', 'duties', 'absenceYn', 'absenceContent'];
    const json2csvParser = new Parser({ fields });
    const csv = json2csvParser.parse(csvData);
    const bom = '\uFEFF';
    const blob = new Blob([bom + csv], { type: 'text/csv;charset=utf-8;' });

    saveAs(blob, '사원 명단.csv');
};

const toModify = () => {
    router.push('/hr/modify/list')
};

const toAddMulti = () => {
    router.push('/hr/add/list')
};

const toAdd = () => {
    router.push('/hr/add')
};

const toInfo = (employeeNumber) => {
    router.push(`/hr/profile/${employeeNumber}`)
};

onMounted(async () => {
    document.addEventListener('click', handleDocumentClick);
    try {
        await findUser();
    } catch {
        console.error('axios error: 데이터를 받아오지 못했습니다.');
    }
});

onUnmounted(() => {
    document.removeEventListener('click', handleDocumentClick);
});

const handleDocumentClick = (event) => {
    const target = event.target;
    const dropdownButton = document.querySelector('.addBtn');
    if (!dropdownButton.contains(target) && !dropdownMenu.value.contains(target)) {
        isAddDropdownOpen.value = false;
    }
};
</script>

<style scoped>
button {
    height: 100%;
}

.hr-main {
    display: grid;
    grid-template-columns: 10% 80% 10%;
    grid-template-rows: 18% 4% 43% 5% 13%;
    height: 100%;
}

.hr-title {
    grid-column-start: 2;
    grid-column-end: 3;

    font-weight: 600;
    margin-top: 2%;
    color: #000000;
    display: grid;
    grid-template-columns: 3% 97%;
    align-items: center;
}

.hr-title h1 {
    margin-left: 0.5%;
    font-weight: 600;
    font-size: 25px;
}

.hr-icon {
    width: 80%;
    filter: invert(0%) sepia(64%) saturate(7%) hue-rotate(334deg) brightness(85%) contrast(101%);
}

.search {
    grid-row-start: 2;
    grid-column-start: 2;
    grid-template-columns: 14% 0.5% 12% 0.5% 12% 36.5% 18% 0.5% 6%;
    display: grid;
    justify-content: flex-end;
    height: 100%;
}

.printBtn {
    grid-column-start: 1;
    margin: 3px;
    height: 100%;
    min-width: 110px;
    background-color: #088A85;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-size: 70%;
    font-style: bold;
    justify-self: flex-start;
    width: 100%;
}

.modifyBtn {
    grid-column-start: 3;
    margin: 3px;
    min-width: 90px;
    height: 100%;
    background-color: #088A85;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
    justify-self: flex-start;
    width: 100%;
}

.dropdown-container {
    grid-column-start: 4;
    margin-left: 5px;
    position: relative;
    display: inline-block;
}

.addBtn {
    grid-column-start: 5;
    margin: 3px;
    width: 100%;
    min-width: 90px;
    height: 100%;
    background-color: #088A85;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
    justify-self: flex-start;
}

.printBtn img {
    width: 15%;
    height: 80%;
    margin: 0 6% 4% 0;
    filter: invert(100%) sepia(65%) saturate(424%) hue-rotate(91deg) brightness(129%) contrast(107%);
    transition: transform 0.3s ease;
}

.modifyBtn img {
    width: 16%;
    height: 50%;
    margin: 0 7% 2% 0;
    filter: invert(100%) sepia(65%) saturate(424%) hue-rotate(91deg) brightness(129%) contrast(107%);
}

.addBtn img {
    width: auto;
    height: auto;
    max-width: 20%;
    max-height: 90%;
    margin: 0 4% 2% 0;
    filter: invert(100%) sepia(65%) saturate(424%) hue-rotate(91deg) brightness(129%) contrast(107%);
    transition: transform 0.3s ease;
}

.addBtn img.rotated {
    transform: rotate(45deg);
}

.dropdown-menu {
    display: none;
    font-size: 12px;
    position: absolute;
    z-index: 999;
    background-color: white;
    border: 1px solid #ddd;
    border-radius: 4px;
    padding: 10px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    margin-top: 5px;
    left: 0;
    /* 버튼 바로 아래에 위치시키기 */
    top: 100%;
    /* 버튼 바로 아래에 위치시키기 */
}

.dropdown-menu.show {
    display: block;
}

.dropdown-menu img {
    width: 16px;
    height: 16px;
    margin: 0 13% 2% 0;
    align-self: center;
}

.dropdown-item {
    padding: 0;
    transition: background-color 0.3s ease;
}

.dropdown-item:hover {
    background-color: #deefef;
}

.dropdown-menu li {
    display: flex;
}

.sortBox {
    grid-column-start: 7;
    margin-left: 2%;
    padding: 5px 5px;
    border-radius: 4px;
    font-size: 12px;
    font-style: bold;
    border: 0.5px solid #088A85;
}

.searchBtn {
    grid-column-start: 9;
    margin-left: 2%;
    width: 100%;
    min-width: 50px;
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
    font-size: 10px;
}

table {
    width: 100%;
    border-collapse: collapse;
}

th,
td {
    border: 1px solid #dddddd;
    text-align: center;
    vertical-align: middle;
    color: #a7a4a4;
    padding: 1rem;
}

th {
    background-color: #f2f2f2;
}

thead th {
    background-color: #f4f4f4;
    font-weight: bold;
}

tbody tr:hover {
    background-color: #f9f9f9;
}

.profile-image {
    width: 25px;
    height: 25px;
    border-radius: 50%;
    object-fit: cover;
}
</style>
