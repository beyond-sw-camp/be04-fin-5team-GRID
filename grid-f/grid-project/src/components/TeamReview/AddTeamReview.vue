<template>
  <div class="content">
    <div class="reviewAll">
      <div class="reviewHeader">
        <img class="reviewIcon" src="@/assets/list-check.png" alt="list-check" />
        <h3>평가 생성</h3>
      </div>
      <div class="reviewContent">
        <div class="buttonContainer">
          <button type="button" class="btn btn-danger me-2" @click="deleteSelectedReviews">
            선택 항목 삭제
          </button>
          <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addQuestionModal">
            항목 추가
          </button>
        </div>
        <div class="reviewBox">
          <table >
            <thead>
              <tr>
                <th>선택</th>
                <th>항목</th>
                <th>내용</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(review, index) in reviews" :key="review.id">
                <td><input type="checkbox" v-model="selectedReviews" :value="review.id" /></td>
                <td>{{ index + 1 }}</td>
                <td>{{ review.listName }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Modal Structure -->
      <div class="modal fade" id="addQuestionModal" tabindex="-1" aria-labelledby="addQuestionModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="addQuestionModalLabel">항목 추가</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              <div class="mb-3">
                <label for="questionText" class="form-label">평가 내용</label>
                <input type="text" class="form-control" id="questionText" v-model="newReviewText" />
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
              <button type="button" class="btn btn-primary" @click="addReview">생성</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

const reviews = ref([]);
const selectedReviews = ref([]);
const newReviewText = ref('');

const fetchReviews = async () => {
  try {
    const response = await axios.get('http://localhost:8080/review/list');
    reviews.value = response.data.result;
    reviews.value.sort((a, b) => a.id - b.id);
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

onMounted(() => {
  fetchReviews();
});

const deleteSelectedReviews = async () => {
  try {
    for (const reviewId of selectedReviews.value) {
      await axios.delete(`http://localhost:8080/review/list/${reviewId}`);
    }
    reviews.value = reviews.value.filter(review => !selectedReviews.value.includes(review.id));
    selectedReviews.value = [];
  } catch (error) {
    console.error('에러 발생:', error);
    alert('삭제하는 데 에러가 발생했습니다.');
  }
};

const addReview = async () => {
  if (!newReviewText.value) {
    alert('질문 내용을 입력해주세요.');
    return;
  }

  try {
    const response = await axios.post('http://localhost:8080/review/list', {
      listName: newReviewText.value
    });
    reviews.value.push(response.data.result);
    newReviewText.value = '';

    const modalElement = document.getElementById('addQuestionModal');
    const modal = bootstrap.Modal.getInstance(modalElement);
    if (modal) {
      modal.hide();
      // 강제로 모달 상태를 초기화
      modalElement.addEventListener('hidden.bs.modal', () => {
        document.body.classList.remove('modal-open');
        document.querySelectorAll('.modal-backdrop').forEach(backdrop => backdrop.remove());
      }, { once: true });
    }
  } catch (error) {
    console.error('에러 발생:', error);
    alert('추가하는 데 에러가 발생했습니다.');
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

.main-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.body-layout {
  display: flex;
  flex-grow: 1;
}

.content {
  flex-grow: 1;
}

.reviewAll {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.reviewHeader {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: 600;
  margin-top: 1%;
  color: #000000;
}

.reviewHeader img {
  margin-right: 10px; /* 이미지와 텍스트 간의 간격 조절 */
}

.reviewContent {
  margin-top: 2%;
}

.buttonContainer {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
  gap: 10px; /* 버튼 간 간격 조절 */
}

.reviewBox {
  width: 100%;
  padding: 10px;
}

.reviewIcon {
  width: 30px; /* 아이콘 크기 조정 */
}

.reviewContent table {
  width: 100%;
  border-collapse: collapse;
}

.reviewContent th,
.reviewContent td {
  border-top: 1px solid #ddd; /* 테이블 상단에만 선 추가 */
  border-bottom: 1px solid #ddd; /* 테이블 하단에만 선 추가 */
  padding: 8px;
  text-align: left;
}

.reviewContent th {
  background-color: #f2f2f2;
  color: #333;
}

.modal-dialog {
  max-width: 500px;
  margin: 1.75rem auto;
}

.modal-header,
.modal-footer {
  border-bottom: 1px solid #dee2e6;
}

.modal-footer {
  border-top: 1px solid #dee2e6;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
  table-layout: fixed;
}

th, td {
  padding: 6px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  word-wrap: break-word;
}

th {
  background-color: #f8f8f8;
}

td {
  font-size: 14px;
}

tr:hover {
  background-color: #f1f1f1;
}

</style>
