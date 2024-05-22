<template>
    <div class="policyAll">
        <div class="policyTitle">
            <img class = "policyIcon" src="@/assets/buttons/policy.png">
            <h1>휴가정책 등록</h1>
        </div>
        <div class="modifyMain">
            <div class="modifyTitle">
                <p>제목</p>
                <input type="text" placeholder="제목을 입력해주세요." class="inputField"/>
            </div>
            <div class="modifyContent">
                <p>내용</p>
                <input type="text" placeholder="내용을 입력해주세요." class="inputField"/>
            </div>
        </div>
        <div class="buttons">
                <button class="regist">등록하기</button>
        </div>
    </div>
    
</template>

<script setup>

</script>

<style scoped>
    

    .policyAll {
        display: grid;
        grid-template-rows: 15% 75% 10%;
        grid-template-columns: 10% 80% 10%;
        height: 100%;
    }

    .policyTitle {
        grid-column-start: 2;
        grid-column-end: 3;
        font-size: 20px;
        font-weight: 600;
        margin-top: 1%;
        color: #000000;
        display: grid;
        grid-template-columns: 5% 95%;
        align-items: center;
    }

    .policyIcon {
        width:100%;

    }

    .modifyMain {
        margin-top: 6%;
        margin-bottom: 2%;
        font-size: 20px;
        grid-column-start: 2;
        grid-column-end: 3;
        height: 80%;
        width: calc(100% - 20px); /* 부모 요소의 너비에 비례하여 폭을 설정 */
        padding: 10px; /* 내부 여백 추가 */
        background-color: #F8F9FAFF;
    }

    .modifyTitle {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 5% 95%;

    }

    .modifyContent {
        margin-top: 3%;
        height: 70%;
        display: grid;
        grid-template-columns: 5% 95%;
    }

    .buttons {
        display: grid;
        grid-template-columns: 40% 5% 10% 5% 40%;
        place-items: center;
        grid-row-start: 3;
        grid-column-start: 2;
    }

    .regist {
        grid-column-start: 3;
        width: 100%;
        background-color: orange;
        color: white;
        padding: 10px 10px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 12px;
        font-style: bold;
    }

    .inputField {
        padding-left: 10px; 
    }   



    
</style>