<template>
  <div class="registOAAll">
    <div class="oaHeader">
      <nav style="--bs-breadcrumb-divider: '>'; margin-top: -35px; margin-bottom: -7px;" aria-label="breadcrumb">
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="http://www.gridhr.site/regist/main"
                                         style="text-decoration: none; color: grey; font-size: 17px;"><i
              class="bi bi-pencil-square"></i>&nbsp; 결재 작성</a></li>
          <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i class="bi bi-clock"></i>&nbsp; 시간 외 근무</span>
          </li>
        </ol>
      </nav>
      <div><h1 class="fw-bolder"><i class="bi bi-clock"></i>&nbsp; 시간 외 근무 신청</h1></div>
    </div>
    <div class="oaContent">
      <b-card class="mt-3" bg-variant="light">
        <b-form-group
            label-cols-lg="3"
            label="시간 외 근무 결재"
            label-size="lg"
            label-class="font-weight-bold pt-0"
            class="mb-0"
        >
          <b-form-group
              label="시작 일자:"
              label-for="nested-street"
              label-cols-sm="3"
              label-align-sm="right"
          >
            <b-form-input type="date" v-model="postData.s_date"></b-form-input>
            <b-form-input type="time" v-model="postData.s_time"></b-form-input>
          </b-form-group>

          <b-form-group
              label="종료 일자:"
              label-for="nested-city"
              label-cols-sm="3"
              label-align-sm="right"
          >
            <b-form-input type="date" v-model="postData.e_date" :min="postData.s_date"></b-form-input>
            <b-form-input type="time" v-model="postData.e_time"></b-form-input>
          </b-form-group>

          <b-form-group
              label="내용:"
              label-for="nested-country"
              label-cols-sm="3"
              label-align-sm="right"
          >
            <b-form-textarea
                id="textarea-auto-height"
                v-model="postData.content"
                placeholder="내용을 입력하세요."
                rows="3"
                max-rows="8"
                style="resize: none;"
            ></b-form-textarea>
          </b-form-group>
        </b-form-group>
      </b-card>
    </div>
    <div class="btnArea">
      <b-button block variant="primary" class="btn" @click="registApproval()">제출</b-button>
    </div>
  </div>

</template>

<script setup>
import {onMounted, reactive, ref, watch} from "vue";
import {useRoute} from "vue-router";
import axios from "axios";
import router from "@/router/router.js";

const route = useRoute();

const userId = ref();

const postData = reactive({
  s_date: "",
  s_time: "",
  e_date: "",
  e_time: "",
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

const updateDateTime = () => {
  postData.startTime = `${postData.s_date} ${postData.s_time}:00`;
  postData.endTime = `${postData.e_date} ${postData.e_time}:00`;
}

const registApproval = async () => {

  postData.requesterId = userId.value;

  const startTIme = new Date(postData.startTime);
  const endTime = new Date(postData.endTime);

  const diff = (endTime.getTime() - startTIme.getTime()) / (1000 * 60 * 60);

  try {
    const confirmed = window.confirm('결재를 제출하시겠습니까?');

    if (confirmed) {
      if (postData.content !== "" && postData.startTime !== " :00" && postData.endTime !== " :00") {
        if (diff < 12) {
          const response = await axios.post("/api/approval/overtime", postData, {
            headers: {
              'Content-Type': "application/json"
            }
          })
          if (!response.data.success) {
            alert(response.data.message)
          } else if (response.status === 201) {
            alert('결재가 제출되었습니다.');
            router.push(response.data.href);
          } else {
            throw new Error("response is not ok");
          }
        } else {
          alert('시간 외 근무 시간은 12시간을 초과할 수 없습니다.');
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

onMounted(async () => {
  const token = localStorage.getItem('access');

  if (token) {
    const decodedToken = parseJwt(token);

    userId.value = decodedToken.id || '';
  }
})
</script>


<style scoped>
.registOAAll {
  display: grid;
  grid-template-rows: 18% 50% 5% 27%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.oaHeader {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
  margin-left: -0.5%;
  color: #000000;
}

.oaHeader h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.oaContent {
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