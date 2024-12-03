import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import api from '@/utils/api'

const app = createApp(App)

/* 공통 Axios 전역 지정 */
app.config.globalProperties.$api = api

/* 공통 뷰 전역 지정*/
const globalComponents = import.meta.glob('@/components/global/*.vue')

Object.entries(globalComponents).forEach(([path, loader]) => {
  // path: '@/components/global/*.vue'
  // loader: (Promise) 비동기 함수
  loader()
    .then((module) => {
      // module: Component 전체 모듈 정보
      const componentName = path
        .split('/')
        .pop()
        .replace(/\.\w+$/, '')
        .split('-')
        .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
        .join('')

      app.component(componentName, module.default)
      // module.default: Component 객체 정보
    })
    .catch((error) => {
      console.error('Error loading global component:', error)
    })
})

app.component()
app.use(router)
app.mount('#app')
