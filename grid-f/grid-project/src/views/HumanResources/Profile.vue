<template>
    <div class="container">
        <div class="header">
            <Header />
        </div>
        <div class="sidebar">
            <Sidebar />
        </div>
        <div class="contents">
            <div class="insideContents">
                <div class="title">
                    <div class="title-image">
                        <img id="title-image" src="@/assets/profile.png" alt="Profile" />
                    </div>
                    <div class="title-name">
                        <h1>인사 정보 상세</h1>
                    </div>
                </div>
                <div class="first">
                    <div class="image">

                    </div>
                    <div class="content">

                    </div>
                    <div class="button">

                    </div>
                </div>
                <div class="tab">


                </div>
                <div class="info1">
                    <div class="hrinfo">

                    </div>
                </div>
                <div class="info2">
                    <div class="basicInfo">

                    </div>
                </div>
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
@font-face {
    font-family: 'IBMPlexSansKR-Regular';
    src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}

body {
    min-height: 100vh;
    min-width: 100vw;
}

.container {
    font-family: 'IBMPlexSansKR-Regular';
    height: 100%;
    min-height: 100%;
    min-width: 100%;
    display: grid;
    grid-template-columns: 250px auto;
    grid-template-rows: 60px auto 10px;
    grid-template-areas:
        "header header"
        "side contents"
        "footer footer";
}

.header {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 1000;
    background-color: aqua;
    grid-area: header;
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

.footer {
    grid-area: footer;
    position: fixed;
    bottom: 0;
    width: 100%;
    z-index: 600;
}

.contents {
    grid-area: contents;
    height: calc(100vh - 70px)px;
}

.insideContents {
    display: flex;
    flex-direction: column;
    width: 80%;
    height: 100%;
    margin: auto;

}

.title {
    margin: 50px 0;
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
}

.title-image {
    width: 42.8px;
    height: 55.8px;
    margin: 0 15px 0 0;
}

#title-image {
    width: 60px;
    height: 100%;
    border-radius: 50%;
    cursor: pointer;
    margin-right: 50px;
}

.title-name {
    width: 80%;
    font-size: 12px;
    font-weight: 0;
}

.first {
    width: 100%;
    height: 70%;
    display: flex;
    flex-direction: row;
}

.image {
    width: 20%;
    min-height: 10px;
    background-color: red;
}

.content {
    width: 45%;
    min-height: 10px;
    background-color: blue;
}

.button {
    width: 35%;
    min-height: 10px;
    background-color: green;
}

.tab {
    width: 100%;
    height: 14%;
    min-height: 10px;
    background-color: yellow;
}

.info1 {
    width: 100%;
    height: 90%;

}

.hrinfo {
    width: 100%;
    height: 100%;
    background-color: orange;
}

.info2 {
    width: 100%;
    height: 90%;
}

.basicInfo {
    width: 100%;
    min-height: 100%;
    background-color: pink;
}
</style>