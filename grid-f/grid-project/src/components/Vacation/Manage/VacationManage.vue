<template>
    <div class="manageAll">
        <div class="manageTitle">
            <h1 class="mb-1"><i class="bi bi-brightness-high fs-3"></i>&nbsp;휴가 종류&nbsp;&nbsp;<i class="bi bi-info-circle fs-5 custom-icon" v-if="userRole === 'ROLE_ADMIN'" id="popover-target-1" style="color: darkgoldenrod" @click="showModal('guideManage')"></i></h1>
            <button class="manageRegist btn-custom-2" type="button" @click="showModal('registVacation')"><span>등록하기</span></button>
            <button class="manageVacation btn-custom-3" type="button" @click="showModal('manageVacation')"><span>관리하기</span></button>
        </div>
        <div class="vacations">
            <div class="card mb-3" v-for="type in filteredTypes" :key="type.id">
                <div class="card-body">
                    <h3 class="card-title">{{ type.typeName }}</h3>
                    <p class="card-text">{{ type.vacationExplain }}</p>
                    <button href="#" v-if="userRole === 'ROLE_ADMIN'" @click="openModifyModal(type.id)"
                        class="btn btn-custom-1" :class="{ 'inactive-card-detail': type.useYn === 'N' }">
                        <span>살펴보기</span></button>
                </div>
            </div>
        </div>

        <!-- 등록 모달 -->
        <div class="modal fade" id="registVacation" tabindex="-1" aria-labelledby="exampleModalLabel"
            aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">휴가 정보 추가</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"
                            @click="closeModal('registVacation')"></button>
                    </div>
                    <div class="modal-body">
                        <form class="needs-validation" @submit.prevent="registType" novalidate>
                            <div class="mb-3">
                                <label for="typeName" class="form-label">휴가 이름</label>
                                <input type="text" class="form-control" id="typeName"
                                    v-model="registVacationType.typeName" required>
                                <div class="invalid-feedback">
                                    휴가 이름을 입력해주세요.
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="modifyVacationYes" class="form-label custom-label">시간체크</label>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="timeCheck" id="modifyVacationYes" v-model="registVacationType.timeCheck" value="Y" required>
                                    <label class="form-check-label" for="modifyVacationYes">Y</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="timeCheck" id="modifyVacationNo" v-model="registVacationType.timeCheck" value="N" required>
                                    <label class="form-check-label" for="modifyVacationNo">N</label>
                                </div>
                                <div class="invalid-feedback">
                                    시간체크 여부를 선택해주세요.
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="modifyVacationNum" class="form-label">휴가 제공시간</label>
                                <input type="text" class="form-control" id="modifyVacationNum" :disabled="registVacationType.timeCheck !== 'Y'"
                                    v-model="registVacationType.vacationTime" required>
                                <div class="invalid-feedback">
                                    휴가 제공시간을 입력해주세요.
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="vacationNum" class="form-label">휴가 제공일수</label>
                                <input type="text" class="form-control" id="vacationNum"
                                    v-model="registVacationType.vacationNum" required>
                                <div class="invalid-feedback">
                                    휴가 제공일수를 입력해주세요.
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="dateOfUse" class="form-label">휴가 사용기한</label>
                                <input type="text" class="form-control" id="dateOfUse"
                                    v-model="registVacationType.dateOfUse" required>
                                <div class="invalid-feedback">
                                    휴가 사용기한을 입력해주세요.
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="vacationExplain" class="form-label">휴가 설명</label>
                                <input type="text" class="form-control" id="vacationExplain"
                                    v-model="registVacationType.vacationExplain" required>
                                <div class="invalid-feedback">
                                    휴가 설명을 입력해주세요.
                                </div>
                            </div>
                            <div class="button-container">
                                <button type="submit" class="btn btn-primary">등록</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- 수정 모달 -->
        <div class="modal fade" id="modifyVacation" tabindex="-1" aria-labelledby="exampleModalLabel"
            aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">휴가 정보 수정</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"
                            @click="closeModal('modifyVacation')"></button>
                    </div>
                    <div class="modal-body">
                        <form class="needs-validation" @submit.prevent="modifyVacationType(modifyType.id)" novalidate>
                            <div class="mb-3">
                                <label for="modifyTypeName" class="form-label">휴가 이름</label>
                                <input type="text" class="form-control" id="modifyTypeName"
                                    v-model="modifyType.typeName" required>
                                <div class="invalid-feedback">
                                    휴가 이름을 입력해주세요.
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="modifyVacationYes" class="form-label custom-label">시간체크</label>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="timeCheck" id="modifyVacationYes" v-model="modifyType.timeCheck" value="Y" required>
                                    <label class="form-check-label" for="modifyVacationYes">Y</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="timeCheck" id="modifyVacationNo" v-model="modifyType.timeCheck" value="N" required>
                                    <label class="form-check-label" for="modifyVacationNo">N</label>
                                </div>
                                <div class="invalid-feedback">
                                    시간체크 여부를 선택해주세요.
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="modifyVacationNum" class="form-label">휴가 제공시간</label>
                                <input type="text" class="form-control" id="modifyVacationNum" :disabled="modifyType.timeCheck !== 'Y'"
                                    v-model="modifyType.vacationTime" required>
                                <div class="invalid-feedback">
                                    휴가 제공시간을 입력해주세요.
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="modifyVacationNum" class="form-label">휴가 제공일수</label>
                                <input type="text" class="form-control" id="modifyVacationNum"
                                    v-model="modifyType.vacationNum" required>
                                <div class="invalid-feedback">
                                    휴가 제공일수를 입력해주세요.
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="modifyDateOfUse" class="form-label">휴가 사용기한</label>
                                <input type="text" class="form-control" id="modifyDateOfUse"
                                    v-model="modifyType.dateOfUse" required>
                                <div class="invalid-feedback">
                                    휴가 사용기한을 입력해주세요.
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="modifyVacationExplain" class="form-label">휴가 설명</label>
                                <input type="text" class="form-control" id="modifyVacationExplain"
                                    v-model="modifyType.vacationExplain" required>
                                <div class="invalid-feedback">
                                    휴가 설명을 입력해주세요.
                                </div>
                            </div>
                            <div class="button-container">
                                <button type="submit" class="btn btn-primary"
                                    :disabled="modifyType.useYn === 'N'">수정</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- 관리 모달 -->
        <div class="modal fade" id="manageVacation" tabindex="-1" aria-labelledby="exampleModalLabel"
            aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">휴가 관리(활성/비활성)</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"
                            @click="closeModal('manageVacation')"></button>
                    </div>
                    <div class="modal-body">
                        <form class="needs-validation" novalidate>
                            <div class="mb-3">
                                <label class="form-label">휴가 타입 선택</label>
                                <div v-for="(type, index) in types" :key="type.id" class="form-check">
                                    <input class="form-check-input" type="checkbox" :id="'vacationType' + type.id"
                                        v-model="selectedVacationTypes" :value="type.id">
                                    <label class="form-check-label" :for="'vacationType' + type.id">
                                        {{ type.typeName }} <span v-if="type.useYn === 'N'"
                                            class="text-muted">(비활성화)</span>
                                    </label>
                                </div>
                            </div>
                            <div class="button-container d-flex justify-content-center gap-2">
                                <button type="button" class="btn btn-danger"
                                    @click="deactivateVacationTypes">비활성화</button>
                                <button type="button" class="btn btn-success"
                                    @click="activateVacationTypes">활성화</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!-- 가이드 모달 -->
        <div class="modal fade" id="guideManage" tabindex="-1" aria-labelledby="guideManageLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" id="guideManageLabel">휴가 종류 가이드</h4>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="example-content">
                            <p>여러가지 휴가의 종류를 관리할 수 있는 페이지 입니다. </p>
                            <p>현재 페이지는 관리자에게만 보입니다.</p>
                            <hr>
                            <p>1. 현재 표시되어 있는 휴가들은 활성화되어있는 휴가들입니다.</p>
                            <p>2. 새로운 휴가종류를 원하시면 <등록하기> 버튼을 통해 등록 가능합니다.</p>
                            <p>2-1. 모든 값을 채우고 등록한 후에 정책페이지로 이동하여 정책을 등록해주세요.</p>
                            <p>2-2. 정책을 등록하면 직원들이 정책을 확인하여 사내에 운영중인 휴가의 종류를 확인할 수 있습니다.</p>
                            <p>2-3. 시간체크의 값은 일 단위휴가(ex..연차,월차)는 N으로 두시고 시간 단위휴가(ex.. 반차,반반차)는 Y로 하시면 됩니다.</p>
                            <p>2-4. 시간체크의 값이 Y일때 휴가 제공시간을 입력가능하고, 몇시간을 제공하는지 입력해주시면 됩니다.</p>
                            <p>3. 사용을 멈추고싶은 휴가의 종류가 있다면 <관리하기> 버튼을 통하여 활성/비활성 할 수 있습니다.</p>
                            <p>3-1. 현재 비활성화 되어있는 휴가는 휴가이름옆에 (비활성화)로 표시되어 있습니다.</p>
                            <p>3-2. 비활성화 하면 휴가정책도 함께 사라집니다.</p>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
                    </div>

                </div>
            </div>
        </div>

    </div>
</template>

<script setup>
import { onBeforeMount, ref, onMounted, computed } from 'vue';
import axios from "axios";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

const types = ref([]);
const userRole = ref('');
const userId = ref('');
const policies = ref([]);
const allInfo = ref([]);
const selectedVacationTypes = ref([]);
const modifyType = ref({
    id: '',
    typeName: '',
    timeCheck:'',
    vacationTime:'',
    vacationNum: '',
    dateOfUse: '',
    vacationExplain: '',
    useYn: ''
});
const registVacationType = ref({
    id: '',
    typeName: '',
    timeCheck:'',
    vacationTime:'',
    vacationNum: '',
    dateOfUse: '',
    vacationExplain: ''
});

const showModal = (modalId) => {
    const modal = new bootstrap.Modal(document.getElementById(modalId));
    modal.show();
};

const filteredTypes = computed(() => {
    return types.value.filter(type => type.useYn === 'Y');
});

const openModifyModal = async (id) => {
    await getVacationType(id);
    const modal = new bootstrap.Modal(document.getElementById('modifyVacation'));
    modal.show();
};

const getVacationType = async (id) => {
    try {
        const response = await axios.get(`/api/vacation/type/${id}`);
        modifyType.value = response.data.result;
    } catch (error) {
        console.error("Error:", error);
    }
};

const closeModal = (modalId) => {
    const modal = new bootstrap.Modal(document.getElementById(modalId));
    modal.hide();
    if (modalId === 'registVacation') {
        registVacationType.value.typeName = '';
        registVacationType.value.timeCheck = '';
        registVacationType.value.vacationTime = '';
        registVacationType.value.vacationNum = '';
        registVacationType.value.dateOfUse = '';
        registVacationType.value.vacationExplain = '';
        const form = document.querySelector(`#${modalId} form`);
        if (form) {
            form.classList.remove('was-validated');
        }
    } else if (modalId === 'modifyVacation') {
        const form = document.querySelector(`#${modalId} form`);
        if (form) {
            form.classList.remove('was-validated');
        }
    } else if (modalId === 'manageVacation') {
        selectedVacationTypes.value = ([]);
        const form = document.querySelector(`#${modalId} form`);
        if (form) {
            form.classList.remove('was-validated');
        }
    }
};

const registType = async () => {
    if (!registVacationType.value.typeName || !registVacationType.value.vacationNum || !registVacationType.value.dateOfUse || !registVacationType.value.vacationExplain) {
        return;
    }

    const duplicateType = types.value.find(type => type.typeName === registVacationType.value.typeName);
    if (duplicateType) {
        alert('이미 등록된 휴가 종류입니다.');
        return;
    }

    try {
        const confirmed = window.confirm('등록하시겠습니까?');
        if (confirmed) {
            const modal = bootstrap.Modal.getInstance(document.getElementById('registVacation'));
            modal.hide();
            const response = await axios.post("/api/vacation/type",
                {
                    typeName: registVacationType.value.typeName,
                    timeCheck: registVacationType.value.timeCheck,
                    vacationTime:registVacationType.value.vacationTime,
                    vacationNum: registVacationType.value.vacationNum,
                    dateOfUse: registVacationType.value.dateOfUse,
                    vacationExplain: registVacationType.value.vacationExplain
                });
            alert('등록이 완료되었습니다.');
            window.location.reload();
        }
    } catch (error) {
        console.error("Error:", error);
    }
};

const modifyVacationType = async (id) => {
    if (!modifyType.value.id || !modifyType.value.typeName || !modifyType.value.vacationNum || !modifyType.value.dateOfUse || !modifyType.value.vacationExplain) {
        return;
    }
    try {
        const confirmed = window.confirm('수정하시겠습니까?');
        if (confirmed) {
            const modal = bootstrap.Modal.getInstance(document.getElementById('modifyVacation'));
            modal.hide();
            const response = await axios.put(`/api/vacation/type/${id}`,
                {
                    id: modifyType.value.id,
                    typeName: modifyType.value.typeName,
                    timeCheck: modifyType.value.timeCheck,
                    vacationTime:modifyType.value.vacationTime,
                    vacationNum: modifyType.value.vacationNum,
                    dateOfUse: modifyType.value.dateOfUse,
                    vacationExplain: modifyType.value.vacationExplain,
                    useYn: 'Y'
                });
            alert('수정이 완료되었습니다.');
            window.location.reload();
        }
    } catch (error) {
        console.error("Error:", error);
    }
};

const deleteVacationType = async (id) => {
    try {
        // 해당 휴가 타입에 연관된 정책이 있는지 확인
        const policiesResponse = await axios.get(`/api/vacation/policy/${id}`);
        const associatedPolicies = policiesResponse.data.result;

        if (associatedPolicies != null) {
            alert('해당 휴가 타입에 등록된 정책이 있어 비활성화할 수 없습니다.');
            return;
        }

        // 모든 휴가 정보 가져오기
        const vacationInfoResponse = await axios.get("/api/vacation/info");
        const allVacationInfo = vacationInfoResponse.data.result;

        // allVacationInfo 안에 삭제하려는 타입의 아이디가 있는지 확인
        const isTypeInUse = allVacationInfo.some(info => info.typeId === id);

        if (isTypeInUse) {
            alert('해당 휴가를 가지고있는 직원이 있어 비활성화할 수 없습니다.');
            return;
        }

        const confirmed = window.confirm('비활성화 하시겠습니까?');
        if (confirmed) {
            const response = await axios.put(`/api/vacation/type/${id}`,
                {
                    id: modifyType.value.id,
                    typeName: modifyType.value.typeName,
                    vacationNum: modifyType.value.vacationNum,
                    dateOfUse: modifyType.value.dateOfUse,
                    vacationExplain: modifyType.value.vacationExplain,
                    useYn: 'N'
                });
            alert('비활성화가 완료되었습니다.');
            window.location.reload();
        }

    } catch (error) {
        console.error("Error:", error);
    }
};

const changeUseYnToY = async (id) => {
    try {
        const confirmed = window.confirm('활성화 하시겠습니까?');
        if (confirmed) {
            const response = await axios.put(`/api/vacation/type/${id}`,
                {
                    id: modifyType.value.id,
                    typeName: modifyType.value.typeName,
                    vacationNum: modifyType.value.vacationNum,
                    dateOfUse: modifyType.value.dateOfUse,
                    vacationExplain: modifyType.value.vacationExplain,
                    useYn: 'Y'
                });
            alert('활성화가 완료되었습니다.');
            window.location.reload();
        }
    } catch (error) {
        console.error("Error:", error);
    }
}


const getAllVacationType = async () => {
    try {
        const response = await axios.get("/api/vacation/all/type");
        types.value = response.data.result;
    } catch (error) {
        console.error("Error:", error);
    }
};

function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } catch (error) {
        console.error('Invalid token', error);
        return null;
    }
}

const getAllVacationPolicy = async () => {
    try {
        const response = await axios.get('/api/vacation/policy/all');
        policies.value = response.data.result;
    } catch (error) {
        console.error('Error:', error);
    }
};

const deactivateVacationTypes = async () => {
    if (selectedVacationTypes.value.length === 0) {
        alert('비활성화할 항목을 선택해주세요.');
        return;
    }

    const confirmed = window.confirm('선택한 휴가 타입을 비활성화하시겠습니까?');
    if (!confirmed) return;

    for (const id of selectedVacationTypes.value) {
        try {
            const vacationType = types.value.find(type => type.id === id);
            if (vacationType && vacationType.useYn === 'Y') {
                await axios.put(`/api/vacation/type/${id}`, {
                    ...vacationType,
                    useYn: 'N'
                });
            } else if (vacationType && vacationType.useYn === 'N') {
                alert(`${vacationType.typeName}는 이미 비활성화되어 있습니다.`);
                return;
            }
        } catch (error) {
            console.error("Error:", error);
        }
    }
    alert('비활성화가 완료되었습니다.');
    window.location.reload();
};

const activateVacationTypes = async () => {
    if (selectedVacationTypes.value.length === 0) {
        alert('활성화할 항목을 선택해주세요.');
        return;
    }

    const confirmed = window.confirm('선택한 휴가 타입을 활성화하시겠습니까?');
    if (!confirmed) return;

    for (const id of selectedVacationTypes.value) {
        try {
            const vacationType = types.value.find(type => type.id === id);
            if (vacationType && vacationType.useYn === 'N') {
                await axios.put(`/api/vacation/type/${id}`, {
                    ...vacationType,
                    useYn: 'Y'
                });
            } else if (vacationType && vacationType.useYn === 'Y') {
                alert(`${vacationType.typeName}는 이미 활성화되어 있습니다.`);
                return;
            }
        } catch (error) {
            console.error("Error:", error);
        }
    }
    alert('활성화가 완료되었습니다.');
    window.location.reload();
};




onBeforeMount(() => {
    const token = localStorage.getItem('access');
    if (token) {
        const decodedToken = parseJwt(token);
        userRole.value = decodedToken?.auth || '';
        userId.value = decodedToken?.id || '';
    }
    getAllVacationType();
    getAllVacationPolicy();
});

onMounted(() => {
    'use strict';
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
});
</script>

<style scoped>
.manageAll {
    display: grid;
    grid-template-rows: 18% minmax(75%, auto) 5% 2%;
    grid-template-columns: 10% 80% 10%;
    height: 100%;
}

.manageTitle {
    grid-column-start: 2;
    grid-column-end: 3;
    font-size: 12px;
    font-weight: 600;
    margin-top: 2%;
    color: #000000;
    display: grid;
    grid-template-columns: 20% 64% 7% 2% 7%;
    align-items: center;
}

.custom-icon {
    cursor: pointer;
}

.manageTitle h1 {
    margin-left: 0.5%;
    margin-bottom: 0;
    font-size: 25px;
    font-weight: 600;
    font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.manageIcon {
    width: 80%;
}

.btn-custom-2 {
    background-color: white;
    color: #088A85;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.4s, color 0.4s;
    position: relative;
    overflow: hidden;
    font-size: 11px;
    font-weight: bold;
    grid-column-start: 3;
}

.btn-custom-2::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background-color: #088A85;
    transition: left 0.4s;
    z-index: 1;
}

.btn-custom-2:hover::before {
    left: 0;
}

.btn-custom-2 span {
    position: relative;
    z-index: 2;
    color: #088A85;
}

.btn-custom-2:hover span {
    color: white;
}


.btn-custom-3 {
    background-color: white;
    color: #088A85;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.4s, color 0.4s;
    position: relative;
    overflow: hidden;
    font-size: 11px;
    font-weight: bold;
    grid-column-start: 5;
}

.btn-custom-3::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background-color: #088A85;
    transition: left 0.4s;
    z-index: 1;
}

.btn-custom-3:hover::before {
    left: 0;
}

.btn-custom-3 span {
    position: relative;
    z-index: 2;
    color: #088A85;
}

.btn-custom-3:hover span {
    color: white;
}
        

.vacations {
    margin-top: 2%;
    grid-column-start: 2;
    grid-column-end: 3;
    grid-row: 2;
    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr;
    /* 3개의 열로 구성 */
    grid-auto-rows: 150px;
    /* 행의 최소 크기 설정 */
    grid-gap: 10px;
    /* 아이템 간의 간격 설정 */
    grid-auto-flow: row dense;
    /* 아이템을 빈 공간에 자동으로 채우도록 설정 */

}

.vacationsTitle {
    display: grid;
    grid-template-columns: 90% 10%;
    align-items: start;
    margin-top: 5%;
}

.plusBtn {
    width: 100%;
}

.typeBox {
    padding: 10px;
    background-color: #F2F2F2;
    display: grid;
    grid-template-rows: 75% 25%;
}

.vacationsNum {
    grid-column-start: 1;
    display: grid;
    grid-template-columns: 90% 10%;
    font-size: 10px;
}

.vacationsNum h3 {
    font-size: 12px;
    font-weight: 600;
}

/* 등록모달 css */
.registMain {
    height: 80%;
    width: calc(100% - 20px);
    padding: 10px;
    background-color: #F2F2F2;
}

.guide {
    width: 30px;
    height: 30px;
    grid-column: 3;
    margin-bottom: 6px;
    cursor: pointer;
}

.registMain textarea {
    width: 100%;
    height: 100%;
    padding: 10px;
    box-sizing: border-box;
    resize: none;
    height: 45px;
    border: none;
}

.registMain h3 {
    font-size: 15px;
    margin: 0;
    font-weight: 600;
}

.registTitle {
    margin-top: 2%;
    display: grid;
    grid-template-columns: 15% 30% 55%;
    font-size: 14px;
    align-items: center;
}

.registNum {
    margin-top: 2%;
    display: grid;
    grid-template-columns: 15% 30% 55%;
    font-size: 14px;
    align-items: center;
}

.registDesc {
    margin-top: 2%;
    display: grid;
    grid-template-columns: 15% 70% 15%;
    font-size: 14px;
    align-items: center;
}

.registPeriod {
    margin-top: 2%;
    display: grid;
    grid-template-columns: 15% 30% 55%;
    font-size: 14px;
    align-items: center;
}

.registBtnArea {
    display: grid;
    grid-template-columns: 40% 5% 10% 5% 40%;
    place-items: center;
    margin-top: 2%;
}

.registBtn {
    width: 100%;
    background-color: #088A85;
    color: white;
    padding: 5px 5px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
    grid-column-start: 3;
}

/* 등록모달 css */

/* 수정모달 css */
.modifyMain {
    height: 80%;
    width: calc(100% - 20px);
    padding: 10px;
    background-color: #F2F2F2;
}

.modifyMain textarea {
    width: 100%;
    height: 100%;
    padding: 10px;
    box-sizing: border-box;
    resize: none;
    height: 45px;
    border: none;
}

.modifyMain h3 {
    font-size: 15px;
    margin: 0;
    font-weight: 600;
}

.vacationType {
    margin-top: 2%;
    display: grid;
    grid-template-columns: 15% 30% 55%;
    font-size: 14px;
    align-items: center;
}

.titleContent {
    background-color: #F8F9FAFF;
    padding: 10px;
    margin: 0;
}

.modifyNum {
    margin-top: 2%;
    display: grid;
    grid-template-columns: 15% 30% 55%;
    font-size: 14px;
    align-items: center;
}

.modifyDesc {
    margin-top: 2%;
    display: grid;
    grid-template-columns: 15% 70% 15%;
    font-size: 14px;
    align-items: center;
}

.modifyPeriod {
    margin-top: 2%;
    display: grid;
    grid-template-columns: 15% 30% 55%;
    font-size: 14px;
    align-items: center;
}

.modifyBtnArea {
    display: grid;
    grid-template-columns: 39% 10% 2% 10% 39%;
    place-items: center;
    margin-top: 2%;
}

.modify {
    width: 20%;
    background-color: #088A85;
    color: white;
    padding: 5px 5px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
}

.delete {
    width: 20%;
    background-color: #088A85;
    color: white;
    padding: 5px 5px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    font-style: bold;
    margin-left: 2%;
}

.btn-primary {
    justify-content: center;
}

.btn-danger {
    margin-left: 2%;
}

.button-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
}

.btn-custom-1 {
    background-color: white;
    grid-row-start: 4;
    color: #088A85;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.4s, color 0.4s;
    position: relative;
    overflow: hidden;
    font-size: 11px;
    font-weight: bold;
}

.btn-custom-1::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background-color: #088A85;
    transition: left 0.4s;
    z-index: 1;
}

.btn-custom-1:hover::before {
    left: 0;
}

.btn-custom-1 span {
    position: relative;
    z-index: 2;
    color: #088A85;
}

.btn-custom-1:hover span{
    color: white;
}

.vacations h3 {
    font-size: 15px;
    font-weight: 600;
    color: black;
    margin: 0;
}

.card-text {
    font-size: 12px;
    font-weight: 600;
    color: black;
    margin-bottom: 0;
}

.card-body {
    width: 100%;
    padding: 0px 0px;
    display: grid;
    grid-template-rows: 0.7fr 1fr 0.1fr 1fr;
    margin: 0;
}

.card {
    padding: 10px 10px;
    border: 2px solid #a0a0a0;
}

.inactive-background {
    background-color: #fc3613;
    /* 비활성화된 항목에 적용할 배경색 */
    color: white;
}

.text-muted {
    color: #6c757d !important;
    /* 부트스트랩의 muted 색상 */
}

.custom-label {
    margin-right: 10px;
}
</style>
