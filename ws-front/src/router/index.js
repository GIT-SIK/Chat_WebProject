import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../views/MainLayout.vue'
import SignupPage from '../components/SignupModal.vue'
import FriendPage from '../views/FriendPage.vue'
import ChatPage from '../views/ChatPage.vue'
import { useUserStore } from '@/store/user'
import MainPage from '../views/MainPage.vue'
import MyAccountPage from '../views/MyAccountPage.vue'

/* */
import AdminPage from '../views/AdminPage.vue'
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
      meta: {
        roles: [null],
      },
    },
    {
      path: '/auth',
      component: MainLayout,
      children: [
        { path: '', component: MainPage },
        { path: 'chat', component: ChatPage },
        { path: 'friend', component: FriendPage },
        { path: 'my', component: MyAccountPage },
      ],
      meta: {
        roles: ['true', 'false'],
      },
    },
    {
      path: '/admin',
      component: AdminPage,
      meta: {
        roles: ['true'],
      },
    },
  ],
})

router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  try {
    await userStore.getUserInfo()
    if (to.meta.roles.includes(userStore.isAdmin)) {
      return next()
    } else {
      if (userStore.isAdmin !== null) {
        return next('/auth')
      } else {
        return next('/')
      }
    }
  } catch (e) {
    return next('/')
  }
})

export default router
