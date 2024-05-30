<script setup>
  import {onMounted, reactive} from "vue";
  import {useRoute} from "vue-router";
  import axios from "axios";

  import ApprovalCard from "@/components/Approval/ApprovalCard.vue";
  import ApprovalChain from "@/components/Approval/ApprovalChain.vue";

  const route = useRoute();
  const typeId = route.params.typeId;
  const approvalId = route.params.approvalId;

  const state = reactive({
    approval: [],
    user: []
  })

  const fetchApproval = async(typeId, approvalId) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval/detail/${typeId}/${approvalId}`);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.approval = response.data.approvalEmpResult;
      state.user = state.approval.user;

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async() => {
    await fetchApproval(typeId, approvalId);
  })
</script>

<template>
  <div>
    <h1 v-if="typeId === '1'">출장</h1>
    <h1 v-if="typeId === '2'">시간 외 근무</h1>
    <h1 v-if="typeId === '3'">단축</h1>
    <h1 v-if="typeId === '4'">휴가</h1>
  </div>
  <div v-if="state.approval && state.approval.user && state.approval.user.duties">
    <img :src="state.user['profilePath']" alt="profile">
    작성자: {{ state.user['name'] }}<br>
    {{ state.approval.user.duties.dutiesName }} / {{ state.user['team'].teamName }}
    <hr>
    {{state.user['email']}}
    {{state.user['callNumber']}}
    <hr>
  </div>
  <ApprovalCard :approval="state.approval"/>
  <ApprovalChain :typeId="typeId" :approvalId="approvalId" :approvalStatus="state.approval['approvalStatus']" :requesterId="state.approval['employeeId']" :cancelStatus="state.approval['cancelYN']"/>
</template>

<style scoped>

</style>