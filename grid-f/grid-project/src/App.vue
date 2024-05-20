<script setup>
    import { RouterLink, RouterView, useRouter } from 'vue-router';
    import Header from '@/components/Header.vue';
    import Sidebar from '@/components/Sidebar.vue';
    import Footer from '@/components/Footer.vue';

    const router = useRouter();


</script>

<template>
  <Header></Header>
  <Sidebar></Sidebar>
  <Footer></Footer>
  <!-- <RouterView /> -->

</template>


<style scoped>
</style>

