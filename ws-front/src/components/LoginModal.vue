<template>
  <div v-if="isVisible" class="login-container">
    <div class="login-content">
      <span class="login-title">Login</span>
      <BaseInput type="text" v-model="loginData.id" placeholder="아이디" />
      <BaseInput type="password" v-model="loginData.pw" placeholder="비밀번호" />
      <BaseButton @click="login">로그인</BaseButton>
      <BaseButton class="btn-darkgray" @click="loginClose">닫기</BaseButton>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { loginApi } from '@/api/login'
import { useLoginStore } from '@/store/login'
import { ref } from 'vue'

export default {
  name: 'LoginModal',
  props: {
    isVisible: {
      type: Boolean,
      default: false,
    },
  },
  setup(props, { emit }) {
    const loginData = ref({ id: '', pw: '' })
    const router = useRouter()
    const loginStore = useLoginStore()

    const loginClose = () => {
      emit('login-close')
    }

    const login = async () => {
      await loginApi(loginData.value.id, loginData.value.pw)
        .then((response) => {
          loginStore.setToken(response.data.token)
          localStorage.setItem('access_token', response.data.token)
          loginStore.getUserInfo()
          emit('login-success')
          router.push({ path: '/' })
        })
        .catch((e) => {
          alert(e.data)
          console.log(e)
        })
    }

    return {
      loginData,
      loginClose,
      login,
    }
  },
}
</script>

<style scoped>
.login-title {
  display: block;
  margin: 0px 20px 20px 20px;
}
.base-input {
  margin-bottom: 10px;
}
.btn {
  margin-top: 10px;
}

.login-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  z-index: 1000;
  justify-content: center;
  align-items: center;
}

.login-content {
  background: white;
  padding: 30px;
  border-radius: 8px;
  width: 300px;
  min-height: 200px;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>
