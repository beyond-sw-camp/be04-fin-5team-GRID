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
      contentHeight: 'auto',
      firstDay: 1,    // 월요일 시작
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
        right: 'mainCalendar adTime'
      },
      customButtons: {
        mainCalendar: {
          text: '메인',
          click: function () {
            router.push('/main'); // main으로 수정
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
      eventDisplay: 'block', // 이벤트 표시를 블록형으로 설정
      eventDidMount: function (info) {
        var eventEl = info.el;
        eventEl.style.overflow = 'hidden';
        eventEl.style.textOverflow = 'ellipsis';
        eventEl.style.whiteSpace = 'nowrap';
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

const fetchEmployeeEvent = async () => {
  try {
    // 출근 조회
    const responseAdTime = await axios.get(`https://api.gridhr.site/ad-time/${userId.value}`);

    const adTime = responseAdTime.data.adTimeDTOList;
    console.log(adTime);

    const adEvents = transformAdEvents(adTime);
    console.log(adTime.attendanceStatus);
    console.log(events.value);
    // 출장 조회
    const responseBt = await axios.get(`https://api.gridhr.site/approval/list/1/1/${userId.value}`);
    console.log(responseBt.data);

    const bt = responseBt.data.approvalEmpResultList
    const btEvents = transformEvents(bt, '출장', '#fad7d7');


    // 시간외 근무 조회
    const responseO = await axios.get(`https://api.gridhr.site/approval/list/2/1/${userId.value}`);
    console.log(responseO.data);

    const o = responseO.data.approvalEmpResultList
    const oEvents = transformEvents(o, '시간외 근무', '#c0caff');

    // 단축 근무 조회
    const responseRw = await axios.get(`https://api.gridhr.site/approval/list/3/1/${userId.value}`);
    console.log(responseRw.data);

    const rw = responseRw.data.approvalEmpResultList
    const rwEvents = transformEvents(rw, '단축 근무', '#cbffb6');

    // 휴가 조회
    const responseV = await axios.get(`https://api.gridhr.site/approval/list/4/1/${userId.value}`);
    console.log(responseV.data);

    const v = responseV.data.approvalEmpResultList
    const vEvents = transformEvents(v, '휴가', '#ffdbf7');


    events.value = [...adEvents, ...btEvents, ...oEvents, ...rwEvents, ...vEvents];
    console.log(events.value);

  } catch (error) {
    console.error('에러 발생:', error);
  }
};

const fetchAllEvent = async () => {
  try {
    // 출근 조회
    const responseAdTime = await axios.get(`https://api.gridhr.site/ad-time/${userId.value}`);

    const adTime = responseAdTime.data.adTimeDTOList;
    console.log(adTime);

    const adEvents = transformAdEvents(adTime);
    console.log(adTime.attendanceStatus);
    console.log(events.value);
    // updateCalendarEvents(events.value);
    // 출장 조회
    const responseBt = await axios.get(`https://api.gridhr.site/approval/list/1/1/${userId.value}`);
    console.log(responseBt.data);

    const bt = responseBt.data.approvalEmpResultList
    const btEvents = transformEvents(bt, '출장', '#fad7d7');


    // 시간외 근무 조회
    const responseO = await axios.get(`https://api.gridhr.site/approval/list/2/1/${userId.value}`);
    console.log(responseO.data);

    const o = responseO.data.approvalEmpResultList
    const oEvents = transformEvents(o, '시간외 근무', '#c0caff');

    // 단축 근무 조회
    const responseRw = await axios.get(`https://api.gridhr.site/approval/list/3/1/${userId.value}`);
    console.log(responseRw.data);

    const rw = responseRw.data.approvalEmpResultList
    const rwEvents = transformEvents(rw, '단축 근무', '#cbffb6');

    // 휴가 조회
    const responseV = await axios.get(`https://api.gridhr.site/approval/list/4/1/${userId.value}`);
    console.log(responseV.data);

    const v = responseV.data.approvalEmpResultList
    const vEvents = transformEvents(v, '휴가', '#ffdbf7');


    events.value = [...adEvents, ...btEvents, ...oEvents, ...rwEvents, ...vEvents];
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


// function transformEvents(list, type, color) {
//   return list.map(item => ({
//     title: type,
//     start: item.startTime? item.startTime.replace(" ", "T"): null,
//     end: item.endTime? item.endTime.replace(" ", "T"): null,
//     color: color
//   }));
// }

// end 하루 증가
function transformEvents(list, type, color) {
  return list.map(item => {
    // 날짜 변환
    const start = item.startTime ? item.startTime.replace(" ", "T") : null;
    let end = item.endTime ? item.endTime.replace(" ", "T") : null;

    // end 날짜를 하루 증가시킴
    if (end) {
      const endDate = new Date(end);
      endDate.setDate(endDate.getDate() + 1);
      end = endDate.toISOString().split('T')[0];
    }

    return {
      title: type,
      start: start,
      end: end,
      color: color
    };
  });
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

<style>
#app {
  place-items: center;
  width: 100%;
  height: 100%;
}


#calendar {
  width: 100%;
  height: 100%;
  overflow-y: auto; /* 스크롤바 추가 */
}

/*.fc-toolbar {
  background-color: #f0f0f0;
  border-bottom: 1px solid #ddd;
}
.fc-daygrid-day {
  background-color: #f9f9f9;
}
.fc-event {
  background-color: #007bff;
  border: none;
  color: #fff;
}*/

#calendar .fc-day-today {
  background-color: #dfe9ff;
}

#calendar .fc-daygrid-day {
  height: 60px; /* 날짜 셀의 고정 높이 */

}

#calendar .fc-daygrid-day-events {
  max-height: 100px; /* 날짜 셀의 고정 높이 */
  text-align: left;
  overflow: auto;
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

  color: #ffffff;
}

#calendar .fc-col-cell {

}

/* 날짜 칸 크기 고정 */
#calendar .fc-daygrid-day-frame {
  display: flex;
  flex-direction: column;
  height: 80px; /* 날짜 셀의 고정 높이 설정 */
  overflow: hidden;
}

#calendar .fc-daygrid-day-top {
  flex: 0 0 auto;
}

#calendar .fc-daygrid-day-events {
  flex: 1 1 auto;
  max-height: 60px; /* 이벤트 영역의 최대 높이 설정 */
  overflow: auto; /* 넘치는 내용 숨기기 */
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