<template>
    <div class="manageAll">
        <div class="manageTitle">
            <img class = "manageIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 관리</h1>
        </div>
        <div class="manageRegist">
            <button class="manageBtn" type="button" @click="showRegistModal = true">등록하기</button>
        </div>
        <div class="vacations">
            <div class="typeBox" v-for="type in types" :key="type.id">
                <div class="vacationsTitle">
                    <h3>{{ type.typeName }}</h3>
                    <img class="plusBtn" @click="openModifyModal(type.id)" src="@/assets/buttons/plus.png">
                </div>
                <div class="vacationsNum">
                    <h3>{{ type.vacationExplain }}</h3>
                </div>
            </div>
        </div>

        <!-- 등록 모달 -->
        <Modal v-if="showRegistModal" @close="showRegistModal = false">
            <div class="registMain">
                <div class="registTitle">
                    <h3>휴가 이름</h3>
                    <textarea v-model="registVacationType.typeName"></textarea>
                </div>
                <div class="registNum">
                    <h3>휴가 제공일수</h3>
                    <textarea v-model="registVacationType.vacationNum"></textarea>
                </div>
                <div class="registPeriod">
                    <h3>휴가 사용기한</h3>
                    <textarea v-model="registVacationType.dateOfUse"></textarea>
                </div>
                <div class="registDesc">
                    <h3>설명</h3>
                    <textarea v-model="registVacationType.vacationExplain"></textarea>
                </div>
                <div class="registBtnArea">
                    <button class="registBtn" @click="registType">등록하기</button>
                </div>
            </div>
        </Modal>

        <!-- 수정 모달 -->
        <Modal v-if="showModifyModal" @close="showModifyModal = false">
                <div class="modifyMain">
                    <div class="vacationType">
                        <h3>휴가 종류</h3>
                        <p class="titleContent">{{ modifyType.typeName }}</p>
                    </div>
                    <div class="modifyNum">
                        <h3>휴가 제공일수</h3>
                        <textarea v-model="modifyType.vacationNum"></textarea>
                    </div>
                    <div class="modifyPeriod">
                        <h3>휴가 사용기한</h3>
                        <textarea v-model="modifyType.dateOfUse"></textarea>
                    </div>
                    <div class="modifyDesc">
                        <h3>설명</h3>
                        <textarea v-model="modifyType.vacationExplain"></textarea>
                    </div>
                    <div class="modifyBtnArea">
                        <button class="modify" @click="modifyVacationType(modifyType.id)">수정하기</button>
                        <button class="delete" @click="deleteVacationType(modifyType.id)">삭제하기</button>
                    </div>
                </div>
                
         </Modal>
    </div>
</template>

<script setup>
import router from '@/router/router';
import { onBeforeMount, ref } from 'vue';
import axios from "axios";
import Modal from '@/components/Vacation/Manage/VacationManageModal.vue';

const types = ref([]);
const showRegistModal = ref(false);
const showModifyModal = ref(false);
const typeName = ref('');
const vacationNum = ref('');
const dateOfUse = ref('');
const vacationExplain = ref('');
const modifyType = ref({
    id: '',
    typeName: '',
    vacationNum: '',
    dateOfUse: '',
    vacationExplain: ''
});
const registVacationType = ref({
    id: '',
    typeName: '',
    vacationNum: '',
    dateOfUse: '',
    vacationExplain: ''
});

const getVacationType = async (id) => {
    try {
        const response = await axios.get(`/api/vacation/type/${id}`);
        modifyType.value = response.data.result;
        console.log(response.data.result);
    } catch (error) {
        console.error("Error:", error);
    }
};

const registType = async () => {
    try {
        const confirmed = window.confirm('수정하시겠습니까?');
        if (confirmed) {
            const response = await axios.post("/api/vacation/type",
            {
                typeName: registVacationType.value.typeName,
                vacationNum: registVacationType.value.vacationNum,
                dateOfUse: registVacationType.value.dateOfUse,
                vacationExplain: registVacationType.value.vacationExplain
            });
            alert('등록이 완료되었습니다.');
            window.location.reload();
        }
    } catch (error) {
        console.error("Error:", error);
    }
};

const openModifyModal = async (id) => {
    try {
      getVacationType(id);
      showModifyModal.value = true;
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const modifyVacationType = async (id) => {
    try {
        const confirmed = window.confirm('수정하시겠습니까?');
        if (confirmed) {
            const response = await axios.put(`/api/vacation/type/${id}`, 
            { 
                vacationNum: modifyType.value.vacationNum, 
                dateOfUse: modifyType.value.dateOfUse, 
                vacationExplain: modifyType.value.vacationExplain 
            });
            alert('수정이 완료되었습니다.');
            window.location.reload();
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

const deleteVacationType = async (id) => {
    try {
        const confirmed = window.confirm('정말로 삭제하시겠습니까?');
        if(confirmed) {
            const response = await axios.delete(`/api/vacation/type/${id}`);
            alert('삭제가 완료되었습니다.');
        }
    } catch (error) {
        console.error("Error:", error);
    }
};
  

const getAllVacationType = async () => {
    try {
        const response = await axios.get("/api/vacation/type");
        types.value = response.data.result;
        console.log(response.data.result);
    } catch (error) {
        console.error("Error:", error);
    }
};

// function modifyType(id) {
//     router.push(`/vacation/manage/modify/${id}`);
// }

// function registType() {
//     router.push('/vacation/manage/regist');
// }


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
        margin-bottom: 0;
        font-size: 25px;
        font-weight: 600;
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
        align-items: start;
        margin-top: 5%;
    }

    .vacations h3 {
        font-size: 15px;
        font-weight: 600;
        margin: 0;
    }

    .plusBtn {
        width: 100%;
    }

    .typeBox {
        padding: 10px;
        background-color: #F2F2F2;
        display: grid;
        grid-template-rows: 75% 25%;
    }

    .vacationsNum {
        grid-column-start: 1;
        display: grid;
        grid-template-columns:90% 10%;
        font-size: 10px;
    }

    .vacationsNum h3 {
        font-size:12px;
        font-weight: 600;
    }

    /* 등록모달 css */
    .registMain {
        height: 80%;
        width: calc(100% - 20px);
        padding: 10px;
        background-color: #F2F2F2;
    }

    .registMain textarea {
        width: 100%;
        height: 100%;
        padding: 10px;
        box-sizing: border-box;
        resize: none;
        height: 45px;
        border: none;
    }

    .registMain h3 {
        font-size: 15px;
        margin: 0;
        font-weight: 600;
    }

    .registTitle {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 15% 30% 55%;
        font-size: 14px;
        align-items: center;
    }

    .registNum {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 15% 30% 55%;
        font-size: 14px;
        align-items: center;
    }

    .registDesc {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 15% 70% 15%;
        font-size: 14px;
        align-items: center;
    }

    .registPeriod {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 15% 30% 55%;
        font-size: 14px;
        align-items: center;
    }

    .registBtnArea {
        display: grid;
        grid-template-columns: 40% 5% 10% 5% 40%;
        place-items: center;
        margin-top: 2%;
    }

    .registBtn {
        width: 100%;
        background-color: #088A85;
        color: white;
        padding: 5px 5px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 12px;
        font-style: bold;
        grid-column-start: 3;
    }
    /* 등록모달 css */

    /* 수정모달 css */
    .modifyMain {
        height: 80%;
        width: calc(100% - 20px);
        padding: 10px;
        background-color: #F2F2F2;
    }

    .modifyMain textarea {
        width: 100%;
        height: 100%;
        padding: 10px;
        box-sizing: border-box;
        resize: none;
        height: 45px;
        border: none;
    }

    .modifyMain h3 {
        font-size: 15px;
        margin: 0;
        font-weight: 600;
    }

    .vacationType {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 15% 30% 55%;
        font-size: 14px;
        align-items: center;
    }

    .titleContent {
        background-color: #F8F9FAFF;
        padding: 10px;
        margin: 0;
    }

    .modifyNum {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 15% 30% 55%;
        font-size: 14px;
        align-items: center;
    }

    .modifyDesc {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 15% 70% 15%;
        font-size: 14px;
        align-items: center;
    }

    .modifyPeriod {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 15% 30% 55%;
        font-size: 14px;
        align-items: center;
    }

    .modifyBtnArea {
        display: grid;
        grid-template-columns: 39% 10% 2% 10% 39%;
        place-items: center;
        margin-top: 2%;
    }

    .modify {
        width: 100%;
        background-color: #088A85;
        color: white;
        padding: 5px 5px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 12px;
        font-style: bold;
        grid-column-start: 2;
    }

    .delete {
        width: 100%;
        background-color: #088A85;
        color: white;
        padding: 5px 5px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 12px;
        font-style: bold;
        grid-column-start: 4;
    }
</style>
        