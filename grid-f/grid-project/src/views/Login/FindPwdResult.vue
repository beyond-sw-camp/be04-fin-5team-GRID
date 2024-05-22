<template>
    <div class="container">
        <div class="left">
            <div class="findPwd">
                <div id="title">
                    <h1>Reset your password!</h1>
                </div>

                <div class="userName">
                    <div class="outBox" :class="{ 'existence': isExistence }">
                        <div class="inputBox">
                            <input type="password" v-model="inputValue" @input="validatePwd">
                            <label for="sampleId">새 비밀번호</label>
                        </div>
                    </div>
                </div>
                <div v-if="isPwdExistence" id="error-message">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                        class="bi bi-exclamation-circle" viewBox="0 0 16 16">
                        <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16" />
                        <path
                            d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0M7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0z" />
                    </svg>
                    &nbsp; 비밀번호를 입력해주세요.
                </div>
                <div v-if="isPwdRight" id="error-message">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                        class="bi bi-exclamation-circle" viewBox="0 0 16 16">
                        <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16" />
                        <path
                            d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0M7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0z" />
                    </svg>
                    &nbsp; 잘못된 비밀번호 형식입니다. 다시 입력해주세요!
                </div>
                <div class="employeeNumber">
                    <div class="outBox" :class="{ 'existence': isExistence2 }">
                        <div class="inputBox">
                            <input type="password" v-model="inputValue2" @input="validatePwd2" @keyup.enter="resetPwd">
                            <label for="samplePwd">비밀번호 확인</label>
                        </div>
                    </div>
                </div>
                <div v-if="isPwdExistence2" id="error-message2">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                        class="bi bi-exclamation-circle" viewBox="0 0 16 16">
                        <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16" />
                        <path
                            d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0M7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0z" />
                    </svg>
                    &nbsp; 재확인 비밀번호를 입력해주세요.
                </div>
                <div v-if="isPwdRight2" id="error-message">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                        class="bi bi-exclamation-circle" viewBox="0 0 16 16">
                        <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16" />
                        <path
                            d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0M7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0z" />
                    </svg>
                    &nbsp; 비밀번호가 일치하지 않습니다. 다시 입력해주세요!
                </div>
                <div class="info">
                    영문, 숫자, 특수 문자 조합으로 이루어진 8~15자리의 값을 입력해주세요.<br>
                </div>
                <div class="button d-grid gap-3" style="max-width: 56%;">
                    <button type="button" class="btn btn-block" @click="resetPwd">Reset</button>
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
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.js';

const inputValue = ref('');
const inputValue2 = ref('');
const isExistence = ref(false);
const isExistence2 = ref(false);
const isPwdExistence = ref(false);
const isPwdExistence2 = ref(false);
const isPwdRight = ref(false);
const isPwdRight2 = ref(false);
const isWrong = ref(false);
const router = useRouter();
const myModal = ref(null);
const pwdRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;

async function resetPwd() {
    isPwdExistence.value = false;
    isPwdExistence2.value = false;
    isWrong.value = false;

    if (inputValue.value == '') {
        isPwdExistence.value = true;
        return false;
    }

    if (inputValue2.value == '') {
        isPwdExistence2.value = true;
        return false;
    }

    await axios.get(`http://localhost:8080/users/${inputValue2.value}/name`)
        .then((response) => {
            console.log(response.data.result);
            if (response.status == 404) {
                console.log(response);
            }
            if (response.data.result.employee_name == inputValue.value) {
                try {
                    axios.get(`http://localhost:8080/mails/pwd/${inputValue2.value}`)
                        .then((response) => {
                            if (response.status == 200) {
                                const modalInstance = new bootstrap.Modal(myModal.value);
                                modalInstance.show();
                            }
                        })
                    return true; 9
                } catch (e) {
                    isWrong.value = true;
                }
            }
            isWrong.value = true;
        })


};

function login() {
    router.push('/');
}

function findId() {
    router.push('/find/id');
}

function validatePwd() {
    if (!pwdRegex.test(inputValue.value)) {
        isPwdRight.value = true;
        return true;
    }
    isPwdRight.value = false;
}

function validatePwd2() {
    if (inputValue.value != inputValue2.value) {
        isPwdRight2.value = true;
        return true;
    }
    isPwdRight2.value = false;
}

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

* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    width: 100%;
    margin: 0;
    padding: 0;
}


.container {
    font-family: 'IBMPlexSansKR-Regular';
    height: 100vh;
    min-width: 100%;
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
    background-color: #002366;
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
    background-color: #002366;
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

.info {
    margin: 3% auto 0;
    color: gray;
    font-size: 12px;
    text-align: center;
}

#title {
    font-size: 1em;
    align-self: center;
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
    color: #002366;
    cursor: pointer;
}

#find-id1 {
    width: 50%;
    display: flex;
    justify-content: center;
}

#find-id2 {
    font-weight: bold;
    color: #002366;
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

.inputBox input[type="password"] {
    padding: 0 10px;

    width: 100%;
    height: 38px;
    font-size: 14px;
    border-radius: 8px;
    box-sizing: border-box;
    border-color: rgb(255, 255, 255);
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

.outBox .inputBox input[type="password"]:focus,
.outBox.existence .inputBox input[type="password"] {
    border: 2px solid #002366;
}

.outBox .inputBox input[type="password"]:focus+label,
.outBox.existence .inputBox label {
    color: #002366;
    transform: scale(0.85) translate(8px, -33px);
}

.outBox .inputBox input[type="password"]:focus,
.outBox.existence .inputBox input[type="password"] {
    border: 2px solid #002366;
}

.outBox .inputBox input[type="password"]:focus+label,
.outBox.existence .inputBox label {
    color: #002366;
    transform: scale(0.85) translate(8px, -33px);
}
</style>