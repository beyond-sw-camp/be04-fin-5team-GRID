<template>
  <div class="reviewDetailContainer">
    <div class="reviewTitle">
      <img class="reviewIcon" src="@/assets/icons/goal_icon.png">
      <h1>업적 평가 조회</h1>
    </div>
    <div class="titleTableContainer">
      <table>
        <tbody>
        <tr>
          <td><label>연도</label></td>
          <td>{{ reviewDetail.year }}</td>
          <td><label>타입</label></td>
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
    <div class="GoalButtonContainer">
      <div v-if="isMember">
        <button @click="memberSave()">팀원 저장</button>
        <button @click="submit()">상신</button>
      </div>
      <div v-if="!isMember">
        <button @click="leaderSave()">팀장 저장</button>
        <button @click="complete()">확인</button>
        <button @click="valid()">확정</button>
      </div>
    </div>
    <div class="performanceTableContainer">
      <div v-if="isMember">
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
              <input
                  v-if="!isReadOnly"
                  v-model="item.actionItem"
                  type="text"
              />
            </td>
            <td>
              {{ item.metric }}
            </td>
            <td>
              <input
                  v-if="!isReadOnly"
                  v-model="item.detailPlan"
                  type="text"
              />
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
            </td>
            <td>
              <select v-if="!isReadOnly" v-model="item.selfId" class="form-select" @change="updateSelfScore(item)">
                <option value="1">S</option>
                <option value="2">A</option>
                <option value="3">B+</option>
                <option value="4">B</option>
                <option value="5">C</option>
              </select>
            </td>
            <td>
              {{ gradeMapping[item.selfId] || 0 }}
            </td>
            <td>
              <input
                  v-if="!isReadOnly"
                  v-model="item.selfComment"
                  type="text"
              />
            </td>
            <td>
              {{ gradeMapping[item.superiorId] || 0 }}
            </td>
            <td>
              {{ item.superiorScore }}
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div v-if="!isMember">
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
              {{ item.selfId }}
            </td>
            <td>
              {{ item.selfScore }}
            </td>
            <td>
              {{ item.selfComment }}
            </td>
            <td>
              <select v-if="!isReadOnly" v-model="item.superiorId" class="form-select"
                      @change="updateSuperiorScore(item)">
                <option value="1">S</option>
                <option value="2">A</option>
                <option value="3">B+</option>
                <option value="4">B</option>
                <option value="5">C</option>
              </select>
            </td>
            <td>
              {{ item.superiorScore }}
            </td>
          </tr>
          </tbody>
        </table>
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

// member, leader, manager
const isMember = ref(null);

const isReadOnly = ref(true);

const fetchReviewDetail = async () => {
  try {
    const route = router.currentRoute.value;
    const id = route.params.id;

    const response = await axios.get(`http://localhost:8080/performance-review/detail/${id}`)
    console.log(response.data);
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

    if (isMember) {
      if (reviewDetail.value.status === '작성 중')
        isReadOnly.value = false;
      console.log(reviewDetail.value.status);
    } else {
      if (reviewDetail.value.status === '상신' || reviewDetail.value.status === '확인 중' || goalDetail.value.status === '확인 완료')
        isReadOnly.value = false;
    }
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

const getApprovalStatus = (status) => {
  switch (status) {
    case 'IP':
      return '작성 중';
    case 'S':
      return '상신';
    case 'R':
      return '확인 중';
    case 'C':
      return '확인 완료';
    case 'V':
      return '확정 완료';
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
    console.log(user.value.duties);
    if (user.value) {
      if (user.value.duties.dutiesName === '팀원')
        isMember.value = true;

      if (user.value.duties.dutiesName === '팀장')
        isMember.value = false;

      fetchReviewDetail();

    }
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

// 상급 평가 변경
const updateSuperiorScore = (item) => {
  const baseScore = scoreMapping[item.superiorId] || 0;
  const weight = parseFloat(item.weight) || 0;
  item.superiorScore = baseScore * weight / 100; // 가중치를 백분율로 계산
};

// 팀원 저장(in-progress)
async function memberSave() {
  if (goalDetail.value.status === '작성 중') {
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
      console.log(sendData);
      try {
        await axios.put(
            `http://localhost:8080/performance-review/in-progress`,
            sendData
        );
        window.location.reload();
      } catch (error) {
        console.error('Error sending data:', error);
      }
    }
  } else {
    alert('평가를 저장할 수 없습니다.')
  }
}

// 팀원 상신(submit)
async function submit() {
  if (goalDetail.value.status === '작성 중') {
    if (confirm("평가를 상신하시겠습니까?")) {
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
      console.log(sendData);
      try {
        await axios.put(
            `http://localhost:8080/performance-review/submit`,
            sendData
        );
        window.location.reload();
      } catch (error) {
        console.error('Error sending data:', error);
      }
    }
  } else {
    alert('평가를 상신할 수 없습니다.')
  }
}

// 팀장 저장(read)
async function leaderSave() {
  if (goalDetail.value.status === '상신' || goalDetail.value.status === '확인 중') {
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
      console.log(sendData);
      try {
        await axios.put(
            `http://localhost:8080/performance-review/read`,
            sendData
        );
        window.location.reload();
      } catch (error) {
        console.error('Error sending data:', error);
      }
    }
  } else {
    alert('평가를 저장할 수 없습니다.')
  }
}

// 팀장 확인(complete)
async function complete() {
  if (goalDetail.value.status === '상신' || goalDetail.value.status === '확인 중') {
    if (confirm("평가를 확인 완료하시겠습니까?")) {
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
      console.log(sendData);
      try {
        await axios.put(
            `http://localhost:8080/performance-review/complete`,
            sendData
        );
        window.location.reload();
      } catch (error) {
        console.error('Error sending data:', error);
      }
    }
  } else {
    alert('평가를 확인 완료할 수 없습니다.')
  }
}

// 팀장 확정(complete)
async function valid() {
  if (goalDetail.value.status === '확인') {
    if (confirm("평가를 확정하시겠습니까?")) {
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
  console.log(sendData);
  try {
    await axios.put(
        `http://localhost:8080/performance-review/valid`,
        sendData
    );
    window.location.reload();
  } catch (error) {
    console.error('Error sending data:', error);
  }
    }
  } else {
    alert('평가를 확정할 수 없습니다.')
  }
}


</script>

<style scoped>
.reviewDetailContainer {
  display: grid;
  grid-template-rows: 18% 21% 8% minmax(40%, auto) 8% 13%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.reviewTitle {
  grid-column-start: 2;
  grid-column-end: 3;
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
  grid-column-end: 3;
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