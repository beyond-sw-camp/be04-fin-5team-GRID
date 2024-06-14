<template>
  <div class="TotalPerformanceReviewListContainer">
    <div class="TotalPerformanceReviewListTitle">
      <h1 class="mb-1"><i class="bi bi-award fs-3"></i>&nbsp; 종합 업적 평가 조회 &nbsp;&nbsp;<i class="bi bi-info-circle fs-5 mt-5" id="popover-target-1" style="color: darkgoldenrod; cursor: pointer;" @click="showModal('guideReview')"></i></h1>
    </div>
    <div class="TotalPerformanceTableContainer">
      <table class="table table-hover">
        <thead>
        <tr>
          <th>No</th>
          <th>연도</th>
          <th>평가명</th>
          <th>평가 대상자 사번</th>
          <th>평가 대상자</th>
          <th>평가자 사번</th>
          <th>평가자</th>
          <th>세부정보</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in paginatedReviews" :key="item.id">
          <td>{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
          <td>{{ item.year }}</td>
          <td>{{ item.reviewName }}</td>
          <td>{{ getEmployeeNumber(item.reviewee) }}</td>
          <td>{{ getEmployeeName(item.reviewee) }}</td>
          <td>{{ getEmployeeNumber(item.reviewer) }}</td>
          <td>{{ getEmployeeName(item.reviewer) }}</td>
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
              <p>팀장</p>
              <p>&nbsp;자신이 평가한 종합 업적 평가를 조회할 수 있습니다.</p>
              <p>팀원</p>
              <p>&nbsp;자신이 평가 받은 종합 업적 평가를 조회할 수 있습니다</p>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>

    <nav class="pg" aria-label="Page navigation example">
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
import {useStore} from 'vuex';

const store = useStore();
const user = computed(() => store.state.user);

const router = useRouter();

const userRole = ref('');
const userId = ref('');

const totalList = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;

const showModal = (modalId) => {
  const modal = new bootstrap.Modal(document.getElementById(modalId));
  modal.show();
};

const fetchMemberTotal = async () => {
  try {
    // 팀원일때
    const response = await axios.get(`/api/total-performance-review/reviewee/${user.value.id}`);

    totalList.value = response.data.findTotalList;
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

const fetchLeaderTotal = async () => {
  try {
    // 팀장일 때
    const response = await axios.get(`/api/total-performance-review/reviewer/${user.value.id}`);

    totalList.value = response.data.findTotalList;
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

const fetchAllTotal = async () => {
  try {
    // 관리자
    const response = await axios.get(`/api/total-performance-review`);

    totalList.value = response.data.findTotalList;
  } catch (error) {
    console.error('에러 발생:', error);
  }
};

function parseJwt(token) {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error('Invalid token', error);
    return null;
  }
}

onMounted(() => {
  const token = localStorage.getItem('access');
  if (token) {
    const decodedToken = parseJwt(token);
    userRole.value = decodedToken?.auth || '';
    userId.value = decodedToken?.id || '';
  }

  if (userRole.value === 'ROLE_ADMIN') {
    fetchAllTotal();
  } else if (userRole.value === 'ROLE_USER') {
    if(user.value.duties.dutiesName === '팀원') {
      fetchMemberTotal();
    } else {
      fetchLeaderTotal();
    }
  }
});

const paginatedReviews = computed(() => {
  if (!totalList.value || totalList.value.length === 0) {
    return [];
  }

  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return totalList.value.slice(start, end);
});

const totalPages = computed(() => {
  if (!totalList.value || totalList.value.length === 0) {
    return 1;
  }

  return Math.ceil(totalList.value.length / itemsPerPage);
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

const getEmployeeName = (employee) => {
  return employee ? employee.employeeName : '';
};

const getEmployeeNumber = (employee) => {
  return employee ? employee.employeeNumber : '';
};

const goToDetailPage = (id) => {

  router.push(`/performance-review/total/detail/${id}`);
};
</script>

<style scoped>
.TotalPerformanceReviewListContainer {
  display: grid;
  grid-template-rows: 18% 4% auto 5% 13%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.TotalPerformanceReviewListTitle {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
  margin-left: -0.5%;
  color: #000000;
  display: grid;
  grid-template-columns: 25% 4%;
  align-items: center;
  min-width: 1200px;
}

.TotalPerformanceReviewListTitle h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
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

.TotalPerformanceTableContainer {
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