<script setup>
  import axios from "axios";
  import {onMounted, reactive, ref} from "vue";

  const userId = ref();
  const userRole = ref();

  const isLoading = ref(true);

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
    registCheck: false
  })

  const putCommentData = reactive({
    chainId: 0,
    comment: ''
  })

  const putStatusData = reactive({
    typeId: 0,
    approvalId: 0,
    chainId: 0,
    chainStatus: '',
  })

  let pathList = [
    'bt', 'overtime', 'rw', 'vacation'
  ]

  function parseJwt(token) {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      return JSON.parse(jsonPayload);
    } catch (error) {
      console.error('Invalid token', error);
      return null;
    }
  }

  const fetchApprovalChain = async (typeId, approvalId) => {
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

  const setChain = async () => {
    for (const chain of state.approvalChainList) {
      if (chain['comment'] !== null) state.comment = true;
      if (chain['employeeId'] === userId.value) {
        state.chain = chain;

        putCommentData.chainId = chain['id'];
        putStatusData.chainId = chain['chainId'];
      }
    }
  }

  // 승인/반려 버튼 표시 여부 판단
  const statusCheck = async () => {
    if (state.approvalChainList[0]['employeeId'] === userId.value && state.approvalChainList[0].chainStatus === 'W') {
      state.show = true;
    }

    if (props.typeId === '1'){
      if (state.approvalChainList[1]['employeeId'] === userId.value && state.approvalChainList[1].chainStatus === 'W'
          && state.approvalChainList[0].chainStatus !== 'W' && props.approvalStatus === 'W' ) {
        state.show = true;
      }
    }
  }

  const registCheck = async () => {
    console.log(state.chain);
    if (state.chain['comment'] === null){
      console.log("댓글 입력 가능");
      state.registCheck = true;
    }
    console.log("그럼 여기?");
  }

  const registComment = async () => {
    if (state.registCheck) {
      console.log(state.registCheck)
      alert('댓글을 작성하시겠습니까?');

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
    } else {
      alert('댓글은 한 번만 작성 가능합니다.')
    }
  }

  const registStatus = async (status) => {

    let msg;

    if (status === 'A') {
      msg = "승인";
    }
    if (status === 'D') {
      msg = "반려";
    }

    alert(msg + '하시겠습니까?')

    putStatusData.chainStatus = status;
    putStatusData.typeId = props.typeId;
    putStatusData.approvalId = props.approvalId;

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

      console.log(response)

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

  const printApproval = async () => {

    alert('문서를 다운로드 하시겠습니까?');

    try {
      const response = await axios.post(`http://localhost:8080/approval/pdf/${props.typeId}/${props.approvalId}`);

      var url = `http://localhost:8080/approval/downloadPdf/${props.typeId}/${props.approvalId}`;

      // 새 창 열기
      window.open(url, "_blank");

      if (response.status !== 201) {
        throw new Error("response is not ok");
      }

    } catch (error) {
      console.error('Fail to post: ', error.message);
    }
  }

  onMounted(async () => {
    const token = localStorage.getItem('access');

    if (token) {
      const decodedToken = parseJwt(token);

      userId.value = decodedToken.id || '';
      userRole.value = decodedToken.auth || '';
    }

    await fetchApprovalChain(props.typeId, props.approvalId);
    await registCheck();
    await statusCheck();
    await setChain();

    isLoading.value = false;
  })
</script>

<template>
  <div>
    <h3 class="fw-bolder mb-3"><i class="bi bi-link-45deg"></i>&nbsp; 결재 라인</h3>
    <b-card class="container shadow">
      <div class="list-group-item list-group-item-action d-flex" aria-current="true">
        <div class=" mb-3 pt-2 d-flex gap-2 w-100 justify-content-between">
          <h6 class="opacity-50"></h6>
          <h6 class="mb-0 opacity-75 fw-bolder">이름</h6>
          <h6 class="mb-0 opacity-75 fw-bolder">&nbsp;&nbsp;&nbsp; 단계</h6>
          <h6 class="mb-0 opacity-75 fw-bolder">상태&nbsp;</h6>
        </div>
      </div>
      <div v-for="chain in state.approvalChainList" :key="chain.id">
        <div href="#" class="list-group-item list-group-item-action d-flex gap-3 py-3" aria-current="true">
          <img :src="chain.user['profilePath']" alt="profile" width="50" height="50" class="rounded-circle flex-shrink-0">
          <div class="d-flex gap-2 w-100 justify-content-between">
            <div class="mt-1">
              <h5 class="fw-bolder mb-0">&nbsp;&nbsp;&nbsp;&nbsp;{{ chain.user['name'] }}</h5>
              <p class="mb-0 opacity-75">&nbsp;&nbsp;&nbsp;&nbsp;{{ chain.user['team'].teamName }} / {{ chain.user['duties'].dutiesName }}</p>
            </div>
            <h6 class="mt-3">{{ chain.stage }}단계</h6>
            <div>
              <b-badge class="mt-3" variant="success" v-if="chain.chainStatus === 'A'">승인</b-badge>
              <b-badge class="mt-3" variant="danger" v-if="chain.chainStatus === 'D'">반려</b-badge>
              <b-badge class="mt-3" variant="warning" v-if="chain.chainStatus === 'W'">대기</b-badge>
            </div>
          </div>
        </div>
      </div>
    </b-card>
    <b-card class="shadow mt-3">
      <h5 class="fw-bolder">댓글</h5>
      <div v-for="chain in state.approvalChainList" :key="chain.id">
        <template v-if="chain.comment !== null">
          <div class="d-flex text-body-secondary pt-3">
            <h4 class="mt-2" ><i class="bi bi-person flex-shrink-0 me-2 rounded"></i></h4>
            <div class="pb-3 small lh-sm border-bottom w-100">
              <div class="d-flex justify-content-between">
                <strong class="text-gray-dark fs-6 mb-1">&nbsp;&nbsp;&nbsp;{{ chain.user['name'] }}</strong>
                <div v-if="chain.approvalTime !== null">&nbsp;{{ chain.approvalTime.substring(0, 10) }}</div>
              </div>
              <span class="d-block fs-6">&nbsp; {{ chain.comment }}</span>
            </div>
          </div>
        </template>
      </div>
      <div style="margin-top: 30px;">
        <b-input-group>
          <b-form-input v-model="putCommentData.comment"></b-form-input>
          <b-input-group-append>
            <b-button variant="outline-success" @click="registComment">등록</b-button>
          </b-input-group-append>
        </b-input-group>
      </div>
    </b-card>
    <div v-if="props.requesterId === userId">
      <!-- 중복 취소 불가하는 코드 추가 -->
      <b-button @click="cancelApproval">취소</b-button>
    </div>
    <div v-if="props.requesterId === userId || userRole === 'ROLE_ADMIN'">
      <b-button @click="printApproval">출력</b-button>
    </div>
    <div v-if="state.show">
      <b-button variant="success" @click="registStatus('A')">승인</b-button>
      <b-button variant="danger" @click="registStatus('D')">반려</b-button>
    </div>
  </div>
</template>

<style scoped>

</style>