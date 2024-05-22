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
                            <input type="text" v-model="inputValue">
                            <label for="sampleId">이메일</label>
                        </div>
                    </div>
                </div>
                <div class="password">
                    <div class="outBox" :class="{ 'existence': isPwdExistence }">
                        <div class="inputBox">
                            <input type="password" v-model="inputPwd">
                            <label for="samplePwd">비밀번호</label>
                        </div>
                    </div>
                    <!-- <div class="input-group" id="pwd-input">
                        <span class="input-group-text">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                class="bi bi-key" viewBox="0 0 16 16">
                                <path
                                    d="M0 8a4 4 0 0 1 7.465-2H14a.5.5 0 0 1 .354.146l1.5 1.5a.5.5 0 0 1 0 .708l-1.5 1.5a.5.5 0 0 1-.708 0L13 9.207l-.646.647a.5.5 0 0 1-.708 0L11 9.207l-.646.647a.5.5 0 0 1-.708 0L9 9.207l-.646.647A.5.5 0 0 1 8 10h-.535A4 4 0 0 1 0 8m4-3a3 3 0 1 0 2.712 4.285A.5.5 0 0 1 7.163 9h.63l.853-.854a.5.5 0 0 1 .708 0l.646.647.646-.647a.5.5 0 0 1 .708 0l.646.647.646-.647a.5.5 0 0 1 .708 0l.646.647.793-.793-1-1h-6.63a.5.5 0 0 1-.451-.285A3 3 0 0 0 4 5" />
                                <path d="M4 8a1 1 0 1 1-2 0 1 1 0 0 1 2 0" />
                            </svg>
                        </span>
                        <input type="password" class="form-control" placeholder="비밀번호">
                    </div> -->
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
                        <div>Forget your email?</div>
                    </div>
                    <div id="find-id2" @click="findId">아이디 찾기</div>
                </div>
                <div class="find-pwd">
                    <div id="find-pwd1">
                        <div>Forget your password?</div>
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

const inputValue = ref('');
const inputPwd = ref('');
const isExistence = ref(false);
const isPwdExistence = ref(false);

async function Login() {
    try {
        console.log(`${inputValue.value}`);
        console.log(`${inputPwd.value}`);
        const response = await axios.post('http://localhost:8080/login', 
            {   email: inputValue.value, 
                pwd: inputPwd.value
            });
        console.log(response);
        console.log(response.headers);
        if(response.status == 200) {
            localStorage.setItem('token', response.headers.access);    
            alert('로그인 되었습니다');
            // 이후 이동할 경로 (rotuer 추가)
        }
    } catch (e) {
        alert('아이디 또는 비밀번호를 확인해주세요.');
    }

    
};


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
    background-color: #002366;
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
    margin: 12% auto 1%;
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
    background-color: #002366;
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

#find-id1 {
    width: 60%;
    display: flex;
    justify-content: center;
}

#find-id2 {
    font-weight: bold;
    color: #002366;
}

#find-pwd1 {
    width: 60%;
    display: flex;
    justify-content: center;
}

#find-pwd2 {
    font-weight: bold;
    color: #002366;
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
    border: 2px solid #002366;
}

.outBox .inputBox input[type="text"]:focus+label,
.outBox.existence .inputBox label {
    color: #002366;
    transform: scale(0.85) translate(8px, -33px);
}

.outBox .inputBox input[type="text"]:focus,
.outBox.existence .inputBox input[type="text"] {
    border: 2px solid #002366;
}

.outBox .inputBox input[type="text"]:focus+label,
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