import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'

const app = createApp(App)
app.mount('#app')
app.provide('$axios', axios)