<template>
  <div class="content"><TotalList/></div>
</template>

<script setup>
import {useRouter} from 'vue-router';
import TotalList from '@/components/PerformanceReview/Total/TotalPerformanceReviewList.vue';
import TotalDetail from "@/components/PerformanceReview/Total/TotalPerformanceReivewDetail.vue";

const router = useRouter();


</script>

<style scoped>
.content {
  width: 100%;
  height: 100%;
  background-color: white;
}
</style>