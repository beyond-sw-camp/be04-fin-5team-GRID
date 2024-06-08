<template>
    <div v-if="isLoading" class="backdrop">
        <div class="spinner">
            <div class="lds-spinner">
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
            </div>
        </div>
    </div>
    <div v-else-if="isSuccess" class="checkmark">
        <div class="checkmark-circle">
            <div class="checkmark-check"></div>
        </div>
    </div>
</template>

<script setup>
import { defineProps } from 'vue';

const props = defineProps({
    isLoading: Boolean,
    isSuccess: Boolean
});
</script>

<style scoped>
.backdrop {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(255, 255, 255, 0.8); /* 백드롭을 흰색 반투명으로 설정 */
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 9999; /* 스피너가 다른 요소들보다 위에 표시되도록 설정 */
}

.spinner {
    display: flex;
    justify-content: center;
    align-items: center;
}

.lds-spinner {
    color: #088A85;
    display: inline-block;
    position: relative;
    width: 80px;
    height: 80px;
}

.lds-spinner div {
    transform-origin: 40px 40px;
    animation: lds-spinner 1.2s linear infinite;
}

.lds-spinner div:after {
    content: " ";
    display: block;
    position: absolute;
    top: 3px;
    left: 37px;
    width: 6px;
    height: 18px;
    border-radius: 20%;
    background: #088A85; /* 스피너 색상 변경 */
}

.lds-spinner div:nth-child(1) {
    transform: rotate(0deg);
    animation-delay: -1.1s;
}

.lds-spinner div:nth-child(2) {
    transform: rotate(30deg);
    animation-delay: -1s;
}

.lds-spinner div:nth-child(3) {
    transform: rotate(60deg);
    animation-delay: -0.9s;
}

.lds-spinner div:nth-child(4) {
    transform: rotate(90deg);
    animation-delay: -0.8s;
}

.lds-spinner div:nth-child(5) {
    transform: rotate(120deg);
    animation-delay: -0.7s;
}

.lds-spinner div:nth-child(6) {
    transform: rotate(150deg);
    animation-delay: -0.6s;
}

.lds-spinner div:nth-child(7) {
    transform: rotate(180deg);
    animation-delay: -0.5s;
}

.lds-spinner div:nth-child(8) {
    transform: rotate(210deg);
    animation-delay: -0.4s;
}

.lds-spinner div:nth-child(9) {
    transform: rotate(240deg);
    animation-delay: -0.3s;
}

.lds-spinner div:nth-child(10) {
    transform: rotate(270deg);
    animation-delay: -0.2s;
}

.lds-spinner div:nth-child(11) {
    transform: rotate(300deg);
    animation-delay: -0.1s;
}

.lds-spinner div:nth-child(12) {
    transform: rotate(330deg);
    animation-delay: 0s;
}

@keyframes lds-spinner {
    0% {
        opacity: 1;
    }
    100% {
        opacity: 0;
    }
}

.checkmark-circle {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    background: green;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
}

.checkmark-check {
    width: 40px;
    height: 20px;
    border: 4px solid white;
    border-top: none;
    border-right: none;
    transform: rotate(-45deg);
    position: absolute;
}
</style>
