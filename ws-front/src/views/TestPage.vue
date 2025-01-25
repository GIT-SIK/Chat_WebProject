<template>
  <div>
    <base-input></base-input>
    <base-button class="btn-darkgray" size="large" @click="checkUser"> 확인 </base-button>
    <p v-if="message" class="status-message">{{ message }}</p>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import api from '@/utils/api'

export default {
  name: 'TestPage',
  setup() {
    const message = ref('')

    // Token 인증된 사용자 여부 파악
    const checkUser = async () => {
      try {
        const token = localStorage.getItem('access_token')

        if (!token) {
          message.value = 'JWT 토큰이 없습니다.'
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

        message.value = `인가된 사용자입니다: ${response.data.userId}`
      } catch (error) {
        console.error(error)
        message.value = '인가되지 않은 사용자입니다.'
      }
    }

    onMounted(() => {
      // console.log(this.$api) // API 상태 확인용 코드
    })

    return {
      message,
      checkUser,
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
