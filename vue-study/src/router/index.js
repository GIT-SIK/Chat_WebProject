import { createRouter, createWebHistory } from 'vue-router'
import WSPage from '../views/WSPage.vue'
import SubPage1 from '../views/SubPage1.vue'
import SubPage2 from '../views/SubPage2.vue'
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
        { path: 'sub-page1', component: SubPage1 },
        { path: 'sub-page2', component: SubPage2 },
      ],
    },
    {
      path: '/wspage',
      component: WSPage,
    },
  ],
})

export default router
