<template>
  <div class="TotalPerformanceReviewListContainer">
    <div class="TotalPerformanceReviewListTitle">
      <img class="PerformanceIcon" src="@/assets/icons/goal_icon.png">
      <h1>종합 업적 평가 조회</h1>
    </div>
    <div class="TotalPerformanceTableContainer">
      <table>
        <thead>
        <tr>
          <th>No</th>
          <th>연도</th>
          <th>평가명</th>
          <th>평가 대상자</th>
          <th>평가자</th>
          <th>세부정보</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in paginatedReviews" :key="item.id">
          <td>{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
          <td>{{ item.year }}</td>
          <td>{{ item.reviewName }}</td>
          <td>{{ getEmployeeName(item.reviewee) }}</td>
          <td>{{ getEmployeeName(item.reviewer) }}</td>
          <td><button @click="goToDetailPage(item.id)">상세보기</button></td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
      <button v-for="page in totalPages" :key="page" @click="goToPage(page)"
              :class="{ active: page === currentPage }">{{ page }}</button>
      <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import {useStore} from 'vuex';

const store = useStore();
const user = computed(() => store.state.user);

const router = useRouter();

const userRole = ref('');
const userId = ref('');

const totalList = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;

const fetchMemberTotal = async () => {
  try {
    // 팀원일때
    const response = await axios.get(`http://localhost:8080/total-performance-review/reviewee/${user.value.id}`);
    console.log(response.data.findTotalList);
    totalList.value = response.data.findTotalList;
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

const fetchLeaderTotal = async () => {
  try {
    // 팀장일 때
    const response = await axios.get(`http://localhost:8080/total-performance-review/reviewer/${user.value.id}`);
    console.log(response.data.findTotalList);
    totalList.value = response.data.findTotalList;
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

const fetchAllTotal = async () => {
  try {
    // 관리자
    const response = await axios.get(`http://localhost:8080/total-performance-review`);
    console.log(response.data.findTotalList);
    totalList.value = response.data.findTotalList;
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

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
    userId.value = decodedToken?.id || '';
  }

  if (userRole.value === 'ROLE_ADMIN') {
    fetchAllTotal();
  } else if (userRole.value === 'ROLE_USER') {
    if(user.value.duties.dutiesName === '팀원') {
      fetchMemberTotal();
    } else {
      fetchLeaderTotal();
    }
  }
});

const paginatedReviews = computed(() => {
  if (!totalList.value || totalList.value.length === 0) {
    return [];
  }

  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return totalList.value.slice(start, end);
});

const totalPages = computed(() => {
  if (!totalList.value || totalList.value.length === 0) {
    return 1;
  }

  return Math.ceil(totalList.value.length / itemsPerPage);
});

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};

const goToPage = (page) => {
  currentPage.value = page;
};

const getEmployeeName = (employee) => {
  return employee ? employee.employeeName : '';
};

const goToDetailPage = (id) => {
  console.log(id);
  router.push(`/performance-review/total/detail/${id}`);
};
</script>

<style scoped>
.TotalPerformanceReviewListContainer {
  display: grid;
  grid-template-rows: 18% 4% 2% auto 5% 13%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.TotalPerformanceReviewListTitle {
  grid-column-start: 2;
  grid-column-end: 3;
  font-size: 12px;
  font-weight: 0;
  margin-top: 2%;
  color: #000000;
  display: grid;
  grid-template-columns: 3% 97%;
  align-items: center;
}

.goalListTitle h1 {
  margin-left: 0.5%;
}

.PerformanceIcon {
  width: 80%;
}

.search {
  grid-row-start: 2;
  grid-column-start: 2;
  display: grid;
  grid-template-columns: 80% 15% 1% 4%;
}

.sortBox {
  grid-column-start: 2;
  margin-left: 2%;
  padding: 5px 5px;
  border-radius: 4px;
  font-size: 12px;
}

.printBtn {
  grid-column-start: 4;
  margin-left: 2%;
  width: 100%;
  background-color: #088A85;
  color: white;
  padding: 5px 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.TotalPerformanceTableContainer {
  grid-row-start: 4;
  grid-column-start: 2;
  grid-column-end: 3;
  margin-top: 20px;
  font-size: 12px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 6px;
  vertical-align: middle;
}

th {
  background-color: #f2f2f2;
}

.pagination {
  grid-row-start: 6;
  grid-column-start: 2;
  grid-column-end: 3;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
}

.pagination button {
  background-color: white;
  color: black;
  padding: 5px 10px;
  border: 1px solid #dddddd;
  border-radius: 4px;
  cursor: pointer;
  margin: 0 5px;
}

.pagination button.active {
  background-color: darkorange;
  font-weight: bold;
  color: white;
}

.pagination button:disabled {
  background-color: #dddddd;
  cursor: not-allowed;
}

.pagination span {
  display: flex;
  align-items: center;
}
</style>