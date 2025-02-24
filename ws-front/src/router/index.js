import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../views/MainLayout.vue'
import SignupPage from '../components/SignupModal.vue'
import FriendPage from '../views/FriendPage.vue'
import ChatPage from '../views/ChatPage.vue'
import { useUserStore } from '@/store/user'
import MainPage from '@/views/MainPage.vue'

/* */
import TestPage from '../views/TestPage.vue'
import LoginPage from '../views/LoginPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      children: [
        { path: '', component: LoginPage },
        { path: 'signup', component: SignupPage },
      ],
    },
    {
      path: '/auth',
      component: MainLayout,
      children: [
        { path: '', component: MainPage },
        { path: 'chat', component: ChatPage },
        { path: 'friend', component: FriendPage },
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
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  await userStore.getUserInfo()
  const authUser =
  userStore.token == null || userStore.token != localStorage.getItem('access_token')
      ? true
      : false
  if (to.path.startsWith('/auth') && authUser) {
    console.log('[index.js] 사용자 정보를 확인할 수 없습니다.')
    next('/')
  } else if (userStore.token && to.path === '/') {
    console.log('[index.js] 로그인 상태')
    next('/auth')
  } else {
    console.log('[index.js] 사용자 확인 완료')
    next()
  }
})

export default router
