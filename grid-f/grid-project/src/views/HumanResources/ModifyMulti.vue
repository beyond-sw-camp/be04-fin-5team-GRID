<template>
    <div class="hr-main">
        <div class="hr-title">
            <img class="hr-icon" src="@/assets/icon2.png" alt="인사 정보 메인 이미지">
            <h1>인사 정보 일괄 수정</h1>
        </div>
        <div class="search">
            <div class="guide">
                <img src="@/assets/buttons/guide.png" alt="guide button" id="guide" @click="openModal">
            </div>
            <button class="downloadBtn" @click="downloadCSV">
                <img src="@/assets/buttons/download.png" alt="download button">
                Format
            </button>
            <button class="uploadBtn" @click="triggerFileUpload">
                <img src="@/assets/buttons/upload.png" alt="upload button">
                Upload CSV
            </button>
            <input type="file" ref="fileInput" class="d-none" @change="handleFileUpload" accept=".csv" />
        </div>
        <div class="tableContainer">
            <table>
                <thead>
                    <tr>
                        <th>변경할 사원 사번</th>
                        <th>이름</th>
                        <th>부서</th>
                        <th>팀</th>
                        <th>직위</th>
                        <th>직책</th>
                        <th>근무 유형</th>
                        <th>계약 종료일</th>
                        <th style="min-width: 300px;">주소</th>
                        <th style="min-width: 50px;">삭제</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(employee, index) in employees" :key="index">
                        <td>
                            <input v-model="employee.employeeNumber"
                                :class="{ 'invalid-input': employee.invalid && !employee.employeeNumber }" required
                                placeholder="사원 번호"
                                :style="{ color: employee.invalid && !employee.employeeNumber ? 'red' : '' }">
                        </td>
                        <td>
                            <input v-model="employee.name"
                                :class="{ 'invalid-input': employee.invalid && !employee.name }" required placeholder="이름"
                                :style="{ color: employee.invalid && !employee.name ? 'red' : '' }">
                        </td>
                        <td>
                            <select v-model="employee.departmentId">
                                <option disabled value="">선택</option>
                                <option v-for="department in departments" :key="department.value"
                                    :value="department.value">
                                    {{ department.text }}
                                </option>
                            </select>
                        </td>
                        <td>
                            <select v-model="employee.teamId">
                                <option v-for="team in teams" :key="team.value" :value="team.value">
                                    {{ team.text }}
                                </option>
                            </select>
                        </td>
                        <td>
                            <select v-model="employee.positionId">
                                <option v-for="position in positions" :key="position.value" :value="position.value">
                                    {{ position.text }}
                                </option>
                            </select>
                        </td>
                        <td>
                            <select v-model="employee.dutiesId">
                                <option v-for="duty in dutiesList" :key="duty.value" :value="duty.value">
                                    {{ duty.text }}
                                </option>
                            </select>
                        </td>
                        <td>
                            <select v-model="employee.workType">
                                <option value="R">정규직</option>
                                <option value="C">계약직</option>
                            </select>
                        </td>
                        <td><input v-model="employee.contractEndDate" type="date" class="no-border" required></td>
                        <td style="min-width: 350px;">
                            <div class="address-container">
                                <div>
                                    <button class="searchBtn" @click="execDaumPostcode(employee)">검색</button>
                                    <input v-model="employee.zipCode" placeholder="우편 번호" style="width: 23%;" required
                                        readonly>
                                    <input v-model="employee.address1" placeholder="주소" style="width: 57%;" required
                                        readonly>
                                </div>
                                <div id="address-container2">
                                    <input v-model="employee.address2" placeholder="상세 주소" style="width: 82%;" required>
                                </div>
                            </div>
                        </td>
                        <td style="width: 60px;">
                            <button @click="removeEmployee(index)" class="deleteBtn">삭제</button>
                        </td>
                        <td style="width: 60px;"><button @click="removeEmployee(index)" class="deleteBtn">삭제</button></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="bottom">
            <div class="employee-count">
                추가된 사원 수 : {{ employees.length }}명
            </div>
            <button class="plusBtn" @click="addEmployee">
                <img src="@/assets/buttons/plus.png" alt="add button">
                Add row
            </button>
            <button class="submitBtn" @click="submitForm">
                <img src="@/assets/buttons/submit.png" alt="submit button">
                Submit
            </button>
        </div>
    </div>
    <div class="modal fade" id="guideModal" tabindex="-1" aria-labelledby="guideModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title" id="guideModalLabel">CSV 작성 가이드</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- Modal body -->
                <div class="modal-body">
                    <div class="example">
                        <img src="@/assets/HR/csvexample.png" alt="csv 예시">
                    </div>
                    <div class="example-content">
                        <hr>
                        <p>1. 성별은 M(남성), F(여성)으로 입력해주세요.</p>
                        <p>2. 입사일은 'yyyy-MM-dd'의 형태로 입력해주세요.</p>
                        <p>3. 입사 유형은 NEW(신입), EXPERIENCED(경력)으로 입력해주세요.</p>
                        <p>4. 근로 유형은 R(정규직), C(계약직)으로 입력해주세요.</p>
                        <p>5. 이외의 부서, 팀, 직위, 직책의 경우 숫자로 입력해주세요.</p>
                        <p>6. 엑셀 이외의 프로그램(메모장, 노트패드 등...)으로 편집시 쉼표(,)를 구분자로 사용하세요.</p>
                    </div>
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
                </div>

            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed, watch } from 'vue';
import Papa from 'papaparse';
import axios from 'axios';
import router from '@/router/router';
import { Modal } from 'bootstrap';

const employees = reactive([]);
const guideModal = ref(null);

const departments = ref([]);
const teams = ref([]);
const positions = ref([]);
const dutiesList = ref([]);
const fileInput = ref(null);

const fetchDropdownData = async () => {
    const departmentRes = await axios.get('http://localhost:8080/department/find-all');
    departments.value = departmentRes.data.result.map(d => ({ value: d.id, text: d.departmentName }));

    const teamRes = await axios.get('http://localhost:8080/team/find-all');
    teams.value = teamRes.data.result.map(t => ({ value: t.id, text: t.teamName }));

    const positionRes = await axios.get('http://localhost:8080/users/positions');
    positions.value = positionRes.data.result.map(p => ({ value: p.id, text: p.positionName }));

    const dutiesRes = await axios.get('http://localhost:8080/users/duties');
    dutiesList.value = dutiesRes.data.result.map(d => ({ value: d.id, text: d.dutiesName }));
};

const openModal = () => {
    if (guideModal.value) {
        guideModal.value.show();
    }
};

const addEmployee = () => {
    employees.push({
        employeeNumber: '',
        name:'',
        departmentId: null,
        teamId: null,
        positionId: null,
        dutiesId: null,
        workType: '',
        contractEndDate: '',
        address1: '',
        address2: '',
        invalid: false // Add invalid field for validation
    });
};

const removeEmployee = (index) => {
    employees.splice(index, 1);
};

const formattedEmployees = computed(() =>
    employees.map(emp => ({
        employeeNumber: emp.employeeNumber,
        name: emp.name,
        departmentId: emp.departmentId,
        teamId: emp.teamId,
        positionId: emp.positionId,
        dutiesId: emp.dutiesId,
        workType: emp.workType,
        contractEndDate: emp.contractEndDate,
        zipCode: emp.zipCode,
        address: emp.address1 ? `${emp.address1} ${emp.address2}`.trim() : null
    }))
);

const execDaumPostcode = (employee) => {
    new daum.Postcode({
        oncomplete: function (data) {
            let addr = '';
            let extraAddr = '';

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            if (data.userSelectedType === 'R') {
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
            }
            employee.zipCode = data.zonecode;
            employee.address1 = addr;
        }
    }).open();
};

const downloadCSV = () => {
    const csvData = [
        ['변경할 사원 사번', '이름', '부서', '팀', '직위', '직책', '근무 유형', '계약 종료일', '주소 1', '주소 2'],
        ...employees.map(emp => [
            emp.employeeNumber,
            emp.name,
            emp.departmentId,
            emp.teamId,
            emp.positionId,
            emp.dutiesId,
            emp.workType,
            emp.contractEndDate,
            emp.address1,
            emp.address2,
        ])
    ];
    const csv = Papa.unparse(csvData, {
        delimiter: ','
    });
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = 'employees.csv';
    link.click();
};

const handleFileUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
        Papa.parse(file, {
            header: true,
            complete: (results) => {
                console.log('Parsed CSV data:', results.data); // 디버깅을 위한 콘솔 출력
                results.data.forEach(row => {
                    console.log('Processing row:', row); // 디버깅을 위한 콘솔 출력
                    employees.push({
                        employeeNumber: (row['변경할 사원 사번'] || '').trim(),
                        name: (row['이름'] || '').trim(),
                        departmentId: (row['부서'] || '').trim(),
                        teamId: (row['팀'] || '').trim(),
                        positionId: (row['직위'] || '').trim(),
                        dutiesId: (row['직책'] || '').trim(),
                        workType: (row['근무 유형'] || '').trim(),
                        contractEndDate: (row['계약 종료일'] || '').trim(),
                        zipCode: (row['우편 번호'] || '').trim(),
                        address1: (row['주소'].split(' ')[0] || '').trim(),
                        address2: (row['상세 주소'] || ''),
                        invalid: false // Add invalid field for validation

                    });
                });
            }
        });
    }
};

const triggerFileUpload = () => {
    fileInput.value.click();
};

onMounted(() => {
    fetchDropdownData();
    guideModal.value = new Modal(document.getElementById('guideModal'));
});

watch(formattedEmployees, (newVal) => {
    console.log('Formatted employees changed:', newVal);
}, { deep: true });

const submitForm = async () => {

    let hasInvalid = false;
    let hasData = false;

    const cleanedEmployees = formattedEmployees.value
        .filter(emp => Object.values(emp).some(value => value !== null && value !== ''))
        .map(emp => {
            const cleanedEmp = {};
            Object.keys(emp).forEach(key => {
                if (emp[key] !== null && emp[key] !== '') {
                    cleanedEmp[key] = emp[key];
                }
            });
            return cleanedEmp;
        })
        .filter(emp => Object.values(emp).some(value => value !== null && value !== ''));
    
    if(cleanedEmployees.length == 0) {
        alert('수정할 데이터가 없습니다.');
        return;
    }

    employees.forEach(emp => {

        if (!emp.employeeNumber || !emp.name) {
            emp.invalid = true;
            hasInvalid = true;
        } else {
            emp.invalid = false;
        }
    }
    );

    if (hasInvalid) {
        alert('사번과 이름을 입력해주세요.');
        return;
    }

    try {
        console.log('보낼 데이터: ', cleanedEmployees)
        await axios.put('http://localhost:8080/users/list', cleanedEmployees);
        alert('수정에 성공하였습니다.');
        router.push('/hr');
    } catch (e) {
        const errorMessage = e.response && e.response.data && e.response.data.message ? e.response.data.message : e.message;
        alert('수정에 실패하였습니다. 입력 정보를 확인해주세요! : ' + errorMessage);
    }
};
</script>

<style scoped>
button {
    height: 100%;
}

.hr-main {
    display: grid;
    grid-template-columns: 10% 80% 10%;
    grid-template-rows: 18% 4% auto 3% 4%;
    height: 100%;
}

.hr-title {
    grid-column-start: 2;
    grid-column-end: 3;
    font-weight: 600;
    margin-top: 2%;
    color: #000000;
    display: grid;
    grid-template-columns: 3% 97%;
    align-items: center;
}

.hr-title h1 {
    margin-left: 0.5%;
    font-weight: 600;
    font-size: 25px;
}

.hr-icon {
    width: 80%;
    filter: invert(0%) sepia(64%) saturate(7%) hue-rotate(334deg) brightness(85%) contrast(101%);
}

.search {
    grid-row-start: 2;
    grid-column-start: 2;
    grid-template-columns: auto 5% 0.5% 10% 0.5% 14%;
    display: grid;
    justify-content: flex-end;
    height: 100%;
}

.guide {
    grid-column-start: 2;
    display: flex;
    justify-content: flex-end;
    align-items: center;
}

.guide img {
    width: 40%;
    height: 60%;
    margin-top: 15%;
    margin-right: 15%;
    filter: invert(41%) sepia(28%) saturate(1738%) hue-rotate(134deg) brightness(92%) contrast(94%);
    cursor: pointer;
}

.example {
    width: 100%;
    height: 100%;
    overflow-wrap: normal;
}

.example img {
    width: 100%;
}

.downloadBtn {
    grid-column-start: 4;
    margin: 3px;
    height: 100%;
    min-width: 80px;
    background-color: #088A85;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-size: 70%;
    font-style: bold;
    justify-self: flex-start;
    width: 100%;
}

.uploadBtn {
    grid-column-start: 6;
    margin: 3px;
    min-width: 114px;
    height: 100%;
    background-color: #088A85;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
    justify-self: flex-start;
    width: 100%;
}

input,
select {
    border: none;
    width: 100%;
    box-sizing: border-box;
    padding: 0.2rem;
    color: #000000;
    text-align: center;
}

input.no-border {
    border: none;
}

input:focus,
select:focus {
    outline: none;
    border: 1px solid #088A85;
}

select {
    background-color: transparent;
    -webkit-appearance: none;
    -moz-appearance: none;
    appearance: none;
    color: inherit;
    font: inherit;
    line-height: inherit;
    padding: 0;
    margin: 0;
    border: 1px solid transparent;
}

.tableContainer {
    grid-column-start: 2;
    grid-column-end: 3;
    margin-top: 20px;
    font-size: 10px;
    max-height: 100%;
    overflow-x: auto;
}

table {
    width: 100%;
    border-collapse: collapse;
}

th,
td {
    border: 1px solid #dddddd;
    text-align: center;
    vertical-align: middle;
    color: #000000;
    padding: 1rem;
    min-width: 80px;
    white-space: nowrap;
}

td {
    padding: 0.7% 0.3% 0.7% 0.3%;
}

th {
    background-color: #f2f2f2;
}

thead th {
    background-color: #f4f4f4;
    font-weight: bold;
    padding: 0.5% 0 0.5% 0;
}

.profile-image {
    width: 25px;
    height: 25px;
    border-radius: 50%;
    object-fit: cover;
}

.bottom {
    grid-row-start: 5;
    grid-column-start: 2;
    grid-template-columns: 20% auto 10% 0.5% 10%;
    display: grid;
}

.employee-count {
    grid-column-start: 1;
    font-size: 12px;
    font-weight: 600;
    display: flex;
    align-self: flex-end;
}

.plusBtn {
    grid-column-start: 3;
    background-color: #088A85;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-size: 70%;
    font-style: bold;
    justify-self: flex-start;
    width: 100%;
}

.submitBtn {
    grid-column-start: 5;
    background-color: #088A85;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-size: 70%;
    font-style: bold;
    justify-self: flex-start;
    width: 100%;
}

.deleteBtn {
    background-color: #088A85;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 90%;
    font-style: bold;
    min-height: 20px;
    min-width: 30px;
}

.searchBtn {
    background-color: #088A85;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 90%;
    font-style: bold;
    min-height: 20px;
    min-width: 40px;
    margin-bottom: 5px;
}

.downloadBtn img {
    width: 20%;
    height: 80%;
    margin: 0 6% 4% 0;
    filter: invert(100%) sepia(65%) saturate(424%) hue-rotate(91deg) brightness(129%) contrast(107%);
    transition: transform 0.3s ease;
}

.uploadBtn img {
    width: 12%;
    height: 50%;
    margin: 0 7% 2% 0;
    filter: invert(100%) sepia(65%) saturate(424%) hue-rotate(91deg) brightness(129%) contrast(107%);
}

.submitBtn img {
    width: 20%;
    height: 80%;
    margin: 0 6% 0 0;
    filter: invert(100%) sepia(65%) saturate(424%) hue-rotate(91deg) brightness(129%) contrast(107%);
    transition: transform 0.3s ease;
}

.plusBtn img {
    width: 14%;
    height: 60%;
    margin: 1% 3% 1% 0;
    filter: invert(100%) sepia(65%) saturate(424%) hue-rotate(91deg) brightness(129%) contrast(107%);
    transition: transform 0.3s ease;
}

.address-container input::placeholder {
    text-align: left;
}

.address-container input {
    text-align: left;
}

.invalid-input::placeholder {
    color: rgb(240, 125, 125);
}

</style>
