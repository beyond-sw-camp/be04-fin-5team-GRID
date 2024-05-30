<template>
    <div class="hr-main" v-if="user">
        <div class="hr-info-title">
            인사 정보
        </div>
        <hr id="hr-1">
        <div class="hr-info-name">
            <div id="hr-info-name">
                <img src="@/assets/HR/info-team.png" alt="부서 아이콘" class="info-img">
                부서
            </div>
            <div id="hr-info-name">
                <img src="@/assets/HR/info-team.png" alt="부서 아이콘" class="info-img">
                팀
            </div>
            <div id="hr-info-name">
                <img src="@/assets/HR/time.png" alt="시간 표시" class="info-img">
                담당 업무
            </div>
            <div id="hr-info-name">
                <img src="@/assets/HR/time.png" alt="시간 표시" class="info-img">
                직위
            </div>
            <div id="hr-info-name">
                <img src="@/assets/HR/time.png" alt="시간 표시" class="info-img">
                직책
            </div>
        </div>
        <div class="hr-info-content">
            <div id="hr-info-content">
                {{ user.department ? user.department.departmentName : 'N/A' }}
            </div>
            <div id="hr-info-content">
                {{ user.team ? user.team.teamName : 'N/A' }}
            </div>
            <div id="hr-info-content">
                {{ assignedTask }}
            </div>
            <div id="hr-info-content">
                {{ user.position ? user.position.positionName : 'N/A' }}
            </div>
            <div id="hr-info-content">
                {{ user.duties ? user.duties.dutiesName : 'N/A' }}
            </div>
        </div>
        <div class="modify-hr-info">
            <div id="modify-hr-info">
                <select v-model="newDepartmentId" :class="{ modified: newDepartmentId !== user.department.id }">
                    <option v-for="dept in departmentInfo" :key="dept.id" :value="dept.id">{{ dept.departmentName }}
                    </option>
                </select>
            </div>
            <div id="modify-hr-info">
                <select v-model="newTeamId" :class="{ modified: newTeamId !== user.team.id }">
                    <option v-for="team in teamInfo" :key="team.id" :value="team.id">{{ team.teamName }}</option>
                </select>
            </div>
            <div id="modify-hr-info">
                <input type="text" v-model="newAssignedTask"
                    :class="{ modified: newAssignedTask !== user.assignedTask }">
            </div>
            <div id="modify-hr-info">
                <select v-model="newPositionId" :class="{ modified: newPositionId !== user.position.id }">
                    <option v-for="position in positionInfo" :key="position.id" :value="position.id">{{
                        position.positionName }}</option>
                </select>
            </div>
            <div id="modify-hr-info">
                <select v-model="newDutiesId" :class="{ modified: newDutiesId !== user.duties.id }">
                    <option v-for="duty in dutiesInfo" :key="duty.id" :value="duty.id">{{ duty.dutiesName }}</option>
                </select>
            </div>
        </div>
        <div class="basic-info-title">
            기본 정보
        </div>
        <hr id="hr-2">
        <div class="basic-info-name">
            <div id="basic-info-name">
                <img src="@/assets/HR/info-team.png" alt="부서 아이콘" class="info-img">
                이름
            </div>
            <div id="basic-info-name">
                <img src="@/assets/HR/info-team.png" alt="부서 아이콘" class="info-img">
                사번
            </div>
            <div id="basic-info-name">
                <img src="@/assets/HR/info-team.png" alt="부서 아이콘" class="info-img">
                이메일
            </div>
            <div id="basic-info-name">
                <img src="@/assets/HR/info-team.png" alt="부서 아이콘" class="info-img">
                휴대전화번호
            </div>
            <div id="basic-info-name">
                <img src="@/assets/HR/info-team.png" alt="부서 아이콘" class="info-img">
                전화번호
            </div>
            <div id="basic-info-name">
                <img src="@/assets/HR/info-team.png" alt="부서 아이콘" class="info-img">
                입사일
            </div>
        </div>
        <div class="basic-info-content">
            <div id="basic-info-content">
                {{ user.name }}
            </div>
            <div id="basic-info-content">
                {{ user.employeeNumber }}
            </div>
            <div id="basic-info-content">
                {{ user.email }}
            </div>
            <div id="basic-info-content">
                {{ user.phoneNumber }}
            </div>
            <div id="basic-info-content">
                {{ callNum }}
            </div>
            <div id="basic-info-content">
                {{ user.joinTime }}
            </div>
        </div>
        <div class="modify-basic-info">
            <div id="modify-basic-info">
                <input type="text" v-model="newName" :class="{ modified: newName !== user.name }">
            </div>
            <div id="modify-basic-info">
                <input type="text" v-model="newEmployeeNumber" @blur="checkDuplicate('employeeNumber')"
                    :class="{ modified: newEmployeeNumber !== user.employeeNumber }">
                <span v-if="duplicateWarning.employeeNumber" class="warning">중복된 사번입니다.</span>
            </div>
            <div id="modify-basic-info">
                <input type="text" v-model="newEmail" @blur="checkDuplicate('email')"
                    :class="{ modified: newEmail !== user.email }">
                <span v-if="duplicateWarning.email" class="warning">중복된 이메일입니다.</span>
            </div>
            <div id="modify-basic-info">
                <input type="text" v-model="newPhoneNumber" @blur="checkDuplicate('phoneNumber')"
                    :class="{ modified: newPhoneNumber !== user.phoneNumber }">
                <span v-if="duplicateWarning.phoneNumber" class="warning">중복된 휴대전화번호입니다.</span>
            </div>
            <div id="modify-basic-info">
                <input type="text" v-model="newCallNum" :class="{ modified: newCallNum !== user.callNumber }">
            </div>
            <div id="modify-admin-info">
                <input v-model="newJoinTime" :class="{ modified: newJoinTime !== user.joinTime }" type="date" required>
            </div>
        </div>
        <div class="admin-info-title">
            관리 정보
        </div>
        <hr id="hr-3">
        <div class="admin-info-name">
            <div id="admin-info-name">
                <img src="@/assets/HR/info-team.png" alt="부서 아이콘" class="info-img">
                주소
            </div>
            <div id="admin-info-name" style="margin-top: 20%;">
                <img src="@/assets/HR/time.png" alt="시간 표시" class="info-img">
                입사 유형
            </div>
            <div id="admin-info-name">
                <img src="@/assets/HR/time.png" alt="시간 표시" class="info-img">
                고용 유형
            </div>
            <div id="admin-info-name">
                <img src="@/assets/HR/time.png" alt="시간 표시" class="info-img">
                계약 시작일
            </div>
            <div id="admin-info-name">
                <img src="@/assets/HR/time.png" alt="시간 표시" class="info-img">
                계약 종료일
            </div>
            <div id="admin-info-name">
                <img src="@/assets/HR/time.png" alt="시간 표시" class="info-img">
                퇴사 여부
            </div>
        </div>
        <div class="admin-info-content">
            <div id="admin-info-content">
                {{ user.zipCode }} <br>
                {{ user.address }}
            </div>
            <div id="admin-info-content" style="margin-top: 11%;">
                {{ joinType }}
            </div>
            <div id="admin-info-content">
                {{ workType }}
            </div>
            <div id="admin-info-content">
                {{ user.contractStartTime }}
            </div>
            <div id="admin-info-content">
                {{ contractEndTime }}
            </div>
            <div id="admin-info-content">
                {{ isResigned ? '퇴사' : '재직' }} &nbsp ({{ resignTime }})
            </div>
        </div>
        <div class="modify-admin-info">
            <div id="modify-admin-info">
                <div id="modify-admin-info-sub1">
                    <button @click="execDaumPostcode">검색</button><br>
                    <input type="text" v-model="newZipCode" readonly :class="{ modified: newZipCode !== user.zipCode }"
                    placeholder="우편 번호" id="sub-1-1">
                    <input type="text" v-model="newAddress" readonly :class="{ modified: newAddress !== user.address }"
                    placeholder="기본 주소" id="sub-1-2">
                </div>
                <div id="modify-admin-info-sub2">
                    <input type="text" v-model="newAddressDetail" placeholder="상세 주소"
                        :class="{ modified: newAddressDetail !== '' }">
                </div>
            </div>
            <div id="modify-admin-info">
                <select v-model="newJoinType" :class="{ modified: newJoinType !== user.joinType }">
                    <option value="NEW">신입</option>
                    <option value="EXPERIENCED">경력</option>
                </select>
            </div>
            <div id="modify-admin-info">
                <select v-model="newWorkType" :class="{ modified: newWorkType !== user.workType }">
                    <option value="R">정규직</option>
                    <option value="C">계약직</option>
                </select>
            </div>
            <div id="modify-admin-info">
                <input v-model="newContractStartTime" type="date" required>
            </div>
            <div id="modify-admin-info">
                <input v-model="newContractEndTime" type="date" required>
            </div>
            <div id="modify-admin-info">
                <button @click="confirmResignation">퇴사</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps, defineEmits, onMounted, ref, watch, computed } from 'vue';
import axios from 'axios';
import { format } from 'date-fns';
import router from '@/router/router';

const props = defineProps({
    user: {
        type: Object,
        required: true
    }
});

const emit = defineEmits(['update-user']);

const callNum = ref('');
const assignedTask = ref('');
const joinType = ref('');
const workType = ref('');
const isResigned = ref(false);
const resignTime = ref('');
const duplicateWarning = ref({
    employeeNumber: false,
    email: false,
    phoneNumber: false
});
const contractEndTime = ref('');

const departmentInfo = ref([]);
const teamInfo = ref([]);
const dutiesInfo = ref([]);
const positionInfo = ref([]);

const newDepartmentId = ref(null);
const newTeamId = ref(null);
const newAssignedTask = ref('');
const newPositionId = ref(null);
const newDutiesId = ref(null);
const newName = ref('');
const newEmployeeNumber = ref('');
const newEmail = ref('');
const newPhoneNumber = ref('');
const newCallNum = ref('');
const newJoinTime = ref('');
const newZipCode = ref('');
const newAddress = ref('');
const newAddressDetail = ref('');
const newJoinType = ref('');
const newWorkType = ref('');
const newIsResigned = ref('N');
const resignedTime = ref('');
const newContractEndTime = ref('');
const newContractStartTime = ref('');

const resultAddress = computed(() => `${newAddress.value} ${newAddressDetail.value}`);

const changeValues = (user) => {
    if (!user) return;
    console.log('props 확인: ', user);
    console.log('props 확인: ', user.name);

    callNum.value = user.callNumber != null ? user.callNumber : '-';
    assignedTask.value = user.assignedTask != null ? user.assignedTask : '-';
    joinType.value = user.joinType === 'NEW' ? '신입' : '경력';
    workType.value = user.workType === 'R' ? '정규직' : '계약직';
    isResigned.value = user.resignYn === 'Y';
    contractEndTime.value = user.contractEndTime != null ? user.contractEndTime : '-';

    if (user.resignTime != null) {
        resignTime.value = user.resignTime;
    }

    newDepartmentId.value = user.department ? user.department.id : 1;
    newTeamId.value = user.team ? user.team.id : 1;
    newAssignedTask.value = user.assignedTask;
    newPositionId.value = user.position ? user.position.id : 1;
    newDutiesId.value = user.duties ? user.duties.id : 1;
    newName.value = user.name;
    newEmployeeNumber.value = user.employeeNumber;
    newEmail.value = user.email;
    newPhoneNumber.value = user.phoneNumber;
    newCallNum.value = callNum.value;
    newJoinTime.value = user.joinTime;
    newJoinType.value = user.joinType;
    newWorkType.value = user.workType;
    newIsResigned.value = user.resignYn ? 'Y' : 'N';
    resignedTime.value = user.resignTime || '';
    newContractStartTime.value = user.contractStartTime;
    newContractEndTime.value = user.contractEndTime;
};

onMounted(async () => {
    changeValues(props.user);
    const depRes = await axios.get('http://localhost:8080/department/find-all');
    departmentInfo.value = depRes.data.result;
    console.log('부서정보:', depRes.data.result);

    const teamRes = await axios.get('http://localhost:8080/team/find-all');
    teamInfo.value = teamRes.data.result;

    const dutiesRes = await axios.get('http://localhost:8080/users/duties');
    dutiesInfo.value = dutiesRes.data.result;

    const positionRes = await axios.get('http://localhost:8080/users/positions');
    positionInfo.value = positionRes.data.result;
});

const checkDuplicate = async (field) => {
    try {
        const response = await axios.post('http://localhost:8080/user/duplication', { field, value: user[field] });
        duplicateWarning.value[field] = response.data.isDuplicate;
    } catch (error) {
        console.error("중복 확인 중 오류 발생: ", error);
    }
};

const execDaumPostcode = () => {
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
            newZipCode.value = data.zonecode;
            newAddress.value = addr;
        }
    }).open();
};

const confirmResignation = () => {
    if (confirm('퇴사 처리 하시겠습니까?')) {
        resignUser();
    }
};

const resignUser = async () => {
    try {
        const response = await axios.put(`http://localhost:8080/users/${props.user.employeeNumber}/status`, {
            resignYn: 'Y',
            resignedTime: format(new Date(), 'yyyy-MM-dd')
        });
        alert('퇴사 처리 되었습니다.');
        console.log('퇴사 처리 완료:', response.data);
        router.push(`/hr/profile/${props.user.employeeNumber}`);
        
    } catch (error) {
        console.error("퇴사 처리 중 오류 발생: ", error);
    }
};

watch(
    () => [
        newDepartmentId.value,
        newTeamId.value,
        newAssignedTask.value,
        newPositionId.value,
        newDutiesId.value,
        newName.value,
        newEmployeeNumber.value,
        newEmail.value,
        newPhoneNumber.value,
        newCallNum.value,
        newJoinTime.value,
        newZipCode.value,
        newAddress.value,
        newAddressDetail.value, // 추가
        newJoinType.value,
        newWorkType.value
    ],
    () => {
        const updatedUser = {
            id: '',
            email: newEmail.value || props.user.email,
            pwd: '',
            name: newName.value || props.user.name,
            employeeNumber: newEmployeeNumber.value || props.user.employeeNumber,
            phoneNumber: newPhoneNumber.value || props.user.phoneNumber,
            callNumber: newCallNum.value || props.user.callNumber,
            zipCode: newZipCode.value || props.user.zipCode,
            address: resultAddress.value || props.user.address, // 수정
            assignedTask: newAssignedTask.value || props.user.assignedTask,
            joinTime: newJoinTime.value || props.user.joinTime,
            joinType: newJoinType.value || props.user.joinType,
            workType: newWorkType.value || props.user.workType,
            departmentId: newDepartmentId.value || props.user.department.id,
            teamId: newTeamId.value || props.user.team.id,
            positionId: newPositionId.value || props.user.position.id,
            dutiesId: newDutiesId.value || props.user.duties.id
        };

        console.log('Updated user:', updatedUser); // 전체 값을 콘솔에 출력
        emit('update-user', updatedUser);
    }
);

watch(
    () => props.user,
    (newUser) => {
        changeValues(newUser);
    },
    { immediate: true }
);
</script>

<style scoped>
hr {
    margin-top: 1%;
}

.hr-main {
    display: grid;
    grid-template-columns: 25% auto 45%;
    grid-template-rows: 4% 2% auto 4% 2% auto 4% 2% auto;
    width: 100%;
    height: 100%;
}

.hr-info-title {
    grid-row-start: 1;
    height: 100%;
    width: 100%;
    color: rgb(180, 177, 177);
    display: flex;
    align-items: flex-end;
    margin-left: 5%
}

.time {
    grid-row-start: 1;
    grid-column-start: 3;
    justify-self: flex-end;
    display: flex;
    flex-direction: row;
    width: 100%;
    align-items: flex-end;
    justify-content: end;
    color: rgb(180, 177, 177);
}

.time img {
    width: 14px;
    height: 14px;
    margin-top: 1%;
    margin-right: 1%;
    margin-bottom: 1%;
    filter: invert(73%) sepia(0%) saturate(377%) hue-rotate(189deg) brightness(101%) contrast(84%);
}

.time-script {
    margin-left: 3%;
}

.info-img {
    width: 10%;
    filter: invert(73%) sepia(0%) saturate(377%) hue-rotate(189deg) brightness(101%) contrast(84%);
}

.hr-info-name {
    grid-row-start: 3;
    margin-top: 10px;
    height: 100%;
    width: 100%;
    justify-content: center;
}

#hr-info-name,
#basic-info-name,
#admin-info-name {
    margin-top: 3%;
    margin-bottom: 9%;
    margin-left: 15%;
    color: rgb(180, 177, 177);
}

.hr-info-content {
    grid-row-start: 3;
    grid-column-start: 2;
    margin-top: 10px;
    height: 100%;
    width: 100%;
}

.modify-hr-info {
    grid-row-start: 3;
    grid-column-start: 3;
    margin-top: 10px;
    height: 100%;
    width: 100%;
}

.modify-basic-info {
    grid-row-start: 6;
    grid-column-start: 3;
    margin-top: 10px;
    height: 100%;
    width: 100%;
}

.modify-admin-info {
    grid-row-start: 9;
    grid-column-start: 3;
    margin-top: 10px;
    height: 100%;
    width: 100%;
}

.modify-admin-info button {
    width: 10%;
    min-width: 36.8px;
    background-color: #df2517;
    color: white;
    padding: 5px 5px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
}

#hr-info-content,
#basic-info-content {
    margin-top: 1%;
    margin-bottom: 8.5%;
}

#admin-info-content {
    margin-top: 1%;
    margin-bottom: 7.5%;
}

#modify-hr-info,
#modify-basic-info {
    margin-top: 1%;
    margin-bottom: 4.5%;
}

#modify-admin-info {
    margin-top: 1%;
    margin-bottom: 4%;
}

#modify-admin-info-sub1 {
    display: flex;
    margin-bottom: 2%;
}

#modify-admin-info-sub1 button {
    width: 10%;
    min-width: 36.8px;
    background-color: #088A85;
    color: white;
    padding: 5px 5px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
}

#sub-1-1 {
    margin-left: 2%;
    margin-right: 2%;
    width: 23%;
    min-width: 95px;
}

#sub-1-2 {
    min-width: 220px;
    width: 100%;
}


#modify-admin-info-sub2 input {
    width: 100%;
    min-width: 300px; 
}

.basic-info-title {
    grid-row-start: 4;
    height: 100%;
    width: 100%;
    color: rgb(180, 177, 177);
    display: flex;
    align-items: flex-end;
    margin-left: 5%
}

.basic-info-name {
    grid-row-start: 6;
    margin-top: 10px;
    height: 100%;
    width: 100%
}

.basic-info-content {
    grid-row-start: 6;
    grid-column-start: 2;
    margin-top: 10px;
    height: 100%;
    width: 100%;
}

.admin-info-title {
    grid-row-start: 7;
    height: 100%;
    width: 100%;
    color: rgb(180, 177, 177);
    display: flex;
    align-items: flex-end;
    margin-left: 5%
}

.admin-info-name {
    grid-row-start: 9;
    margin-top: 10px;
    height: 100%;
    width: 100%
}

.admin-info-content {
    grid-row-start: 9;
    grid-column-start: 2;
    margin-top: 10px;
    height: 100%;
    width: 100%;
}

#hr-1 {
    grid-row-start: 2;
    grid-column-start: 1;
    grid-column-end: 4;
    margin-top: 5px;
    margin-bottom: 1.5px;
}

#hr-2 {
    grid-row-start: 5;
    grid-column-start: 1;
    grid-column-end: 4;
    margin-top: 5px;
    margin-bottom: 1.5px;
}

#hr-3 {
    grid-row-start: 8;
    grid-column-start: 1;
    grid-column-end: 4;
    margin-top: 5px;
    margin-bottom: 1.5px;
}

.modified {
    border: 2px solid #088A85;
}
</style>
