import api from '@/utils/api'

export async function signupApi(userId, password, nickname) {
  const data = {
    userId: userId,
    userPw: password,
    userNickName: nickname,
  }

  return await api.post('/api/signup', data)
}

export async function checkUserApi(type, tData) {
  const data = {
    type: type,
    data: tData,
  }

  return await api.post('/api/checkuser', data)
}
