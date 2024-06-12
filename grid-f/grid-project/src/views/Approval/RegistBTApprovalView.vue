<template>
  <div class="registBTAll">
    <div class="registBTHeader">
      <nav style="--bs-breadcrumb-divider: '>'; margin-top: -35px; margin-bottom: -7px;" aria-label="breadcrumb">
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="http://www.gridhr.site/regist/main" style="text-decoration: none; color: grey; font-size: 17px;"><i class="bi bi-pencil-square"></i>&nbsp; 결재 작성</a></li>
          <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i class="bi bi-briefcase"></i>&nbsp; 출장</span></li>
        </ol>
      </nav>
      <div><h1 class="fw-bolder"><i class="bi bi-briefcase"></i>&nbsp; 출장 신청</h1></div>
    </div>
    <div class="registBTContent">
      <b-card class="mt-3" bg-variant="light">
        <b-form-group
            label-cols-lg="3"
            label="출장 결재"
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
            <b-form-input type="date" id="start" v-model="postData.startTime"></b-form-input>
          </b-form-group>

          <b-form-group
              label="종료 일자:"
              label-for="nested-city"
              label-cols-sm="3"
              label-align-sm="right"
          >
            <b-form-input type="date" id="end" v-model="postData.endTime" :min="postData.startTime"></b-form-input>
          </b-form-group>

          <b-form-group
              label="출장지:"
              label-for="nested-state"
              label-cols-sm="3"
              label-align-sm="right"
          >
            <b-form-input id="destination" v-model="postData.destination"></b-form-input>
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
                style="resize: none; height: 200px;"
            ></b-form-textarea>
          </b-form-group>
        </b-form-group>
      </b-card>
    </div>
    <div class="btnArea" style="margin-top: 10%;">
      <b-button class="btn" block variant="primary" @click="registApproval()">제출</b-button>
    </div>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref, watch} from "vue";
  import {useRoute} from "vue-router";
  import axios from "axios";
  import router from "@/router/router.js";

  const userId = ref();

  const postData = reactive({
    startTime: "",
    endTime: "",
    destination: "",
    content: "",
    requesterId: 0
  })

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

  const registApproval = async() => {

    postData.requesterId = userId.value;

    try {
      const confirmed = window.confirm('결재를 제출하시겠습니까?');

      if(confirmed) {
        if (postData.content !== '' && postData.startTime !== '' && postData.endTime !== '' && postData.destination !== '') {
          const response = await axios.post(`/api/approval/bt`, postData, {
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
          alert('모든 필드를 입력해주세요.');
        }
      }
    } catch (error) {
      console.error('Fail to post: ', error.message);
    }
  }

  watch(
      () => [postData.startTime],
      () => {
        postData.endTime = '';
      }
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
.registBTAll {
  display: grid;
  grid-template-rows: 18% 45% 5% 27%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.registBTHeader {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
  margin-left: -0.5%;
  color: #000000;
}

.registBTContent {
  grid-column-start: 2;
  grid-row-start: 2;
}

.registBTHeader h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
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