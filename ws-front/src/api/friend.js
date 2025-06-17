import api from '@/utils/api'

const FRIENDS_API = '/api/friends'

/*
 * 등록된 친구 목록 API
 *
 */

export async function getFriendListApi() {
  return await api.get(FRIENDS_API)
}

/*
 * 친구 요청 수락/ 거절 처리 API
 *
 */
export async function friendRespondApi(senderUserUuid, friendStatus) {
  const data = {
    senderUserUuid: senderUserUuid,
    friendStatus: friendStatus,
  }

  return await api.put(`${FRIENDS_API}`, data)
}

/*
 * 친구 신청 처리 API
 *
 */

export async function friendRequestApi(friendUuid) {
  return await api.post(`${FRIENDS_API}`, { friendUuid })
}

/*
 * 친구 검색 API
 *
 */

export async function getSearchFriendListApi(searchData) {
  return await api.get(`${FRIENDS_API}/search`, { params: { v: searchData } })
}
