<template>
  <v-card class="mx-auto" max-width="75" variant="text">
    <v-item-group selected-class="left-nav-item">
      <v-item v-slot="{ isSelected, selectedClass, toggle }" v-for="(item, i) in items" :key="i">
        <template v-if="item.icon !== undefined">
          <template v-if="item.tooltip !== undefined">
            <v-tooltip location="right">
              <template v-slot:activator="{ props }">
                <v-btn
                  variant="text"
                  icon
                  v-bind="props"
                  :class="['d-flex align-center', selectedClass]"
                  class="my-1"
                  size="large"
                  @click="handleClick(item.val)"
                >
                  <v-icon color="brown-darken-2">{{ item.icon }}</v-icon>
                </v-btn>
              </template>
              <span>{{ item.tooltip }}</span>
            </v-tooltip>
          </template>

          <template v-else>
            <v-btn
              variant="text"
              icon
              :class="['d-flex align-center', selectedClass]"
              class="my-1"
              size="large"
            >
              <template v-if="item.badge">
                <v-badge v-model="isNotificationRead" color="red" left overlap dot>
                  <template v-slot:badge>
                    <span>6</span>
                  </template>
                  <v-icon color="brown-darken-2" @click="toggleNotification">{{
                    item.icon
                  }}</v-icon>
                </v-badge>
              </template>
              <template v-else>
                <v-icon color="brown-darken-2">{{ item.icon }}</v-icon>
              </template>
            </v-btn>
          </template>
        </template>

        <template v-if="item.tab !== undefined">
          <div style="height: 30px"></div>
        </template>
      </v-item>
    </v-item-group>
  </v-card>

  <div class="notification-container">
    <Notification :isVisible="isNotificationVisible" @closeNotification="toggleNotification" />
  </div>
</template>

<script>
import Notification from './NotificationList.vue'
import { ref, inject } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

export default {
  components: {
    Notification,
  },

  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    const isNotificationRead = ref(true)
    const isNotificationVisible = ref(false)
    const showToast = inject('showToast')

    const toggleNotification = () => {
      isNotificationRead.value = !isNotificationRead.value
      isNotificationVisible.value = !isNotificationVisible.value
    }

    const items = [
      { tab: '' },
      { tab: '' },
      { icon: 'mdi-home', badge: false, tooltip: 'Home', val: 'home' },
      { icon: 'mdi-bell', badge: true },
      { tab: '' },
      { icon: 'mdi-account-group-outline', badge: false, tooltip: 'Friends', val: 'friends' },
      { icon: 'mdi-chat-outline', badge: false, tooltip: 'Chat', val: 'chat' },
      { tab: '' },
      { tab: '' },
      { icon: 'mdi-cog-outline', badge: false, tooltip: 'My Account' },
      { icon: 'mdi-logout', badge: false, tooltip: 'Logout', val: 'logout' },
    ]

    const handleClick = (item) => {
      if (item === 'logout') {
        console.log(item)
        logout()
      } else if (item === 'friends') router.push({ path: '/auth/friend' })
      else if (item === 'chat') router.push({ path: '/auth/chat' })
      else if (item === 'home') router.push({ path: '/auth' })
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

    return {
      items,
      handleClick,
      isNotificationRead,
      isNotificationVisible,
      toggleNotification,
    }
  },
}
</script>

<style>
.left-nav-item {
  background-color: #efebe9; /* brown-lighten-5 */
}

.notification-container {
  max-width: 300px;
  position: absolute;
  top: 30px;
  left: 50px;
  z-index: 10;
  margin: 0 auto;
  padding: 20px;
}
</style>
