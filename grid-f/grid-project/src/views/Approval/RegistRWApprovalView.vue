<script setup>
  import {computed, onMounted, reactive, ref} from "vue";
  import {useRoute} from "vue-router";
  import axios from "axios";
  import router from "@/router/router.js";
  import {useStore} from "vuex";

  const route = useRoute();
  const store = useStore();

  const user = computed(() => store.state.user);

  const userId = ref();

  const fileInput = ref(null);

  const postData = reactive({
    startTime: "",
    endTime: "",
    file: null,
    content: "",
    requesterId: 0,
    originName: '',
    renameName: '',
    path: ''
  })

  const handleFileChange = () => {
    const file = fileInput.value;

    if (file) {
      console.log('Selected file:', file);
    } else {
      console.error('No file selected');
    }
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

  const registApproval = async () => {

    alert('결재를 제출하시겠습니까?');

    postData.requesterId = userId.value;

    const formData = new FormData();

    formData.append('postData', JSON.stringify(postData));
    formData.append('file', fileInput.value);

    try {
      if (user.value.gender === 'M') {
        const response = await axios.post(`http://localhost:8080/approval/rw`, formData, {
          headers: {
            'Content-Type': "multipart/form-data"
          }
        })
        if (response.status !== 201) {
          throw new Error("response is not ok");
        } else {
          alert('결재가 제출되었습니다.')
          router.push(response.data.href);
        }
      } else if (user.value.gender === 'M'){
        alert('남직원은 임신 단축 근무를 신청할 수 없습니다.')
      } else {
        alert('단축 근무 기간입니다.')
      }
    } catch (error) {
      console.error('Fail to post: ', error.message);
    }
  }

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
      <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i class="bi bi-heart-pulse"></i>&nbsp; 단축 근무</span></li>
    </ol>
  </nav>
  <div><h3 class="fw-bolder pb-5"><i class="bi bi-heart-pulse"></i>&nbsp; 단축 근무 신청</h3></div>
  <div>
    <b-card class="mt-3" bg-variant="light">
      <b-form-group
          label-cols-lg="3"
          label="단축 근무 결재"
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
          <b-form-input type="date" :state="false" id="start" v-model="postData.startTime"></b-form-input>
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
            label="증명 서류:"
            label-for="nested-state"
            label-cols-sm="3"
            label-align-sm="right"
        >
          <input type="file" @change="handleFileChange" ref="fileInput" class="form-control" />
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