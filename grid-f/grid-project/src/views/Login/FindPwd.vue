<template>
    <div class="container">
        <div class="left">
            <div class="findPwd">
                <div id="title">
                    <h1>Forgot your Password..?</h1>
                </div>
                <div id="title-content">
                    Trouble to sign in?
                </div>
                <div id="title-content2">
                    Please submit your name & email to reset your password.
                </div>
                <div class="userName">
                    <div class="outBox" :class="{ 'existence': isExistence }">
                        <div class="inputBox">
                            <input type="text" v-model="inputValue">
                            <label for="sampleId">이름</label>
                        </div>
                    </div>
                </div>
                <div v-if="isNameExistence" id="error-message">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                        class="bi bi-exclamation-circle" viewBox="0 0 16 16">
                        <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16" />
                        <path
                            d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0M7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0z" />
                    </svg>
                    &nbsp; 이름을 입력해주세요.
                </div>
                <div class="employeeNumber">
                    <div class="outBox" :class="{ 'existence': isExistence2 }">
                        <div class="inputBox">
                            <input type="text" v-model="inputValue2" @keyup.enter="sendMail">
                            <label for="samplePwd">이메일</label>
                        </div>
                    </div>
                </div>
                <div v-if="isEmailExistence" id="error-message2">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                        class="bi bi-exclamation-circle" viewBox="0 0 16 16">
                        <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16" />
                        <path
                            d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0M7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0z" />
                    </svg>
                    &nbsp; 이메일을 입력해주세요.
                </div>
                <div v-if="isWrong" id="error-message">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                        class="bi bi-exclamation-circle" viewBox="0 0 16 16">
                        <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16" />
                        <path
                            d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0M7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0z" />
                    </svg>
                    &nbsp; 해당하는 정보가 없습니다! 이름과 이메일을 확인해주세요!
                </div>
                <div class="button d-grid gap-3" style="max-width: 56%;">
                    <button type="button" class="btn btn-block" @click="sendMail">Send Email</button>
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
                <div class="toLogin">
                    <div id="toLogin1">
                        <div>Already registered?</div>
                    </div>
                    <div id="toLogin2" @click="login">로그인</div>
                </div>
            </div>
        </div>
        <div class="right">

        </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="myModal" ref="myModal">
        <div class="modal-dialog">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title">메일 전송 완료</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- Modal body -->
                <div class="modal-body">
                    입력하신 메일을 확인해주세요! 로그인 화면으로 돌아갑니다.
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-bs-dismiss="modal" @click="login">Close</button>
                </div>

            </div>
        </div>
    </div>
    <Spinner :isLoading="isLoading" :isSuccess="isSuccess" />
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.js';
import Spinner from '@/components/Spinner.vue';

const inputValue = ref('');
const inputValue2 = ref('');
const isExistence = ref(false);
const isExistence2 = ref(false);
const isNameExistence = ref(false);
const isEmailExistence = ref(false);
const isWrong = ref(false);
const router = useRouter();
const myModal = ref(null);
const isLoading = ref(false);
const isSuccess = ref(false);

async function sendMail() {
    isNameExistence.value = false;
    isEmailExistence.value = false;
    isLoading.value = true;
    isSuccess.value = false;
    isWrong.value = false;

    if (inputValue.value == '') {
        isNameExistence.value = true;
        isLoading.value = false;
        return false;
    }

    if (inputValue2.value == '') {
        isEmailExistence.value = true;
        isLoading.value = false;
        return false;
    }

    try {
        const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/${inputValue2.value}/name`);

        if (response.data.result.employee_name == inputValue.value) {
            try {
                const mailResponse = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/mails/pwd/${inputValue2.value}`);
                if (mailResponse.status == 200) {
                    const modalInstance = new bootstrap.Modal(myModal.value);
                    modalInstance.show();
                }
                isLoading.value = false;
                isSuccess.value = true;
                return true;
            } catch (e) {
                console.error(e);
                isWrong.value = true;
                isLoading.value = false;
            }
        } else {
            isWrong.value = true;
            isLoading.value = false;
        }
    } catch (error) {
        isLoading.value = false;
        if (error.response && error.response.status == 404) {

            isWrong.value = true;
        } else {
            console.error(error);
            isWrong.value = true;
        }
    }
}

function login() {
    router.push('/');
}

function findId() {
    router.push('/find/id');
}

onMounted(() => {
    const modalElement = myModal.value;
    new bootstrap.Modal(modalElement);
})

watch(inputValue, (newValue) => {
    isExistence.value = newValue !== '';
});

watch(inputValue2, (newValue) => {
    isExistence2.value = newValue !== '';
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

.findPwd {
    margin: auto;
    height: 70%;
    width: 60%;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.userName {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    margin: 12% auto 0;
    width: 70.8%;
    font-weight: bold;
}

.employeeNumber {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: center;
    margin: 1% auto 0;
    width: 70.8%;
    font-weight: bold;
}

.button {
    width: 70.8%;
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
    justify-content: center;
}

h1 {
    font-weight: bold;
    font-size: 50px;
}

hr {
    width: 35%;
    margin: auto 10px;
}

.toLogin {
    font-size: 14px;
    width: 80%;
    margin: 1% auto 0 16%;
    display: flex;
    flex-direction: row;
}

.find-id {
    font-size: 14px;
    width: 80%;
    margin: 1% auto 0 16%;
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
    align-self: center;
    font-size: 0.9em;
    color: rgb(179, 173, 173);
}

#title-content2 {
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

#toLogin1 {
    width: 50%;
    display: flex;
    justify-content: center;
}

#toLogin2 {
    font-weight: bold;
    color: #088A85;
    cursor: pointer;
}

#find-id1 {
    width: 50%;
    display: flex;
    justify-content: center;
}

#find-id2 {
    font-weight: bold;
    color: #088A85;
    cursor: pointer;
}

#error-message {
    color: red;
    font-size: 12px;
    text-align: left;
    margin: 1% 0 0 22.5%;
    width: 85%;
}

#error-message2 {
    color: red;
    font-size: 12px;
    text-align: left;
    margin: 1% 0 0 22.5%;
    width: 85%;
}

.outBox {
    margin: 0 auto;
    padding: 0;
    width: 79%;
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
</style>
