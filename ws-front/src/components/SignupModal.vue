<template>
    <div v-if="isVisible" class="signup-container">
      <div class="signup-content">
        <span class="signup-title">회원가입</span>
        <form @submit.prevent="signup">
          <BaseInput type="text" placeholder="아이디" />
          <BaseInput type="text" placeholder="닉네임" />
          <BaseInput type="password" placeholder="비밀번호" />
          <BaseInput type="password" placeholder="비밀번호 확인" />
          <BaseButton @click="signup">가입</BaseButton>
          <BaseButton class="btn-darkgray" @click="signupClose">닫기</BaseButton>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  
  export default {
    name: "SignupModal",
    props: {
      isVisible: {
        type: Boolean,
        default: false,
      },
    },
    methods: {
      signupClose(){
        this.$emit("signup-close");
      },
      signup() {
        
      },
    },
  };
  </script>
  
  <style scoped>
    .signup-title {
    display : block;
    margin : 0px 20px 20px 20px;
  }
  .base-input {
    margin-bottom: 10px; 
  }
  .btn {
    margin-top : 10px;
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
  </style>
  