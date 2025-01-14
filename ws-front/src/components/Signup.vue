<template>
    <div class="signup-container">
      <span>Sign Up</span>
      <form @submit.prevent="signup">
        <BaseInput type="text" placeholder="아이디" />
        <BaseInput type="text" placeholder="닉네임" />
        <BaseInput type="password" placeholder="비밀번호" />
        <BaseInput type="password" placeholder="비밀번호 확인" />
        <BaseButton>회원가입</BaseButton>
      </form>
    </div>
  </template>
  
  <script>
  
  export default {
    name: "SignUp",
    methods: {
      signup() {
        
      },
    },
  };
  </script>
  
  <style scoped>
  .signup-container {
    max-width: 400px;
    margin: 50px auto;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    text-align: center;
  }
  
  .signup-container form > * {
    margin-bottom: 15px;
    width: 100%;
  }
  </style>
  