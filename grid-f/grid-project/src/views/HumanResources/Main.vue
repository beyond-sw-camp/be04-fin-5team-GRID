<template>
    <div class="container">

        <div class="header">
            <Header />
        </div>

        <div>
            <div id="sidebar">
                <Sidebar />
            </div>

        </div>


        <div class="footer">
            <Footer />
        </div>
    </div>

</template>

<script setup>
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import Footer from '@/components/Footer.vue';

</script>

<style scoped>
.header {
    position: fixed;
    /* top: 0;
    left: 0;
    width: 100%; */
    z-index: 1000;
}

.sidebar {
    position: fixed;
    /* top: 60px;
    left: 0;
    width: 250px;
    height: calc(100vh - 60px); */

    z-index: 800;
}

.footer {
    position: fixed;
    z-index: 600;
}

.container {
    
    display: grid;
    grid-template-columns: 250px auto;
    grid-template-rows: 60px, auto, 10px; 
    grid-area: 
        header header
        side body
        side footer;
    
}
</style>
