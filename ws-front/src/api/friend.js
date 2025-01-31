import api from '@/utils/api'

export async function getFriendApi(userId) {
  const data = {
    userId: userId,
  }

  return await api.post('/api/auth/gfriend', data)
}
