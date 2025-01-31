import { createRouter, createWebHistory } from 'vue-router'
import WSPage from '../views/WSPage.vue'
import MainPage from '../views/MainPage.vue'
import MainLayout from '../views/MainLayout.vue'
import SignupPage from '../components/SignupModal.vue'
import LoginPage from '../components/LoginModal.vue'
import TestPage from '../views/TestPage.vue'
import FriendPage from '../components/FriendList.vue'
import { useLoginStore } from '@/store/login'

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
      ],
    },
    {
      path: '/auth',
      component: MainLayout,
      children: [
        { path: 'ws', component: WSPage },
        { path: 'fl', component: FriendPage },
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
router.beforeEach((to, from, next) => {
  const loginStore = useLoginStore()
  const authUser =
    loginStore.token == null || loginStore.token != localStorage.getItem('access_token')
      ? true
      : false
  if (to.path.startsWith('/auth') && authUser) {
    console.log('[index.js] 사용자 정보를 확인할 수 없습니다.')
    next('/')
  } else {
    console.log('[index.js] 사용자 확인 완료')
    next()
  }
})

export default router
