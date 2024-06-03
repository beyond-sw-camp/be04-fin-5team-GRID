<script setup>
  import {onMounted, reactive, ref} from "vue";
  import axios from "axios";

  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const userRole = ref('');
  const userId = ref();

  const isLoading = ref(true);

  const state = reactive({
    approvalList:[]
  });

  const fetchApprovalList = async() => {
    try {
      const url = `http://localhost:8080/approval/all/3/5`;

      const response = await axios.get(url);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.approvalList = response.data.approvalEmpResultList;
      state.approvalList.type = "rw";

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async() => {
    await fetchApprovalList();

    isLoading.value = false;
  })
</script>

<template>
  <nav style="--bs-breadcrumb-divider: '>'; margin-top: -35px; margin-bottom: -7px;" aria-label="breadcrumb">
    <ol class="breadcrumb">
      <li class="breadcrumb-item"><a href="http://localhost:5173/work" style="text-decoration: none; color: grey; font-size: 17px;"><i class="bi bi-list-check"></i>&nbsp; 근무 목록</a></li>
      <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i class="bi bi-heart-pulse"></i>&nbsp; 단축 근무</span></li>
    </ol>
  </nav>
  <h3 class="fw-bolder pb-5"><i class="bi bi-heart-pulse"></i>&nbsp; 단축 근무</h3>
  <div v-if="isLoading">로딩 중</div>
  <div v-else>
    <ApprovalList :approvalList="state.approvalList"/>
  </div>
</template>

<style scoped>

</style>