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
          <th>직원</th>
          <th>부서명</th>
          <th>작성일</th>
          <th>평가 기간</th>
          <th>분기</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="review in filteredReviews" :key="review.id">
          <td>{{ review.id }}</td>
          <td>{{ review.content }}</td>
          <td>{{ review.departmentName }}</td>
          <td>{{ formatDate(review.writeTime) }}</td>
          <td>{{ review.endTime }}</td>
          <td>{{ review.quarter }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const searchQuery = ref('');
const reviews = ref([]);

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
  return date.toLocaleDateString(); 
};
</script>

<style scoped>
/* No custom styles applied */
</style>
