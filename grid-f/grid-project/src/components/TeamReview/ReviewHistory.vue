<template>
  <div class="container">
    <div class="header">
      <div class="header-title">
        <img class="reviewIcon" src="@/assets/list-check.png" alt="list-check" />
        <h3>전체 평가 목록</h3>
      </div>
      <button class="addNewBtn" @click="openAddNewModal">생성</button>
    </div>
    <div class="search-and-add">
      <div class="search-group">
        <input type="text" v-model="searchQuery" placeholder="Title" class="searchBox"/>
        <button @click="search" class="searchBtn">검색</button>
      </div>
    </div>
    <table class="reviewTable">
      <thead>
        <tr>
          <th style="width: 10%;">직원</th>
          <th style="width: 10%;">부서 명</th>
          <th style="width: 30%;">평가 명</th>
          <th style="width: 20%;">작성일</th>
          <th style="width: 10%;">연도</th>
          <th style="width: 10%;">분기</th>
          <th style="width: 10%;">대상자</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="review in paginatedReviews" :key="review.id">
          <td>{{ review.reviewerName }}</td>
          <td>{{ review.departmentName }}</td>
          <td>{{ review.content }}</td>
          <td>{{ formatDate(review.writeTime) }}</td>
          <td>{{ review.year }}</td>
          <td>{{ review.quarter }}</td>
          <td>{{ review.revieweeName }}</td>
        </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">&laquo;</button>
      <button v-for="page in totalPages" :key="page" @click="changePage(page)" :class="{ active: page === currentPage }">{{ page }}</button>
      <button @click="nextPage" :disabled="currentPage === totalPages">&raquo;</button>
    </div>

    <!-- Add New Modal -->
    <div v-if="isAddNewModalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeAddNewModal">&times;</span>
        <h2>동료평가 생성</h2>
        <form @submit.prevent="addNewReview">
          <div>
            <label for="newReviewContent">평가명 :</label>
            <input type="text" id="newReviewContent" v-model="newReviewContent" required />
          </div>
          <div>
            <label for="newReviewYear">연도:</label>
            <input type="number" id="newReviewYear" v-model="newReviewYear" required />
          </div>
          <div>
            <label for="newReviewQuarter">분기:</label>
            <select id="newReviewQuarter" v-model="newReviewQuarter">
              <option value="1">1분기</option>
              <option value="2">2분기</option>
            </select>
          </div>
          <div>
            <label for="newRevieweeId">평가 대상자:</label>
            <select id="newRevieweeId" v-model="newRevieweeId" required>
              <option v-for="employee in employees" :key="employee.id" :value="employee.id">
                {{ employee.name }}
              </option>
            </select>
          </div>
          <button type="submit" class="submitBtn">생성</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const searchQuery = ref('');
const reviews = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;

// New state for Add New modal
const isAddNewModalOpen = ref(false);
const employees = ref([]);
const newReviewContent = ref('');
const newReviewYear = ref(new Date().getFullYear());
const newReviewQuarter = ref(1);
const newRevieweeId = ref('');

const fetchReviews = async () => {
  try {
    const response = await axios.get('http://localhost:8080/review/history-list');
    const reviewList = response.data.result;

    await Promise.all(reviewList.map(async review => {
      const reviewerResponse = await axios.get(`http://localhost:8080/users/id/${review.reviewerId}`);
      const revieweeResponse = await axios.get(`http://localhost:8080/users/id/${review.revieweeId}`);
      review.reviewerName = reviewerResponse.data.result.name;
      review.departmentId = reviewerResponse.data.result.department.id;
      review.revieweeName = revieweeResponse.data.result.name;

      const departmentResponse = await axios.get(`http://localhost:8080/department/${review.departmentId}`);
      review.departmentName = departmentResponse.data.result.departmentName;
    }));
    reviews.value = reviewList;
  } catch (error) {
    console.error('평가 내역을 가져오는 중 오류 발생:', error);
  }
};

const fetchEmployees = async () => {
  try {
    const response = await axios.get('http://localhost:8080/users/list');
    employees.value = response.data.result;
  } catch (error) {
    console.error('Error fetching employees:', error);
  }
};

onMounted(() => {
  fetchReviews();
  fetchEmployees();
});

const filteredReviews = computed(() => {
  return reviews.value.filter(review =>
    review.reviewerName.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const totalPages = computed(() => {
  return Math.ceil(filteredReviews.value.length / itemsPerPage);
});

const paginatedReviews = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredReviews.value.slice(start, start + itemsPerPage);
});

const changePage = (page) => {
  currentPage.value = page;
};

const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--;
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++;
};

const formatDate = (datetime) => {
  if (!datetime) return '-';
  const date = new Date(datetime);
  return date.toLocaleString();
};

const openAddNewModal = () => {
  isAddNewModalOpen.value = true;
};

const closeAddNewModal = () => {
  isAddNewModalOpen.value = false;
};

const search = () => {
  // 검색 로직을 추가합니다.
  filteredReviews.value = reviews.value.filter(review =>
    review.reviewerName.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
};

const addNewReview = async () => {
  const newReview = {
    content: newReviewContent.value,
    year: newReviewYear.value,
    quarter: newReviewQuarter.value,
    revieweeId: newRevieweeId.value,
    reviewerId: 1 // 현재 로그인한 리뷰어 ID를 사용합니다.
  };

  try {
    await axios.post('http://localhost:8080/review/history', newReview);
    await fetchReviews();
    closeAddNewModal();
  } catch (error) {
    console.error('Error adding new review:', error);
  }
};
</script>

<style scoped>
@font-face {
  font-family: 'IBMPlexSansKR-Regular';
  src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

.container {
  display: flex;
  flex-direction: column;
  padding: 0 10%;
  margin-top: 70px; /* 변경된 부분: 고정된 크기의 margin-top 추가 */
  font-family: 'IBMPlexSansKR-Regular';
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-title {
  display: flex;
  align-items: center;
}

.header-title h3 {
  font-size: 25px;
  font-weight: 600;
  margin-left: 10px;
}

.reviewIcon {
  width: 20px;
  margin-bottom: 9px;
}

.addNewBtn {
  background-color: #088A85;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.addNewBtn:hover {
  background-color: #065f5b;
}

.search-and-add {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.search-group {
  display: flex;
  align-items: center;
  justify-content: end;
  flex-grow: 1;
}

.searchBox {
  padding: 10px;
  font-size: 14px;
  border-radius: 4px;
  border: 1px solid #ddd;
  width: 200px;
  max-width: 200px;
}

.searchBtn {
  background-color: #088A85;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-left: 10px;
}

.searchBtn:hover {
  background-color: #065f5b;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
  table-layout: fixed;
}

th, td {
  padding: 8px;
  text-align: left;
  border-top: 1px solid #ddd;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
  color: #333;
}

td {
  font-size: 14px;
}

tr:hover {
  background-color: #f1f1f1;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination button {
  margin: 0 5px;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: white;
  cursor: pointer;
}

.pagination button.active {
  background-color: #088A85;
  color: white;
  border: none;
}

.pagination button:disabled {
  background-color: #ddd;
  cursor: not-allowed;
}

.pagination button:hover:not(.active):not(:disabled) {
  background-color: #f1f1f1;
}

.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  width: 500px;
  max-width: 80%;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

.submitBtn {
  background-color: #088A85;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.submitBtn:hover {
  background-color: #065f5b;
}
</style>
