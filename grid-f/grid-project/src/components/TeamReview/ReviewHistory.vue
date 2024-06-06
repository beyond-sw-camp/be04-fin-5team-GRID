<template>
  <div class="container">
    <div class="header-title">
      <img class="reviewIcon" src="@/assets/list-check.png" alt="list-check" />
      <h1>전체 평가 목록</h1>
      <button href="#" class="addNewBtn" @click="showModal('addReview')">생성</button>
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
          <th style="width: 20%;">평가 명</th>
          <th style="width: 20%;">작성일</th>
          <th style="width: 10%;">연도</th>
          <th style="width: 10%;">분기</th>
          <th style="width: 10%;">대상자</th>
          <!-- <th style="width: 10%;">평가 보기</th> -->
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
          <!-- <td>
            <button class="view-review-btn" @click="openModal(review.id)">평가 보기</button>
          </td> -->
        </tr>
      </tbody>
    </table>

    <nav class="pg" aria-label="Page navigation example" v-if="totalPages > 1">
      <ul class="pagination">
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <a class="page-link" href="#" aria-label="First" @click.prevent="goToFirstPage">
            <span aria-hidden="true">&laquo;&laquo;</span>
          </a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <a class="page-link" href="#" aria-label="Previous" @click.prevent="prevPage">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li v-for="page in filteredPages" :key="page" class="page-item" :class="{ active: page === currentPage }">
          <a class="page-link" @click.prevent="goToPage(page)">{{ page }}</a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <a class="page-link" aria-label="Next" @click.prevent="nextPage">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <a class="page-link" href="#" aria-label="Last" @click.prevent="goToLastPage">
            <span aria-hidden="true">&raquo;&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>

    <!-- Add New Modal -->
    <div class="modal fade" id="addReview" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">동료평가 생성</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" @click="closeModal('addReview')"></button>
          </div>
          <div class="modal-body">
            <form class="needs-validation" @submit.prevent="validateAndRegistContent" novalidate>
              <div class="mb-3">
                <label for="newReviewContent" class="form-label">평가명</label>
                <input class="form-control" type="text" id="newReviewContent" v-model="newReviewContent" required />
                <div class="invalid-feedback">
                  평가명을 입력해주세요.
                </div>
              </div>
              <div class="mb-3">
                <label for="vacationNum" class="form-label">연도</label>
                <input class="form-control" type="number" id="newReviewYear" v-model="newReviewYear" required />
                <div class="invalid-feedback">
                  연도를 입력해주세요.
                </div>
              </div>
              <div class="mb-3">
                <label for="employeeNum" class="form-label">분기</label>
                <select class="form-select" id="newReviewQuarter" v-model="newReviewQuarter" required>
                  <option value="" disabled selected>분기를 선택해주세요.</option>
                  <option value="1">1분기</option>
                  <option value="2">2분기</option>
                </select>
                <div class="invalid-feedback">
                  분기를 선택해주세요.
                </div>
              </div>
              <div class="mb-3">
                <label for="dayOfUsing" class="form-label">평가 대상자</label>
                <select class="form-select" id="newRevieweeId" v-model="newRevieweeId" required>
                  <option value="" disabled selected>평가 대상자를 선택해주세요.</option>
                  <option v-for="employee in employees" :key="employee.id" :value="employee.id">
                    {{ employee.name }}
                  </option>
                </select>
              </div>
              <div class="button-container">
                <button type="submit" class="btn btn-primary">생성</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';
import router from '@/router/router';

const searchQuery = ref('');
const reviews = ref([]);
const filteredReviews = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;

const employees = ref([]);
const newReviewContent = ref('');
const currentYear = new Date().getFullYear();
const newReviewYear = ref(currentYear);
const newReviewQuarter = ref(1);
const newRevieweeId = ref('');
const userRole = ref('');
const user = ref({});
const selectedReviews = ref([]);
const isModalOpen = ref(false);
const selectedReview = ref(null);
const reviewItems = ref([]);
const reviewContents = ref({});


const showModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.show();
};

const fetchReviews = async () => {
  try {
    const response = await axios.get('https://api.gridhr.site/review/history-list');
    const reviewList = response.data.result;

    await Promise.all(reviewList.map(async review => {
      const reviewerResponse = await axios.get(`https://api.gridhr.site/users/id/${review.reviewerId}`);
      const revieweeResponse = await axios.get(`https://api.gridhr.site/users/id/${review.revieweeId}`);
      review.reviewerName = reviewerResponse.data.result.name;
      review.departmentId = reviewerResponse.data.result.department.id;
      review.revieweeName = revieweeResponse.data.result.name;

      const departmentResponse = await axios.get(`https://api.gridhr.site/department/${review.departmentId}`);
      review.departmentName = departmentResponse.data.result.departmentName;
    }));
    reviews.value = reviewList;
    filteredReviews.value = reviewList; // 초기에는 모든 리뷰를 표시
  } catch (error) {
    console.error('평가 내역을 가져오는 중 오류 발생:', error);
  }
};

const fetchEmployees = async () => {
  try {
    const response = await axios.get('https://api.gridhr.site/users/list/all');
    employees.value = response.data.result;
  } catch (error) {
    console.error('Error fetching employees:', error);
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
    user.value = decodedToken || {};
  }

  if (userRole.value !== 'ROLE_ADMIN') {
    alert('접근 권한이 없습니다.');
    router.go(-1);
  } else {
    fetchReviews();
    fetchEmployees();
  }
});

const totalPages = computed(() => {
  return Math.ceil(filteredReviews.value.length / itemsPerPage);
});

const paginatedReviews = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredReviews.value.slice(start, start + itemsPerPage);
});

const changePage = (page) => {
  currentPage.value = page;
};

const filteredPages = computed(() => {
  const maxPages = 5; // 페이지당 최대 표시할 페이지 수
  const startPage = Math.max(1, currentPage.value - Math.floor(maxPages / 2));
  const endPage = Math.min(totalPages.value, startPage + maxPages - 1);

  const pages = [];
  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }
  return pages;
});

const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--;
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++;
};

const goToPage = (page) => {
  currentPage.value = page;
};

// 처음 페이지로 이동
const goToFirstPage = () => {
  currentPage.value = 1;
};

// 마지막 페이지로 이동
const goToLastPage = () => {
  currentPage.value = totalPages.value;
};

const formatDate = (datetime) => {
  if (!datetime) return '-';
  const date = new Date(datetime);
  return date.toLocaleString();
};

const search = () => {
  if (!searchQuery.value.trim()) {
    alert('검색어를 입력해주세요.');
    return;
  }
  filteredReviews.value = reviews.value.filter(review =>
    review.reviewerName.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
  currentPage.value = 1; // 검색 후 첫 페이지로 이동
};

const closeModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.hide();
  if (modalId === 'addReview') {
    newReviewContent.value = '';
    newReviewYear.value = '';
    newReviewQuarter.value = '';
    newRevieweeId.value = '';
    const form = document.querySelector(`#${modalId} form`);
    if (form) {
      form.classList.remove('was-validated');
    }
  }
};

const validateAndRegistContent = () => {
  const form = document.querySelector('#addReview form');
  if (!form.checkValidity()) {
    form.classList.add('was-validated');
  } else {
    if (newReviewYear.value <= currentYear - 1) {
      alert(`${currentYear - 1}년 이하로는 설정할 수 없습니다.`);
    } else {
      addNewReview();
    }
  }
};

const addNewReview = async () => {
  if (newReviewYear.value <= currentYear - 1) {
    alert(`${currentYear - 1}년 이하로는 설정할 수 없습니다.`);
    return;
  }

  const newReview = {
    content: newReviewContent.value,
    year: newReviewYear.value,
    quarter: newReviewQuarter.value,
    revieweeId: newRevieweeId.value,
  };

  try {
    // 중복된 리뷰가 있는지 확인
    const isDuplicate = reviews.value.some(review =>
      review.year === newReview.year &&
      review.quarter === newReview.quarter &&
      review.revieweeId === newReview.revieweeId
    );

    if (isDuplicate) {
      alert('같은 연도, 같은 분기, 같은 대상자로 이미 생성된 평가가 있습니다.');
      return;
    }

    const confirmed = window.confirm('생성하시겠습니까?');
    if (confirmed) {
      await axios.post('https://api.gridhr.site/review/history', newReview);
      alert('생성 완료되었습니다!');
      await fetchReviews();
      closeModal('addReview');
      window.location.reload();
    }
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
  display: grid;
  grid-template-rows: 18% 13% 4% 53% 8%;
  grid-template-columns: 10% 80% 10%;
  padding: 0;
  font-family: 'IBMPlexSansKR-Regular';
}

.header-title {
  grid-column-start: 2;
  grid-row-start: 1;
  align-items: center;
  display:grid;
  grid-template-columns: 3% 92% 5%;
  grid-column-start:2;
}

.header-title h1 {
  margin-left: 0.5%;
  margin-bottom: 0;
  font-size: 25px;
  font-weight: 600;
}

.reviewIcon {
  width: 25px;
}

.addNewBtn {
  grid-column-start: 3;
  grid-row-start: 1;
  background-color: #088A85;
  color: white;
  padding: 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.addNewBtn:hover {
  background-color: #065f5b;
}

.search-and-add {
  grid-row-start: 3;
  grid-column-start: 2;
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

searchBtn:hover {
  background-color: #065f5b;
}

table {
  grid-column-start: 2;
  grid-row-start: 4;  
  width: 100%;
  margin-top: 20px;
  height:10px;
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

.pg {
  grid-row-start: 5;
  grid-column-start: 2;
  grid-column-end: 3;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
}

.pagination .page-item.active .page-link {
  background-color: #088A85; /* 원하는 배경색 */
  border-color: #088A85; /* 원하는 테두리 색 */
  color: white; /* 원하는 텍스트 색 */
}

.pagination .page-item .page-link {
  color: #088A85; /* 기본 텍스트 색 */
}

.pagination .page-item.disabled .page-link {
  color: #088A85; /* 비활성화된 페이지 색 */
}

.view-review-btn {
  background-color: #088A85;
  color: white;
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.view-review-btn:hover {
  background-color: #065f5b;
}
</style>
