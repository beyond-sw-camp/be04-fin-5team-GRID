<template>
  <div class="calenderContainer">
    <div class="calenderTitle">
      <h1><i class="bi bi-stickies"></i>&nbsp;근무 관리</h1>
    </div>
    <div class="content">
      <WorkCalendar />
    </div>
  </div>
</template>

<script setup>
import {useRouter} from 'vue-router';
import WorkCalendar from '@/components/AdTime/WorkCalendar.vue';

const router = useRouter();


</script>


<style scoped>
.calenderContainer {
  display: grid;
  /*grid-template-rows: 18% 65.5% 4.5% 12%;*/
  grid-template-rows: 18% 69% 13%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.calenderTitle {
  grid-column-start: 2;
  grid-column-end: 3;
  font-size: 12px;
  font-weight: 0;
  margin-top: 2%;
  color: #000000;
  display: grid;
  //grid-template-columns: 3% 97%;
  align-items: center;
}

.calenderTitle h1 {
  margin-left: 0.5%;
  font-weight: 600;
  font-size: 25px;
}

.calenderIcon {
  width: 80%;
}

.content {
  grid-row-start: 2;
  grid-row-end: 4;
  grid-column-start: 2;
  grid-column-end: 3;
  overflow: hidden;
}

.content > * {
  width: 100%;
  height: 100%;
}

/*.content {
  grid-row-start: 3;
  grid-row-end: 4;
  grid-column-start: 2;
  grid-column-end: 3;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}*/
</style>