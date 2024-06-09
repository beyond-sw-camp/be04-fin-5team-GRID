<template>
  <div style="margin-top: 45px; margin-left: 10px;">
    <div class="fs-6 text-muted mb-4"><strong>시작 일자 :  &nbsp;</strong>{{ props.approval['startTime'] }}</div>
    <div class="fs-6 text-muted mb-4"><strong>종료 일자 :  &nbsp;</strong>{{ props.approval['endTime'] }}</div>
    <div v-if="props.approval['destination'] !== null" class="fs-6 text-muted mb-4"><strong>출장지 :  &nbsp;</strong>{{ props.approval['destination'] }}</div>
    <div v-if="props.approval['vacationType'] !== null" class="fs-6 text-muted mb-4"><strong>휴가 유형 :  &nbsp;</strong>{{ props.approval['vacationType'] }}</div>
    <div class="fs-6 text-muted mb-4"><strong>내용 :  &nbsp;</strong>{{ props.approval['content'] }}</div>
    <div v-if="props.typeId === '3'" class="fs-6 text-muted mb-4" @click="printRW"> <strong>증명 서류 :  &nbsp;</strong><button>확인</button></div>
  </div>
</template>

<script setup>
  const props = defineProps({
    approval: Object,
    typeId: 0
  })

  const printRW = async () => {
    const confirmed = window.confirm('pdf 파일을 다운로드 하시겠습니까?');

    console.log(props.approval['id'])
    if(confirmed) {
      var url = `/api/approval/downloadRW/${props.approval['id']}`;

      // 새 창 열기
      window.open(url, "_blank");
    }
  }

</script>

<style scoped>

</style>