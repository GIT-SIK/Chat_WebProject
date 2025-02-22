<template>
    <ChatRoomList />
    <hr />
    <ChatRoom />
  </template>
  
  <script>
  import ChatRoomList from '@/components/ChatRoomList.vue'
  import ChatRoom from '@/components/ChatRoom.vue'
  
  export default {
    name: 'ChatPage',
    components: {
        ChatRoom,
      ChatRoomList
    },
  }
  </script>