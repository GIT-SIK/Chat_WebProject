<template>
  <nav class="nav-container">
    <h3><router-link to="/"> HOME </router-link></h3>
    <ul>
      <!-- 로그인하지 않은 경우에만 회원가입, 로그인 버튼 표시 -->
      <template v-if="!loginStore.userId">
        <li><BaseButton @click="toggleModal('signup')"> 회원가입</BaseButton></li>
      </template>
      <!-- 로그인한 경우 채팅, 로그아웃 버튼 표시 -->
      <template v-else>
        <li>
          <BaseButton class="btn-darkgray" @click="$router.push('/auth/ws')">채팅</BaseButton>
        </li>
        <li><BaseButton @click="$router.push('/auth/fl')"> 친구 목록 </BaseButton></li>
        <li><BaseButton @click="logout"> 로그아웃</BaseButton></li>
      </template>
    </ul>
  </nav>
  <signupModal :isVisible="modals.signup" @signup-close="toggleModal('signup', false)" />
</template>

<script>
import { ref, inject } from 'vue'
import { useRouter } from 'vue-router'
import { useLoginStore } from '@/store/login'

export default {
  name: 'NavBar',
  setup() {
    const router = useRouter()
    const loginStore = useLoginStore()
    const showToast = inject('showToast')
    const modals = ref({
      login: false,
      signup: false,
    })
    const logout = () => {
      showToast('다음에 또 만나요!')
      localStorage.removeItem('access_token')
      loginStore.token = null
      loginStore.userId = null
      loginStore.isAdmin = null
      router.push({ path: '/' })
    }

    return {
      modals,
      loginStore,
      logout,
    }
  },
}
</script>

<style scoped>
.nav-container {
  background-color: #333;
  color: white;
  padding: 10px;
  display: flex;
  justify-content: space-between;
}

.nav-container h3 {
  margin: 0;
}

.nav-container ul {
  list-style-type: none;
  display: flex;
  gap: 10px;
  margin: 0;
  justify-content: flex-end;
}

.nav-container li {
  margin: 0;
}

.nav-container a {
  color: white;
  text-decoration: none;
}

.nav-container a:hover {
  text-decoration: underline;
}
</style>
