<script setup>
  import {useRouter} from "vue-router";
  import {onMounted, reactive} from "vue";

  const props = defineProps({
    approvalList: Array,
    short: 0
  })

  const state = reactive({
    type: props.approvalList.type,
    short: props.short
  })

  const fields = [
    { key: 'index', label: '번호', sortable: false },
    { key: 'content', label: '내용', sortable: false },
    { key: 'employeeNumber', label: '사번', sortable: true },
    { key: 'employeeName', label: '작성자', sortable: false },
    { key: 'writeTime', label: '작성일자', sortable: true },
    { key: 'approvalStatus', label: '결재 상태', sortable: false }
  ]

  const btFields = [
    { key: 'index', label: '번호', sortable: false },
    { key: 'content', label: '내용', sortable: false },
    { key: 'employeeNumber', label: '사번', sortable: true },
    { key: 'employeeName', label: '작성자', sortable: false },
    { key: 'destination', label: '출장지', sortable: true },
    { key: 'startTimeendTIme', label: '출장 기간', sortable: false }
  ]

  const oFields = [
    { key: 'index', label: '번호', sortable: false },
    { key: 'content', label: '내용', sortable: false },
    { key: 'employeeNumber', label: '사번', sortable: true },
    { key: 'employeeName', label: '작성자', sortable: false },
    { key: 'startTimeendTIme', label: '근무 기간', sortable: false }
  ]

  const rwFields = [
    { key: 'index', label: '번호', sortable: false },
    { key: 'content', label: '내용', sortable: false },
    { key: 'employeeNumber', label: '사번', sortable: true },
    { key: 'employeeName', label: '작성자', sortable: false },
    { key: 'startTimeendTIme', label: '단축 기간', sortable: false }
  ]

  const router = useRouter();

  const approvalDetail = (typeId, approvalId) => {
    router.push(`/approval/detail/${typeId}/${approvalId}`);
  }
</script>

<template>
  <div v-if="props.approvalList.length === 0">
    문서가 없습니다.
  </div>
  <div v-else>
    <template v-if="props.approvalList.type === 'bt'">
      <b-table id="table" :fields="btFields" :items="props.approvalList" hover small
               :per-page=10 :current-page="currentPage">
        <template #cell(index)="data">
          {{ data.index + 1 }}
        </template>
        <template #cell(content)="data">
<!--          <span @click="approvalDetail(1, data.item.id)">{{ data.value }}</span>-->
          <span>{{ data.value }}</span>
        </template>
        <template #cell(startTimeendTIme)="data">
          <span>{{ data.item.startTime.substring(0, 10) }} ~ {{ data.item.endTime.substring(0, 10)}}</span>
        </template>
        <template #cell()="data">
          <span>{{ data.value }}</span>
        </template>
      </b-table>
    </template>

    <template v-else-if="props.approvalList.type === 'o'">
      <b-table id="table" :fields="oFields" :items="props.approvalList" hover small
               :per-page=10 :current-page="currentPage">
        <template #cell(index)="data">
          {{ data.index + 1 }}
        </template>
        <template #cell(content)="data">
          <span>{{ data.value }}</span>
        </template>
        <template #cell(startTimeendTIme)="data">
          <span>{{ data.item.startTime }} ~ {{ data.item.endTime }}</span>
        </template>
        <template #cell()="data">
          <span>{{ data.value }}</span>
        </template>
      </b-table>
    </template>

    <template v-else-if="props.approvalList.type === 'rw'">
      <b-table id="table" :fields="rwFields" :items="props.approvalList" hover small
               :per-page=10 :current-page="currentPage">
        <template #cell(index)="data">
          {{ data.index + 1 }}
        </template>
        <template #cell(content)="data">
          <span>{{ data.value }}</span>
        </template>
        <template #cell(startTimeendTIme)="data">
          <span>{{ data.item.startTime.substring(0, 10) }} ~ {{ data.item.endTime.substring(0, 10)}}</span>
        </template>
        <template #cell()="data">
          <span>{{ data.value }}</span>
        </template>
      </b-table>
    </template>

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

    <template v-else>
      <b-table id="table" :fields="fields" :items="props.approvalList" hover small
               :per-page=10 :current-page="currentPage" :sort-by.sync='employeeNumber' :sort-desc.sync="false">
        <template #cell(index)="data">
          {{ data.index + 1 }}
        </template>
        <template #cell(content)="data">
          <span @click="approvalDetail(props.approvalList.type, data.item.id)">{{ data.value }}</span>
        </template>
        <template #cell(writeTime)="data">
          <span>{{ data.value.substring(0, 10) }}</span>
        </template>
        <template #cell(approvalStatus)="data">
          <b-badge variant="success" v-if="data.value === 'A'">승인</b-badge>
          <b-badge variant="danger" v-if="data.value === 'D'">반려</b-badge>
          <b-badge variant="warning" v-if="data.value === 'N'">대기</b-badge>
        </template>
        <template #cell()="data">
          <span>{{ data.value }}</span>
        </template>
      </b-table>
      <br>
    </template>

    <template v-if="state.short !== 1 && props.approvalList.length > 10">
      <b-pagination align="center"
                    v-model="currentPage"
                    :total-rows=props.approvalList.length
                    :per-page="10"
                    aria-controls="table"
      ></b-pagination>
    </template>
  </div>
</template>

<style scoped>

</style>