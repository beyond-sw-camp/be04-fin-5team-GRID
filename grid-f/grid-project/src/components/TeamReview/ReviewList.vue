<template>
  <div class="container">
    <div class="header-title">
      <h1 class="mb-1"><i class="bi bi-person-check fs-3"></i>&nbsp; 내 평가 목록</h1>
    </div>

    <div class="search-and-add">
      <div class="search-group">
        <input type="text" v-model="searchQuery" placeholder="평가대상자 검색" class="searchBox" />
        <button @click="search" class="searchBtn">검색</button>
      </div>
    </div>

    <table class="reviewTable">
      <thead>
        <tr>
          <th class="fixed-width" style="width: 25%;">제목</th>
          <th style="width: 7.5%;">연도</th>
          <th style="width: 7.5%;">분기</th>
          <th style="width: 30%;">작성 시간</th>
          <th style="width: 10%;">평가 대상자</th>
          <th style="width: 10%;">평가 확인</th>
          <th style="width: 10%;">평가 하기</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="review in paginatedReviews" :key="review.id">
          <td class="fixed-width">{{ review.content }}</td>
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

    <!-- Review Modal -->
    <div v-if="isModalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <div v-if="selectedReview">
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
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/review/assigned-review/${user.value.id}`);
    const reviewList = response.data.result;

    await Promise.all(reviewList.map(async review => {
      try {
        const revieweeResponse = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/id/${review.revieweeId}`);
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
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/review/list`);
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
  return reviews.value.filter(review => review.revieweeName.toLowerCase().includes(searchQuery.value.toLowerCase()));
});

const paginatedReviews = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredReviews.value.slice(start, start + itemsPerPage);
});

const totalPages = computed(() => {
  return Math.ceil(filteredReviews.value.length / itemsPerPage);
});

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
    await axios.post('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/review', reviewData);
    await axios.put(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/review/history`, {
      id: selectedReview.value.id
    });

    await fetchReviews();
    closeModal();
  } catch (error) {
    console.error('Error submitting review:', error);
  }
};

const search = () => {
  if (!searchQuery.value.trim()) {
    alert('검색어를 입력해주세요.');
    return;
  }
  currentPage.value = 1;
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
  grid-template-rows: 18% 8% 4% 75% 8%;
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
  margin-bottom: 20px;
  table-layout: fixed;
  height:10px;
  margin-top: 20px;
}

th, td {
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

.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  width: 50%;
  max-width: 80%;
  height: 90%;
  overflow: auto;
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

th, td {
  text-align: center;
}

.fixed-width {  
  width: 160px;
  max-width: 160px;
  word-wrap: break-word;
}
</style>
