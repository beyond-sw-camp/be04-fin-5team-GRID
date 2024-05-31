<template>
    <div class="profile-main">
        <div class="profile-title">
            <img class="profile-icon" src="@/assets/profile.png" alt="인사 정보 메인 이미지">
            <h1>인사 정보 상세 </h1>
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
                        {{ isAbsence ? '부재중' : '재실' }}
                    </div>
                </div>
            </div>
            <div class="button" v-if="userRole === 'ROLE_ADMIN'">
                <div>
                    <button class="modifyBtn" @click="toModify(result.employeeNumber, result)">회원 정보 수정</button>
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
</template>

<script setup>
import HumanResourcesInfo from '@/components/HumanResources/HumanResourcesInfo.vue';
import WB from '@/components/HumanResources/WorkVacation.vue';
import defaultProfileImage from '@/assets/defaultProfile.jpg';

import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';

const currentTab = ref('human-resources');
const route = useRoute();
const result = ref({});
const isAbsence = ref(false);
const userRole = ref('');
const givenEmail = ref('');
const router = useRouter();

const profileUrl = computed(() => {
    return result.value.profilePath ? result.value.profilePath : defaultProfileImage;
});

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

function toModify(employeeNumber, user) {
    sessionStorage.setItem('user', JSON.stringify(user));
    router.push({
        path: `/hr/modify/${employeeNumber}`
    });
}

onMounted(async () => {
    const token = localStorage.getItem('access');
    if (token) {
        const decodedToken = parseJwt(token);
        userRole.value = decodedToken?.auth || '';
        console.log('토큰:', decodedToken.auth);
    }

    const response = await axios.get(`http://localhost:8080/users/${route.params.employeeNumber}`);
    result.value = response.data.result;
    if (result.value.absenceYn === 'Y') {
        isAbsence.value = true;
    }
    console.log(result.value.profilePath);
    console.log('온마운트결과:', result.value);
    givenEmail.value = result.value.email;
}

)
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
    grid-template-columns: 23% 47% 30%;
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