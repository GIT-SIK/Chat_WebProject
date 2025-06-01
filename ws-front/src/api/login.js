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
