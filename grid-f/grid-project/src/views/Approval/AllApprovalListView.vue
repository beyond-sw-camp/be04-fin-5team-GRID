<script setup>
  import {onMounted, reactive} from "vue";
  import axios from "axios";

  import ApprovalList from "@/components/Approval/ApprovalList.vue";

  const admin = 1;
  const employeeId = 1;

  const state = reactive({
    approvalList: [],
    reqApprovalList: []
  });

  const fetchApprovalList = async(typeId, approvalStatus, employeeId) => {

    try {
      let url = `http://localhost:8080/approval/all/${typeId}/${approvalStatus}`;

      if (admin !== 1) {
        url = `http://localhost:8080/approval/list/${typeId}/${approvalStatus}/${employeeId}`;
      }

      const response = await axios.get(url);

      if (response === null || response.status !== 200) {
        throw new Error("response is not ok");
      }

      // 상태 업데이트
      state.approvalList = response.data.approvalEmpResultList;
      state.approvalList.type = typeId;

      console.log(url);

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  const fetchReqApprovalList = async(typeId, approvalStatus, approverId) => {
    try {
      const response = await axios.get(`http://localhost:8080/approval/approver/${typeId}/${approvalStatus}/${approverId}`);

      if (response.status !== 200) {
        throw new Error("response is not ok");
      }

      state.reqApprovalList = response.data.approvalEmpResultList;
      state.reqApprovalList.type = typeId;

    } catch (error) {
      console.error('Fetch error: ' + error.message);
    }
  }

  onMounted(async() => {
    // 버튼 클릭에 따라 다르게 실행
    // 0: 전체, 1: 승인, 2: 반려, 3: 대기
    await fetchApprovalList(1,0,  employeeId);
    await fetchReqApprovalList(1, 5, employeeId);
  })
</script>

<template>
  <div>결재 목록</div>
  <div v-if="admin === 1">
  <!-- 관리자 -->
    <div>
      <b-card no-body>
        <b-tabs card>
          <b-tab title="출장" @click="fetchApprovalList(1, 0, employeeId)" active>
            <b-card-text><ApprovalList :approvalList="state.approvalList"/></b-card-text>
          </b-tab>
          <b-tab title="시간 외 근무" @click="fetchApprovalList(2, 0, employeeId)">
            <ApprovalList :approvalList="state.approvalList"/>
          </b-tab>
          <b-tab title="단축 근무" @click="fetchApprovalList(3, 0, employeeId)">
          <ApprovalList :approvalList="state.approvalList"/>
          </b-tab>
          <b-tab title="휴가" @click="fetchApprovalList(4, 0, employeeId)">
            <ApprovalList :approvalList="state.approvalList"/>
          </b-tab>
        </b-tabs>
      </b-card>
    </div>
  </div>
  <div v-else>
    <b-card title="내가 작성한 문서">
      <br>
      <ApprovalList :approvalList="state.approvalList" :short="1"/>
    </b-card>
    <br>
    <b-card title="결재 필요 문서">
      <br>
      <ApprovalList :approvalList="state.reqApprovalList" :short="1"/>
    </b-card>
  </div>
</template>

<style scoped>
</style>