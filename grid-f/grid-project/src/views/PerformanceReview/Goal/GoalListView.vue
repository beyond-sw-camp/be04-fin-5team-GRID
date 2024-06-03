<template>
  <div class="content"><GoalList /></div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import GoalList from '@/components/PerformanceReview/Goal/GoalList.vue';
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