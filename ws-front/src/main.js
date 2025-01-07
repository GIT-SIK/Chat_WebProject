import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import api from '@/utils/api'
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";

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
      .replace(/\.\w+$/, '');

      console.log('공통 컴포넌트 등록: ', module.default.name)

      app.component(componentName, module.default)
    }
  } catch (error) {
    console.error('[main.js] Error loading global components:', error)
  }
}
loadComponents().then(() => {
  app.use(router)
  app.mount('#app')
})

// app.component()

