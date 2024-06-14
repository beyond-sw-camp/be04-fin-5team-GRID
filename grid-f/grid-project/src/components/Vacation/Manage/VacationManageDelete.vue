<template>
    <div class="manageDeleteAll">
        <div class="manageTitle">
            <img class = "deleteIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 수정/삭제</h1>
        </div>
        <div class="deleteMain">
            <div class="vacationType">
                <h3>휴가 종류</h3>
                <p class="titleContent">{{ type.typeName }}</p>
            </div>
            <div class="vacationGiveNum">
                <h3>휴가 제공일수</h3>
                <textarea v-model="type.vacationNum"></textarea>
            </div>
            <div class="vacationPeriod">
                <h3>휴가 사용기한</h3>
                <textarea v-model="type.dateOfUse"></textarea>
            </div>
            <div class="vacationDesc">
                <h3>설명</h3>
                <textarea v-model="type.vacationExplain"></textarea>
            </div>
        </div>
        <div class="buttons">
                <button class="modify" @click="modifyVacationType">수정하기</button>
                <button class="delete" @click="deleteVacationType">삭제하기</button>
        </div>
    </div>
</template>

<script setup>
import { ref, onBeforeMount } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';
import router from '@/router/router';

const type = ref({
    typeName: '',
    vacationNum: '',
    dateOfUse: '',
    vacationExplain: ''
});

const route = useRoute();

const getVacationType = async () => {
    try {
        const response = await axios.get(`/api/vacation/type/${route.params.id}`);
        type.value = response.data.result;
    } catch (error) {
        console.error("Error:", error);
    }
};

const modifyVacationType = async () => {
    try {
        const confirmed = window.confirm('수정하시겠습니까?');
        if (confirmed) {
            const response = await axios.put(`/api/vacation/type/${route.params.id}`, 
            { 
                vacationNum: type.value.vacationNum, 
                dateOfUse: type.value.dateOfUse, 
                vacationExplain: type.value.vacationExplain 
            });
            alert('수정이 완료되었습니다.');
            window.location.reload();
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

const deleteVacationType = async () => {
    try {
        const confirmed = window.confirm('정말로 삭제하시겠습니까?');
        if(confirmed) {
            const response = await axios.delete(`/api/vacation/type/${route.params.id}`);
            alert('삭제가 완료되었습니다.');
            router.go(-1);
        }
    } catch (error) {
        console.error("Error:", error);
    }
};

onBeforeMount(() => {
    getVacationType();
});

</script>

<style scoped>
    .manageDeleteAll {
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

    .deleteIcon {
        width:80%;
    }

    .deleteMain {
        margin-top: 3%;
        margin-bottom: 2%;
        grid-column-start: 2;
        grid-column-end: 3;
        height: 70%;
        width: calc(100% - 20px); /* 부모 요소의 너비에 비례하여 폭을 설정 */
        padding: 10px; /* 내부 여백 추가 */
        background-color: #F2F2F2;
    }

    .deleteMain h3 {
        font-size: 15px;
        margin: 0;
        font-weight: 600;
    }

    .vacationType {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 10% 90%;
        font-size: 14px; 
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
        margin-top: 2%;
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
        grid-template-columns: 40% 8% 4% 8% 40%;
        place-items: center;
        grid-row-start: 3;
        grid-column-start: 2;
    }

    .delete {
        grid-column-start: 4;
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

    .modify {
        grid-column-start: 2;
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

    .titleContent {
        background-color: #F8F9FAFF;
        padding: 10px;
        margin: 0;
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

    .vacationType textarea {
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
</style>