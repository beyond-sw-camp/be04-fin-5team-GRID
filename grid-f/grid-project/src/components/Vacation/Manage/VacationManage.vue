<template>
    <div class="manageAll">
        <div class="manageTitle">
            <img class = "manageIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 관리</h1>
        </div>
        <div class="manageRegist">
            <button class="manageBtn" type="button" @click="registVacation()">등록하기</button>
        </div>
        <div class="vacations">
            <div class="annual">
                <div class="vacationsTitle">
                    <h3>연차</h3>
                    <img class="plusBtn" @click="modifyVacation()" src="@/assets/buttons/plus.png">
                </div>
                <div class="vacationsNum">
                    <h3>임직원의 기본 휴가</h3>
                </div>
            </div>
            <div class="month">
                <div class="vacationsTitle">
                    <h3>월차</h3>
                    <img class="plusBtn" src="@/assets/buttons/plus.png">
                </div>
                <div class="vacationsNum">
                    <h3>1년미만의 직원들이 달마다 받는 휴가</h3>
                </div>    
            </div>
            <div class="regular">
                <div class="vacationsTitle">
                    <h3>정기휴가</h3>
                    <img class="plusBtn" src="@/assets/buttons/plus.png">
                </div>
                <div class="vacationsNum">
                    <h3>1년에 4개씩 기본으로 제공되는 휴가</h3>
                </div>    
            </div>
            <div class="health">
                <div class="vacationsTitle">
                    <h3>보건휴가</h3>
                    <img class="plusBtn" src="@/assets/buttons/plus.png">
                </div>
                <div class="vacationsNum">
                    <h3>한달에 한개씩 여직원에게 제공되는 휴가</h3>
                </div>    
            </div>
        </div>
    </div>
</template>

<script setup>
import router from '@/router/router';

const modifyVacation = () => {
    router.push('/vacation/manage/delete');
}

const registVacation = () => {
    router.push('/vacation/manage/regist');
}

</script>

<style scoped>
    .manageAll {
        display: grid;
        grid-template-rows: 19% 5% 19% 19% 19% 19% ;
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

    .manageIcon {
        width:100%;
    }

    .manageRegist {
        display: grid;
        grid-template-columns: 87% 8% 5%;
        grid-column-start:2 ;
    }

    .manageBtn {
        grid-column-start: 2;
        width: 100%;
        background-color: orange;
        color: white;
        padding: 8px 8px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 12px;
        font-style: bold;
    }

    .vacations {
        margin-top: 2%;
        grid-column-start: 2;
        grid-column-end: 3;
        grid-row: 3;
        display: grid;
        grid-template-columns: 20% 5% 20% 5% 20% 5% 20% 5%;
    }

    .vacationsTitle {
        display: grid;
        grid-template-columns: 90% 10%;
        align-items: center;
    }

    .vacationsTitle p{
        margin:0;
    }

    .vacationsNum {
        grid-column-start: 1;
        display: grid;
        grid-template-columns:90% 10%;
        font-size: 10px;
    }

    .plusBtn {
        width: 100%;
    }

    .annual {
        width: calc(100% - 20px);
        padding: 10px; 
        background-color: #F8F9FAFF;
    }

    .month {
        width: calc(100% - 20px); 
        padding: 10px; 
        background-color: #F8F9FAFF;
        grid-column-start: 3;
    }

    .regular {
        width: calc(100% - 20px); 
        padding: 10px;
        background-color: #F8F9FAFF;
        grid-column-start: 5;
    }

    .health {
        width: calc(100% - 20px);
        padding: 10px;
        background-color: #F8F9FAFF;
        grid-column-start: 7;
    }
</style>           