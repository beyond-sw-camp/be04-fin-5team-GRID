<template>
  <!-- <div class="all">
      <div class="header">
          <RouterLink to="/vacation/history" replace>history</RouterLink>
          <RouterLink to="/vacation/policy" replace>policy</RouterLink>
          <RouterLink to="/vacation/manage" replace>manage</RouterLink>
          <RouterLink to="/vacation/info" replace>info</RouterLink>
      </div>
      <div class="sidebar">Sidebar</div>
      <div class="content"> <RouterView/></div>
  </div> -->
  <div class="wrapper">
    <RouterView/>
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
  import { RouterLink, RouterView, useRouter } from 'vue-router';

  const router = useRouter();

</script>


<style scoped>

</style>
