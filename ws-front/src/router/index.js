import { createRouter, createWebHistory } from 'vue-router'
import WSPage from '../views/WSPage.vue'
import MainPage from '../views/MainPage.vue'
import Layout from '../views/Layout.vue'
import TestPage from '../views/TestPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: Layout,
      children: [
        { path: '', component: MainPage },
        { path: 'wspage', component: WSPage },
      ],
    },
    // *************************
    // CSS 테스트 용도 페이지 라우터
    {
      path: '/testpage',
      component: TestPage,
    },
  ],
})

export default router
