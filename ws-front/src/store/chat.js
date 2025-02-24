import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useChatStore = defineStore('chat', () => {
    const roomId = ref(null);
    const otherUserId = ref(null);

    async function setRoomId(nRoomId) {
        roomId.value = nRoomId
      }
    async function setOtherUserId(nOtherUserId) {
      otherUserId.value = nOtherUserId
    }

    return {
        roomId,
        otherUserId,
        setOtherUserId,
        setRoomId
    }
})