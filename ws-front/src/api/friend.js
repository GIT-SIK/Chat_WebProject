import api from '@/utils/api'

export async function getFriendApi(userId) {
  const data = {
    userId: userId,
  }

  return await api.post('/api/auth/gfriend', data)
}

export async function getSearchFriendApi(userId) {
  return await api.get('/api/auth/sfriend', { params: { search: userId } })
}
