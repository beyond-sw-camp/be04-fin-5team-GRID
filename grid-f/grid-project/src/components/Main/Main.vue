<template>
    <header>
        <nav>
            <RouterLink to="/vacation/history" replace>history</RouterLink>
            <RouterLink to="/vacation/policy" replace>policy</RouterLink>
            <RouterLink to="/vacation/manage" replace>manage</RouterLink>
        </nav>
    </header>
    <main>
        <RouterView/>
    </main>
</template>

<script setup>
    import { provide, ref } from 'vue';
    import router from '@/router/router';
    import {RouterLink, RouterView} from 'vue-router';


    // function history() {
    //     router.push("/vacation/history")
    // }

    // function policy() {
    //     router.push("/vacation/policy")
    // }

    // function manage() {
    //     router.push("/vacation/manage")
    // }
</script>

<style scoped>

</style>