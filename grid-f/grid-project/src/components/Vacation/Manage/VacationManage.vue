<template>
    <div class="manageAll">
        <div class="manageTitle">
            <img class = "manageIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 관리</h1>
        </div>
        <div class="manageRegist">
            <button class="manageBtn" type="button" @click="registType()">등록하기</button>
        </div>
        <div class="vacations">
            <div class="typeBox" v-for="type in types" :key="type.id">
                <div class="vacationsTitle">
                    <h3>{{ type.typeName }}</h3>
                    <img class="plusBtn" @click="modifyType(type.id)" src="@/assets/buttons/plus.png">
                </div>
                <div class="vacationsNum">
                    <h3>{{ type.vacationExplain }}</h3>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import router from '@/router/router';
import { onBeforeMount, ref } from 'vue';
import axios from "axios";

const types = ref([]);

const getAllVacationType = async () => {
    try {
        const response = await axios.get("/api/vacation/type");
        types.value = response.data.result;
        console.log(response.data.result);
    } catch (error) {
        console.error("Error:", error);
    }
};

function modifyType(id) {
    router.push(`/vacation/manage/modify/${id}`);
}

function registType() {
    router.push('/vacation/manage/regist');
}

onBeforeMount(() => {
    getAllVacationType();
});
</script>

<style scoped>
    .manageAll {
        display: grid;
        grid-template-rows: 18% 4% minmax(75%,auto) 3% ;
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
        grid-template-columns: 3% 92% 5%;
        align-items: center;
    }

    .manageTitle h1 {
        margin-left:0.5%;
    }

    .manageIcon {
        width:80%;
    }

    .manageRegist {
        display: grid;
        grid-template-columns: 87% 8% 5%;
        grid-column-start:2 ;
    }

    .manageBtn {
        grid-column-start: 3;
        width: 100%;
        background-color: #088A85;
        color: white;
        padding: 5px 5px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 11px;
        font-style: bold;
    }

    .vacations {
        margin-top: 2%;
        grid-column-start: 2;
        grid-column-end: 3;
        grid-row: 3;
        display: grid;
        grid-template-columns: 1fr 1fr 1fr 1fr; /* 3개의 열로 구성 */
        grid-auto-rows: 150px; /* 행의 최소 크기 설정 */
        grid-gap: 10px; /* 아이템 간의 간격 설정 */
        grid-auto-flow: row dense; /* 아이템을 빈 공간에 자동으로 채우도록 설정 */
    }

    .vacationsTitle {
        display: grid;
        grid-template-columns: 90% 10%;
        align-items: center;
    }

    .vacationsTitle p {
        margin: 0;
    }

    .plusBtn {
        width: 100%;
    }

    .typeBox {
        padding: 10px;
        background-color: #F2F2F2;
    }

    .vacationsNum {
        grid-column-start: 1;
        display: grid;
        grid-template-columns:90% 10%;
        font-size: 10px;
        margin-top:10%;
    }
</style>
        