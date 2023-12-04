import { registerPlugins } from '@/plugins'

// Components
import App from './App.vue'

// Composables
import { createApp } from 'vue'

import './index.css'
import './assets/global.css';
import { createPinia } from "pinia"

const pinia = createPinia()
const app = createApp(App)

app.use(pinia)

registerPlugins(app)

app.mount('#app')
