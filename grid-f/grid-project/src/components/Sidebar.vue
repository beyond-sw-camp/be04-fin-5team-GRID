<template>
  <aside class="sidebar">
    <div class="profile">
      <img :src="profileUrl" alt="Profile Picture" class="profile-pic" />
      <div class="profile-info">
        <h3>{{ user?.name }}</h3>
        <p>{{ user?.email }}</p>
      </div>
    </div>
    <div class="w3-sidebar w3-card" style="width:250px">
      <div v-for="(menu, key) in filteredMenus" :key="key" class="menu-item">
        <button class="w3-button w3-block w3-left-align dropdown-title" :class="{ active: activeMenu === key }"
          @click="toggleMenu(key)">
          <span class="title-text">{{ menu.title }}</span>
          <img :class="{ rotated: activeMenu === key }" src="@/assets/buttons/plus.png" class="dropdown-icon">
        </button>
        <transition name="slide-fade">
          <div v-show="activeMenu === key" class="w3-bar-block w3-white w3-card-4 dropdown-content">
            <a v-for="item in menu.items" :key="item.label" @click="navigateTo(item.path)"
              class="w3-bar-item w3-button dropdown-item">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                class="bi bi-chevron-right dropdown-item-icon" viewBox="0 0 16 16">
                <path fill-rule="evenodd"
                  d="M4.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L10.293 8 4.646 2.354a.5.5 0 0 1 0-.708" />
              </svg> &nbsp {{ item.label }}
            </a>
          </div>
        </transition>
      </div>
    </div>
  </aside>
</template>


<script setup>
import { ref, onMounted, computed, watchEffect } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import defaultProfileImage from '@/assets/defaultProfile.jpg';

const router = useRouter();
const store = useStore();
const userRole = ref('');
const userId = ref('');
const activeMenu = ref('');

const user = computed(() => store.state.user);
const profileUrl = computed(() => {
  return user.value?.profilePath ? user.value.profilePath : defaultProfileImage;
});

const menus = {
  workManagement: {
    title: '근태 관리',
    items: [
      { label: '출퇴근 조회', path: '/ad-time' },
      { label: '근무 정보', path: '/work' },
      { label: '휴가 종류', path: '/vacation/manage', role: 'ROLE_ADMIN' },
      { label: '휴가 정책', path: '/vacation/policy', role: 'ROLE_ADMIN' },
      { label: '휴가 종류/정책', path: '/vacation/policy', role: 'ROLE_USER' },
      { label: '휴가 보유 정보', path: '/vacation/info', role: 'ROLE_ADMIN'},
      { label: '휴가 보유 정보/변화 이력', path: '/vacation/userInfo', role: 'ROLE_USER'},
      { label: '휴가 변화 이력', path: '/vacation/changeInfo', role: 'ROLE_ADMIN'},
    ]
  },
  paymentManagement: {
    title: '결재 관리',
    items: [
      { label: '결재 문서 작성', path: '/regist/main' },
      { label: '결재 문서 목록', path: '/approval' },
    ]
  },
  hrManagement: {
    title: '인사 관리',
    items: [
      { label: '인사 정보', path: '/hr' },
    ]
  },
  departmentManagement: {
    title: '부서 관리',
    items: [
      { label: '부서 정보', path: '/department' },
    ]
  },
  departmentEvaluation: {
    title: '동료 평가',
    items: [
      { label: '평가 받은 목록', path: () => `/team-review/myreview/${userId.value}`, role: 'ROLE_USER' },
      { label: '동료 평가 작성', path: () => `/team-review/list/${userId.value}`, role: 'ROLE_USER' },
      { label: '평가 항목 등록', path: '/team-review/add', role: 'ROLE_ADMIN' },
      { label: '전체 평가 목록', path: '/team-review/history', role: 'ROLE_ADMIN' },
    ]
  },
  performanceReview: {
    title: '업적 평가 관리',
    items: [
      { label: '목표 작성', path: '/performance-review/goal/add', role: 'ROLE_USER', dutiesId: '4' },
      { label: '목표 조회', path: '/performance-review/goal', role: 'ROLE_USER' },
      { label: '중간 평가 작성', path: '/performance-review/mid', role: 'ROLE_USER', dutiesId: '4' },
      { label: '연말 평가 작성', path: '/performance-review/final', role: 'ROLE_USER', dutiesId: '4' },
      { label: '평가 조회', path: '/performance-review', role: 'ROLE_USER' },
      { label: '종합 평가 조회', path: '/performance-review/total' },
    ]
  }
};

const toggleMenu = (menu) => {
  activeMenu.value = activeMenu.value === menu ? '' : menu;
};

const navigateTo = (path) => {
  if (typeof path === 'function') {
    path = path();
  }
  router.push(path);
};

const filteredMenus = ref({});

watchEffect(() => {
  if (user.value && user.value.duties && user.value.duties.id && user.value.department && user.value.department.id &&
      user.value.position && user.value.position.id && user.value.team && user.value.team.id) {
    filteredMenus.value = Object.keys(menus).reduce((acc, key) => {
      const items = menus[key].items.filter(item => {
        const hasRole = !item.role || item.role === userRole.value;
        const hasDutiesId = !item.dutiesId || item.dutiesId == user.value.duties.id;

        return hasRole && hasDutiesId;
      });
      if (items.length) {
        acc[key] = { ...menus[key], items };
      }
      return acc;
    }, {});
  }
});


onMounted(() => {
  const token = localStorage.getItem('access');
  if (token) {
    const decodedToken = parseJwt(token);
    userId.value = decodedToken?.id || '';
    userRole.value = decodedToken?.auth || '';
  }

  if (user.value) {
    if (user.value.duties && user.value.duties.id) {
      console.log(user.value.duties.id);
    } else {
      console.log("User duties are not loaded yet.");
    }

    if (user.value.department && user.value.department.id) {
      console.log(user.value.department.id);
    } else {
      console.log("User department is not loaded yet.");
    }

    if (user.value.position && user.value.position.id) {
      console.log(user.value.position.id);
    } else {
      console.log("User position is not loaded yet.");
    }

    if (user.value.team && user.value.team.id) {
      console.log(user.value.team.id);
    } else {
      console.log("User team is not loaded yet.");
    }
  }
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

.menu-item {
  position: relative;
}

.w3-button.active {
  background-color: #088A85;
  color: white;
}

.w3-button.active .dropdown-icon {
  filter: invert(100%) sepia(65%) saturate(424%) hue-rotate(91deg) brightness(129%) contrast(107%);
}

.dropdown-title:hover {
  background-color: #088A85;
  color: white;
}

.dropdown-title:hover .dropdown-icon {
  filter: invert(100%) sepia(65%) saturate(424%) hue-rotate(91deg) brightness(129%) contrast(107%);
}

.dropdown-icon {
  float: right;
  transition: transform 0.3s, filter 0.3s;
  height: 18px;
  width: 18px;
}

.rotated {
  transform: rotate(45deg);
}

.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: max-height 0.5s ease, opacity 0.5s ease;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  max-height: 0;
  opacity: 0;
}

.slide-fade-enter-to,
.slide-fade-leave-from {
  max-height: 500px;
  /* Adjust according to the content's height */
  opacity: 1;
}

.dropdown-content {
  overflow: hidden;
}

.w3-bar-item {
  padding: 8px;
  padding-left: 20px;
  /* Add padding for indent */
  text-decoration: none;
  display: flex;
  align-items: center;
  color: #888; /* Change text color */
}

.w3-bar-item .dropdown-item-icon {
  color: #888; /* Change icon color */
}

.w3-bar-item:hover {
  background-color: #ddd;
}

.dropdown-title {
  position: relative;
  overflow: hidden;
  font-weight: bold;
  /* Make the title bold */
}

.dropdown-title:before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #088A85;
  transition: transform 0.5s;
  z-index: 0;
  transform: scaleX(0);
  transform-origin: left;
}

.dropdown-title:hover:before {
  transform: scaleX(1);
}

.dropdown-title span {
  position: relative;
  z-index: 1;
  transition: color 0.3s;
}

.dropdown-title:hover span {
  color: white;
}
</style>
