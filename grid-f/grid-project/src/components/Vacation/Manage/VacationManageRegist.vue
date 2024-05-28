<template>
    <div class="manageRegistAll">
        <div class="manageTitle">
            <img class = "registIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 등록</h1>
        </div>
        <div class="registMain">
            <div class="vacationType">
                <h3>휴가 종류</h3>
                <textarea v-model="typeName"></textarea>
            </div>
            <div class="vacationGiveNum">
                <h3>휴가 제공일수</h3>
                <textarea v-model="vacationNum"></textarea>
            </div>
            <div class="vacationPeriod">
                <h3>휴가 사용기한</h3>
                <textarea v-model="dateOfUse"></textarea>
            </div>
            <div class="vacationDesc">
                <h3>설명</h3>
                <textarea v-model="vacationExplain"></textarea>
            </div>
        </div>
        <div class="buttons">
            <button class="regist" @click="registType">등록하기</button>
        </div>
    </div>
</template>

<script setup>
import { ref, onBeforeMount } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';
import router from '@/router/router';

const typeName = ref('');
const vacationNum = ref('');
const dateOfUse = ref('');
const vacationExplain = ref('');

const route = useRoute();

const registType = async () => {
    try {
        const response = await axios.post("/api/vacation/type",
        {
            typeName: typeName.value,
            vacationNum: vacationNum.value,
            dateOfUse: dateOfUse.value,
            vacationExplain: vacationExplain.value
        });
        alert('등록이 완료되었습니다.');
        window.location.reload();
        router.go(-1); // 이전 화면으로 이동
    } catch (error) {
        console.error("Error:", error);
    }
};
</script>

<style scoped>
    .manageRegistAll {
        display: grid;
        grid-template-rows: 18% 65% 12% 5%;
        grid-template-columns: 10% 80% 10%;
        height: 100%;
    }

    .manageTitle {
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

    .manageTitle h1 {
        margin-left:0.5%;
        margin-bottom: 0;
        font-size: 25px;
        font-weight: 600;
    }

    .registIcon {
        width:80%;
    }

    .registMain {
        margin-top: 3%;
        margin-bottom: 2%;
        grid-column-start: 2;
        grid-column-end: 3;
        height: 70%;
        width: calc(100% - 20px); /* 부모 요소의 너비에 비례하여 폭을 설정 */
        padding: 10px; /* 내부 여백 추가 */
        background-color: #F2F2F2;
    }

    .registMain h3 {
        font-size: 15px;
        margin: 0;
        font-weight: 600;
    }

    .vacationType textarea {
        width: 100%;
        height: 100%;
        padding: 10px;
        box-sizing: border-box;
        resize: none; /* textarea 크기 변경 비활성화 */
        height: 50px; /* 고정 높이 설정 */
        border: none;
    }

    .vacationType {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 10% 90%;
        font-size: 14px;

    }

    .vacationGiveNum textarea {
        width: 100%;
        height: 100%;
        padding: 10px;
        box-sizing: border-box;
        resize: none; /* textarea 크기 변경 비활성화 */
        height: 50px; /* 고정 높이 설정 */
        border: none;
    }

    .vacationPeriod textarea {
        width: 100%;
        height: 100%;
        padding: 10px;
        box-sizing: border-box;
        resize: none; /* textarea 크기 변경 비활성화 */
        height: 50px; /* 고정 높이 설정 */
        border: none;
    }

    .vacationDesc textarea {
        width: 100%;
        height: 100%;
        padding: 10px;
        box-sizing: border-box;
        resize: none; /* textarea 크기 변경 비활성화 */
        height: 100px; /* 고정 높이 설정 */
        border: none;
    }

    .vacationGiveNum {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 10% 90%;
        font-size: 14px;
    }

    .vacationPeriod {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 10% 90%;
        font-size: 14px;

    }

    .vacationDesc {
        margin-top: 3%;
        height: 40%;
        display: grid;
        grid-template-columns: 10% 90%;
        font-size: 14px;
    }

    .vacationDesc h3 {
        margin-top: 0;
    }

    .buttons {
        display: grid;
        grid-template-columns: 40% 6% 8% 6% 40%;
        place-items: center;
        grid-row-start: 3;
        grid-column-start: 2;
    }

    .regist {
        grid-column-start: 3;
        width: 100%;
        background-color: #088A85;
        color: white;
        padding: 10px 10px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 12px;
        font-style: bold;
    }

    .inputField {
        padding-left: 10px; 
    } 
</style>