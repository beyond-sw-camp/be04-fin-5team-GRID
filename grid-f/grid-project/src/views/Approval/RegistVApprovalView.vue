<script setup>
import {onMounted, reactive, ref, watch} from "vue";
import {useRoute} from "vue-router";
import axios from "axios";
import router from '@/router/router';

const route = useRoute();

const userId = ref();

const allInfo = ref([]);
const vacationNum = ref(0);

const state = reactive({
  vacationType: [],
  isEndDateDisabled: false,
  isTimeDisabled: false
});

const postData = reactive({
  s_date: "",
  s_time: "00:00:00",
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

    if(id === 5 || id === 6) {
      const vacationInfo = allInfo.value.find(info => info.typeId === 1);
      vacationNum.value = vacationInfo ? vacationInfo.vacationNum : 0;
    } else {
      const vacationInfo = allInfo.value.find(info => info.typeId === id);
      vacationNum.value = vacationInfo ? vacationInfo.vacationNum : 0;
    }


  } catch (error) {
    console.error("Error:", error);
  }
};

const updateDateTime = () => {
  if (postData.startTime === '') {
    postData.startTime = `${postData.s_date} 00:00:00`;
  } else {
    postData.startTime = `${postData.s_date} ${postData.s_time}:00`;
  }

  if (postData.endTime === '') {
    postData.endTime = `${postData.e_date} 00:00:00`
  } else {
    postData.endTime = `${postData.e_date} ${postData.e_time}:00`
  }
}

const fetchVacationType = async() => {
  try {
    const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/vacation/type`);

    if (response.status !== 200) {
      throw new Error("response is not ok");
    }

    let typeList = response.data.result;

    for (const type of typeList) {
      state.vacationType.push({value: parseInt(type.id), text: type.typeName});
    }

  } catch (error) {
    console.error('Fetch error: ' + error.message);
  }
};

function calculateDaysBetweenDates(startDate, endDate) {
const start = new Date(startDate);
const end = new Date(endDate);

// 시작 날짜와 종료 날짜의 차이를 밀리초 단위로 계산
const differenceInTime = end.getTime() - start.getTime();

// 밀리초를 일(day)로 변환
const differenceInDays = differenceInTime / (1000 * 3600 * 24);

return differenceInDays;
}

const registApproval = async () => {
  postData.requesterId = userId.value;

  if(postData.infoId === 5 || postData.infoId === 6) {
    postData.e_date = postData.s_date;
    postData.e_time = postData.s_time;
  }

  await getUserVacationInfo(postData.infoId);

  const daysBetween = calculateDaysBetweenDates(postData.s_date, postData.e_date);
  console.log(daysBetween)

  try {
    const confirmed = window.confirm('휴가를 사용하시겠습니까?');

    if(confirmed) {
      console.log(postData.startTime, postData.endTime);
      if (postData.content !== '' && postData.startTime !== ' 00:00:00' && postData.endTime !== ' 00:00:00' && postData.infoId !== 0) {
        if (vacationNum.value >= daysBetween) {
          const response = await axios.post(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/approval/vacation`, postData, {
            headers: {
              'Content-Type': "application/json"
            }
          })
          if (response.status !== 201) {
            throw new Error("response is not ok");
          } else {
            alert('결재가 제출되었습니다.')
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
}

watch(
    () => postData.infoId,
    (newInfoId) => {
      const selectedType = state.vacationType.find(type => type.value === newInfoId);

      state.isEndDateDisabled = selectedType && (selectedType.text === '반차' || selectedType.text === '반반차');
      state.isTimeDisabled = selectedType && [1, 2, 3, 4].includes(selectedType.value);
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

<template>
  <div class="vAll">
    <div class="vHeader">
  <nav style="--bs-breadcrumb-divider: '>'; margin-top: -35px; margin-bottom: -7px;" aria-label="breadcrumb">
  <ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="http://localhost:5173/regist/main" style="text-decoration: none; color: grey; font-size: 17px;"><i class="bi bi-pencil-square"></i>&nbsp; 결재 작성</a></li>
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
          <b-form-input type="time" v-model="postData.s_time" :disabled="state.isTimeDisabled"></b-form-input>
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
              max-rows="8"
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

<style scoped>
.vAll {
display: grid;
grid-template-rows: 18% 60% 5% 17%;
grid-template-columns: 10% 80% 10%;
height: 100%;
}

.vHeader {
grid-column-start: 2;
align-content: center;
margin-top: 2%;
}

.vHeader h1 {
margin-left: 0.5%;
margin: 0;
font-size: 25px;
font-weight: 600;
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
