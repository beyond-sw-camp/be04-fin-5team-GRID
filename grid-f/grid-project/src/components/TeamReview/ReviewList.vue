<template>
  <div class="container">
    <div class="header">
      <div class="header-title">
        <img class="reviewIcon" src="@/assets/list-check.png" alt="list-check" />
        <h3>내 평가 목록</h3>
      </div>
    </div>

    <div class="search-and-add">
      <div class="search-group">
        <input type="text" v-model="searchQuery" placeholder="Title" class="searchBox" />
        <button @click="search" class="searchBtn">검색</button>
      </div>
    </div>

    <table class="reviewTable">
      <thead>
        <tr>
          <th>제목</th>
          <th>연도</th>
          <th>분기</th>
          <th>작성 시간</th>
          <th>평가 대상자</th>
          <th>평가 확인</th>
          <th>평가 하기</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="review in paginatedReviews" :key="review.id">
          <td>{{ review.content }}</td>
          <td>{{ review.year }}</td>
          <td>{{ review.quarter }}</td>
          <td>{{ review.writeTime ? formatDate(review.writeTime) : '-' }}</td>
          <td>{{ review.revieweeName }}</td>
          <td>{{ getStatusLabel(review.reviewStatus) }}</td>
          <td>
            <button 
              @click="openModal(review)" 
              :disabled="review.reviewStatus === 'Y'" 
              class="view-details-btn"
              :class="{ disabled: review.reviewStatus === 'Y' }"
            >
              Check
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">&laquo;</button>
      <button v-for="page in totalPages" :key="page" @click="changePage(page)" :class="{ active: page === currentPage }">{{ page }}</button>
      <button @click="nextPage" :disabled="currentPage === totalPages">&raquo;</button>
    </div>

    <!-- Review Modal -->
    <div v-if="isModalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <div v-if="selectedReview">
          <img :src="selectedReview.revieweeImage" alt="Reviewee Image" class="reviewee-image" />
          <h2>{{ selectedReview.revieweeName }}</h2>
          <h3>{{ selectedReview.revieweePosition.positionName }}</h3>
          <table class="table table-striped table-hover">
            <thead>
              <tr>
                <th>항목</th>
                <th>내용</th>
                <th>평가</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in reviewItems" :key="index">
                <td>{{ index + 1 }}</td>
                <td>{{ item.listName }}</td>
                <td>
                  <div class="button-group">
                    <button v-for="option in item.options" :key="option" @click="selectOption(index, option)" :class="{ selected: selectedOptions[index] === option }" class="option-button">{{ option }}</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <button @click="submitReview" class="submitBtn">완료</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import { useStore } from 'vuex';

const store = useStore();
const searchQuery = ref('');
const reviews = ref([]);
const isModalOpen = ref(false);
const selectedReview = ref(null);
const reviewItems = ref([]);
const selectedOptions = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;

const user = computed(() => store.state.user);

const optionToScoreMap = {
  S: 95,
  A: 85,
  B: 75,
  C: 65
};

const fetchReviews = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/review/assigned-review/${user.value.id}`);
    const reviewList = response.data.result;

    await Promise.all(reviewList.map(async review => {
      try {
        const revieweeResponse = await axios.get(`http://localhost:8080/users/id/${review.revieweeId}`);
        review.revieweeName = revieweeResponse.data.result.name || 'Unknown';
        review.revieweeImage = revieweeResponse.data.result.image || '';
        review.revieweePosition = revieweeResponse.data.result.position || '';
      } catch (error) {
        console.error('Error fetching reviewee:', error);
        review.revieweeName = 'Unknown';
        review.revieweeImage = '';
        review.revieweePosition = '';
      }
    }));

    reviews.value = reviewList;
  } catch (error) {
    console.error('평가 내역을 가져오는 중 오류 발생:', error);
  }
};

const fetchReviewItems = async (reviewId) => {
  try {
    const response = await axios.get(`http://localhost:8080/review/list`);
    reviewItems.value = response.data.result.map(item => ({
      listName: item.listName,
      reviewId: item.id,
      options: ['S', 'A', 'B', 'C', 'D']
    }));
    selectedOptions.value = Array(response.data.result.length).fill(null);
    console.log(reviewItems.value);
  } catch (error) {
    console.error('Error fetching review items:', error);
  }
};

onMounted(fetchReviews);

const filteredReviews = computed(() => {
  return reviews.value.filter(review => review.content.toLowerCase().includes(searchQuery.value.toLowerCase()));
});

const paginatedReviews = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredReviews.value.slice(start, start + itemsPerPage);
});

const totalPages = computed(() => {
  return Math.ceil(filteredReviews.value.length / itemsPerPage);
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
  const date = new Date(datetime);
  return date.toLocaleString();
};

const getStatusLabel = (status) => {
  if (status === 'Y') return '완료';
  if (status === 'N') return '미완료';
  return '알 수 없음';
};

const openModal = async (review) => {
  selectedReview.value = review;
  await fetchReviewItems(review.id);
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
  selectedReview.value = null;
};

const selectOption = (itemIndex, option) => {
  selectedOptions.value[itemIndex] = option;
  console.log(`Selected option ${option} for item ${itemIndex + 1}`);
};

const submitReview = async () => {
  if (selectedOptions.value.some(option => option === null)) {
    alert('모든 평가 항목을 선택해주세요.');
    return;
  }

  const reviewData = reviewItems.value.map((item, index) => ({
    reviewId: item.reviewId,
    score: optionToScoreMap[selectedOptions.value[index]],
    historyId: selectedReview.value.id
  }));

  console.log('Submitting payload:', JSON.stringify(reviewData));

  try {
    await axios.post('http://localhost:8080/review', reviewData);
    await axios.put(`http://localhost:8080/review/history`, {
      id: selectedReview.value.id
    });

    await fetchReviews();
    closeModal();
  } catch (error) {
    console.error('Error submitting review:', error);
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
  width: 20px; /* 이미지 크기 유지 */
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
  width: 60%;
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

.option-button {
  margin: 5px;
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.option-button.selected {
  background-color: #088A85;
  color: white;
}

.submitBtn {
  background-color: #088A85;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-top: 20px;
}

.submitBtn:hover {
  background-color: #065f5b;
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

.view-details-btn.disabled {
  background-color: #ddd;
  color: #999;
  cursor: not-allowed;
}
</style>
