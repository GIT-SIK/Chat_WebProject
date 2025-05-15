import api from '@/utils/api'

export async function getFriendApi(userId) {
  const data = {
    userId: userId,
  }

  return await api.post('/api/auth/gfriend', data)
}


export async function friendRequestApi(senderUserId, status) {
  const data = {
    senderUserId: senderUserId,
    friendStatus : status
  }

  return await api.post('/api/auth/ufriend', data)
}

export async function getSearchFriendApi(userId) {
  return await api.get('/api/auth/sfriend', { params: { search: userId } })
}

export async function addFriendApi(userId) {
  return await api.get('/api/auth/cfriend', { params: { add: userId } })
}
