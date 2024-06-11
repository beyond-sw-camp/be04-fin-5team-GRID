<template>
  <div class="container">
    <div class="header-title">
      <h1 class="mb-1"><i class="bi bi-person-check fs-3"></i>&nbsp; 동료 평가 목록</h1>
      <button href="#" class="addNewBtn btn-custom-1" @click="showModal('addReview')"><span>생성</span></button>
    </div>
    <div class="search-and-add">
      <div class="search-group">
        <input type="text" v-model="searchQuery" placeholder="이름 검색" class="searchBox"/>
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

    <div class="pagination-container">
      <ul class="pagination">
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <a class="page-link" @click.prevent="goToFirstPage">««</a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <a class="page-link" @click.prevent="prevPage">«</a>
        </li>
        <li class="page-item" v-for="page in visiblePages" :key="page" :class="{ active: currentPage === page }">
          <a class="page-link" @click.prevent="goToPage(page)">{{ page }}</a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <a class="page-link" @click.prevent="nextPage">»</a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <a class="page-link" @click.prevent="goToLastPage">»»</a>
        </li>
      </ul>
    </div>

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
                <label for="newReviewYear" class="form-label">연도</label>
                <input class="form-control" type="number" id="newReviewYear" v-model="newReviewYear" required readonly />
              </div>
              <div class="mb-3">
                <label for="newReviewQuarter" class="form-label">분기</label>
                <input class="form-control" type="text" id="newReviewQuarter" v-model="formattedQuarter" required readonly />
              </div>
              <div class="mb-3">
              </div>
              <div class="button-container">
                <button type="submit" class="btn btn-primary" >생성</button>
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
const currentPage = ref(1);
const itemsPerPage = ref(10); // 페이지당 항목 수
const totalPages = ref(1);

const employees = ref([]);
const newReviewContent = ref('');
const currentYear = new Date().getFullYear();
const newReviewYear = ref(currentYear);
const newReviewQuarter = ref(new Date().getMonth() < 6 ? 1 : 2); // 분기를 자동 계산
const newRevieweeId = ref(null);
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
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/review/employees-history?page=${currentPage.value}&size=${itemsPerPage.value}`);
    const reviewPage = response.data;
    reviews.value = reviewPage.list;
    totalPages.value = Math.ceil(reviewPage.total / itemsPerPage.value); // 총 페이지 수를 계산합니다.
  } catch (error) {
    console.error('평가 내역을 가져오는 중 오류 발생:', error);
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
  }
});

const paginatedReviews = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return reviews.value.slice(start, end);
});

const changePage = (page) => {
  currentPage.value = page;
  fetchReviews(); // 페이지 변경 시 새로운 데이터 요청
};

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
    fetchReviews();
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
    fetchReviews();
  }
};

const goToPage = (page) => {
  currentPage.value = page;
  fetchReviews();
};

const goToFirstPage = () => {
  currentPage.value = 1;
  fetchReviews();
};

const goToLastPage = () => {
  currentPage.value = totalPages.value;
  fetchReviews();
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
  currentPage.value = 1;
  fetchReviews();
};

const closeModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.hide();
  if (modalId === 'addReview') {
    newReviewContent.value = '';
    newReviewYear.value = currentYear;
    newReviewQuarter.value = new Date().getMonth() < 6 ? 1 : 2;
    newRevieweeId.value = null;
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
    addNewReview();
  }
};

const addNewReview = async () => {
  const currentQuarter = new Date().getMonth() < 6 ? 1 : 2;
  if (newReviewYear.value !== currentYear || newReviewQuarter.value !== currentQuarter) {
    alert('현재 연도와 분기만 설정할 수 있습니다.');
    return;
  }

  const newReview = {
    content: newReviewContent.value,
    year: newReviewYear.value,
    quarter: newReviewQuarter.value,
    revieweeId: newRevieweeId.value,
  };

  try {
    const isDuplicate = reviews.value.some(review =>
      review.year === newReview.year &&
      review.quarter === newReview.quarter
    );

    if (isDuplicate) {
      alert('이미 생성된 평가가 있습니다.');
      return;
    }

    const confirmed = window.confirm('생성하시겠습니까?');
    if (confirmed) {
      await axios.post('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/review/history', newReview);
      alert('생성 완료되었습니다!');
      await fetchReviews();
      closeModal('addReview');
      window.location.reload();
    }
  } catch (error) {
    console.error('Error adding new review:', error);
  }
};

const visiblePages = computed(() => {
  const totalVisible = 5;
  let startPage = currentPage.value - Math.floor(totalVisible / 2);
  if (startPage < 1) startPage = 1;
  let endPage = startPage + totalVisible - 1;
  if (endPage > totalPages.value) {
    endPage = totalPages.value;
    startPage = endPage - totalVisible + 1;
    if (startPage < 1) startPage = 1;
  }
  const pages = [];
  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }
  return pages;
});

const formattedQuarter = computed(() => {
  return newReviewQuarter.value === 1 ? '1분기' : '2분기';
});

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
  grid-template-rows: 18% 13% 4% 68% 8%;
  grid-template-columns: 10% 80% 10%;
  padding: 0;
  font-family: 'IBMPlexSansKR-Regular';
}

.header-title {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
  margin-left: -0.5%;
  color: #000000;
  display: grid;
  grid-template-columns: 24% 65% 4%;
  align-items: center;
}

.header-title h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.button-container {
  display: flex;
  justify-content: center; /* 중앙 정렬을 위해 추가 */
}

.reviewIcon {
  width: 25px;
}

.addNewBtn {
  grid-column-start: 4;
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
  padding: 5px;
  font-size: 14px;
  border-radius: 4px;
  border: 1px solid #ddd;
  width: 200px;
  max-width: 200px;
}

.searchBtn {
  background-color: #088A85;
  color: white;
  padding: 5px;
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
  cursor: pointer;
}

.pagination .page-item.disabled .page-link {
  color: #088A85; /* 비활성화된 페이지 색 */
  cursor: not-allowed;
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

.btn-custom-1 {
    background-color: white;
    color: #088A85;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.4s, color 0.4s;
    position: relative;
    overflow: hidden;
    font-size: 11px;
    font-weight: bold;
    
}

.btn-custom-1::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background-color: #088A85;
    transition: left 0.4s;
    z-index: 1;
    
}

.btn-custom-1:hover::before {
    left: 0;
    
}

.btn-custom-1 span {
    position: relative;
    z-index: 2;
    color: #088A85;
}

.btn-custom-1:hover span {
    color: white;
}

.pagination-container {
  grid-row-start: 5;
  grid-column-start: 2;
  justify-self: center;
}
</style>
