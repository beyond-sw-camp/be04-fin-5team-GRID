<template>
  <aside class="sidebar">
    <div class="profile">
      <!-- 추후 이미지 파일 업로드로 받아오기. -->
      <img src="@/assets/profile.png" alt="Profile Picture" class="profile-pic" />
      <div class="profile-info">
        <h3>{{employee.name}}</h3>
        <p>{{employee.email}} </p>
      </div>
    </div>
    <nav class="menu">
      <ul>
        <li>
          <span @click="toggleMenu('workManagement')">근태 관리</span>
          <ul v-show="activeMenus.workManagement">
            <li>근무 관리</li>
            <li @click="navigateTo('/work')">근무 정보</li>
            <li>휴가 정책</li>
            <li>휴가 보유 정보 목록</li>
            <li>휴가 기록 목록</li>
          </ul>
        </li>
        <li>
          <span @click="toggleMenu('paymentManagement')">결재 관리</span>
          <ul v-show="activeMenus.paymentManagement">
            <li @click="navigateTo('/regist/main')">결재 문서 작성</li>
            <li @click="navigateTo('/approval')">결재 문서 목록</li>
          </ul>
        </li>
        <li>
          <span @click="toggleMenu('hrManagement')">인사 관리</span>
          <ul v-show="activeMenus.hrManagement">
            <li @click="toHR">인사 정보</li>
          </ul>
        </li>
        <li>
          <span @click="toggleMenu('departmentManagement')">부서 관리</span>
          <ul v-show="activeMenus.departmentManagement">
            <li @click="gotodepartmentInfo">부서 정보</li>
          </ul>
        </li>
        <li>
          <span @click="toggleMenu('departmentEvaluation')">부서 평가</span>
          <ul v-show="activeMenus.departmentEvaluation">
            <li>본인 평가 목록</li>
            <li>동료 평가 작성</li>
            <li @click="goToAddTeamReview">평가 생성</li>
          </ul>
        </li>
        <li>
          <span @click="toggleMenu('performanceReview')">업적 평가 관리</span>
          <ul v-show="activeMenus.performanceReview">
            <li @click="navigateTo('/performance-review-goal/add')">업적 평가 목표 작성</li>
            <li @click="navigateTo('/performance-review-goal')">업적 평가 목표 조회</li>
            <li>업적 평가 작성</li>
            <li>업적 평가 조회</li>
            <li>종합 업적 평가</li>
          </ul>
        </li>
      </ul>
    </nav>
  </aside>
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted,reactive } from 'vue';
import { useRouter } from 'vue-router';

const employee = ref([]);
const error = ref([]);
const router = useRouter();

const fetchEmployee = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/users/240201`); // ${employeeNumber}로 수정예정
    employee.value = response.data.result;
  } catch (err) {
    console.error('Error fetching employee:', err);
    error.value = 'Failed to fetch employee data.';
  }
};

onMounted(() => {
  fetchEmployee();
});


const activeMenus = reactive({
  workManagement: false,
  paymentManagement: false,
  hrManagement: false,
  departmentManagement: false,
  departmentEvaluation: false,
});

const toggleMenu = (menu) => {
  activeMenus[menu] = !activeMenus[menu];
};

const gotodepartmentInfo = () => {
  router.push('/department');
}

const goToAddTeamReview = () => {
  router.push('/addteamreview');
}

const navigateTo = (path) => {
  router.push(path);
};

function toHR () {
  router.push('/hr');
}

</script>

<style scoped>
@font-face {
    font-family: 'IBMPlexSansKR-Regular';
    src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}

.sidebar {
  width: 250px;
  background: #fff;
  border-right: 1px solid #e5e5e5;
  height: 100vh;
  overflow-y: auto;
  font-family: 'IBMPlexSansKR-Regular';
}
.sidebar{
   -ms-overflow-style: none;
}
.sidebar::-webkit-scrollbar{
  display:none;
}

.profile {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid #e5e5e5;
}

.profile-pic {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

.profile-info {
  margin-top: 10px;
}

.profile-info h3 {
  margin: 10px 0 5px;
  font-size: 18px;
}

.profile-info p {
  margin: 0;
  color: #888;
  font-size: 14px;
}

.menu {
  padding: 10px 0;
}

.menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.menu li {
  padding: 10px 20px;
  cursor: pointer;
}

.menu li span {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.menu li ul {
  padding-left: 20px;
  margin-top: 10px;
}

.menu li ul li {
  padding: 5px 0;
}
</style>  