<template>
    <div class="profile-main">
        <div class="profile-title">
            <img class="profile-icon" src="@/assets/profile.png" alt="인사 정보 메인 이미지">
            <h1>인사 정보 수정 </h1>
        </div>
        <div class="first" v-if="user">
            <div class="image">
                <img src="https://talkimg.imbc.com/TVianUpload/tvian/TViews/image/2020/04/12/FydNALvKf23Z637223013461671479.jpg"
                    alt="">
            </div>  
            <div class="button">
                <div style="margin-right: 2%;">
                    <button class="pwdBtn" data-bs-toggle="modal" data-bs-target="#myModal">비밀번호 변경</button>
                </div>
                <div>
                    <button class="modifyBtn" @click="submitModifications">수정</button>
                </div>
            </div>
        </div>
        <div class="second">
            <div class="oldInfo">
                기존 정보
            </div>
            <div class="newInfo">
                변경 정보
            </div>        
        </div>
        <div class="content">
            <ModifyInfo :user="user"/>
        </div>
    </div>
</template>

<script setup>
import ModifyInfo from '@/components/HumanResources/ModifyInfo.vue';
import ResetPwd from '@/components/Login/ResetPassword.vue';
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const user = ref();
const name = ref('');
const updatedUser = ref(null);

const cleanUserData = (userData) => {
    return {
        email: userData.email,
        pwd: userData.pwd,
        name: userData.name,
        employeeNumber: userData.employeeNumber,
        phoneNumber: userData.phoneNumber,
        callNumber: userData.callNumber,
        zipCode: userData.zipCode,
        address: userData.address,
        assignedTask: userData.assignedTask,
        joinTime: userData.joinTime,
        joinType: userData.joinType,
        workType: userData.workType,
        resignYn: userData.resignYn,
        resignedTime: userData.resignedTime,
        departmentId: userData.departmentId,
        teamId: userData.teamId,
        positionId: userData.positionId,
        dutiesId: userData.dutiesId,
    };
};

const submitModifications = async () => {
    try {
        console.log('변경될 정보 확인: ', updatedUser.value);
        const cleanedData = cleanUserData(updatedUser.value);
        console.log('변경될 정보 확인22: ', cleanedData);
        const response = await axios.put(`http://localhost:8080/users/${user.value.id}`, cleanedData);
        alert("수정이 완료되었습니다.");
        router.push(`/hr/profile/${user.employeeNumber}`);
    } catch (error) {
        console.error("수정 중 오류 발생: ", error);
        alert("수정 중 오류가 발생했습니다.");
    }
};

const updateUser = (newData) => {
    updatedUser.value = { 
        ...user.value, 
        ...newData,
        departmentId: newData.departmentId !== undefined ? newData.departmentId : user.value.department?.id,
        teamId: newData.teamId !== undefined ? newData.teamId : user.value.team?.id,
        positionId: newData.positionId !== undefined ? newData.positionId : user.value.position?.id,
        dutiesId: newData.dutiesId !== undefined ? newData.dutiesId : user.value.duties?.id,
    };
};

onMounted(() => {
    const userData = sessionStorage.getItem('user');
    if (userData) {
        const parsedUser = JSON.parse(userData);
        if (parsedUser && parsedUser.id) {
            user.value = parsedUser;
            updatedUser.value = { ...user.value };
            console.log("유저 정보 확인: ", user.value);
        } else {
            console.error("유저 정보가 올바르지 않습니다. ID가 없습니다.");
        }
    } else {
        console.error("SessionStorage에 유저 정보가 없습니다.");
    }
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
    grid-template-rows: 18% 33% 10% auto;
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
    grid-template-columns: 17% 23% 30% 30%;
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
    grid-column-start: 4;
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

.modifyFirst {
    grid-column-start: 3;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    padding: 0.5em;
    height: 100%;
    width: 100%;
}

.second {
    grid-row-start: 3;
    grid-column-start: 2;
    height: 100%;
    width: 100%;
    display: grid;
    align-items: flex-end;
    grid-template-columns: 25% auto 45%;
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

.oldInfo {
    grid-column-start: 2;
    text-align: center;
    color: rgb(180, 177, 177);
    font-weight: bold;
}

.newInfo {
    grid-column-start: 3;
    text-align: center;
    color: rgb(180, 177, 177);
    font-weight: bold;
}
</style>