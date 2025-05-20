<template>
  <div>
      <v-row>
        <v-col
          v-for="friend in friendList"
          :key="friend.id"
          cols="12"
          sm="4"
          md="3"
          lg="3"
        >
          <v-card 
          flat
            class="pa-3 d-flex flex-column align-center"
            style="cursor: pointer;"
            @click="handleChatRoomClk(friend.friendId)"
            >
            <v-avatar size="56" class="mb-3">
              <v-img :src="defaultUserImage" alt="유저 이미지" />
            </v-avatar>

            <div class="text-subtitle-2 font-weight-bold">
              <span>
                {{
                  friend.friendId
                }}
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

export default {
  setup() {
    const authUser = useUserStore()
    const friendStore = useFriendStore()
    const router = useRouter()

    /* 채팅방 열기 */
    const handleChatRoomClk = (friendId) => {
      router.push({ path: '/auth/chat', query: { userId: friendId } })
    }

    /* 친구 목록 가져오기 */
    onMounted(() => {
      friendStore.fetchFriendList(authUser.userId)
    })

    return {
      userId: authUser.userId,
      defaultUserImage,
      friendList : friendStore.friendList,
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