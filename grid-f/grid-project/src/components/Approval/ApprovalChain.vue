<script setup>
  import axios from "axios";
  import {onMounted, reactive, ref} from "vue";

  const employeeId = 1;

  const props = defineProps({
    typeId: 0,
    approvalId: 0
  })

  const state = reactive({
    approvalChainList: [],
    flag: false
  })

  const postData = reactive({
    chainId: 0,
    comment: ''
  })

  let pathList = [
      'bt', 'overtime', 'rw', 'vacation'
  ]

  const commentCheck = async() => {
    for (const chain of state.approvalChainList) {
      if (chain.comment === null) {
        if (chain['user'].id === employeeId) {
          postData.chainId = chain.chainId;
          state.flag = true;
        }
      }
    }
    console.log(state.flag)
  }

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

  const registComment = async () => {
    alert('댓글을 작성하시겠습니까?');

    console.log(postData);
    try {
      const response = await axios.put(`http://localhost:8080/approval-chain/${pathList[props.typeId - 1]}`, postData, {
        headers: {
          'Content-Type': "application/json"
        }
      })
      if (response.status !== 200) {
        throw new Error("response is not ok");

      }

    } catch (error) {
      console.error('Fail to post: ', error.message);
    }
  }

  onMounted(async() => {
    await fetchApprovalChain(props.typeId, props.approvalId);
    await commentCheck();
  })
</script>

<template>
  <div>
    <h5>결재 라인</h5>
    <div v-for="chain in state.approvalChainList" :key="chain.id">
      <div><img :src="chain.user['profilePath']"></div>
      <div>{{ chain.user['name'] }}</div>
      <div>{{ chain.user['team'].teamName }} {{ chain.user['duties'].dutiesName }}</div>
      <div>{{ chain.stage }}</div>
      <div>{{ chain.chainStatus }}</div>
    </div>
    <div v-for="chain in state.approvalChainList" :key="chain.id">
      <template v-if="chain.comment !== null">
        <div>{{ chain.user['name'] }}</div>
        <div>{{ chain.comment }}</div>
        <div>{{ chain.approvalTime }}</div>
      </template>
    </div>
    <div v-if="state.flag">
      <b-input-group>
        <b-form-input v-model="postData.comment"></b-form-input>
        <b-input-group-append>
          <b-button variant="outline-success" @click="registComment">Button</b-button>
        </b-input-group-append>
      </b-input-group>
    </div>
  </div>
</template>

<style scoped>

</style>