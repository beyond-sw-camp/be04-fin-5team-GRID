<template>
    <div class="manageAll">
        <div class="manageTitle">
            <img class = "manageIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 종류</h1>
            <button class="manageRegist" type="button" @click="showModal('registVacation')" v-if="userRole === 'ROLE_ADMIN'">등록하기</button>
        </div>
        <div class="vacations">
            <div class="card mb-3" v-for="type in types" :key="type.id">
              <div class="card-body">
                <h3 class="card-title">{{ type.typeName }}</h3>
                <p class="card-text">{{ type.vacationExplain }}</p>
                <button href="#" v-if="userRole === 'ROLE_ADMIN'" @click="openModifyModal(type.id)" class="btn btn-custom">살펴보기</button>
              </div>
            </div>
        </div>

         <!-- 등록 모달 -->
        <div class="modal fade" id="registVacation" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">휴가 정보 추가</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="registType">
                        <div class="mb-3">
                            <label for="typeName" class="form-label">휴가 이름</label>
                            <input type="text" class="form-control" id="typeName" v-model="registVacationType.typeName" required>
                        </div>
                        <div class="mb-3">
                            <label for="departmentCode" class="form-label">휴가 제공일수</label>
                            <input type="text" class="form-control" id="vacationNum" v-model="registVacationType.vacationNum" required>
                        </div>
                        <div class="mb-3">
                            <label for="dateOfUse" class="form-label">휴가 사용기한</label>
                            <input type="text" class="form-control" id="dateOfUse" v-model="registVacationType.dateOfUse" required>
                        </div>
                        <div class="mb-3">
                            <label for="vacationExplain" class="form-label">설명</label>
                            <input type="text" class="form-control" id="vacationExplain" v-model="registVacationType.vacationExplain" required>
                        </div>
                        <div class="button-container">
                            <button type="submit" class="btn btn-primary">등록</button>
                        </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>  

        <!-- 수정 모달 -->
        <div class="modal fade" id="modifyVacation" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">휴가 정보 수정</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="modifyVacationType(modifyType.id)">
                            <div class="mb-3">
                                <label for="modifyTypeName" class="form-label">휴가 이름</label>
                                <input type="text" class="form-control" id="modifyTypeName" v-model="modifyType.typeName" required>
                            </div>
                            <div class="mb-3">
                                <label for="modifyVacationNum" class="form-label">휴가 제공일수</label>
                                <input type="text" class="form-control" id="modifyVacationNum" v-model="modifyType.vacationNum" required>
                            </div>
                            <div class="mb-3">
                                <label for="modifyDateOfUse" class="form-label">휴가 사용기한</label>
                                <input type="text" class="form-control" id="modifyDateOfUse" v-model="modifyType.dateOfUse" required>
                            </div>
                            <div class="mb-3">
                                <label for="modifyVacationExplain" class="form-label">설명</label>
                                <input type="text" class="form-control" id="modifyVacationExplain" v-model="modifyType.vacationExplain" required>
                            </div>
                            <div class="button-container">
                                <button type="button" class="btn btn-primary" @click="modifyVacationType(modifyType.id)">수정</button>
                                <button type="button" class="btn btn-danger" @click="deleteVacationType(modifyType.id)">삭제</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>  
    </div>
</template>

<script setup>
import router from '@/router/router';
import { onBeforeMount, ref } from 'vue';
import axios from "axios";
import Modal from '@/components/Vacation/Manage/VacationManageModal.vue';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

const types = ref([]);
const showRegistModal = ref(false);
const showModifyModal = ref(false);
const userRole = ref('');
const userId =ref('');
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

const showModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.show();
};

const openModifyModal = async (id) => {
    await getVacationType(id);
    const modal = new bootstrap.Modal(document.getElementById('modifyVacation'));
    modal.show();
};

const getVacationType = async (id) => {
    try {
        const response = await axios.get(`/api/vacation/type/${id}`);
        modifyType.value = response.data.result;
        console.log(response.data.result);
    } catch (error) {
        console.error("Error:", error);
    }
};

// const registType = async () => {
//     try {
//         const confirmed = window.confirm('등록하시겠습니까?');
//         if (confirmed) {
//             const response = await axios.post("/api/vacation/type",
//             {
//                 typeName: registVacationType.value.typeName,
//                 vacationNum: registVacationType.value.vacationNum,
//                 dateOfUse: registVacationType.value.dateOfUse,
//                 vacationExplain: registVacationType.value.vacationExplain
//             });
//             alert('등록이 완료되었습니다.');
//             window.location.reload();
//         }
//     } catch (error) {
//         console.error("Error:", error);
//     }
// };

const registType = async () => {
    try {
        const confirmed = window.confirm('등록하시겠습니까?');
        if (confirmed) {
            const modal = bootstrap.Modal.getInstance(document.getElementById('registVacation'));
            modal.hide();
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

// const modifyVacationType = async (id) => {
//     try {
//         const confirmed = window.confirm('수정하시겠습니까?');
//         if (confirmed) {
//             const modal = bootstrap.Modal.getInstance(document.getElementById('modifyVacation'));
//             modal.hide();
//             const response = await axios.put(`/api/vacation/type/${id}`,
//             { 
//                 vacationNum: modifyType.value.vacationNum, 
//                 dateOfUse: modifyType.value.dateOfUse, 
//                 vacationExplain: modifyType.value.vacationExplain 
//             });
//             alert('수정이 완료되었습니다.');
//             window.location.reload();
//         }
//     } catch (error) {
//         console.error("Error:", error);
//     }
// };



// const openModifyModal = async (id) => {
//     try {
//       getVacationType(id);
//       showModifyModal.value = true;
//     } catch (error) {
//       console.error("Error:", error);
//     }
//   };

//   const modifyVacationType = async (id) => {
//     try {
//         const confirmed = window.confirm('수정하시겠습니까?');
//         if (confirmed) {
//             const response = await axios.put(`/api/vacation/type/${id}`, 
//             { 
//                 vacationNum: modifyType.value.vacationNum, 
//                 dateOfUse: modifyType.value.dateOfUse, 
//                 vacationExplain: modifyType.value.vacationExplain 
//             });
//             alert('수정이 완료되었습니다.');
//             window.location.reload();
//         }
//     } catch (error) {
//         console.error("Error:", error);
//     }
// }

const modifyVacationType = async (id) => {
    try {
        const confirmed = window.confirm('수정하시겠습니까?');
        if (confirmed) {
            const modal = bootstrap.Modal.getInstance(document.getElementById('modifyVacation'));
            modal.hide();
            const response = await axios.put(`/api/vacation/type/${id}`,
            {
                id: modifyType.value.id,
                typeName: modifyType.value.typeName,
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
};

const deleteVacationType = async (id) => {
    try {
        const confirmed = window.confirm('삭제하시겠습니까?');
        if (confirmed) {
            const response = await axios.delete(`/api/vacation/type/${id}`);
            alert('삭제이 완료되었습니다.');
            window.location.reload();
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


onBeforeMount(() => {
    const token = localStorage.getItem('access');
    if (token) {
        const decodedToken = parseJwt(token);
        userRole.value = decodedToken?.auth || '';
        userId.value = decodedToken?.id || '';
    }
    getAllVacationType();
});
</script>

<style scoped>
    .manageAll {
        display: grid;
        grid-template-rows: 18% minmax(75%,auto) 5% 2% ;
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

    .vacations {
        margin-top: 2%;
        grid-column-start: 2;
        grid-column-end: 3;
        grid-row: 2;
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
        width: 20%;
        background-color: #088A85;
        color: white;
        padding: 5px 5px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 12px;
        font-style: bold;
    }

    .delete {
        width: 20%;
        background-color: #088A85;
        color: white;
        padding: 5px 5px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 12px;
        font-style: bold;
        margin-left: 2%;
    }

    .btn-primary {
        background-color: #088A85; /* 새로운 배경색 */
        border-color: #088A85;
        justify-content: center;
    }

    .btn-danger {
        margin-left: 2%;
    }

    .button-container {
        display: flex;
        justify-content: center;
        margin-top: 20px;
    }

    .btn-custom {
    font-size:11px;
    font-weight: 600;
    color:black;
    background-color: #CDE8E5;
    border-color:#CDE8E5 ;
  }

  .vacations h3 {
      font-size: 15px;
      font-weight: 600;
      color:black;
      margin: 0;
    }
  
    .card-text {
        font-size: 13px;
      font-weight: 600;
      color:black;
      margin: 0;
    }

  .card-body {
    width:100%;
    padding: 0px 0px;
    display:grid;
    grid-template-rows: 1fr 1fr 1fr;
    margin: 0;
  }

  .card {
    padding: 10px 10px;
    background-color: #088A85;
  }

    
</style>
        