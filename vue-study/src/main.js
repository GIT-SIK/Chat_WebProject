import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import api from '@/utils/api'

const app = createApp(App)

/* 공통 Axios 전역 지정 */
app.config.globalProperties.$api = api

/* 공통 뷰 전역 지정*/
const globalComponents = import.meta.glob('@/components/global/*.vue')

const loadComponents = async () => {
  // async -> await
  try {
    for (const [path, loader] of Object.entries(globalComponents)) {
      const module = await loader()
      console.log('path: ', path)
      const componentName = path
        .split('/')
        .pop()
        .replace(/\.\w+$/, '')
        .split('-')
        .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
        .join('')

      console.log('공통 컴포넌트 등록: ', module.default.name)

      app.component(componentName, module.default)
    }
  } catch (error) {
    console.error('[main.js] Error loading global components:', error)
  }
}
loadComponents().then(() => {
  app.mount('#app')
})

app.component()
app.use(router)
