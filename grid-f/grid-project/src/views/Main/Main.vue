 <template>
  <div class="mainContainer">
    <div class="mainTitle">
      <img class="mainIcon" src="@/assets/icons/goal_icon.png">
      <h1>메인</h1>
    </div>
    <div class="content">
      <AdTime />
    </div>
    <div class="approvalContent">
      <b-card title="결재 필요 문서">
        <div class="text-end">
          <h6 class="text-muted" style="margin-bottom: 10px; margin-top: -30px;" @click="navigateTo('/required')">상세 <i class="bi bi-chevron-right"></i></h6>
        </div>
        <br>
        <ApprovalList :approvalList="state.sReqApprovalList" :short="1"/>
      </b-card>
    </div>
  </div>
</template>

<script setup>
  import AdTime from '@/components/AdTime/AdTimeAdd.vue'
  import axios from "axios";
  import {onMounted, provide, reactive, ref} from 'vue';
  import {RouterLink, RouterView, useRouter} from 'vue-router';
  import WorkCalendar from "@/components/AdTime/WorkCalendar.vue";

  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const router = useRouter();

  const userRole = ref('');
  const userId = ref();

  const isLoading = ref(true);

  const state = reactive({
    sReqApprovalList: []
  })

  const navigateTo = (path) => {
    router.push(path);
  };

  // function history() {
  //     router.push("/vacation/history")
  // }

  // function policy() {
  //     router.push("/vacation/policy")
  // }

  // function manage() {
  //     router.push("/vacation/manage")
  // }
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

  const fetchReqApprovalList = async (typeId, approvalStatus, approverId) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval/approver/${typeId}/${approvalStatus}/${approverId}`);
      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.sReqApprovalList = response.data.approvalEmpResultList.slice(0, 5);
      console.log(state.sReqApprovalList);
      state.sReqApprovalList.type = 1;

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async () => {
    const token = localStorage.getItem('access');
    if (token) {
      const decodedToken = parseJwt(token);

      userId.value = decodedToken.id || '';
      userRole.value = decodedToken.auth || '';
    }

    if (userRole.value !== 'ROLE_ADMIN') {
      await fetchReqApprovalList(0, 5, userId.value);
    }

    isLoading.value = false;
  })
</script>

<style scoped>
.mainContainer {
  display: grid;
  grid-template-rows: 18% 30% min-content 13%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.mainTitle {
  grid-column-start: 2;
  grid-column-end: 3;
  font-size: 12px;
  font-weight: 0;
  margin-top: 2%;
  color: #000000;
  display: grid;
  grid-template-columns: 3% 97%;
  align-items: center;
}

.mainTitle h1 {
  margin-left: 0.5%;
  font-weight: 600;
  font-size: 25px;
}

.mainIcon {
  width: 80%;
}

.content {
  grid-row-start: 2;
  grid-row-end: 3;
  grid-column-start: 2;
  grid-column-end: 3;
  overflow: hidden;
}

.content > * {
  width: 100%;
  height: 100%;
}

.approvalContent {
  grid-row-start: 3;
  grid-row-end: 4;
  grid-column-start: 2;
  grid-column-end: 3;
}
</style>