<template>
  <div class="goalDetailContainer">
    <div class="goalTitle">
      <img class="goalIcon" src="@/assets/icons/goal_icon.png">
      <h1>업적 평가 목표 조회</h1>
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
      <div v-if="isMember">
        <button @click="memberSave()">팀원 저장</button>
        <button @click="submit()">상신</button>
      </div>
      <div v-if="!isMember">
        <button @click="leaderSave()">팀장 저장</button>
        <button @click="approval()">승인</button>
        <button @click="denied()">반려</button>
      </div>
    </div>
    <div class="tableContainer">
      <div v-if="isMember">
        <table>
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
              <input
                  v-if="!isReadOnly"
                  v-model="item.jobName"
                  type="text"
              />
              <span v-else>{{ item.jobName }}</span>
            </td>
            <td>
              <input
                  v-if="!isReadOnly"
                  v-model="item.goal"
                  type="text"
              />
              <span v-else>{{ item.goal }}</span>
            </td>
            <td>
              <input
                  v-if="!isReadOnly"
                  v-model="item.metric"
                  type="text"
              />
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
              <input
                  v-if="!isReadOnly"
                  v-model="item.plan"
                  type="text"
              />
              <span v-else>{{ item.plan }}</span>
            </td>
            <td>{{ item.objection }}</td>
            <td v-if="!isReadOnly">
              <button @click="deleteItem(index)">삭제</button>
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
            <th>*업무명</th>
            <th>*목표</th>
            <th>측정지표</th>
            <th>가중치</th>
            <th>계획</th>
            <th>반려의견</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in goalItemList" :key="item.id">
            <td>{{ index + 1 }}</td>
            <td>{{ item.jobName }}</td>
            <td>{{ item.goal }}</td>
            <td>{{ item.metric }}</td>
            <td>{{ item.weight }}</td>
            <td>{{ item.plan }}</td>
            <td>
              <input
                  v-if="!isReadOnly"
                  v-model="item.objection"
                  type="text"
              />
              <span v-else>{{ item.objection }}</span>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

    </div>
    <div class="addButton" v-if="!isReadOnly">
      <button class="btn btn-dark" @click="addRow()">목표 추가</button>
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

// member, leader, manager
const isMember = ref(null);

const isReadOnly = ref(true);

const fetchGoalDetail = async () => {
  try {
    console.log(isMember.value);
    const route = router.currentRoute.value;
    const id = route.params.id;
    const response = await axios.get(`http://localhost:8080/review-goal/detail/${id}`);
    const goal = response.data.findDetailGoal;
    console.log(response.data.findDetailGoal);
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

    if (isMember.value) {
      if (goalDetail.value.status === '작성 중' || goalDetail.value.status === '반려')
        isReadOnly.value = false;
      console.log(goalDetail.value.status);
    } else {
      if (goalDetail.value.status === '상신' || goalDetail.value.status === '확인 중')
        isReadOnly.value = false;
      console.log(isReadOnly.value);
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
    case 'A':
      return '승인';
    case 'D':
      return '반려';
    default:
      return '기타';
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

      fetchGoalDetail();

    }
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
  if(item.weight < 0 || item.weight > 100) {
    alert("0부터 100사이의 숫자를 입력해주세요")
    item.weight = 0;
  }
}

// 팀원 저장(in-progress)
async function memberSave() {
  if (goalDetail.value.status === '작성 중') {
    if (confirm("목표를 저장하시겠습니까?")) {
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

// 팀장 확인중(read)
async function leaderSave() {
  if (goalDetail.value.status === '상신' || goalDetail.value.status === '확인 중') {
    if (confirm("목표를 저장하시겠습니까?")) {
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
            `http://localhost:8080/review-goal/read`,
            sendData
        );
        console.log("확인: ", response);

        // window.location.reload();
        console.log("변경완료")
      } catch (error) {
        console.error('Error sending data:', error);
      }
    }
  } else {
    alert('목표를 저장할 수 없습니다.')
  }
}

// 팀장 승인
async function approval() {
  if (goalDetail.value.status === '상신' || goalDetail.value.status === '확인 중') {
    if (confirm("목표를 승인하시겠습니까?")) {
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
        const response = await axios.put(
            `http://localhost:8080/review-goal/approval`,
            sendData
        );
        console.log("확인: ", response);

        if (response.data.href) {
          console.log("실행")
          const href = response.data.href;
          console.log(href)
          router.push(`/${href}`);
        }
        console.log("변경완료")
      } catch (error) {
        console.error('Error sending data:', error);
        window.location.reload();
      }
    }
  } else {
    alert('목표를 승인할 수 없습니다.')
  }
}


// 팀장 반려
async function denied() {
  if (goalDetail.value.status === '상신' || goalDetail.value.status === '확인 중') {
    if (confirm("목표를 반려하시겠습니까?")) {
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
            `http://localhost:8080/review-goal/denied`,
            sendData
        );
        console.log("확인: ", response);

        if (response.data.href) {
          const href = response.data.href;
          router.push(`/${href}`);
        }

      } catch (error) {
        console.error('Error sending data:', error);
        window.location.reload();
      }
    }
  } else {
    alert('목표를 반려할 수 없습니다.')
  }
}

</script>

<style scoped>
.goalDetailContainer {
  display: grid;
  grid-template-rows: 18% 21% 8% minmax(50%, auto) 8% 13%;
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
}

.goalIcon {
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


.tableContainer {
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
  background-color: #f2f2f2;
}

/* .table th:nth-child(1),
.table td:nth-child(1) {
    min-width: 15px;
    width: 5%;
} */


.addButton {
  grid-row-start: 5;
  grid-column-start: 2;
  grid-column-end: 3;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
