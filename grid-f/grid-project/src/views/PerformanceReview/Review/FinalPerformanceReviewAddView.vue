<template>
  <div class="content"><ReviewAdd /></div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import ReviewAdd from '@/components/PerformanceReview/Review/FinalPerformanceReviewAdd.vue';
import GoalList from "@/components/PerformanceReview/Goal/GoalList.vue";
const router = useRouter();


</script>

<style scoped>
.content {
  width: 100%;
  height: 100%;
  background-color: white;
}
</style>