<template>
  <nav class="nav-container">
    <h3><router-link to="/"> HOME </router-link></h3>
    <ul>
      <!-- 로그인하지 않은 경우에만 회원가입, 로그인 버튼 표시 -->
      <template v-if="!loginStore.userId">
        <li><BaseButton @click="toggleModal('signup')"> 회원가입</BaseButton></li>
        <li><BaseButton @click="toggleModal('login')"> 로그인</BaseButton></li>
      </template>
      <!-- 로그인한 경우 채팅, 로그아웃 버튼 표시 -->
      <template v-else>
        <li><BaseButton class="btn-darkgray" @click="$router.push('/wspage')">채팅</BaseButton></li>
        <li><BaseButton @click="logout"> 로그아웃</BaseButton></li>
      </template>
    </ul>
  </nav>
  <loginModal
    :isVisible="modals.login"
    @login-close="toggleModal('login', false)"
    @login-success="loginSuccess"
  />
  <signupModal :isVisible="modals.signup" @signup-close="toggleModal('signup', false)" />
</template>

<script>
import { ref } from 'vue'
import { useLoginStore } from '@/store/login' // store 경로는 실제 경로에 맞게 수정해주세요
import loginModal from '../components/LoginModal.vue'
import signupModal from '../components/SignupModal.vue'

export default {
  name: 'NavBar',
  components: {
    loginModal,
    signupModal,
  },
  setup() {
    const loginStore = useLoginStore()
    const modals = ref({
      login: false,
      signup: false,
    })

    const toggleModal = (name, state = true) => {
      if (modals.value[name] !== undefined) {
        modals.value[name] = state
      }
    }

    const loginSuccess = () => {
      toggleModal('login', false)
    }

    const logout = () => {
      // 로그아웃 처리
      localStorage.removeItem('access_token')
      loginStore.token = null
      loginStore.userId = null
      loginStore.role = null
    }

    return {
      modals,
      toggleModal,
      loginStore,
      logout,
      loginSuccess,
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
