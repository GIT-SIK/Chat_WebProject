import { createRouter, createWebHistory } from 'vue-router'
import WSPage from '../views/WSPage.vue'
import MainPage from '../views/MainPage.vue'
import Layout from '../views/Layout.vue'

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
    // {
    //   path: '/wspage',
    //   component: WSPage,
    // },
  ],
})

export default router
