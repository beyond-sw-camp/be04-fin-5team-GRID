<template>
  <div class="registAll">
    <div class="registHeader">
      <div>
        <h1 class="fw-bolder mb-1"><i class="bi bi-pencil-square"></i>&nbsp; 결재 작성 &nbsp;&nbsp;<i class="bi bi-info-circle fs-5 mt-5" id="popover-target-1" style="color: darkgoldenrod"></i></h1>
        <b-popover target="popover-target-1" triggers="hover" placement="right">
          <template #title><i class="bi bi-info-circle sm"></i>&nbsp;&nbsp; 결재 취소 시 유의사항</template>
          미열람 상태의 결재를 취소하게 되면 결재가 회수되어 바로 취소 처리되지만 승인 상태의 결재를 취소하게 되면 취소 결재가 생성되어 생성된 취소 결재를 승인 받은 후에만 취소 처리가 됩니다.
        </b-popover>
      </div>
    </div>
    <div class="registContent">
      <div class="container py-3 mx-0 px-0" id="featured-3">
        <div class="row py-5 row-cols-1 row-cols-lg-4">
          <div class="feature col">
            <b-card class="mx-1 card-custom">
              <div
                  class="feature-icon d-inline-flex align-items-center justify-content-center text-bg bg-gradient fs-2 mb-3 px-1">
                <i class="bi bi-briefcase"></i>
              </div>
              <h3>출장</h3>
              <p>출장 결재를 작성할 때는 출장의 목적, 기간, 경로 및 예상 비용을 명확히 기재해 주세요. 승인 절차를 원활하게 진행하기 위해 필요한 모든 세부 정보를 빠짐없이 포함해야 합니다.</p>
              <div class="button-wrapper">
                <button class="btn btn-primary btn-sm btn-custom-1" @click="navigateTO('bt')"><span>작성하기</span></button>
              </div>
            </b-card>
          </div>
          <div class="feature col">
            <b-card class="mx-1 card-custom">
              <div
                  class="feature-icon d-inline-flex align-items-center justify-content-center text-bg bg-gradient fs-2 mb-3 px-1">
                <i class="bi bi-clock"></i>
              </div>
              <h3>시간 외 근무</h3>
              <p>시간 외 근무는 평일 오후 7시부터 익일 오전 6시까지, 주말 모든 시간 가능합니다. 주별 시간 외 근무 합계는 12시간을 넘을 수 없으며, 이를 준수하지 않을 경우 결재 제출이 불가하니 유의하시기 바랍니다.</p>
              <div v-if="user['gender'] === 'P'" class="button-wrapper" tabindex="0" data-bs-toggle="tooltip" title="임신 중인 직원은 시간 외 근무를 신청할 수 없습니다.">
                <button class="btn btn-primary btn-sm btn-custom-1 disabled" @click="navigateTO('overtime')"><span>작성하기</span></button>
              </div>
              <div v-else class="button-wrapper">
                <button class="btn btn-primary btn-sm btn-custom-1" @click="navigateTO('overtime')"><span>작성하기</span></button>
              </div>
            </b-card>
          </div>
          <div class="feature col">
            <b-card class="mx-1 card-custom">
              <div
                  class="feature-icon d-inline-flex align-items-center justify-content-center text-bg bg-gradient fs-2 mb-3 px-1">
                <i class="bi bi-heart-pulse"></i>
              </div>
              <h3>단축 근무</h3>
              <p>단축 근무 신청 결재는 임신 중인 직원만 제출 가능하며, 승인 시 신청 기간동안 1일 2시간씩 근무 시간이 단축됩니다. 원활한 승인 절차를 위해 결재 제출 시 증명 서류 첨부가 필요합니다.</p>
              <div v-if="user['gender'] === 'F'" class="button-wrapper">
                <button class="btn btn-primary btn-sm btn-custom-1" @click="navigateTO('rw')"><span>작성하기</span></button>
              </div>
              <div v-else class="button-wrapper" tabindex="0" data-bs-toggle="tooltip" title="단축 근무 신청 대상자가 아닙니다.">
                <button class="btn btn-primary btn-sm btn-custom-1 disabled" @click="navigateTO('rw')"><span>작성하기</span></button>
              </div>
            </b-card>
          </div>
          <div class="feature col">
            <b-card class="mx-1 card-custom">
              <div
                  class="feature-icon d-inline-flex align-items-center justify-content-center text-bg bg-gradient fs-2 mb-3 px-1">
                <i class="bi bi-brightness-high"></i>
              </div>
              <h3>휴가</h3>
              <p>휴가 결재를 작성할 때, 여러 종류의 휴가를 동시에 사용하려면 각각 별도의 결재를 작성해야 합니다. 정기 휴가, 연차, 월차는 자신이 보유한 휴가 일수를 초과하여 사용할 수 없습니다.</p>
              <div class="button-wrapper">
                <button class="btn btn-primary btn-sm btn-custom-1" @click="navigateTO('vacation')"><span>작성하기</span></button>
              </div>
            </b-card>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import {useStore} from "vuex";
import {computed} from "vue";

const router = useRouter();

const store = useStore();

const user = computed(() => store.state.user);

const navigateTO = (path) => {
  router.push(`/regist/${path}`);
}
</script>

<style scoped>
.registAll {
  display: grid;
  grid-template-rows: 18% 74% 8%;
  grid-template-columns: 10% 80% 10%;
  height: 100%;
}

.registHeader {
  grid-column-start: 2;
  align-content: center;
  margin-top: 2%;
  margin-left: -0.5%;
  color: #000000;
}

.registContent {
  grid-column-start: 2;
  grid-row-start: 2;
}

.registHeader h1 {
  margin-left: 0.5%;
  font-size: 25px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.card-custom {
  height: 300px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.mx-1 h3 {
  margin-left: 0.5%;
  margin: 0;
  margin-bottom: 8%;
  font-size: 20px;
  font-weight: 600;
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
}

.mx-1 p {
  margin-left: 0.5%;
  margin-bottom: 10%;
  font-size: 13px;
  font-weight: 300;
  flex-grow: 1;
}

.button-wrapper {
  display: flex;
  justify-content: flex-start;
  align-items: flex-end;
  padding-bottom: 10px;
}

.btn-primary {
  grid-column-start: 6;
  margin-left: 2%;
  width: 100%;
  background-color: #088A85;
  color: white;
  padding: 5px 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.btn-custom-1 {
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

.btn-custom-1:hover span {
  color: white;
}

.feature {
  max-width: 230px;
  max-height: 270px;
  min-width: 250px;
  min-height: 280px;
}
</style>
