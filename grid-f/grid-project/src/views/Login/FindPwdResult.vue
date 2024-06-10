<template>
    <div class="container">
        <div class="left">
            <div class="findPwd">
                <div id="title">
                    <h1>Reset your password!</h1>
                </div>
                <ResetPassword :givenEmail="givenEmail" @passwordResetSuccess="handlePasswordResetSuccess"/>
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
import { useRouter, useRoute } from 'vue-router';
import ResetPassword from '@/components/Login/ResetPassword.vue';

const router = useRouter();
const route = useRoute();

const givenEmail = atob(route.params.email);


function login() {
    router.push('/');
}

function findId() {
    router.push('/find/id');
}

function handlePasswordResetSuccess() {
    router.push('/');
}


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

h1 {
    font-weight: bold;
    font-size: 50px;
}

hr {
    width: 35%;
    margin: auto 10px;
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


.line {
    width: 85%;
    margin: 1% auto 0;
    display: flex;
    flex-direction: row;
    justify-content: center;

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
    max-width: 55%;
}

#title {
    font-size: 1em;
    align-self: center;
}

#title h1 {
  font-family: 'IBMPlexSansKR-Regular';
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

</style>