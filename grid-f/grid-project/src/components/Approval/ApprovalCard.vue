<script setup>
  const props = defineProps({
    approval: Object
  })
</script>

<template>
  <div>
    <div>시작 일자: {{ props.approval['startTime'] }}</div>
    <div>종료 일자: {{ props.approval['endTime'] }}</div>
    <div>내용: {{props.approval['content']}}</div>
    <div v-if="props.approval['destination'] !== null">출장지: {{ props.approval['destination'] }}</div>
    <div v-if="props.approval['vacationType'] !== null">휴가 유형: {{ props.approval['vacationType'] }}</div>
  </div>
</template>

<style scoped>

</style>