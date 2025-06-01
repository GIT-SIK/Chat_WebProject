import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as chat from '@/api/chat'

export const useChatStore = defineStore('chat', () => {
  const roomId = ref(null)
  const otherUserUuid = ref(null)
  const otherUserNickname = ref(null)
  const olderMessages = ref(null)

  async function setRoomId(nRoomId) {
    roomId.value = nRoomId
  }
  async function setOtherUserUuid(nOtherUserUuid) {
    otherUserUuid.value = nOtherUserUuid
  }

  async function setOtherUserNickname(nOtherUserNickname) {
    otherUserNickname.value = nOtherUserNickname
  }

  async function setOlderMessages(nOlderMessages) {
    olderMessages.value = nOlderMessages
  }

  async function getChatRoomInfo(friendUuid) {
    const roomInfo = await chat.getChatRoomInfoApi(friendUuid)
    if (!roomInfo.data.error) {
      roomId.value = roomInfo.data.chatRoomInfo.roomId
      otherUserUuid.value = roomInfo.data.chatRoomInfo.otherUserUuid
      otherUserNickname.value = roomInfo.data.otherUserNickname
      olderMessages.value = roomInfo.data.chatRoomMessages
    }
  }

  return {
    roomId,
    otherUserUuid,
    olderMessages,
    otherUserNickname,
    setRoomId,
    setOtherUserUuid,
    setOtherUserNickname,
    setOlderMessages,
    getChatRoomInfo,
  }
})
