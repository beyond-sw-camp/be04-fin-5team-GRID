<template>
  <div class="content"><ReviewDetail /></div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import ReviewDetail from '@/components/PerformanceReview/Review/PerformanceReviewDetail.vue';
import ReviewAdd from "@/components/PerformanceReview/Review/MidPerformanceReviewAdd.vue";
const router = useRouter();


</script>

<style scoped>
.content {
  width: 100%;
  height: 100%;
  background-color: white;
}
</style>