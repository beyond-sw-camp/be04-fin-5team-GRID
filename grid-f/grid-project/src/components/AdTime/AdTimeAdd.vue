<template>
  <div class="adTimeAddController">
    <div class="adList">
    <button @click="goToAdTimeList()" class="btn btn-primary adBtn">출퇴근</button>
    </div>
    <div class="adTimeBox">
      <div class="card-deck adCard">
        <div class="card adCardI" style="width: 18rem;">
          <div class="card-body">
            <h5 class="card-title" v-if="adTime.startTime">{{ adTime.startTime }}</h5>
            <h5 class="card-title" v-else>00:00</h5>
            <button @click="addArrivalTime()" class="btn btn-primary adBtn">출근</button>
          </div>
        </div>
        <div class="card adCardI" style="width: 18rem;">
          <div class="card-body">
            <h5 class="card-title" v-if="adTime.endTime">{{ adTime.endTime }}</h5>
            <h5 class="card-title" v-else>00:00</h5>
            <button @click="addDepartureTime()" class="btn btn-primary adBtn">퇴근</button>
          </div>
        </div>
      </div>
    </div>
<!--    <div class="weekCalender" id="app">-->
<!--      <div id="calendar"></div>-->
<!--    </div>-->
  </div>


</template>

<script setup>
import {ref, onMounted, nextTick} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';
import {Calendar} from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

const router = useRouter();

const adTime = ref({
  id: '',
  startTime: '',
  endTime: '',
  employeeId: '',
});

const userRole = ref('');
const userId = ref('');

// 유저 확인
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

const today = ref();

// 오늘 출퇴근 시간 조회
const fetchAdTime = async () => {
  try {
    const currentTime = getCurrentDateTimeString();
    const currentDate = currentTime.slice(0, 10);

    today.value = currentDate;

    console.log(currentDate);
    const response = await axios.get(`http://localhost:8080/ad-time/${currentDate}/${userId.value}`);
    console.log('출근 조회: ', response.data);
    const data = response.data.adTimeDTO;
    console.log(response.data.adTimeDTO);
    adTime.value = {
      id: data.id,
      startTime: data.startTime ? data.startTime.slice(11, 16) : "00:00",
      endTime: data.endTime ? data.endTime.slice(11, 16) : "00:00",
      employeeId: data.employeeId,
    };

    console.log(adTime.value);
  } catch (error) {
    console.error('에러 발생:', error);
  }
}

// 일주일 근태 조회
const events = ref([]);

let calendar;
const initCalendar = async (events) => {
  let calendarEl = document.getElementById('calendar');
  if (calendarEl) {
    calendar = new Calendar(calendarEl, {
      plugins: [dayGridPlugin, interactionPlugin],
      initialView: 'dayGridWeek',
      headerToolbar: {
        left: 'prev,next today',
        center: '',
        right: 'monthCalendar adTime'
      },
      customButtons: {
        monthCalendar: {
          text: 'Month',
          click: function () {
            router.push('/work-calendar');
          }
        },
        adTime: {
          text: '출퇴근',
          click: function () {
            router.push('/ad-time');
          }
        }
      },
      events: events.value,
      eventClick: function (info) {
        console.log('Event clicked:', info.event.title);
        // 이벤트 클릭 시 실행할 동작을 여기에 추가
        // 예: 모달 열기, 이벤트 상세 정보 표시 등
      }
    });
    calendar.render();
  } else {
    console.error("Calendar element not found");
  }
}


// 직원용
const fetchEmployeeEvent = async () => {

  try {
    // 출근 조회
    const responseAdTime = await axios.get(`http://localhost:8080/ad-time/${userId.value}`);

    const adTime = responseAdTime.data.adTimeDTOList;
    console.log(adTime);

    const adEvents = transformAdEvents(adTime);
    console.log(adTime.attendanceStatus);
    console.log(events.value);
    // updateCalendarEvents(events.value);
    // 출장 조회
    const responseBt = await axios.get(`http://localhost:8080/approval/list/1/1/${userId.value}`);
    console.log(responseBt.data);

    const bt = responseBt.data.approvalEmpResultList
    const btEvents = transformEvents(bt, '출장', 'red');

    // 시간외 근무 조회
    const responseO = await axios.get(`http://localhost:8080/approval/list/2/1/${userId.value}`);
    console.log(responseO.data);

    const O = responseBt.data.approvalEmpResultList
    const OEvents = transformEvents(O, '시간외 근무', 'blue');

    // 단축 근무 조회
    const responseRw = await axios.get(`http://localhost:8080/approval/list/3/1/${userId.value}`);
    console.log(responseRw.data);

    const Rw = responseBt.data.approvalEmpResultList
    const RwEvents = transformEvents(Rw, '단축 근무', 'green');

    // 휴가 조회
    const responseV = await axios.get(`http://localhost:8080/approval/list/4/1/${userId.value}`);
    console.log(responseV.data);

    const V = responseBt.data.approvalEmpResultList
    const VEvents = transformEvents(V, '휴가', 'purple');


    events.value = [...adEvents, ...btEvents, ...OEvents, ...RwEvents, ...VEvents];
    console.log(events.value);

  } catch (error) {
    console.error('에러 발생:', error);
  }
};

// 관리자용
const fetchAllEvent = async () => {
  try {
    // 출근 조회
    const responseAdTime = await axios.get(`http://localhost:8080/ad-time/all`);

    const adTime = responseAdTime.data.adTimeDTOList;
    console.log(adTime);
    events.value = transformAdEvents(adTime);
    console.log(adTime.attendanceStatus);
    console.log(events.value);

  } catch (error) {
    console.error('에러 발생:', error);
  }
};


function transformAdEvents(list) {
  return list.map(item => ({
    title: `${item.attendanceStatus}`,
    start: item.startTime ? item.startTime.replace(" ", "T") : null,
    end: item.endTime ? item.endTime.replace(" ", "T") : null
  }));
}

function transformEvents(list, type, color) {
  return list.map(item => ({
    title: type,
    start: item.startTime ? item.startTime.replace(" ", "T") : null,
    end: item.endTime ? item.endTime.replace(" ", "T") : null,
    color: color

  }));
}


onMounted(async () => {

  // 유저 확인
  const token = localStorage.getItem('access');
  if (token) {
    const decodedToken = parseJwt(token);
    userRole.value = decodedToken?.auth || '';
    userId.value = decodedToken?.id || '';
  }

  if (userRole.value === 'ROLE_ADMIN') {
    // fetchAllAdTime();
    // await fetchAllEvent();
    // initCalendar(events);
  } else if (userRole.value === 'ROLE_USER') {
    console.log('조회');
    fetchAdTime();
    // await fetchEmployeeEvent();
    // initCalendar(events);
  }


  // Load Google Charts and draw the chart
  // google.charts.load('current', { packages: ['corechart'] });
  // google.charts.setOnLoadCallback(() => {
  //   nextTick(drawChart);
  // });

  // google.charts.load('current', { packages: ['corechart'] });
  // google.charts.setOnLoadCallback(() => {
  //   nextTick(() => {
  //     const chartElement = document.querySelector('div[ref="chart"]');
  //     if (chartElement) {
  //       drawChart();
  //     } else {
  //       console.error("Chart element not found");
  //     }
  //   });
  // });

});


// 출근 시간 추가 요청
const addArrivalTime = async () => {
  if (userRole.value === 'ROLE_USER') {
    const currentTime = getCurrentDateTimeString();
    console.log(currentTime);
    await axios.post(
        'http://localhost:8080/ad-time/arrival-time',
        {
          id: null,
          startTime: currentTime,
          endTime: null,
          employeeId: userId.value   // 로그인한 사람
        })
        .then(response => {
          window.location.reload();
          console.log('출근 시간이 기록되었습니다.');
        })
        .catch(error => {
          alert('출근 시간을 체크할 수 없습니다.')
          console.error('에러 발생:', error);
        });
  } else {
    alert('직원만 가능합니다.')
  }
}

// 퇴근 시간 추가 요청
const addDepartureTime = async () => {
  if (userRole.value === 'ROLE_USER') {
    const currentTime = getCurrentDateTimeString();
    console.log(currentTime);
    await axios.put(
        'http://localhost:8080/ad-time/departure-time',
        {
          id: null,
          startTime: null,
          endTime: currentTime,
          employeeId: userId.value   // 로그인한 사람
        })
        .then(response => {

          window.location.reload();
          console.log('퇴근 시간이 기록되었습니다.');
        })
        .catch(error => {
          alert('퇴근 시간을 체크할 수 없습니다.');
          console.error('에러 발생:', error);
        });
  } else {
    alert('직원만 가능합니다.')
  }
}

const goToAdTimeList = () => {
  router.push(`/ad-time`);
};


</script>

<style>
.adTimeAddController {
  display: grid;
  grid-template-rows: 30% 70%;
  height: 100%;
}

.adList {
  grid-row-start: 1;
}

.adTimeBox {
  grid-row-start: 2;
  width: 50%;
  align-items: center;
}

.adCardI {
  align-items: center;
}

.adBtn {
  width: 60px;
  background-color: #088A85;
  color: white;
  padding: 5px 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.weekCalender {
  grid-row-start: 2;
  width: 100%;
  height: 100%;
}

#calendar {
  width: 100%;
  height: 100%;
  overflow: auto;
}

#calendar .fc-day-today {
  background-color: #dfe9ff;
}

#calendar .fc-daygrid-day {
  height: 100%; /* 날짜 셀의 고정 높이 */

}

/*#calendar .fc {
  width: 100%;
  height: 100%;
}*/

#calendar .fc-daygrid-day-events {
  max-height: 100px; /* 날짜 셀의 고정 높이 */
  text-align: left;
  //overflow: auto;
  text-decoration: none;
}

#calendar .fc-col-header-cell {
  background-color: #8ec0c0;
}

#calendar .fc-col-header-cell-cushion {
  /* 밑줄 제거 */
  text-decoration: none;

  /* 기본 커서로 변경 */
  cursor: default;

  //background-color: #3fb9b8;
  color: #ffffff;
  height: 10%;
}

#calendar .fc-col-cell {

}

/* 날짜 칸 크기 고정 */
#calendar .fc-daygrid-day-frame {
  display: flex;
  flex-direction: column;
  height: 100px; /* 날짜 셀의 고정 높이 설정 */
  overflow: hidden;
}

#calendar .fc-daygrid-day-top {
  flex: 0 0 auto;
}

#calendar .fc-daygrid-day-events {
  flex: 1 1 auto;
  max-height: 60px; /* 이벤트 영역의 최대 높이 설정 */
  overflow: hidden; /* 넘치는 내용 숨기기 */
}

/* 날짜 셀의 하이퍼링크를 일반 텍스트로 표시 */
#calendar .fc-daygrid-day-number {
  pointer-events: none; /* 클릭 이벤트 제거 */
  text-decoration: none; /* 밑줄 제거 */
  color: inherit; /* 기본 텍스트 색상 사용 */
  cursor: default; /* 기본 커서로 변경 */
}

/*
.fc .fc-event {
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;

}*/

.fc .fc-toolbar-title {
  color: rgb(6, 6, 31); /* 변경하고자 하는 색상으로 설정 */
  font-size: 20px; /* 변경하고자 하는 크기로 설정 */
  font-weight: bold; /* 변경하고자 하는 폰트의 높이로 설정 */
}

.fc .fc-button {
  border: none; /* 버튼 테두리 제거 */
  box-shadow: none; /* 버튼 그림자 제거 */
  height: 35px;
  font-size: 15px;
}

:root {
  --fc-border-color: rgb(187, 187, 187);
  --fc-daygrid-event-dot-width: 5px;
  --fc-today-bg-color: #fefee9;
  --fc-button-bg-color: rgb(142, 192, 192);
  --fc-button-active-bg-color: rgb(99, 178, 177);
  --fc-daygrid-day-height: 100px;
}

</style>