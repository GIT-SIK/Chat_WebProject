<template>
  <v-row class="h-100">
    <v-col cols="12" sm="4">
      <ChatRoomList :roomListData="chatRoomList.data" @other-user-id="chatRoomListEmitData" />
    </v-col>
    <v-col cols="12" sm="8" class="h-100">
      <ChatRoom />
    </v-col>
  </v-row>
</template>

<script>
import ChatRoom from '@/components/chat/ChatRoom.vue'
import ChatRoomList from '@/components/chat/ChatRoomList.vue'
import * as chat from '@/api/chat.js'
import { useChatStore } from '@/store/chat'
import { onMounted, ref } from 'vue'

export default {
  components: {
    ChatRoom,
    ChatRoomList,
  },
  setup() {
    const chatStore = useChatStore()
    const chatRoomList = ref({ data: [] })

    /* 채팅방 정보, 채팅 내역 상태 저장 */
    const chatRoomListEmitData = async (data) => {
      const roomInfo = await chat.getChatRoomInfoApi(data)
      chatStore.setRoomId(roomInfo.data.chatRoomInfo.roomId)
      chatStore.setOtherUserId(roomInfo.data.chatRoomInfo.otherUserId)
      chatStore.setOlderMessages(roomInfo.data.chatRoomMessages)
    }

    /* 채팅방 리스트 */
    const getRoomList = async () => {
      try {
        chatRoomList.value = await chat.getChatRoomListApi()
      } catch (e) {
        console.log('(ChatPage.vue) 채팅방 리스트 불러오는 중 에러 발생 - ' + e)
      }
    }

    onMounted(getRoomList)

    return {
      chatRoomList,
      chatRoomListEmitData,
    }
  },
}
</script>

<style>
</style>
