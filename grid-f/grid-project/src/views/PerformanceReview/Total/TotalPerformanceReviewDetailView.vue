<template>
  <div class="content"><TotalDetail/></div>
</template>

<script setup>
import {useRouter} from 'vue-router';
import TotalDetail from '@/components/PerformanceReview/Total/TotalPerformanceReivewDetail.vue';
import HistoryMain from "@/components/Vacation/History/VacationHistory.vue";

const router = useRouter();


</script>

<style scoped>
.content {
  width: 100%;
  height: 100%;
  background-color: white;
}
</style>