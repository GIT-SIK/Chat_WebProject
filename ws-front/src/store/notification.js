import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useNotificationStore = defineStore('notification', () => {
  const badgeStatus = ref(false)
  const toggleStatus = ref(false)
  const data = ref(null)

  async function setBadgeStatus(bool) {
    badgeStatus.value = bool
  }

  async function toggle() {
    toggleStatus.value = !toggleStatus.value
  }

  async function setData(nData) {
    data.value = nData
  }

  return {
    toggle,
    badgeStatus,
    toggleStatus,
    setBadgeStatus,
    setData,
  }
})
