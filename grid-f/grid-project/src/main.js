import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'
import router from '../src/router/router.js'

const app = createApp(App);


app.provide('$axios', axios);
app.use(router);
app.mount('#app');