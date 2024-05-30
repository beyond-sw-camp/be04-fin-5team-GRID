<template>
    <div>
      <div>
        <h2>팀 정보 > {{ teamName }}</h2>
      </div>
  
      <div>
        <input type="text" placeholder="검색" v-model="searchQuery" @input="filterBySearch">
      </div>
  
      <table>
        <thead>
          <tr>
            <th>이름</th>
            <th>사원 번호</th>
            <th>이메일</th>
            <th>업무용 번호</th>
            <th>직급</th>
            <th>직책</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="employee in paginatedEmployees" :key="employee.employeeId">
            <td>{{ employee.name }}</td>
            <td>{{ employee.employeeNumber }}</td>
            <td>{{ employee.email }}</td>
            <td>{{ employee.phoneNumber }}</td>
            <td><span>{{ employee.duties.dutiesName }}</span></td>
            <td><span>{{ employee.position.positionName }}</span></td>
          </tr>
        </tbody>
      </table>
  
      <nav>
        <ul>
          <li>
            <button @click.prevent="prevPage">❮</button>
          </li>
          <li v-for="page in totalPages" :key="page">
            <button @click.prevent="goToPage(page)">{{ page }}</button>
          </li>
          <li>
            <button @click.prevent="nextPage">❯</button>
          </li>
        </ul>
      </nav>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted, watch } from 'vue';
  import axios from 'axios';
  import { useRoute } from 'vue-router';
  
  const route = useRoute();
  const teamId = ref(route.params.teamId); // 라우트에서 teamId를 가져옴
  
  const teamName = ref('');
  const searchQuery = ref('');
  const currentPage = ref(1);
  const itemsPerPage = 5;
  const employees = ref([]);
  const filteredEmployees = ref([]);
  const employeeDetails = ref([]); // 직원 상세 정보 저장용
  const teams = ref([]); // API로부터 가져올 팀 목록
  
  const fetchEmployees = async (teamId) => {
    try {
      const response = await axios.get(`http://localhost:8080/users/team-list/${teamId}`);
      const employeeIds = response.data.result.map(emp => emp.id);
      
      // 모든 직원의 상세 정보를 가져옴
      const employeeDetailsPromises = employeeIds.map(id => axios.get(`http://localhost:8080/users/id/${id}`));
      const employeeDetailsResponses = await Promise.all(employeeDetailsPromises);
      
      // 상세 정보를 employees에 저장
      employees.value = employeeDetailsResponses.map(response => response.data.result);
      filteredEmployees.value = employees.value;
    } catch (error) {
      console.error('직원 데이터를 가져오는 중 오류 발생:', error);
    }
  };
  
  const getTeamName = async (teamId) => {
    try {
      const response = await axios.get(`http://localhost:8080/team/${teamId}`);
      teamName.value = response.data.result.teamName;
    } catch (error) {
      console.error('팀 이름을 가져오는 중 오류 발생:', error);
    }
  };
  
  onMounted(() => {
    fetchEmployees(teamId.value);
    getTeamName(teamId.value);
  });
  
  watch(
    () => route.params.teamId,
    (newId) => {
      teamId.value = newId;
      fetchEmployees(newId);
      getTeamName(newId);
    },
    { immediate: true }
  );
  
  const filterBySearch = () => {
    filteredEmployees.value = employees.value.filter(emp =>
      emp.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    );
    currentPage.value = 1;
  };
  
  const paginatedEmployees = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    return filteredEmployees.value.slice(start, end);
  });
  
  const totalPages = computed(() => {
    return Math.ceil(filteredEmployees.value.length / itemsPerPage);
  });
  
  const prevPage = () => {
    if (currentPage.value > 1) {
      currentPage.value -= 1;
    }
  };
  
  const nextPage = () => {
    if (currentPage.value < totalPages.value) {
      currentPage.value += 1;
    }
  };
  
  const goToPage = (page) => {
    currentPage.value = page;
  };
  </script>
  