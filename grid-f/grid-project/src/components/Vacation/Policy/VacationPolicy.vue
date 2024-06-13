<template>
  <div class="policyAll">
      <div class="policyTitle" v-if="userRole === 'ROLE_ADMIN'">
        <h1 class="mb-1"><i class="bi bi-brightness-high fs-3"></i>&nbsp;휴가 정책&nbsp;&nbsp;<i class="bi bi-info-circle fs-5 custom-icon" v-if="userRole === 'ROLE_ADMIN'" id="popover-target-1" style="color: darkgoldenrod" @click="showModal('guideManage')"></i></h1>
        <button class="policyRegist btn-custom-1" @click="showModal('registPolicy')" v-if="userRole === 'ROLE_ADMIN'"><span>등록하기</span></button>
      </div>
      <div class="policyTitleUser" v-if="userRole === 'ROLE_USER'">
        <i class="bi bi-brightness-high fs-3"></i>
        <h1 class="mb-1">휴가 종류/정책</h1>
      </div>
      <div class="vacations">
          <div class="card mb-3" v-for="policy in policies" :key="policy.id">
            <div class="card-body">
              <h3 class="card-title">{{ policy.typeName }}</h3>
              <p class="card-text"></p>
              <button href="#" @click="openNewModifyModal(policy.id)" class="btn btn-custom"><span>정책보기</span></button>
            </div>
          </div>
        </div>

    <!-- 수정 모달 -->
    <div class="modal fade" id="modifyPolicy" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">휴가 정책 수정</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" @click="closeModal('modifyPolicy')"></button>
                </div>
                <div class="modal-body">
                    <form class="needs-validation" novalidate>
                        <div class="mb-3">
                            <label for="modifyTypeName" class="form-label">휴가 이름</label>
                            <input type="text" class="form-control" id="modifyTypeName" v-model="modifyPolicy.typeName" disabled>
                        </div>
                        <div class="mb-3">
                            <label for="modifyVacationExplain" class="form-label">휴가 설명</label>
                            <textarea class="form-control explainForm" id="modifyVacationExplain" v-model="modifyPolicyContent" :disabled="userRole === 'ROLE_USER'" required></textarea>
                            <div class="invalid-feedback">
                                휴가 설명을 입력해주세요.
                            </div>
                        </div>
                        <div class="button-container">
                            <button type="button" class="btn btn-primary" v-if="userRole==='ROLE_ADMIN'" @click="validateAndModifyContent(modifyPolicy.id)">수정</button>
                            <button type="button" class="btn btn-danger" v-if="userRole==='ROLE_ADMIN'" @click="deleteVacationPolicy(modifyPolicy.id)">삭제</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>


    <!-- 등록 모달 -->
    <div class="modal fade" id="registPolicy" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">휴가 정책 등록</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" @click="closeModal('registPolicy')"></button>
                </div>
                <div class="modal-body">
                    <form class="needs-validation" @submit.prevent="registPolicy(modifyPolicy.id)" novalidate>
                        <div class="mb-3">
                            <label for="modifyTypeName" class="form-label">휴가 이름</label>
                            <select class="form-select" v-model="selectedType" id="modifyTypeName" required>
                              <option value="" disabled selected>선택해주세요</option>
                              <option v-for="type in types" :key="type.id" :value="type.id">{{ type.typeName }}</option>
                            </select>
                            <div class="invalid-feedback">
                                휴가 이름을 선택해주세요.
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="modifyVacationExplain" class="form-label">휴가 설명</label>
                            <textarea class="form-control explainForm" id="modifyVacationExplain" placeholder="내용을 입력해주세요." v-model="content" required></textarea>
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

    <!-- 가이드 모달 -->
  <div class="modal fade" id="guideManage" tabindex="-1" aria-labelledby="guideManageLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
          <div class="modal-content">
              <div class="modal-header">
                  <h4 class="modal-title" id="guideManageLabel">휴가 정책 가이드</h4>
                  <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
              </div>
              <div class="modal-body">
                  <div class="example-content">
                      <p>여러가지 휴가의 정책을 관리할 수 있는 페이지 입니다. </p>
                      <p>등록하기 버튼과 정책 수정 삭제는 관리자만 가능합니다.</p>
                      <hr>
                      <p>1. 현재 표시되어 있는 정책들은 휴가가 활성화되어있는 휴가의 정책들입니다.</p>
                      <p>2. 새로운 휴가의 정책을 원하시면 <등록하기> 버튼을 통해 등록 가능합니다.</p>
                      <p>3. 정책을 수정하고 싶으시면 <정책보기>를 통해 해당 휴가의 정책으로 들어간 후에 수정할 수 있습니다.</p>
                  </div>
              </div>
              <div class="modal-footer">
                  <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
              </div>

          </div>
      </div>
  </div>
  </div>
</template>

<script setup>
import { ref, onBeforeMount, watch,computed, onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

const policies = ref([]);
const userRole = ref('');
const selectedType = ref('');
const content = ref('');
const types = ref([]);

const detailPolicy = ref({
  typeName: '',
  content: ''
});

const modifyPolicy = ref({
  typeName: '',
  content: ''
});
const modifyPolicyContent = ref('');
const detailPolicyContent = ref('');
const route = useRoute();

const formattedDetailPolicyContent = computed(() => {
  return detailPolicyContent.value.replace(/\n/g, '<br>');
});

const showModal = (modalId) => {
const modal = new bootstrap.Modal(document.getElementById(modalId));
modal.show();
};

const openNewModifyModal = async (id) => {
  openModifyModal(id);
  const modal = new bootstrap.Modal(document.getElementById('modifyPolicy'));
  modal.show();
};

const getAllVacationPolicy = async () => {
  try {
    const response = await axios.get('/api/vacation/policy/all');
    policies.value = response.data.result;
  } catch (error) {
    console.error('Error:', error);
  }
};

const getVacationType = async () => {
  try {
    const response = await axios.get('/api/vacation/type');
    types.value = response.data.result;
  } catch (error) {
    console.error('Error:', error);
  }
};

const closeModal = (modalId) => {
const modal = new bootstrap.Modal(document.getElementById(modalId));
modal.hide();
if (modalId === 'registPolicy') {
  selectedType.value = '';
  content.value = '';
  const form = document.querySelector(`#${modalId} form`);
  if (form) {
    form.classList.remove('was-validated');
  }
} else if (modalId === 'modifyPolicy') {
  const form = document.querySelector(`#${modalId} form`);
  if (form) {
    form.classList.remove('was-validated');
  }
}
};

const registPolicy = async () => {
// selectedType.value나 content.value가 비어있는지 확인
if(!selectedType.value || !content.value) {
  return; 
}

// policies 배열 안에 typeId가 중복되는지 확인
const isDuplicate = policies.value.some(policy => policy.typeId === selectedType.value);
if (isDuplicate) {
  alert('이미 존재하는 휴가의 정책입니다.');
  return;
}

// 중복되지 않을 경우에만 등록 절차 진행
try {
  const confirmed = window.confirm('등록하시겠습니까?');
  if(confirmed) {
    const response = await axios.post('/api/vacation/policy', {
      typeId: selectedType.value,
      content: content.value,
    });
    alert('등록이 완료되었습니다.');
    window.location.reload();
  }
} catch (error) {
  console.error('Error:', error);
}
};


const openModifyModal = async (id) => {
  try {
    const response = await axios.get(`/api/vacation/policy/${id}`);
    modifyPolicy.value = response.data.result;
    modifyPolicyContent.value = modifyPolicy.value.content.replace(/<br\s*\/?>/gi, '\n');
    // showModifyModal.value = true;
  } catch (error) {
    console.error("Error:", error);
  }
};


const modifyContent = async (id) => {
  try {
    const confirmed = window.confirm('수정하시겠습니까?');
    if (confirmed) {
      modifyPolicy.value.content = modifyPolicyContent.value.replace(/\n/g, '<br>');
      const response = await axios.put(`/api/vacation/policy/${id}`, { content: modifyPolicy.value.content });
      alert('수정이 완료되었습니다.');
      window.location.reload();
    }
  } catch (error) {
    console.error("Error:", error);
  }
};

const validateAndModifyContent = (id) => {
'use strict';
const form = document.querySelector('#modifyPolicy .needs-validation');
if (!form.checkValidity()) {
  form.classList.add('was-validated');
} else {
  modifyContent(id);
}
};

const deleteVacationPolicy = async (id) => {
  try {
    const confirmed = window.confirm('정말로 삭제하시겠습니까?');
    if (confirmed) {
      const response = await axios.delete(`/api/vacation/policy/${id}`);
      alert('삭제가 완료되었습니다.');
      window.location.reload();
    }
  } catch (error) {
    console.error("Error:", error);
  }
};

watch(modifyPolicyContent, (newContent) => {
  modifyPolicy.value.content = newContent.replace(/\n/g, '<br>');
});

onBeforeMount(() => {
  const token = localStorage.getItem('access');
  if (token) {
    const decodedToken = parseJwt(token);
    userRole.value = decodedToken?.auth || '';
  }

  getAllVacationPolicy();
  getVacationType();
});

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

const currentPage = ref(1);
const itemsPerPage = ref(3); // 페이지 당 항목 수

// 현재 페이지에 따라 표시할 항목을 계산하는 computed 속성
const displayedPolicies = computed(() => {
const startIndex = (currentPage.value - 1) * itemsPerPage.value;
const endIndex = startIndex + itemsPerPage.value;
return policies.value.slice(startIndex, endIndex);
});

// 총 페이지 수를 계산하는 computed 속성
const totalPages = computed(() => Math.ceil(policies.value.length / itemsPerPage.value));

// 페이지를 변경하는 함수
const goToPage = (page) => {
if (page >= 1 && page <= totalPages.value) {
  currentPage.value = page;
}
};

// 이전 페이지로 이동하는 함수
const previousPage = () => {
if (currentPage.value > 1) {
  currentPage.value--;
}
};

// 다음 페이지로 이동하는 함수
const nextPage = () => {
if (currentPage.value < totalPages.value) {
  currentPage.value++;
}
};

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
.policyAll {
  display: grid;
  grid-template-rows: 18% minmax(75%, max-content) 5% 2%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.custom-icon {
  cursor: pointer;
}

.policyTitle {
  grid-column-start: 2;
  grid-column-end: 3;
  font-size: 12px;
  font-weight: 600;
  margin-top: 2%;
  color: #000000;
  align-items: center;
  display: grid;
  grid-template-columns: 21% 63% 7% 2% 7%;
}

.policyTitle h1 {
  margin-left: 0.5%;
  margin-bottom: 0;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.policyTitleUser {
  grid-column-start: 2;
  grid-column-end: 3;
  font-size: 12px;
  font-weight: 600;
  margin-top: 2%;
  color: #000000;
  align-items: center;
  display: grid;
  grid-template-columns: 3% 20% 77%;
}

.policyTitleUser h1 {
  margin-bottom: 0;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.policyIcon {
  width: 80%;
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
  min-width: 51.4px;
  grid-column-start: 5;
}

.guide {
  width: 30px;
  height: 30px;
  grid-column: 3;
  margin-bottom: 6px;
  cursor: pointer;
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

.registMain {
  height: 80%;
  width: calc(100% - 20px);
  padding: 10px;
  background-color: #F2F2F2;
}

.registMain h3 {
  font-size: 15px;
  margin: 0;
  font-weight: 600;
}

.registTitle {
  margin-top: 2%;
  display: grid;
  grid-template-columns: 5% 20% 75%;
  font-size: 14px;
  align-items: center;
}

.registContent {
  margin-top: 3%;
  height: 50%;
  display: grid;
  grid-template-columns: 5% 95%;
  font-size: 14px;
}

.registContent h3 {
  margin-top: 0;
}

.registContent textarea {
  width: 100%;
  height: 100%;
  padding: 10px;
  box-sizing: border-box;
  resize: none;
  height: 300px;
  border: none;
}

.modifyMain {
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
  grid-template-columns: 5% 30% 65%;
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
  resize: none;
  height: 300px;
  border: none;
}

.modifyButtons {
  display: grid;
  grid-template-columns: 40% 8% 4% 8% 40%;
  place-items: center;
  grid-row-start: 3;
  margin-top: 3%;
}

.modify {
  width: 100%;
  background-color: #088A85;
  color: white;
  padding: 10px 10px;
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
  padding: 10px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-style: bold;
  grid-column-start: 4;
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
  cursor: pointer;
}

.detailTitle {
  margin-top: 2%;
  display: grid;
  grid-template-columns: 5% 30% 65%;
  font-size: 14px;
  align-items: center;
}

.detailContent {
  margin-top: 3%;
  height: 50%;
  display: grid;
  grid-template-columns: 5% 95%;
  font-size: 14px;
}

.detailContent h3 {
  margin-top: 0;
}

.detail {
  background-color: #F8F9FAFF;
  padding: 10px;
  margin-bottom: 0;
  height: auto;
}

.explainForm {
  height: 400px;
  resize: none;
}

.modal-lg {
  max-width: 70%; /* 기본 80%에서 90%로 조정 */
}

.button-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.btn-danger {
  margin-left: 2%;
}

.btn-custom {
  background-color: white;
  color: #088A85;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.4s, color 0.4s;
  position: relative;
  overflow: hidden;
  font-size: 11px;
  font-weight: bold;
}

.btn-custom::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background-color: #088A85;
  transition: left 0.4s;
  z-index: 1;
}

.btn-custom:hover::before {
  left: 0;
}

.btn-custom span {
  position: relative;
  z-index: 2;
  color: #088A85;
}

.btn-custom:hover span {
  color: white;
}

.vacations h3 {
  font-size: 15px;
  font-weight: 600;
  color: black;
  margin: 0;
}

.card-body {
  width: 100%;
  padding: 0px 0px;
  display: grid;
  grid-template-rows: 1fr 1fr 1fr;
  margin: 0;
}

.card {
  padding: 10px 10px;
  border: 2px solid #a0a0a0;
}

.btn-custom-1 {
  background-color: white;
  color: #088A85;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.4s, color 0.4s;
  position: relative;
  overflow: hidden;
  font-size: 11px;
  font-weight: bold;
}

.btn-custom-1::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background-color: #088A85;
  transition: left 0.4s;
  z-index: 1;
}

.btn-custom-1:hover::before {
  left: 0;
}

.btn-custom-1 span {
  position: relative;
  z-index: 2;
  color: #088A85;
}

.btn-custom-1:hover span {
  color: white;
}
</style>
