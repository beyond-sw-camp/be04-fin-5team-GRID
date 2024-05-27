<script setup>
  import axios from "axios";
  import {onMounted, reactive} from "vue";

  const props = defineProps({
    typeId: 0,
    approvalId: 0
  })

  const state = reactive({
    approvalChainList: [],
    commentList: []
  })

  const fetchApprovalChain = async(typeId, approvalId) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval-chain/list/${typeId}/${approvalId}`);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.approvalChainList = response.data.chainResultList;

    } catch {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async() => {
    await fetchApprovalChain(props.typeId, props.approvalId);
  })
</script>

<template>
  <div>
    <h5>결재 라인</h5>
    <div v-for="chain in state.approvalChainList" :key="chain.id">
      <div><img :src="chain.user['profilePath']"></div>
      <div>{{ chain.user['name'] }}</div>
      <div>{{ chain.user['teamId'] }} {{ chain.user['duties'] }}</div>
      <div>{{ chain.stage }}</div>
      <div>{{ chain.chainStatus }}</div>
    </div>
    <div v-for="chain in state.approvalChainList" :key="chain.id">
      <template  v-if="chain.comment !== null">
        <div>{{ chain.user['name'] }}</div>
        <div>{{ chain.comment }}</div>
        <div>{{ chain.approvalTime }}</div>
      </template>
    </div>
  </div>
</template>

<style scoped>

</style>