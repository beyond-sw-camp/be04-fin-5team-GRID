<template>
  <div>
    <div>
      <img src="@/assets/logo.png" alt="Evaluation Icon" />
      <h1>전체 평가 목록</h1>
    </div>

    <button @click="openAddNewModal">Add New</button> <!-- Add New 버튼 추가 -->

    <div>
      <input type="text" v-model="searchQuery" placeholder="Title" />
    </div>

    <table>
      <thead>
        <tr>
          <th>직원</th>
          <th>부서 명</th>
          <th>평가 명</th>
          <th>작성일</th>
          <th>연도</th>
          <th>분기</th>
          <th>대상자</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="review in filteredReviews" :key="review.id">
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

    <!-- Add New Modal -->
    <div v-if="isAddNewModalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeAddNewModal">&times;</span>
        <h2>Add New Review</h2>
        <form @submit.prevent="addNewReview">
          <div>
            <label for="newReviewContent">Review Content:</label>
            <input type="text" id="newReviewContent" v-model="newReviewContent" required />
          </div>
          <div>
            <label for="newReviewYear">Year:</label>
            <input type="number" id="newReviewYear" v-model="newReviewYear" required />
          </div>
          <div>
            <label for="newReviewQuarter">Quarter:</label>
            <select id="newReviewQuarter" v-model="newReviewQuarter">
              <option value="1">1분기</option>
              <option value="2">2분기</option>
            </select>
          </div>
          <div>
            <label for="newRevieweeId">Reviewee:</label>
            <select id="newRevieweeId" v-model="newRevieweeId" required>
              <option v-for="employee in employees" :key="employee.id" :value="employee.id">
                {{ employee.name }}
              </option>
            </select>
          </div>
          <button type="submit">Submit</button>
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

<style>
/* 테이블 스타일 */
table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
  table-layout: fixed;
}

/* 테이블 헤더와 셀 스타일 */
th, td {
  padding: 8px;
  text-align: left;
  border-top: 1px solid #ddd; /* 테이블 상단에만 선 추가 */
  border-bottom: 1px solid #ddd; /* 테이블 하단에만 선 추가 */
}

/* 테이블 헤더 스타일 */
th {
  background-color: #f2f2f2;
  color: #333;
}

/* 테이블 데이터 셀 스타일 */
td {
  font-size: 14px;
}

/* 테이블 행 호버 스타일 */
tr:hover {
  background-color: #f1f1f1;
}

/* 모달 스타일 */
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

/* 모달 콘텐츠 스타일 */
.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  width: 500px;
  max-width: 80%;
}

/* 닫기 버튼 스타일 */
.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

/* 닫기 버튼 호버 및 포커스 스타일 */
.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}
</style>
