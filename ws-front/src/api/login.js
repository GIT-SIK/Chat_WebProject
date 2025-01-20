import api from '@/utils/api'

export async function loginApi(userId, password) {
  const data = {
    userId: userId,
    userPw: password,
  }
  return await api.post('/login', data)
}
