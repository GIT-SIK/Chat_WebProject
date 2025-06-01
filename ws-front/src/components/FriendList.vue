<template>
  <div>
    <v-row>
      <v-col v-for="friend in friendList" :key="friend.id" cols="12" sm="4" md="3" lg="3">
        <v-card
          flat
          class="pa-3 d-flex flex-column align-center"
          style="cursor: pointer"
          @click="handleChatRoomClk(friend.friendUuid)"
        >
          <v-avatar size="56" class="mb-3">
            <v-img :src="defaultUserImage" alt="유저 이미지" />
          </v-avatar>

          <div class="text-subtitle-2 font-weight-bold">
            <span>
              {{ friend.friendNickname }}
            </span>
          </div>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import { onMounted, ref } from 'vue'
import * as friend from '@/api/friend'
import { useUserStore } from '@/store/user'
import { useFriendStore } from '@/store/friend'
import defaultUserImage from '@/assets/default_user.png'
import { useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'

export default {
  setup() {
    const authUser = useUserStore()
    const friendStore = useFriendStore()
    const router = useRouter()
    /* 친구목록 데이터 갱신 감지 */
    const { friendList } = storeToRefs(friendStore)

    /* 채팅방 열기 */
    const handleChatRoomClk = (friendUuid) => {
      router.push({ path: '/auth/chat', query: { fu: friendUuid } })
    }

    /* 친구 목록 가져오기 (최초 1회) */
    onMounted(() => {
      friendStore.fetchFriendList()
    })

    return {
      defaultUserImage,
      friendList,
      handleChatRoomClk,
    }
  },
}
</script>

<style scoped>
/* 친구 목록 스타일 */
.fl-card {
  background-color: #ffffff;
  height: calc(100vh - 80px);
  width: 100%;
}
</style>
