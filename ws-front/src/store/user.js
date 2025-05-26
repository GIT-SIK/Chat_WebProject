import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as login from '@/api/login'

export const useUserStore = defineStore('login', () => {
  // 상태
  const token = ref(null)
  const userId = ref(null)
  const userUuid = ref(null)
  const userNickname = ref(null)
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
      userUuid.value = response.data.userUuid
      isAdmin.value = response.data.isAdmin
      userNickname.value = response.data.userNickname
    } catch (e) {
      localStorage.removeItem('access_token')
    }
  }

  return {
    token,
    userUuid,
    isAdmin,
    userNickname,
    setToken,
    getUserInfo,
  }
})
