<script setup>
import { computed, onMounted, watch } from 'vue';
import { RouterView, useRoute } from 'vue-router';
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import { useStore } from 'vuex';
import axios from 'axios';

const store = useStore();
const route = useRoute();

const layoutHiddenPaths = [
  '/',
  '/find/id',
  '/find/pwd',
  '/find/id/result',
  '/find/pwd/:email/result',
  '/test'
];
const showLayout = computed(() => {
  return !layoutHiddenPaths.some(hiddenPath => {
    const regex = new RegExp(`^${hiddenPath.replace(/:\w+/g, '[^/]+')}$`);
    return regex.test(route.path);
  });
});

const containerClass = computed(() => {
  return showLayout.value ? 'container' : 'container-full';
});

const mainContentClass = computed(() => {
  return showLayout.value ? 'main-content' : 'main-content-full';
});

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

const addTokenTime = async () => {
  const response = await axios.post(
    '/api/tokens/re-auth',
    {},
    {
      headers: {
        'Content-Type': 'application/json'
      },
      withCredentials: true
    }
  );
  localStorage.setItem('access', response.data.access);
};

onMounted(async () => {
  const token = localStorage.getItem('access');
  if (token) {
    const decodedToken = parseJwt(token);
    const email = decodedToken?.sub || '';
    await store.dispatch('fetchUserByEmail', email);
  }
  addTokenTime();
});

watch(
  () => route.path,
  () => {
    addTokenTime();
  }
);

</script>

<template>
  <div class="container" :class="containerClass">
    <div class="header" v-if="showLayout">
      <Header />
    </div>
    <div class="sidebar" v-if="showLayout">
      <Sidebar />
    </div>
    <div class="main-content" :class="mainContentClass">
      <RouterView />
    </div>

  </div>

</template>

<style scoped>
@font-face {
  font-family: 'IBMPlexSansKR-Regular';
  src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

body {
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
  margin: 0;
  padding: 0;
  width: 100vw;
  height: 100vh;
}

.container {
  font-family: 'IBMPlexSansKR-Regular', sans-serif;
  display: grid;
  grid-template-columns: 250px 1fr;
  grid-template-rows: 50px auto 35px;
  grid-template-areas:
    "header header"
    "side body"
    "side footer";
  height: 100%;
  min-width: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
}

.header {
  grid-area: header;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  padding: 0;
}

.sidebar {
  grid-area: side;
  position: fixed;
  top: 60px;
  left: 0;
  width: 250px;
  height: calc(100vh - 60px);
  z-index: 800;
  font-size: 14px;
}

.main-content {
  grid-area: body;
  margin-top: 60px;
  padding: 0;
  height: calc(100vh - 60px);
}

.footer {
  grid-area: footer;
  position: fixed;
  bottom: 0;
  width: calc(100% - 200px);
  z-index: 600;
  font-size: 10px;
  justify-self: flex-end;
}

.container-full {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  padding: 0;
}

.main-content-full {
  padding: 0;
  margin: 0;
}
</style>