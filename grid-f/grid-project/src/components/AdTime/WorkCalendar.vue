<template>
  <div id="app">
    <div id="calendar"></div>
  </div>
</template>

<script setup>
import {ref, onMounted} from "vue";
import axios from 'axios';
import {Calendar} from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import listPlugin from '@fullcalendar/list';

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

const events = ref([]);
let calendar;
const initCalendar = async (events) => {
  let calendarEl = document.getElementById('calendar');
  if (calendarEl) {
    calendar = new Calendar(calendarEl, {
      plugins: [dayGridPlugin],
      initialView: 'dayGridMonth',
      // initialView: 'dayGridWeek', //일주일 단위로
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

const fetchAllEvent = async ()  => {
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

function transformEvents(list) {
  return list.map(item => ({
    title: `${item.attendanceStatus}`,
    start: item.startTime? item.startTime.replace(" ", "T"): null,
    end: item.endTime? item.endTime.replace(" ", "T"): null
  }));
}

onMounted(async () => {

  const token = localStorage.getItem('access');
  if (token) {
    const decodedToken = parseJwt(token);
    userRole.value = decodedToken?.auth || '';
    userId.value = decodedToken?.id || '';
  }

  if (userRole.value === 'ROLE_ADMIN') {
    await fetchAllEvent();
    initCalendar(events);
  } else if (userRole.value === 'ROLE_USER') {
    await fetchEmployeeEvent();
    initCalendar(events);
  }
});


</script>

<style scoped>
#calendar {
  width: 100%;
  height: 600px;
}
</style>