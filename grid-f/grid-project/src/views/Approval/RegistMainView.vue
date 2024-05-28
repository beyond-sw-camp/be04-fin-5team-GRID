<script setup>
  import {useRouter} from "vue-router";

  const router = useRouter();

  const navigateTO = (path) => {
    router.push(`/regist/${path}`);
  }

</script>

<template>
  <div>
    <b-card-group deck>
      <b-card title="출장" img-src="https://picsum.photos/300/300/?image=41" img-alt="Image" img-top @click="navigateTO('bt')">
        <b-card-text>
          This is a wider card with supporting text below as a natural lead-in to additional content.
          This content is a little bit longer.
        </b-card-text>
        <template #footer>
          <small class="text-muted">Last updated 3 mins ago</small>
        </template>
      </b-card>

      <b-card title="시간 외 근무" img-src="https://picsum.photos/300/300/?image=41" img-alt="Image" img-top @click="navigateTO('overtime')">
        <b-card-text>
          This card has supporting text below as a natural lead-in to additional content.
        </b-card-text>
        <template #footer>
          <small class="text-muted">Last updated 3 mins ago</small>
        </template>
      </b-card>

      <b-card title="단축 근무" img-src="https://picsum.photos/300/300/?image=41" img-alt="Image" img-top @click="navigateTO('rw')">
        <b-card-text>
          This is a wider card with supporting text below as a natural lead-in to additional content.
          This card has even longer content than the first to show that equal height action.
        </b-card-text>
        <template #footer>
          <small class="text-muted">Last updated 3 mins ago</small>
        </template>
      </b-card>

      <b-card title="휴가" img-src="https://picsum.photos/300/300/?image=41" img-alt="Image" img-top @click="navigateTO('vacation')">
        <b-card-text>
          This is a wider card with supporting text below as a natural lead-in to additional content.
          This card has even longer content than the first to show that equal height action.
        </b-card-text>
        <template #footer>
          <small class="text-muted">Last updated 3 mins ago</small>
        </template>
      </b-card>
    </b-card-group>
  </div>
</template>

<style scoped>

</style>