<template>
    <div class="container">
        <div class="header">
            <Header />
        </div>
        <div class="sidebar">
            <Sidebar />
        </div>
        <div class="main-content">
            <!-- 메인 콘텐츠를 여기에 추가하거나 현재 라우트의 컴포넌트를 렌더링합니다. -->
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
    grid-area: header;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 1000;
}

.sidebar {
    grid-area: side;
    position: fixed;
    top: 60px;
    left: 0;
    width: 250px;
    height: calc(100vh - 60px);
    z-index: 800;
}

.main-content {
    grid-area: body;
    margin-top: 60px;
    margin-left: 250px;
    padding: 20px;
    height: calc(100vh - 60px);
}

.footer {
    grid-area: footer;
    position: fixed;
    bottom: 0;
    width: 100%;
    z-index: 600;
}

.container {
    display: grid;
    grid-template-columns: 250px auto;
    grid-template-rows: 60px auto 10px;
    grid-template-areas: 
        "header header"
        "side body"
        "footer footer";
    height: 100vh;
}
</style>
