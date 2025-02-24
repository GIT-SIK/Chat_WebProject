<template>
  <v-card>
    <v-layout>
      <v-navigation-drawer v-model="drawer" :rail="rail" permanent @click="rail = false">
        <v-list>
          <v-list-item :prepend-avatar="defaultUserImage" :title="navUserId">
            <template v-slot:append>
              <v-btn icon="mdi-chevron-left" variant="text" @click.stop="openRail()"></v-btn>
            </template>
          </v-list-item>
        </v-list>
        <v-divider></v-divider>

        <v-list density="compact" nav>
          <v-list-item
            v-for="item in items"
            :key="item.title"
            :prepend-icon="item.icon"
            :title="item.title"
            :value="item.val"
            @click="handleClick(item.val)"
          ></v-list-item>
        </v-list>
      </v-navigation-drawer>
      <v-main style="height: 100vh"></v-main>
    </v-layout>
  </v-card>
</template>

<script>
import defaultUserImage from '@/assets/default_user.png'
import { ref, inject } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

export default {
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    const showToast = inject('showToast')

    /* 네비 전체 보이기 여부 */
    const drawer = ref(true)

    /* 네비 서브 보이기 여부 */
    const rail = ref(!false)

    const openRail = () => {
      rail.value = !rail.value
    }

    const handleClick = (item) => {
      if (item === 'logout') {
        logout()
      } else if (item === 'friend') router.push({ path: '/auth/friend' })
      else if(item === 'chat') router.push({path : '/auth/chat'})
      else if(item === 'home') router.push({path : '/auth'})
      
      else {
        console.log('지정된 값이 없습니다. 값 : ' + item)
      }
    }

    const logout = () => {
      showToast('다음에 또 만나요!')
      localStorage.removeItem('access_token')
      userStore.token = null
      userStore.userId = null
      userStore.isAdmin = null
      router.push({ path: '/' })
    }

    /* 네비 컨텐츠 데이터 */
    const items = ref([])

    items.value = [
      { title: 'Home', icon: 'mdi-home-city', val: 'home' },
      { title: 'My Account', icon: 'mdi-account', val: 'account' },
      { title: 'Friend', icon: 'mdi-account-group-outline', val: 'friend' },
      { title: 'Chat', icon: 'mdi-account-group-outline', val: 'chat' },
      { title: 'Logout', icon: 'mdi-account-arrow-right', val: 'logout' },
    ]

    return {
      drawer,
      rail,
      openRail,
      navUserId : userStore.userId,      

      handleClick,
      logout,
      defaultUserImage,
      items,
    }
  },
}
</script>
