<template>
    <div class="policyAll">
      <div class="policyTitle">
        <img class="policyIcon" src="@/assets/buttons/vacation.png">
        <h1>휴가정책</h1>
        <button class="policyRegist" @click="showRegistModal = true" v-if="userRole === 'ROLE_ADMIN'">등록하기</button>
      </div>
      <div class="policyContent">
      <div class="policyBox" v-for="(policy, index) in displayedPolicies" :key="policy.id">
        <div class="policyHeader">
          <h3 class="policyName">{{ policy.typeName }}</h3>
          <img class="policyModify" src="@/assets/buttons/edit.png" @click="openModifyModal(policy.id)" v-if="userRole === 'ROLE_ADMIN'">
        </div>
        <h5 class="policies" v-html="policy.content"></h5>
      </div>
    </div>
    <div class="pagingButtons">
      <button class="pre" @click="previousPage" :disabled="currentPage === 1">Previous</button>
      <div class="pageNumberButtons">
        <button v-for="page in totalPages" :key="page" @click="goToPage(page)" :class="{ active: currentPage === page }">{{ page }}</button>
      </div>
      <button class="next" @click="nextPage" :disabled="currentPage === totalPages">Next</button>
    </div>

  
      
      <!-- 등록 모달 -->
      <Modal v-if="showRegistModal" @close="showRegistModal = false">
        <div class="registMain">
          <div class="registTitle">
            <h3 for="type">타입</h3>
            <select id="type" v-model="selectedType" class="selectField">
              <option value="">선택해주세요.</option>
              <option v-for="type in types" :key="type.id" :value="type.id">{{ type.typeName }}</option>
            </select>
          </div>
          <div class="registContent">
            <h3>내용</h3>
            <textarea v-model="content" placeholder="내용을 입력해주세요." class="inputField"></textarea>
          </div>
          <div class="registBtnArea">
            <button class="registBtn" @click="registPolicy">등록하기</button>
          </div>
        </div>
      </Modal>
  
      <!-- 수정 모달 -->
      <Modal v-if="showModifyModal" @close="showModifyModal = false">
        <div class="modifyMain">
          <div class="modifyTitle">
            <h3>타입</h3>
            <p class="titleContent">{{ modifyPolicy.typeName }}</p>
          </div>
          <div class="modifyContent">
            <h3>내용</h3>
            <textarea v-model="modifyPolicyContent"></textarea>
          </div>
          <div class="modifyButtons">
            <button class="modify" @click="modifyContent(modifyPolicy.id)">수정</button>
            <button class="delete" @click="deleteVacationPolicy(modifyPolicy.id)">삭제</button>
            </div>
        </div>
      </Modal>
    </div>
  </template>
  
  <script setup>
  import { ref, onBeforeMount, watch,computed } from 'vue';
  import axios from 'axios';
  import { useRoute } from 'vue-router';
  import router from '@/router/router';
  import Modal from '@/components/Vacation/Policy/Modal.vue'; // 모달 컴포넌트 임포트
  
  const policies = ref([]);
  const userRole = ref('');
  const showRegistModal = ref(false);
  const showModifyModal = ref(false);
  const selectedType = ref('');
  const content = ref('');
  const types = ref([]);
  
  const modifyPolicy = ref({
    typeName: '',
    content: ''
  });
  const modifyPolicyContent = ref('');
  const route = useRoute();
  
  const getAllVacationPolicy = async () => {
    try {
      const response = await axios.get('/api/vacation/policy/all');
      policies.value = response.data.result;
      console.log(response.data.result);
    } catch (error) {
      console.error('Error:', error);
    }
  };
  
  const getVacationType = async () => {
    try {
      const response = await axios.get('/api/vacation/type');
      types.value = response.data.result;
      console.log(response.data.result);
    } catch (error) {
      console.error('Error:', error);
    }
  };
  
  const registPolicy = async () => {
  if (!selectedType.value) {
    alert('타입을 선택해주세요.');
    return; // 선택된 타입이 없을 경우 함수 종료
  }

  try {
    const response = await axios.post('/api/vacation/policy', {
      typeId: selectedType.value,
      content: content.value,
    });
    alert('등록이 완료되었습니다.');
    window.location.reload();
  } catch (error) {
    console.error('Error:', error);
  }
};
  
  const openModifyModal = async (id) => {
    try {
      const response = await axios.get(`/api/vacation/policy/${id}`);
      modifyPolicy.value = response.data.result;
      modifyPolicyContent.value = modifyPolicy.value.content.replace(/<br\s*\/?>/gi, '\n');
      showModifyModal.value = true;
    } catch (error) {
      console.error("Error:", error);
    }
  };
  
  const modifyContent = async (id) => {
    try {
      const confirmed = window.confirm('수정하시겠습니까?');
      console.log(id)
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
  </script>
  
  <style scoped>
  .policyAll {
    display: grid;
    grid-template-rows: 18% minmax(75%, max-content) 5% 2%;
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
    margin-left: 0.5%;
    margin-bottom: 0;
    font-size: 25px;
    font-weight: 600;
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
    overflow-y: visible;
  }

  .policyContent::-webkit-scrollbar {
    width: 0; /* 너비를 0으로 설정하여 스크롤바를 감춥니다. */
}
  
  .policies {
    margin-top: 0;
    margin-bottom: 0;
    white-space: pre-wrap;
    font-size: 13px;
  }
  
  .policyBox {
    width: 100%;
    padding: 10px;
    margin-top: 10px;
    margin-bottom: 0;
    background-color: white;
    border: 3px solid #088A85;
    border-radius: 4px;
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

  .registBtn{
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

  .registBtnArea {
    display: grid;
    grid-template-columns: 40% 6% 8% 6% 40%;
    place-items: center;
    grid-row-start: 3;
    grid-column-start: 2;
    margin-top: 2%;
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

  .pagingButtons {
    grid-row-start: 3;
    grid-column-start: 2;
    display: flex;
    justify-content: center;
        align-items: center;
  }

  .pageNumberButtons {
  display: flex;
}

.pageNumberButtons button {
  margin: 0 5px;
  background-color: white;
    color: black;
    padding: 5px 10px;
    border: 1px solid #dddddd;
    border-radius: 4px;
    cursor: pointer;
    margin: 0 5px;
}

.pageNumberButtons button.active {
  background-color: #088A85;
  color: white;
}

.pre {
    background-color: white;
    color: black;
    padding: 5px 10px;
    border: 1px solid #dddddd;
    border-radius: 4px;
    cursor: pointer;
    margin: 0 5px;
}

.next {
    background-color: white;
    color: black;
    padding: 5px 10px;
    border: 1px solid #dddddd;
    border-radius: 4px;
    cursor: pointer;
    margin: 0 5px;
}

.pagingButtons button:disabled {
        background-color: #dddddd;
        cursor: not-allowed;
    }

  </style>
  