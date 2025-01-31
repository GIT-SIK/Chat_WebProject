<template>
  <div>
    <h4>{{ userId }} 님의 친구 목록</h4>
    <template v-for="friend in friendList" :key="friend.id">
      <li v-if="friend.senderUserId == userId">
        <span>{{ friend.receiverUserId }}</span>
      </li>
      <li v-else>
        <span>{{ friend.senderUserId }}</span>
      </li>
    </template>
  </div>
</template>

<script>
import { onMounted, ref } from 'vue'
import * as friend from '@/api/friend'
import { useLoginStore } from '@/store/login'
export default {
  setup() {
    const authUser = useLoginStore()
    const friendList = ref([])
    const getFriendList = async () => {
      authUser.getUserInfo()
      console.log(authUser.userId)
      const response = await friend.getFriendApi(authUser.userId)
      friendList.value = response.data
    }

    onMounted(() => {
      getFriendList()
    })

    return {
      friendList,
      userId: authUser.userId,
    }
  },
}
</script>
