<template>
  <div>
    <header class="header">
      <div class="logo">
        <img src="@/assets/newLogo.png" @click="main()" class="logoimage">
      </div>
      <div class="icons">
        <button class="icon-button">
          <img src="@/assets/icon1.png" alt="Button 1" class="icon-image" />
        </button>
        <button class="icon-button" type="button" data-bs-toggle="offcanvas" data-bs-target="#demo">
          <img src="@/assets/icon2.png" alt="Button 2" class="icon-image" />
        </button>
        <div class="dropdown">
          <img src="https://talkimg.imbc.com/TVianUpload/tvian/TViews/image/2020/04/12/FydNALvKf23Z637223013461671479.jpg"
            alt="profile" class="profile dropdown-toggle" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
          <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
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
import { ref, onMounted } from 'vue';
import axios from 'axios';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import draggable from 'vuedraggable';
import { useRouter } from 'vue-router';

const departments = ref([]);
const info = ref();
const router = useRouter();

const fetchDepartments = async () => {
  try {
    const response = await axios.get('http://localhost:8080/department/find-all');
    departments.value = response.data.result.map(department => ({
      id: department.id,
      departmentName: department.departmentName,
      // 필요한 다른 속성들도 추가
    }));
  } catch (error) {
    console.error('부서 정보를 가져오는 데 실패했습니다:', error);
  }
};

const handleDragEnd = async () => {
  try {
    const updatedDepartments = departments.value.map((department, index) => ({
      id: department.id,
      departmentName: department.departmentName,
      order: index + 1, // 서버가 기대하는 순서 값을 추가
    }));

    await axios.put('http://localhost:8080/department/list', { departments: updatedDepartments });
  } catch (error) {
    console.error('부서 순서를 업데이트하는 데 실패했습니다:', error);
  }
};

const goToProfile = (employeeNumber) => {
  router.push(`/hr/profile/${employeeNumber}`); // 개인 정보 페이지로 이동
};

const logout = () => {
  localStorage.removeItem('access');
  document.cookie = 'refresh=; Max-Age=0; path=/;';
  router.push('/');
};

onMounted(fetchDepartments);
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
  display: grid;
  grid-template-columns: 5% 85% 10%;
  justify-content: space-between;
  align-items: center;
  font-family: 'IBMPlexSansKR-Regular';
}

.logo img {
  width: 100%;
  margin-bottom:3px;
}

.search {
  padding: 5px;
  border-radius: 10px;
  border: none;
  width: 400px;
}

.icons {
  grid-column-start: 3;
  align-items: center;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
}

.icons img {
  margin:0;
}

.profile {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
  object-fit: cover;
}

.icon-button {
  background: none;
  border: none;
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
  /* 드래그 가능한 커서를 추가 */
}
</style>