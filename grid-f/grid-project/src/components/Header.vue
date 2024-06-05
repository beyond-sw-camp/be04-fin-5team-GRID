<template>
  <div>
    <header class="header">
      <div class="logo">
        <img src="@/assets/logo.png" @click="main()" class="logoimage" style="cursor: pointer;">
      </div>
      
      <div class="icons">
        <div class="tokenArea">
          <span class="token-timer">{{ timeLeft }}</span>
          <button class="newToken" type="button" @click="addTokenTime()">연장하기</button>
        </div>
        <button class="icon-button" type="button" data-bs-toggle="offcanvas" data-bs-target="#demo">
          <img src="@/assets/people.png" alt="Button 2" class="icon-image" />
        </button>
        
        <div class="dropdown">
          <img
            :src="profileUrl"
            alt="profile" class="profile" @click="toggleDropdown">
          <ul class="dropdown-menu" ref="dropdownMenu">
            <li><a class="dropdown-item" href="#" @click="goProfile">개인 정보</a></li>
            <li><a class="dropdown-item" href="#" @click="logout">로그 아웃</a></li>
          </ul>
        </div>
      </div>
    </header>


    <div class="offcanvas offcanvas-end" id="demo">
      <div class="offcanvas-header">
        <h1 class="offcanvas-title">부서 리스트</h1>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
      </div>
      <div class="offcanvas-body" style="position: relative;">
        <draggable v-model="departments" @end="handleDragEnd" tag="ul" class="list-group" :itemKey="item => item.id" :disabled="userRole === 'ROLE_USER'">
          <template #item="{ element, index }">
            <li class="list-group-item" :data-id="element.id">
              <div @click="toggleTeams(element.id)" style="cursor: pointer;">
                {{ element.departmentName }}
              </div>
              <draggable v-if="element.showTeams" v-model="element.teams" :group="{ name: 'teams', pull: true, put: true }" @end="handleTeamDragEnd($event)" class="list-group" :itemKey="item => item.id" :disabled="userRole === 'ROLE_USER'">
                <template #item="{ element: team }">
                  <li class="list-group-item">
                    <div @click="toggleEmployees(team.id)" style="cursor: pointer;">
                      {{ team.teamName }}
                    </div>
                    <ul v-if="team.showEmployees">
                      <li v-for="employee in team.employees" :key="employee.id" @click="goToProfile(employee.employeeNumber)" style="cursor: pointer;">
                        {{ employee.name }}
                      </li>
                    </ul>
                  </li>
                </template>
              </draggable>
            </li>
          </template>
        </draggable>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { Dropdown } from 'bootstrap';
import draggable from 'vuedraggable';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import defaultProfileImage from '@/assets/defaultProfile.jpg';
import Cookies from 'js-cookie';


const departments = ref([]);
const router = useRouter();
const store = useStore();
const dropdownMenu = ref(null);
const timeLeft = ref('');
const isTokenChecking = ref(false);
const userId = ref('');
const userRole = ref('');

const user = computed(() => store.state.user);



const profileUrl = computed(() => {
  return user.value?.profilePath ? user.value.profilePath : defaultProfileImage;
});

const fetchDepartments = async () => {
  try {
    const response = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/department/find-all');
    departments.value = response.data.result
      .map(department => ({
        id: department.id,
        departmentName: department.departmentName,
        departmentStatus: department.departmentStatus,
        startTime: department.startTime,
        endTime: department.endTime,
        memberCnt: department.memberCnt,
        leaderId: department.leaderId,
        departmentCode: department.departmentCode,
        sequence: department.sequence,
        showTeams: false,  // 팀 리스트를 보여줄지 여부
        teams: []  // 팀 데이터
      }))
      .sort((a, b) => a.sequence - b.sequence);  // sequence 순으로 정렬
  } catch (error) {
    console.error('부서 정보를 가져오는 데 실패했습니다:', error);
  }
};

const goToProfile = async (employeeNumber) => {
  closeOffCanvas();
  setTimeout(() => {
    router.push(`/hr/profile/${employeeNumber}`).then(() => {
      window.location.reload();
    });
  }, 300); // 모달이 닫히는 애니메이션 시간을 고려하여 약간의 지연을 줌
};

const goProfile = () => {
  if (user.value && user.value.employeeNumber) {
    router.push(`/hr/profile/${user.value.employeeNumber}`);
  }
}

const fetchTeams = async (departmentId) => {
  try {
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/team/sub-department/${departmentId}`);
    return response.data.result
      .map(team => ({
        ...team,
        showEmployees: false, // 팀원 리스트를 보여줄지 여부
        employees: [] // 팀원 데이터
      }))
      .sort((a, b) => a.sequence - b.sequence);  // sequence 순으로 정렬
  } catch (error) {
    console.error('팀 정보를 가져오는 데 실패했습니다:', error);
    return [];
  }
};

const fetchEmployees = async (teamId) => {
  try {
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/team-list/${teamId}`);
    return response.data.result.sort((a, b) => a.sequence - b.sequence);  // sequence 순으로 정렬
  } catch (error) {
    console.error('직원 정보를 가져오는 데 실패했습니다:', error);
    return [];
  }
};

const toggleTeams = async (departmentId) => {
  const department = departments.value.find(d => d.id === departmentId);
  if (department) {
    if (department.showTeams) {
      department.showTeams = false;  // 이미 열려있는 경우 닫기
    } else {
      if (department.teams.length === 0) {
        department.teams = await fetchTeams(departmentId);  // 팀 데이터 로드
      }
      department.showTeams = true;  // 팀 리스트 열기
    }
  }
};

const toggleEmployees = async (teamId) => {
  for (const department of departments.value) {
    const team = department.teams.find(t => t.id === teamId);
    if (team) {
      if (team.showEmployees) {
        team.showEmployees = false; // 이미 열려있는 경우 닫기
      } else {
        if (team.employees.length === 0) {
          team.employees = await fetchEmployees(teamId); // 직원 데이터 로드
        }
        team.showEmployees = true; // 직원 리스트 열기
      }
    }
  }
};

const handleDragEnd = async () => {
  try {
    const updatedDepartments = departments.value.map((department, index) => ({
      id: department.id,
      departmentName: department.departmentName,
      departmentStatus: department.departmentStatus,
      startTime: department.startTime,
      endTime: department.endTime,
      memberCnt: department.memberCnt,
      leaderId: department.leaderId,
      departmentCode: department.departmentCode,
      sequence: index + 1
    }));
    console.log(updatedDepartments);

    await axios.put('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/department/list', updatedDepartments, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
  } catch (error) {
    console.error('부서 순서를 업데이트하는 데 실패했습니다:', error);
  }
};

const toggleDropdown = () => {
  if (dropdownMenu.value.style.display === 'block') {
    dropdownMenu.value.style.display = 'none';
  } else {
    dropdownMenu.value.style.display = 'block';
  }
};

const handleTeamDragEnd = async (event) => {
  // 새 부서의 ID를 올바르게 가져옵니다.
  const newDepartmentId = event.to.closest('.list-group-item').getAttribute('data-id');
  const teamId = event.item._underlying_vm_.id;

  if (newDepartmentId) {
    // 팀을 모든 부서에서 찾아서 업데이트합니다.
    let team;
    for (const department of departments.value) {
      const foundTeam = department.teams.find(t => t.id === teamId);
      if (foundTeam) {
        team = foundTeam;
        break;
      }
    }

    if (team) {
      team.departmentId = newDepartmentId;
      try {
        await axios.put(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/team`, {
          id: team.id,
          teamName: team.teamName,
          teamStatus: team.teamStatus,
          startTime: team.startTime,
          endTime: team.endTime,
          leaderId: team.leaderId,
          departmentId: team.departmentId,
          sequence: team.sequence
        }, {
          headers: {
            'Content-Type': 'application/json'
          }
        });
        console.log(`팀 ${teamId}이 부서 ${newDepartmentId}로 이동되었습니다.`);
      } catch (error) {
        console.error('팀 부서 이동 업데이트에 실패했습니다:', error);
      }
    } else {
      console.error('팀을 찾을 수 없습니다.');
    }
  } else {
    console.error('새 부서를 찾을 수 없습니다.');
  }
};

const addTokenTime = async () => {
  try {
      const confirmed = window.confirm('접속시간을 연장하시겠습니까?');
      if (confirmed) {
        const refreshToken = Cookies.get('refresh');
        console.log(refreshToken);


        if (!refreshToken) {
        alert('Refresh 토큰이 없습니다.');
        return;
      }

        const response = await axios.post(
        'http://grid-frontend-env-1.eba-xymvvqgw.ap-northeast-2.elasticbeanstalk.com/tokens/re-auth',
        {},
        {
          headers: {
            'Content-Type': 'application/json',
            'Cookie': `refresh=${refreshToken}`,
          },
        }
      );
        localStorage.setItem('access', response.data.access); // 새로운 access 토큰 저장
        alert('접속시간이 연장되었습니다!');
        window.location.reload();  
      } 
    } catch (error) {
        console.error("Error:", error);
        alert("토큰 갱신에 실패했습니다.");
    }
};

const getNewToken = async () => {
    try {
      const confirmed = window.confirm('접속시간을 연장하시겠습니까?');
      if (confirmed) {
        const refreshToken = Cookies.get('refresh');
        console.log(refreshToken);

        if (!refreshToken) {
        alert('Refresh 토큰이 없습니다.');
        return;
      }
      const response = await axios.post(
        'http://grid-frontend-env-1.eba-xymvvqgw.ap-northeast-2.elasticbeanstalk.com/tokens/re-auth',
        {},
        {
          headers: {
            'Content-Type': 'application/json',
            'Cookie': `refresh=${refreshToken}`,
          },
        }
      );
        localStorage.setItem('access', response.data.access); // 새로운 access 토큰 저장
        alert('접속시간이 연장되었습니다!');
        window.location.reload();  
      } else {
        alert("접속시간 연장을 취소했습니다. 로그아웃합니다.");
        await axios.post(
      'http://grid-frontend-env-1.eba-xymvvqgw.ap-northeast-2.elasticbeanstalk.com/logout',
      {},
      {
        headers: {
          'Content-Type': 'application/json',
          'Cookie': `refresh=${refreshToken}`,
        },
        withCredentials: true,
      }
    );
        localStorage.removeItem('access');
        localStorage.removeItem('email');
        store.dispatch('resetState');
        router.push('/');
      }
        
    } catch (error) {
        console.error("Error:", error);
        alert("토큰 갱신에 실패했습니다.");
    }
};



function isTokenExpired(token) {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const exp = payload.exp * 1000; // Expiration time in milliseconds
    return Date.now() > exp;
}

function calculateTimeLeft(token) {
  const payload = JSON.parse(atob(token.split('.')[1]));
  const exp = payload.exp * 1000; // Expiration time in milliseconds
  const timeLeft = exp - Date.now();
  if (timeLeft > 0) {
    const minutes = Math.floor(timeLeft / 60000);
    const seconds = Math.floor((timeLeft % 60000) / 1000);
    return `${minutes}분 ${seconds}초`;
  }
  return '토큰이 만료되었습니다.';
}

function checkToken() {
    const token = localStorage.getItem('access'); // access 토큰을 저장한 위치에 따라 변경
    if (token) {
        timeLeft.value = calculateTimeLeft(token);
        if (isTokenExpired(token) && !isTokenChecking.value) {
            isTokenChecking.value = true;
            getNewToken().finally(() => {
                isTokenChecking.value = false;
            });
        }
    } else {
        timeLeft.value = '로그인 해주세요.';
    }
}

// 주기적으로 토큰 상태를 확인
setInterval(checkToken, 100); 

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

const closeOffCanvas = () => {
  const offCanvasElement = document.getElementById('demo');
  const offCanvas = bootstrap.Offcanvas.getInstance(offCanvasElement);
  if (offCanvas) {
    offCanvas.hide();
  }
  // 강제로 backdrop 제거
  removeBackdrop();
};

const removeBackdrop = () => {
  document.querySelectorAll('.offcanvas-backdrop, .modal-backdrop').forEach(backdrop => backdrop.remove());
};

onMounted(() => {
  const token = localStorage.getItem('access');
  if (token) {
    const decodedToken = parseJwt(token);
    userRole.value = decodedToken?.auth || '';
    userId.value = decodedToken?.id || '';
  }
  fetchDepartments();
  // Bootstrap 드롭다운 초기화
  const dropdownElementList = [].slice.call(document.querySelectorAll('.dropdown-toggle'));
  dropdownElementList.map(function (dropdownToggleEl) {
    return new Dropdown(dropdownToggleEl);
  });

  // 오프캔버스가 닫힐 때 backdrop을 제거하는 이벤트 리스너 추가
  const offCanvasElement = document.getElementById('demo');
  offCanvasElement.addEventListener('hide.bs.offcanvas', removeBackdrop);
});

const logout = async () => {
  try {

    const refreshToken = Cookies.get('refresh');
    console.log(refreshToken);

    if (!refreshToken) {
      alert('Refresh 토큰이 없습니다.');
      return;
    }

    await axios.post(
      'http://grid-frontend-env-1.eba-xymvvqgw.ap-northeast-2.elasticbeanstalk.com/logout',
      {},
      {
        headers: {
          'Content-Type': 'application/json',
          'Cookie': `refresh=${refreshToken}`,
        },
        withCredentials: true,
      }
    );
    localStorage.removeItem('access');
    localStorage.removeItem('email');
    store.dispatch('resetState');
    alert('로그아웃 되었습니다');
    router.push('/');
  } catch (error) {
    console.error('로그아웃 중 오류가 발생했습니다:', error);
  }
};

function main() {
  router.push('/main');
}

onMounted(fetchDepartments);
</script>



<style scoped>
@font-face {
  font-family: 'IBMPlexSansKR-Regular';
  src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

.header {
  background: #088A85;
  color: white;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-family: 'IBMPlexSansKR-Regular';
}

.logo {
  display: flex;
  align-items: center;
}

.logo img {
  height: 40px;
  margin-right: 10px;
}

.icons {
  display: flex;
  align-items: center;
}

.profile {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
  margin-right: 20px;
  object-fit: cover;
}

.icon-button {
  background: none;
  border: none;
  margin: 0 5px;
  padding: 5px;
  cursor: pointer;
  display: flex;
  align-items: center;
  margin-right: 20px;
}

.icon-image {
  width: 25px;
  height: 25px;
}

.list-group-item {
  cursor: move;
}

.dropdown {
  position: relative;
}

.dropdown-menu {
  display: none;
  position: absolute;
  top: 100%;
  right: 0;
  background: white;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  z-index: 1;
  list-style: none;
  padding: 10px 0;
  margin: 0;
}

.dropdown-item {
  padding: 8px 16px;
  cursor: pointer;
  text-decoration: none;
  display: block;
  color: black;
}

.dropdown-item:hover {
  background-color: #f1f1f1;
}

.token-timer {
  color: black;
  font-weight: 600;
  margin-right: 10px;
  margin-left: 10px;
}

.tokenArea {
  align-items: center;
  justify-content: center;
  margin-right:20px;
  padding: 5px;
  border: 1px solid black;
}

.tokenArea button {
  background-color: #77B0AA;
  color: black;
}
</style>
