<template>
  <div class="reviewAddContainer">
    <div class="reviewTitle">
      <h1 class="mb-1"><i class="bi bi-award fs-3"></i>&nbsp; 연말 업적 평가 작성 &nbsp;&nbsp;<i class="bi bi-info-circle fs-5 mt-5" id="popover-target-1" style="color: darkgoldenrod; cursor: pointer;" @click="showModal('guideReview')"></i></h1>
    </div>
    <div class="titleTableContainer">
      <table>
        <tbody>
        <tr>
          <td><label>연도</label></td>
          <td>{{ reviewDetail.year }}</td>
          <td><label>유형</label></td>
          <td>{{ reviewDetail.type }}</td>
        </tr>
        <tr>
          <td><label>평가명</label></td>
          <td colspan="3">{{ reviewDetail.reviewName }}</td>
        </tr>
        <tr>
          <td><label>작성자</label></td>
          <td>{{ reviewDetail.writerName }}</td>
          <td><label>작성시간</label></td>
          <td>{{ reviewDetail.writeTime }}</td>
        </tr>
        <tr>
          <td><label>결재자</label></td>
          <td>{{ reviewDetail.approverName }}</td>
          <td><label>결재시간</label></td>
          <td>{{ reviewDetail.approvalTime }}</td>
        </tr>
        <tr>
          <td><label>문서 상태</label></td>
          <td colspan="3">{{ reviewDetail.status }}</td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="GoalButtonContainer" v-if="!isReadOnly">
      <div class="buttonWrapper">
        <button class="performanceBtn" @click="memberSave()">저장</button>
        <button class="performanceBtn" @click="submit()">상신</button>
      </div>
    </div>
    <div class="performanceTableContainer">
      <div>
        <table class="table">
          <thead>
          <tr>
            <th>No</th>
            <th>업무명</th>
            <th>목표</th>
            <th>측정지표</th>
            <th>계획</th>
            <th>가중치</th>
            <th>추진실적</th>
            <th>자기 평가</th>
            <th>자기 평가 점수</th>
            <th>자기 평가 의견</th>
            <th>상급 평가</th>
            <th>상급 평가 점수</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in reviewItemList" :key="item.id">
            <td>{{ index + 1 }}</td>
            <td>
              {{ item.goal }}
            </td>
            <td>
              <span>{{ item.actionItem }}</span>
            </td>
            <td>
              {{ item.metric }}
            </td>
            <td>
              <span>{{ item.detailPlan }}</span>
            </td>
            <td>
              {{ item.weight }}
            </td>
            <td>
              <input
                  v-if="!isReadOnly"
                  v-model="item.performance"
                  type="text"
              />
              <span v-else>{{ item.performance }}</span>
            </td>
            <td>
              <select v-if="!isReadOnly" v-model="item.selfId" class="form-select" @change="updateSelfScore(item)">
                <option value="1">S</option>
                <option value="2">A</option>
                <option value="3">B+</option>
                <option value="4">B</option>
                <option value="5">C</option>
              </select>
              <span v-else>{{ gradeMapping[item.selfId] || 0 }}</span>
            </td>
            <td>
              {{ item.selfScore }}
            </td>
            <td>
              <input
                  v-if="!isReadOnly"
                  v-model="item.selfComment"
                  type="text"
              />
              <span v-else>{{ item.selfComment }}</span>
            </td>
            <td>
              <span>
                {{ gradeMapping[3] || 0 }}
              </span>
            </td>
            <td>
              <span>
                {{ item.weight * 75 * 0.01 }}
              </span>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 가이드 모달 -->
    <div class="modal fade" id="guideReview" tabindex="-1" aria-labelledby="guideManageLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title" id="guideManageLabel">연말 평가 가이드</h4>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div class="example-content">
              <p>연말 업적 평가를 작성하는 페이지 입니다. </p>
              <hr>
              <p>
                팀원은 1년에 한 번 연말 평가를 작성할 수 있습니다.<br>
                연말 업적 평가는 팀원인 직원만 12월에 작성 가능합니다.<br>
                중간 평가가 확정되어야 연말 평가를 작성할 수 있습니다.
                업적 평가의 점수는 가중치와 등급을 계산하여 부여됩니다.
                (현재는 모든 기간 작성할 수 있도록 열어두었습니다.)
              </p>
              <p>1. 승인 상태</p>
              <p>&nbsp;1-1. 작성 중: 팀원이 목표를 작성하고 있는 상태</p>
              <p>&nbsp;1-2. 상신: 팀원이 목표를 작성하고 결재를 올린 상태</p>
              <p>&nbsp;1-3. 확인 중: 팀장이 목표를 보고 확정을 판단 중인 상태</p>
              <p>&nbsp;1-4. 확정: 팀장이 팀원의 평가를 확정한 상태 </p>
              <p>2. 저장 버튼</p>
              <p>&nbsp;2-1. 작성한 평가를 저장할 수 있습니다..</p>
              <p>3. 상신 버튼</p>
              <p>&nbsp;3-1. 모든 내용을 필수로 작성해야 상신 가능합니다.</p>
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
import {ref, onMounted, computed} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';
import {useStore} from 'vuex';

const store = useStore();
const user = computed(() => store.state.user);

const router = useRouter();

const reviewItemList = ref([]);

const reviewDetail = ref({
  id: '',
  year: '',
  type: '',
  reviewName: '',
  writer: '',
  createdTime: '',
  approver: '',
  approvalTime: '',
  status: ''
});

const isReadOnly = ref(true);

const showModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.show();
};

// 현재 시간
function getCurrentDateTimeString() {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

const fetchReviewAdd = async () => {
  try {
    // 올해 생성된 연말 평가가 있으면 조회 아니라면 새로 생성

    const currentYear = new Date().getFullYear();   // 올해 년도
    const currentTime = getCurrentDateTimeString()  // 현재 시간

    const responseReview = await axios.get(`/api/performance-review/final/${currentYear}/${user.value.id}`);

    const responseGoal = await axios.get(`/api/review-goal/${currentYear}/${user.value.id}`);

    // 승인된 목표만 생성 가능
    if(!responseGoal || responseGoal.data.findGoal.approvalStatus !== 'A'){
      throw new Error('평가 목표가 승인되지 않았습니다.');
    }

    const responseMid = await axios.get(`/api/performance-review/mid/${currentYear}/${user.value.id}`)

    // 승인된 중간 평가가 없으면 예외
    if(!responseMid || responseMid.data.findReview.approvalStatus !== 'V') {
      throw new Error('중간 평가가 확정되지 않았습니다.');
    }



    if (!responseReview.data.findReview) {
      // 생성된 연말 평가 없을 때
      const sendData = {
        type: "F",
        year: currentYear,
        reviewName: `${currentYear} 인사평가`,
        writerId: user.value.id
      }

      const responseAdd = await axios.post(
          `/api/performance-review`,
          sendData
      );

      const id = responseAdd.data.performanceReview.id
      const response = await axios.get(`/api/performance-review/detail/${id}`);

      const review = response.data.findDetailReview;
      reviewItemList.value = review.reviewItemList;

      reviewDetail.value = {
        id: review.id,
        year: review.year,
        type: getType(review.type),
        reviewName: review.reviewName,
        writerName: review.writer ? review.writer.employeeName : '없음',
        writeTime: review.writeTime || '없음',
        approverName: review.approver ? review.approver.employeeName : '없음',
        approvalTime: review.approvalTime || '없음',
        status: getApprovalStatus(review.approvalStatus)
      };
    } else {
      // 생성된 평가 있을 때
      const id = responseReview.data.findReview.id
      const response = await axios.get(`/api/performance-review/detail/${id}`);

      const review = response.data.findDetailReview;
      reviewItemList.value = review.reviewItemList;

      reviewDetail.value = {
        id: review.id,
        year: review.year,
        type: getType(review.type),
        reviewName: review.reviewName,
        writerName: review.writer ? review.writer.employeeName : '없음',
        writeTime: review.writeTime || '없음',
        approverName: review.approver ? review.approver.employeeName : '없음',
        approvalTime: review.approvalTime || '없음',
        status: getApprovalStatus(review.approvalStatus)
      };
    }

  } catch (error) {
    console.error('에러 발생:', error);
    alert('평가를 생성할 수 없습니다.')
    router.push(`/performance-review`);
  }
};

const getApprovalStatus = (status) => {
  switch (status) {
    case 'IP':
      isReadOnly.value = false;
      return '작성 중';
    case 'S':
      return '상신';
    case 'R':
      return '확인 중';
    case 'C':
      return '확인';
    case 'V':
      return '확정';
    default:
      return '기타';
  }
};

const getType = (type) => {
  if (type === 'M') {
    return '중간 평가';
  } else {
    return '연말 평가';
  }
};

onMounted(() => {
  try {
    // 12월에만 작성 가능, 시연을 위해 주석처리
    // const currentMonth = new Date().getMonth() + 1;
    // if (currentMonth !== 12){
    //   alert('현재 연말 평가 작성기간이 아닙니다.')
    //   router.push('/performance-review');
    // }

    if (user.value.duties.dutiesName === '팀원')
      fetchReviewAdd();

  } catch (error) {
    console.log("에러 발생: ", error);
  }
});

// 점수 계산
const scoreMapping = {
  1: 95, // S
  2: 85,  // A
  3: 75,  // B+
  4: 65,  // B
  5: 55   // C
};

const gradeMapping = {
  1: 'S', // S
  2: 'A',  // A
  3: 'B+',  // B+
  4: 'B',  // B
  5: 'C'   // C
};

// 자기평가 변경
const updateSelfScore = (item) => {
  const baseScore = scoreMapping[item.selfId] || 0;
  const weight = parseFloat(item.weight) || 0;
  item.selfScore = baseScore * weight / 100; // 가중치를 백분율로 계산
};

// 팀원 저장(in-progress)
async function memberSave() {
  if (reviewDetail.value.status === '작성 중') {
    if (confirm("평가를 저장하시겠습니까?")) {

      const sendData = {
        reviewId: reviewDetail.value.id,
        performanceReviewItemList: reviewItemList.value.map(item => ({
          id: item.id,
          goal: item.goal,
          actionItem: item.actionItem,
          metric: item.metric,
          detailPlan: item.detailPlan,
          weight: item.weight,
          performance: item.performance,
          selfId: item.selfId,
          selfScore: item.selfScore,
          selfComment: item.selfComment,
          superiorId: item.superiorId,
          superiorScore: item.superiorScore,
          reviewId: reviewDetail.value.id
        }))
      };

      try {
        await axios.put(
            `/api/performance-review/in-progress`,
            sendData
        );
        alert('평가를 저장했습니다.')
        window.location.reload();
      } catch (error) {
        console.error('Error sending data:', error);
        alert('평가를 저장할 수 없습니다.')
      }
    }
  } else {
    alert('평가를 저장할 수 없습니다.')
  }
}

// 팀원 상신(submit)
async function submit() {
  if (reviewDetail.value.status === '작성 중') {
    if (confirm("평가를 상신하시겠습니까?")) {

      // 필수 값이 입력되지 않은 경우
      for (const item of reviewItemList.value) {
        if (!item.goal || !item.actionItem || !item.metric || item.weight === undefined || item.weight === 0
            || item.weight === null || !item.detailPlan || !item.performance || !item.selfComment
            || !item.selfId || !item.selfScore) {
          alert('상신 시 모든 필수 값을 입력해야 합니다.');
          return;
        }
      }

      const sendData = {
        reviewId: reviewDetail.value.id,
        performanceReviewItemList: reviewItemList.value.map(item => ({
          id: item.id,
          goal: item.goal,
          actionItem: item.actionItem,
          metric: item.metric,
          detailPlan: item.detailPlan,
          weight: item.weight,
          performance: item.performance,
          selfId: item.selfId,
          selfScore: item.selfScore,
          selfComment: item.selfComment,
          superiorId: item.superiorId,
          superiorScore: item.superiorScore,
          reviewId: reviewDetail.value.id
        }))
      };

      try {
        await axios.put(
            `/api/performance-review/submit`,
            sendData
        );

        alert('평가를 상신했습니다.')
        window.location.reload();
      } catch (error) {
        console.error('Error sending data:', error);
        alert('평가를 상신할 수 없습니다.')
      }
    }
  } else {
    alert('평가를 상신할 수 없습니다.')
  }
}

</script>

<style scoped>
.reviewAddContainer {
  display: grid;
  grid-template-rows: 18% 23% 7% 39% 13%;
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
  grid-template-columns: 24% 4%;
  align-items: center;
  min-width: 1200px;
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

.GoalButtonContainer {
  grid-row-start: 3;
  grid-column-start: 2;
  grid-column-end: 3;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 10px;
}

.buttonWrapper {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 10px; /* 버튼 사이의 간격 설정 */
  width: 30%;
}

.performanceBtn {
  grid-column-start: 6;
  margin-left: 2%;
  width: 60px;
  background-color: #088A85;
  color: white;
  padding: 5px 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
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
  text-align: center;
  //background-color: #f2f2f2;
  //z-index: 1;
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
  min-width: 90px; /* 자기 평가 */
  text-align: center;
}

.performanceTableContainer th:nth-child(9),
.performanceTableContainer td:nth-child(9) {
  min-width: 100px; /* 자기 평가 점수 */
  text-align: center;
}

.performanceTableContainer th:nth-child(10),
.performanceTableContainer td:nth-child(10) {
  min-width: 500px; /* 자기 평가 의견 */
}

.performanceTableContainer th:nth-child(11),
.performanceTableContainer td:nth-child(11) {
  min-width: 90px; /* 상급 평가 */
  text-align: center;
}

.performanceTableContainer th:nth-child(12),
.performanceTableContainer td:nth-child(12) {
  min-width: 100px; /* 상급 평가 점수 */
  text-align: center;
}
</style>