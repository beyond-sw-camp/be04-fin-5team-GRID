<template>
    <div class="profile-main">
        <div class="profile-title">
            <nav class="title-nav" style="--bs-breadcrumb-divider: '>'; font-weight: normal;" aria-label="breadcrumb">
                <ol class="breadcrumb" style="margin-bottom: 0;">
                    <li class="breadcrumb-item"><a href="http://www.gridhr.site/hr"
                            style="text-decoration: none; color: grey; font-size: 17px;"><i
                                class="bi bi-people"></i>&nbsp; 인사 정보</a></li>
                    <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i
                                class="bi bi-file-person"></i>&nbsp; 인사 정보 상세</span></li>
                </ol>
            </nav>
            <div class="title-content">
                <h1><i class="bi bi-file-person"></i> &nbsp 인사 정보 상세 </h1>
            </div>
        </div>
        <div class="first">
            <div class="image">
                <img :src="profileUrl" alt="프로필 이미지">
            </div>
            <div class="name">
                <div id="name">
                    {{ result.name }}
                </div>
                <div id="other-info">
                    <div id="teamInfo">
                        {{ result.team ? result.team.teamName : 'N/A' }}
                    </div>
                    <div id="dot">•</div>
                    <div id="dutiesInfo">
                        {{ result.duties ? result.duties.dutiesName : 'N/A' }}
                    </div>
                    <div id="dot">•</div>
                    <div id="absenceInfo">
                        <b-badge v-if="isAbsence" variant="danger">부재중</b-badge>
                        <b-badge v-else variant="success">재실중</b-badge>
                    </div>
                </div>
                <div style="display: flex;">
                    <div>
                        <b-badge v-if="result.absenceStartTime" variant="light">{{ result.absenceContent }} : &nbsp; {{
                            result.absenceStartTime }} - {{ result.absenceEndTime }} </b-badge>
                        <b-badge v-if="result.resignYn === 'Y'" variant="danger"> 퇴사 </b-badge>
                    </div>
                </div>
            </div>

            <div class="button" v-if="userRole === 'ROLE_ADMIN' || userId == result.id">
                <div>
                    <button class="custom-button" @click="toModify(result.employeeNumber, result)" :userRole="userRole">
                        <i class="bi bi-gear"></i>&nbsp;
                        <span>회원 정보 수정</span>
                    </button>
                </div>
            </div>
        </div>
        <div class="second">
            <div style="width: 50%;">
                <b-nav pills>
                    <b-nav-item
                        :class="[currentTab === 'human-resources' ? 'active-tab' : 'inactive-tab', 'tab-button']"
                        @click="navigateToTab('human-resources')">
                        <div style="color: white;">정보</div>
                    </b-nav-item>
                    <b-nav-item :class="[currentTab === 'wb' ? 'active-tab' : 'inactive-tab', 'tab-button']"
                        @click="navigateToTab('wb')" style="margin-left: 2%;">
                        <div style="color: white;"> 근무/휴가 </div>
                    </b-nav-item>
                </b-nav>
            </div>
        </div>
        <div class="content">
            <component :is="currentTabComponent" :result="result" :userRole="userRole" :userId="userId" :profileId="profileId">
            </component>
        </div>
    </div>
</template>

<script setup>
import HumanResourcesInfo from '@/components/HumanResources/HumanResourcesInfo.vue';
import WB from '@/components/AdTime/WorkCalendar.vue';
import defaultProfileImage from '@/assets/defaultProfile.jpg';

import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';

const currentTab = ref('');
const route = useRoute();
const result = ref({});
const isAbsence = ref(false);
const userRole = ref('');
const userId = ref('');
const givenEmail = ref('');
const router = useRouter();
const profileId = ref('');

const profileUrl = computed(() => {
    return result.value.profilePath ? result.value.profilePath : defaultProfileImage;
});

const tabComponents = {
    'human-resources': HumanResourcesInfo,
    'wb': WB,
};

const currentTabComponent = computed(() => tabComponents[currentTab.value]);

function navigateToTab(tab) {
    currentTab.value = tab;
    if (tab === 'wb') {
        router.push({
            query: { tab: tab, profileId: result.value.id }
        });
    } else {
        router.push({ query: { tab: tab } });
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

function formatDateString(dateString) {

    const date = new Date(dateString);
    const year = date.getFullYear().toString().slice(2);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}.${month}.${day}`;
}

function toModify(employeeNumber, user) {
    sessionStorage.setItem('user', JSON.stringify(user));

    router.push({
        path: `/hr/modify/${employeeNumber}`,
        query: { userRole: userRole.value }
    });
}

onMounted(async () => {
    const token = localStorage.getItem('access');
    if (token) {
        const decodedToken = parseJwt(token);
        userRole.value = decodedToken?.auth || '';
        userId.value = decodedToken?.id || '';
    }

    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/${route.params.employeeNumber}`);
    result.value = response.data.result;
    if (result.value.absenceYn === 'Y') {
        isAbsence.value = true;
    }

    givenEmail.value = result.value.email;
    profileId.value = result.value.id;
    currentTab.value = route.query.tab || 'human-resources';
});
</script>

<style scoped>
@import '@/assets/css/button-styles.css';

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
    grid-template-columns: 22% 56% 22%;
    grid-template-rows: 18% 33% 15% 80%;
    height: 100%;
}

.tab-button {
    width: 110px;
    /* 고정된 너비 */
    height: 30px;
    text-align: center;
    font-weight: bold;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.active-tab {
    background-color: #088A85;
    color: white !important;
}

.inactive-tab {
    background-color: #c1dcdb;
    /* 비활성화된 탭의 배경색 */
    color: black !important;
}

.b-nav-item:hover {
    cursor: pointer;
}

.b-nav-item .nav-link {
    font-weight: bold;
    /* 볼드체 적용 */
    text-decoration: none;
    /* 기본 밑줄 제거 */
    padding: 0;
}

.profile-title {
    grid-column-start: 2;
    grid-column-end: 3;
    margin-top: 2%;
    color: #000000;
    display: grid;
    align-items: center;
    grid-template-rows: 20% 80%;
}

.oaHeader {
    grid-column-start: 2;
    align-content: center;
    margin-top: 2%;
}

.oaHeader h1 {
    margin-left: 0.5%;
    font-size: 25px;
    font-weight: 600;
}

.profile-title h1 {
    font-weight: bold;
    font-size: 14pt;
}

.first {
    grid-row-start: 2;
    grid-column-start: 2;
    display: grid;
    grid-template-columns: 27% 3% 40% 30%;
    max-height: 100%;
    max-width: 100%;
    height: 300px;
    /* 고정 높이 설정 */
}

.image {
    border-radius: 18%;
    /* 이미지 컨테이너 둥근 모서리 */
    grid-column-start: 1;
    padding: 0;
    height: 100%;
    width: 100%;
    overflow: hidden;
    /* 이미지가 컨테이너를 넘지 않도록 설정 */
}

.image img {
    border-radius: 18%;
    /* 이미지 둥근 모서리 */
    width: 100%;
    height: 100%;
}

.name {
    grid-column-start: 3;
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
    grid-column-start: 4;
    display: flex;
    justify-content: flex-end;
}

.custom-btn {
    margin-left: 2%;
    width: 50%;
    min-width: 110px;
    min-height: 28px;
    /* custom button 스타일 */
    border: none;
    font-weight: bold;
    font-size: 12px;
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
    grid-column-end: 3;
    height: 100%;
    width: 100%;
    display: flex;
    align-items: flex-end;
}

.content {
    grid-row-start: 4;
    grid-column-start: 2;
    margin-top: 15px;
    overflow-y: auto;
    /* 스크롤 가능하게 설정 */
    min-height: 750px;
    /* 고정 높이 설정 */
}

.modal-body {
    padding: 0 0 50px 0;
    height: 50%;
}

.title-nav {
    display: grid;
    align-items: flex-end;
    width: 100%;
}

.title-content {
    display: grid;
    align-items: flex-start;
    height: 80%;
}
</style>
