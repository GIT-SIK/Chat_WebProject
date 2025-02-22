<template class="chat-page-container">
<v-row no-gutters>
  <v-col class="pa-4">
    <ChatRoom />
  </v-col>
  <v-col class="pa-4" cols="auto">
    <ChatRoomList :roomListData="chatRoomList.data"/>
  </v-col>
</v-row>
  </template>
  
  <script>
  import ChatRoomList from '../components/ChatRoomList.vue'
  import ChatRoom from '../components/ChatRoom.vue'
  import * as chat from '@/api/chat.js'
  import { onMounted, ref } from 'vue'
  
  export default {
    name: 'ChatPage',
    components: {
      ChatRoom,
      ChatRoomList
    },
    setup () {
        const chatRoomList = ref({data : []});

        const getRoomList = async() => {
            try {
                chatRoomList.value = await chat.getChatRoomListApi();
            } catch (e) {
                console.log("(ChatPage.vue) 채팅방 리스트 불러오는 중 에러 발생 - " + e )
            }
        }

        onMounted (getRoomList);

        return {
            chatRoomList
        }
    }
  }
  </script>
  <style>
  .chat-page-container {
    background-color: #f4f4f9;
   }
  </style>