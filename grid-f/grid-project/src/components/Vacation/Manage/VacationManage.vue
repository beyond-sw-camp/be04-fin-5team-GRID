<template>
    <div class="manageAll">
        <div class="manageTitle">
            <img class = "manageIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 종류</h1>
            <button class="manageRegist" type="button" @click="showModal('registVacation')" v-if="userRole === 'ROLE_ADMIN'">등록하기</button>
        </div>
        <div class="vacations">
            <div class="card mb-3" v-for="type in types" :key="type.id" :class="{'inactive-card': type.useYn === 'N'}">
              <div class="card-body">
                <h3 class="card-title">{{ type.typeName }}</h3>
                <p class="card-text">{{ type.vacationExplain }}</p>
                <button href="#" v-if="userRole === 'ROLE_ADMIN'" @click="openModifyModal(type.id)" class="btn btn-custom" :class="{'inactive-card-detail': type.useYn === 'N'}">살펴보기</button>
              </div>
            </div>
        </div>

         <!-- 등록 모달 -->
         <div class="modal fade" id="registVacation" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">휴가 정보 추가</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" @click="closeModal('registVacation')"></button>
            </div>
            <div class="modal-body">
                <form class="needs-validation" @submit.prevent="registType" novalidate>
                    <div class="mb-3">
                        <label for="typeName" class="form-label">휴가 이름</label>
                        <input type="text" class="form-control" id="typeName" v-model="registVacationType.typeName" required>
                        <div class="invalid-feedback">
                            휴가 이름을 입력해주세요.
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="vacationNum" class="form-label">휴가 제공일수</label>
                        <input type="text" class="form-control" id="vacationNum" v-model="registVacationType.vacationNum" required>
                        <div class="invalid-feedback">
                            휴가 제공일수를 입력해주세요.
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="dateOfUse" class="form-label">휴가 사용기한</label>
                        <input type="text" class="form-control" id="dateOfUse" v-model="registVacationType.dateOfUse" required>
                        <div class="invalid-feedback">
                            휴가 사용기한을 입력해주세요.
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="vacationExplain" class="form-label">휴가 설명</label>
                        <input type="text" class="form-control" id="vacationExplain" v-model="registVacationType.vacationExplain" required>
                        <div class="invalid-feedback">
                            휴가 설명을 입력해주세요.
                        </div>
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
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" @click="closeModal('modifyVacation')"></button>
            </div>
            <div class="modal-body">
                <form class="needs-validation" @submit.prevent="modifyVacationType(modifyType.id)" novalidate>
                    <div class="mb-3">
                        <label for="modifyTypeName" class="form-label">휴가 이름</label>
                        <input type="text" class="form-control" id="modifyTypeName" v-model="modifyType.typeName" required>
                        <div class="invalid-feedback">
                            휴가 이름을 입력해주세요.
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="modifyVacationNum" class="form-label">휴가 제공일수</label>
                        <input type="text" class="form-control" id="modifyVacationNum" v-model="modifyType.vacationNum" required>
                        <div class="invalid-feedback">
                            휴가 제공일수를 입력해주세요.
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="modifyDateOfUse" class="form-label">휴가 사용기한</label>
                        <input type="text" class="form-control" id="modifyDateOfUse" v-model="modifyType.dateOfUse" required>
                        <div class="invalid-feedback">
                            휴가 사용기한을 입력해주세요.
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="modifyVacationExplain" class="form-label">휴가 설명</label>
                        <input type="text" class="form-control" id="modifyVacationExplain" v-model="modifyType.vacationExplain" required>
                        <div class="invalid-feedback">
                            휴가 설명을 입력해주세요.
                        </div>
                    </div>
                    <div class="button-container">
                        <button type="submit" class="btn btn-primary" :disabled="modifyType.useYn === 'N'">수정</button>
                        <button type="button" class="btn btn-danger" @click="deleteVacationType(modifyType.id)" v-if="modifyType.useYn === 'Y'">비활성화</button>
                        <button type="button" class="btn btn-danger" @click="changeUseYnToY(modifyType.id)" v-if="modifyType.useYn === 'N'">활성화</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
  
    </div>
</template>

<script setup>
import { onBeforeMount, ref, onMounted } from 'vue';
import axios from "axios";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

const types = ref([]);
const userRole = ref('');
const userId =ref('');
const policies = ref([]);
const allInfo = ref([]);
const modifyType = ref({
    id: '',
    typeName: '',
    vacationNum: '',
    dateOfUse: '',
    vacationExplain: '',
    useYn: ''
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
    } catch (error) {
        console.error("Error:", error);
    }
};

const closeModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.hide();
  if (modalId === 'registVacation') {
    registVacationType.value.typeName = '';
    registVacationType.value.vacationNum = '';
    registVacationType.value.dateOfUse = '';
    registVacationType.value.vacationExplain = '';
    const form = document.querySelector(`#${modalId} form`);
    if (form) {
      form.classList.remove('was-validated');
    }
  } else if (modalId === 'modifyVacation') {
    const form = document.querySelector(`#${modalId} form`);
    if (form) {
      form.classList.remove('was-validated');
    }
  }
};

const registType = async () => {
    if( !registVacationType.value.typeName || !registVacationType.value.vacationNum || !registVacationType.value.dateOfUse || !registVacationType.value.vacationExplain ){
        return;
    }
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

const modifyVacationType = async (id) => {
    if( !modifyType.value.id || !modifyType.value.typeName || !modifyType.value.vacationNum || !modifyType.value.dateOfUse || !modifyType.value.vacationExplain ){
        return;
    }
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
                vacationExplain: modifyType.value.vacationExplain,
                useYn: 'Y'
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
        // 해당 휴가 타입에 연관된 정책이 있는지 확인
        const policiesResponse = await axios.get(`/api/vacation/policy/${id}`);
        const associatedPolicies = policiesResponse.data.result;

        if (associatedPolicies != null) {
            alert('해당 휴가 타입에 등록된 정책이 있어 비활성화할 수 없습니다.');
            return;
        }

        // 모든 휴가 정보 가져오기
        const vacationInfoResponse = await axios.get("/api/vacation/info");
        const allVacationInfo = vacationInfoResponse.data.result;

        // allVacationInfo 안에 삭제하려는 타입의 아이디가 있는지 확인
        const isTypeInUse = allVacationInfo.some(info => info.typeId === id);

        if (isTypeInUse) {
            alert('해당 휴가를 가지고있는 직원이 있어 비활성화할 수 없습니다.');
            return;
        }

        const confirmed = window.confirm('비활성화 하시겠습니까?');
        if (confirmed) {
            const response = await axios.put(`/api/vacation/type/${id}`,
            {
                id: modifyType.value.id,
                typeName: modifyType.value.typeName,
                vacationNum: modifyType.value.vacationNum,
                dateOfUse: modifyType.value.dateOfUse,
                vacationExplain: modifyType.value.vacationExplain,
                useYn : 'N'
            });
            alert('비활성화가 완료되었습니다.');
            window.location.reload();
        }
        
    } catch (error) {
        console.error("Error:", error);
    }
};

const changeUseYnToY = async (id) => {
    try {
        const confirmed = window.confirm('활성화 하시겠습니까?');
        if (confirmed) {
            const response = await axios.put(`/api/vacation/type/${id}`,
            {
                id: modifyType.value.id,
                typeName: modifyType.value.typeName,
                vacationNum: modifyType.value.vacationNum,
                dateOfUse: modifyType.value.dateOfUse,
                vacationExplain: modifyType.value.vacationExplain,
                useYn : 'Y'
            });
            alert('활성화가 완료되었습니다.');
            window.location.reload();
        }
    } catch (error) {
        console.error("Error:", error);
    }
}
  

const getAllVacationType = async () => {
    try {
        const response = await axios.get("/api/vacation/all/type");
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

const getAllVacationPolicy = async () => {
    try {
      const response = await axios.get('/api/vacation/policy/all');
      policies.value = response.data.result;
    } catch (error) {
      console.error('Error:', error);
    }
  };



onBeforeMount(() => {
    const token = localStorage.getItem('access');
    if (token) {
        const decodedToken = parseJwt(token);
        userRole.value = decodedToken?.auth || '';
        userId.value = decodedToken?.id || '';
    }
    getAllVacationType();
    getAllVacationPolicy();
});

onMounted(() => {
  'use strict';
  const forms = document.querySelectorAll('.needs-validation');
  Array.from(forms).forEach(form => {
    form.addEventListener('submit', event => {
      if (!form.checkValidity()) {
        event.preventDefault();
        event.stopPropagation();
      }
      form.classList.add('was-validated');
    }, false);
  });
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
    color:white;
    background-color: #77B0AA;
    border-color:#77B0AA ;
    grid-row-start: 4;
  }

  .vacations h3 {
      font-size: 15px;
      font-weight: 600;
      color:white;
      margin: 0;
    }
  
    .card-text {
        font-size: 12px;
      font-weight: 600;
      color:white;
      margin-bottom: 0;
    }

  .card-body {
    width:100%;
    padding: 0px 0px;
    display:grid;
    grid-template-rows: 0.7fr 1fr 0.1fr 1fr ;
    margin: 0;
  }

  .card {
    padding: 10px 10px;
    background-color: #088A85;
  }

  .inactive-card {
        background-color: #fc3613 !important; /* Tomato 색상 */
    }

    .inactive-card-detail {
        background-color: #f36868 !important; 
        border: #bd6d6d;
    }

    
</style>
        