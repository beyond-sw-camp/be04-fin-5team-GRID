<template>
    <div class="historyAll">
        <div class="historyTitle">
            <img class = "historyIcon" src="@/assets/buttons/vacation.png">
            <h1>휴가 기록</h1>
        </div>
        <div class="vacations">
            <div class="today">
                <div class="vacationsTitle">
                    <h3>오늘 휴가 인원</h3>
                    <img class="plusBtn" src="@/assets/buttons/plus.png">
                </div>
                <div class="vacationsNum">
                    <h3>10</h3>
                </div>
            </div>
            <div class="tomorrow">
                <div class="vacationsTitle">
                    <h3>내일 휴가 인원</h3>
                    <img class="plusBtn" src="@/assets/buttons/plus.png">
                </div>
                <div class="vacationsNum">
                    <h3>10</h3>
                </div>    
            </div>
        </div>
        <div class="search">
            <button class="sortBox">정렬하기</button>
            <button class="printBtn">출력하기</button>
        </div> 
        <div class="tableContainer">
            <table>
                <thead>
                    <tr>
                        <th></th>
                        <th>이름</th>
                        <th>휴가종류</th>
                        <th>신청날짜</th>
                        <th>연차 사용기간</th>
                        <th>사용개수</th>
                        <th>결재상태</th>
                        <th>세부정보</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="index in 10" :key="index">
                        <td></td>
                        <td>이름 {{ index }}</td>
                        <td>연차</td>
                        <td>2024-05-01</td>
                        <td>2024-05-02 ~ 2024-05-10</td>
                        <td>5</td>
                        <td>완료</td>
                        <td><img class="more" src="@/assets/buttons/zoom.png"></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script setup>

</script>

<style scoped>
    .historyAll {
        display: grid;
        grid-template-rows: 150px  150px 50px 500px 100px ;
        grid-template-columns: 5% 85% 10%;
    }

    .historyTitle {
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

    .historyIcon {
        width:100%;
    }

    .vacations {
        grid-column-start: 2;
        grid-column-end: 3;
        display: grid;
        grid-template-columns: 20% 5% 20% 5% 50%;
    }

    .vacationsTitle {
        display: grid;
        grid-template-columns: 90% 10%;
        align-items: center;
    }

    .vacationsTitle p{
        margin:0;
    }

    .todayBtn{
        height: 20%;
    }

    .vacationsNum {
        grid-column-start: 1;
        display: grid;
        grid-template-columns:15% 85%;
        font-size: 20px;
    }

    .todayBtn {
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

    .plusBtn {
        width: 100%;
    }

    .today {
        width: calc(100% - 20px); /* 부모 요소의 너비에 비례하여 폭을 설정 */
        padding: 10px; /* 내부 여백 추가 */
        background-color: #F8F9FAFF;
    }

    .tomorrow {
        width: calc(100% - 20px); /* 부모 요소의 너비에 비례하여 폭을 설정 */
        padding: 10px; /* 내부 여백 추가 */
        background-color: #F8F9FAFF;
        grid-column-start: 3;
    }

    .search {
        margin-top: 2%;
        grid-row-start:3;
        grid-column-start: 2;
        display: grid;
        grid-template-columns: 85% 10% 5%;
    }

    .sortBox {
        grid-column-start: 2;
        margin-left: 2%;
    }

    .printBtn {
        grid-column-start: 3;
        margin-left: 2%;
        padding: 1%;
    }

    .tableContainer {
        grid-column-start: 2;
        grid-column-end: 3;
        margin-top: 10px;
    }

    table {
        width: 100%;
        border-collapse: collapse;
    }

    th, td {
        border: 1px solid #dddddd;
        text-align: left;
        padding: 8px;
        vertical-align: middle;
    }

    th {
        background-color: #f2f2f2;
    }

    .more {
        width:15px;
        
    }

    
</style>