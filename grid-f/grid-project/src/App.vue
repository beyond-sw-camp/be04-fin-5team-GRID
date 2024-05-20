<script setup>
    import { RouterLink, RouterView, useRouter } from 'vue-router';
    import Header from '@/components/Header.vue';
    import Sidebar from '@/components/Sidebar.vue';
    import Footer from '@/components/Footer.vue';

    const router = useRouter();


</script>

<template>
  <div class="header">
    <Header/>
  </div>

  <div>
    <div id="sidebar">
      <Sidebar/> 
    </div>
    <!-- <div class="content">
      <RouterView />
    </div> -->
  </div>
  <div class="footer">
    <Footer/>
  </div>

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

