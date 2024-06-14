<template>
    <div class="profile-main">
        <div class="profile-title">
            <nav class="title-nav" style="--bs-breadcrumb-divider: '>'; font-weight: normal;" aria-label="breadcrumb">
                <ol class="breadcrumb" style="margin-bottom: 0;">
                    <li class="breadcrumb-item"><a href="http://www.gridhr.site/hr"
                            style="text-decoration: none; color: grey; font-size: 17px;"><i
                                class="bi bi-people"></i>&nbsp; 인사
                            정보</a></li>
                    <li class="breadcrumb-item"><a :href="`http://www.gridhr.site/hr/profile/${employeeNum}`"
                            style="text-decoration: none; color: grey; font-size: 17px;"><i
                                class="bi bi-file-person"></i>&nbsp; 인사 
                            정보 상세</a></li>
                    <li class="breadcrumb-item active" aria-current="page"><span class="fw-bolder"><i
                                class="bi bi-pencil-square"></i>&nbsp; 인사 정보 수정</span></li>
                </ol>
            </nav>
            <div class="title-content">
                <h1> <i class="bi bi-pencil-square"></i>&nbsp;인사 정보 수정&nbsp;&nbsp;<i class="bi bi-info-circle fs-5 mt-5" id="popover-target-1" style="color: darkgoldenrod" @click="openGuideModal"></i></h1>
            </div>
        </div>
        <div class="first" v-if="user">
            <div class="image">
                <img :src="profileUrl" alt="프로필 이미지" id="image">
            </div>
            <div class="sealImg">
                <img :src="sealUrl" alt="인감 이미지" id="sealImg">
            </div>
            <div class="button">
                <div style="margin-right: 2%">
                    <button class="custom-btn" data-bs-toggle="modal" data-bs-target="#myModal">
                        <i class="bi bi-key"></i>&nbsp; &nbsp;
                        <span>비밀번호 변경</span>
                    </button>
                </div>
                <div>
                    <button class="custom-btn" @click="submitModifications">
                        <i class="bi bi-gear"></i>&nbsp; &nbsp;
                        <span>수정</span>
                    </button>
                </div>
            </div>
        </div>
        <div class="half">
            <div style="margin: auto" id="profileInput">
                <button class="pwdBtn" @click="triggerFileUpload('fileInput')"><span>프로필 업로드</span></button>
                <input type="file" ref="fileInput" @change="uploadProfileImage" style="display: none;">
            </div>
            <div style="margin: auto" id="sealInput">
                <button class="pwdBtn" @click="triggerFileUpload('sealFileInput')"><span>인감 업로드</span></button>
                <input type="file" ref="sealFileInput" @change="uploadSealImage" style="display: none;">
            </div>
        </div>
        <div class="second">
            <div class="oldInfo">
                기존 정보
            </div>
            <div class="newInfo">
                변경 정보
            </div>
        </div>
        <div class="content">
            <ModifyInfo :user="user" :userRole="userRole" @update-user="updateUser" />
        </div>
    </div>
    <div class="modal fade" id="myModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">유저 비밀번호 변경</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <ResetPwd :givenEmail="givenEmail" @passwordResetSuccess="closeModal" />
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="guideModal" tabindex="-1" aria-labelledby="guideModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title" id="guideModalLabel">인사 정보 수정 가이드</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <!-- Modal body -->
                <div class="modal-body">
                    <div class="example">
                        <img src="@/assets/HR/modify.png" alt="hr modify example">
                    </div>
                    <div class="example-content">
                        <hr>
                        <p>1. <프로필 업로드>를 통해 프로필을 업로드 할 수 있습니다.</p>
                        <p>1-1. 프로필을 업로드 하는 경우 <수정>버튼을 누를 필요 없이 바로 적용됩니다.</p>
                        <p>2. <인감 업로드>를 통해 인감 이미지를 업로드 할 수 있습니다.</p>
                        <p>2-1. 인감 이미지를 업로드 하는 경우 <수정>버튼을 누를 필요 없이 바로 적용됩니다.</p>
                        <p>3. <비밀번호 변경>을 통해 비밀번호를 변경할 수 있습니다.</p>
                        <p>3-1. 비밀번호를 변경 하는 경우 <수정>버튼을 누를 필요 없이 바로 적용됩니다.</p>
                        <p>4. 인사 정보를 수정한 뒤, <수정>버튼을 눌러 정보를 수정할 수 있습니다.</p>
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
import ModifyInfo from '@/components/HumanResources/ModifyInfo.vue';
import ResetPwd from '@/components/Login/ResetPassword.vue';
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';
import defaultProfileImage from '@/assets/defaultProfile.jpg';
import defaultSealImage from '@/assets/defaultSeal.png';
import { Modal } from 'bootstrap';

const router = useRouter();
const route = useRoute();
const user = ref();
const updatedUser = ref(null);
const givenEmail = ref('');
const profilePath = ref('');
const sealPath = ref('');
const userRole = ref('');
const employeeNum = ref('');

const profileUrl = computed(() => {
    return profilePath.value ? `${profilePath.value}?t=${new Date().getTime()}` : defaultProfileImage;
});

const sealUrl = computed(() => {
    return sealPath.value ? `${sealPath.value}?t=${new Date().getTime()}` : defaultSealImage;
});

const fileInput = ref(null);
const sealFileInput = ref(null);
const guideModal = ref(null);

const cleanUserData = (userData) => {
    return {
        id: user.value.id,
        email: userData.email,
        pwd: userData.pwd,
        name: userData.name,
        employeeNumber: userData.employeeNumber,
        phoneNumber: userData.phoneNumber,
        callNumber: userData.callNumber,
        zipCode: userData.zipCode,
        address: userData.address,
        assignedTask: userData.assignedTask,
        joinTime: userData.joinTime,
        joinType: userData.joinType,
        workType: userData.workType,
        resignYn: userData.resignYn,
        resignedTime: userData.resignedTime,
        departmentId: userData.departmentId,
        teamId: userData.teamId,
        positionId: userData.positionId,
        dutiesId: userData.dutiesId,
    };
};

const triggerFileUpload = (inputType) => {
    const input = inputType === 'fileInput' ? fileInput.value : sealFileInput.value;
    if (input) {
        input.click();
    }
};

const uploadProfileImage = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);
    formData.append('id', user.value.id);
    formData.append('typeId', 2);

    try {
        const response = await axios.put('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/img', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });

        alert("프로필 이미지가 업로드 되었습니다.");
        profilePath.value = response.data.result;

    } catch (error) {
        console.error("이미지 업로드 중 오류 발생: ", error);
        alert("이미지 업로드 중 오류가 발생했습니다.");
    }
};

const uploadSealImage = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);
    formData.append('id', user.value.id);
    formData.append('typeId', 1);

    try {
        const response = await axios.put('http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/img', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });

        alert("인감 이미지가 업로드 되었습니다.");
        sealPath.value = response.data.result;

    } catch (error) {
        console.error("이미지 업로드 중 오류 발생: ", error);
        alert("이미지 업로드 중 오류가 발생했습니다.");
    }
};

const submitModifications = async () => {
    try {
        if (updatedUser.value.callNumber == '-') {
            updatedUser.value.callNumber = null;
        }

        const cleanedData = cleanUserData(updatedUser.value);
        const response = await axios.put(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/${user.value.id}`, cleanedData);
        alert("수정이 완료되었습니다.");
        router.push(`/hr/profile/${cleanedData.employeeNumber}`);
    } catch (error) {
        if (error.response && error.response.data && error.response.data.message) {
            console.error("수정 중 오류 발생: ", error.response.data.message);
            let errorMessage = error.response.data.message;
            if (errorMessage.length > 100) {
                errorMessage = errorMessage.substring(0, 100) + "...";
            }

            alert("수정 중 오류가 발생했습니다: " + errorMessage);
        } else {
            console.error("수정 중 오류 발생: ", error.message);
            alert("수정 중 오류가 발생했습니다: " + error.message);
        }
    }
};

const closeModal = () => {
    const modalElement = document.getElementById('myModal');
    const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);

    if (modalInstance) {
        modalInstance.hide();
    }

    const modalBackdrop = document.querySelector('.modal-backdrop');
    if (modalBackdrop) {
        modalBackdrop.remove();
    }

    document.body.classList.remove('modal-open');
    document.body.style = '';

    window.location.reload();
};

const openGuideModal = () => {
    if (!guideModal.value) {
        guideModal.value = new Modal(document.getElementById('guideModal'));
    }
    guideModal.value.show();
};

const updateUser = (newData) => {
    updatedUser.value = {
        ...updatedUser.value,
        ...newData,
        departmentId: newData.departmentId !== undefined ? newData.departmentId : user.value.department?.id,
        teamId: newData.teamId !== undefined ? newData.teamId : user.value.team?.id,
        positionId: newData.positionId !== undefined ? newData.positionId : user.value.position?.id,
        dutiesId: newData.dutiesId !== undefined ? newData.dutiesId : user.value.duties?.id,
    };

};

onMounted(() => {
    const userData = sessionStorage.getItem('user');
    if (userData) {
        const parsedUser = JSON.parse(userData);
        if (parsedUser && parsedUser.id) {
            user.value = parsedUser;
            employeeNum.value = user.value.employeeNumber;
            givenEmail.value = user.value.email;
            updatedUser.value = { ...user.value };
            sealPath.value = user.value.sealPath;
            profilePath.value = user.value.profilePath;
        } else {
            console.error("유저 정보가 올바르지 않습니다. ID가 없습니다.");
        }
    } else {
        console.error("SessionStorage에 유저 정보가 없습니다.");
    }

    userRole.value = route.query.userRole;
});
</script>

<style scoped>
@font-face {
    font-family: 'IBMPlexSansKR-Regular';
    src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}

body {
    font-family: 'IBMPlexSansKR-Regula';
    min-height: 100vh;
    min-width: 100vw;
}

.profile-main {
    display: grid;
    grid-template-columns: 10% 80% 10%;
    grid-template-rows: 18% 33% 8% 10% 140%;
    height: 100%;
}

.profile-title {
    grid-column-start: 2;
    grid-column-end: 3;
    margin-top: 2%;
    color: #000000;
    display: grid;
    align-items: center;
    grid-template-rows: 20% 80%;
}

.profile-title h1 {
    font-weight: bold;
    font-size: 14pt;
}

.profile-icon {
    width: 110%;
    margin: 0 40px 10px 0;
    filter: invert(0%) sepia(64%) saturate(7%) hue-rotate(334deg) brightness(85%) contrast(101%);
}

.first {
    grid-row-start: 2;
    grid-column-start: 2;
    display: grid;
    grid-template-columns: 20% 2% 20% 28% 30%;
    max-height: 100%;
    max-width: 100%;
}

.image {
    border-radius: 18%;
    grid-column-start: 1;
    padding: 0;
    height: 100%;
    width: 100%;
    overflow: hidden;
}

.image img {
    border-radius: 18%;
    width: 90%;
    height: 100%;
}

.sealImg {
    grid-column-start: 3;
    display: flex;
    align-items: flex-end;
    justify-content: center;
    overflow: hidden;
}

#sealImg {
    width: 50%;
    height: 50%;
    margin-bottom: 15%;
}

.name {
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    padding: 0.5em;
    height: 100%;
    width: 100%;
}

#name {
    margin-bottom: 1%;
    font-weight: bold;
}

#other-info {
    display: flex;
    flex-direction: row;
}

#teamInfo {
    font-size: 13px;
}

#dutiesInfo {
    font-size: 13px;
}

#dot {
    margin: auto 9px;
    font-size: 10px;
}

#absenceInfo {
    font-size: 13px;
}

.button {
    grid-column-start: 5;
    display: flex;
    justify-content: flex-end;
}

.custom-btn, .pwdBtn {
    background-color: white;
    color: #088A85;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.4s, color 0.4s;
    position: relative;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-left: 2%;
    width: 50%;
    min-width: 102.24px;
    min-height: 28px;
    font-size: 12px;
    font-weight: bold;
}

.custom-btn::before, .pwdBtn::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background-color: #088A85;
    transition: left 0.4s;
    z-index: 1; /* 기본 z-index로 설정 */
}

.custom-btn:hover::before, .pwdBtn:hover::before {
    left: 0;
}

.custom-btn span, .custom-btn i, .pwdBtn span, .pwdBtn i {
    position: relative;
    z-index: 2; /* 기본 z-index로 설정 */
    color: #088A85;
}

.custom-btn:hover span, .custom-btn:hover i, .pwdBtn:hover span, .pwdBtn:hover i {
    color: white;
}

.custom-btn:hover, .pwdBtn:hover {
    color: white;
}

.modifyFirst {
    grid-column-start: 3;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    padding: 0.5em;
    height: 100%;
    width: 100%;
}

.half {
    grid-row-start: 3;
    grid-column-start: 2;
    display: grid;
    grid-template-columns: 20% 2% 20% auto;
}

#profileInput {
    grid-column-start: 1;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
}

#sealInput {
    grid-column-start: 3;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
}

.second {
    grid-row-start: 4;
    grid-column-start: 2;
    height: 100%;
    width: 100%;
    display: grid;
    align-items: flex-end;
    grid-template-columns: 25% auto 45%;
}

.content {
    grid-row-start: 5;
    grid-column-start: 2;
    margin-top: 15px;
}

.modal-body {
    padding: 0 0 50px 0;
    height: 50%;
}

.oldInfo {
    grid-column-start: 2;
    text-align: center;
    color: rgb(180, 177, 177);
    font-weight: bold;
}

.newInfo {
    grid-column-start: 3;
    text-align: center;
    color: rgb(180, 177, 177);
    font-weight: bold;
}

.title-content {
    display: grid;
    grid-template-columns: 210px auto;
    align-items: flex-start;
    height: 80%;
}

.guide img {
    width: 21px;
    height: 21px;
    margin: 0 0 0 20px;
    cursor: pointer;
}

.example img {
    width: 100%;
}

.modal-footer .btn {
    background-color: #dc3545; /* 기본 모달 닫기 버튼 색상 */
    color: white; /* 닫기 버튼 글자 색상 */
}

.modal-footer .btn:hover {
    background-color: #c82333; /* 닫기 버튼 hover 색상 */
    color: white; /* 닫기 버튼 글자 색상 */
}
</style>
