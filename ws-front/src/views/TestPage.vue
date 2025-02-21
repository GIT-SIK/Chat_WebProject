<template>
  <div>
    <base-button class="btn-darkgray" size="large" @click="checkUser"> 토큰 확인 </base-button>
    <v-divider></v-divider>
    <div class="room-info">
      <v-text-field class= "other-user-id-txt" v-model="otherUserId" label="OTHER USER ID"> </v-text-field>
      <base-button class="btn-darkgray" size="large" @click="checkRoomInfo"> 방 생성 및 방 데이터 확인 </base-button>
     </div>
        <base-button class="btn-darkgray" size="large" @click="checkRoomList"> 채팅방 리스트 확인 </base-button>
    <v-divider></v-divider>
    <base-button size="large" @click="$router.push('/')"> HOME </base-button>
  </div>
</template>

<script>
import { onMounted, inject, ref } from 'vue'
import api from '@/utils/api'
import * as chat from '@/api/chat'
import { useRouter } from 'vue-router'

export default {
  name: 'TestPage',
  setup() {
    const showToast = inject('showToast')
    const router = useRouter();
    const otherUserId = ref('');


    const checkRoomInfo = async() => {
      try {
        console.log("채팅방 정보 : " + otherUserId.value + " 회원과의 방 정보를 가져옵니다.")
        const response = await chat.getChatRoomInfoApi(otherUserId.value);

        console.log(response);
        showToast("ROOM ID : " + response.data.roomId)
      } catch(e) { console.log("(TestPage.vue) 채팅방 체크 중 에러 발생 - " + e )}
    }


    const checkRoomList = async() => {
      try {
        const response = await chat.getChatRoomListApi();
        console.log(response)
      } catch (e) {
        console.log("(TestPage.vue) 채팅방 리스트 불러오는 중 에러 발생 - " + e )
      }
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
      checkRoomInfo,
      checkRoomList,
      router,
      otherUserId
    }
  },
}
</script>

<style scoped>
.status-message {
  margin-top: 20px;
  font-size: 16px;
}

.room-info {
  display : flex;
  width : 300px;
}

.v-divider {
  padding : 10px 0px 10px 0;
}
</style>
