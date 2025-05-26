<template>
  <v-container>
    <v-row>
      <v-col cols="12" sm="4">
        <v-card class="pa-4" style="background-color: #adc178" flat>
          <v-card-title>친구 수</v-card-title>
          <v-card-text class="text-h5">{{ friendCount }}명</v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" sm="4">
        <v-card class="pa-4" style="background-color: #d4a373" flat>
          <v-card-title>친구 요청</v-card-title>
          <v-card-text class="text-h5">{{ requestCount }}건</v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" sm="4">
        <v-card class="pa-4" style="background-color: #a98467" flat>
          <v-card-title>대화 수</v-card-title>
          <v-card-text class="text-h5">{{ chatRoomCount }}개</v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <v-row class="mt-4">
      <v-col cols="12">
        <v-card variant="flat">
          <v-card-title>최근 대화 목록</v-card-title>
          <v-divider></v-divider>
          <v-list v-if="recentMessages.length">
            <v-list-item
              v-for="(msg, i) in recentMessages"
              :key="i"
              @click="goToChatRoom(msg.user)"
            >
              <v-list-item-title>
                {{ msg.user }}
              </v-list-item-title>
              <v-list-item-subtitle>
                {{ msg.text }}
              </v-list-item-subtitle>
            </v-list-item>
          </v-list>
          <v-card-text v-else>최근 대화가 없습니다.</v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { useFriendStore } from '@/store/friend'
import * as chat from '@/api/chat'
import * as friend from '@/api/friend'

const router = useRouter()
const userStore = useUserStore()
const friendStore = useFriendStore()

const userUuid = userStore.userUuid
const friendCount = ref(0)
const requestCount = ref(0)
const chatRoomCount = ref(0)
const recentMessages = ref([])

const fetchDashboardData = async () => {
  /* 친구 수 */
  await friendStore.fetchFriendList(userUuid)
  friendCount.value = friendStore.friendList.length

  /* 받은 요청 수 */
  const response = await friend.getFriendListApi(userUuid)
  requestCount.value = response.data.filter(
    (f) => f.friendStatus === 'PENDING' && !f.isSender,
  ).length

  /* 채팅방 */
  const chatResponse = await chat.getChatRoomListApi()
  chatRoomCount.value = chatResponse.data.length

  /* 최근 대화내역 */
  recentMessages.value = chatResponse.data.slice(0, 3).map((room) => ({
    user: room.otherUserId,
    text: room.lastMessage,
  }))
}

const goToChatRoom = (friendUuid) => {
  router.push({ path: '/auth/chat', query: { fu: friendUuid } })
}

onMounted(fetchDashboardData)
</script>
