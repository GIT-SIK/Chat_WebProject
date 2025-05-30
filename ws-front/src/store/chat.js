import { defineStore } from 'pinia'
import { ref } from 'vue'

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

  return {
    roomId,
    otherUserUuid,
    olderMessages,
    otherUserNickname,
    setRoomId,
    setOtherUserUuid,
    setOtherUserNickname,
    setOlderMessages,
  }
})
