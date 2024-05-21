import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'
import router from '../src/router/router.js'

const app = createApp(App);

app.mount('#app');
app.provide('$axios', axios);
app.use(router);