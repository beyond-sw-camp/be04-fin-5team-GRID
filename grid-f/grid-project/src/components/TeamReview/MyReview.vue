<template>
  <div class="container">
    <div class="header">
      <div class="header-title">
        <img class="reviewIcon" src="@/assets/list-check.png" alt="list-check" />
        <h3>본인 평가 목록</h3>
      </div>
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
          <td><button @click="openModal(review.id, review.revieweeId)" class="view-details-btn">Check</button></td>
        </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">&laquo;</button>
      <button 
        v-for="page in totalPages" 
        :key="page" 
        @click="changePage(page)" 
        :class="{ active: page === currentPage }"
      >{{ page }}</button>
      <button @click="nextPage" :disabled="currentPage === totalPages">&raquo;</button>
    </div>

    <!-- Modal Structure -->
    <div v-if="isModalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <div v-if="selectedReview">
          <img :src="selectedReview.revieweeImage" alt="Reviewee Image" class="reviewee-image" />
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
    const response = await axios.get(`http://localhost:8080/review/my-review/${revieweeId.value}`);
    reviews.value = response.data.result;
  } catch (error) {
    console.error('Error fetching reviews:', error);
  }
};

const fetchReviewDetails = async (id, revieweeId) => {
  try {
    const response = await axios.get(`http://localhost:8080/review/history-score/${id}/${revieweeId}`);
    const responseEmployee = await axios.get(`http://localhost:8080/users/id/${revieweeId}`);
    selectedReview.value = responseEmployee.data.result;
    reviewItems.value = response.data.result.map(item => ({
      ...item,
      reviewInfo: item.reviewInfo.map(info => ({
        ...info,
        listName: null // Initialize with null
      }))
    }));
    await fetchReviewContents();
    console.log(reviewItems.value);
  } catch (error) {
    console.error('Error fetching review details:', error);
  }
};

const fetchReviewContents = async () => {
  try {
    for (const item of reviewItems.value) {
      for (const info of item.reviewInfo) {
        if (!reviewContents.value[info.reviewId]) {
          const response = await axios.get(`http://localhost:8080/review/list/${info.reviewId}`);
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
  padding: 0 10%; /* 변경된 부분: padding으로 좌우 공백 추가 */
  margin-top: 7%;
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
  margin-bottom: 8px;
}

.search-and-add {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.search-group {
  display: flex;
  align-items: center;
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

.reviewTable {
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
  width: 600px;
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
  background-color: #088A85;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.view-details-btn:hover {
  background-color: #065f5b;
}
</style>
