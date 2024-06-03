<template>
  <div class="content"><GoalAdd /></div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import GoalAdd from '@/components/PerformanceReview/Goal/GoalAdd.vue';
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