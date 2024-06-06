<template>
  <div class="PerformanceReviewListContainer">
    <div class="PerformanceReviewListTitle">
      <img class="PerformanceIcon" src="@/assets/icons/goal_icon.png">
      <h1>업적 평가 조회</h1>
    </div>
    <div class="PerformanceTableContainer">
      <table class="table">
        <thead>
        <tr>
          <th>No</th>
          <th>연도</th>
          <th>종류</th>
          <th>평가명</th>
          <th>승인상태</th>
          <th>작성자</th>
          <th>결재자</th>
          <th>세부정보</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in paginatedReviews" :key="item.id">
          <td>{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
          <td>{{ item.year }}</td>
          <td>{{ getType(item.type) }}</td>
          <td>{{ item.reviewName }}</td>
          <td>
            <b-badge variant="warning" v-if="item.approvalStatus === 'IP'">작성 중</b-badge>
            <b-badge variant="info" v-else-if="item.approvalStatus === 'S'">상신</b-badge>
            <b-badge variant="primary" v-else-if="item.approvalStatus === 'R'">확인 중</b-badge>
            <b-badge variant="primary" v-else-if="item.approvalStatus === 'C'">확인 중</b-badge>
            <b-badge variant="success" v-else-if="item.approvalStatus === 'V'">확정</b-badge>
          </td>
          <td>{{ getEmployeeName(item.writer) }}</td>
          <td>{{ getEmployeeName(item.approver) }}</td>
          <td><button @click="goToDetailPage(item.id)"><img class="more" src="@/assets/buttons/zoom.png"></button></td>
        </tr>
        </tbody>
      </table>
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
import {ref, onMounted, computed} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';
import {useStore} from 'vuex';

const store = useStore();
const user = computed(() => store.state.user);

const router = useRouter();

const reviewList = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;

const fetchMemberReview = async () => {
  try {
    // 팀원일때
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/performance-review/member/${user.value.id}`);

    console.log(response.data.findReviewList);
    reviewList.value = response.data.findReviewList;
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

const fetchLeaderReview = async () => {
  try {
    // 팀장일 때
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/performance-review/leader/${user.value.id}`);
    console.log(response.data.findReviewList);
    reviewList.value = response.data.findReviewList;
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

onMounted(() => {
  if (user.value.duties.dutiesName === '팀원') {
    fetchMemberReview();
  } else {
    fetchLeaderReview();
  }

});

const paginatedReviews = computed(() => {
  if (!reviewList.value || reviewList.value.length === 0) {
    return [];
  }

  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return reviewList.value.slice(start, end);
});

const totalPages = computed(() => {
  if (!reviewList.value || reviewList.value.length === 0) {
    return 1;
  }

  return Math.ceil(reviewList.value.length / itemsPerPage);
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

const getEmployeeName = (employee) => {
  return employee ? employee.employeeName : '';
};

const goToDetailPage = (id) => {
  console.log(id);
  router.push(`/performance-review/detail/${id}`);
};
</script>

<style scoped>
.PerformanceReviewListContainer {
  display: grid;
  grid-template-rows: 18% 4% auto 5% 13%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.PerformanceReviewListTitle {
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

.PerformanceReviewListTitle h1 {
  margin-left: 0.5%;
  font-weight: 600;
  font-size: 25px;
}

.PerformanceIcon {
  width: 80%;
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

.PerformanceTableContainer {
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

th, td {
  text-align: left;
  padding: 6px;
  vertical-align: middle;
}

/*th {
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