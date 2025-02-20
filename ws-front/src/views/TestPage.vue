<template>
  <div>
    <base-button class="btn-darkgray" size="large" @click="checkUser"> 토큰 확인 </base-button>
    <base-button class="btn-darkgray" size="large" @click="checkRoom"> USER1 방 확인 </base-button>
    <base-button size="large" @click="$router.push('/')"> HOME </base-button>
  </div>
</template>

<script>
import { onMounted, inject } from 'vue'
import api from '@/utils/api'
import * as chat from '@/api/chat'
import { useRouter } from 'vue-router'

export default {
  name: 'TestPage',
  setup() {
    const showToast = inject('showToast')
    const router = useRouter();


    const checkRoom = async() => {
      try {
        const response = await chat.getSearchChatRoomApi("USER1")

        console.log(response);
        showToast("ROOM ID : " + response.data.roomId)
      } catch(e) { console.log("(TestPage.vue) 채팅방 체크 중 에러 발생 - " + e )}
    }


    // Token 인증된 사용자 여부 파악
    const checkUser = async () => {
      try {
        const token = localStorage.getItem('access_token')

        if (!token) {
          showToast('JWT 토큰이 없습니다.')
          return
        }

        const response = await api.post(
          '/api/auth/info',
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          },
        )

        showToast(`인가된 사용자입니다: ${response.data.userId}`)
      } catch (error) {
        console.error(error)
        showToast('인가되지 않은 사용자입니다.')
      }
    }

    onMounted(() => {
      // console.log(this.$api) // API 상태 확인용 코드
    })

    return {
      checkUser,
      checkRoom,
      router
    }
  },
}
</script>

<style scoped>
.status-message {
  margin-top: 20px;
  font-size: 16px;
}
</style>
