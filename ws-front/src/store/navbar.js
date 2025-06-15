import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useNavBarStore = defineStore('navbar', () => {
  const toggleStatus = ref(false)

  const toggle = async () => {
    toggleStatus.value = !toggleStatus.value
  }

  return {
    toggle,
    toggleStatus,
  }
})
