<template>
  <!-- 메뉴 -->
  <v-card class="mx-auto" max-width="75" variant="text">
    <v-item-group selected-class="left-nav-item">
      <v-item v-slot="{ selectedClass }" v-for="(item, i) in items" :key="i">
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
            <template v-if="item.badge">
              <v-btn
                variant="text"
                icon
                :class="['d-flex align-center', selectedClass]"
                class="my-1"
                size="large"
                @click="notificationStatusUpdate"
              >
                <v-badge v-model="badgeStatus" color="red" left overlap dot>
                  <v-icon color="brown-darken-2">{{ item.icon }}</v-icon>
                </v-badge>
              </v-btn>
            </template>
            <template v-else>
              <v-btn
                variant="text"
                icon
                :class="['d-flex align-center', selectedClass]"
                class="my-1"
                size="large"
              >
                <v-icon color="brown-darken-2">{{ item.icon }}</v-icon>
              </v-btn>
            </template>
          </template>
        </template>

        <template v-if="item.tab !== undefined">
          <div style="height: 30px"></div>
        </template>
      </v-item>
    </v-item-group>
  </v-card>
  <!-- 알림 -->
  <div v-show="toggleStatus" class="notification-container">
    <NotificationList />
  </div>
</template>

<script>
import { ref, inject } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { useNotificationStore } from '@/store/notification'
import { storeToRefs } from 'pinia'
import NotificationList from '../components/NotificationList.vue'

export default {
  components: {
    NotificationList,
  },
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    const showToast = inject('showToast')
    const notificationStore = useNotificationStore()

    /* 알림, 토글 갱신 상태 */
    const { badgeStatus } = storeToRefs(notificationStore)
    const { toggleStatus } = storeToRefs(notificationStore)

    /* 알림 클릭시 뱃지 읽음 처리와 알림창 열기 */
    const notificationStatusUpdate = () => {
      notificationStore.setBadgeStatus(false)
      notificationStore.toggle()
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
        logout()
      } else if (item === 'friends') router.push({ path: '/auth/friend' })
      else if (item === 'chat') router.push({ path: '/auth/chat' })
      else if (item === 'home') router.push({ path: '/auth' })
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
      notificationStatusUpdate,
      handleClick,
      badgeStatus,
      toggleStatus,
      notificationStore,
    }
  },
}
</script>

<style>
.left-nav-item {
  background-color: #efebe9; /* brown-lighten-5 */
}

/* 알림 스타일 */
.notification-container {
  position: absolute;
  top: 90px;
  left: 50px;
  z-index: 10;
  margin: 0 auto;
  padding: 20px;
}

.notification-container::after {
  content: '';
  position: absolute;
  top: 60px;
  left: 0px;
  transform: translateY(-50%);
  border: 10px solid transparent;
  border-right-color: #ffffff;
  z-index: 2;
}

.notification-container::before {
  content: '';
  position: absolute;
  top: 61px;
  left: -1px;
  transform: translateY(-50%);
  border: 10px solid transparent;
  border-right-color: rgba(0, 0, 0, 0.1);
  z-index: 1;
}
</style>
