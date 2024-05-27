import { createApp } from 'vue';
import App from './App.vue';
import router from './router/router.js';
import axios from 'axios';
import store from './store.js';


const app = createApp(App);
app.use(store);
app.use(router);
app.mount('#app');
app.provide('$axios', axios);