<template>
  <div class="vAll">
    <div class="vHeader">
      <nav style="--bs-breadcrumb-divider: '>'; margin-top: -35px; margin-bottom: -7px;" aria-label="breadcrumb">
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="http://www.gridhr.site/regist/main" style="text-decoration: none; color: grey; font-size: 17px;"><i class="bi bi-pencil-square"></i>&nbsp; 결재 작성</a></li>
          <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i class="bi bi-brightness-high"></i>&nbsp; 휴가</span></li>
        </ol>
      </nav>
      <div><h1 class="fw-bolder"><i class="bi bi-brightness-high"></i>&nbsp; 휴가 신청</h1></div>
    </div>
    <div class="vContent">
      <b-card class="mt-3" bg-variant="light">
        <b-form-group
            label-cols-lg="3"
            label="휴가 결재"
            label-size="lg"
            label-class="font-weight-bold pt-0"
            class="mb-0"
        >
          <b-form-group
              label="휴가 종류:"
              label-cols-sm="3"
              label-align-sm="right"
          >
            <b-form-select v-model="postData.infoId" :options="state.vacationType"></b-form-select>
          </b-form-group>
          <b-form-group
              label="시작 일자:"
              label-cols-sm="3"
              label-align-sm="right"
          >
          <b-form-input type="date" v-model="postData.s_date"></b-form-input>
          <b-form-select v-model="postData.s_time_period" :disabled="state.isTimePeriodDisabled" :options="state.timePeriods"></b-form-select>
          <b-form-input type="time" v-model="postData.s_time" :disabled="state.isTimeDisabled || state.isTimePeriodDisabled"></b-form-input>
          </b-form-group>

          <b-form-group
              label="종료 일자:"
              label-cols-sm="3"
              label-align-sm="right"
          >
            <b-form-input type="date" v-model="postData.e_date" :min="postData.s_date" :disabled="state.isEndDateDisabled"></b-form-input>
            <b-form-input type="time" v-model="postData.e_time" :disabled="state.isEndDateDisabled || state.isTimeDisabled"></b-form-input>
          </b-form-group>

          <b-form-group
              label="내용:"
              label-cols-sm="3"
              label-align-sm="right"
          >
            <b-form-textarea
                id="textarea-auto-height"
                v-model="postData.content"
                placeholder="내용을 입력하세요."
                rows="3"
                style="resize: none;"
            ></b-form-textarea>
          </b-form-group>
        </b-form-group>
      </b-card>
    </div>
    <div class="btnArea">
      <b-button block variant="primary" @click="registApproval()">제출</b-button>
    </div>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref, watch} from "vue";
import {useRoute} from "vue-router";
import axios from "axios";
import router from '@/router/router';

const route = useRoute();

const userId = ref();

const allInfo = ref([]);
const vacationNum = ref(0);
const userVacationEndTime = ref('');
 
const state = reactive({
  vacationType: [],
  isEndDateDisabled: false,
  isTimeDisabled: false,
  isTimePeriodDisabled: true,
  timePeriods: [
    { value: 'AM', text: '오전' },
    { value: 'PM', text: '오후' }
  ]
});

const postData = reactive({
  s_date: "",
  s_time: "00:00:00",
  s_time_period: "",
  e_date: "",
  e_time: "00:00:00",
  infoId: 0,
  content: "",
  requesterId: 0
});

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

const getUserVacationInfo = async (id) => {
  try {
    const response = await axios.get(`/api/vacation/info/${userId.value}`);

    allInfo.value = response.data.result;

    let vacationInfo;
    if(id === 5 || id === 6) {
      vacationInfo = allInfo.value.find(info => info.typeId === 1);
    } else {
      vacationInfo = allInfo.value.find(info => info.typeId === id);
    }

    vacationNum.value = vacationInfo ? vacationInfo.vacationNum : 0;
    userVacationEndTime.endTime = vacationInfo ? vacationInfo.endTime : '00:00:00';

  } catch (error) {
    console.error("Error:", error);
  }
};

const updateDateTime = () => {
  if (postData.s_time === '') {
    postData.startTime = `${postData.s_date} 00:00:00`;
  } else {
    postData.startTime = `${postData.s_date} ${postData.s_time}`;
  }

  if (postData.e_time === '') {
    postData.endTime = `${postData.e_date} 00:00:00`
  } else {
    postData.endTime = `${postData.e_date} ${postData.e_time}`
  }
}

const fetchVacationType = async() => {
  try {
    const response = await axios.get(`/api/vacation/type`);

    if (response.status !== 200) {
      throw new Error("response is not ok");
    }

    let typeList = response.data.result;

    for (const type of typeList) {
      state.vacationType.push({value: parseInt(type.id), text: type.typeName, timeCheck: type.timeCheck, vacationTime:type.vacationTime});
    }

  } catch (error) {
    console.error('Fetch error: ' + error.message);
  }
};

const calculateBusinessDays = (startDate, endDate) => {
  let count = 0;
  const start = new Date(startDate);
  const end = new Date(endDate);
  
  // 시작 날짜와 종료 날짜가 같은 경우
  if (start.toDateString() === end.toDateString()) {
    const dayOfWeek = start.getDay();
    if (dayOfWeek !== 0 && dayOfWeek !== 6) { // 주말이 아닌 경우
      return 1;
    } else {
      return 0; // 주말인 경우
    }
  }

  const curDate = new Date(startDate);

  while (curDate <= end) {
    const dayOfWeek = curDate.getDay();
    if (dayOfWeek !== 0 && dayOfWeek !== 6) { // 0 = Sunday, 6 = Saturday
      count++;
    }
    curDate.setDate(curDate.getDate() + 1);
  }
  
  return count;
}


const registApproval = async () => {
  postData.requesterId = userId.value;

  const selectedType = state.vacationType.find(type => type.value === postData.infoId);
  const timeCheck = selectedType ? selectedType.timeCheck : null;

  await getUserVacationInfo(postData.infoId);

  if (vacationNum.value <= 0) {
    alert('해당 휴가를 보유하고 있지 않습니다.');
    return;
  }

  // 휴가 사용 종료 날짜가 지난 경우 신청을 막음
  if (userVacationEndTime.endTime < postData.e_date || userVacationEndTime.endTime < postData.s_date) {
    alert('휴가 사용기한 안에서만 신청 가능합니다.');
    return;
  }

  // 주말을 제외한 일수 계산
  const businessDaysBetween = calculateBusinessDays(postData.s_date, postData.e_date);
  
  if (selectedType.vacationTime === 4) {
    if (postData.s_time_period === 'AM') {
      postData.s_time = '09:00:00';
      postData.e_time = '13:00:00';
    } else if (postData.s_time_period === 'PM') {
      postData.s_time = '14:00:00';
      postData.e_time = '18:00:00';
    }
    postData.e_date = postData.s_date; // 시작일과 종료일이 같도록 설정
  }

  if (selectedType.vacationTime === 2) {
    if (postData.s_time_period === 'AM') {
      postData.s_time = '09:00:00';
      postData.e_time = '11:00:00';
    } else if (postData.s_time_period === 'PM') {
      postData.s_time = '16:00:00';
      postData.e_time = '18:00:00';
    }
    postData.e_date = postData.s_date; // 시작일과 종료일이 같도록 설정
  }

  updateDateTime(); // 시간 및 날짜 정보 업데이트

  try {
    const confirmed = window.confirm('휴가를 사용하시겠습니까?');

    if (confirmed) {
      if (postData.content !== '' && postData.startTime !== ' 00:00:00' && postData.endTime !== ' 00:00:00' && postData.infoId !== 0) {
        if (vacationNum.value >= businessDaysBetween) {
          const response = await axios.post(`/api/approval/vacation`, postData, {
            headers: {
              'Content-Type': "application/json"
            }
          });
          if (response.status !== 201) {
            throw new Error("response is not ok");
          } else {
            alert('결재가 제출되었습니다.');
            router.push(response.data.href);
          }
        } else {
          alert('휴가가 부족합니다.');
          return;
        }
      } else {
        alert('모든 필드를 입력해주세요.');
      }
    }
  } catch (error) {
    console.error('Fail to post: ', error.message);
  }
};


watch(
  () => postData.infoId,
  (newInfoId) => {
    const selectedType = state.vacationType.find(type => type.value === newInfoId);

    state.isEndDateDisabled = selectedType && (selectedType.timeCheck === 'Y');
    state.isTimeDisabled = selectedType && (selectedType.timeCheck === 'N');
    state.isTimePeriodDisabled = selectedType && (selectedType.timeCheck !== 'Y');

    // 휴가 종류가 변경될 때마다 관련 필드 초기화
    postData.s_date = "";
    postData.s_time = "00:00:00";
    postData.s_time_period = "";
    postData.e_date = "";
    postData.e_time = "00:00:00";
    postData.content = "";

    if (selectedType.timeCheck === 'Y') {
      state.isTimeDisabled = true;
    }
  }
);


watch(
    () => [postData.s_date, postData.s_time],
    () => {
      updateDateTime();
      postData.e_date = '';
      postData.e_time = '';
    }
);

watch(
    () => [postData.e_date, postData.e_time],
    updateDateTime
);

onMounted(async() => {
  const token = localStorage.getItem('access');

  if (token) {
    const decodedToken = parseJwt(token);

    userId.value = decodedToken.id || '';
  }

  await fetchVacationType();
})
</script>

<style scoped>
.vAll {
display: grid;
grid-template-rows: 18% 55% 5% 17%;
grid-template-columns: 10% 80% 10%;
height: 100%;
}

.vHeader {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
  margin-left: -0.5%;
  color: #000000;
}

.vHeader h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.vContent {
grid-column-start: 2;
grid-row-start: 2;
}

.btnArea {
grid-column-start: 2;
grid-row-start: 3;
align-content: center;
display: grid;
grid-template-columns: 45% 10% 45%;
}

.btn {
grid-column-start: 2;
width: 100%;
background-color: #088A85;
color: white;
padding: 5px 5px;
border: none;
border-radius: 4px;
cursor: pointer;
font-size: 12px;
font-style: bold;
}
</style>
