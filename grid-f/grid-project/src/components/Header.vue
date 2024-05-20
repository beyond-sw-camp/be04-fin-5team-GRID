<template>
  <head>
  <title>Vue.js with Bootstrap</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
  <header class="header">
    <div class="logo">
            <img src="@/assets/logo.png" @click="main()" class="logoimage">
    </div>
    <div>

    </div>
    <input class="search" type="text" placeholder="Search" />
    
    <div class="icons">
      <button class="icon-button">
        <img src="@/assets/icon1.png" alt="Button 1" class="icon-image" />
      </button>

      <button class="icon-button" type="button" data-bs-toggle="offcanvas" data-bs-target="#demo">
        <img src="@/assets/icon2.png" alt="Button 2" class="icon-image" />
      </button>
      <img class="profile" src="@/assets/profile.png" alt="Profile Picture" />

</div>

<div class="offcanvas offcanvas-end" id="demo">
  <div class="offcanvas-header">
    <h1 class="offcanvas-title">부서 리스트</h1>
    <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
  </div>
  <div class="offcanvas-body">
    <draggable-tree :data="departments" @update="handleUpdate">
      <template #default="{ node }">
        <div class="department-item">{{ node.name }}</div>
      </template>
    </draggable-tree>
    <button class="btn btn-secondary" type="button">A Button</button>
  </div>
</div>
    
  </header>
</template>

<script setup>
    import { ref, onMounted } from 'vue';
    import axios from 'axios';
    import 'bootstrap/dist/js/bootstrap.bundle.min.js';
    import {DraggableTree} from 'vue-draggable-nested-tree'


const departments = ref([]);

const fetchDepartments = async () => {
  try {
    const response = await axios.get('http://localhost:8080/department/find-all');
    departments.value = response.data;
  } catch (error) {
    console.error('부서 정보를 가져오는 데 실패했습니다:', error);
  }
};

const handleDragEnd = async () => {
  try {
    await axios.post('/api/departments/reorder', departments.value);
  } catch (error) {
    console.error('부서 순서를 업데이트하는 데 실패했습니다:', error);
  }
};

onMounted(fetchDepartments);
</script>

<style scoped>
.header {
  background: #002366;
  color: white;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
}

.logo img {
  height: 40px;
  margin-right: 10px;
}

.search {
  padding: 5px;
  border-radius: 10px;
  border: none;
  width: 400px;
}

.icons {
  display: flex;
  align-items: center;
}

.profile {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
  margin-right: 50px;
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
</style>
