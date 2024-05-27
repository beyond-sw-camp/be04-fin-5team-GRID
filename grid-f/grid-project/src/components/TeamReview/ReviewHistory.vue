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
          <th>부서 명</th>
          <th>평가 명</th>
          <th>작성일</th>
          <th>평가 기간</th>
          <th>분기</th>
          <th>대상자</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="review in filteredReviews" :key="review.id">
          <td>{{ review.reviewerName }}</td>
          <td>{{ review.departmentName }}</td>
          <td>{{ review.content }}</td>
          <td>{{ formatDate(review.writeTime) }}</td>
          <td>{{ review.endTime }}</td>
          <td>{{ review.quarter }}</td>
          <td>{{ review.revieweeName }}</td>
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
    const reviewList = response.data.result;
    await Promise.all(reviewList.map(async review => {
      // 리뷰어 정보 가져오기
      const reviewerResponse = await axios.get(`http://localhost:8080/users/id/${review.reviewerId}`);
      const revieweeResponse = await axios.get(`http://localhost:8080/users/id/${review.revieweeId}`);
      review.reviewerName = reviewerResponse.data.result.name;
      review.departmentId = reviewerResponse.data.result.departmentId;
      review.revieweeName = revieweeResponse.data.result.name;

      // 부서명 정보 가져오기
      const departmentResponse = await axios.get(`http://localhost:8080/department/${review.departmentId}`);
      review.departmentName = departmentResponse.data.result.departmentName;
    }));
    reviews.value = reviewList; 
    console.log(reviews.value);
  } catch (error) {
    console.error('평가 내역을 가져오는 중 오류 발생:', error);
  }
};

onMounted(fetchReviews); 

const filteredReviews = computed(() => {
  return reviews.value.filter(review => 
    review.reviewerName.toLowerCase().includes(searchQuery.value.toLowerCase())
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
