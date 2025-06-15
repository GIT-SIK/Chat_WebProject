import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as chat from '@/api/chat'

export const useChatStore = defineStore('chat', () => {
  const roomId = ref(null)
  const otherUserUuid = ref(null)
  const otherUserNickname = ref(null)
  const olderMessages = ref(null)

  const setRoomId = async (nRoomId) => {
    roomId.value = nRoomId
  }

  const setOtherUserUuid = async (nOtherUserUuid) => {
    otherUserUuid.value = nOtherUserUuid
  }

  const setOtherUserNickname = async (nOtherUserNickname) => {
    otherUserNickname.value = nOtherUserNickname
  }

  const setOlderMessages = async (nOlderMessages) => {
    olderMessages.value = nOlderMessages
  }

  const getChatRoomInfo = async (friendUuid) => {
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
