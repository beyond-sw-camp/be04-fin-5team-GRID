<script setup>
  import { RouterLink, RouterView, useRouter } from 'vue-router';
  import Header from '@/components/Header.vue';
  import Sidebar from '@/components/Sidebar.vue';
  import Footer from '@/components/Footer.vue';

  import { computed } from 'vue';
  import {RouterLink, RouterView, useRoute, useRouter} from 'vue-router';
  import Header from '@/components/Header.vue';
  import Sidebar from '@/components/Sidebar.vue';
  import Footer from '@/components/Footer.vue';

  // 현재 경로를 가져오는 useRoute 훅 사용
  const route = useRoute();

  // 특정 경로에서는 레이아웃을 숨기기 위한 경로 설정
  const layoutHiddenPaths = [
  '/',
  '/find/id',
  '/find/pwd',
  '/find/id/result',
  '/find/pwd/:email/result',
  '/test'
  ];

  // 현재 경로가 layoutHiddenPaths에 포함되어 있는지 확인하는 computed 프로퍼티
  const showLayout = computed(() => {
  return !layoutHiddenPaths.some(hiddenPath => {
    const regex = new RegExp(`^${hiddenPath.replace(/:\w+/g, '[^/]+')}$`);
    return regex.test(route.path);
  });
  });

  // 컨테이너 클래스 설정
  const containerClass = computed(() => {
  return showLayout.value ? 'container' : 'container-full';
  });

  // 메인 컨텐츠 클래스 설정
  const mainContentClass = computed(() => {
  return showLayout.value ? 'main-content' : 'main-content-full';
  });

</script>

<template>

  <RouterView/>

</template>

<style scoped>
  .header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  }

  .sidebar {
  position: fixed;
  top: 60px; /* Header 높이만큼 아래로 */
  left: 0;
  width: 250px;
  height: calc(100vh - 60px); /* Header 높이만큼 뺀 높이 */
  z-index: 800;
  }

  .content {
  margin-top: 60px; /* Header 높이만큼 아래로 */
  margin-left: 250px; /* Sidebar 너비만큼 왼쪽 여백 추가 */
  z-index: 500;
  }

  .footer {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  z-index: 600;
  }
</style>




