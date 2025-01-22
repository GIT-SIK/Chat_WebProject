import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as login from '@/api/login'

export const useLoginStore = defineStore('login', () => {
  // 상태
  const token = ref(null)
  const userId = ref(null)
  const isAdmin = ref(null)

  async function setToken(nToken) {
    token.value = nToken
  }

  // 유저 정보
  async function getUserInfo() {
    try {
      const localToken = localStorage.getItem('access_token')
      const response = await login.getUserInfo(localToken)

      token.value = localToken
      userId.value = response.data.userId
      isAdmin.value = response.data.isAdmin
    } catch (e) {
      localStorage.removeItem('access_token')
    }
  }

  return {
    token,
    userId,
    isAdmin,
    setToken,
    getUserInfo,
  }
})
