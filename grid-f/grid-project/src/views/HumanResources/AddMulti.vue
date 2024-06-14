<template>
    <div class="hr-main">
        <div class="hr-title">
            <nav class="title-nav" style="--bs-breadcrumb-divider: '>'; font-weight: normal;" aria-label="breadcrumb">
                <ol class="breadcrumb" style="margin-bottom: 0;">
                    <li class="breadcrumb-item"><a href="http://www.gridhr.site/hr"
                            style="text-decoration: none; color: grey; font-size: 17px;"><i
                                class="bi bi-people"></i>&nbsp; 인사
                            정보</a></li>
                    <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i
                                class="bi bi-pencil-square"></i>&nbsp; 신규 사원 등록</span></li>
                </ol>
            </nav>
            <div class="title-content">
                <h1> <i class="bi bi-person-plus"></i>&nbsp; 신규 사원 등록 &nbsp;&nbsp;<i class="bi bi-info-circle fs-5 mt-5" id="popover-target-1" style="color: darkgoldenrod" @click="openModal"></i></h1>
            </div>
        </div>
        <div class="search">
            <button class="downloadBtn" @click="downloadCSV">
                <i class="bi bi-download"></i> &nbsp
                작성 양식
            </button>
            <button class="uploadBtn" @click="triggerFileUpload">
                <i class="bi bi-upload"></i> &nbsp
                양식 적용
            </button>
            <input type="file" ref="fileInput" class="d-none" @change="handleFileUpload" accept=".csv" />
        </div>
        <div class="tableContainer">
            <table>
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>사번</th>
                        <th style="min-width: 140px;">이메일</th>
                        <th>성별</th>
                        <th style="min-width: 140px;">휴대폰 번호</th>
                        <th>입사일</th>
                        <th>입사 유형</th>
                        <th>근로 유형</th>
                        <th>계약 시작일</th>
                        <th>계약 종료일</th>
                        <th>부서</th>
                        <th>팀</th>
                        <th>직위</th>
                        <th>직책</th>
                        <th style="min-width: 300px;">주소</th>
                        <th style="min-width: 50px;">삭제</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(employee, index) in employees" :key="index">
                        <td>
                            <input v-model="employee.name" 
                                :class="{ 'invalid-input': employee.invalid && !employee.name }" required
                                placeholder="이름" :style="{ borderColor: employee.invalid && !employee.name ? 'red' : '' }">
                        </td>
                        <td>
                            <input v-model="employee.employeeNumber"
                                :class="{ 'invalid-input': employee.invalid && !employee.employeeNumber }" required
                                placeholder="사번"
                                :style="{ borderColor: employee.invalid && !employee.employeeNumber ? 'red' : '' }">
                        </td>
                        <td style="width: 140px;">
                            <div class="input-container">
                                <input v-model="employee.email" type="email" class="no-border" required
                                    placeholder="이메일" @blur="validateEmail(employee)"
                                    :class="{ 'invalid-input': employee.invalid && !employee.email }" style="width: 90%;">
                                <img v-if="employee.invalidEmail && employee.email != ''" src="@/assets/HR/fail.png"
                                    alt="실패 이미지" style="width:15px; height:15px;">
                            </div>
                        </td>
                        <td>
                            <select v-model="employee.gender"
                                :class="{ 'invalid-input': employee.invalid && !employee.gender }"
                                :style="{ borderColor: employee.invalid && !employee.gender ? 'red' : '' }"
                                style="min-height: 22.6px;">
                                <option disabled value="">선택</option>
                                <option value="M">남성</option>
                                <option value="F">여성</option>
                            </select>
                        </td>
                        <td style="width: 140px;">
                            <div class="input-container">
                                <input v-model="employee.phoneNumber" class="no-border" required placeholder="휴대폰 번호"
                                    @blur="validatePhoneNumber(employee)"
                                    :class="{ 'invalid-input': employee.invalid && !employee.phoneNumber }" style="width: 80%;">
                                <img v-if="employee.invalidPhoneNumber && employee.phoneNumber != ''"
                                    src="@/assets/HR/fail.png" alt="실패 이미지" style="width:15px; height:15px;">
                            </div>
                        </td>
                        <td>
                            <input v-model="employee.hireDate" type="date" class="no-border" required placeholder="입사일"
                                :class="{ 'invalid-input': employee.invalid && !employee.hireDate }">
                        </td>
                        <td>
                            <select v-model="employee.hireType"
                                :class="{ 'invalid-input': employee.invalid && !employee.hireType }"
                                :style="{ borderColor: employee.invalid && !employee.hireType ? 'red' : '' }"
                                style="min-height: 22.6px;">
                                <option disabled value="">선택</option>
                                <option value="NEW">신입</option>
                                <option value="EXPERIENCED">경력</option>
                            </select>
                        </td>
                        <td>
                            <select v-model="employee.workType"
                                :class="{ 'invalid-input': employee.invalid && !employee.workType }"
                                :style="{ borderColor: employee.invalid && !employee.workType ? 'red' : '' }"
                                style="min-height: 22.6px;">
                                <option disabled value="">선택</option>
                                <option value="R">정규직</option>
                                <option value="C">계약직</option>
                            </select>
                        </td>
                        <td>
                            <input v-model="employee.contractStartTime" type="date" class="no-border" required>
                        </td>
                        <td>
                            <input v-model="employee.contractEndTime" type="date" class="no-border" required>
                        </td>
                        <td>
                            <select v-model="employee.departmentId"
                                :class="{ 'invalid-input': employee.invalid && !employee.departmentId }"
                                :style="{ borderColor: employee.invalid && !employee.departmentId ? 'red' : '' }"
                                style="min-height: 22.6px;">
                                <option disabled value="">선택</option>
                                <option v-for="department in departments" :key="department.value"
                                    :value="department.value">
                                    {{ department.text }}
                                </option>
                            </select>
                        </td>
                        <td>
                            <select v-model="employee.teamId"
                                :class="{ 'invalid-input': employee.invalid && !employee.teamId }"
                                :style="{ borderColor: employee.invalid && !employee.teamId ? 'red' : '' }"
                                style="min-height: 22.6px;">
                                <option disabled value="">선택</option>
                                <option v-for="team in teams" :key="team.value" :value="team.value">
                                    {{ team.text }}
                                </option>
                            </select>
                        </td>
                        <td>
                            <select v-model="employee.positionId"
                                :class="{ 'invalid-input': employee.invalid && !employee.positionId }"
                                :style="{ borderColor: employee.invalid && !employee.positionId ? 'red' : '' }"
                                style="min-height: 22.6px;">
                                <option disabled value="">선택</option>
                                <option v-for="position in positions" :key="position.value" :value="position.value">
                                    {{ position.text }}
                                </option>
                            </select>
                        </td>
                        <td>
                            <select v-model="employee.dutiesId"
                                :class="{ 'invalid-input': employee.invalid && !employee.dutiesId }"
                                :style="{ borderColor: employee.invalid && !employee.dutiesId ? 'red' : '' }"
                                style="min-height: 22.6px;">
                                <option disabled value="">선택</option>
                                <option v-for="duty in dutiesList" :key="duty.value" :value="duty.value">
                                    {{ duty.text }}
                                </option>
                            </select>
                        </td>
                        <td style="min-width: 350px;">
                            <div class="address-container">
                                <div>
                                    <button class="searchBtn" @click="execDaumPostcode(employee)">검색</button>
                                    <input v-model="employee.zipCode" placeholder="우편 번호" style="width: 23%;" required
                                        readonly >
                                    <input v-model="employee.address1" placeholder="주소" style="width: 57%;" required
                                        readonly >
                                </div>
                                <div id="address-container2">
                                    <input v-model="employee.address2" placeholder="상세 주소" style="width: 82%;" required
                                        >
                                </div>
                            </div>
                        </td>
                        <td style="width: 60px;">
                            <button @click="removeEmployee(index)" class="deleteBtn">삭제</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="bottom">
            <div class="employee-count">
                추가된 사원 수 : {{ employees.length }}명
            </div>
            <button class="plusBtn" @click="addEmployee">
                <i class="bi bi-plus-circle"></i>&nbsp &nbsp;
                행 추가
            </button>
            <button class="submitBtn" @click="submitForm">
                <i class="bi bi-check2-circle"></i>&nbsp &nbsp;
                확인
            </button>
        </div>
    </div>
    <div class="modal fade" id="guideModal" tabindex="-1" aria-labelledby="guideModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title" id="guideModalLabel">사원 등록 가이드</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- Modal body -->
                <div class="modal-body">
                    <div class="example">
                        <img src="@/assets/HR/multi.png" alt="일괄 추가 설명">
                    </div>
                    <div class="example-content">
                        <hr>
                        <p>1. <행 추가> 버튼을 통해 새롭게 등록할 행을 추가할 수 있습니다. </p>
                        <p>2. <삭제> 버튼을 통해 기존에 추가된 행을 삭제할 수 있습니다.</p>
                        <p>3. <작성 양식> 버튼을 통해 csv작성에 필요한 기본 양식을 받을 수 있습니다. </p>
                        <p>3-1. 기본적인 csv파일의 작성 방법은 아래의 'CSV 작성 가이드'를 참고해주세요. </p>
                        <p>4. <양식 적용> 버튼을 통해 작성한 csv를 업로드하여 일괄 등록이 가능합니다. </p>
                        <p>5. 작성 정보를 확인 후, <확인> 버튼을 통해 신규 사원을 추가할 수 있습니다. </p>
                    </div>
                    <hr>
                    <br>
                    <h5>CSV 작성 가이드</h5>

                    <div class="example">
                        <img src="@/assets/HR/csvexample.png" alt="csv 예시">
                    </div>
                    <div class="example-content">
                        <hr>
                        <p>1. 성별은 M(남성), F(여성)으로 입력해주세요.</p>
                        <p>2. 휴대폰 번호는 '-'를 포함하여 입력해주세요. (ex 010-0000-0000)</p>
                        <p>3. 날짜는 'yyyy-MM-dd' 형태로 입력해주세요.(ex: 1999-12-31)</p>
                        <p>4. 입사 유형은 NEW(신입), EXPERIENCED(경력)으로 입력해주세요. </p>
                        <p>5. 근로 유형은 R(정규직), C(계약직)으로 입력해주세요. </p>
                        <p>6. 이외의 부서, 팀, 직위, 직책의 경우 숫자로 입력해주세요. </p>
                        <p>7. 엑셀 이외의 프로그램(메모장, 노트패드 등...)으로 편집시 쉼표(,)를 구분자로 사용하세요.</p>
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

const emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const phoneRegExp = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;

const validateEmail = (employee) => {
    const emailTrimmed = employee.email.trim();
    employee.invalidEmail = !emailTrimmed || !emailRegExp.test(emailTrimmed);
};

const validatePhoneNumber = (employee) => {
    const phoneTrimmed = employee.phoneNumber.trim();
    employee.invalidPhoneNumber = !phoneTrimmed || !phoneRegExp.test(phoneTrimmed);
};

const fetchDropdownData = async () => {
    const departmentRes = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/department/find-all');
    departments.value = departmentRes.data.result.map(d => ({ value: d.id, text: d.departmentName }));

    const teamRes = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/team/find-all');
    teams.value = teamRes.data.result.map(t => ({ value: t.id, text: t.teamName }));

    const positionRes = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/positions');
    positions.value = positionRes.data.result.map(p => ({ value: p.id, text: p.positionName }));

    const dutiesRes = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/duties');
    dutiesList.value = dutiesRes.data.result.map(d => ({ value: d.id, text: d.dutiesName }));
};

const openModal = () => {
    if (guideModal.value) {
        guideModal.value.show();
    }
};

const addEmployee = () => {
    employees.push({
        name: '',
        employeeNumber: '',
        email: '',
        gender: '',
        phoneNumber: '',
        hireDate: '',
        hireType: '',
        workType: '',
        contractStartTime: '',
        contractEndTime: '',
        departmentId: '',
        teamId: '',
        positionId: '',
        dutiesId: '',
        zipCode: '',
        address1: '',
        address2: '',
        invalid: false,
        invalidEmail: false,
        invalidPhoneNumber: false
    });
};

const removeEmployee = (index) => {
    employees.splice(index, 1);
};

const formattedEmployees = computed(() =>
    employees.map(emp => ({
        email: emp.email,
        pwd: emp.employeeNumber,
        name: emp.name,
        employeeNumber: emp.employeeNumber,
        gender: emp.gender,
        phoneNumber: emp.phoneNumber,
        joinTime: emp.hireDate,
        joinType: emp.hireType,
        workType: emp.workType,
        contractStartTime: emp.contractStartTime,
        contractEndTime: emp.contractEndTime,
        departmentId: emp.departmentId,
        teamId: emp.teamId,
        positionId: emp.positionId,
        dutiesId: emp.dutiesId,
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
        ['이름', '사번', '이메일', '성별', '휴대폰 번호', '입사일', '입사 유형', '근로 유형', '계약 시작일', '계약 종료일', '부서', '팀', '직위', '직책', '우편 번호', '도로명 주소', '상세 주소'],
        ...employees.map(emp => [
            emp.name,
            emp.employeeNumber,
            emp.email,
            emp.gender,
            emp.phoneNumber,
            emp.hireDate,
            emp.hireType,
            emp.workType,
            emp.contractStartTime,
            emp.contractEndTime,
            emp.departmentId,
            emp.teamId,
            emp.positionId,
            emp.dutiesId,
            emp.zipCode,
            emp.address1,
            emp.address2
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
                results.data.forEach(row => {
                    employees.push({
                        name: (row['이름'] || '').trim(),
                        employeeNumber: (row['사번'] || '').trim(),
                        email: (row['이메일'] || '').trim(),
                        gender: (row['성별'] || '').trim(),
                        phoneNumber: (row['휴대폰 번호'] || '').trim(),
                        hireDate: (row['입사일'] || '').trim(),
                        hireType: (row['입사 유형'] || '').trim(),
                        workType: (row['근로 유형'] || '').trim(),
                        contractStartTime: (row['계약 시작일'] || '').trim(),
                        contractEndTime: (row['계약 종료일'] || '').trim(),
                        departmentId: (row['부서'] || '').trim(),
                        teamId: (row['팀'] || '').trim(),
                        positionId: (row['직위'] || '').trim(),
                        dutiesId: (row['직책'] || '').trim(),
                        zipCode: (row['우편 번호'] || '').trim(),
                        address1: (row['도로명 주소'] || '').trim(),
                        address2: (row['상세 주소'] || '').trim(),
                        invalid: false,
                        invalidEmail: false,
                        invalidPhoneNumber: false
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
}, { deep: true });

const submitForm = async () => {
    let hasInvalid = false;

    employees.forEach(emp => {
        emp.invalid = false;

        if (!emp.name || !emp.employeeNumber || !emp.email || !emp.gender || !emp.phoneNumber || 
            !emp.hireDate || !emp.hireType || !emp.workType || !emp.departmentId || !emp.teamId || !emp.positionId || 
            !emp.dutiesId) {
            emp.invalid = true;
            hasInvalid = true;
        }

        validateEmail(emp);
        validatePhoneNumber(emp);

        if (emp.invalidEmail || emp.invalidPhoneNumber) {
            hasInvalid = true;
        }
    });

    if (hasInvalid) {
        alert('필수 정보를 전부 입력해주세요.');
        return;
    }

    const cleanedEmployees = formattedEmployees.value
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

    if (cleanedEmployees.length === 0) {
        alert('수정할 데이터를 입력해주세요.');
        return;
    }

    try {
        await axios.post('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/list', cleanedEmployees);
        alert('회원 등록이 성공하였습니다.');
        router.push('/hr');
    } catch (e) {
        const errorMessage = e.response && e.response.data && e.response.data.message ? e.response.data.message : e.message;
        alert('등록에 실패하였습니다. 입력 정보를 확인해주세요! : ' + errorMessage);
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
    align-items: center;
}

.hr-title h1 {
    font-weight: bold;
    font-size: 14pt;
}

.search {
    grid-row-start: 2;
    grid-column-start: 2;
    grid-template-columns: auto 5% 0.5% 8% 0.5% 9%;
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
    width: 22px;
    height: 22px;
    margin: 0 0 0 20px;
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
    min-width: 70px;
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
    min-width: 70px;
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
    grid-template-columns: 20% auto 8% 0.5% 6%;
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
    min-width: 70px;
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
    min-width: 55px;
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
    min-width: 30px;
    margin-right: 7%;
}

.address-container {
    display: flex;
    flex-direction: column;
}

#address-container2 {
    display: flex;
    justify-content: flex-end;
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

.title-content {
    display: flex;
    align-items: flex-start;
    height: 90%;
}

.invalid-input {
    border: 1px solid red !important;
}
</style>
