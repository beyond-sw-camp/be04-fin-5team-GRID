<template>
  <div>
    <div>
      <img src="@/assets/logo.png" alt="Evaluation Icon" />
      <h1>내 평가 목록</h1>
    </div>

    <div>
      <input type="text" v-model="searchQuery" placeholder="Title" />
    </div>

    <table>
      <thead>
        <tr>
          <th>
            <input type="checkbox" @change="selectAllCheckbox" />
          </th>
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
        <tr v-for="review in filteredReviews" :key="review.id">
          <td>
            <input type="checkbox" />
          </td>
          <td>{{ review.content }}</td>
          <td>{{ review.year }}</td>
          <td>{{ review.quarter }}</td>
          <td>{{ review.writeTime ? formatDate(review.writeTime) : '-' }}</td> <!-- 작성 시간이 null인 경우 '-' 표시 -->
          <td>{{ review.revieweeName }}</td>
          <td>{{ getStatusLabel(review.reviewStatus) }}</td>
          <td>
            <button @click="openModal(review)" :disabled="review.reviewStatus === 'Y'">Check</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Review Modal -->
    <div v-if="isModalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <div v-if="selectedReview">
          <img :src="selectedReview.revieweeImage" alt="Reviewee Image" class="reviewee-image" />
          <h2>{{ selectedReview.revieweeName }}</h2>
          <h3>{{ selectedReview.revieweePosition }}</h3>
          <table>
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
                    <button 
                      v-for="option in item.options" 
                      :key="option" 
                      @click="selectOption(index, option)"
                      :class="{ selected: selectedOptions[index] === option }"
                      class="option-button"
                    >
                      {{ option }}
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <button @click="submitReview">Submit</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const searchQuery = ref('');
const reviews = ref([]);
const isModalOpen = ref(false);
const selectedReview = ref(null);
const reviewItems = ref([]);
const selectedOptions = ref([]);

const optionToScoreMap = {
  S: 95,
  A: 85,
  B: 75,
  C: 65
};

const fetchReviews = async () => {
  try {
    const response = await axios.get('http://localhost:8080/review/assigned-review/1');
    const reviewList = response.data.result;

    await Promise.all(reviewList.map(async review => {
      try {
        const revieweeResponse = await axios.get(`http://localhost:8080/users/id/${review.revieweeId}`);
        review.revieweeName = revieweeResponse.data.result.name || 'Unknown';
        review.revieweeImage = revieweeResponse.data.result.image || ''; // assuming there is an image field
        review.revieweePosition = revieweeResponse.data.result.position || ''; // assuming there is a position field
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
      reviewId: item.id, // assuming there is an id field
      options: ['S', 'A', 'B', 'C', 'D'] // options are hard-coded for simplicity
    }));
    selectedOptions.value = Array(response.data.result.length).fill(null);
    console.log(reviewItems.value);
  } catch (error) {
    console.error('Error fetching review items:', error);
  }
};

onMounted(fetchReviews);

const filteredReviews = computed(() => {
  return reviews.value.filter(review =>
    review.content.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

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
  // 모든 평가 항목이 선택되었는지 확인
  if (selectedOptions.value.some(option => option === null)) {
    alert('모든 평가 항목을 선택해주세요.');
    return;
  }

  const reviewData = reviewItems.value.map((item, index) => ({
    reviewId: item.reviewId,
    score: optionToScoreMap[selectedOptions.value[index]],
    historyId: selectedReview.value.id // assuming historyId is the id of selectedReview
  }));

  console.log('Submitting payload:', JSON.stringify(reviewData));

  try {
    // 리뷰 데이터 전송
    await axios.post('http://localhost:8080/review', reviewData);

    // 리뷰 상태 업데이트
    await axios.put(`http://localhost:8080/review/history`, {
      id: selectedReview.value.id 
    });

    // 상태 업데이트 후 리뷰 목록 갱신
    await fetchReviews();
    closeModal();
  } catch (error) {
    console.error('Error submitting review:', error);
  }
};
</script>
