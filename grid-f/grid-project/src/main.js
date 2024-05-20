import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'
import 'bootstrap/dist/css/bootstrap.min.css'

const app = createApp(App)
app.mount('#app')
app.provide('$axios', axios)