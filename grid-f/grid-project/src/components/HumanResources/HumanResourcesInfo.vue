<template>
    <div class="hr-main">
        <div class="hr-info-title">
            인사 정보
        </div>
        <div class="time">
            <i class="bi bi-clock" style="width: 12px; height: 12px; margin-bottom: 8px;"></i> &nbsp;
            {{ currentTime }}
            <div class="time-script" style="color: rgb(180, 177, 177);">현재</div>
        </div>
        <hr id="hr-1">
        <div class="hr-info-name">
            <div id="hr-info-name1" style="color: rgb(180, 177, 177);">
                <i class="bi bi-people " style="width: 12px; height: 12px;"></i> &nbsp; 
                부서 / 팀
            </div>
            <div id="hr-info-name2" style="color: rgb(180, 177, 177);">
                <i class="bi bi-briefcase-fill" style="width: 12px; height: 12px;"></i> &nbsp;
                담당 업무
            </div>
            <div id="hr-info-name3" style="color: rgb(180, 177, 177);">
                <i class="bi bi-grid" style="width: 12px; height: 12px;"></i> &nbsp;
                직위
            </div>
            <div id="hr-info-name4" style="color: rgb(180, 177, 177);">
                <i class="bi bi-grid" style="width: 12px; height: 12px;"></i> &nbsp;
                직책
            </div>
        </div>
        <div class="hr-info-content">
            <div id="hr-info-content1">
                {{ result.department ? result.department.departmentName : 'N/A' }} / {{ result.team ?
                    result.team.teamName : 'N/A' }}
            </div>
            <div id="hr-info-content2">
                {{ assignedTask }}
            </div>
            <div id="hr-info-content3">
                {{ result.position ? result.position.positionName : 'N/A' }}
            </div>
            <div id="hr-info-content4">
                {{ result.duties ? result.duties.dutiesName : 'N/A' }}
            </div>
        </div>
        <div class="basic-info-title">
            기본 정보
        </div>
        <hr id="hr-2">
        <div class="basic-info-name" style="color: rgb(180, 177, 177);">
            <div id="basic-info-name1">
                <i class="bi bi-file-person " style="width: 12px; height: 12px;"></i> &nbsp;
                이름
            </div>
            <div id="basic-info-name2">
                <i class="bi bi-circle-square" style="width: 12px; height: 12px;"></i> &nbsp;
                사번
            </div>
            <div id="basic-info-name3">
                <i class="bi bi-mailbox" style="width: 12px; height: 12px;"></i> &nbsp;
                이메일
            </div>
            <div id="basic-info-name4">
                <i class="bi bi-phone-vibrate" style="width: 12px; height: 12px;"></i> &nbsp;
                휴대전화번호
            </div>
            <div id="basic-info-name5">
                <i class="bi bi-telephone-inbound " style="width: 12px; height: 12px;"></i> &nbsp;
                전화번호
            </div>
            <div id="basic-info-name6">
                <i class="bi bi-calendar2-date " style="width: 12px; height: 12px;"></i> &nbsp;
                입사일
            </div>
        </div>
        <div class="basic-info-content">
            <div id="basic-info-content1">
                {{ result.name }}
            </div>
            <div id="basic-info-content2">
                {{ result.employeeNumber }}
            </div>
            <div id="basic-info-content3">
                {{ result.email }}
            </div>
            <div id="basic-info-content4">
                {{ result.phoneNumber }}
            </div>
            <div id="basic-info-content5">
                {{ callNumber }}
            </div>
            <div id="basic-info-content6">
                {{ result.joinTime }}
            </div>
        </div>
        <div class="admin-info-title" v-if="userRole === 'ROLE_ADMIN' || userId == result.id">
            관리 정보
        </div>
        <hr id="hr-3" v-if="userRole === 'ROLE_ADMIN' || userId == result.id">
        <div class="admin-info-name" v-if="userRole === 'ROLE_ADMIN' || userId == result.id"
            style="color: rgb(180, 177, 177);">
            <div id="admin-info-name1" style="min-height: 48px;">
                <i class="bi bi-pin-map" style="width: 12px; height: 12px;"></i> &nbsp;
                주소
            </div>
            <div id="admin-info-name2" style="margin-top: 15%;">
                <i class="bi bi-command " style="width: 12px; height: 12px;"></i> &nbsp;
                입사 유형
            </div>
            <div id="admin-info-name3">
                <i class="bi bi-command " style="width: 12px; height: 12px;"></i> &nbsp;
                고용 유형
            </div>
            <div id="admin-info-name4">
                <i class="bi bi-calendar2-date " style="width: 12px; height: 12px;"></i> &nbsp;
                계약 시작일
            </div>
            <div id="admin-info-name5">
                <i class="bi bi-calendar2-date " style="width: 12px; height: 12px;"></i> &nbsp;
                계약 종료일
            </div>
            <div id="admin-info-name6">
                <i class="bi bi-door-open " style="width: 12px; height: 12px;"></i> &nbsp;
                퇴사 여부
            </div>
        </div>
        <div class="admin-info-content" v-if="userRole === 'ROLE_ADMIN' || userId == result.id">
            <div id="admin-info-content1" style="min-height: 48px;">
                {{ result.zipCode }} <br>
                {{ result.address }}
            </div>
            <div id="admin-info-content2" style="margin-top: 7%;">
                {{ joinType }}
            </div>
            <div id="admin-info-content3">
                {{ workType }}
            </div>
            <div id="admin-info-content4">
                {{ contractStartTime }}
            </div>
            <div id="admin-info-content5">
                {{ contractEndTime }}
            </div>
            <div id="admin-info-content6">
                <b-badge v-if="isResigned === '퇴사'" variant="danger"> 퇴사:&nbsp; {{ resignTime }} </b-badge>
                <b-badge v-else variant="success"> 재직중 </b-badge>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps, onMounted, ref, watch } from 'vue';

const props = defineProps({
    result: {
        type: Object,
        required: true
    },
    userRole: {
        type: String,
        required: true
    },
    profileId: {
        type: String,
        required: true
    },
    userId: {
        type: String,
        required: true
    }
});

const updateValues = () => {

    joinType.value = props.result.joinType === 'NEW' ? '신입' : '경력';
    workType.value = props.result.workType === 'R' ? '정규직' : '계약직';
    isResigned.value = props.result.resignYn === 'N' ? '재직' : '퇴사';
    callNumber.value = props.result.callNumber != null ? props.result.callNumber : '-';
    assignedTask.value = props.result.assignedTask != null ? props.result.assignedTask : '-';
    resignTime.value = props.result.resignTime != null ? props.result.resignTime : '';
    contractStartTime.value = props.result.contractStartTime != null ? props.result.contractStartTime : '';
    contractEndTime.value = props.result.contractEndTime != null ? props.result.contractEndTime : '-';
};

const today = new Date();
const year = today.getFullYear();
const month = String(today.getMonth() + 1).padStart(2, '0');
const day = String(today.getDate()).padStart(2, '0');
const joinType = ref('');
const workType = ref('');
const isResigned = ref('');
const resignTime = ref('');
const contractStartTime = ref('');
const contractEndTime = ref('');
const callNumber = ref('');
const assignedTask = ref('');

const currentTime = `${year}-${month}-${day}`;

onMounted(updateValues);


watch(() => props.result, updateValues, { immediate: true });
watch(() => props.userRole, updateValues, { immediate: true });
</script>

<style scoped>
hr {
    margin-top: 1%;
}

.hr-main {
    display: grid;
    grid-template-columns: 35% 40% auto;
    grid-template-rows: 4% 2% auto 4% 2% auto 4% 2% auto;
    width: 100%;
    height: 100%;
    overflow: auto;
}

.hr-main::-webkit-scrollbar {
    display: none;
    /* Hide scrollbar for webkit browsers */
}

.hr-main {
    -ms-overflow-style: none;
    /* IE and Edge */
    scrollbar-width: none;
    /* Firefox */
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

.time-script {
    margin-left: 3%;
}

.info-img {
    width: 10%;
    filter: invert(73%) sepia(0%) saturate(377%) hue-rotate(189deg) brightness(101%) contrast(84%);
}

.hr-info-name {
    display: grid;
    grid-template-rows: auto auto auto auto;
    grid-row-start: 3;
    margin-top: 10px;
    height: 100%;
    width: 100%;
}

#hr-info-name1,
#hr-info-content1,
#basic-info-name1,
#basic-info-content1 {
    grid-row-start: 1;
    min-height: 47.76px;
}

#hr-info-name2,
#hr-info-content2,
#basic-info-name2,
#basic-info-content2 {
    grid-row-start: 2;
    min-height: 47.76px;
}


#hr-info-name3,
#hr-info-content3,
#basic-info-name3,
#basic-info-content3 {
    grid-row-start: 3;
    min-height: 47.76px;
}

#hr-info-name4,
#hr-info-content4,
#basic-info-name4,
#basic-info-content4 {
    grid-row-start: 4;
    min-height: 47.76px;
}

#basic-info-name5,
#basic-info-content5 {
    grid-row-start: 5;
    min-height: 47.76px;
}

#basic-info-name6,
#basic-info-content6 {
    grid-row-start: 6;
    min-height: 47.76px;
}

#admin-info-name1,
#admin-info-content1 {
    grid-row-start: 1;
    grid-row-end: 2;
    min-height: 80px;
}

#admin-info-name2,
#admin-info-content2 {
    grid-row-start: 3;
    min-height: 47.76px;
}

#admin-info-name3,
#admin-info-content3 {
    grid-row-start: 4;
    min-height: 47.76px;
}

#admin-info-name4,
#admin-info-content4 {
    grid-row-start: 5;
    min-height: 47.76px;
}

#admin-info-name5,
#admin-info-content5 {
    grid-row-start: 6;
    min-height: 47.76px;
}

#admin-info-name6,
#admin-info-content6 {
    grid-row-start: 7;
    min-height: 47.76px;
    margin-bottom: 20px;
}

.hr-info-content {
    display: grid;
    grid-template-rows: auto auto auto auto;
    grid-row-start: 3;
    grid-column-start: 2;
    margin-top: 10px;
    height: 100%;
    width: 100%;
}

#hr-info-content,
#basic-info-content,
#admin-info-content {
    margin-top: 1%;
    margin-bottom: 4.7%;
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
    grid-template-rows: auto auto auto auto auto auto;
    grid-row-start: 9;
    margin-top: 10px;
    height: 100%;
    width: 100%
}

.admin-info-content {
    display: grid;
    grid-template-rows: auto auto auto auto auto auto;
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
</style>
