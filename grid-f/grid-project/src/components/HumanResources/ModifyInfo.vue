<template>
    <div class="hr-main" v-if="user">
        <div class="hr-info-title">
            인사 정보
        </div>
        <hr id="hr-1">
        <div class="hr-info-name">
            <div id="hr-info-name1" style="color: rgb(180, 177, 177);">
                <i class="bi bi-people " style="width: 12px; height: 12px;"></i> &nbsp;
                부서
            </div>
            <div id="hr-info-name2" style="color: rgb(180, 177, 177);">
                <i class="bi bi-people " style="width: 12px; height: 12px;"></i> &nbsp;
                팀
            </div>
            <div id="hr-info-name3" style="color: rgb(180, 177, 177);">
                <i class="bi bi-briefcase-fill" style="width: 12px; height: 12px;"></i> &nbsp;
                담당 업무
            </div>
            <div id="hr-info-name4" style="color: rgb(180, 177, 177);">
                <i class="bi bi-grid" style="width: 12px; height: 12px;"></i> &nbsp;
                직위
            </div>
            <div id="hr-info-name5" style="color: rgb(180, 177, 177);">
                <i class="bi bi-grid" style="width: 12px; height: 12px;"></i> &nbsp;
                직책
            </div>
        </div>
        <div class="hr-info-content">
            <div id="hr-info-content1">
                {{ user.department ? user.department.departmentName : 'N/A' }}
            </div>
            <div id="hr-info-content2">
                {{ user.team ? user.team.teamName : 'N/A' }}
            </div>
            <div id="hr-info-content3">
                {{ assignedTask }}
            </div>
            <div id="hr-info-content4">
                {{ user.position ? user.position.positionName : 'N/A' }}
            </div>
            <div id="hr-info-content5">
                {{ user.duties ? user.duties.dutiesName : 'N/A' }}
            </div>
        </div>
        <div class="modify-hr-info">
            <div id="modify-hr-info1">
                <select v-if="userRole === 'ROLE_ADMIN'" v-model="newDepartmentId"
                    :class="{ modified: newDepartmentId !== user.department.id }">
                    <option v-for="dept in departmentInfo" :key="dept.id" :value="dept.id">{{ dept.departmentName }}
                    </option>
                </select>
            </div>
            <div id="modify-hr-info2">
                <select v-if="userRole === 'ROLE_ADMIN'" v-model="newTeamId"
                    :class="{ modified: newTeamId !== user.team.id }">
                    <option v-for="team in teamInfo" :key="team.id" :value="team.id">{{ team.teamName }}</option>
                </select>
            </div>
            <div id="modify-hr-info3">
                <input type="text" v-model="newAssignedTask"
                    :class="{ modified: newAssignedTask !== user.assignedTask }">
            </div>
            <div id="modify-hr-info4">
                <select v-if="userRole === 'ROLE_ADMIN'" v-model="newPositionId"
                    :class="{ modified: newPositionId !== user.position.id }">
                    <option v-for="position in positionInfo" :key="position.id" :value="position.id">{{
        position.positionName }}</option>
                </select>
            </div>
            <div id="modify-hr-info5">
                <select v-if="userRole === 'ROLE_ADMIN'" v-model="newDutiesId"
                    :class="{ modified: newDutiesId !== user.duties.id }">
                    <option v-for="duty in dutiesInfo" :key="duty.id" :value="duty.id">{{ duty.dutiesName }}</option>
                </select>
            </div>
        </div>
        <div class="basic-info-title">
            기본 정보
        </div>
        <hr id="hr-2">
        <div class="basic-info-name">
            <div id="basic-info-name1" style="color: rgb(180, 177, 177);">
                <i class="bi bi-file-person " style="width: 12px; height: 12px;"></i> &nbsp;
                이름
            </div>
            <div id="basic-info-name2" style="color: rgb(180, 177, 177);">
                <i class="bi bi-circle-square" style="width: 12px; height: 12px;"></i> &nbsp;
                사번
            </div>
            <div id="basic-info-name3" style="color: rgb(180, 177, 177);">
                <i class="bi bi-mailbox" style="width: 12px; height: 12px;"></i> &nbsp;
                이메일
            </div>
            <div id="basic-info-name4" style="color: rgb(180, 177, 177);">
                <i class="bi bi-phone-vibrate" style="width: 12px; height: 12px;"></i> &nbsp;
                휴대전화번호
            </div>
            <div id="basic-info-name5" style="color: rgb(180, 177, 177);">
                <i class="bi bi-telephone-inbound " style="width: 12px; height: 12px;"></i> &nbsp;
                전화번호
            </div>
            <div id="basic-info-name6" style="color: rgb(180, 177, 177);">
                <i class="bi bi-calendar2-date " style="width: 12px; height: 12px;"></i> &nbsp;
                입사일
            </div>
        </div>
        <div class="basic-info-content">
            <div id="basic-info-content1">
                {{ user.name }}
            </div>
            <div id="basic-info-content2">
                {{ user.employeeNumber }}
            </div>
            <div id="basic-info-content3">
                {{ user.email }}
            </div>
            <div id="basic-info-content4">
                {{ user.phoneNumber }}
            </div>
            <div id="basic-info-content5">
                {{ callNum }}
            </div>
            <div id="basic-info-content6">
                {{ user.joinTime }}
            </div>
        </div>
        <div class="modify-basic-info">
            <div id="modify-basic-info1">
                <input type="text" v-if="userRole === 'ROLE_ADMIN'" v-model="newName"
                    :class="{ modified: newName !== user.name }">
            </div>
            <div id="modify-basic-info2">
                <input v-if="userRole === 'ROLE_ADMIN'" type="text" v-model="newEmployeeNumber"
                    @blur="checkDuplicate('employeeNumber')"
                    :class="{ modified: newEmployeeNumber !== user.employeeNumber }">
                <span v-if="duplicateWarning.employeeNumber" class="warning">중복된 사번입니다.</span>
            </div>
            <div id="modify-basic-info3">
                <input v-if="userRole === 'ROLE_ADMIN'" type="text" v-model="newEmail" @blur="checkEmailValidity"
                    :class="{ modified: newEmail !== user.email }">
                <img v-if="duplicateWarning.email && newEmail != ''" src="@/assets/HR/fail.png" alt="실패 이미지"
                    style="width: 15px; height: 15px; margin: 0 0 1px 10px;">
            </div>
            <div id="modify-basic-info4">
                <input v-if="userRole === 'ROLE_ADMIN'" type="text" v-model="newPhoneNumber" @blur="checkPhoneValidity"
                    :class="{ modified: newPhoneNumber !== user.phoneNumber }">
                <img v-if="duplicateWarning.phoneNumber && newPhoneNumber != ''" src="@/assets/HR/fail.png" alt="실패 이미지"
                    style="width: 15px; height: 15px; margin: 0 0 1px 10px;">
            </div>
            <div id="modify-basic-info5">
                <input v-if="userRole === 'ROLE_ADMIN'" type="text" v-model="newCallNum" @blur="checkLandlineValidity"
                    :class="{ modified: newCallNum !== user.callNumber }">
                <img v-if="duplicateWarning.callNumber && newCallNum != ''" src="@/assets/HR/fail.png" alt="실패 이미지"
                    style="width: 15px; height: 15px; margin: 0 0 1px 10px;">
            </div>
            <div id="modify-admin-info6">
                <input v-if="userRole === 'ROLE_ADMIN'" v-model="newJoinTime"
                    :class="{ modified: newJoinTime !== user.joinTime }" type="date" required>
            </div>
        </div>
        <div class="admin-info-title">
            관리 정보
        </div>
        <hr id="hr-3">
        <div class="admin-info-name">
            <div id="admin-info-name1" style="color: rgb(180, 177, 177);">
                <i class="bi bi-pin-map" style="width: 12px; height: 12px;"></i> &nbsp;
                주소
            </div>
            <div id="admin-info-name2" style="color: rgb(180, 177, 177);">
                <i class="bi bi-command " style="width: 12px; height: 12px;"></i> &nbsp;
                입사 유형
            </div>
            <div id="admin-info-name3" style="color: rgb(180, 177, 177);">
                <i class="bi bi-command " style="width: 12px; height: 12px;"></i> &nbsp;
                고용 유형
            </div>
            <div id="admin-info-name4" style="color: rgb(180, 177, 177);">
                <i class="bi bi-calendar2-date " style="width: 12px; height: 12px;"></i> &nbsp;
                계약 시작일
            </div>
            <div id="admin-info-name5" style="color: rgb(180, 177, 177);">
                <i class="bi bi-calendar2-date " style="width: 12px; height: 12px;"></i> &nbsp;
                계약 종료일
            </div>
            <div id="admin-info-name6" style="color: rgb(180, 177, 177);">
                <i class="bi bi-door-open " style="width: 12px; height: 12px;"></i> &nbsp;
                퇴사 여부
            </div>
        </div>
        <div class="admin-info-content">
            <div id="admin-info-content1" style="padding: 0 15px 0 0">
                {{ user.zipCode }} <br>
                {{ address }}
            </div>
            <div id="admin-info-content2">
                {{ joinType }}
            </div>
            <div id="admin-info-content3">
                {{ workType }}
            </div>
            <div id="admin-info-content4">
                {{ user.contractStartTime }}
            </div>
            <div id="admin-info-content5">
                {{ contractEndTime }}
            </div>
            <div id="admin-info-content6">
                <b-badge v-if="user.resignYn === 'Y'" variant="danger"> 퇴사:&nbsp; {{ resignTime }} </b-badge>
                <b-badge v-else variant="light"> 재직 </b-badge>
            </div>
        </div>
        <div class="modify-admin-info">
            <div id="modify-admin-info1">
                <div id="modify-admin-info-sub1">
                    <button @click="execDaumPostcode" v-if="userRole === 'ROLE_ADMIN'">검색</button><br>
                    <input v-if="userRole === 'ROLE_ADMIN'" type="text" v-model="newZipCode" readonly
                        :class="{ modified: newZipCode !== user.zipCode }" placeholder="우편 번호" id="sub-1-1">
                    <input v-if="userRole === 'ROLE_ADMIN'" type="text" v-model="newAddress" readonly
                        :class="{ modified: newAddress !== user.address }" placeholder="기본 주소" id="sub-1-2">
                </div>
                <div id="modify-admin-info-sub2">
                    <input v-if="userRole === 'ROLE_ADMIN'" type="text" v-model="newAddressDetail" placeholder="상세 주소"
                        :class="{ modified: newAddressDetail !== '' }">
                </div>
            </div>
            <div id="modify-admin-info2">
                <select v-if="userRole === 'ROLE_ADMIN'" v-model="newJoinType"
                    :class="{ modified: newJoinType !== user.joinType }">
                    <option value="NEW">신입</option>
                    <option value="EXPERIENCED">경력</option>
                </select>
            </div>
            <div id="modify-admin-info3">
                <select v-if="userRole === 'ROLE_ADMIN'" v-model="newWorkType"
                    :class="{ modified: newWorkType !== user.workType }">
                    <option value="R">정규직</option>
                    <option value="C">계약직</option>
                </select>
            </div>
            <div id="modify-admin-info4">
                <input v-if="userRole === 'ROLE_ADMIN'" v-model="newContractStartTime" type="date" required>
            </div>
            <div id="modify-admin-info5">
                <input v-if="userRole === 'ROLE_ADMIN'" v-model="newContractEndTime" type="date" required>
            </div>
            <div id="modify-admin-info6">
                <button v-if="userRole === 'ROLE_ADMIN' && user.resignYn === 'N'"
                    @click="confirmResignation">퇴사</button>
                <button v-if="userRole === 'ROLE_ADMIN' && user.resignYn === 'Y'" @click="rejoin"
                    style="min-width: 50px; background-color: #088A85;">재입사</button>
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
    },
    userRole: {
        type: String,
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
const address = ref('');

const duplicateWarning = ref({
    employeeNumber: false,
    email: false,
    phoneNumber: false,
    callNumber: false // 추가
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

    callNum.value = user.callNumber != null ? user.callNumber : '-';
    assignedTask.value = user.assignedTask != null ? user.assignedTask : '-';
    joinType.value = user.joinType === 'NEW' ? '신입' : '경력';
    workType.value = user.workType === 'R' ? '정규직' : '계약직';
    isResigned.value = user.resignYn === 'Y';
    contractEndTime.value = user.contractEndTime != null ? user.contractEndTime : '-';
    address.value = user.address != null ? user.address : '-';
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
    const depRes = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/department/find-all');
    departmentInfo.value = depRes.data.result;

    const teamRes = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/team/find-all');
    teamInfo.value = teamRes.data.result;

    const dutiesRes = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/duties');
    dutiesInfo.value = dutiesRes.data.result;

    const positionRes = await axios.get('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/positions');
    positionInfo.value = positionRes.data.result;
});

const checkDuplicate = async (field) => {
    try {
        const response = await axios.post('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/duplication', { field, value: props.user[field] });
        duplicateWarning.value[field] = response.data.isDuplicate;
    } catch (error) {
        if (error.response) {
            console.error("중복 확인 중 오류 발생: ", error.response.data);
            alert("중복 확인 중 오류 발생: " + error.response.data);
        } else if (error.request) {
            console.error("서버로부터 응답을 받지 못했습니다. 네트워크 문제일 수 있습니다.");
        } else {
            console.error("오류가 발생했습니다: ", error.message);
        }
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

const rejoin = () => {
    if (confirm('재입사 처리 하시겠습니까?')) {
        rejoinUser();
    }
}

const resignUser = async () => {
    try {
        const response = await axios.put(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/${props.user.employeeNumber}/status`, {
            resignYn: 'Y',
            resignedTime: format(new Date(), 'yyyy-MM-dd')
        });
        alert('퇴사 처리 되었습니다.');
        router.push(`/hr/profile/${props.user.employeeNumber}`);

    } catch (error) {
        console.error("퇴사 처리 중 오류 발생: ", error);
    }
};

const rejoinUser = async () => {
    try {
        const response = await axios.put(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/${props.user.employeeNumber}/status`, {
            resignYn: 'N',
            resignedTime: null
        });
        alert('재입사 처리 되었습니다.');
        router.push(`/hr/profile/${props.user.employeeNumber}`);
    } catch (error) {
        console.error("재입사 처리 중 오류 발생: ", error);
    }
}

const emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const phoneRegExp = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;
const landlineRegExp = /^\d{2,3}-\d{3,4}-\d{4}$/;

const checkEmailValidity = () => {
    const emailTrimmed = newEmail.value.trim();
    if (!emailTrimmed || !emailRegExp.test(emailTrimmed)) {
        duplicateWarning.value.email = true;
    } else {
        duplicateWarning.value.email = false;
    }
};

const checkPhoneValidity = () => {
    const phoneTrimmed = newPhoneNumber.value.trim();
    if (!phoneTrimmed || !phoneRegExp.test(phoneTrimmed)) {
        duplicateWarning.value.phoneNumber = true;
    } else {
        duplicateWarning.value.phoneNumber = false;
    }
};

const checkLandlineValidity = () => {
    const callNumberTrimmed = newCallNum.value.trim();
    if (!callNumberTrimmed || !landlineRegExp.test(callNumberTrimmed)) {
        duplicateWarning.value.callNumber = true;
    } else {
        duplicateWarning.value.callNumber = false;
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

.warning {
    color: red;
    font-weight: bold;
    margin-left: 5px;
}

.hr-main {
    display: grid;
    grid-template-columns: 25% auto 35%;
    grid-template-rows: 4% 2% 30% 4% 2% 36% 4% 2% 42%;
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
    display: grid;
    grid-template-rows: auto auto auto auto auto;
    grid-row-start: 3;
    margin-top: 10px;
    height: 100%;
    width: 100%;
}

#hr-info-name1,
#hr-info-content1,
#modify-hr-info1,
#basic-info-name1,
#basic-info-content1,
#modify-basic-info1 {
    grid-row-start: 1;
    min-height: 47.76px;
}

#hr-info-name2,
#hr-info-content2,
#modify-hr-info2,
#basic-info-name2,
#basic-info-content2,
#modify-basic-info2 {
    grid-row-start: 2;
    min-height: 47.76px;
}

#hr-info-name3,
#hr-info-content3,
#modify-hr-info3,
#basic-info-name3,
#basic-info-content3,
#modify-basic-info3 {
    grid-row-start: 3;
    min-height: 47.76px;
}

#hr-info-name4,
#hr-info-content4,
#modify-hr-info4,
#basic-info-name4,
#basic-info-content4,
#modify-basic-info4 {
    grid-row-start: 4;
    min-height: 47.76px;
}

#hr-info-name5,
#hr-info-content5,
#modify-hr-info5,
#basic-info-name5,
#basic-info-content5,
#modify-basic-info5 {
    grid-row-start: 5;
    min-height: 47.76px;
}

#basic-info-name6,
#basic-info-content6,
#modify-basic-info6 {
    grid-row-start: 6;
    min-height: 47.76px;
}


#admin-info-name1,
#admin-info-content1,
#modify-admin-info1 {
    grid-row-start: 1;
    grid-row-end: 2;
    min-height: 80px;
}

#admin-info-name2,
#admin-info-content2,
#modify-admin-info2 {
    grid-row-start: 3;
    min-height: 47.76px;
}

#admin-info-name3,
#admin-info-content3,
#modify-admin-info3 {
    grid-row-start: 4;
    min-height: 47.76px;
}

#admin-info-name4,
#admin-info-content4,
#modify-admin-info4 {
    grid-row-start: 5;
    min-height: 47.76px;
}

#admin-info-name5,
#admin-info-content5,
#modify-admin-info5 {
    grid-row-start: 6;
    min-height: 47.76px;
}

#admin-info-name6,
#admin-info-content6,
#modify-admin-info6 {
    grid-row-start: 7;
    min-height: 47.76px;
}

.modify-admin-info input,
.modify-basic-info input,
.modify-hr-info input,
.modify-admin-info select,
.modify-basic-info select,
.modify-hr-info select {
    border: 0.5px solid;
    font-size: 13px;
}

.hr-info-content {
    display: grid;
    grid-template-rows: auto auto auto auto auto;
    grid-row-start: 3;
    grid-column-start: 2;
    margin-top: 10px;
    height: 100%;
    width: 100%;
}

.modify-hr-info {
    display: grid;
    grid-template-rows: auto auto auto auto auto;
    grid-row-start: 3;
    grid-column-start: 3;
    margin-top: 10px;
    height: 100%;
    width: 100%;
}

.modify-basic-info {
    display: grid;
    grid-template-rows: auto auto auto auto auto auto;
    grid-row-start: 6;
    grid-column-start: 3;
    margin-top: 10px;
    height: 100%;
    width: 100%;
}

.modify-admin-info {
    display: grid;
    grid-template-rows: auto auto auto auto auto auto auto;
    grid-row-start: 9;
    grid-column-start: 3;
    margin-top: 10px;
    height: 100%;
    width: 100%;
}

.modify-admin-info button {
    width: 10%;
    min-width: 38.8px;
    background-color: #df2517;
    color: white;
    padding: 5px 5px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
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
    min-width: 80px;
}

#sub-1-2 {
    min-width: 150px;
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
    display: grid;
    grid-template-rows: auto auto auto auto auto auto;
    grid-row-start: 6;
    margin-top: 10px;
    height: 100%;
    width: 100%
}

.basic-info-content {
    display: grid;
    grid-template-rows: auto auto auto auto auto auto;
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
    display: grid;
    grid-template-rows: auto auto auto auto auto auto auto;
    grid-row-start: 9;
    margin-top: 10px;
    height: 100%;
    width: 100%
}

.admin-info-content {
    display: grid;
    grid-template-rows: auto auto auto auto auto auto auto;
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

input,
select {
    border: solid 0.5px;
    border-radius: 4px;
}
</style>
