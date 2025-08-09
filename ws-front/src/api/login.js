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
 * 로그아웃
 * @param refreshToken
 * @returns
 */

export async function logoutApi(refreshToken) {
  return await api.post('/logout', null, {
      skipAuth: true,
      headers: {Authorization: `Bearer ${refreshToken}`}
    }
  )
}