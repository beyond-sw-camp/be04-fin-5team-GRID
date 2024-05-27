<template>
  <div class="all">
    <div class="header">
      <Header />
    </div>
    <div class="sidebar">
      <Sidebar />
    </div>
    <div class="content">
      <AdTime />
    </div>
    <!-- <div class="footer">
    <Footer/>
</div> -->
  </div>
</template>

<script setup>
import { RouterLink, RouterView, useRouter } from 'vue-router';
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import Footer from '@/components/Footer.vue';
import AdTime from '@/components/AdTime/AdTimeList.vue';

const router = useRouter();



</script>


<style scoped>
html,
body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
}

.all {
  display: grid;
  grid-template-rows: 100px minmax(764px, auto);
  grid-template-columns: 250px minmax(1286px, auto);
  width: 100%;
  height: 100vh;
}

.header {
  grid-column-start: 1;
  grid-column-end: 3;
  background-color: #088A85;
}

.sidebar {
  grid-column-start: 1;
  grid-column-end: 2;
  background-color: #F8F9FAFF;
}

.content {
  grid-column-start: 2;
  grid-column-end: 3;
  background-color: white;
}
</style>