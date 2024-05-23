<template>
    <div class="policyAll">
        <div class="policyTitle">
            <img class="policyIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가정책</h1>
            <button class="policyRegist" @click="registPolicy()">등록하기</button>
        </div>
        <div class="policyContent">
            <div class="policyBox" v-for="policy in policies" :key="policy.id">
                <div class="policyHeader">
                    <h3 class="policyName">{{ policy.typeName }}</h3>
                    <img class="policyModify" src="@/assets/buttons/edit.png" @click="modifyPolicy(policy.id)">
                </div>
                <h5 class="policies" v-html="policy.content"></h5>
            </div>
        </div>
    </div>
</template>

<script setup>
import router from '@/router/router';
import { onBeforeMount, ref } from 'vue';
import axios from "axios";

const policies = ref([]);

const getAllVacationPolicy = async () => {
    try {
        const response = await axios.get("/api/vacation/policy/all");
        policies.value = response.data.result;
        console.log(response.data.result);
    } catch (error) {
        console.error("Error:", error);
    }
};

function modifyPolicy(id) {
    router.push(`/vacation/policy/modify/${id}`);
}

function registPolicy() {
    router.push('/vacation/policy/regist');
}

onBeforeMount(() => {
    getAllVacationPolicy();
});
</script>

<style scoped>
.policyAll {
    display: grid;
    grid-template-rows: 18% minmax(80%,auto) 2%;
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
    grid-template-columns: 3% 92% 5%;
    align-items: center;
}

.policyTitle h1 {
    margin-left:0.5%;
}

.policyIcon {
    width: 80%;
}

.policyName {
    margin-top: 0;
    margin-bottom: 0;
    font-size: 15px;
    grid-column-start: 1;
    grid-column-end: 2;
}

.policyContent {
    margin-top: 2%;
    margin-bottom: 0;
    grid-column-start: 2;
    grid-column-end: 3;
    font-size: 15px;
}

.policies {
    margin-top: 0;
    margin-bottom: 0;
    white-space: pre-wrap; /* 공백과 줄 바꿈을 유지 */
}

.policyBox {
    width: calc(100% - 20px); /* 부모 요소의 너비에 비례하여 폭을 설정 */
    padding: 10px; /* 내부 여백 추가 */
    margin-top: 10px;
    margin-bottom: 0;
    background-color: #F2F2F2;
}

.policyRegist {
    background-color: #088A85;
    color: white;
    padding: 5px 5px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 11px;
    font-style: bold;
    width: 100%;
}

.policyHeader {
    display: grid;
    grid-template-columns: 97% 3%;
    align-items: center;
    margin-bottom: 2%;
}

.policyModify {
    justify-self: end;
    cursor: pointer;
    width: 70%;
}
</style>
