<script setup>
  import {useRouter} from "vue-router";

  const router = useRouter();

  const navigateTO = (path) => {
    router.push(`/regist/${path}`);
  }

</script>

<template>
  <div><h3 class="fw-bolder"><i class="bi bi-pencil-square"></i>&nbsp; 결재 작성</h3></div>
  <div class="container py-3 mx-0 px-0" id="featured-3">
    <div class="row py-5 row-cols-1 row-cols-lg-4">
      <div class="feature col">
        <b-card class="mx-1">
          <div class="feature-icon d-inline-flex align-items-center justify-content-center text-bg bg-gradient fs-2 mb-3 px-1">
            <i class="bi bi-briefcase"></i>
          </div>
          <h3 class="fs-1 text-body-emphasis py-3">출장</h3>
          <p>출장 결재를 작성할 때는 출장의 목적, 기간, 경로 및 예상 비용을 명확히 기재해 주세요. 승인 절차를 원활하게 진행하기 위해 필요한 모든 세부 정보를 빠짐없이 포함해야 합니다.<br><br></p>
          <br><button class="btn btn-primary btn-sm" @click="navigateTO('bt')">작성하기</button>
        </b-card>
      </div>
      <div class="feature col">
        <b-card class="mx-1">
          <div class="feature-icon d-inline-flex align-items-center justify-content-center text-bg bg-gradient fs-2 mb-3 px-1">
            <i class="bi bi-clock"></i>
          </div>
        `<h3 class="fs-1 text-body-emphasis py-3">시간 외 근무</h3>
          <p>시간 외 근무는 평일 오후 7시부터 익일 오전 6시까지, 주말 모든 시간 가능합니다. 주별 시간 외 근무 합계는 12시간을 넘을 수 없으며, 이를 준수하지 않을 경우 결재 제출이 불가하니 유의하시기 바랍니다.</p>
          <br><button class="btn btn-primary btn-sm" @click="navigateTO('overtime')">작성하기</button>
        </b-card>
      </div>
      <div class="feature col">
        <b-card class="mx-1">
          <div class="feature-icon d-inline-flex align-items-center justify-content-center text-bg bg-gradient fs-2 mb-3 px-1">
            <i class="bi bi-heart-pulse"></i>
          </div>
          <h3 class="fs-1 text-body-emphasis py-3">단축 근무</h3>
          <p>단축 근무 신청 결재는 임신 중인 직원만 제출 가능하며, 승인 시 신청 기간동안 1일 2시간씩 근무 시간이 단축됩니다. 원활한 승인 절차를 위해 결재 제출 시 증명 서류 첨부가 필요합니다.<br><br></p>
          <br><button class="btn btn-primary btn-sm" @click="navigateTO('rw')">작성하기</button>
        </b-card>
      </div>
      <div class="feature col">
        <b-card class="mx-1">
          <div class="feature-icon d-inline-flex align-items-center justify-content-center text-bg bg-gradient fs-2 mb-3 px-1">
            <i class="bi bi-brightness-high"></i>
          </div>
          <h3 class="fs-1 text-body-emphasis py-3">휴가</h3>
          <p>휴가 결재를 작성할 때, 여러 종류의 휴가를 동시에 사용하려면 각각 별도의 결재를 작성해야 합니다. 정기 휴가, 연차, 월차는 자신이 보유한 휴가 일수를 초과하여 사용할 수 없습니다.<br><br></p>
          <br><button class="btn btn-primary btn-sm" @click="navigateTO('vacation')">작성하기</button>
        </b-card>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>