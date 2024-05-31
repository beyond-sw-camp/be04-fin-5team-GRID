<template>
  <div id="app">
    <div id="calendar-container">
      <div id="calendar"></div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from "vue";
import axios from 'axios';
import {Calendar} from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import {useRouter} from 'vue-router';

const router = useRouter();

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

      plugins: [dayGridPlugin, interactionPlugin],
      initialView: 'dayGridMonth',
      headerToolbar: {
        left:'prev,next today',
        center: 'title',
        right: 'mainCalendar'
      },
      customButtons: {
        mainCalendar: {
          text: 'Main',
          click: function () {
            router.push('/ad-time/add'); // main으로 수정
          }
        }
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

    const adEvents = transformAdEvents(adTime);
    console.log(adTime.attendanceStatus);
    console.log(events.value);
    // 출장 조회
    const responseBt= await axios.get(`http://localhost:8080/approval/list/1/1/${userId.value}`);
    console.log(responseBt.data);

    const bt = responseBt.data.approvalEmpResultList
    const btEvents = transformEvents(bt, '출장', 'red');


    // 시간외 근무 조회
    const responseO= await axios.get(`http://localhost:8080/approval/list/2/1/${userId.value}`);
    console.log(responseO.data);

    const O = responseBt.data.approvalEmpResultList
    const OEvents = transformEvents(O, '시간외 근무', 'blue');

    // 단축 근무 조회
    const responseRw= await axios.get(`http://localhost:8080/approval/list/3/1/${userId.value}`);
    console.log(responseRw.data);

    const Rw = responseBt.data.approvalEmpResultList
    const RwEvents = transformEvents(Rw, '단축 근무', 'green');

    // 휴가 조회
    const responseV = await axios.get(`http://localhost:8080/approval/list/4/1/${userId.value}`);
    console.log(responseV.data);

    const V = responseBt.data.approvalEmpResultList
    const VEvents = transformEvents(V, '휴가', 'purple');


    events.value = [...adEvents, ...btEvents, ...OEvents, ...RwEvents, ...VEvents];

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

    const adEvents = transformAdEvents(adTime);
    console.log(adTime.attendanceStatus);
    console.log(events.value);
    // updateCalendarEvents(events.value);
    // 출장 조회
    const responseBt= await axios.get(`http://localhost:8080/approval/list/1/${userId.value}/1`);
    console.log(responseBt.data);

    const bt = responseBt.data.approvalEmpResultList
    const btEvents = transformEvents(bt, '출장', 'red');


    // 시간외 근무 조회
    const responseO= await axios.get(`http://localhost:8080/approval/list/2/${userId.value}/1`);
    console.log(responseO.data);

    const O = responseBt.data.approvalEmpResultList
    const OEvents = transformEvents(O, '시간외 근무', 'blue');

    // 단축 근무 조회
    const responseRw= await axios.get(`http://localhost:8080/approval/list/3/${userId.value}/1`);
    console.log(responseRw.data);

    const Rw = responseBt.data.approvalEmpResultList
    const RwEvents = transformEvents(Rw, '단축 근무', 'green');

    // 휴가 조회
    const responseV = await axios.get(`http://localhost:8080/approval/list/4/${userId.value}/1`);
    console.log(responseV.data);

    const V = responseBt.data.approvalEmpResultList
    const VEvents = transformEvents(V, '휴가', 'purple');


    events.value = [...adEvents, ...btEvents, ...OEvents, ...RwEvents, ...VEvents];
    console.log(events.value);

  } catch (error) {
    console.error('에러 발생:', error);
  }
};


function transformAdEvents(list) {
  return list.map(item => ({
    title: `${item.attendanceStatus}`,
    start: item.startTime? item.startTime.replace(" ", "T"): null,
    end: item.endTime? item.endTime.replace(" ", "T"): null
  }));
}


function transformEvents(list, type, color) {
  return list.map(item => ({
    title: type,
    start: item.startTime? item.startTime.replace(" ", "T"): null,
    end: item.endTime? item.endTime.replace(" ", "T"): null,
    color: color
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
#app {
  display: grid;
  place-items: center;
  width: 100vw;
  height: 100vh;
}

#calendar-container {
  width: 70%;
  height: 65%;
  display: grid;
}

#calendar {
  width: 100%;
  height: 100%;
  overflow: hidden; /* 스크롤을 없애기 위해 추가 */
}
</style>