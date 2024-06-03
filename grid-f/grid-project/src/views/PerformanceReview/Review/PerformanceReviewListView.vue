<template>
  <div class="content"><ReviewList /></div>
</template>

<script setup>
import { RouterLink, RouterView, useRouter } from 'vue-router';
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import Footer from '@/components/Footer.vue';
import ReviewList from '@/components/PerformanceReview/Review/PerformanceReivewList.vue';
import ReviewDetail from "@/components/PerformanceReview/Review/PerformanceReviewDetail.vue";
const router = useRouter();


</script>

<style scoped>
.content {
  width: 100%;
  height: 100%;
  background-color: white;
}
</style>