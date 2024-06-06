<script setup>
  import axios from "axios";
  import {onMounted, reactive, ref} from "vue";
  import {useRouter} from "vue-router";

  const userId = ref();
  const userRole = ref();

  const isLoading = ref(true);

  const router = useRouter();

  const props = defineProps({
    typeId: 0,
    approvalId: 0,
    approvalStatus: '',
    requesterId: 0,
    cancelStatus: '',
    cancelDoc: 0
  })

  const state = reactive({
    approvalChainList: [],
    chain: null,
    show: false,
    comment: false,
    status: props.approvalStatus
  })

  let pathList = [
    'bt', 'overtime', 'rw', 'vacation'
  ]

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
      const response = await axios.get(`https://api.gridhr.site/approval-chain/list/${typeId}/${approvalId}`);

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
      if (chain['employeeId'] === userId.value) {
        state.chain = chain;

        putCommentData.chainId = chain['id'];
        putStatusData.chainId = chain['chainId'];

        if (chain['comment'] !== null) state.comment = true;
      }
    }
  }

  // 승인/반려 버튼 표시 여부 판단
  const statusCheck = async () => {
    if (state.chain !== null) {
      if (state.chain['employeeId'] === state.approvalChainList[0]['employeeId'] && state.chain['chainStatus'] === 'W') {
        state.show = true;
      }

      if (props.typeId === '1'){
        if (state.chain['employeeId'] === state.approvalChainList[1]['employeeId'] && state.chain['chainStatus'] === 'W'
            && state.approvalChainList[0].chainStatus !== 'W' && (props.approvalStatus === 'W' || props.approvalStatus === 'V') ) {

          state.show = true;
        }
      }
    }

  }

  const registCheck = async () => {
    if (state.chain['comment'] === null){
      console.log("댓글 입력 가능");
      state.registCheck = true;
    }
  }

  const registComment = async () => {
    if (state.registCheck) {
      try {
        const confirmed = window.confirm('댓글을 작성하시겠습니까?');

        if(confirmed) {
          const response = await axios.put(`https://api.gridhr.site/approval-chain/${pathList[props.typeId - 1]}`, putCommentData, {
            headers: {
              'Content-Type': "application/json"
            }
          })

          window.location.reload();

          if (response.status !== 200) {
            throw new Error("response is not ok");
          }
        }
      } catch (error) {
        console.error('Fail to post: ', error.message);
      }
    } else {
      alert('댓글 작성이 불가합니다.');
    }
  }

  const registStatus = async (status) => {

    let msg;

    if (status === 'A') {
      msg = "승인";
    } else {
      msg = "반려";
    }

    putStatusData.chainStatus = status;
    putStatusData.typeId = props.typeId;
    putStatusData.approvalId = props.approvalId;
    putStatusData.chainId = state.chain['chainId'];

    try {
      const confirmed = window.confirm(msg + '하시겠습니까?');

      if(confirmed) {
        const response = await axios.put(`https://api.gridhr.site/approval-chain/status`, putStatusData, {
          headers: {
            'Content-Type': "application/json"
          }
        })

        window.location.reload();

        if (response.status !== 200) {
          throw new Error("response is not ok");
        }
      }
    } catch (error) {
      console.error('Fail to post: ', error.message);
    }
  }

  const cancelApproval = async () => {

    try {
      const confirmed = window.confirm('결재를 취소하시겠습니까?');

      if(confirmed) {
        const response = await axios.post(`https://api.gridhr.site/approval/${pathList[props.typeId - 1]}/${props.approvalId}`);

        if (response.status !== 201) {
          throw new Error("response is not ok");
        }
        if (props.approvalStatus === 'N') {
          alert('결재 제출이 취소되었습니다.');
          window.location.reload();
        } else {
          alert('취소 결재가 생성되었습니다.')
          router.push('/my');
        }
      }
    } catch (error) {
      console.error('Fail to post: ', error.message);
    }
  }

  const printApproval = async () => {

    try {
      const confirmed = window.confirm('pdf 파일을 다운로드 하시겠습니까?');

      if(confirmed) {
        const response = await axios.post(`https://api.gridhr.site/approval/pdf/${props.typeId}/${props.approvalId}`);

        var url = `https://api.gridhr.site/approval/downloadPdf/${props.typeId}/${props.approvalId}`;

        // 새 창 열기
        window.open(url, "_blank");

        if (response.status !== 201) {
          throw new Error("response is not ok");
        }
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
    await setChain();

    if (state.chain !== null) {
      await statusCheck();
      await registCheck();
    }

    isLoading.value = false;
  })
</script>
  <template>
    <div v-if="isLoading">
      로딩 중
    </div>
    <div v-else>
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
                <b-badge class="mt-3" variant="warning" v-if="chain.chainStatus === 'W'">진행중</b-badge>
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
                <b-button v-if="userId === props.requesterId || props.approvalStatus === 'A' || props.approvalStatus === 'D'" variant="outline-success" @click="registComment" disabled>등록</b-button>
                <b-button v-else variant="outline-success" @click="registComment">등록</b-button>
            </b-input-group-append>
          </b-input-group>
        </div>
      </b-card>
      <div>
          <!-- 중복 취소 불가하는 코드 추가 -->
          <div class="d-flex justify-content-center mt-3">
            <b-button v-if="props.requesterId === userId && props.cancelStatus === 'N' && props.cancelDoc === 0" @click="cancelApproval" class="mx-2">취소</b-button>
            <b-button v-if="(props.requesterId === userId || userRole === 'ROLE_ADMIN') && props.approvalStatus === 'A'" @click="printApproval" class="mx-2">출력</b-button>
          </div>
          <div v-if="state.show && props.cancelStatus === 'N'" class="d-flex justify-content-center mt-3">
            <b-button variant="success" @click="registStatus('A')" class="mx-2">승인</b-button>
            <b-button variant="danger" @click="registStatus('D')" class="mx-2">반려</b-button>
          </div>
      </div>
    </div>
  </template>

<style scoped>
.lineAll h3{
  margin-left: 0.5%;
  margin-bottom: 0;
  font-size: 25px;
  font-weight: 600;
}

.buttons {
  display:grid;
  grid-template-columns: 10% 18% 2% 18% 2% 18% 2% 18% 2% 10%;
  margin-top: 20px;
}

.cancel {
  grid-column-start: 2;
}

.print {
  grid-column-start: 4;
}

.approvalBtn {
  grid-column-start: 6;
}

.danger {
  grid-column-start: 8;
}
</style>