import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import api from '@/utils/api'

const app = createApp(App)

import cbtn from '@/components/global/BaseButton.vue'
app.component('BaseButton', cbtn)
app.component()
/* 공통 Axios 전역 지정 */
app.config.globalProperties.$api = api

app.use(router)

app.mount('#app')
