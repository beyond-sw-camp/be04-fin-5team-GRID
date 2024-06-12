<template>
  <div class="reviewAll">
    <div class="reviewHeader">
      <h1 class="mb-1"><i class="bi bi-person-check fs-3"></i>&nbsp; 동료 평가 생성</h1>
    </div>
    <div class="buttonContainer">
      <button type="button" class="deleteBtn btn-custom-1" @click="deleteSelectedReviews"><span>선택 항목 삭제</span></button>
      <button type="button" class="addBtn btn-custom-1" data-bs-toggle="modal" data-bs-target="#addQuestionModal"><span>항목 추가</span></button>
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
            <div class="button-container">
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
import router from '@/router/router';

const reviews = ref([]);
const selectedReviews = ref([]);
const newReviewText = ref('');
const userRole = ref('');
const userId = ref('');

const fetchReviews = async () => {
  try {
    const response = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/review/list');
    reviews.value = response.data.result;
    reviews.value.sort((a, b) => a.id - b.id);
  } catch (error) {
    console.error('에러 발생:', error);
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
    userId.value = decodedToken?.id || '';
  }

  if (userRole.value !== 'ROLE_ADMIN') {
    alert('접근 권한이 없습니다.');
    router.go(-1);
  } else {
    fetchReviews();
  }
});

const deleteSelectedReviews = async () => {
  if (selectedReviews.value.length === 0) {
    alert('하나 이상의 항목을 선택해주세요.');
    return;
  }

  const confirmDeletion = confirm('선택한 항목을 삭제하시겠습니까?');
  if (!confirmDeletion) {
    return;
  }

  try {
    for (const reviewId of selectedReviews.value) {
      await axios.delete(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/review/list/${reviewId}`);
    }
    reviews.value = reviews.value.filter(review => !selectedReviews.value.includes(review.id));
    selectedReviews.value = [];
    alert('삭제되었습니다.');
  } catch (error) {
    console.error('에러 발생:', error);
    alert('평가되지 않는 항목만 삭제할 수 있습니다.');
  }
};


const addReview = async () => {
  if (!newReviewText.value) {
    alert('질문 내용을 입력해주세요.');
    return;
  }

  try {
    const response = await axios.post('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/review/list', {
      listName: newReviewText.value
    });
    reviews.value.push(response.data.result);
    newReviewText.value = '';
    alert('추가되었습니다.');
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

.reviewAll {
  display: grid;
  grid-template-rows: 18% 11% 6% 53% 8%;
  grid-template-columns: 10% 80% 10%;
  height:100%;
  padding: 0;
  font-family: 'IBMPlexSansKR-Regular';
}

.reviewHeader {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
  margin-left: -0.5%;
  color: #000000;
  display: grid;
  grid-template-columns: 24% 4%;
  align-items: center;
}

.reviewHeader img {
  width: 25px;
}

.reviewHeader h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.reviewContent {
  margin-top: 2%;
}

.buttonContainer {
  grid-row-start: 3;
  grid-column-start: 2;
  align-items: center;
  margin-bottom: 20px;
  display: grid;
  grid-template-columns:80% 9% 2% 9%;
}

.reviewBox {
  grid-row-start: 4;
  grid-column-start: 2;
  width: 100%;
  border-collapse: collapse;
  height:10px;
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

.modal-footer {
  border-top: 1px solid #dee2e6;
  display: flex;
  justify-content: center;
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
.deleteBtn {
  width: 100%;
  background-color: #088A85;
  color: white;
  padding: 5px 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-style: bold;
  grid-column-start: 2;
}

.btn btn-primary{
  display: flex;
  justify-content: center;
}

.addBtn {
  width: 100%;
  background-color: #088A85;
  color: white;
  padding: 5px 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-style: bold;
  grid-column-start: 4;
}

.deleteBtn:hover, .addBtn:hover {
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

.button-container {
  display: flex;
  justify-content: center;
  width: 100%;
  padding-top: 10px;
}
</style>
