<template>
  <div class="container">
    <div class="left">
      <div class="login">
        <div id="title">
          <h1>Welcome to Grid!</h1>
        </div>
        <div id="title-content">
          Log in to streamline your business processes.
        </div>
        <div id="title-content2">
          Have a nice day
        </div>
        <div class="email">
          <div class="outBox" :class="{ 'existence': isExistence }">
            <div class="inputBox">
              <input type="text" v-model="inputValue" @keyup.enter="Login">
              <label for="sampleId">이메일</label>
            </div>
          </div>
        </div>
        <div v-if="isValueExistence" id="error-message">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
            class="bi bi-exclamation-circle" viewBox="0 0 16 16">
            <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16" />
            <path
              d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0M7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0z" />
          </svg>
          &nbsp; 이메일을 입력해주세요.
        </div>
        <div class="password">
          <div class="outBox" :class="{ 'existence': isPwdExistence }">
            <div class="inputBox">
              <input type="password" v-model="inputPwd" @keyup.enter="Login">
              <label for="samplePwd">비밀번호</label>
            </div>
          </div>
        </div>
        <div v-if="isValue2Existence" id="error-message2">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
            class="bi bi-exclamation-circle" viewBox="0 0 16 16">
            <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16" />
            <path
              d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0M7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0z" />
          </svg>
          &nbsp; 비밀번호를 입력해주세요.
        </div>
        <div v-if="isWrong" id="error-message">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
            class="bi bi-exclamation-circle" viewBox="0 0 16 16">
            <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16" />
            <path
              d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0M7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0z" />
          </svg>
          &nbsp; 아이디 또는 비밀번호를 확인해주세요!
        </div>
        <div class="button d-grid gap-3" style="max-width: 68%;">
          <button type="button" class="btn btn-block" @click="Login">Sign in</button>
        </div>
        <div class="line">
          <hr>
          <div>or</div>
          <hr>
        </div>
        <div class="find-id">
          <div id="find-id1">
            <div>Forgot your email?</div>
          </div>
          <div id="find-id2" @click="findId">아이디 찾기</div>
        </div>
        <div class="find-pwd">
          <div id="find-pwd1">
            <div>Forgot your password?</div>
          </div>
          <div id="find-pwd2" @click="findPwd">비밀번호 찾기</div>
        </div>
      </div>
    </div>
    <div class="right">

    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';

const inputValue = ref('');
const inputPwd = ref('');
const isExistence = ref(false);
const isPwdExistence = ref(false);
const isValueExistence = ref(false);
const isValue2Existence = ref(false);
const isWrong = ref(false);
const router = useRouter();
const store = useStore();

async function Login() {
  isValueExistence.value = false;
  isValue2Existence.value = false;
  isWrong.value = false;

  if (inputValue.value === '') {
    isValueExistence.value = true;
    return;
  }

  if (inputPwd.value === '') {
    isValue2Existence.value = true;
    return;
  }

  try {
    const response = await axios.post('/api/login', {
      email: inputValue.value,
      pwd: inputPwd.value
    }, {
      withCredentials: true
    });


    if (response.status === 200) {
      const token = response.data.access;

      const response2 = await axios.get(`/api/users/mail/${inputValue.value}`);
      const user = response2.data.result;

      // 이메일과 토큰을 로컬 스토리지에 저장
      localStorage.setItem('email', inputValue.value);
      localStorage.setItem('access', token);

      // Vuex 스토어에 사용자 정보와 이메일 저장
      store.commit('setUser', user);

      alert('로그인 되었습니다');
      isWrong.value = false;

      router.push('/main');
    }
  } catch (e) {
    isWrong.value = true;
  }
}


function findId() {
  router.push('/find/id');
}

function findPwd() {
  router.push('/find/pwd');
}


watch(inputValue, (newValue) => {
  isExistence.value = newValue !== '';
});

watch(inputPwd, (newValue) => {
  isPwdExistence.value = newValue !== '';
});

</script>

<style scoped>
@font-face {
  font-family: 'IBMPlexSansKR-Regular';
  src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

body {
  width: 100vh;
  margin: 0;
  padding: 0;

}

* {
  box-sizing: border-box;
}

.container {
  font-family: 'IBMPlexSansKR-Regular';
  height: 100vh;
  min-width: 100%;
  width: 100%;
  padding: 0;
  display: flex;

  margin: 0;
}

.left {
  width: 70%;
  display: flex;
  padding: 0;
  margin: 0;
}

.right {
  background-color: #088A85;
  width: 30%;
  padding: 0;
  margin: 0;

}

.login {
  margin: auto;
  height: 70%;
  width: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.email {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 12% auto 0;
  width: 85%;
  font-weight: bold;
}

.password {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  margin: 1% auto 0;
  width: 85%;
  font-weight: bold;
}

.button {
  width: 85%;
  margin: 3% auto 0;

}

.btn {
  background-color: #088A85;
  color: white;
  font-weight: 600;
}

.line {
  width: 85%;
  margin: 1% auto 0;
  display: flex;
  flex-direction: row;

}

h1 {
  font-weight: bold;
  font-size: 50px;
}

hr {
  width: 49%;
  margin: auto 10px;
}

.find-id {
  font-size: 14px;
  width: 85%;
  margin: 1% auto 0;
  display: flex;
  flex-direction: row;
}

.find-pwd {
  font-size: 14px;
  width: 85%;
  margin: 1% auto 0;
  display: flex;
  flex-direction: row;
}

#title {
  font-size: 1em;
  align-self: center;
}

#title h1 {
  font-family: 'IBMPlexSansKR-Regular';
}

#title-content {
  font-family: 'IBMPlexSansKR-Regular';
  align-self: center;
  font-size: 0.9em;
  color: rgb(179, 173, 173);
}

#title-content2 {
  font-family: 'IBMPlexSansKR-Regular';
  align-self: center;
  font-size: 0.9em;
  color: rgb(179, 173, 173);
}

#email-input {
  width: 100%;
}

#email-form {
  color: rgb(179, 173, 173);
}

#find-id1 {
  width: 60%;
  display: flex;
  justify-content: center;
}

#find-id2 {
  font-weight: bold;
  color: #088A85;
  cursor: pointer;
}

#find-pwd1 {
  width: 60%;
  display: flex;
  justify-content: center;
}

#find-pwd2 {
  font-weight: bold;
  color: #088A85;
  cursor: pointer;
}

#error-message {
  color: red;
  font-size: 12px;
  text-align: left;
  margin: 1% 0 0 17%;
  width: 85%;
}

#error-message2 {
  color: red;
  font-size: 12px;
  text-align: left;
  margin: 1% 0 0 17%;
  width: 85%;
}

.outBox {
  margin: 0 auto;
  padding: 0;
  width: 100%;
  max-width: 80%;
}

.outBox .inputBox {
  position: relative;
  height: 38px;
}

.inputBox input[type="text"] {
  padding: 0 10px;
  width: 100%;
  height: 38px;
  font-size: 14px;
  border-radius: 8px;
  box-sizing: border-box;
  border-color: rgb(255, 255, 255);
  outline: none;
}

.outBox input[type="text"] {
  padding: 0 10px;
  width: 100%;
  height: 38px;
  font-size: 14px;
  border-radius: 8px;
  border: 1px solid rgb(220, 220, 220);
  box-sizing: border-box;
  outline: none;
}

.inputBox input[type="password"] {
  padding: 0 10px;
  width: 100%;
  height: 20px;
  font-size: 14px;
  border-radius: 8px;
  box-sizing: border-box;
  outline: none;
}

.outBox input[type="password"] {
  padding: 0 10px;
  width: 100%;
  height: 38px;
  font-size: 14px;
  border-radius: 8px;
  border: 1px solid rgb(220, 220, 220);
  box-sizing: border-box;
  outline: none;
}

.outBox .inputBox label {
  position: absolute;
  left: 0;
  bottom: 0;
  padding: 0 6px;
  font-size: 14px;
  color: darkgray;
  font-weight: normal;
  background: #fff;
  transform: scale(1) translate(4px, -10px);
  transition: all 0.15s;
  border-radius: 20px;
  pointer-events: none;
}

.outBox .inputBox input[type="text"]:focus,
.outBox.existence .inputBox input[type="text"] {
  border: 2px solid #088A85;
}

.outBox .inputBox input[type="text"]:focus+label,
.outBox.existence .inputBox label {
  color: #088A85;
  transform: scale(0.85) translate(8px, -33px);
}

.outBox .inputBox input[type="text"]:focus,
.outBox.existence .inputBox input[type="text"] {
  border: 2px solid #088A85;
}

.outBox .inputBox input[type="text"]:focus+label,
.outBox.existence .inputBox label {
  color: #088A85;
  transform: scale(0.85) translate(8px, -33px);
}


.outBox .inputBox input[type="password"]:focus,
.outBox.existence .inputBox input[type="password"] {
  border: 2px solid #088A85;
}

.outBox .inputBox input[type="password"]:focus+label,
.outBox.existence .inputBox label {
  color: #088A85;
  transform: scale(0.85) translate(8px, -33px);
}

.outBox .inputBox input[type="password"]:focus,
.outBox.existence .inputBox input[type="password"] {
  border: 2px solid #088A85;
}

.outBox .inputBox input[type="password"]:focus+label,
.outBox.existence .inputBox label {
  color: #088A85;
  transform: scale(0.85) translate(8px, -33px);
}
</style> 