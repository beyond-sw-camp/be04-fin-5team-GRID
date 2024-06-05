<template>
    <div class="hr-main">
        <div class="hr-title">
            <img class="hr-icon" src="@/assets/HR/hr.png" alt="인사 정보 메인 이미지">
            <h1>인사 정보</h1>
        </div>
        <div class="search">
            <button class="printBtn" @click="downloadCSV">
                <img src="@/assets/buttons/download.png" alt="download button">
                Download
            </button>
            <button type="button" class="modifyBtn" @click="toModify" v-if="userRole === 'ROLE_ADMIN'">
                <img src="@/assets/buttons/modify-btn.png" alt="modify button">
                Modify
            </button>
            <button type="button" class="addBtn" @click="toAddMulti" v-if="userRole === 'ROLE_ADMIN'">
                <img src="@/assets/buttons/plus.png" alt="add button">
                Add new
            </button>
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
        <nav class="pg" aria-label="Page navigation example" v-if="totalPages > 1">
            <ul class="pagination">
                <li class="page-item" :class="{ disabled: currentPage === 0 }">
                    <a class="page-link" href="#" aria-label="First" @click.prevent="goToPage(0)">
                        <span aria-hidden="true">&laquo;&laquo;</span>
                    </a>
                </li>
                <li class="page-item" :class="{ disabled: currentPage === 0 }">
                    <a class="page-link" href="#" aria-label="Previous" @click.prevent="prevPage">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                <li v-for="page in visiblePages" :key="page" class="page-item" :class="{ active: page === currentPage + 1 }">
                    <a class="page-link" href="#" @click.prevent="goToPage(page - 1)">{{ page }}</a>
                </li>
                <li class="page-item" :class="{ disabled: currentPage + 1 === totalPages }">
                    <a class="page-link" href="#" aria-label="Next" @click.prevent="nextPage">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
                <li class="page-item" :class="{ disabled: currentPage + 1 === totalPages }">
                    <a class="page-link" href="#" aria-label="Last" @click.prevent="goToPage(totalPages - 1)">
                        <span aria-hidden="true">&raquo;&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import defaultProfileImage from '@/assets/defaultProfile.jpg';
import axios from 'axios';
import router from '@/router/router';
import { saveAs } from 'file-saver';
import { Parser } from '@json2csv/plainjs';

const employeeList = ref([]);
const searchCondition = ref('');
const currentPage = ref(0);
const totalPages = ref(0);
const pageSize = ref(8);
const visiblePages = ref([]);
const userRole = ref('');

const getProfileUrl = (profilePath) => {
    return profilePath ? profilePath : defaultProfileImage;
};

const findUser = async () => {
    let response = null;
    const url = searchCondition.value.trim() === ''
        ? `http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/list?page=${currentPage.value}&size=${pageSize.value}`
        : `http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/list/${encodeURIComponent(searchCondition.value)}?page=${currentPage.value}&size=${pageSize.value}`;

    response = await axios.get(url);
    employeeList.value = response.data.result;
    totalPages.value = response.data.totalPages;
    updateVisiblePages();
    console.log("사원리스트 조회 결과: ", employeeList.value);
};

const updateVisiblePages = () => {
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);

    let startPage = Math.max(currentPage.value - halfVisiblePages, 0);
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages.value - 1);

    if (endPage - startPage < maxVisiblePages - 1) {
        startPage = Math.max(endPage - maxVisiblePages + 1, 0);
    }

    visiblePages.value = [];
    for (let i = startPage; i <= endPage; i++) {
        visiblePages.value.push(i + 1);
    }
};

const downloadCSV = async () => {
    const response = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/list/all');
    const csvData = response.data.result.map(
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

const prevPage = () => {
    if (currentPage.value > 0) {
        currentPage.value--;
        findUser();
    }
};

const nextPage = () => {
    if (currentPage.value + 1 < totalPages.value) {
        currentPage.value++;
        findUser();
    }
};

const goToPage = (page) => {
    if (page >= 0 && page < totalPages.value) {
        currentPage.value = page;
        findUser();
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

onMounted(async () => {
    const token = localStorage.getItem('access');
    if (token) {
        const decodedToken = parseJwt(token);
        userRole.value = decodedToken?.auth || '';
        console.log('토큰:', decodedToken.auth);
    }

    try {
        await findUser();
    } catch {
        console.error('axios error: 데이터를 받아오지 못했습니다.');
    }
});
</script>

<style scoped>
button {
    height: 100%;
}

.hr-main {
    display: grid;
    grid-template-columns: 10% 80% 10%;
    grid-template-rows: 18% 4% 80% 5%;
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
    margin-left: 1.2%;
    font-weight: bold;
    font-size: 14pt;
}

.hr-icon {
    color: black;
    width: 110%;
    filter: invert(0%) sepia(97%) saturate(0%) hue-rotate(82deg) brightness(98%) contrast(100%) !important;
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
    grid-row-start: 3;
    grid-column-start: 2;
    grid-column-end: 3;
    margin-top: 20px;
    font-size: 10px;
    overflow: auto; 
}

.tableContainer::-webkit-scrollbar {
    width: 0px;  
    height: 0px; 
}

.tableContainer {
    scrollbar-width: none; 
    -ms-overflow-style: none; 
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
.pg {
    grid-row-start: 4;
    grid-column-start: 2;
}

.profile-image {
    width: 25px;
    height: 25px;
    border-radius: 50%;
    object-fit: cover;
}

.pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 10px;
}

.pagination a {
    color: rgb(124, 122, 122); /* 기본 글자색을 검은색으로 설정 */
}

.pagination button {
    background-color: white;
    color: black;
    padding: 0;
    border: 1px solid #dddddd;
    border-radius: 4px;
    cursor: pointer;
    margin: 0 5px;
}

.pagination button:disabled {
    background-color: #dddddd;
    cursor: not-allowed;
}

.page-item.active .page-link {
    background-color: #088A85;
    border-color: #088A85;
    color: white; /* 선택된 버튼의 글자색을 흰색으로 설정 */
}
</style>
