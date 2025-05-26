import api from '@/utils/api'

export async function getFriendListApi(userUuid) {
  const data = {
    userUuid: userUuid,
  }

  return await api.post('/api/auth/gfriend', data)
}

/* 로직 변경 필요 */
export async function friendRequestApi(senderUserUuid, status) {
  const data = {
    senderUserUuid: senderUserUuid,
    friendStatus: status,
  }

  return await api.post('/api/auth/ufriend', data)
}

export async function getSearchFriendApi(searchData) {
  return await api.get('/api/auth/sfriend', { params: { search: searchData } })
}

export async function addFriendApi(userUuid) {
  return await api.get('/api/auth/cfriend', { params: { add: userUuid } })
}
