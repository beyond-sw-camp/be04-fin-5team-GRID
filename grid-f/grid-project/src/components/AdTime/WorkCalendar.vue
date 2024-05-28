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

const fetchEvent = async ()  => {
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
    start: item.startTime.replace(" ", "T"),
    end: item.endTime.replace(" ", "T")
  }));
}

onMounted(async () => {
  await fetchEvent();
  initCalendar(events);
});


</script>

<style scoped>
#calendar {
  width: 100%;
  height: 600px;
}
</style>