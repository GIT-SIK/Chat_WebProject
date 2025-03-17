import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useNotificationStore = defineStore('notification', () => {
  const badgeStatus = ref(false)
  const toggleStatus = ref(false)

  async function setBadgeStatus(bool) {
    badgeStatus.value = bool
  }

  async function setToggleStatus(bool) {
    toggleStatus.value = bool
  }

  async function toggle() {
    toggleStatus.value = !toggleStatus.value
  }

  return {
    toggle,
    badgeStatus,
    toggleStatus,
    setToggleStatus,
    setBadgeStatus,
  }
})
