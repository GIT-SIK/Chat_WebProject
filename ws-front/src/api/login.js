import api from '@/utils/api'

/**
 * 로그인
 *
 * @param userId
 * @param password
 * @returns
 */

export async function loginApi(userId, password) {
  const data = {
    userId: userId,
    userPw: password,
  }
  return await api.post('/login', data)
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
