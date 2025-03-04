<template>
    <div>
      <ul>
        <li v-for="(notification, index) in notifications" :key="index">
          <a>메시지 : {{ notification.notificationMessage }}</a>
          <p>시간 : {{ notification.notiCreatedAt }}</p>
        </li>
      </ul>
    </div>
  </template>
  
  <script>
  import { subscribeToSse } from '@/api/notification';
  import { ref, onBeforeUnmount } from 'vue';
  import {useUserStore} from '@/store/user'
  
  export default {
    setup() {
      const userStore = useUserStore();
      const notifications = ref([]); 
      userStore.getUserInfo();
      const userId = userStore.userId;  
      let eventSource = null;  
  
      // 알림 구독
      const subscribe = () => {
        eventSource = subscribeToSse(userId, handleNewNotification, handleSseError);
      };
  
      // 알림 받기
      const handleNewNotification = (notification) => {
        notifications.value.push(notification); 
        console.log(notifications.value);
      };
  
      // SSE 오류 처리
      const handleSseError = (error) => {
        console.error("알림 받는 중 오류 발생:", error);
      };

      onBeforeUnmount(() => {
        if (eventSource) {
          eventSource.close();
        }
      });
  
      // 구독 시작
      subscribe();
  
      return {
        notifications,
      };
    },
  };
  </script>
  