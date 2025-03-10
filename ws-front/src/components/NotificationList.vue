<template>
  <v-card v-show="toggleStatus" class="mx-auto" width="300">
    <v-toolbar class="transparent-bg">
      <v-toolbar-title>알림</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon="mdi-close" @click="closeNotification" variant="text"></v-btn>
    </v-toolbar>

    <v-list class="transparent-bg overflow-y-auto" lines="three" max-height="450">
      <v-list-item v-for="(notification, index) in notifications" :key="index">
        <!-- <template textv-slot:prepend>
              <v-avatar>
                <img :src="notification.prependAvatar" alt="Avatar" />
              </v-avatar>
            </template> -->

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
import defaultUserImage from '@/assets/default_user.png'

/* SSE api */
import { subscribeToSse } from '@/api/notification'

/* pinia */
import { storeToRefs } from 'pinia'
import { useNotificationStore } from '@/store/notification'
import { useUserStore } from '@/store/user'

export default {
  setup() {
    const notificationStore = useNotificationStore()
    const { toggleStatus } = storeToRefs(notificationStore)
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
      notifications.value.push(notification)
      console.log(notifications.value)
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

    /*
       prependAvatar: defaultUserImage,
        title: 'List Title 1',
        subtitle: `<span class="text-primary">Subtitle 1</span> &mdash; Text`,
      },
    */

    const closeNotification = () => {
      notificationStore.toggle()
    }

    return {
      notifications,
      toggleStatus,
      closeNotification,
    }
  },
}
</script>

<style>
.notification-sub-container {
  background-color: #f5f2f1 !important;
}

.transparent-bg {
  background-color: transparent !important;
}
</style>
