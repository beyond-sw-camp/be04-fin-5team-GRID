<template>
  <div class="goalListContainer">
    <div class="goalListTitle">
      <h1 class="mb-1"><i class="bi-award fs-3"></i>&nbsp; 업적 평가 목표 조회 &nbsp;&nbsp;<i class="bi bi-info-circle fs-5 mt-5" id="popover-target-1" style="color: darkgoldenrod; cursor: pointer;" @click="showModal('guideGoalList')"></i></h1>
    </div>
    <div class="tableContainer">
      <table class="table table-hover">
        <thead>
        <tr>
          <th>No</th>
          <th>연도</th>
          <th>평가명</th>
          <th>승인상태</th>
          <th>작성자 사번</th>
          <th>작성자</th>
          <th>결재자 사번</th>
          <th>결재자</th>
          <th>세부정보</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in paginatedGoals" :key="item.id">
          <td>{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
          <td>{{ item.year }}</td>
          <td>{{ item.reviewName }}</td>
          <td>
            <b-badge variant="warning" v-if="item.approvalStatus === 'IP'">작성 중</b-badge>
            <b-badge variant="info" v-else-if="item.approvalStatus === 'S'">상신</b-badge>
            <b-badge variant="primary" v-else-if="item.approvalStatus === 'R'">확인 중</b-badge>
            <b-badge variant="success" v-else-if="item.approvalStatus === 'A'">승인</b-badge>
            <b-badge variant="danger" v-else-if="item.approvalStatus === 'D'">반려</b-badge>
            <!--            {{ getApprovalStatus(item.approvalStatus) }}-->
          </td>
          <td>{{ getEmployeeNumber(item.writer) }}</td>
          <td>{{ getEmployeeName(item.writer) }}</td>
          <td>{{ getEmployeeNumber(item.approver) }}</td>
          <td>{{ getEmployeeName(item.approver) }}</td>
          <td>
            <b-badge @click="goToDetailPage(item.id)" class="detailIcon">
              &#x2139;
            </b-badge>
<!--            <button @click="goToDetailPage(item.id)"><img class="more" src="@/assets/buttons/zoom.png"></button>-->
            </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- 가이드 모달 -->
    <div class="modal fade" id="guideGoalList" tabindex="-1" aria-labelledby="guideManageLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title" id="guideManageLabel">업적 평가 목표 가이드</h4>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div class="example-content">
              <p>업적 평가 목표를 볼 수 있는 페이지 입니다. </p>
              <hr>
              <p>
                팀원들은 1년에 한 번 자신의 업무 목표를 설정할 수 있습니다.<br>
                업적 평가 목표는 팀원인 직원들만 3월에 작성 가능합니다.<br>
                (현재는 모든 기간 작성할 수 있도록 열어두었습니다.)
              </p>
              <p>팀장</p>
              <p>&nbsp;자신이 결재 요청 받은 업적 평가 목표를 조회할 수 있습니다.</p>
              <p>팀원</p>
              <p>&nbsp;자신이 작성한 업적 평가 목표를 조회할 수 있습니다</p>
              <p>공통</p>
              <p>1. 승인 상태</p>
              <p>&nbsp1-1. 작성 중: 팀원이 목표를 작성하고 있는 상태</p>
              <p>&nbsp;1-2. 상신: 팀원이 목표를 작성하고 결재를 올린 상태</p>
              <p>&nbsp;1-3. 확인 중: 팀장이 목표를 보고 승인여부를 판단 중인 상태</p>
              <p>&nbsp;1-4. 승인: 팀장이 팀원의 목표를 승인한 상태 </p>
              <p>&nbsp;1-5. 반려: 팀장이 팀원의 목표를 반려한 상태, 팀원은 목표 재설정 가능</p>
              <p>2. 세부 정보를 통해 자세한 목표 내용을 볼 수 있습니다.</p>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>

    <nav class="pg" aria-label="Page navigation example" >
      <ul class="pagination">
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <a class="page-link" href="#" aria-label="First" @click.prevent="goToFirstPage">
            <span aria-hidden="true">&laquo;&laquo;</span>
          </a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <a class="page-link" href="#" aria-label="Previous" @click.prevent="prevPage">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li v-for="page in filteredPages" :key="page" class="page-item" :class="{ active: page === currentPage }">
          <a class="page-link" @click.prevent="goToPage(page)">{{ page }}</a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <a class="page-link" aria-label="Next" @click.prevent="nextPage">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <a class="page-link" href="#" aria-label="Last" @click.prevent="goToLastPage">
            <span aria-hidden="true">&raquo;&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { useStore } from 'vuex';

const store = useStore();
const user = computed(() => store.state.user);

const router = useRouter();

const goalList = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;


const showModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.show();
};

const fetchMemberGoal = async () => {
  try {
    // 팀원일때
    const response = await axios.get(`/api/review-goal/member/${user.value.id}`);

    goalList.value = response.data.findGoalList;
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

const fetchLeaderGoal = async () => {
  try {

    // 팀장일 때
    const response = await axios.get(`/api/review-goal/leader/${user.value.id}`);

    goalList.value = response.data.findGoalList;
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

onMounted(() => {

  if(user.value.duties.dutiesName === '팀원') {

    fetchMemberGoal();
  } else {
    fetchLeaderGoal();
  }

});

const paginatedGoals = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return goalList.value.slice(start, end);
});

const totalPages = computed(() => {
    const total = Math.ceil(goalList.value.length / itemsPerPage);
    return total > 0 ? total : 1;

});

const filteredPages = computed(() => {
  const maxPages = 5; // 페이지당 최대 표시할 페이지 수
  const startPage = Math.max(1, currentPage.value - Math.floor(maxPages / 2));
  const endPage = Math.min(totalPages.value, startPage + maxPages - 1);

  const pages = [];
  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }
  return pages;
});

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};

const goToPage = (page) => {
  currentPage.value = page;
};

// 처음 페이지로 이동
const goToFirstPage = () => {
  currentPage.value = 1;
};

// 마지막 페이지로 이동
const goToLastPage = () => {
  currentPage.value = totalPages.value;
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

const getEmployeeName = (employee) => {
  return employee ? employee.employeeName : '';
};

const getEmployeeNumber = (employee) => {
  return employee ? employee.employeeNumber : '';
};

const goToDetailPage = (id) => {

  router.push(`/performance-review/goal/detail/${id}`);
};
</script>

<style scoped>
.goalListContainer {
  display: grid;
  grid-template-rows: 18% 4% auto 5% 13%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.goalListTitle {
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

.goalListTitle h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.GoalIcon {
  width: 80%;
}

.detailIcon {
  cursor: pointer;
}

.guide {
  width: 60%;
  height: 25px;
  grid-column: 2;
  margin: 0;
  cursor: pointer;
}

.more {
  width: 12px;
}

.search {
  grid-row-start: 2;
  grid-column-start: 2;
  display: grid;
  grid-template-columns: 80% 15% 1% 4%;
}

.sortBox {
  grid-column-start: 2;
  margin-left: 2%;
  padding: 5px 5px;
  border-radius: 4px;
  font-size: 12px;
}

.printBtn {
  grid-column-start: 4;
  margin-left: 2%;
  width: 100%;
  background-color: #088A85;
  color: white;
  padding: 5px 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.tableContainer {
  grid-row-start: 3;
  grid-column-start: 2;
  grid-column-end: 3;
  margin-top: 20px;
  font-size: 12px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,td {
  text-align: left;
  padding: 6px;
  vertical-align: middle;
}
/*
th {
    background-color: #f2f2f2;
}*/

/* .pagination {
    grid-row-start: 6;
    grid-column-start: 2;
    grid-column-end: 3;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 10px;
}

.pagination button {
    background-color: white;
    color: black;
    padding: 5px 10px;
    border: 1px solid #dddddd;
    border-radius: 4px;
    cursor: pointer;
    margin: 0 5px;
}

.pagination button.active {
    background-color: darkorange;
    font-weight: bold;
    color: white;
}

.pagination button:disabled {
    background-color: #dddddd;
    cursor: not-allowed;
}

.pagination span {
    display: flex;
    align-items: center;
} */

.pg {
  grid-row-start: 4;
  grid-column-start: 2;
  grid-column-end: 3;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
}

.pagination .page-item.active .page-link {
  background-color: #088A85; /* 원하는 배경색 */
  border-color: #088A85; /* 원하는 테두리 색 */
  color: white; /* 원하는 텍스트 색 */
}

.pagination .page-item .page-link {
  color: #088A85; /* 기본 텍스트 색 */
}

.pagination .page-item.disabled .page-link {
  color: #088A85; /* 비활성화된 페이지 색 */
}

</style>