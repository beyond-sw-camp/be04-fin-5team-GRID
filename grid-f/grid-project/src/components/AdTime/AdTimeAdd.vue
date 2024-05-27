<template>
  <div class="adTimeAddController">
    {{ today }}
    <div>
      <div v-if="adTime.startTime">{{ adTime.startTime }}</div>
      <div v-else>00:00</div>
      <button @click="addArrivalTime()">출근</button>
    </div>
    <div>
      <div v-if="adTime.endTime">{{ adTime.endTime }}</div>
      <div v-else>00:00</div>
      <button @click="addDepartureTime()">퇴근</button>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';

const router = useRouter();

const adTime = ref({
  id: '',
  startTime: '',
  endTime: '',
  employeeId: '',
});

// 현재 시간
function getCurrentDateTimeString() {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

const today = ref();

// 오늘 출퇴근 시간 조회
const fetchAdTime = async () => {
  try {
    const currentTime = getCurrentDateTimeString();
    const currentDate = currentTime.slice(0, 10);

    today.value = currentDate;

    console.log(currentDate);
    const response = await axios.get(`http://localhost:8080/ad-time/${currentDate}/2`);
    console.log(response.data);
    const data = response.data.adTimeDTO;
    console.log(response.data.adTimeDTO);
    adTime.value = {
      id: data.id,
      startTime: data.startTime ? data.startTime.slice(11, 16) : "00:00",
      endTime: data.endTime ? data.endTime.slice(11, 16) : "00:00",
      employeeId: data.employeeId,
    };

    console.log(adTime.value);
  } catch (error) {
    console.error('에러 발생:', error);
  }
}

onMounted(() => {
  fetchAdTime();
});


// 출근 시간 추가 요청
const addArrivalTime = async () => {
  const currentTime = getCurrentDateTimeString();
  console.log(currentTime);
  await axios.post(
      'http://localhost:8080/ad-time/arrival-time',
      {
        id: null,
        startTime: currentTime,
        endTime: null,
        employeeId: 2   // 로그인한 사람
      })
      .then(response => {
        // 성공적으로 처리된 경우에 실행할 로직
        window.location.reload();
        console.log('출근 시간이 기록되었습니다.');
      })
      .catch(error => {
        // 오류 발생 시 처리할 로직
        alert('출근 시간이 존재합니다')
        console.error('에러 발생:', error);
        // console.error('출근 시간을 기록하는 중 오류가 발생했습니다:', error);
      });
}

// 퇴근 시간 추가 요청
const addDepartureTime = async () => {
  const currentTime = getCurrentDateTimeString();
  console.log(currentTime);
  await axios.put(
      'http://localhost:8080/ad-time/departure-time',
      {
        id: null,
        startTime: null,
        endTime: currentTime,
        employeeId: 2   // 로그인한 사람
      })
      .then(response => {
        // 성공적으로 처리된 경우에 실행할 로직
        window.location.reload();
        console.log('퇴근 시간이 기록되었습니다.');
      })
      .catch(error => {
        alert('퇴근 시간이 존재합니다');
        console.error('에러 발생:', error);
      });

}


</script>

<style scoped>

</style>