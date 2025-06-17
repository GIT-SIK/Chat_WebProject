import api from '@/utils/api'

const SIGNUP_API = '/api/signup'

export async function signupApi(userId, password, nickname) {
  const data = {
    userId: userId,
    userPw: password,
    userNickName: nickname,
  }

  return await api.post(SIGNUP_API, data)
}

export async function checkUserApi(type, tData) {
  const data = {
    type: type,
    data: tData,
  }

  return await api.get(SIGNUP_API, data)
}
