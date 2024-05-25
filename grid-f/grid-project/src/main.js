import { createApp } from 'vue'
import App from './App.vue'
import router from './router/router.js'
import axios from 'axios'


const app = createApp(App)
app.use(router);
app.mount('#app')
app.provide('$axios', axios)