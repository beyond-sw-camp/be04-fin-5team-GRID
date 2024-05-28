<script setup>
  import { computed } from 'vue';
  import { RouterView, useRoute } from 'vue-router';
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
  <div class="container" :class="containerClass">
    <div class="header" v-if="showLayout">
      <Header />
    </div>
    <div class="sidebar" v-if="showLayout">
      <Sidebar />
    </div>
    <div class="main-content" :class="mainContentClass">
      <RouterView />
    </div>
    <div class="footer" v-if="showLayout">
<!--      <Footer />-->
    </div>
  </div>

</template>

<style scoped>

body {
    margin: 0;
    padding: 0;
    width: 100vw;
    height: 100vh;
}

.container {
    display: grid;
    grid-template-columns: 200px 1fr;
    grid-template-rows: 60px auto 10px;
    grid-template-areas:
        "header header"
        "side body"
        "side footer";
    height: 100%;
    min-width: 100%;
    width: 100%;
    margin: 0;
    padding: 0;
}

.header {
    grid-area: header;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 1000;
    padding: 0;
}

.sidebar {
    grid-area: side;
    position: fixed;
    top: 60px;
    left: 0;
    width: 180px;
    height: calc(100vh - 60px);
    z-index: 800;
    font-size: 14px;
}

.main-content {
    grid-area: body;
    margin-top: 60px;
    padding: 0 ;
    height: calc(100vh - 60px);
}

.footer {
    grid-area: footer;
    position: fixed;
    bottom: 0;
    width: calc(100% - 180px);
    z-index: 600;
    font-size: 10px;
    justify-self: flex-end;
}


.container-full {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  padding: 0;
}

.main-content-full {
  padding: 0;
  margin: 0;
}
</style>
