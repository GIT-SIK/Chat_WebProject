<template>
  <v-card class="w-100 mb-3 ml-2 pl-4 h-auto" variant="text" style="background-color: #ffffff">
    <v-text-field
      prepend-icon="mdi-magnify"
      single-line
      label="Search"
      class="mb-3"
      variant="plain"
      hide-details
      v-model="searchFriendData"
      @keyup.enter="searchFriend"
    ></v-text-field>
  </v-card>

  <!-- 친구 목록 영역 -->
  <v-card variant="text" class="w-100">
    <v-row class="pa-2" v-if="!hasNoFriends && searchFriendList.length">
      <v-col
        v-for="searchfriendItem in searchFriendList"
        :key="searchfriendItem.id"
        cols="12"
        sm="4"
        md="3"
        lg="3"
      >
        <v-card class="pa-3 d-flex flex-column align-center" flat>
          <v-avatar size="56" class="mb-3">
            <v-img :src="defaultUserImage" alt="유저 이미지" />
          </v-avatar>
          <div class="text-center font-weight-medium mb-2">
            {{ searchfriendItem.userNickname }}
          </div>
          <v-card-actions>
            <v-btn @click="friendRequest(searchfriendItem.userUuid)"> 친구 추가 </v-btn>
            <v-btn @click="handleChatRoomClk(searchfriendItem.userUuid)"> 메시지 보내기 </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>

    <v-row v-else class="pa-4">
      <v-col cols="12" class="text-center">
        <span>검색된 친구가 없습니다.</span>
      </v-col>
    </v-row>
  </v-card>
</template>

<script>
import { ref, inject } from 'vue'
import * as friend from '@/api/friend'
import defaultUserImage from '@/assets/default_user.png'
import { useRouter } from 'vue-router'

export default {
  setup() {
    const showToast = inject('showToast')
    const searchFriendList = ref([])
    const searchFriendData = ref()
    const hasNoFriends = ref(false)
    const router = useRouter()
    const searchFriend = async () => {
      try {
        const response = await friend.getSearchFriendApi(searchFriendData.value)
        if (response.data.length > 0) {
          showToast(response.data.length + `명의 친구를 찾았습니다.`)
        } else {
          showToast(`검색된 친구가 없습니다.`)
        }
        searchFriendList.value = response.data
        hasNoFriends.value = false
      } catch (e) {
        if (e.status === 500) {
          hasNoFriends.value = true
        }
      }
      searchFriendData.value = ''
    }

    /* 채팅방 열기 */
    const handleChatRoomClk = (friendUuid) => {
      router.push({ path: '/auth/chat', query: { fu: friendUuid } })
    }

    const friendRequest = async (friendUuid) => {
      /* 반환시 모두 OK로 처리하여 Try가 필요 없음. */
      const response = await friend.friendRequestApi(friendUuid)
      showToast(response.data)
    }

    return {
      handleChatRoomClk,
      hasNoFriends,
      searchFriend,
      showToast,
      searchFriendList,
      searchFriendData,
      defaultUserImage,
      friendRequest,
    }
  },
}
</script>

<style scoped>
.v-card {
  transition: 0.3s ease-in-out;
}
</style>
