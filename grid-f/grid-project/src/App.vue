<template>
  <div class="all">
      <div class="header">
          <RouterLink to="/vacation/history" replace>history</RouterLink>
          <RouterLink to="/vacation/policy" replace>policy</RouterLink>
          <RouterLink to="/vacation/manage" replace>manage</RouterLink>
      </div>
      <div class="sidebar">Sidebar</div>
      <div class="content"> <RouterView/></div>
  </div>
</template>

<script setup>
  import Policy from "@/components/Vacation/Policy/VacationPolicy.vue";
  import PolicyModify from "@/components/Vacation/Policy/VacationPolicyModify.vue";
  import PolicyRegist from "@/components/Vacation/Policy/VacationPolicyRegist.vue";
  import History from "@/components/Vacation/History/VacationHistory.vue";
  import Manage from "@/components/Vacation/Manage/VacationManage.vue";
  import ManageRegist from "@/components/Vacation/Manage/VacationManageRegist.vue";
  import ManageDelete from "@/components/Vacation/Manage/VacationManageDelete.vue";
  import Main from "@/views/MainView.vue"
  import {RouterLink, RouterView} from 'vue-router';

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
      grid-template-rows: 100px minmax(764px, auto);
      grid-template-columns: 250px minmax(1286px,auto);
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
