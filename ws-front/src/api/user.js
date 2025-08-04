import api from '@/utils/api'

const USER_API = '/users/me'
const AUTH_API = '/auth/refresh'

/**
 * 유저 (MyAccount) 업데이트
 * @param data(isPublic, userChatReceiveScope)
 * @returns 완료 여부
 */
export async function updateUserDataApi(data) {
  return await api.put(USER_API, data)
}

/**
 * 토큰 -> 유저 정보 api
 * @param token
 * @returns {Promise<void>} (USER DATA)
 */
export async function getUserInfo(token) {
  const data = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  }
  return await api.get(USER_API, data)
  
}

/**
 * refreshToken -> AccessToken api
 * @param refreshToken
 * @returns 
 */
export async function getAccessToken(refreshToken) {
  return await api.post(AUTH_API, null, {
    skipAuth: true,
    headers: {
      Authorization: `Bearer ${refreshToken}`,
    },
  })
}