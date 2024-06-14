<template>
    <div class="container">
        <div class="left">
            <div class="findId">
                <div id="title">
                    <h1>Your Id is...</h1>
                </div>
                <div class="contents">
                    <div>"" </div>
                    <h3>&nbsp {{ email }} &nbsp</h3>
                    <div> ""</div>
                </div>
                <div class="return" @click="login">
                    <h5>Return to Sign in!</h5>
                </div>
                <div class="line">
                    <hr>
                    <div>or</div>
                    <hr>
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
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();
const email = route.query.email;

function login() {
    router.push('/');
}

function findPwd() {
    router.push('/find/pwd');
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
    width: 100vh;
    margin: 0;
    padding: 0;

}

h1 {
    font-weight: bold;
    font-size: 50px;
}

h3 {
    font-weight: bold;
}

hr {
    width: 49%;
    margin: auto 10px;
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

.findId {
    margin: auto;
    height: 70%;
    width: 50%;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.contents { 
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
    margin: 20% auto 5%;
}

.return {
    display: flex;
    justify-content: center;
    cursor: pointer;
    color: gray;
}


.line {
    width: 85%;
    margin: 1% auto 0;
    display: flex;
    flex-direction: row;

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

</style>