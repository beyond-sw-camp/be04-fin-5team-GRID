<script setup>
  import {useRouter} from "vue-router";

  const props = defineProps({
    approvalList: Array
  })

  const router = useRouter();

  const approvalDetail = (typeId, approvalId) => {
    router.push(`/approval/detail/${typeId}/${approvalId}`);
  }
</script>

<template>
  <div v-if="props.approvalList.length === 0">
    Loading
  </div>
  <table v-if="props.approvalList.type === 'bt'">
    <thead>
    <tr>
      <th>번호</th>
      <th>내용</th>
      <th>사번</th>
      <th>작성자</th>
      <th>출장지</th>
      <th>출장 기간</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="(approval, index) in props.approvalList" :key="approval.id">
      <td>{{ index + 1 }}</td>
      <td>{{ approval.content }}</td>
      <td>{{ approval.employeeNumber }}</td>
      <td>{{ approval.employeeName }}</td>
      <td>{{ approval.destination }}</td>
      <td>{{ approval.startTime.substring(0, 10) }} ~ {{ approval.endTime.substring(0, 10) }}</td>
    </tr>
    </tbody>
  </table>

  <table v-else-if="props.approvalList.type === 'o'">
    <thead>
    <tr>
      <th>번호</th>
      <th>내용</th>
      <th>사번</th>
      <th>작성자</th>
      <th>근무 기간</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="(approval, index) in props.approvalList" :key="approval.id">
      <td>{{ index + 1 }}</td>
      <td>{{ approval.content }}</td>
      <td>{{ approval.employeeNumber }}</td>
      <td>{{ approval.employeeName }}</td>
      <td>{{ approval.startTime.substring(0, 16) }} ~ {{ approval.endTime.substring(0, 16) }}</td>
    </tr>
    </tbody>
  </table>

  <table v-else-if="props.approvalList.type === 'rw'">
    <thead>
    <tr>
      <th>번호</th>
      <th>내용</th>
      <th>사번</th>
      <th>작성자</th>
      <th>단축 근무 기간</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="(approval, index) in props.approvalList" :key="approval.id">
      <td>{{ index + 1 }}</td>
      <td>{{ approval.content }}</td>
      <td>{{ approval.employeeNumber }}</td>
      <td>{{ approval.employeeName }}</td>
      <td>{{ approval.startTime.substring(0, 16) }} ~ {{ approval.endTime.substring(0, 16) }}</td>
    </tr>
    </tbody>
  </table>

  <table v-else-if="props.approvalList.type === 'v'">
    <thead>
    <tr>
      <th>번호</th>
      <th>내용</th>
      <th>사번</th>
      <th>작성자</th>
      <th>휴가 유형</th>
      <th>휴가 기간</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="(approval, index) in props.approvalList" :key="approval.id">
      <td>{{ index + 1 }}</td>
      <td>{{ approval.content }}</td>
      <td>{{ approval.employeeNumber }}</td>
      <td>{{ approval.employeeName }}</td>
      <td>{{ approval.vacationType }}</td>
      <td>{{ approval.startTime.substring(0, 16) }} ~ {{ approval.endTime.substring(0, 16) }}</td>
    </tr>
    </tbody>
  </table>

  <table v-else>
    <thead>
      <tr>
        <th>번호</th>
        <th>내용</th>
        <th>사번</th>
        <th>작성자</th>
        <th>작성일자</th>
        <th>결재상태</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(approval, index) in props.approvalList" :key="approval.id" @click="approvalDetail(props.approvalList.type, approval.id)">
        <td>{{ index + 1 }}</td>
        <td>{{ approval.content }}</td>
        <td>{{ approval.employeeNumber }}</td>
        <td>{{ approval.employeeName }}</td>
        <td>{{ approval.writeTime.substring(0, 10) }}</td>
        <td>{{ approval.approvalStatus }}</td>
      </tr>
    </tbody>
  </table>
</template>

<style scoped>

</style>