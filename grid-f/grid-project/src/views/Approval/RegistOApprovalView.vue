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

  const registApproval = async() => {

    postData.requesterId = userId.value;

    const startTIme = new Date(postData.startTime);
    const endTime = new Date(postData.endTime);

    const diff = (endTime.getTime() - startTIme.getTime()) / (1000 * 60 * 60);

    try {
      const confirmed = window.confirm('결재를 제출하시겠습니까?');

      if(confirmed) {
        if (postData.content !== '') {
          if (diff < 12) {
            const response = await axios.post("http://localhost:8080/approval/overtime", postData, {
              headers: {
                'Content-Type': "application/json"
              }
            })
            if (response.status === 201) {
              alert('결재가 제출되었습니다.');
              router.push(response.data.href);
            } else {
              throw new Error("response is not ok");
            }
          } else {
            alert('시간 외 근무 시간은 12시간을 초과할 수 없습니다.');
          }
        } else {
          alert('내용을 입력해주세요');
        }
      }
    } catch (error) {
        console.error('Fail to post: ', error.message);
    }
  }

  watch(
      () => [postData.s_date, postData.s_time],
      updateDateTime
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

<template>
  <nav style="--bs-breadcrumb-divider: '>'; margin-top: -35px; margin-bottom: -7px;" aria-label="breadcrumb">
    <ol class="breadcrumb">
      <li class="breadcrumb-item"><a href="http://localhost:5173/regist/main" style="text-decoration: none; color: grey; font-size: 17px;"><i class="bi bi-pencil-square"></i>&nbsp; 결재 작성</a></li>
      <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i class="bi bi-clock"></i>&nbsp; 시간 외 근무</span></li>
    </ol>
  </nav>
  <div><h3 class="fw-bolder mb-5"><i class="bi bi-clock"></i>&nbsp; 시간 외 근무 신청</h3></div>
  <div>
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
          ></b-form-textarea>
        </b-form-group>
      </b-form-group>
    </b-card>
  </div>
  <b-button block variant="primary" @click="registApproval()">제출</b-button>
</template>

<style scoped>

</style>