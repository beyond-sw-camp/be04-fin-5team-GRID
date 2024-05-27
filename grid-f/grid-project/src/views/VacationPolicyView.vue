<template>
    
    
    <div class="all">
      <div class="header">
          <RouterLink to="/vacation/history" replace>history</RouterLink>
          <RouterLink to="/vacation/policy" replace>policy</RouterLink>
          <RouterLink to="/vacation/manage" replace>manage</RouterLink>
          <RouterLink to="/vacation/info" replace>info</RouterLink>
      </div>
      <div class="sidebar">Sidebar</div>
      <div class="content"><Policy /></div>
</div>
</template>

<script setup>
    import Policy from "../components/Vacation/Policy/VacationPolicy.vue";

</script>

<style scoped>
  .all {
    display: grid;
    grid-template-rows: 100px minmax(764px, auto);
    grid-template-columns: 250px minmax(1286px,auto);
    width: 100%;
    height: 100vh;
  }

  .header {
      grid-column-start: 1;
      grid-column-end: 3;
      background-color: #088A85;
  }

  .sidebar {
      grid-row: 2;
      background-color: #088A85;
  }

  .content {
     grid-row: 2;
      background-color: white;
  }
</style>
