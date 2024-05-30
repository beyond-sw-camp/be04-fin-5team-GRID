<template>
  <div class="adTimeAddController">
<!--    <div class="adTimeListTitle">-->
<!--      <img class="adTimeIcon" src="@/assets/icons/goal_icon.png">-->
<!--      <h1>Main</h1>-->
<!--    </div>-->
    <div class="adTimeBox">
    {{ today }}
    <div>
      <div v-if="adTime.startTime">{{ adTime.startTime }}</div>
      <div v-else>00:00</div>
      <button @click="addArrivalTime()">출근</button>
    </div>
    <div>
      <div v-if="adTime.endTime">{{ adTime.endTime }}</div>
      <div v-else>00:00</div>
      <button @click="addDepartureTime()">퇴근</button>
    </div>
  </div>
  <div class="weekCalender" id="app">
    <div id="calendar"></div>
  </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';
import {Calendar} from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';



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
      plugins: [dayGridPlugin],
      initialView: 'dayGridWeek',
      headerToolbar: {
        left:'',
        center: 'title',
        right: 'prev,next today'
      },
      events: events.value
    });
    calendar.render();
  } else {
    console.error("Calendar element not found");
  }
}

const updateCalendarEvents = (events) => {
  if (calendar) {
    calendar.removeAllEvents();  // 기존 이벤트 제거
    calendar.addEventSource(events);  // 새로운 이벤트 추가
  }
}

// 관리자용
const fetchEmployeeEvent = async ()  => {
  try {
    // 출근 조회
    const responseAdTime = await axios.get(`http://localhost:8080/ad-time/${userId.value}`);

    const adTime = responseAdTime.data.adTimeDTOList;
    console.log(adTime);
    events.value = transformEvents(adTime);
    console.log(adTime.attendanceStatus);
    console.log(events.value);
    // updateCalendarEvents(events.value);
    // 휴가 조회


    // 시간외 근무 조회
    // 단축 근무 조회
    // 출장 조회

  } catch (error) {
    console.error('에러 발생:', error);
  }
};

// 직원용
const fetchAllEvent = async ()  => {
  try {
    // 출근 조회
    const responseAdTime = await axios.get(`http://localhost:8080/ad-time/2`);

    const adTime = responseAdTime.data.adTimeDTOList;
    console.log(adTime);
    events.value = transformEvents(adTime);
    console.log(adTime.attendanceStatus);
    console.log(events.value);
    // updateCalendarEvents(events.value);
    // 휴가 조회


    // 시간외 근무 조회
    // 단축 근무 조회
    // 출장 조회

  } catch (error) {
    console.error('에러 발생:', error);
  }
};




function transformEvents(list) {
  return list.map(item => ({
    title: `${item.attendanceStatus}`,
    start: item.startTime? item.startTime.replace(" ", "T"): null,
    end: item.endTime? item.endTime.replace(" ", "T"): null
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
    fetchAllAdTime();
    await fetchAllEvent();
    initCalendar(events);
  } else if (userRole.value === 'ROLE_USER') {
    console.log('조회');
    fetchAdTime();
    await fetchEmployeeEvent();
    initCalendar(events);
  }

});


// 출근 시간 추가 요청
const addArrivalTime = async () => {
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
        alert('출근 시간이 존재합니다')
        console.error('에러 발생:', error);
      });
}

// 퇴근 시간 추가 요청
const addDepartureTime = async () => {
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
        alert('퇴근 시간이 존재합니다');
        console.error('에러 발생:', error);
      });

}


</script>

<style scoped>
.adTimeAddController {
  display: grid;
  grid-template-rows: 18% 4% auto 5% 13%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.adTimeListTitle {
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

.adTimeListTitle h1 {
  margin-left: 0.5%;
}

.adTimeIcon {
  width: 80%;
}

.adTimeBox {
  grid-column-start: 2;
  grid-column-end: 3;
}

.weekCalender {
  grid-row-start: 3;
  grid-column-start: 2;
  grid-column-end: 3;
}

#calendar {
  width: 100%;
  max-height: 25%
}

</style>