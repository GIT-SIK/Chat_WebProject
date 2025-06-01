import api from '@/utils/api'

const FRIENDS_API = '/api/auth/friends'

export async function getFriendListApi(userUuid) {
  const data = {
    userUuid: userUuid,
  }

  return await api.post(FRIENDS_API, data)
}

export async function friendRequestApi(senderUserUuid, status) {
  const data = {
    senderUserUuid: senderUserUuid,
    friendStatus: status,
  }

  return await api.post(`${FRIENDS_API}/accept`, data)
}

export async function getSearchFriendApi(searchData) {
  return await api.get(`${FRIENDS_API}/search`, { params: { s: searchData } })
}

export async function addFriendApi(friendUuid) {
  const data = {
    friendUuid: friendUuid,
  }
  return await api.post(`${FRIENDS_API}/add`, data)
}
