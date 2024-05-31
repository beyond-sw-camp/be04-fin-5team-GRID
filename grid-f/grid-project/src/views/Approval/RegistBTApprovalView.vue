<script setup>
  import {onMounted, reactive, ref} from "vue";
  import {useRoute} from "vue-router";
  import axios from "axios";

  const route = useRoute();

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

    alert('결재를 제출하시겠습니까?');
    postData.requesterId = userId.value;

    try {
      const response = await axios.post(`http://localhost:8080/approval/bt`, postData, {
        headers: {
          'Content-Type': "application/json"
        }
      })

      if (response.status !== 201) {
        throw new Error("response is not ok");
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
  <div>
    <b-card bg-variant="light">
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
          <b-form-input type="date" :state="false" id="start" v-model="postData.startTime"></b-form-input>
        </b-form-group>

        <b-form-group
            label="종료 일자:"
            label-for="nested-city"
            label-cols-sm="3"
            label-align-sm="right"
        >
          <b-form-input type="date" id="end" v-model="postData.endTime"></b-form-input>
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
              placeholder="Auto height textarea"
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