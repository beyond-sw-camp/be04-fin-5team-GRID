<template>
    <div class="profile-main">
        <div class="profile-title">
            <img class="profile-icon" src="@/assets/profile.png" alt="인사 정보 메인 이미지">
            <h1>인사 정보 수정</h1>
        </div>
        <div class="first">
            <div class="image">
                <img src="https://talkimg.imbc.com/TVianUpload/tvian/TViews/image/2020/04/12/FydNALvKf23Z637223013461671479.jpg" alt="">
            </div>
            <div class="name">
                <div id="name">
                    <input v-model="user.name" type="text" />
                </div>
                <div id="other-info">
                    <div id="teamInfo">
                        <select v-model="user.teamId">
                            <option v-for="team in teams" :key="team.id" :value="team.id">
                                {{ team.teamName }}
                            </option>
                        </select>
                    </div>
                    <div id="dot">•</div>
                    <div id="dutiesInfo">
                        <select v-model="user.dutiesId">
                            <option v-for="duty in duties" :key="duty.id" :value="duty.id">
                                {{ duty.dutiesName }}
                            </option>
                        </select>
                    </div>
                    <div id="dot">•</div>
                    <div id="absenceInfo">
                        <select v-model="user.absenceYn">
                            <option value="Y">부재중</option>
                            <option value="N">재실</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="button" v-if="userRole === 'ROLE_ADMIN'">
                <div style="margin-right: 2%;">
                    <button class="pwdBtn" data-bs-toggle="modal" data-bs-target="#myModal">비밀번호 변경</button>
                </div>
                <div>
                    <button class="modifyBtn" @click="submitForm">수정 내용 저장</button>
                </div>
            </div>
        </div>
        <div class="second">
            <ul class="nav nav-tabs">
                <li class="nav-item" @click="currentTab = 'human-resources'">
                    <a class="nav-link" :class="{ active: currentTab === 'human-resources' }" href="#">정보</a>
                </li>
                <li class="nav-item" @click="currentTab = 'wb'">
                    <a class="nav-link" :class="{ active: currentTab === 'wb' }" href="#">근무/휴가</a>
                </li>
            </ul>
        </div>
        <div class="content">
            <component :is="currentTabComponent" :result="result" :userRole="userRole"></component>
        </div>
    </div>
    <div class="modal fade" id="myModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">유저 비밀번호 변경</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <ResetPwd :givenEmail="givenEmail" @passwordResetSuccess="handlePasswordResetSuccess" />
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import HumanResourcesInfo from '@/components/HumanResources/HumanResourcesInfo.vue';
import WB from '@/components/HumanResources/WorkVacation.vue';
import ResetPwd from '@/components/Login/ResetPassword.vue';
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const currentTab = ref('human-resources');
const route = useRoute();
const result = ref('');
const isAbsence = ref(false);
const userRole = ref('');
const givenEmail = ref('');
const user = ref({}); // to hold the user data for form editing
const departments = ref([]);
const teams = ref([]);
const positions = ref([]);
const duties = ref([]);

const tabComponents = {
    'human-resources': HumanResourcesInfo,
    'wb': WB,
};

const currentTabComponent = computed(() => tabComponents[currentTab.value]);

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

function handlePasswordResetSuccess() {
    const myModal = new bootstrap.Modal(document.getElementById('myModal'));
    myModal.hide();
}

async function fetchDropdownData() {
    const departmentResponse = await axios.get('/api/departments');
    const teamResponse = await axios.get('/api/teams');
    const positionResponse = await axios.get('/api/positions');
    const dutiesResponse = await axios.get('/api/duties');
    departments.value = departmentResponse.data;
    teams.value = teamResponse.data;
    positions.value = positionResponse.data;
    duties.value = dutiesResponse.data;
}

async function submitForm() {
    try {
        await axios.put(`http://localhost:8080/users/${route.params.employeeNumber}`, user.value);
        alert('수정 내용이 저장되었습니다.');
    } catch (error) {
        alert('수정 내용 저장에 실패했습니다.');
        console.error(error);
    }
}

onMounted(async () => {
    const token = localStorage.getItem('access');
    if (token) {
        const decodedToken = parseJwt(token);
        userRole.value = decodedToken?.auth || '';
    }

    const response = await axios.get(`http://localhost:8080/users/${route.params.employeeNumber}`);
    result.value = response.data.result;
    user.value = response.data.result; // initialize user data for form
    if (result.value.absenceYn === 'Y') {
        isAbsence.value = true;
    }

    givenEmail.value = result.value.email;
    console.log("result: ", result);
    console.log("result2: ", givenEmail.value);

    await fetchDropdownData(); // fetch dropdown data after loading user data
});
</script>

<style scoped>
@font-face {
    font-family: 'IBMPlexSansKR-Regular';
    src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}

body {
    font-family: 'IBMPlexSansKR-Regula';
    min-height: 100vh;
    min-width: 100vw;
}

.profile-main {
    display: grid;
    grid-template-columns: 10% 80% 10%;
    grid-template-rows: 18% 33% 15% auto;
    height: 100%;
}

.profile-title {
    grid-column-start: 2;
    grid-column-end: 3;
    margin-top: 2%;
    color: #000000;
    display: grid;
    grid-template-columns: 3% 97%;
    align-items: center;
}

.profile-title h1 {
    margin-left: 1.2%;
    font-weight: bold;
    font-size: 14pt;
}

.profile-icon {
    width: 130%;
    margin: 0 40px 10px 0;
    filter: invert(0%) sepia(64%) saturate(7%) hue-rotate(334deg) brightness(85%) contrast(101%);
}

.first {
    grid-row-start: 2;
    grid-column-start: 2;
    display: grid;
    grid-template-columns: 17% 53% 30%;
    max-height: 100%;
    max-width: 100%;
}

.image {
    grid-column-start: 1;
    padding: 0.5em;
    height: 100%;
    width: 100%;
}

.image img {
    width: 100%;
    height: 100%;
}

.name {
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    padding: 0.5em;
    height: 100%;
    width: 100%;
}

#name {
    margin-bottom: 1%;
    font-weight: bold;
}

#other-info {
    display: flex;
    flex-direction: row;
}

#teamInfo {
    font-size: 13px;
}

#dutiesInfo {
    font-size: 13px;
}

#dot {
    margin: auto 9px;
    font-size: 10px;
}

#absenceInfo {
    font-size: 13px;
}

.button {
    display: flex;
    justify-content: flex-end;
}

.modifyBtn {
    margin-left: 2%;
    width: 50%;
    min-width: 102.24px;
    min-height: 28px;
    background-color: #088A85;
    color: white;
    padding: 5px 5px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
}

.pwdBtn {
    margin-left: 2%;
    margin-right: 4%;
    width: 50%;
    min-width: 102.24px;
    min-height: 28px;
    background-color: #088A85;
    color: white;
    padding: 5px 5px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
}

.second {
    grid-row-start: 3;
    grid-column-start: 2;
    height: 100%;
    width: 100%;
    display: flex;
    align-items: flex-end;
}

.content {
    grid-row-start: 4;
    grid-column-start: 2;
    margin-top: 15px;
}

.modal-body {
    padding: 0 0 50px 0;
    height: 50%;
}
</style>
