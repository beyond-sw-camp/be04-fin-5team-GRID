<template>
  <div class="reviewDetailContainer">
    <div class="reviewTitle">
      <img class="reviewIcon" src="@/assets/icons/goal_icon.png">
      <h1>종합 업적 평가 조회</h1>
    </div>
    <div class="titleTableContainer">
      <table>
        <tbody>
        <tr>
          <td><label>연도</label></td>
          <td colspan="3">{{ totalDetail.year }}</td>
        </tr>
        <tr>
          <td><label>평가명</label></td>
          <td colspan="3">{{ totalDetail.reviewName }}</td>
        </tr>
        <tr>
          <td><label>평가대상자</label></td>
          <td>{{ totalDetail.revieweeName }}</td>
          <td><label>평가자</label></td>
          <td>{{ totalDetail.reviewerName }}</td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="performanceTableContainer">
        <table>
          <thead>
          <tr>
            <th>No</th>
            <th>목표</th>
            <th>실행과제</th>
            <th>측정지표</th>
            <th>세부계획</th>
            <th>가중치</th>
            <th>추진실적</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in reviewItemList" :key="item.id">
            <td>{{ index + 1 }}</td>
            <td>
              {{ item.goal }}
            </td>
            <td>
              {{item.actionItem}}
            </td>
            <td>
              {{ item.metric }}
            </td>
            <td>
              {{ item.detailPlan }}
            </td>
            <td>
              {{ item.weight }}
            </td>
            <td>
              {{ item.performance}}
            </td>
          </tr>
          </tbody>
        </table>
    </div>
    <div class="scoreTableContainer">
      <table>
        <thead>
        <tr>
          <td>점수</td>
          <td>{{totalDetail.totalScore}}</td>
        </tr>
        <tr>
          <td>등급</td>
          <td>{{totalDetail.totalGrade}}</td>
        </tr>
        </thead>
      </table>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';

const router = useRouter();

const reviewItemList = ref([]);

const totalDetail = ref({
  id: '',
  year: '',
  reviewName: '',
  midtermId: '',
  finalId: '',
  totalGrade: '',
  totalScore: '',
  reviewee: '',
  reviewer: ''
});

// member, leader, manager
const userRole = ref(null);

const fetchReviewDetail = async () => {
  try {
    const route = router.currentRoute.value;
    const id = route.params.id;

    // 종합평가 먼저 조회해서 넣고
    const responseTotalReview = await axios.get(`http://localhost:8080/total-performance-review/${id}`)
    console.log(responseTotalReview.data.findTotal);

    const total =  responseTotalReview.data.findTotal;

    totalDetail.value = {
      id: total.id,
      year: total.year,
      reviewName: total.reviewName,
      midtermId: total.midtermId,
      finalId: total.finalId,
      totalGrade: total.totalGrade? total.totalGrade.grade : '없음',
      totalScore: total.totalGrade? total.totalGrade.score : '없음',
      revieweeName: total.reviewee ? total.reviewee.employeeName : '없음',
      reviewerName: total.reviewer ? total.reviewer.employeeName : '없음'
    };

    console.log(totalDetail.value);

    // 연말 평가 조회하기
    const finalId = totalDetail.value.id;
    const response = await axios.get(`http://localhost:8080/performance-review/detail/${finalId}`);
    if(response.data.findDetailReview) {
      const review = response.data.findDetailReview;
      reviewItemList.value = review.reviewItemList;
    }

  } catch (error) {
    console.error('에러 발생:', error);
  }
};

onMounted(() => {
  fetchReviewDetail();
});

// 점수 계산
const scoreMapping = {
  1: 95, // S
  2: 85,  // A
  3: 75,  // B+
  4: 65,  // B
  5: 55   // C
};

</script>

<style scoped>
.reviewDetailContainer {
  display: grid;
  grid-template-rows: 18% 21% 1% minmax(40%, auto) 3% 8% 13%;
  grid-template-columns: 10% 40% 40% 10%;
  height: 100%;
}

.reviewTitle {
  grid-column-start: 2;
  grid-column-end: 4;
  font-size: 12px;
  font-weight: 0;
  margin-top: 2%;
  color: #000000;
  display: grid;
  grid-template-columns: 3% 97%;
  align-items: center;
}

.reviewTitle h1 {
  margin-left: 0.5%;
}

.reviewIcon {
  width: 80%;
}

.titleTableContainer {
  grid-row-start: 2;
  grid-column-start: 2;
  grid-column-end: 4;
  margin-top: 20px;
  font-size: 12px;
}

.titleTableContainer td {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 6px;
  vertical-align: middle;
}

.titleTableContainer label {
  font-weight: bold;
}

.GoalButtonContainer {
  grid-row-start: 3;
  grid-column-start: 2;
  grid-column-end: 3;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 10px;
  margin-top: 20px;
}


.performanceTableContainer {
  grid-row-start: 4;
  grid-column-start: 2;
  grid-column-end: 4;
  /* margin-top: 20px; */
  font-size: 12px;

  overflow-x: auto;
  /* 가로 스크롤을 필요로 하는 경우 */
  overflow-y: auto;
  /* 세로 스크롤을 필요로 하는 경우 */
  max-height: 500px;
  /* 원하는 높이로 설정 */
}

.scoreTableContainer {
  grid-row-start: 6;
  grid-column-start: 3;
  grid-column-end: 4;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 6px;
  vertical-align: middle;
}

th {
  position: sticky;
  top: 0;
  background-color: #f2f2f2;
  //z-index: 1;
}

.performanceTableContainer td {
  height: 100px;
}
.performanceTableContainer td {
  height: 100px;
}

.performanceTableContainer td input[type="text"],
.performanceTableContainer td input[type="int"],
.performanceTableContainer td select {
  width: 90%;
  height: 100%;
}

.performanceTableContainer th:nth-child(1),
.performanceTableContainer td:nth-child(1) {
  min-width: 30px; /* No */
}

.performanceTableContainer th:nth-child(2),
.performanceTableContainer td:nth-child(2) {
  min-width: 500px; /* *목표 */
}

.performanceTableContainer th:nth-child(3),
.performanceTableContainer td:nth-child(3) {
  min-width: 500px; /* 실행과제 */
}

.performanceTableContainer th:nth-child(4),
.performanceTableContainer td:nth-child(4) {
  min-width: 500px; /* 측정지표 */
}

.performanceTableContainer th:nth-child(5),
.performanceTableContainer td:nth-child(5) {
  min-width: 500px; /* 세부계획 */
}

.performanceTableContainer th:nth-child(6),
.performanceTableContainer td:nth-child(6) {
  min-width: 70px; /* 가중치 */
}

.performanceTableContainer th:nth-child(7),
.performanceTableContainer td:nth-child(7) {
  min-width: 500px; /* 추진실적 */
}

.performanceTableContainer th:nth-child(8),
.performanceTableContainer td:nth-child(8) {
  min-width: 90px; /* 자기 평가 */
}

.performanceTableContainer th:nth-child(9),
.performanceTableContainer td:nth-child(9) {
  min-width: 100px; /* 자기 평가 점수 */
}

.performanceTableContainer th:nth-child(10),
.performanceTableContainer td:nth-child(10) {
  min-width: 500px; /* 자기 평가 의견 */
}

.performanceTableContainer th:nth-child(11),
.performanceTableContainer td:nth-child(11) {
  min-width: 90px; /* 상급 평가 */
}

.performanceTableContainer th:nth-child(12),
.performanceTableContainer td:nth-child(12) {
  min-width: 100px; /* 상급 평가 점수 */
}
</style>