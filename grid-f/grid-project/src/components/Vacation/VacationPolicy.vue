<template>
    <div class="policyAll">
        <div class="policyTitle">
            <img class = "policyIcon" src="@/assets/buttons/policy.png">
            <h1>휴가 정책</h1>
            <button class = "policyRegist">등록하기</button>
        </div>
        <div class="policyContent">
            <div class="policyBox">
                <div class="policyHeader">
                    <h3 class="policyName">연차</h3>
                    <button class="policyModify">수정하기</button>
                </div>
                <h5 class="policies">근로기준법 제 60조(연차 유급휴가)
                    <br> 1. 사용자는 1년간 80퍼센트 이상 출근한 근로자에게 15일의 유급휴가를 주어야 한다. <개정 2012. 2. 1.>
                    <br> 2. 사용자는 계속하여 근로한 기간이 1년 미만인 근로자 또는 1년간 80퍼센트 미만 출근한 근로자에게 1개월 개근 시 1일의 유급휴가를 주어야 한다. <개정 2012. 2. 1.> 
                    <br> 3. 사용자는 3년 이상 계속하여 근로한 근로자에게는 제1항에 따른 휴가에 최초 1년을 초과하는 계속 근로 연수 매 2년에 대하여 1일을 가산한 유급휴가를 주어야 한다.
                    <br> 이 경우 가산휴가를 포함한 총 휴가 일수는 25일을 한도로 한다.
                    <br> 4. 사용자는 제1항부터 제4항까지의 규정에 따른 휴가를 근로자가 청구한 시기에 주어야 하고, 그 기간에 대하여는 취업규칙 등에서 정하는 통상임금 또는 평균임금을
                    <br> 지급하여야 한다. 다만, 근로자가 청구한 시기에 휴가를 주는 것이 사업 운영에 막대한 지장이 있는 경우에는 그 시기를 변경할 수 있다.
                    <br> 5. 제1항 및 제2항을 적용하는 경우 다음 각 호의 어느 하나에 해당하는 기간은 출근한 것으로 본다. <개정 2012. 2. 1., 2017. 11. 28.>
                    <br>
                    <br>  &nbsp; 1. 근로자가 업무상의 부상 또는 질병으로 휴업한 기간
                    <br>  &nbsp; 2. 임신 중의 여성이 제74조제1항부터 제3항까지의 규정에 따른 휴가로 휴업한 기간
                    <br>  &nbsp; 3. 「남녀고용평등과 일ㆍ가정 양립 지원에 관한 법률」 제19조제1항에 따른 육아휴직으로 휴업한 기간
                    <br>
                    <br> 6. 제1항ㆍ제2항 및 제4항에 따른 휴가는 1년간(계속하여 근로한 기간이 1년 미만인 근로자의 제2항에 따른 유급휴가는 최초 1년의 근로가 끝날 때까지의 기간을 말한다) 
                    <br> 행사하지 아니하면 소멸된다. 다만, 사용자의 귀책사유로 사용하지 못한 경우에는 그러하지 아니하다. <개정 2020. 3. 31.>    
                </h5>
            </div>
            <div class="policyBox">
                <h3 class="policyName">보건 휴가</h3>
                <h5 class="policies">1. 여성 직원은 매달 1일 보건휴가 1일이 자동으로 추가된다.
                    <br> 2. 사용하지 않은 보건휴가는 매달  말일 자동으로 소멸된다.
                </h5>
            </div>
            <div class="policyBox">
                <h3 class="policyName">정기 휴가</h3>
                <h5 class="policies">1. 정기휴가는 입사이후 1년부터 기본 4일이 제공된다.
                    <br> 2. 받은후로부터 1년안에 사용해야하며, 사용하지 않은 정기휴가는 소멸되고 따로 보상받을 수 없다. 
                </h5>
            </div>
        
        </div>
    

    </div>
    
</template>

<script setup>

</script>

<style scoped>
    

    .policyAll {
        display: grid;
        grid-template-rows: 150px minmax(830px, auto);
        grid-template-columns: 5% 85% 10%;
    }

    .policyTitle {
        grid-column-start: 2;
        grid-column-end: 3;
        font-size: 20px;
        font-weight: 600;
        margin-top: 1%;
        color: #000000;
        display: grid;
        grid-template-columns: 5% 90% 5%;
        align-items: center;
    }

    .policyName {
        margin-top: 0;
        margin-bottom: 2%;
        font-size: 20px;
        grid-column-start: 2;
        grid-column-end: 3;
    }

    .policyContent {
        margin-top: 2%;
        margin-bottom: 0;
        grid-column-start: 2;
        grid-column-end: 3;
    }

    .policies {
        margin-top: 0;
        margin-bottom: 0;
    }

    .policyBox {
        width: calc(100% - 20px); /* 부모 요소의 너비에 비례하여 폭을 설정 */
        padding: 10px; /* 내부 여백 추가 */
        margin-top: 10px;
        margin-bottom: 0;
        background-color: aliceblue;
    }

    .policyIcon {
        width:100%;

    }

    .policyRegist {
        background-color: orange;
        color: white;
        padding: 10px 10px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 12px;
        font-style: bold;
    }
    

    
</style>