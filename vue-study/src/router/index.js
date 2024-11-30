import { createRouter, createWebHistory } from 'vue-router'
import NPage1 from '../views/NPage1.vue'
import Page1 from '../views/Page1.vue'
import Page2 from '../views/Page2.vue'
import Home from '../views/Home.vue'
import Layout from '../views/Layout.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: Layout,
      children : [
        { path : '', component: Home},
        { path : 'page1', component : Page1 },
        { path : 'page2', component : Page2 }
      ]
    },
    {
      path: '/npage1',
      component: NPage1
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      
    },
  ],
})

export default router
