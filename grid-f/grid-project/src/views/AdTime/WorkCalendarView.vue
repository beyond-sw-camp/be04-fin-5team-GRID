<template>
  <div class="all">
    <WorkCalendar />
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import WorkCalendar from "@/components/AdTime/WorkCalendar.vue";

const router = useRouter();



</script>


<style scoped>
html,
body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
}

.all {
  display: grid;
  grid-template-rows: minmax(764px, auto);
  grid-template-columns: minmax(1286px, auto);
  width: 100%;
  height: 100vh;
}
</style>