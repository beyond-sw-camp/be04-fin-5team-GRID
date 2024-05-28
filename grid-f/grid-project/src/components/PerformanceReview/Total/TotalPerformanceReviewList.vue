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
          <td>{{ getEmployeeName(item.writer) }}</td>
          <td>{{ getEmployeeName(item.approver) }}</td>
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

const router = useRouter();

const totalList = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;

const fetchTotal = async () => {
  try {
    // 팀원일때
    // const response = await axios.get(`http://localhost:8080/total-performance-review/reviewee/8`);

    // 팀장일 때
    const response = await axios.get(`http://localhost:8080/total-performance-review/reviewer/5`);
    console.log(response.data.findTotalList);
    totalList.value = response.data.findTotalList;
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

onMounted(() => {
  fetchTotal();
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

const getApprovalStatus = (status) => {
  switch (status) {
    case 'IP':
      return '작성 중';
    case 'S':
      return '상신';
    case 'R':
      return '확인 중';
    case 'C':
      return '평가 완료';
    case 'V':
      return '확정';
    default:
      return '기타';
  }
};

const getType = (type) => {
  if(type === 'M') {
    return '중간 평가';
  } else {
    return '연말 평가';
  }
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