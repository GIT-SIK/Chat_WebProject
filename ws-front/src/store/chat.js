import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useChatStore = defineStore('chat', () => {
    const roomId = ref(null);
    const otherUserId = ref(null);
    const olderMessages = ref(null);

    async function setRoomId(nRoomId) {
        roomId.value = nRoomId
      }
    async function setOtherUserId(nOtherUserId) {
      otherUserId.value = nOtherUserId
    }

    async function setOlderMessages(nOlderMessages) {
      olderMessages.value = nOlderMessages;
    }

    return {
        roomId,
        otherUserId,
        olderMessages,
        setRoomId,
        setOtherUserId,
        setOlderMessages,
    }
})