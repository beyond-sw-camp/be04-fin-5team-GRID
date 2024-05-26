<template>
    <div class="container">
      <header class="header">
        <div class="logo">부서정보</div>
        <div class="title">영업</div>
      </header>
      <div class="toolbar">
        <el-select v-model="selectedDepartment" placeholder="All Departments" class="mb-2">
          <el-option label="All Departments" value=""></el-option>
          <el-option label="Products" value="Products"></el-option>
          <el-option label="Growth" value="Growth"></el-option>
        </el-select>
        <el-input v-model="searchQuery" placeholder="Search..." class="mb-2"></el-input>
      </div>
      <div class="content">
        <el-table :data="filteredTeams" style="width: 100%">
          <el-table-column prop="name" label="Name"></el-table-column>
          <el-table-column prop="teamId" label="Team ID"></el-table-column>
          <el-table-column prop="jobTitle" label="Job Title"></el-table-column>
          <el-table-column prop="department" label="Department"></el-table-column>
          <el-table-column prop="employmentType" label="Employment Type"></el-table-column>
          <el-table-column prop="office" label="Office"></el-table-column>
        </el-table>
        <el-pagination
          background
          layout="prev, pager, next"
          :total="filteredTeams.length"
          :page-size="itemsPerPage"
          @current-change="handlePageChange"
        ></el-pagination>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted } from 'vue';
  import axios from 'axios';
  
  const selectedDepartment = ref('');
  const searchQuery = ref('');
  const teams = ref([]);
  const currentPage = ref(1);
  const itemsPerPage = ref(7);
  
  const fetchTeams = async () => {
    try {
      const response = await axios.get('https://api.example.com/teams'); // 실제 API 엔드포인트로 변경하세요
      teams.value = response.data;
    } catch (error) {
      console.error('팀 데이터를 가져오는 중 오류 발생:', error);
    }
  };
  
  onMounted(fetchTeams);
  
  const filteredTeams = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage.value;
    const end = start + itemsPerPage.value;
    return teams.value.filter(team => {
      return (
        (selectedDepartment.value === '' || team.department === selectedDepartment.value) &&
        (searchQuery.value === '' || team.name.toLowerCase().includes(searchQuery.value.toLowerCase()))
      );
    }).slice(start, end);
  });
  
  const handlePageChange = (newPage) => {
    currentPage.value = newPage;
  };
  </script>
  
  <style scoped>
  .container {
    display: grid;
    grid-template-rows: 18% 15% 4% 43% 5% 13%;
    grid-template-columns: 10% 80% 10%;
    height: 100%;
  }
  
  .header {
    grid-column: 1 / -1;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    background-color: #f0f0f0;
  }
  
  .logo {
    font-size: 1.5em;
  }
  
  .title {
    font-size: 1.2em;
  }
  
  .toolbar {
    grid-column: 2 / 3;
    display: flex;
    justify-content: space-between;
    padding: 10px;
  }
  
  .content {
    grid-column: 2 / 3;
    padding: 10px;
  }

  
table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
  table-layout: fixed;
}

th, td {
  padding: 6px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  word-wrap: break-word;
}

th {
  background-color: #f8f8f8;
}

td {
  font-size: 14px;
}

tr:hover {
  background-color: #f1f1f1;
}
  </style>
  