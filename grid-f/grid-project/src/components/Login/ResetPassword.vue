<template>
    <div class="firstPwd">
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
    <div class="secondPwd">
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
        영문, 숫자, 특수 문자 조합으로 이루어진 8~15자리 값을 입력하세요.<br>
    </div>
    <div class="button d-grid gap-3" style="max-width: 56%;">
        <button type="button" class="btn btn-block" @click="resetPwd">Reset</button>
    </div>
</template>

<script setup>
import { ref, watch, defineProps, defineEmits } from 'vue';
import axios from 'axios';


const props = defineProps({
    givenEmail: {
        type: String,
        required: true
    }
});

const emit = defineEmits(['passwordResetSuccess']);
const inputValue = ref('');
const inputValue2 = ref('');
const isExistence = ref(false);
const isExistence2 = ref(false);
const isPwdExistence = ref(false);
const isPwdExistence2 = ref(false);
const isPwdRight = ref(false);
const isPwdRight2 = ref(false);
const pwdRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;

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

async function resetPwd() {

    isPwdExistence.value = false;
    isPwdExistence2.value = false;

    if (inputValue.value == '') {
        isPwdExistence.value = true;
        return false;
    }

    if (!pwdRegex.test(inputValue.value)) {
        isPwdRight.value = true;
        return false;
    }

    if (inputValue2.value == '') {
        isPwdExistence2.value = true;
        return false;
    }

    if (inputValue2.value != inputValue.value) {
        isPwdRight2.value = true;
        return false;
    }

    try {
        await axios.put(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/pwd`, {
            pwd: inputValue.value,
            email: props.givenEmail
        });
        alert("비밀번호가 성공적으로 변경되었습니다.");
        emit('passwordResetSuccess');
    } catch (error) {
        console.error("비밀번호 변경 중 오류 발생: ", error);
        alert("비밀번호 변경 중 오류가 발생했습니다.");
    }
};

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

.firstPwd {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    margin: 12% auto 0;
    width: 70.8%;
    font-weight: bold;
}

.secondPwd {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: center;
    margin: 1% auto 0;
    width: 70.8%;
    font-weight: bold;
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
    border: 2px solid #088A85;
}

.outBox .inputBox input[type="password"]:focus+label,
.outBox.existence .inputBox label {
    color: #088A85;
    transform: scale(0.85) translate(8px, -33px);
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

.info {
    margin: 3% auto 0;
    color: gray;
    font-size: 12px;
    text-align: center;
    max-width: 55%;
}

.button {
    width: 70.8%;
    margin: 3% auto 0;

}

.btn {
    background-color: #088A85;
    color: white;
    font-weight: 600;
    height: 38px;
    /* 버튼의 높이를 명시적으로 설정 */
}
</style>