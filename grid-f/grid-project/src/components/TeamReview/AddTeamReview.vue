<template>
  <div class="content">
    <div class="reviewAll">
      <div class="reviewHeader">
        <img class="reviewIcon" src="@/assets/list-check.png" alt="list-check" />
        <h3 class="reviewTitle">평가 생성</h3>
      </div>
      <div class="reviewContent">
        <div class="buttonContainer">
          <button type="button" class="deleteBtn" @click="deleteSelectedReviews">선택 항목 삭제</button>
          <button type="button" class="addBtn" data-bs-toggle="modal" data-bs-target="#addQuestionModal">항목 추가</button>
        </div>
        <div class="reviewBox">
          <table>
            <thead>
              <tr>
                <th style="width: 5%;">선택</th>
                <th style="width: 10%;">항목</th>
                <th style="width: 85%;">내용</th>
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

.content {
  flex-grow: 1;
  margin-right: 10%;
  margin-left: 10%;
  font-family: 'IBMPlexSansKR-Regular';
}

.reviewAll {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.reviewHeader {
  display: flex;
  align-items: center;
  font-size: 25px;
  margin-top: 8%;
  color: #000000;
}

.reviewHeader img {
  width: 20px;
  margin-right: 10px;
  margin-bottom: 5px;
}

.reviewTitle {
  font-weight: 600 !important;
}

.reviewContent {
  margin-top: 2%;
}

.buttonContainer {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
  gap: 10px;
}

.reviewBox {
  width: 100%;
  padding: 10px;
}

.reviewContent table {
  width: 100%;
  border-collapse: collapse;
}

.reviewContent th,
.reviewContent td {
  border-top: 1px solid #ddd;
  border-bottom: 1px solid #ddd;
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

/* 버튼 스타일 */
.deleteBtn, .addBtn {
  background-color: #088A85;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.deleteBtn:hover, .addBtn:hover {
  background-color: #065f5b;
}

.btn-close {
  background-color: #888;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 50%;
  cursor: pointer;
}

.btn-close:hover {
  background-color: #555;
}
</style>
