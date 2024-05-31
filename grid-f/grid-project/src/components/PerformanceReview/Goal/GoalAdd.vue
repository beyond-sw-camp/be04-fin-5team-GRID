<template>
  <div class="goalDetailContainer">
    <div class="goalTitle">
      <img class="goalIcon" src="@/assets/icons/goal_icon.png">
      <h1>업적 평가 목표 작성</h1>
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
    <div class="GoalButtonContainer">
      <div class="buttonWrapper">
        <button class="goalBtn" @click="memberSave()">저장</button>
        <button class="goalBtn" @click="submit()">상신</button>
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
            {{ item.objection }}
          </td>
          <td v-if="!isReadOnly">
            <button @click="deleteItem(index)">삭제</button>
          </td>
        </tr>
        </tbody>
      </table>
      <div class="addButton">
        <div>
          <button class="goalBtn" @click="addRow()" v-if="!isReadOnly">+</button>
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


const fields = [
  {key: 'index', label: 'No', tdClass: 'goalNo'},
  {key: 'jobName', label: '*업무명', tdClass: 'goalJobName'},
  {key: 'goal', label: '*목표', tdClass: 'goalGoal'},
  {key: 'metric', label: '측정지표', tdClass: 'goalMetric'},
  {key: 'weight', label: '가중치', tdClass: 'goalWeight'},
  {key: 'plan', label: '계획', tdClass: 'goalPlan'},
  {key: 'objection', label: '반려의견', tdClass: 'goalObjection'},
  {key: 'delete', label: '삭제', tdClass: 'goalDelete'} // 삭제 버튼 열
];

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

    console.log(currentTime);

    // 올해 생성된 목표 평가가 있는지 확인
    const responseGoal = await axios.get(`http://localhost:8080/review-goal/${currentYear}/${user.value.id}`);

    console.log('목표 조회', responseGoal.data);
    // 생성된 목표 없으면
    if (!responseGoal.data.findGoal) {

      // 팀장 조회
      const leader = await axios.get(`http://localhost:8080/users/${user.value.id}/leaders`);

      const sandData = {
        "year": currentYear,
        "reviewName": `${currentYear} 인사평가`,
        "approvalStatus": "IP",
        "writerId": user.value.id,
        "writeTime": currentTime,
        "approverId": leader.data.result.teamLeaderId
      }

      const responseAdd = await axios.post(
          `http://localhost:8080/review-goal`,
          sandData
      );

      const id = responseAdd.data.goal.id;
      const response = await axios.get(`http://localhost:8080/review-goal/detail/${id}`);
      console.log(response.data);
      const goal = response.data.findDetailGoal;
      goalItemList.value = goal.goalItemList;

      console.log(goalItemList.value);
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

    } else {
      // 생성된 목표가 있을 때
      const id = responseGoal.data.findGoal.id;
      const response = await axios.get(`http://localhost:8080/review-goal/detail/${id}`);
      console.log(response.data);
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
      console.log(id);

      if (id != null) {
        await axios.delete(`http://localhost:8080/goal-item/${id}`);
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
  if (goalDetail.value.status === '작성 중') {
    if (confirm("목표를 저장하시겠습니까?")) {
      console.log(goalItemList.value);
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
            `http://localhost:8080/review-goal/in-progress`,
            sendData
        );

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
  if (goalDetail.value.status === '작성 중') {
    if (confirm("목표를 상신하시겠습니까?")) {
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

      console.log(sendData);

      try {
        const response = await axios.put(
            `http://localhost:8080/review-goal/submit`,
            sendData
        );

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
  grid-template-rows: 18% 21% 8% minmax(50%, auto) 5% 13%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.goalTitle {
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

.goalTitle h1 {
  margin-left: 0.5%;
  font-weight: 600;
  font-size: 25px;
}

.goalIcon {
  width: 80%;
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
  margin-top: 20px;
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
  text-align: center;
  padding: 6px;
  vertical-align: middle;
}

th {
  position: sticky;
  top: 0;
}

.addButton {
  grid-row-start: 5;
  grid-column-start: 2;
  grid-column-end: 3;
  display: flex;
  justify-content: center;
  align-items: center;
}

.goalBtn {
  grid-column-start: 6;
  margin-left: 2%;
  width: 100%;
  background-color: #088A85;
  color: white;
  padding: 5px 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-style: bold;
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
}


</style>
