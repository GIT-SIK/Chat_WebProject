// store/friend.js

import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as friend from '@/api/friend'
import { useUserStore } from '@/store/user'

export const useFriendStore = defineStore('friend', () => {
  const tabStatus = ref("")
  const friendList = ref([])
  const authUser = useUserStore() 

  const setTabStatus = (newTabStatus) => {
    tabStatus.value = newTabStatus
  }

  const fetchFriendList = async (userId) => {
    try {
      const response = await friend.getFriendApi(userId)
      friendList.value = response.data
        .filter(item => item.friendStatus === 'ACCEPTED')
        .map(item => ({
          friendId: item.senderUserId === authUser.userId ? item.receiverUserId : item.senderUserId
        }))
    } catch (e) {
      console.error('친구 목록 불러오기 실패:', e)
    }
  }

  return {
    tabStatus,
    setTabStatus,
    friendList,       
    fetchFriendList   
  }
}, {
  persist: true
})
