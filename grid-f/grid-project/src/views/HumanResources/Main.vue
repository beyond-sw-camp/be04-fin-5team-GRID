<template>
    <div class="hr-main">
        <div class="hr-title">
            <img class="hr-icon" src="@/assets/icon2.png">
            <h1>인사 정보</h1>
        </div>
        <div class="search">
            <button class="printBtn">
                <img src="@/assets/buttons/download.png" alt="download button">
                Download
            </button>

            <button type="button" class="modifyBtn">
                <img src="@/assets/buttons/modify-btn.png" alt="modify button">
                Modify
            </button>
            <button type="button" class="addBtn" data-bs-toggle="dropdown" @click="toggleAddDropdown">
                <img src="@/assets/buttons/plus.png" :class="{ rotated: isAddDropdownOpen }" alt="add button">
                Add new
            </button>
            <ul class="dropdown-menu">
                <li style="margin-bottom: 5%;"><a class="dropdown-item" href="#"><img
                            src="@/assets/buttons/add-one.png">신규 직원 등록</a></li>
                <li><a class="dropdown-item" href="#"><img src="@/assets/buttons/add-multi.png">일괄 등록</a></li>
            </ul>
        </div>
        <div class="tableContainer">
            <table>
                <thead>
                    <tr>
                        <th></th>
                        <th>이름</th>
                        <th>사원 번호</th>
                        <th>부서</th>
                        <th>팀</th>
                        <th>직위</th>
                        <th>직책</th>
                        <th>부재중</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="employee in employeeList" :key="employee.id">
                        <td><img :src="employee.profilePath" alt="profile"></td>
                        <td>{{ employee.name }}</td>
                        <td>{{ employee.employeeNumber }}</td>
                        <td>{{ employee.department }}</td>
                        <td>{{ employee.team }}</td>
                        <td>{{ employee.position }}</td>
                        <td>{{ employee.duties }}</td>
                        <td>{{ employee.absenceYn === 'Y' ? 'Yes' : 'No' }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import axios from 'axios';

const isAddDropdownOpen = ref(false);
const employeeList = ref([]);

const toggleAddDropdown = () => {
    isAddDropdownOpen.value = !isAddDropdownOpen.value;
    console.log("Add Dropdown:", isAddDropdownOpen.value);
};

onMounted(async () => {
    isAddDropdownOpen.value = false;
    document.addEventListener('click', handleDocumentClick);

    try {
        const response = await axios.get('http://localhost:8080/users/list');
        employeeList.value = response.data.result;
    } catch {
        console.error('axios error: 데이터를 받아오지 못했습니다.');
    }
})

onUnmounted(() => {
    document.removeEventListener('click', handleDocumentClick);
});

const handleDocumentClick = (event) => {
    const target = event.target;
    const dropdownButton = document.querySelector('.addBtn');
    if (!dropdownButton.contains(target)) {
        isAddDropdownOpen.value = false;
        console.log("Add Dropdown:", isAddDropdownOpen.value);
    }
};

</script>

<style scoped>
.hr-main {
    display: grid;
    grid-template-columns: 10% 80% 10%;
    grid-template-rows: 18% 5% 43% 5% 13%;

    height: 100%;
}

.hr-title {
    grid-column-start: 2;
    grid-column-end: 3;
    margin-top: 2%;
    color: #000000;
    display: grid;
    grid-template-columns: 3% 97%;
    align-items: center;
}

.hr-title h1 {
    margin-left: 0.5%;
    font-weight: bold;
    font-size: 14pt;
}

.hr-icon {
    width: 80%;
    filter: invert(0%) sepia(64%) saturate(7%) hue-rotate(334deg) brightness(85%) contrast(101%);
}

.vacations {
    grid-column-start: 2;
    grid-column-end: 3;
    display: grid;
    grid-template-columns: 10% 5% 20% 5% 20% 30%;
    align-items: center;
    margin-bottom: 5%;
}

.vacationsTitle {
    margin-left: 5%;
    display: grid;
    grid-template-columns: 85% 10% 5%;
    align-items: center;
    height: 10vh;
}

.vacationsTitle h3 {
    font-size: 12pt;
}

.plusBtn {
    width: 100%;
    cursor: pointer;
}

.annual {
    width: calc(100% - 20px);
    background-color: #F2F2F2;
    font-size: 15px;
}

.month {
    width: calc(100% - 20px);
    background-color: #F2F2F2;
    grid-column-start: 3;
    font-size: 15px;
}

.diretly {
    width: calc(100% - 20px);
    background-color: #F2F2F2;
    grid-column-start: 5;
    font-size: 15px;
}

.search {
    grid-row-start: 2;
    grid-column-start: 2;
    grid-template-columns: 52% 16% 16% 16%;
    display: grid;
    justify-content: flex-end;
}

.printBtn {
    min-width: 90px;
    grid-column-start: 2;
    margin: 3px;
    width: 90%;
    background-color: #088A85;
    color: white;
    padding: 5px 5px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    /* font-size: 12px; */
    font-size: 70%;
    font-style: bold;
    justify-self: end;
}

.modifyBtn {
    grid-column-start: 3;
    margin: 3px;
    width: 80%;
    min-width: 90px;
    background-color: #088A85;
    color: white;
    padding: 5px 5px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
    justify-self: flex-end;
}

.addBtn {
    grid-column-start: 4;
    margin: 3px;
    width: 80%;
    min-width: 90px;
    background-color: #088A85;
    color: white;
    padding: 5px 5px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
    justify-self: flex-end;
}

.printBtn img {
    width: 18%;
    height: 90%;
    margin: 0 6% 4% 0;
    filter: invert(100%) sepia(65%) saturate(424%) hue-rotate(91deg) brightness(129%) contrast(107%);
    transition: transform 0.3s ease;
}

.modifyBtn img {
    width: 16%;
    height: 70%;
    margin: 0 7% 2% 0;
    filter: invert(100%) sepia(65%) saturate(424%) hue-rotate(91deg) brightness(129%) contrast(107%);
}

.addBtn img {
    width: 18%;
    height: 80%;
    margin: 0 4% 2% 0;
    filter: invert(100%) sepia(65%) saturate(424%) hue-rotate(91deg) brightness(129%) contrast(107%);
    transition: transform 0.3s ease;
}

.addBtn img.rotated {
    transform: rotate(45deg);
}

.dropdown-menu {
    font-size: 12px;
    position: absolute;
    z-index: 999;
    background-color: white;
    border: 1px solid #ddd;
    border-radius: 4px;
    padding: 10px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    margin-top: 5px;
}

.dropdown-menu img {
    width: 16px;
    height: 16px;
    margin: 0 13% 2% 0;
    align-self: center;
}

.dropdown-item {
    padding: 0;
    transition: background-color 0.3s ease;
}

.dropdown-item:hover {
    background-color: #deefef;

}

.dropdown-menu li {
    display: flex;
}

.tableContainer {
    grid-column-start: 2;
    grid-column-end: 3;
    margin-top: 20px;
    font-size: 10px;
}

table {
    width: 100%;
    border-collapse: collapse;
}

th,
td {
    border: 1px solid #ffffff;
    text-align: center;
    padding: 6px;
    vertical-align: middle;
    color: #a7a4a4
}

th {
    background-color: #f2f2f2;
}
</style>
