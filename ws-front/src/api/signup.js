import api from '@/utils/api'

const SIGNUP_API = '/signup'

export async function signupApi(userId, password, nickname) {
  const data = {
    userId: userId,
    userPw: password,
    userNickname: nickname,
  }

  return await api.post(SIGNUP_API, data)
}

export async function checkUserApi(type, tData) {
  return await api.get(SIGNUP_API, { params: { [type]: tData } })
}
