<template>
    <div class="tableContainer">
        <table>
            <thead>
                <tr>
                    <th>번호</th>
                    <th>이름</th>
                    <th>지급종류</th>
                    <th>휴가종류</th>
                    <th>변경일</th>
                    <th>사유</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(history, index) in paginatedHistories" :key="history.id">
                    <td>{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
                    <td>{{ history.employeeName }}</td>
                    <td>{{ history.changeTypeName }}</td>
                    <td>{{ history.typeName }}</td>
                    <td>{{ history.changeTime }}</td>
                    <td>{{ history.changeReason }}</td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="pagination">
        <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
        <button v-for="page in totalPages" :key="page" @click="goToPage(page)"
            :class="{ active: page === currentPage }">{{ page }}</button>
        <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
    </div>
</template>

<script setup>

</script>

<style lang="scss" scoped></style>