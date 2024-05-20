<template>
  <div class="all">
      <div class="header">Header</div>
      <div class="sidebar">Sidebar</div>
      <div class="content"><Policy /></div>
  </div>
</template>

<script setup>
  import Policy from "@/components/Vacation/VacationPolicy.vue";
</script>


<style scoped>
  /* Custom styles */
  html, body {
      margin: 0;
      padding: 0;
      width: 100%;
      height: 100%;
  }

  .all {
      display: grid;
      grid-template-rows: 100px minmax(980px, auto);
      grid-template-columns: 300px minmax(1620px,auto);
      width: 100%;
      height: 100vh;
  }

  .header {
      grid-column-start: 1;
      grid-column-end: 3;
      background-color: orange;
  }

  .sidebar {
      grid-column-start: 1;
      grid-column-end: 2;
      background-color: orange;
  }

  .content {
      grid-column-start: 2;
      grid-column-end: 3;
      background-color: white;
  }
</style>
