<!--메인으로 합쳐짐-->
<template>
  <div class="all">
      <AdTimeAdd />
  </div>
</template>

<script setup>
import { RouterLink, RouterView, useRouter } from 'vue-router';
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import Footer from '@/components/Footer.vue';
import AdTimeAdd from '@/components/AdTime/AdTimeAdd.vue';

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
  grid-template-rows: minmax(764px, auto);
  grid-template-columns: minmax(1286px, auto);
  width: 100%;
  height: 100vh;
}
</style>