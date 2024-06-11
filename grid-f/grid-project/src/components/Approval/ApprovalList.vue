<template>
  <div v-if="props.approvalList.length === 0">
    문서가 없습니다.
  </div>
  <div v-else>
    <template v-if="props.approvalList.type === 'bt'">
      <b-table id="table" :fields="approvalFields" :items="props.approvalList" hover small :per-page="10" :current-page="currentPage">
        <template #cell(index)="data">
          {{ data.index + 1 }}
        </template>
        <template #cell(startTimeendTIme)="data">
          <span>{{ data.item.startTime.substring(0, 10) }} ~ {{ data.item.endTime.substring(0, 10) }}</span>
        </template>
        <template #cell(details)="data">
          <b-badge class="detail" @click="approvalDetail(1, data.item.id)">
            &#x2139;
          </b-badge>
        </template>
        <template #cell()="data">
          <span>{{ data.value }}</span>
        </template>
      </b-table>
    </template>

    <template v-else-if="props.approvalList.type === 'o'">
      <b-table id="table" :fields="approvalFields" :items="props.approvalList" hover small
               :per-page=10 :current-page="currentPage">
        <template #cell(index)="data">
          {{ data.index + 1 }}
        </template>
        <template #cell(startTimeendTIme)="data">
          <span>{{ data.item.startTime }} ~ {{ data.item.endTime }}</span>
        </template>
        <template #cell(details)="data">
          <b-badge class="detail" @click="approvalDetail(2, data.item.id)">
            &#x2139;
          </b-badge>
        </template>
        <template #cell()="data">
          <span>{{ data.value }}</span>
        </template>
      </b-table>
    </template>
    <template v-else-if="props.approvalList.type === 'rw'">
      <b-table id="table" :fields="approvalFields" :items="props.approvalList" hover small
               :per-page=10 :current-page="currentPage">
        <template #cell(index)="data">
          {{ data.index + 1 }}
        </template>
        <template #cell(startTimeendTIme)="data">
          <span>{{ data.item.startTime.substring(0, 10) }} ~ {{ data.item.endTime.substring(0, 10)}}</span>
        </template>
        <template #cell(details)="data">
          <b-badge class="detail" @click="approvalDetail(3, data.item.id)">
            &#x2139;
          </b-badge>
        </template>
        <template #cell()="data">
          <span>{{ data.value }}</span>
        </template>
      </b-table>
    </template>

    <template v-else-if="props.approvalList.type === 0">
      <b-table id="table" :fields="fields" :items="props.approvalList" hover small
               :per-page=10 :current-page="currentPage" :sort-by.sync='employeeNumber' :sort-desc.sync="false">
        <template #cell(index)="data">
          {{ data.index + 1 }}
        </template>
        <template #cell(writeTime)="data">
          <span>{{ data.value.substring(0, 10) }}</span>
        </template>
        <template #cell(approvalStatuscancelYncancelDocId)="data">
          <b-badge variant="dark" v-if="data.item.cancelYn === 'Y'">취소</b-badge>
          <b-badge variant="success" v-else-if="data.item.approvalStatus === 'A'">승인</b-badge>
          <b-badge variant="danger" v-else-if="data.item.approvalStatus === 'D'">반려</b-badge>
          <b-badge variant="warning" v-else-if="data.item.approvalStatus === 'V'">열람</b-badge>
          <b-badge variant="secondary" v-else-if="data.item.approvalStatus === 'N'">미열람</b-badge>
          <span>&nbsp;</span><b-badge variant="primary" v-if="data.item.cancelDocId > 0">취소결재</b-badge>
        </template>
        <template #cell()="data">
          <span>{{ data.value }}</span>
        </template>
      </b-table>
    </template>

    <template v-else>
      <b-table id="table" :fields="fields" :items="props.approvalList" hover small
               :per-page=10 :current-page="currentPage" :sort-by.sync='employeeNumber' :sort-desc.sync="false"
               @row-clicked="approvalDetail(props.approvalList.type, $event['id'], $event['employeeId'], $event['approvalStatus'])">
      <template #cell(index)="data">
          {{ data.index + 1 }}
        </template>
        <template #cell(content)="data">
          <span class="detail">{{ data.value }}</span>
        </template>
        <template #cell(writeTime)="data">
          <span>{{ data.value.substring(0, 10) }}</span>
        </template>
        <template #cell(approvalStatuscancelYncancelDocId)="data">
          <b-badge variant="dark" v-if="data.item.cancelYn === 'Y'">취소</b-badge>
          <b-badge variant="success" v-else-if="data.item.approvalStatus === 'A'">승인</b-badge>
          <b-badge variant="danger" v-else-if="data.item.approvalStatus === 'D'">반려</b-badge>
          <b-badge variant="warning" v-else-if="data.item.approvalStatus === 'V'">열람</b-badge>
          <b-badge variant="secondary" v-else-if="data.item.approvalStatus === 'N'">미열람</b-badge>
          <span>&nbsp;</span><b-badge variant="primary" v-if="data.item.cancelDocId > 0">취소결재</b-badge>
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

<script setup>
  import {useRouter} from "vue-router";
  import {computed, onMounted, reactive, ref} from "vue";
  import axios from "axios";

  const props = defineProps({
    approvalList: Array,
    short: 0
  })

  const state = reactive({
    type: props.approvalList.type,
    short: props.short
  })

  let pathList = [
    'bt', 'overtime', 'rw', 'vacation'
  ]

  const fields = [
    { key: 'index', label: '번호', sortable: false },
    { key: 'content', label: '내용', sortable: false },
    { key: 'employeeNumber', label: '사번', sortable: true },
    { key: 'employeeName', label: '작성자', sortable: false },
    { key: 'writeTime', label: '작성일자', sortable: true },
    { key: 'approvalStatuscancelYncancelDocId', label: '결재 상태', sortable: false }
  ]

  const approvalFields = [
    { key: 'index', label: '번호', sortable: false },
    { key: 'content', label: '내용', sortable: false },
    { key: 'employeeNumber', label: '사번', sortable: true },
    { key: 'employeeName', label: '작성자', sortable: false },
    { key: 'startTimeendTIme', label: '기간', sortable: false },
    { key: 'details', label: '상세보기', sortable: false }
  ]

  const router = useRouter();

  const userRole = ref('');
  const userId = ref();

  const isLoading = ref(true);

  function print(item) {
    console.log(item)
  }

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

  const registStatus = async (status, approvalId) => {

    try {
      const response = await axios.put("/api/approval/" + pathList[props.approvalList.type - 1] + `-status/${approvalId}`, {
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

  const approvalDetail = (typeId, approvalId, employeeId, approvalStatus) => {
    if (approvalStatus === 'N' && userRole.value !== 'ROLE_ADMIN' && userId.value !== employeeId) {
      registStatus('V', approvalId);
    }
    router.push(`/approval/detail/${typeId}/${approvalId}`);    // 추후에 모달로 변경
  }

  onMounted(async () => {
    const token = localStorage.getItem('access');
    if (token) {
      const decodedToken = parseJwt(token);

      userRole.value = decodedToken.auth || '';
      userId.value = decodedToken.id || '';
    }

    isLoading.value = false;
  })
</script>

<style scoped>
.detail {
  cursor: pointer;
}
</style>