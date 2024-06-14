<template>
  <div class="reviewDetailContainer">
    <div class="reviewTitle">
      <h1 class="mb-1"><i class="bi bi-award fs-3"></i>&nbsp; 업적 평가 상세 조회 &nbsp;&nbsp;<i class="bi bi-info-circle fs-5 mt-5" id="popover-target-1" style="color: darkgoldenrod; cursor: pointer;" @click="showModal('guideReview')"></i></h1>
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
    <div class="GoalButtonContainer" v-if="!isReadOnly">
      <div class="buttonWrapper">
        <button class="performanceBtn" v-if="isMember" @click="memberSave()">저장</button>
        <button class="performanceBtn" v-if="isMember" @click="submit()">상신</button>
        <button class="performanceBtn" v-if="!isMember" @click="leaderSave()">저장</button>
        <!--        <button class="performanceBtn" v-if="!isMember" @click="complete()">확인</button>-->
        <button class="performanceBtn" v-if="!isMember" @click="valid()">확정</button>
      </div>
    </div>
    <div class="performanceTableContainer">
      <div v-if="isMember">
        <table  class="table">
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
              {{ gradeMapping[item.selfId] || 0 }}
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
              <span v-else>{{ gradeMapping[item.superiorId] || 0 }}</span>
            </td>
            <td>
              {{ item.superiorScore }}
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 팀장 가이드 모달 -->
    <div class="modal fade" id="guideReview" tabindex="-1" aria-labelledby="guideManageLabel" aria-hidden="true"
         v-if="!isMember">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title" id="guideManageLabel">업적 평가 가이드</h4>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div class="example-content">
              <p>업적 평가를 하는 페이지 입니다. </p>
              <hr>
              <p>
                팀장은 상신된 업적 평가를 평가할 수 있습니다.
              </p>
              <p>1. 승인 상태</p>
              <p>&nbsp;1-1. 작성 중: 팀원이 목표를 작성하고 있는 상태</p>
              <p>&nbsp;1-2. 상신: 팀원이 목표를 작성하고 결재를 올린 상태</p>
              <p>&nbsp;1-3. 확인 중: 팀장이 목표를 보고 확정을 판단 중인 상태</p>
              <p>&nbsp;1-4. 확정: 팀장이 팀원의 평가를 확정한 상태 </p>
              <p>2. 저장 버튼</p>
              <p>&nbsp;2-1. 작성한 평가를 저장할 수 있습니다..</p>
              <p>3. 확정 버튼</p>
              <p>&nbsp;3-1. 등록한 점수를 확정할 수 있습니다.</p>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 팀원 가이드 모달 -->
    <div class="modal fade" id="guideReview" tabindex="-1" aria-labelledby="guideManageLabel" aria-hidden="true"
         v-if="isMember">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title" id="guideManageLabel">업적 평가 가이드</h4>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div class="example-content">
              <p>업적 평가를 작성하는 페이지 입니다. </p>
              <hr>
              <p>
                팀원은 1년에 중간 평가와 연말 평가를 작성할 수 있습니다.<br>
                중간 업적 평가는 팀원인 직원만 6월에 작성 가능합니다.<br>
                연말 업적 평가는 팀원인 직원만 12월에 작성 가능합니다.<br>
                평가 목표가 승인되어야 중간 평가를 작성할 수 있습니다.
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
              <p>&nbsp;2-1. 작성한 평가를 저장할 수 있습니다.</p>
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

// member, leader, manager
const isMember = ref(null);

const isReadOnly = ref(true);

const showModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.show();
};

const fetchReviewDetail = async () => {
  try {
    const route = router.currentRoute.value;
    const id = route.params.id;

    const response = await axios.get(`/api/performance-review/detail/${id}`)

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

    } else {
      if (reviewDetail.value.status === '상신' || reviewDetail.value.status === '확인 중' || reviewDetail.value.status === '확인')
        isReadOnly.value = false;
    }
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

const getApprovalStatus = (status) => {
  switch (status) {
    case 'IP':
      if (isMember.value) {
        isReadOnly.value = false;
      }
      return '작성 중';
    case 'S':
      if (!isMember.value) {
        isReadOnly.value = false;
      }
      return '상신';
    case 'R':
      if (!isMember.value) {
        isReadOnly.value = false;
      }
      return '확인 중';
    case 'C':
      if (!isMember.value) {
        isReadOnly.value = false;
      }
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
  if (reviewDetail.value.status === '상신' || reviewDetail.value.status === '확인 중') {
    if (confirm("평가를 저장하시겠습니까?")) {

      // 필수 값이 입력되지 않은 경우
      for (const item of reviewItemList.value) {
        if (!item.goal || !item.actionItem || !item.metric || item.weight === undefined || item.weight === 0
            || item.weight === null || !item.detailPlan || !item.performance || !item.selfComment
            || !item.selfId || !item.selfScore || !item.superiorId || !item.superiorScore) {
          alert('저장 시 모든 필수 값을 입력해야 합니다.');
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
            `/api/performance-review/read`,
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

// 팀장 확인(complete)
async function complete() {
  if (reviewDetail.value.status === '상신' || reviewDetail.value.status === '확인 중') {
    if (confirm("평가를 확인 완료하시겠습니까?")) {

      // 필수 값이 입력되지 않은 경우
      for (const item of reviewItemList.value) {
        if (!item.goal || !item.actionItem || !item.metric || item.weight === undefined || item.weight === 0
            || item.weight === null || !item.detailPlan || !item.performance || !item.selfComment
            || !item.selfId || !item.selfScore || !item.superiorId || !item.superiorScore) {
          alert('확인 완료 시 모든 필수 값을 입력해야 합니다.');
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
            `/api/performance-review/complete`,
            sendData
        );

        alert('평가를 확인 완료했습니다.')
        window.location.reload();
      } catch (error) {
        console.error('Error sending data:', error);
        alert('평가를 확인 완료할 수 없습니다.')
      }
    }
  } else {
    alert('평가를 확인 완료할 수 없습니다.')
  }
}

// 팀장 확정(complete)
async function valid() {
  if (reviewDetail.value.status === '상신' || reviewDetail.value.status === '확인 중' || reviewDetail.value.status === '확인') {
    if (confirm("평가를 확정하시겠습니까?")) {

      // 필수 값이 입력되지 않은 경우
      for (const item of reviewItemList.value) {
        if (!item.goal || !item.actionItem || !item.metric || item.weight === undefined || item.weight === 0
            || item.weight === null || !item.detailPlan || !item.performance || !item.selfComment
            || !item.selfId || !item.selfScore || !item.superiorId || !item.superiorScore) {
          alert('확정 시 모든 필수 값을 입력해야 합니다.');
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
            `/api/performance-review/valid`,
            sendData
        );

        alert('평가를 확정했습니다.')
        window.location.reload();
      } catch (error) {
        console.error('Error sending data:', error);
        alert('평가를 확정할 수 없습니다.')
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