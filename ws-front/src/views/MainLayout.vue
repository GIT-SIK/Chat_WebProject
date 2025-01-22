<template>
  <div>
    <NavBar />
    <RouterView />
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import { useLoginStore } from '@/store/login'

export default {
  name: 'MainLayout',
  components: {
    NavBar, // nav-bar 등록
  },

  setup() {
    const ls = useLoginStore()
    ls.getUserInfo()
  },
}
</script>
