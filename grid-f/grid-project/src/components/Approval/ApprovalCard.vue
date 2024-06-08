<template>
  <div style="margin-top: 45px; margin-left: 10px;">
    <div class="fs-6 text-muted mb-4"><strong>시작 일자 :  &nbsp;</strong>{{ props.approval['startTime'] }}</div>
    <div class="fs-6 text-muted mb-4"><strong>종료 일자 :  &nbsp;</strong>{{ props.approval['endTime'] }}</div>
    <div v-if="props.approval['destination'] !== null" class="fs-6 text-muted mb-4"><strong>출장지 :  &nbsp;</strong>{{ props.approval['destination'] }}</div>
    <div v-if="props.approval['vacationType'] !== null" class="fs-6 text-muted mb-4"><strong>휴가 유형 :  &nbsp;</strong>{{ props.approval['vacationType'] }}</div>
    <div class="fs-6 text-muted mb-4"><strong>내용 :  &nbsp;</strong>{{ props.approval['content'] }}</div>
  </div>
</template>

<script setup>
  const props = defineProps({
    approval: Object
  })
</script>

<style scoped>

</style>