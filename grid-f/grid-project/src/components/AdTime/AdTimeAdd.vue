<template>
  <div class="adTimeAddController">
    <div class="adTimeBox">
      <div class="todayTitle">{{ today }}</div>
      <b-card class="card arrival">
        <div v-if="adTime.startTime">{{ adTime.startTime }}</div>
        <div v-else>00:00</div>
        <button @click="addArrivalTime()">출근</button>
      </b-card>
      <b-card class="card departure">
        <div v-if="adTime.endTime">{{ adTime.endTime }}</div>
        <div v-else>00:00</div>
        <button @click="addDepartureTime()">퇴근</button>
      </b-card>

    </div>
    <div class="weekCalender" id="calendar"></div>
  </div>
  <div class="weekCalender" id="app">
    <div id="calendar"></div>
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
        right: 'monthCalendar'
      },
      customButtons: {
        monthCalendar: {
          text: 'Month',
          click: function () {
            router.push('/work-calendar');
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
    fetchAllAdTime();

    await fetchAllEvent();
    initCalendar(events);
  } else if (userRole.value === 'ROLE_USER') {
    console.log('조회');
    fetchAdTime();
    await fetchEmployeeEvent();
    initCalendar(events);
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


const drawChart = () => {
  const data = new google.visualization.DataTable();
  data.addColumn('string', 'Topping');
  data.addColumn('number', 'Slices');
  data.addRows([
    ['Mushrooms', 3],
    ['Onions', 1],
    ['Olives', 1],
    ['Zucchini', 1],
    ['Pepperoni', 2]
  ]);

  const options = {
    title: 'How Much Pizza I Ate Last Night',
    width: 400,
    height: 300
  };

  const chart = new google.visualization.PieChart(document.querySelector('[ref="chart"]'));
  chart.draw(data, options);
}


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

.adTimeAddController {
  display: grid;
  grid-template-rows: 25% 4% auto;
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
  display: grid;
  grid-template-rows: 10% 90%;
  grid-template-columns: 25% 25% 50%;
}

.todayTitle {
  grid-row-start: 1;
}

.arrival {
  grid-row-start: 2;
  grid-column-start: 1;
  grid-column-end: 2;
}

.departure {
  grid-row-start: 2;
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