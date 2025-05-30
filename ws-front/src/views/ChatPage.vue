<template>
  <v-row class="h-100">
    <v-col cols="12" sm="4">
      <ChatRoomList :roomListData="chatRoomList.data" @other-user-data="chatRoomListEmitData" />
    </v-col>
    <v-col cols="12" sm="8" class="h-100">
      <ChatRoom />
    </v-col>
  </v-row>
</template>

<script>
import { useRoute } from 'vue-router'
import ChatRoom from '@/components/chat/ChatRoom.vue'
import ChatRoomList from '@/components/chat/ChatRoomList.vue'
import * as chat from '@/api/chat.js'
import { useChatStore } from '@/store/chat'
import { onMounted, ref, inject } from 'vue'

export default {
  components: {
    ChatRoom,
    ChatRoomList,
  },
  setup() {
    const showToast = inject('showToast')
    const route = useRoute()
    const chatStore = useChatStore()
    const chatRoomList = ref({ data: [] })

    /* 채팅방 정보, 채팅 내역 상태 저장 */
    const chatRoomListEmitData = async (data) => {
      const roomInfo = await chat.getChatRoomInfoApi(data.ouUuid)
      if (!roomInfo.data.error) {
        chatStore.setRoomId(roomInfo.data.chatRoomInfo.roomId)
        chatStore.setOtherUserUuid(roomInfo.data.chatRoomInfo.otherUserUuid)
        chatStore.setOtherUserNickname(data.ouNickname)
        chatStore.setOlderMessages(roomInfo.data.chatRoomMessages)
      } else {
        showToast(roomInfo.data.message)
      }
    }

    /* 채팅방 리스트 */
    const getRoomList = async () => {
      try {
        chatRoomList.value = await chat.getChatRoomListApi()
      } catch (e) {
        console.log('(ChatPage.vue) 채팅방 리스트 불러오는 중 에러 발생 - ' + e)
      }
    }

    /* 친구목록 -> 채팅방 열기 */
    const handleFriendClk = () => {
      const userUuid = route.query.userUuid
      if (userUuid) {
        chatRoomListEmitData(userUuid)
      }
    }

    onMounted(() => {
      handleFriendClk()
      getRoomList()
    })

    return {
      chatRoomList,
      chatRoomListEmitData,
      handleFriendClk,
    }
  },
}
</script>

<style></style>
