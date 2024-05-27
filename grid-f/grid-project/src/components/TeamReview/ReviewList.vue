<template>
  <div>
    <div>
      <img src="@/assets/logo.png" alt="Evaluation Icon" />
      <h1>전체 평가 목록</h1>
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
          <th>분기</th>
          <th>평가 상태</th>
          <th>작성 시간</th>
          <th>피평가자 ID</th>
          <th>평가자 ID</th>
          <th>평가 확인</th>
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
          <td>{{ review.reviewStatus }}</td>
          <td>{{ formatDate(review.writeTime) }}</td>
          <td>{{ review.revieweeId }}</td>
          <td>{{ review.reviewerId }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const searchQuery = ref('');
const reviews = ref([]); // 초기 빈 배열로 설정

const fetchReviews = async () => {
  try {
    const response = await axios.get('http://localhost:8080/review/history-list'); 
    reviews.value = response.data.result; 
    console.log(reviews.value);
  } catch (error) {
    console.error('평가 내역을 가져오는 중 오류 발생:', error);
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

</script>

<style scoped>
/* No custom styles applied */
</style>
