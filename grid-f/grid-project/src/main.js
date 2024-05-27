import { createApp } from 'vue'
import App from './App.vue'
import router from './router/router.js'
import axios from 'axios'
import router from '../src/router/router.js'

const app = createApp(App)
app.use(router);
app.mount('#app')
app.provide('$axios', axios)

