import api from '@/utils/api'

/**
 * 유저
 *
 *
 *
 *
 */

export async function setUserDataApi() {
  const data = {}
  return await api.post('/api/auth/user/', data)
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
  return await api.get('/api/userinfo', data)
}
