<template>
  <div class="mainContainer">
  <div class="mainTitle">
    <img class="mainIcon" src="@/assets/icons/goal_icon.png">
    <h1>메인</h1>
  </div>
  <div class="content">
    <AdTime />
  </div>
  </div>
</template>

<script setup>
import AdTime from '@/components/AdTime/AdTimeAdd.vue'
    import { provide, ref } from 'vue';
    import router from '@/router/router.js';
    import {RouterLink, RouterView} from 'vue-router';
    import WorkCalendar from "@/components/AdTime/WorkCalendar.vue";


    // function history() {
    //     router.push("/vacation/history")
    // }

    // function policy() {
    //     router.push("/vacation/policy")
    // }

    // function manage() {
    //     router.push("/vacation/manage")
    // }
</script>

<style scoped>
.mainContainer {
  display: grid;
  grid-template-rows: 18% 40% min-content 13%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.mainTitle {
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

.mainTitle h1 {
  margin-left: 0.5%;
  font-weight: 600;
  font-size: 25px;
}

.mainIcon {
  width: 80%;
}

.content {
  grid-row-start: 2;
  grid-row-end: 3;
  grid-column-start: 2;
  grid-column-end: 3;
  overflow: hidden;
}

.content > * {
  width: 100%;
  height: 100%;
}
</style>