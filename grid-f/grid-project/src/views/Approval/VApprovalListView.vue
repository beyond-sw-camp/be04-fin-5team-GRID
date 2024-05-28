<script setup>
import { onMounted, reactive } from "vue";
import axios from "axios";
import ApprovalList from "@/components/Approval/ApprovalList.vue";

const typeId = 4;
const state = reactive({
    approvalList: [],
    overtimeInWeek: 0
});

const fetchApprovalList = async (id) => {
    try {
        const response = await axios.get(`http://localhost:8080/approval/list/${typeId}/${id}/0`);

        if (response.status !== 200) {
            throw new Error("response is not ok");
        }

        state.approvalList = response.data.approvalEmpResultList;
        state.approvalList.type = "v";

    } catch (error) {
        console.error('Fetch error: ' + error.message);
    }
}

onMounted(async () => {
    await fetchApprovalList(1);
})
</script>

<template>
    <ApprovalList :approvalList="state.approvalList" />
</template>

<style scoped></style>