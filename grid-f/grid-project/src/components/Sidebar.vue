<template>
  <aside class="sidebar">
    <div class="profile">
      <!-- 프로필 이미지 -->
      <img :src="profileUrl" alt="Profile Picture" class="profile-pic" />
      <div class="profile-info">
        <h3>{{ user?.name }}</h3>
        <p>{{ user?.email }}</p>
      </div>
    </div>
    <nav class="menu">
      <ul>
        <li>
          <span @click="toggleMenu('workManagement')">근태 관리</span>
          <ul v-show="activeMenus.workManagement">
            <li @click="toWorkCalender()">근무 관리</li>
            <li @click="navigateTo('/work')">근무 정보</li>
            <li @click="toVacationManage()" v-if="userRole === 'ROLE_ADMIN'">휴가 종류</li>
            <li @click="toVacationPolicy()" v-if="userRole === 'ROLE_ADMIN'">휴가 정책</li>
            <li @click="toVacationPolicy()" v-if="userRole === 'ROLE_USER'">휴가 종류/정책</li>
            <li @click="toVacationInfo()">휴가 보유 정보</li>
            <li @click="toVacationChangeInfo()">휴가 변화 이력</li>
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
          <span @click="toggleMenu('departmentEvaluation')">동료 평가</span>
          <ul v-show="activeMenus.departmentEvaluation">
            <li @click="goToTeamMyReview">본인 평가 목록</li>
            <li @click="goToTeamReviewList">동료 평가 작성</li>
            <li @click="goToAddTeamReview">평가 생성</li>
            <li @click="goToTeamReviewHistory">전체 평가 내역</li>
          </ul>
        </li>
        <li>
          <span @click="toggleMenu('performanceReview')">업적 평가 관리</span>
          <ul v-show="activeMenus.performanceReview">
            <li @click="toAddPerformanceReviewGoal()" v-if="userRole === 'ROLE_USER'">목표 작성</li>
            <li @click="toPerformanceReviewGoal()" v-if="userRole === 'ROLE_USER'">목표 조회</li>
            <li @click="toAddMidPerformanceReview()" v-if="userRole === 'ROLE_USER'">중간 평가 작성</li>
            <li @click="toAddFinalPerformanceReview()" v-if="userRole === 'ROLE_USER'">연말 평가 작성</li>
            <li @click="toPerformanceReview()" v-if="userRole === 'ROLE_USER'">평가 조회</li>
            <li @click="toTotalPerformanceReview()">종합 평가 조회</li>
          </ul>
        </li>
      </ul>
    </nav>
  </aside>
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted, reactive, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import defaultProfileImage from '@/assets/defaultProfile.jpg';

const router = useRouter();
const store = useStore();
const userRole = ref('');
const error = ref([]);

const user = computed(() => store.state.user);
const profileUrl = computed(() => {
  return user.value?.profilePath ? user.value.profilePath : defaultProfileImage;
});

const activeMenus = reactive({
  workManagement: false,
  paymentManagement: false,
  hrManagement: false,
  departmentManagement: false,
  departmentEvaluation: false,
  performanceReview: false,
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

function toHR() {
  router.push('/hr');
}

function toVacationManage() {
  router.push('/vacation/manage');
}

function toVacationPolicy() {
  router.push('/vacation/policy');
}

function toVacationInfo() {
  router.push('/vacation/info');
}

function toVacationChangeInfo() {
  router.push('/vacation/changeInfo');
}

const goToTeamReviewHistory = () => {
  router.push('/team-review/history');
}

const goToTeamReviewList = () => {
  router.push(`/team-review/list/${user.value.id}`);
}

const goToTeamMyReview = () => {
  router.push(`/team-review/myreview/${user.value.id}`);
}

function toAddPerformanceReviewGoal() {
  if(user.value.duties.dutiesName === '팀원'){
    router.push('/performance-review/goal/add');
  } else {
    alert('팀원만 작성 가능합니다.');
  }


  // 3월에만 평가 작성할 수 있게 함
  // if(user.value.duties.dutiesName === '팀원'){
  //   const currentMonth = new Date().getMonth() + 1; // JavaScript에서 월은 0부터 시작하므로 +1을 해줍니다.
  //   if (currentMonth === 3) {
  //     router.push('/performance-review/goal/add');
  //   } else {
  //     alert('현재 목표 작성 기간이 아닙니다.');
  //   }
  // } else {
  //   alert('팀원만 작성 가능합니다.');
  // }
}

function toPerformanceReviewGoal() {
  router.push('/performance-review/goal');
}

function toAddMidPerformanceReview() {
  if(user.value.duties.dutiesName === '팀원'){
    router.push('/performance-review/mid');
  } else {
    alert('팀원만 작성 가능합니다.');
  }

  // 6월에만 평가 작성할 수 있게 함
  // if(user.value.duties.dutiesName === '팀원'){
  //   const currentMonth = new Date().getMonth() + 1; // JavaScript에서 월은 0부터 시작하므로 +1을 해줍니다.
  //   if (currentMonth === 6) {
  //     router.push('/performance-review/mid');
  //   } else {
  //     alert('현재 중간 평가 작성 기간이 아닙니다.');
  //   }
  // } else {
  //   alert('팀원만 작성 가능합니다.');
  // }
}

function toAddFinalPerformanceReview() {
  if(user.value.duties.dutiesName === '팀원'){
    router.push('/performance-review/final');
  } else {
    alert('팀원만 작성 가능합니다.');
  }

  // 12월에만 평가 작성할 수 있게 함
  // if(user.value.duties.dutiesName === '팀원'){
  //   const currentMonth = new Date().getMonth() + 1; // JavaScript에서 월은 0부터 시작하므로 +1을 해줍니다.
  //   if (currentMonth === 12) {
  //     router.push('/performance-review/final');
  //   } else {
  //     alert('현재 연말 평가 작성 기간이 아닙니다.');
  //   }
  // } else {
  //   alert('팀원만 작성 가능합니다.');
  // }

}

function toPerformanceReview() {
  router.push('/performance-review');
}

function toTotalPerformanceReview() {
  router.push('/performance-review/total');
}

function toWorkCalender() {
  router.push('/work-calendar');
}


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



onMounted(() => {
  const token = localStorage.getItem('access');
  if (token) {
    const decodedToken = parseJwt(token);
    userRole.value = decodedToken?.auth || '';
  }
});
</script>

<style scoped>
@font-face {
  font-family: 'IBMPlexSansKR-Regular';
  src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

.sidebar {
  width: 200px;
  background: #fff;
  border-right: 1px solid #e5e5e5;
  height: 100vh;
  overflow-y: auto;
  font-family: 'IBMPlexSansKR-Regular';
}

.sidebar {
  -ms-overflow-style: none;
}

.sidebar::-webkit-scrollbar {
  display: none;
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