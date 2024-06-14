<template>
    <div class="policyAll">
        <div class="policyTitle">
            <img class="policyIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가정책 수정</h1>
        </div>
        <div class="modifyMain">
            <div class="modifyTitle">
                <h3>타입</h3>
                <p class="titleContent">{{ policies.typeName }}</p>
            </div>
            <div class="modifyContent">
                <h3>내용</h3>
                <textarea v-model="plainContent"></textarea>
            </div>
        </div>
        <div class="modifyButtons">
            <button class="modify" @click="modifyContent">수정</button>
            <button class="delete" @click="deleteVacationPolicy">삭제</button>
        </div>
    </div>
</template>

<script setup>
import { ref, onBeforeMount, watch } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';
import router from '@/router/router';

const policies = ref({
    typeName: '',
    content: ''
});
const route = useRoute();
const plainContent = ref('');

const getVacationPolicy = async () => {
    try {
        const response = await axios.get(`/api/vacation/policy/${route.params.id}`);
        policies.value = response.data.result;
        plainContent.value = policies.value.content.replace(/<br\s*\/?>/gi, '\n');
    } catch (error) {
        console.error("Error:", error);
    }
};

const modifyContent = async () => {
    try {
        const confirmed = window.confirm('수정하시겠습니까?');
        if(confirmed) {
            policies.value.content = plainContent.value.replace(/\n/g, '<br>');
            const response = await axios.put(`/api/vacation/policy/${route.params.id}`, { content: policies.value.content });
            alert('수정이 완료되었습니다.');
            window.location.reload();
        }
    } catch (error) {
        console.error("Error:", error);
    }
};

const deleteVacationPolicy = async () => {
    try {
        const confirmed = window.confirm('정말로 삭제하시겠습니까?');
        if(confirmed) {
            const response = await axios.delete(`/api/vacation/policy/${route.params.id}`);
            alert('삭제가 완료되었습니다.');
            router.go(-1); // 이전 화면으로 이동
        }
    } catch (error) {
        console.error("Error:", error);
    }
};

watch(plainContent, (newContent) => {
    policies.value.content = newContent.replace(/\n/g, '<br>');
});

onBeforeMount(() => {
    getVacationPolicy();
});
</script>

<style scoped>
.policyAll {
    display: grid;
    grid-template-rows: 18% 70% 7% 5%;
    grid-template-columns: 10% 80% 10%;
    height: 100%;
}

.policyTitle {
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
    grid-template-columns: 5% 95%;
    font-size: 14px;
    align-items: center;
}

.titleContent {
    background-color: #F8F9FAFF;
    padding: 10px;
    margin-bottom: 0;
}

.modifyContent {
    margin-top: 3%;
    height: 50%;
    display: grid;
    grid-template-columns: 5% 95%;
    font-size: 14px;
}

.modifyContent h3 {
    margin-top: 0;
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

.modifyButtons {
    display: grid;
    grid-template-columns: 40% 8% 4% 8% 40%;
    place-items: center;
    grid-row-start: 3;
    grid-column-start: 2;
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
</style>
