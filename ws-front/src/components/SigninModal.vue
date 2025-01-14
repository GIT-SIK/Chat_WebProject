<template>
    <div v-if="isVisible" class="signin-container">
      <div class="signin-content">
        <span class="signin-title">Sign In</span>
        <BaseInput type="text" placeholder="아이디" />
        <BaseInput type="password" placeholder="비밀번호" />
        <BaseButton  @click="signin">로그인</BaseButton>
        <BaseButton class="close-btn" @click="signinClose">닫기</BaseButton>
      </div>
    </div>
  </template>
  
  <script>
  
  export default {
    name: "SigninModal",
    props: {
      isVisible: {
        type: Boolean,
        default: false,
      },
    },
    methods: {
      signinClose() {
        this.$emit("signin-close");
      },
      signin() {
        
      },
    },
  };
  </script>
  
  <style scoped>
  .signin-title {
    display : block;
    margin : 0px 20px 20px 20px;
  }
  .base-input {
    margin-bottom: 10px; 
  }
  .btn {
    margin-top : 10px;
  }

  .signin-container {
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
  
  .signin-content {
    background: white;
    padding: 30px;
    border-radius: 8px;
    width: 300px;
    min-height: 200px;
    text-align: center;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }
  
  .close-btn {
    padding: 5px 10px;
    border: none;
    background: transparent;
    color: #888;
    cursor: pointer;
    font-size: 12px;
  }
  </style>
  