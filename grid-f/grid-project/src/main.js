import { createApp } from 'vue'
import App from './App.vue'
import router from './router/router.js'
import axios from 'axios'
import 'bootstrap/dist/css/bootstrap.min.css'

const app = createApp(App)
app.use(router);
app.mount('#app')
app.provide('$axios', axios)