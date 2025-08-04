import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as user from '@/api/user'

export const useUserStore = defineStore('user', () => {
  // 상태
  const accessToken = ref(null)
  const refreshToken = ref(null)
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

  const setAccessToken = async (nToken) => {
    localStorage.setItem('access_token', nToken)
    accessToken.value = nToken
  }

  const setRefreshToken = async (nToken) => {
    localStorage.setItem('refresh_token', nToken)
    refreshToken.value = nToken
  }

  /* 유저 정보 불러오기
   * 사용 : 로그인, 사용자 정보가 필요한 로직
   */
  const getUserInfo = async () => {
    try {
      const localToken = localStorage.getItem('access_token')
      const response = await user.getUserInfo(localToken)
      accessToken.value = localToken
      setUserAccessData(response)
      return true
    } catch (e) {
      if (e.status === 401 || e.status === 403) {
      const newAccessToken = await refreshAccessToken()
      if (newAccessToken) {
        try {
          const response = await user.getUserInfo(newAccessToken)
          accessToken.value = newAccessToken
          setUserAccessData(response)
          return true
        } catch (e2) {
          console.error("사용자 정보 요청 실패", e2)
        }
      }
    }
    }
  }

const setUserAccessData = (response) => {
  userId.value = response.data.userId
  userUuid.value = response.data.userUuid
  isAdmin.value = response.data.isAdmin
  userChatReceiveScope.value = response.data.userChatReceiveScope
  isPublic.value = response.data.isPublic
  userCreatedAt.value = response.data.userCreatedAt
  userNickname.value = response.data.userNickname
}


const refreshAccessToken = async () => {
  try {
    const refreshToken = localStorage.getItem('refresh_token')
    if (!refreshToken) {
      return false
    }

    const response = await user.getAccessToken(refreshToken)
    const newAccessToken = response.data
    localStorage.setItem('access_token', newAccessToken)
    accessToken.value = newAccessToken
    return newAccessToken
  } catch (e) {
    accessToken.value = ''
    refreshToken.value = ''
    localStorage.removeItem('access_token')
    localStorage.removeItem('refresh_token')
    return false
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
      //
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
    accessToken,
    refreshToken,
    userUuid,

    userNickname,
    isPublic,
    isAdmin,
    userChatReceiveScope,
    isUpdated,
    userCreatedAt,

    setIsUpdated,
    setAccessToken,
    setRefreshToken,
    setUserData,
    getUserInfo,
  }
})
