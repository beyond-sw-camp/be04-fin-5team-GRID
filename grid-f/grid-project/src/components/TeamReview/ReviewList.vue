<template>
  <div>
    <div>
      <img src="@/assets/logo.png" alt="Evaluation Icon" />
      <h1>내 평가 목록</h1>
    </div>

    <div>
      <input type="text" v-model="searchQuery" placeholder="Title" />
    </div>

    <button @click="openAddNewModal">Add New</button> <!-- Add New 버튼 추가 -->

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

    <!-- Add New Review Modal -->
    <div v-if="isAddNewModalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeAddNewModal">&times;</span>
        <div>
          <h2>Add New Review</h2>
          <!-- 평가 대상자 선택 -->
          <div>
            <label for="newRevieweeId">평가 대상자:</label>
            <select v-model="newRevieweeId" id="newRevieweeId">
              <option v-for="employee in employees" :key="employee.id" :value="employee.id">
                {{ employee.name }}
              </option>
            </select>
          </div>
          <div>
            <label for="newReviewContent">평가 제목:</label>
            <input type="text" v-model="newReviewContent" id="newReviewContent" />
          </div>
          <div>
            <label for="newReviewYear">연도:</label>
            <input type="number" v-model="newReviewYear" id="newReviewYear" />
          </div>
          <div>
            <label for="newReviewQuarter">분기:</label>
            <input type="number" v-model="newReviewQuarter" id="newReviewQuarter" />
          </div>
          <button @click="addNewReview">Add Review</button>
        </div>
      </div>
    </div>

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

// New state for Add New modal
const isAddNewModalOpen = ref(false);
const employees = ref([]);
const newReviewContent = ref('');
const newReviewYear = ref(new Date().getFullYear());
const newReviewQuarter = ref(1);
const newRevieweeId = ref('');

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

const fetchEmployees = async () => {
  try {
    const response = await axios.get('http://localhost:8080/users/list');
    employees.value = response.data.result;
  } catch (error) {
    console.error('Error fetching employees:', error);
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

onMounted(() => {
  fetchReviews();
  fetchEmployees();
});

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

// Functions for Add New modal
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

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: #fff;
  padding: 20px;
  border-radius: 5px;
  width: 90%;
  max-width: 800px;
  position: relative;
}

.close {
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
}

.reviewee-image {
  width: 100px;
  height: 100px;
}

button {
  margin: 5px;
}

.button-group {
  display: flex;
  gap: 5px;
}

.option-button {
  padding: 10px 20px;
  margin: 5px;
  border: 1px solid #ccc;
  background: #f9f9f9;
  cursor: pointer;
}

.option-button.selected {
  background-color: #4CAF50; /* 선택된 버튼의 배경색 */
  color: white; /* 선택된 버튼의 글자색 */
}
</style>
