<script setup>
  import axios from "axios";
  import {onMounted, reactive} from "vue";

  const employeeId = 1;

  const props = defineProps({
    typeId: 0,
    approvalId: 0,
    approvalStatus: '',
    requesterId: 0,
    cancelStatus: ''
  })

  const state = reactive({
    approvalChainList: [],
    chain: Object,
    show: false,
  })

  const putCommentData = reactive({
    chainId: 0,
    comment: ''
  })

  const putStatusData = reactive({
    typeId: props.typeId,
    approvalId: props.approvalId,
    chainId: 0,
    chainStatus: '',
  })

  let pathList = [
      'bt', 'overtime', 'rw', 'vacation'
  ]

  const fetchApprovalChain = async (typeId, approvalId) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval-chain/list/${typeId}/${approvalId}`);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.approvalChainList = response.data.chainResultList;
      console.log(state.approvalChainList);

    } catch {
      console.error('Fetch error: ' + error.message);
    }
  }

  const setChain = async () => {
    for (const chain of state.approvalChainList) {
      if (chain['employeeId'] === employeeId) {
        state.chain = chain;
        putCommentData.chainId = chain['id'];
        putStatusData.chainId = chain['chainId'];
      }
    }
    console.log(state.chain);
  }

  // 버튼과 댓글 입력 표시 여부 판단
  const statusCheck = async () => {
    if (state.approvalChainList[0]['employeeId'] === employeeId && state.approvalChainList[0].chainStatus === 'W') {
      state.show = true;
    }

    if (props.typeId === '1'){
      if (state.approvalChainList[1]['employeeId'] === employeeId && state.approvalChainList[1].chainStatus === 'W'
          && state.approvalChainList[0].chainStatus !== 'W' && props.approvalStatus === 'W' ) {
        state.show = true;
      }
    }
  }

  const registCheck = async () => {
    if (state.approvalChainList[0]['employeeId'] === employeeId) {
      if (state.chain['comment'] === null){
        console.log("댓글 입력 가능");
        return true;
      }
    }

    if (props.typeId === 1){
      if (state.approvalChainList[1]['employeeId'] === employeeId) {
        if (state.chain['comment'] === null) {
          return true;
        }
      }
    }
  }

  const registComment = async () => {
    if (registCheck) {
      alert('댓글을 작성하시겠습니까?');
      console.log(putCommentData);

      try {
        const response = await axios.put(`http://localhost:8080/approval-chain/${pathList[props.typeId - 1]}`, putCommentData, {
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
  }

  const registStatus = async (status) => {

    let text;

    if (status === 'A') {
      text = "승인";
    }
    if (status === 'D') {
      text = "반려";
    }

    alert(text + '하시겠습니까?')

    putStatusData.chainStatus = status;
    console.log(putStatusData);

    try {
      const response = await axios.put(`http://localhost:8080/approval-chain/status`, putStatusData, {
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

  const cancelApproval = async () => {

    alert('결재를 취소하시겠습니까?');

    try {
      const response = await axios.post(`http://localhost:8080/approval/bt/${props.approvalId}`);

      if (response.status !== 201) {
        throw new Error("response is not ok");
      }

    } catch (error) {
      console.error('Fail to post: ', error.message);
    }
  }

  onMounted(async () => {
    await fetchApprovalChain(props.typeId, props.approvalId);
    await statusCheck();
    await setChain();
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
    <div v-if="registCheck && props.requesterId !== employeeId">
    <!-- 무효화는 show를 활용 -->
      <b-input-group>
        <b-form-input v-model="putCommentData.comment"></b-form-input>
        <b-input-group-append>
          <b-button variant="outline-success" @click="registComment">Button</b-button>
        </b-input-group-append>
      </b-input-group>
    </div>
    <div v-if="props.requesterId === employeeId && props.cancelStatus === 'N'">
      <b-button @click="cancelApproval">취소</b-button>
    </div>
    <div v-if="state.show">
      <b-button variant="success" @click="registStatus('A')">승인</b-button>
      <b-button variant="danger" @click="registStatus('D')">반려</b-button>
    </div>
  </div>
</template>

<style scoped>

</style>