<template>
  <div>
    <div id="calendar"></div>
  </div>
</template>

<script setup>
import {ref, onMounted} from "vue";
import axios from 'axios';
import {Calendar} from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import {useRoute, useRouter} from 'vue-router';

const route = useRoute();
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
      contentHeight: 'auto',
      titleFormat: function (info) {  // tile 설정
        let year = info.date.year;
        let month = info.date.month + 1;

        return year + "년 " + month + "월";
      },
      dayCellContent: function (arg) {
        // 날짜의 일을 가져옴
        const day = arg.date.getDate().toString();

        // 날짜를 HTML로 반환하여 출력
        return `${day}`;
      },
      locale: 'ko', //한국어 설정
      headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: ''
      },

      events: events.value,
      eventOrder: 'priority',  // 우선순위 필드에 따라 정렬
      eventDisplay: 'block', // 이벤트 표시를 블록형으로 설정
      dayMaxEventRows: 4,
      moreLinkClick: 'popover', // "more" 링크 클릭 시 팝오버로 표시
      eventDidMount: function (info) {
        var eventEl = info.el;
        eventEl.style.overflow = 'hidden';
        eventEl.style.textOverflow = 'ellipsis';
        eventEl.style.whiteSpace = 'nowrap';

        var popover = new bootstrap.Popover(info.el, {
          content: info.event.title,
          trigger: 'hover',
          placement: 'auto'
        });
      },
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


const fetchAllEvent = async () => {
  try {
    // 출장 조회
    const responseBt = await axios.get(`/api/approval/all/1/1`);
    console.log(responseBt.data);

    const bt = responseBt.data.approvalEmpResultList
    const btEvents = transformEvents(bt, '출장', '#fad7d7', 2);


    // 시간외 근무 조회
    const responseO = await axios.get(`/api/approval/all/2/1`);
    console.log(responseO.data);

    const o = responseO.data.approvalEmpResultList
    const oEvents = transformEvents(o, '시간외 근무', '#c0caff', 4);

    // 휴가 조회
    const responseV = await axios.get(`/api/approval/all/4/1`);
    console.log(responseV.data);

    const v = responseV.data.approvalEmpResultList
    const vEvents = transformEvents(v, '휴가', '#ffdbf7', 3);


    events.value = [...btEvents, ...oEvents, ...vEvents];
    console.log(events.value);

  } catch (error) {
    console.error('에러 발생:', error);
  }
};

function transformEvents(list, type, color, priority) {
  return list.map(item => {
    if (type === '휴가' && ['연차', '월차', '정기휴가'].includes(item.vacationType)) {
      return {
        title: `${item.vacationType} ${item.employeeNumber} ${item.employeeName}`,
        start: item.startTime ? item.startTime.split(" ")[0] : null,
        end: item.endTime ? item.endTime.split(" ")[0] : null,
        color: color,
        textColor: "#424242",
        priority: priority
      };
    } else {
      return {
        title: type !== '휴가' ? `${type} ${item.employeeNumber} ${item.employeeName}` : `${item.vacationType} ${item.employeeNumber} ${item.employeeName}`,
        start: item.startTime ? item.startTime.replace(" ", "T") : null,
        end: item.endTime ? item.endTime.replace(" ", "T") : null,
        color: color,
        textColor: "#424242",
        priority: priority
      };
    }
  });

  return list.map(item => ({
    title: `${type} ${item.employeeNumber} ${item.employeeName}`,
    start: item.startTime? item.startTime.replace(" ", "T"): null,
    end: item.endTime? item.endTime.replace(" ", "T"): null,
    color: color,
    textColor : "#424242",
    priority:  priority
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
  }
});


</script>

<style>
/*#app {
  place-items: center;
  width: 100%;
  height: 100%;
}*/

.calenderContainer {
  width: 100%;
  height: 100%;
}


#calendar {
  width: 100%;
  height: 100%;
  overflow: auto; /* 스크롤바 추가 */
}

#calendar .fc-day-today {
  background-color: #dfe9ff;
}

#calendar .fc-daygrid-day {
  height: 60px; /* 날짜 셀의 고정 높이 */
}

#calendar .fc-daygrid-day-events {
  height: 60px; /* 날짜 셀의 고정 높이 */
  text-align: left;
  text-decoration: none;
}

#calendar .fc-daygrid-more-link {
  text-decoration: none;
  color: #333333;
}

#calendar .fc-col-cell {

}

/* 날짜 셀의 하이퍼링크를 일반 텍스트로 표시 */
#calendar .fc-daygrid-day-number {
  pointer-events: none; /* 클릭 이벤트 제거 */
  text-decoration: none; /* 밑줄 제거 */
  color: inherit; /* 기본 텍스트 색상 사용 */
  cursor: default; /* 기본 커서로 변경 */
}

.fc .fc-toolbar-title {
  color: rgb(6, 6, 31); /* 변경하고자 하는 색상으로 설정 */
  font-size: 20px; /* 변경하고자 하는 크기로 설정 */
  font-weight: bold; /* 변경하고자 하는 폰트의 높이로 설정 */
}

.fc .fc-button {
  border: none; /* 버튼 테두리 제거 */
  box-shadow: none; /* 버튼 그림자 제거 */
}


#weekCalendar .fc .fc-button {
  border: none; /* 버튼 테두리 제거 */
  box-shadow: none; /* 버튼 그림자 제거 */
  height: 10%;
  font-size: 15px;
  background-color: rgb(142, 192, 192);
}

#calendar .fc-col-header-cell-cushion {
  /* 밑줄 제거 */
  text-decoration: none;

  cursor: pointer;

  color: #ffffff;
  //height: 10%;
}

#calendar .fc-col-header-cell {
  background-color: #8ec0c0;
}

.fc .fc-toolbar-title {
  color: rgb(157, 204, 204); /* 변경하고자 하는 색상으로 설정 */
  font-size: 20px; /* 변경하고자 하는 크기로 설정 */
  font-weight: bold; /* 변경하고자 하는 폰트의 높이로 설정 */
}

:root {
  --fc-border-color: rgb(187, 187, 187);
  --fc-today-bg-color: #fefee9;
  --fc-button-bg-color: rgb(142, 192, 192);
  --fc-button-active-bg-color: rgb(99, 178, 177);
  --fc-button-hover-bg-color: rgb(100, 150, 150);
}
</style>