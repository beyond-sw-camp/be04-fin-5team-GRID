<template>
  <div>
    <header class="header">
      <div class="logo">
        <img src="@/assets/logo.png" @click="main()" class="logoimage" style="cursor: pointer;">
      </div>
      <div class="icons">
        <button class="icon-button">
          <img src="@/assets/icon1.png" alt="Button 1" class="icon-image" />
        </button>
        <button class="icon-button" type="button" data-bs-toggle="offcanvas" data-bs-target="#demo">
          <img src="@/assets/icon2.png" alt="Button 2" class="icon-image" />
        </button>
        <div class="dropdown">
          <img
            src="https://talkimg.imbc.com/TVianUpload/tvian/TViews/image/2020/04/12/FydNALvKf23Z637223013461671479.jpg"
            alt="profile" class="profile" @click="toggleDropdown">
          <ul class="dropdown-menu" ref="dropdownMenu">
            <li><a class="dropdown-item" href="#" @click="goToProfile">개인 정보</a></li>
            <li><a class="dropdown-item" href="#" @click="logout">로그 아웃</a></li>
          </ul>
        </div>
      </div>
    </header>

    <div class="offcanvas offcanvas-end" id="demo">
      <div class="offcanvas-header">
        <h1 class="offcanvas-title">부서 리스트</h1>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
      </div>
      <div class="offcanvas-body" style="position: relative;">
        <draggable v-model="departments" @end="handleDragEnd" tag="ul" class="list-group">
          <template #item="{ element }">
            <li class="list-group-item">{{ element.departmentName }}</li>
          </template>
        </draggable>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { Dropdown } from 'bootstrap';
import draggable from 'vuedraggable';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';

const departments = ref([]);
const router = useRouter();
const store = useStore();
const dropdownMenu = ref(null);

const user = computed(() => store.state.user);

const fetchDepartments = async () => {
  try {
    const response = await axios.get('http://localhost:8080/department/find-all');
    departments.value = response.data.result.map(department => ({
      id: department.id,
      departmentName: department.departmentName,
    }));
  } catch (error) {
    console.error('부서 정보를 가져오는 데 실패했습니다:', error);
  }
};

const goToProfile = () => {
  if (user.value && user.value.employeeNumber) {
    router.push(`/hr/profile/${user.value.employeeNumber}`);
  }
};

const handleDragEnd = async () => {
  try {
    const updatedDepartments = departments.value.map((department, index) => ({
      id: department.id,
      departmentName: department.departmentName,
      order: index + 1,
    }));

    await axios.put('http://localhost:8080/department/list', { departments: updatedDepartments });
  } catch (error) {
    console.error('부서 순서를 업데이트하는 데 실패했습니다:', error);
  }
};

const toggleDropdown = () => {
  if (dropdownMenu.value.style.display === 'block') {
    dropdownMenu.value.style.display = 'none';
  } else {
    dropdownMenu.value.style.display = 'block';
  }
};

const logout = async () => {
  try {
    await axios.post('http://localhost:8080/logout', {}, { withCredentials: true });
    localStorage.removeItem('access');
    store.dispatch('resetState');
    alert('로그아웃 되었습니다');
    router.push('/');
  } catch (error) {
    console.error('로그아웃 중 오류가 발생했습니다:', error);
  }
};

function main() {
  router.push('/main');
}

onMounted(async () => {
  fetchDepartments();

  const dropdownElementList = [].slice.call(document.querySelectorAll('.dropdown-toggle'));
  dropdownElementList.map(function (dropdownToggleEl) {
    return new Dropdown(dropdownToggleEl);
  });
});
</script>

<style scoped>
@font-face {
  font-family: 'IBMPlexSansKR-Regular';
  src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

.header {
  background: #088A85;
  color: white;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-family: 'IBMPlexSansKR-Regular';
}

.logo {
  display: flex;
  align-items: center;
}

.logo img {
  height: 40px;
  margin-right: 10px;
}

.icons {
  display: flex;
  align-items: center;
}

.profile {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  cursor: pointer;
  margin-right: 50px;
  object-fit: cover;
}

.icon-button {
  background: none;
  border: none;
  margin: 0 5px;
  padding: 5px;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.icon-image {
  width: 24px;
  height: 24px;
}

.list-group-item {
  cursor: move;
}

.dropdown {
  position: relative;
}

.dropdown-menu {
  display: none;
  position: absolute;
  top: 100%;
  right: 0;
  background: white;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  z-index: 1;
  list-style: none;
  padding: 10px 0;
  margin: 0;
}

.dropdown-item {
  padding: 8px 16px;
  cursor: pointer;
  text-decoration: none;
  display: block;
  color: black;
}

.dropdown-item:hover {
  background-color: #f1f1f1;
}
</style>
