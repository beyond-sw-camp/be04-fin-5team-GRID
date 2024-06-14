<template>
  <div class="reviewDetailContainer">
    <div class="reviewTitle">
      <h1 class="mb-1"><i class="bi bi-award fs-3"></i>&nbsp; 종합 업적 평가 상세 조회 &nbsp;&nbsp;<i class="bi bi-info-circle fs-5 mt-5" id="popover-target-1" style="color: darkgoldenrod; cursor: pointer;" @click="showModal('guideReview')"></i></h1>
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
      <table class="table">
        <thead>
        <tr>
          <th>No</th>
          <th>목표</th>
          <th>실행과제</th>
          <th>측정지표</th>
          <th>세부계획</th>
          <th>가중치</th>
          <th>추진실적</th>
          <th>중간 평가 등급</th>
          <th>중간 평가 점수</th>
          <th>연말 평가 등급</th>
          <th>연말 평가 점수</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in finReviewItemList" :key="item.id">
          <td>{{ index + 1 }}</td>
          <td>
            {{ item.goal }}
          </td>
          <td>
            {{ item.actionItem }}
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
            {{ item.performance }}
          </td>
          <td>
            {{ gradeMapping[midReviewItemList[index].superiorId] }}
          </td>
          <td>
            {{ midReviewItemList[index].superiorScore }}
          </td>
          <td>
            {{ gradeMapping[item.superiorId]}}
          </td>
          <td>
            {{ item.superiorScore}}
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="scoreTableContainer">
      <span>중간 점수 * 0.3 + 연말 점수 * 0.7 = 종합 점수</span>
      <div class="scoreTable">
        <table>
          <thead>
          <tr>
            <td>중간 점수</td>
            <td>{{ Math.round(midScore * 100) / 100 }}</td>
            <td>연말 점수</td>
            <td>{{ Math.round(finScore * 100) / 100 }}</td>
            <td>종합 점수</td>
            <td>{{ Math.round(totalDetail.totalScore * 100) / 100 }}</td>
          </tr>
          <tr>
            <td>중간 등급</td>
            <td>{{ midGrade }}</td>
            <td>연말 등급</td>
            <td>{{ finGrade }}</td>
            <td>종합 등급</td>
            <td>{{ totalDetail.totalGrade }}</td>
          </tr>
          </thead>
        </table>
      </div>
    </div>

    <!-- 가이드 모달 -->
    <div class="modal fade" id="guideReview" tabindex="-1" aria-labelledby="guideManageLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title" id="guideManageLabel">종합 업적 평가 가이드</h4>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div class="example-content">
              <p>종합 업적 평가를 볼 수 있는 페이지 입니다. </p>
              <hr>
              <p>
                해당연도 확정된 중간 평가와 연말 평가를 합산한 결과를 볼 수 있습니다.
              </p>
              <p>
                종합 업적 평가의 점수는<br>
                중간 업적 평가의 점수 0.3, 연말 업적 평가의 점수 0.7 비율로 계산됩니다.
              </p>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';

const router = useRouter();

const midReviewItemList = ref([]);
const finReviewItemList = ref([]);

const midScore = ref(null);
const finScore = ref(null);
const midGrade = ref(null);
const finGrade = ref(null);

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

const showModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.show();
};

const fetchReviewDetail = async () => {
  try {
    const route = router.currentRoute.value;
    const id = route.params.id;

    // 종합평가 먼저 조회해서 넣기
    const responseTotalReview = await axios.get(`/api/total-performance-review/${id}`)

    const total = responseTotalReview.data.findTotal;

    totalDetail.value = {
      id: total.id,
      year: total.year,
      reviewName: total.reviewName,
      midtermId: total.midtermId,
      finalId: total.finalId,
      totalGrade: total.totalGrade ? total.totalGrade.grade : '없음',
      totalScore: total.totalGrade ? total.totalGrade.score : '없음',
      revieweeName: total.reviewee ? total.reviewee.employeeName : '없음',
      reviewerName: total.reviewer ? total.reviewer.employeeName : '없음'
    };


    // 중간 평가 조회하기
    const midtermId = totalDetail.value.midtermId;
    const responseMid = await axios.get(`/api/performance-review/detail/${midtermId}`);

    if (responseMid.data.findDetailReview) {
      const reviewMid = responseMid.data.findDetailReview;
      midReviewItemList.value = reviewMid.reviewItemList;
    }

    // 중간 평가 점수와 등급
    let scoreM = 0;
    for (const midItem of midReviewItemList.value) {
      scoreM += midItem.superiorScore;
    }

    midScore.value = scoreM;
    midGrade.value = mappingScoreGrade(scoreM);


    // 연말 평가 조회하기
    const finalId = totalDetail.value.finalId;
    const responseFin = await axios.get(`/api/performance-review/detail/${finalId}`);
    if (responseFin.data.findDetailReview) {
      const reviewFin = responseFin.data.findDetailReview;
      finReviewItemList.value = reviewFin.reviewItemList;
    }

    // 연말 평가 점수와 등급
    let scoreF = 0;
    for (const finItem of finReviewItemList.value) {
      scoreF += finItem.superiorScore;
    }

    finScore.value = scoreF;
    finGrade.value = mappingScoreGrade(scoreF);

  } catch (error) {
    console.error('에러 발생:', error);
    alert("종합 평가를 조회할 수 없습니다.")
    router.push(`/performance-review/total`);
  }
};

onMounted(() => {
  fetchReviewDetail();
});

const mappingScoreGrade = (score) => {

  if (score <= 95 && score > 85)
    return 'S';
  if (score <= 85 && score > 75)
    return 'A'
  if (score <= 75 && score > 65)
    return 'B+'
  if (score <= 65 && score > 55)
    return 'B'
  if (score <= 55)
    return 'C'
}

const gradeMapping = {
  1: 'S', // S
  2: 'A',  // A
  3: 'B+',  // B+
  4: 'B',  // B
  5: 'C'   // C
};

</script>

<style scoped>
.reviewDetailContainer {
  display: grid;
  grid-template-rows: 18% 18% 1% minmax(40%, auto) 3% 10% 13%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.reviewTitle {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
  margin-left: -0.5%;
  color: #000000;
  display: grid;
  grid-template-columns: 25% 4%;
  align-items: center;
  min-width: 1300px;
}

.reviewTitle h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.reviewIcon {
  width: 80%;
}

.guide {
  width: 60%;
  height: 25px;
  grid-column: 2;
  margin: 0;
  cursor: pointer;
}

.titleTableContainer {
  grid-row-start: 2;
  grid-column-start: 2;
  grid-column-end: 3;
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

.performanceTableContainer {
  grid-row-start: 4;
  grid-column-start: 2;
  grid-column-end: 3;
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
  grid-column-start: 2;
  grid-column-end: 3;
  display: grid;
  grid-template-columns: 50% 50%;
}

.scoreTable {
  grid-column-start: 2;
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
  text-align: center;
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
  text-align: center;
}

.performanceTableContainer th:nth-child(7),
.performanceTableContainer td:nth-child(7) {
  min-width: 500px; /* 추진실적 */
}

.performanceTableContainer th:nth-child(8),
.performanceTableContainer td:nth-child(8) {
  min-width: 90px; /* 중간 평가 등급 */
  text-align: center;
}

.performanceTableContainer th:nth-child(9),
.performanceTableContainer td:nth-child(9) {
  min-width: 90px; /* 중간 평가 점수 */
  text-align: center;
}

.performanceTableContainer th:nth-child(10),
.performanceTableContainer td:nth-child(10) {
  min-width: 90px; /* 연말 평가 등급 */
  text-align: center;
}

.performanceTableContainer th:nth-child(11),
.performanceTableContainer td:nth-child(11) {
  min-width: 90px; /* 연말 평가 점수 */
  text-align: center;
}
</style>