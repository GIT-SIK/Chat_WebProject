import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as user from '@/api/user'

export const useUserStore = defineStore('user', () => {
  // 상태
  const token = ref(null)
  const userId = ref(null)
  const userUuid = ref(null)

  /* My Page data */
  const userNickname = ref(null)
  const isAdmin = ref(null)
  const isPublic = ref(null)
  const userChatReceiveScope = ref(null)
  const userCreatedAt = ref(null)

  /* My Page Updated ? */
  const isUpdated = ref(false)

  const setToken = async (nToken) => {
    token.value = nToken
  }

  /* 유저 정보 불러오기
   * 사용 : 로그인, 사용자 정보가 필요한 로직
   */
  const getUserInfo = async () => {
    try {
      const localToken = localStorage.getItem('access_token')
      const response = await user.getUserInfo(localToken)

      token.value = localToken
      userId.value = response.data.userId
      userUuid.value = response.data.userUuid
      isAdmin.value = response.data.isAdmin
      userChatReceiveScope.value = response.data.userChatReceiveScope
      isPublic.value = response.data.isPublic
      userCreatedAt.value = response.data.userCreatedAt

      userNickname.value = response.data.userNickname
    } catch (e) {
      localStorage.removeItem('access_token')
    }
  }

  /*
   * 유저 정보 저장
   * 사용 : 마이페이지
   */
  const setUserData = async (data) => {
    try {
      // userNickname.value = data.userNickname
      isPublic.value = data.isPublic
      userChatReceiveScope.value = data.userChatReceiveScope

      if (isUpdated.value) {
        isUpdated.value = false
        const msg = await user.updateUserDataApi(data)
        return msg.data
      }
    } catch (e) {
      console.error('유저 정보 Error => ', e)
    }
  }

  /*
   * 유저 정보 업데이트 여부
   * 사용 : 마이페이지
   */
  const setIsUpdated = (bool) => {
    isUpdated.value = bool
  }

  return {
    token,
    userUuid,

    userNickname,
    isPublic,
    isAdmin,
    userChatReceiveScope,
    isUpdated,
    userCreatedAt,

    setIsUpdated,
    setToken,
    setUserData,
    getUserInfo,
  }
})
