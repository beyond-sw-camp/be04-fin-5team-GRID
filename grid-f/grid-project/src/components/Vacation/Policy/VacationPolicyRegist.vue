<template>
    <div class="policyAll">
        <div class="policyTitle">
            <img class = "policyIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가정책 등록</h1>
        </div>
        <div class="modifyMain">
            <div class="modifyTitle">
                <h3 for="type">타입</h3>
                <select id="type" v-model="selectedType" class="selectField">
                    <option value="">선택해주세요.</option>
                    <option v-for="type in types" :key="type.id" :value="type.id">{{ type.typeName }}</option>
                </select>
            </div>
            <div class="modifyContent">
                <h3>내용</h3>
                <textarea v-model="content" placeholder="내용을 입력해주세요." class="inputField"></textarea>
            </div>
        </div>
        <div class="buttons">
            <button class="regist" @click="registPolicy">등록하기</button>
        </div>
    </div>
</template>

<script setup>
import { ref, onBeforeMount } from 'vue';
import axios from "axios";
import router from '@/router/router';

const types = ref([]);
const selectedType = ref('');
const content = ref('');

const getVacationType = async () => {
    try {
        const response = await axios.get("/api/vacation/type");
        types.value = response.data.result;
    } catch (error) {
        console.error("Error:", error);
    }
};

const registPolicy = async () => {
    try {
        const response = await axios.post("/api/vacation/policy",{
            typeId: selectedType.value,
            content: content.value
        });
        alert('등록이 완료되었습니다.');
        window.location.reload();
        router.go(-1); // 이전 화면으로 이동
    } catch (error) {
        console.error("Error:", error);
    }
};

onBeforeMount(() => {
    getVacationType();
});
</script>

<style scoped>
.policyAll {
    display: grid;
    grid-template-rows: 18% 70% 7% 5%;
    grid-template-columns: 10% 80% 10%;
    height: 100%;
    width: 100%;
}

.policyTitle {
    grid-column-start: 2;
    grid-column-end: 3;
    font-size: 12px;
    font-weight: 600;
    margin-top: 1%;
    color: #000000;
    display: grid;
    grid-template-columns: 3% 97%;
    align-items: center;
}

.policyTitle h1 {
    margin-left:0.5%;
    margin-bottom: 0;
    font-size: 25px;
    font-weight: 600;
}

.policyIcon {
    width: 80%;
}

.modifyMain {
    margin-top: 3%;
    margin-bottom: 2%;
    font-size: 20px;
    grid-column-start: 2;
    grid-column-end: 3;
    height: 80%;
    width: calc(100% - 20px); 
    padding: 10px; 
    background-color: #F2F2F2;
}

.modifyMain h3 {
    font-size: 15px;
    margin: 0;
    font-weight: 600;
}

.modifyTitle {
    margin-top: 2%;
    display: grid;
    grid-template-columns: 5% 20% 75%;
    font-size: 14px;
    align-items: center;
}

.modifyContent {
    margin-top: 3%;
    height: 50%;
    display: grid;
    grid-template-columns: 5% 95%;
    font-size: 14px;
}

.modifyContent textarea {
    width: 100%;
    height: 100%;
    padding: 10px;
    box-sizing: border-box;
    resize: none; /* textarea 크기 변경 비활성화 */
    height: 300px; /* 고정 높이 설정 */
    border: none;
}

.modifyContent h3 {
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

.selectField {
    height: 100%;
    border: none;
    padding-left: 10px; 
}
</style>
