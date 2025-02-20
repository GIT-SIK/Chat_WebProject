<template>
  <v-card :disabled="loading" :loading="loading" class="mx-auto login-container" max-width="380">
    <template v-slot:loader="{ isActive }">
      <v-progress-linear
        :active="isActive"
        color="brown-darken-2"
        height="4"
        indeterminate
      ></v-progress-linear>
    </template>

    <v-card-title color="brown-darken-2"> Login </v-card-title>
    <v-container>
      <v-text-field
        color="brown-darken-4"
        label="ID"
        v-model="loginData.id"
        variant="underlined"
      ></v-text-field>
      <v-text-field
        color="brown-darken-4"
        type="password"
        label="Password"
        v-model="loginData.pw"
        variant="underlined"
      ></v-text-field>
    </v-container>
    <v-card-actions>
      <v-btn color="brown-darken-4" text="로그인" block border @click="login"></v-btn>
    </v-card-actions>
    <v-container class="signup-component-container">
      <v-label class="signup-label" @click="toggleModal('signup')"> 회원이 아니신가요? </v-label>
      <signupModal :isVisible="modals.signup" @signup-close="toggleModal('signup', false)" />
    </v-container>
  </v-card>
</template>
<script>
import { useRouter } from 'vue-router'
import { loginApi } from '@/api/login'
import { useLoginStore } from '@/store/login'
import signupModal from '../components/SignupModal.vue'
import { ref, inject } from 'vue'

export default {
  components: {
    signupModal,
  },
  setup() {
    const loading = ref(false)
    const showToast = inject('showToast')
    const loginData = ref({ id: '', pw: '' })
    const router = useRouter()
    const loginStore = useLoginStore()
    const modals = ref({ signup: false })

    const toggleModal = (name, state = true) => {
      if (modals.value[name] !== undefined) {
        modals.value[name] = state
      }
    }

    const login = async () => {
      loading.value = true
      await loginApi(loginData.value.id, loginData.value.pw)
        .then((response) => {
          loginStore.setToken(response.data.token)
          localStorage.setItem('access_token', response.data.token)
          loginStore.getUserInfo()
          showToast(loginData.value.id + `님! 환영합니다.`)
          loading.value = false
          router.push({ path: '/auth' })
        })
        .catch((e) => {
          loading.value = false
          showToast(e.data || '서버와 연결을 확인해주세요.')
          console.log(e)
        })
    }

    return {
      toggleModal,
      signupModal,
      login,
      modals,
      loginData,
      loading,
      router,
    }
  },
}
</script>

<style>
.login-container {
  top: 15vh;
}

.signup-component-container {
  text-align: right;
}
.signup-label {
  font-size: 14px !important;
}
</style>
