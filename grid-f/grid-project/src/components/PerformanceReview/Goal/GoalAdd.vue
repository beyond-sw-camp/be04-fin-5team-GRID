<template>
  <div class="goalDetailContainer">
    <div class="goalTitle">
      <h1 class="mb-1"><i class="bi bi-award fs-3"></i>&nbsp; 업적 평가 목표 작성 &nbsp;&nbsp;<i class="bi bi-info-circle fs-5 mt-5" id="popover-target-1" style="color: darkgoldenrod; cursor: pointer;" @click="showModal('guideGoal')"></i></h1>
    </div>
    <div class="titleTableContainer">
      <table>
        <tbody>
        <tr>
          <td><label>연도</label></td>
          <td colspan="3">{{ goalDetail.year }}</td>
        </tr>
        <tr>
          <td><label>평가명</label></td>
          <td colspan="3">{{ goalDetail.reviewName }}</td>
        </tr>
        <tr>
          <td><label>작성자</label></td>
          <td>{{ goalDetail.writerName }}</td>
          <td><label>작성시간</label></td>
          <td>{{ goalDetail.writeTime }}</td>
        </tr>
        <tr>
          <td><label>결재자</label></td>
          <td>{{ goalDetail.approverName }}</td>
          <td><label>결재시간</label></td>
          <td>{{ goalDetail.approvalTime }}</td>
        </tr>
        <tr>
          <td><label>문서 상태</label></td>
          <td colspan="3">{{ goalDetail.status }}</td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="GoalButtonContainer" v-if="!isReadOnly">
      <div class="buttonWrapper">
        <button class="goalBtn1" @click="memberSave()">저장</button>
        <button class="goalBtn1" @click="submit()">상신</button>
      </div>
    </div>
    <div class="GoalAddTableContainer">
      <table class="table table-bordered goalItemTable">
        <thead>
        <tr>
          <th>No</th>
          <th>*업무명</th>
          <th>*목표</th>
          <th>측정지표</th>
          <th>가중치</th>
          <th>계획</th>
          <th>반려의견</th>
          <th v-if="!isReadOnly">삭제</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in goalItemList" :key="item.id">
          <td>{{ index + 1 }}</td>
          <td>
            <textarea
                v-if="!isReadOnly"
                v-model="item.jobName"
            ></textarea>
            <span v-else>{{ item.jobName }}</span>
          </td>
          <td>
            <textarea
                v-if="!isReadOnly"
                v-model="item.goal"
            ></textarea>
            <span v-else>{{ item.goal }}</span>
          </td>
          <td>
            <textarea
                v-if="!isReadOnly"
                v-model="item.metric"
            ></textarea>
            <span v-else>{{ item.metric }}</span>
          </td>
          <td>
            <input
                v-if="!isReadOnly"
                v-model="item.weight"
                type="number"
                min="0"
                max="100"
                @input="validateWeightInput(item)"
            />
            <span v-else>{{ item.weight }}</span>
          </td>
          <td>
            <textarea
                v-if="!isReadOnly"
                v-model="item.plan"
            ></textarea>
            <span v-else>{{ item.plan }}</span>
          </td>
          <td>
            <span v-if="!isReadOnly">
              {{ item.objection }}
            </span>
            <span v-else>
              {{ '' }}
            </span>
          </td>
          <td v-if="!isReadOnly">
            <button class="goalBtn1" @click="deleteItem(index)">삭제</button>
          </td>
        </tr>
        </tbody>
      </table>
      <div class="addButton">
        <div>
          <button class="goalBtn2" @click="addRow()" v-if="!isReadOnly">+</button>
        </div>
      </div>
    </div>

    <!-- 가이드 모달 -->
    <div class="modal fade" id="guideGoal" tabindex="-1" aria-labelledby="guideManageLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title" id="guideManageLabel">업적 평가 목표 가이드</h4>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div class="example-content">
              <p>업적 평가 목표를 작성하는 페이지 입니다. </p>
              <hr>
              <p>
                팀원은 1년에 한 번 자신의 업무 목표를 설정할 수 있습니다.<br>
                업적 평가 목표는 팀원인 직원들만 3월에 작성 가능합니다.<br>
                (현재는 모든 기간 작성할 수 있도록 열어두었습니다.)
              </p>
              <p>1. 승인 상태</p>
              <p>&nbsp;1-1. 작성 중: 팀원이 목표를 작성하고 있는 상태</p>
              <p>&nbsp;1-2. 상신: 팀원이 목표를 작성하고 결재를 올린 상태</p>
              <p>&nbsp;1-3. 확인 중: 팀장이 목표를 보고 승인여부를 판단 중인 상태</p>
              <p>&nbsp;1-4. 승인: 팀장이 팀원의 목표를 승인한 상태 </p>
              <p>&nbsp;1-5. 반려: 팀장이 팀원의 목표를 반려한 상태, 팀원은 목표 재설정 가능</p>
              <p>2. 저장 버튼</p>
              <p>&nbsp;2-1. 업무명과 목표는 필수로 작성해야 저장 가능합니다.</p>
              <p>3. 상신 버튼</p>
              <p>&nbsp;3-1. 모든 내용을 필수로 작성해야 상신 가능합니다.</p>
              <p>&nbsp;3-2. 모든 항목의 가중치의 합계를 100으로 맞춰야 상신 가능합니다.</p>
              <p>4. + 버튼</p>
              <p>&nbsp;4-1. 목표 항목을 추가할 수 있습니다.</p>
              <p>&nbsp;4-2. 업무명과 목표가 채워져야 새로 추가 가능합니다.</p>
              <p>5. 삭제 버튼</p>
              <p>&nbsp;5-1. 작성한 목표 항목을 삭제할 수 있습니다.</p>
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

const goalItemList = ref([]);

const goalDetail = ref({
  id: '',
  year: '',
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


const fetchGoalAdd = async () => {
  try {
    // 올해 생성된 목표가 있으면 조회 아니라면 새로 생성

    const currentYear = new Date().getFullYear();   // 올해 년도
    const currentTime = getCurrentDateTimeString()  // 현재 시간


    // 올해 생성된 목표 평가가 있는지 확인
    const responseGoal = await axios.get(`/api/review-goal/${currentYear}/${user.value.id}`);

    // 생성된 목표 없으면
    if (!responseGoal.data.findGoal) {

      // 팀장 조회
      const leader = await axios.get(`/api/users/${user.value.id}/leaders`);

      const sandData = {
        "year": currentYear,
        "reviewName": `${currentYear} 인사평가`,
        "approvalStatus": "IP",
        "writerId": user.value.id,
        "writeTime": currentTime,
        "approverId": leader.data.result.teamLeaderId
      }

      const responseAdd = await axios.post(
          `/api/review-goal`,
          sandData
      );

      const id = responseAdd.data.goal.id;
      const response = await axios.get(`/api/review-goal/detail/${id}`);
      const goal = response.data.findDetailGoal;
      goalItemList.value = goal.goalItemList;

      goalDetail.value = {
        id: id,
        year: goal.year,
        reviewName: goal.reviewName,
        writerName: goal.writer ? goal.writer.employeeName : '없음',
        writeTime: goal.writeTime || '없음',
        approverName: goal.approver ? goal.approver.employeeName : '없음',
        approvalTime: goal.approvalTime || '없음',
        status: getApprovalStatus(goal.approvalStatus)
      };

    } else {
      // 생성된 목표가 있을 때
      const id = responseGoal.data.findGoal.id;
      const response = await axios.get(`/api/review-goal/detail/${id}`);
      const goal = response.data.findDetailGoal;
      goalItemList.value = goal.goalItemList;

      goalDetail.value = {
        id: goal.id,
        year: goal.year,
        reviewName: goal.reviewName,
        writerName: goal.writer ? goal.writer.employeeName : '없음',
        writeTime: goal.writeTime || '없음',
        approverName: goal.approver ? goal.approver.employeeName : '없음',
        approvalTime: goal.approvalTime || '없음',
        status: getApprovalStatus(goal.approvalStatus)
      };
    }


  } catch (error) {
    console.error('에러 발생:', error);
  }
};

const getApprovalStatus = (status) => {
  // 문서 상태별 수정 가능 여부 추가
  // IP, D 수정 가능 S, R, V 수정x
  switch (status) {
    case 'IP':
      isReadOnly.value = false;
      return '작성 중';
    case 'S':
      return '상신';
    case 'R':
      return '확인 중';
    case 'A':
      return '승인';
    case 'D':
      isReadOnly.value = false;
      return '반려';
    default:
      return '기타';
  }
};

onMounted(() => {
  try {
    //3월에만 작성 가능, 시연을 위해 주석처리
    // const currentMonth = new Date().getMonth() + 1;
    // if (currentMonth !== 3){
    //   alert('현재 목표 작성기간이 아닙니다.')
    //   router.push('/performance-review/goal');
    // }

    if (user.value.duties.dutiesName === '팀원')
      fetchGoalAdd();
  } catch (error) {
    console.log("에러 발생: ", error);
  }
});

// 목표 추가
const addRow = () => {
  const lastItem = goalItemList.value[goalItemList.value.length - 1];

  // 빈 행이 이미 있는지 확인
  // jobName, goal이 null이 아니라면 추가 가능
  let emptyCheck = false;

  if (lastItem) {
    const jobNameAndGoalFilled = lastItem.jobName !== '' && lastItem.goal !== '';

    if (jobNameAndGoalFilled) {
      emptyCheck = true;
    }
  }

  if (emptyCheck || !lastItem) {
    goalItemList.value.push({
      id: '',
      jobName: '',
      goal: '',
      metric: '',
      weight: '',
      plan: '',
      objection: ''
    });
  }
};

// 목표 항목 삭제
async function deleteItem(index) {
  if (confirm("목표를 삭제하시겠습니까?")) {
    try {
      const id = goalItemList.value[index].id;

      if (id != '') {
        await axios.delete(`/api/goal-item/${id}`);
      }
      goalItemList.value.splice(index, 1);
      // 삭제 후 인덱스 값 업데이트
      for (let i = index; i < goalItemList.value.length; i++) {
        goalItemList.value[i].no = i + 1;
      }

    } catch (error) {
      console.error('Error sending data:', error);
    }
  }
}

// 가중치 숫자 입력
async function validateWeightInput(item) {
  if (item.weight < 0 || item.weight > 100) {
    alert("0부터 100사이의 숫자를 입력해주세요")
    item.weight = 0;
  }
}

// 팀원 저장(in-progress)
async function memberSave() {
  if (goalDetail.value.status === '작성 중' || goalDetail.value.status === '반려') {
    if (confirm("목표를 저장하시겠습니까?")) {

      //목표 항목 빈 배열x
      if(goalItemList.value === null  || goalItemList.value.length === 0){
        alert('목표 항목을 입력해주세요');
        return;
      }

      // 업무명, 목표 입력 확인
      for (const item of goalItemList.value) {
        if (!item.jobName || !item.goal) {
          alert('업무명과 목표는 필수로 작성해주세요');
          return;
        }
      }

      const sendData = {
        id: goalDetail.value.id,
        goalItemList: goalItemList.value.map(item => ({
          id: item.id || null,
          jobName: item.jobName,
          goal: item.goal,
          metric: item.metric || null,
          weight: item.weight || 0,
          plan: item.plan || null,
          objection: item.objection || null
        }))
      };

      try {
        await axios.put(
            `/api/review-goal/in-progress`,
            sendData
        );

        alert('목표를 저장했습니다.')
        window.location.reload();
      } catch (error) {
        console.error('Error sending data:', error);
      }
    }
  } else {
    alert('목표를 저장할 수 없습니다.')
  }
}

// 팀원 상신(submit)
async function submit() {
  if (goalDetail.value.status === '작성 중' || goalDetail.value.status === '반려') {
    if (confirm("목표를 상신하시겠습니까?")) {

      //목표 항목 빈 배열x
      if(goalItemList.value === null  || goalItemList.value.length === 0){
        alert('목표 항목을 입력해주세요');
        return;
      }

      // 필수 값이 입력되지 않은 경우
      for (const item of goalItemList.value) {
        if (!item.jobName || !item.goal || !item.metric || item.weight === undefined || item.weight === 0
            || item.weight === null || !item.plan) {
          alert('상신 시 모든 필수 값을 입력해야 합니다.');
          return;
        }
      }

      // 가중치 100인지 확인
      let sumWeight = 0;
      for (const item of goalItemList.value) {
        sumWeight += item.weight;
      }

      // 100보다 작을 때
      if(sumWeight < 100) {
        alert('가중치의 합계가 100보다 작습니다.');
        return;
      }

      // 100보다 작을 때
      if(sumWeight > 100) {
        alert('가중치의 합계가 100보다 큽니다.');
        return;
      }

      const sendData = {
        id: goalDetail.value.id,
        goalItemList: goalItemList.value.map(item => ({
          id: item.id || null,
          jobName: item.jobName,
          goal: item.goal,
          metric: item.metric,
          weight: item.weight,
          plan: item.plan,
          objection: null
        }))
      };


      try {
        const response = await axios.put(
            `/api/review-goal/submit`,
            sendData
        );

        alert('목표를 상신했습니다.')
        window.location.reload();
      } catch (error) {
        console.error('Error sending data:', error);
      }
    }
  } else {
    alert('목표를 상신할 수 없습니다.')
  }
}

</script>

<style scoped>
.goalDetailContainer {
  display: grid;
  grid-template-rows: 18% 23% 7% 39% 13%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.goalTitle {
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

.goalTitle h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.goalIcon {
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
  gap: 10px; /* 버튼 사이의 간격 설정 */
  width: 30%; /* wrapper의 너비를 절반으로 설정 */
}

.GoalAddTableContainer {
  grid-row-start: 4;
  grid-column-start: 2;
  grid-column-end: 3;
  /* margin-top: 20px; */
  font-size: 12px;

  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  display: table; /* 추가: 테이블이 화면에 맞게 동적으로 늘어남 */
  //table-layout: fixed; /* 추가: 테이블 너비를 고정함 */
}

/*.goalItemTable {
  overflow-x: auto;
  !* 가로 스크롤을 필요로 하는 경우 *!
  overflow-y: auto;
}*/


th,
td {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 6px;
  vertical-align: middle;
}

.goalItemTable th {
  position: sticky;
  top: 0;
  text-align: center;
}

.addButton {
  grid-row-start: 5;
  grid-column-start: 2;
  grid-column-end: 3;
  display: flex;
  justify-content: center;
  align-items: center;
}

.goalBtn1 {
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

.goalBtn2 {
  grid-column-start: 6;
  margin-left: 2%;
  width: 100%;
  background-color: #088A85;
  color:  white;
  padding: 5px 5px;
  border: 1px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 15px;
}

.GoalAddTableContainer td {
  height: 100px;
}

.GoalAddTableContainer input[type="text"],
.GoalAddTableContainer input[type="number"],
.GoalAddTableContainer select,
.GoalAddTableContainer textarea,
.GoalAddTableContainer span {
  width: 100%;
  height: 100%;
}

.goalItemTable th:nth-child(1),
.goalItemTable td:nth-child(1) {
  min-width: 30px; /* No */
  text-align: center;
}

.goalItemTable th:nth-child(2),
.goalItemTable td:nth-child(2) {
  min-width: 300px; /*업무명 */
}

.goalItemTable th:nth-child(3),
.goalItemTable td:nth-child(3) {
  min-width: 500px; /* 목표 */
}

.goalItemTable th:nth-child(4),
.goalItemTable td:nth-child(4) {
  min-width: 500px; /* 측정지표 */
}


.goalItemTable th:nth-child(5),
.goalItemTable td:nth-child(5) {
  min-width: 70px; /* 가중치 */
}

.goalItemTable th:nth-child(6),
.goalItemTable td:nth-child(6) {
  min-width: 500px; /* 계획 */
}

.goalItemTable th:nth-child(7),
.goalItemTable td:nth-child(7) {
  min-width: 300px; /* 반려의견 */
}

.goalItemTable th:nth-child(8),
.goalItemTable td:nth-child(8) {
  min-width: 100px; /* 삭제 */
  text-align: center;
}


</style>
