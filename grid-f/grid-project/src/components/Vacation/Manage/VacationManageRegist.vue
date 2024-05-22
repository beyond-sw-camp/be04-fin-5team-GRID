<template>
    <div class="manageRegistAll">
        <div class="manageTitle">
            <img class = "registIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 등록</h1>
        </div>
        <div class="registMain">
            <div class="vacationType">
                <p>휴가 종류</p>
                <input type="text" placeholder="휴가 종류를 입력해주세요." class="inputField"/>
            </div>
            <div class="vacationGiveNum">
                <p>휴가 제공일수</p>
                <input type="text" placeholder="휴가 제공일수를 입력해주세요." class="inputField"/>
            </div>
            <div class="vacationPeriod">
                <p>휴가 사용기한</p>
                <input type="text" placeholder="휴가 사용기한을 입력해주세요." class="inputField"/>
            </div>
            <div class="vacationDesc">
                <p>설명</p>
                <input type="text" placeholder="설명을 입력해주세요." class="inputField"/>
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
    .manageRegistAll {
        display: grid;
        grid-template-rows: 15% 80% 5% ;
        grid-template-columns: 10% 80% 10%;
        height: 100%;
    }

    .manageTitle {
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

    .registIcon {
        width:100%;
    }

    .registMain {
        margin-top: 4%;
        margin-bottom: 2%;
        font-size: 20px;
        grid-column-start: 2;
        grid-column-end: 3;
        height: 80%;
        width: calc(100% - 20px); /* 부모 요소의 너비에 비례하여 폭을 설정 */
        padding: 10px; /* 내부 여백 추가 */
        background-color: #F8F9FAFF;
    }

    .vacationType {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 8% 92%;
        font-size: 15px;

    }

    .vacationGiveNum {
        margin-top: 3%;
        display: grid;
        grid-template-columns: 8% 92%;
        font-size: 15px;
    }

    .vacationPeriod {
        margin-top: 2%;
        display: grid;
        grid-template-columns: 8% 92%;
        font-size: 15px;

    }

    .vacationDesc {
        margin-top: 3%;
        height: 50%;
        display: grid;
        grid-template-columns: 8% 92%;
        font-size: 15px;
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