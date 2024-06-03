<template>
  <div class="content"><GoalDetail /></div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import GoalDetail from '@/components/PerformanceReview/Goal/GoalDetail.vue';
import GoalAdd from "@/components/PerformanceReview/Goal/GoalAdd.vue";

const router = useRouter();


</script>


<style scoped>
.content {
  width: 100%;
  height: 100%;
  background-color: white;
}
</style>