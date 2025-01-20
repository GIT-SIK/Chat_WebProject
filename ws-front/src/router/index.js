import { createRouter, createWebHistory } from 'vue-router'
import WSPage from '../views/WSPage.vue'
import MainPage from '../views/MainPage.vue'
import MainLayout from '../views/MainLayout.vue'
import SignupPage from '../components/SignupModal.vue'
import LoginPage from '../components/LoginModal.vue'
import TestPage from '../views/TestPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: MainLayout,
      children: [
        { path: '', component: MainPage },
        { path: 'signup', component: SignupPage },
        { path: 'login', component: LoginPage },
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
