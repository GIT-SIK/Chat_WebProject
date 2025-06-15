import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useNotificationStore = defineStore('notification', () => {
  const badgeStatus = ref(false)
  const toggleStatus = ref(false)

  const setBadgeStatus = async (bool) => {
    badgeStatus.value = bool
  }

  const setToggleStatus = async (bool) => {
    toggleStatus.value = bool
  }

  const toggle = async () => {
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
