<template>
    <div class="hr-main">
        <div class="hr-title">
            <h1 class="mb-1"><i class="bi bi-people"></i>&nbsp; 인사 정보 &nbsp;&nbsp;<i class="bi bi-info-circle fs-5 mt-5" id="popover-target-1" style="color: darkgoldenrod" @click="openModal"></i></h1>
        </div>
        <div class="search">
            <button class="printBtn" @click="downloadCSV">
                <i class="bi bi-download"></i>&nbsp; &nbsp;
                <span>목록 받기</span>
            </button>
            <button type="button" class="modifyBtn" @click="toModify" v-if="userRole === 'ROLE_ADMIN'">
                <i class="bi bi-pencil-square"></i>&nbsp; &nbsp;
                <span>정보 수정</span>
            </button>
            <button type="button" class="addBtn" @click="toAddMulti" v-if="userRole === 'ROLE_ADMIN'">
                <i class="bi bi-plus-circle"></i>&nbsp; &nbsp;
                <span>사원 추가</span>
            </button>
            <input class="sortBox" v-model="searchCondition" type="text" placeholder="이름" @keyup.enter="findUser">
            <button class="searchBtn" @click="findUser">검색</button>
        </div>
        <div class="tableContainer">
            <b-table hover small :fields="fields" :items="employeeList" @row-clicked="rowClicked">
                <template #cell(profileImage)="data">
                    <img :src="getProfileUrl(data.item.profilePath)" alt="profile" class="profile-image">
                </template>
                <template #cell(name)="data">
                    <span>{{ data.item.name }}</span>
                </template>
                <template #cell(employeeNumber)="data">
                    <span>{{ data.item.employeeNumber }}</span>
                </template>
                <template #cell(departmentName)="data">
                    <span>{{ data.item.department.departmentName }}</span>
                </template>
                <template #cell(teamName)="data">
                    <span>{{ data.item.team.teamName }}</span>
                </template>
                <template #cell(positionName)="data">
                    <span>{{ data.item.position.positionName }}</span>
                </template>
                <template #cell(dutiesName)="data">
                    <span>{{ data.item.duties.dutiesName }}</span>
                </template>
                <template #cell(absenceStatus)="data">
                    <b-badge variant="danger" v-if="data.item.absenceYn === 'Y'">부재중</b-badge>
                    <b-badge variant="success" v-else>재실중</b-badge>
                </template>
                <template #cell(absenceContent)="data">
                    <b-badge variant="warning" v-if="data.item.absenceContent === '휴가'">휴가</b-badge>
                    <b-badge variant="warning" v-if="data.item.absenceContent === '출장'">출장</b-badge>
                    <b-badge variant="danger" v-if="data.item.absenceContent === '퇴사'">퇴사</b-badge>
                    <!-- <span>{{ data.item.absenceContent }}</span> -->
                </template>
            </b-table>
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
                <li v-for="page in visiblePages" :key="page" class="page-item"
                    :class="{ active: page === currentPage + 1 }">
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
    <div class="modal fade" id="guideModal" tabindex="-1" aria-labelledby="guideModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title" id="guideModalLabel">인사 정보 가이드</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" style="background-color: white;"></button>
                </div>
                <!-- Modal body -->
                <div class="modal-body">
                    <div class="example">
                        <img src="@/assets/HR/hr-main.png" alt="csv 예시">
                    </div>
                    <div>
                        <p><정보 수정> 및 <사원 추가> 버튼은 관리자 계정에서만 확인 가능합니다. </p>
                    </div>
                    <div class="example-content">
                        <hr>
                        <p>1. <목록 받기>를 통해 전체 직원의 리스트를 다운로드 받을 수 있습니다.</p>
                        <p>2. <정보 수정>으로 사원 정보를 일괄 수정할 수 있습니다. </p>
                        <p>2-1. 사원 정보의 상세 수정은 '인사 정보 상세' > <회원 정보 수정>에서 가능합니다. </p>
                        <p>3. <사원 추가>를 통해 사원 정보를 일괄 등록할 수 있습니다. </p>
                    </div>
                </div>
                <!-- Modal footer -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import router from '@/router/router';
import { saveAs } from 'file-saver';
import { Parser } from '@json2csv/plainjs';
import defaultProfileImage from '@/assets/defaultProfile.jpg';
import { Modal } from 'bootstrap';

const employeeList = ref([]);
const searchCondition = ref('');
const currentPage = ref(0);
const totalPages = ref(0);
const pageSize = ref(12); // 페이징을 12명 기준으로 설정
const visiblePages = ref([]);
const userRole = ref('');
const guideModal = ref(null);

const fields = [
    { key: 'profileImage', label: '프로필' },
    { key: 'name', label: '이름' },
    { key: 'employeeNumber', label: '사원 번호' },
    { key: 'departmentName', label: '부서' },
    { key: 'teamName', label: '팀' },
    { key: 'positionName', label: '직위' },
    { key: 'dutiesName', label: '직책' },
    { key: 'absenceStatus', label: '부재 상태' },
    { key: 'absenceContent', label: '비고' }
];

const getProfileUrl = (profilePath) => {
    return profilePath ? profilePath : defaultProfileImage;
};

const findUser = async () => {
    let response = null;
    const token = localStorage.getItem('access');
    const headers = token ? { 'Authorization': `Bearer ${token}` } : {};

    const url = searchCondition.value.trim() === ''
        ? `http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/list?page=${currentPage.value}&size=${pageSize.value}`
        : `http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/list/${encodeURIComponent(searchCondition.value)}?page=${currentPage.value}&size=${pageSize.value}`;

    response = await axios.get(url, { headers });
    employeeList.value = response.data.result;
    totalPages.value = response.data.totalPages;
    updateVisiblePages();

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
            department: item.department.departmentName,
            team: item.team.teamName,
            position: item.position.positionName,
            duties: item.duties.dutiesName,
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

const rowClicked = (item) => {
    toInfo(item.employeeNumber);
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

const openModal = () => {
    if (!guideModal.value) {
        guideModal.value = new Modal(document.getElementById('guideModal'));
    }
    guideModal.value.show();
};

onMounted(async () => {
    const token = localStorage.getItem('access');
    if (token) {
        const decodedToken = parseJwt(token);
        userRole.value = decodedToken?.auth || '';
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
    grid-template-rows: 18% 4% 75% 5% 10%;
    height: 100%;
}

.hr-title {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
  margin-left: -0.5%;
  color: #000000;
  display: grid;
  grid-template-columns: 24% 4%;
  align-items: center;
  min-width: 1000px;
}

.hr-title h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.hr-icon {
    color: black;
    width: 80%;
    filter: invert(0%) sepia(97%) saturate(0%) hue-rotate(82deg) brightness(98%) contrast(100%) !important;
}

.search {
    grid-row-start: 2;
    grid-column-start: 2;
    grid-template-columns: 10% 0.5% 10% 0.5% 10% 44.5% 18% 0.5% 6%;
    display: grid;
    justify-content: flex-start;
    height: 100%;
}

button {
    background-color: white;
    color: #088A85;
    border: 2px solid #088A85;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.4s, color 0.4s;
    position: relative;
    overflow: hidden;
}

button::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background-color: #088A85;
    transition: left 0.4s;
    z-index: 1;
    /* 기본 z-index로 설정 */
}

button:hover::before {
    left: 0;
}

button span,
button i {
    position: relative;
    z-index: 2;
    /* 기본 z-index로 설정 */
    color: #088A85;
}

button:hover span,
button:hover i {
    color: white;
}

button:hover {
    color: white;
}

.printBtn,
.modifyBtn,
.addBtn {
    display: flex;
    align-items: center;
    justify-content: center;
}

.printBtn {
    grid-column-start: 1;
    margin: 3px;
    height: 100%;
    min-width: 70px;
    font-size: 12px;
    font-style: bold;
    justify-self: flex-start;
    width: 100%;
    border: none;
    font-weight: bold;
}

.modifyBtn {
    grid-column-start: 3;
    margin: 3px;
    min-width: 70px;
    height: 100%;
    font-size: 12px;
    font-style: bold;
    justify-self: flex-start;
    width: 100%;
    border: none;
    font-weight: bold;
}

.addBtn {
    grid-column-start: 5;
    margin: 3px;
    width: 100%;
    min-width: 70px;
    height: 100%;
    font-size: 12px;
    font-style: bold;
    justify-self: flex-start;
    border: none;
    font-weight: bold;
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
    color: white !important;
    padding: 5px 5px;
    border: 1px solid #088A85;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
    transition: background-color 0.4s, color 0.4s;
    position: relative;
    overflow: hidden;
}

.searchBtn::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background-color: #088A85;
    transition: left 0.4s;
    z-index: 1;
    /* 기본 z-index로 설정 */
}

.searchBtn span,
.searchBtn i {
    position: relative;
    z-index: 2;
    /* 기본 z-index로 설정 */
    color: #088A85;
}

.searchBtn:hover::before {
    left: -100%;
}

.searchBtn:hover span,
.searchBtn:hover i {
    color: #088A85;
}

.searchBtn:hover {
    color: #088A85;
}

.tableContainer {
    grid-row-start: 3;
    grid-column-start: 2;
    grid-column-end: 3;
    margin-top: 30px;
    font-size: 12pt;
    /* 전체 폰트 크기를 15pt로 설정 */
    overflow: auto;
    cursor: pointer;
}

.tableContainer::-webkit-scrollbar {
    width: 0px;
    height: 0px;
}

.tableContainer {
    scrollbar-width: none;
    -ms-overflow-style: none;
}

.tableContainer .table th {
    font-weight: bold;
    padding: 1rem;
    background-color: #f2f2f2;
    color: #a7a4a4;
    border: 1px solid #dddddd;
    text-align: center;
    /* th의 내용을 가운데 정렬 */
}

.tableContainer .table td {
    font-size: 13pt;
    /* td의 폰트 크기를 13pt로 설정 */
    text-align: center;
    vertical-align: middle;
    /* 각 내용물의 세로 가운데 정렬 */
    padding: 1rem;
    border: 1px solid #dddddd;
}

.profile-image {
    width: 25px;
    height: 25px;
    border-radius: 50%;
    object-fit: cover;
}

.pg {
    grid-row-start: 4;
    grid-column-start: 2;
}

.pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 10px;
}

.pagination a {
    color: rgb(124, 122, 122);
    /* 기본 글자색을 검은색으로 설정 */
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
    color: white;
    /* 선택된 버튼의 글자색을 흰색으로 설정 */
}

.badge-class {
    display: flex;
    align-items: center;
    justify-content: center;
}

.guide img {
    width: 21px;
    height: 21px;
    margin: 0 0 0 20px;
    cursor: pointer;
}

.example img {
    width: 100%;
}

.modal-content button {
    background-color: #088A85;
    color: white;
    border: none;
}

.modal-content button:hover::before {
    left: -100%;
    background-color: transparent; /* Remove background transition */
}

.modal-content button:hover span,
.modal-content button:hover i {
    color: white; /* Keep the original color */
}

.modal-content button:hover {
    color: white; /* Keep the original color */
}

</style>
