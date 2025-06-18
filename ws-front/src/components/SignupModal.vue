<template>
  <div v-if="isVisible" class="signup-container">
    <div class="signup-content">
      <span class="signup-title">회원가입</span>
      <form @submit.prevent="signup">
        <div class="signup-sub-container">
          <BaseInput
            v-model="signupData.id"
            :borderColor="validationColors.id"
            type="text"
            placeholder="아이디"
          />
          <BaseButton type="button" @click="checkUser('id')">중복 확인</BaseButton>
        </div>
        <div class="signup-sub-container">
          <BaseInput
            v-model="signupData.nickname"
            :borderColor="validationColors.nickname"
            type="text"
            placeholder="닉네임"
          />
          <BaseButton type="button" @click="checkUser('nickname')">중복 확인</BaseButton>
        </div>
        <BaseInput v-model="signupData.password" type="password" placeholder="비밀번호" />
        <BaseInput v-model="signupData.cpassword" type="password" placeholder="비밀번호 확인" />
        <div class="signup-sub-container" style="margin-top: 10px">
          <BaseButton>가입</BaseButton>
          <BaseButton type="button" class="btn-darkgray" @click="signupClose">닫기</BaseButton>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref, watch, inject } from 'vue'
import { signupApi, checkUserApi } from '@/api/signup'

export default {
  name: 'SignupModal',
  props: {
    isVisible: {
      type: Boolean,
      default: false,
    },
  },
  setup(props, { emit }) {
    const showToast = inject('showToast')
    // 유효성 상태
    const validationColors = ref({
      id: 'default',
      nickname: 'default',
    })

    // 회원가입 데이터
    const signupData = ref({
      id: '',
      nickname: '',
      password: '',
      cpassword: '',
    })

    // watch
    watch(
      () => signupData.value.id,
      (newVal, oldVal) => {
        if (newVal !== oldVal) {
          validationColors.value.id = 'default'
        }
      },
    )

    watch(
      () => signupData.value.nickname,
      (newVal, oldVal) => {
        if (newVal !== oldVal) {
          validationColors.value.nickname = 'default'
        }
      },
    )

    // 아이디, 닉네임 중복여부 체크
    const checkUser = async (cType) => {
      try {
        if (signupData.value[cType].length > 2 && signupData.value[cType].length < 9) {
          const response = await checkUserApi(cType, signupData.value[cType])
          validationColors.value[response.data.type] = response.data.data ? 'red' : 'green'
        } else {
          if (cType === 'id') {
            showToast('아이디는 3~8자만 사용 가능합니다.')
          } else {
            showToast('닉네임는 3~8자만 사용 가능합니다.')
          }
          validationColors.value[cType] = 'red'
        }
      } catch (error) {}
    }

    // 회원가입
    const signup = async () => {
      if (validationColors.value.id !== 'green' || validationColors.value.nickname !== 'green') {
        showToast('아이디와 닉네임을 확인해주세요.')
        return
      }

      if (signupData.value.password !== signupData.value.cpassword) {
        showToast('비밀번호가 일치하지 않습니다.')
        return
      }
      console.log(signupData.value.password.length)
      if (!(signupData.value.password.length > 3 && signupData.value.password.length < 16)) {
        showToast('비밀번호는 4~15자만 가능합니다.')
        return
      }

      try {
        const response = await signupApi(
          signupData.value.id,
          signupData.value.password,
          signupData.value.nickname,
        )
        if (response.data) {
          showToast('회원가입이 완료되었습니다.')
          signupData.value = {
            id: '',
            nickname: '',
            password: '',
            cpassword: '',
          }
          signupClose()
        } else {
          showToast('회원가입이 실패하였습니다.')
        }
      } catch (e) {
        if (e.response) {
          if (e.response === 500) {
            showToast('회원가입을 실패하였습니다.')
          } else if (e.response.status === 400) {
            console.error('400 : ', e.response.data)
          }
        } else {
          console.error('Network error:', e)
        }
      }
    }

    // 모달 닫기
    const signupClose = () => {
      emit('signup-close')
    }

    return {
      validationColors,
      signupData,
      checkUser,
      signup,
      signupClose,
    }
  },
}
</script>

<style scoped>
.signup-title {
  display: block;
  margin: 0px 20px 20px 20px;
}

.signup-container {
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

.signup-container form > * {
  margin-bottom: 15px;
  width: 100%;
}

.signup-content {
  background: white;
  padding: 30px;
  border-radius: 8px;
  width: 300px;
  min-height: 300px;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.signup-sub-container {
  display: flex;
}

.signup-sub-container .base-input {
  margin-right: 5px;
}
</style>
