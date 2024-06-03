import App from './App.vue';
import { createApp } from 'vue';
import router from './router/router.js';
import axios from 'axios';
import store from './store.js';
import { BootstrapVue3 } from 'bootstrap-vue-3';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';

const app = createApp(App);

app.use(store);
app.use(router);
app.use(BootstrapVue3);

app.mount('#app');
app.provide('$axios', axios);

