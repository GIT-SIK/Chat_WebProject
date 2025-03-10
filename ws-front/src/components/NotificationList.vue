<template>
  <v-card class="mx-auto" width="300">
    <v-toolbar class="transparent-bg">
      <v-toolbar-title>알림</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon="mdi-close" @click="closeNotification" variant="text"></v-btn>
    </v-toolbar>

    <v-list class="transparent-bg overflow-y-auto" lines="three" max-height="450">
      <v-list-item
        v-for="(notification, index) in notifications"
        :key="index"
        @click="$router.push(notification.actionUrl)"
      >
        <v-list-item-title>{{ notification.notificationMessage }}</v-list-item-title>
        <v-list-item-subtitle>
          {{ notification.notiCreatedAt }}
        </v-list-item-subtitle>
      </v-list-item>
    </v-list>
  </v-card>
</template>

<script>
import { ref, onBeforeUnmount } from 'vue'

/* SSE api */
import { subscribeToSse } from '@/api/notification'

/* pinia */
import { useNotificationStore } from '@/store/notification'
import { useUserStore } from '@/store/user'

export default {
  setup() {
    const notificationStore = useNotificationStore()
    const userStore = useUserStore()

    const notifications = ref([])

    userStore.getUserInfo()
    const userId = userStore.userId

    let eventSource = null

    // 알림 구독
    const subscribe = () => {
      eventSource = subscribeToSse(userId, handleNewNotification, handleSseError)
    }

    // 알림 받기
    const handleNewNotification = (notification) => {
      if (notification.notiCreatedAt) {
        notification.notiCreatedAt = formatDate(notification.notiCreatedAt)
      }
      notifications.value.push(notification)
      notificationStore.setBadgeStatus(true)
    }

    // SSE 오류 처리
    const handleSseError = (error) => {
      console.error('알림 받는 중 오류 발생:', error)
    }

    onBeforeUnmount(() => {
      if (eventSource) {
        eventSource.close()
      }
    })

    // 구독 시작
    subscribe()

    /* 날짜 변환 */
    const formatDate = (isoDate) => {
      if (!isoDate) return ''
      return new Date(isoDate).toLocaleString('ko-KR', {
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        hour12: true,
      })
    }

    const closeNotification = () => {
      notificationStore.toggle()
    }

    return {
      notifications,
      closeNotification,
    }
  },
}
</script>

<style>
.transparent-bg {
  background-color: transparent !important;
}
</style>
