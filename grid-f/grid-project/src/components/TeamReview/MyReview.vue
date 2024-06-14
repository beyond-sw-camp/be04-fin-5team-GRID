<template>
  <div class="container">
    <div class="header-title">
      <h1 class="mb-1"><i class="bi bi-person-check fs-3"></i>&nbsp; 평가 받은 목록</h1>
    </div>

    <div class="search-and-add">
      <div class="search-group">
        <input type="text" v-model="searchQuery" placeholder="내용 검색" class="searchBox" />
        <button @click="search" class="searchBtn">검색</button>
      </div>
    </div>

    <table class="reviewTable">
      <thead>
        <tr>
          <th>내용</th>
          <th>연도</th>
          <th>분기</th>
          <th>평가 보기</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="review in paginatedReviews" :key="review.id">
          <td>{{ review.content }}</td>
          <td>{{ review.year }}</td>
          <td>{{ review.quarter }}</td>
          <td>
            <button
              @click="openModal(review.id, review.revieweeId)"
              class="view-details-btn"
              :disabled="review.reviewStatus === 'N'"
            >
              Check
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <nav class="pg" aria-label="Page navigation example">
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

    <!-- Modal Structure -->
    <div v-if="isModalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <div v-if="selectedReview">
          <h2>{{ selectedReview.name }}</h2>
          <h3>{{ selectedReview.position.positionName }}</h3>
          <table>
            <thead>
              <tr>
                <th>항목</th>
                <th>내용</th>
                <th>점수</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(info, index) in flattenedReviewInfo" :key="info.id">
                <td>{{ index + 1 }}</td>
                <td>{{ info.listName }}</td>
                <td>{{ info.score }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const revieweeId = ref(route.params.id);

const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = 10;
const reviews = ref([]);
const isModalOpen = ref(false);
const selectedReview = ref(null);
const reviewItems = ref([]);
const reviewContents = ref({});

const fetchReviews = async () => {
  try {
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/review/my-review/${revieweeId.value}`);
    reviews.value = response.data.result;
  } catch (error) {
    console.error('Error fetching reviews:', error);
  }
};

const fetchReviewDetails = async (id, revieweeId) => {
  try {
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/review/history-score/${id}/${revieweeId}`);
    const responseEmployee = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/id/${revieweeId}`);
    selectedReview.value = responseEmployee.data.result;
    reviewItems.value = response.data.result.map(item => ({
      ...item,
      reviewInfo: item.reviewInfo.map(info => ({
        ...info,
        listName: null // Initialize with null
      }))
    }));
    await fetchReviewContents();

  } catch (error) {
    console.error('Error fetching review details:', error);
  }
};

const fetchReviewContents = async () => {
  try {
    for (const item of reviewItems.value) {
      for (const info of item.reviewInfo) {
        if (!reviewContents.value[info.reviewId]) {
          const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/review/list/${info.reviewId}`);
          reviewContents.value[info.reviewId] = response.data.result.listName;
        }
        info.listName = reviewContents.value[info.reviewId];
      }
    }
  } catch (error) {
    console.error('Error fetching review contents:', error);
  }
};

const flattenedReviewInfo = computed(() => {
  return reviewItems.value.flatMap(item => item.reviewInfo);
});

onMounted(fetchReviews);

watch(() => route.params.id, (newId) => {
  revieweeId.value = newId;
  fetchReviews();
});

const filteredReviews = computed(() => {
  return reviews.value.filter(review =>
    review.content.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
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

const openModal = async (id, revieweeId) => {
  await fetchReviewDetails(id, revieweeId);
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
  selectedReview.value = null;
  reviewItems.value = [];
  reviewContents.value = {};
};

const search = () => {
  if (!searchQuery.value.trim()) {
    alert('검색어를 입력해주세요.');
    return;
  }
  currentPage.value = 1;
  // No need to explicitly trigger the filteredReviews computation, as it will reactively update based on searchQuery
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
  grid-template-rows: 18% 5% 4% 75% 8%;
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
  grid-template-columns: 24% 4%;
  align-items: center;
}

.header-title h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.reviewIcon {
  width: 25px; /* 이미지 크기 유지 */
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

.searchBtn:hover {
  background-color: #065f5b;
}

.reviewTable {
  grid-row-start: 4;
  grid-column-start: 2;
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  height: 10px;
  margin-top: 20px;
}

th,
td {
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
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
  background-color: #088a85; /* 원하는 배경색 */
  border-color: #088a85; /* 원하는 테두리 색 */
  color: white; /* 원하는 텍스트 색 */
}

.pagination .page-item .page-link {
  color: #088a85; /* 기본 텍스트 색 */
}

.pagination .page-item.disabled .page-link {
  color: #088a85; /* 비활성화된 페이지 색 */
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
  padding: 30px;
  border-radius: 10px;
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

.reviewee-image {
  width: 100px;
  height: 100px;
  border-radius: 50%;
}

.view-details-btn {
  padding: 5px 10px;
  background-color: #088a85;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.view-details-btn:hover {
  background-color: #065f5b;
}

.view-details-btn:disabled {
  background-color: grey;
  cursor: not-allowed;
}

td, th {
  text-align : center;  
}
</style>
