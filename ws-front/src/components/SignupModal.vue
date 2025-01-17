<template>
    <div v-if="isVisible" class="signup-container">
      <div class="signup-content">
        <span class="signup-title">회원가입</span>
        <form @submit.prevent="signup">
          <BaseInput v-model="signupData.id" type="text" placeholder="아이디" />
          <BaseInput v-model="signupData.nickname" type="text" placeholder="닉네임" />
          <BaseInput v-model="signupData.password" type="password" placeholder="비밀번호" />
          <BaseInput v-model="signupData.cpassword" type="password" placeholder="비밀번호 확인" />
          <BaseButton>가입</BaseButton>
          <BaseButton type="button" class="btn-darkgray" @click="signupClose">닫기</BaseButton>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  
  export default {
    name: "SignupModal",
    data() {
      return {
        signupData: {
          id : "",
          nickname : "",
          password : "",
          cpassword : "",
        }
      };
    },
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
      async signup() {
          if(this.signupData.password !== this.signupData.cpassword) {
            alert ("비밀번호가 일치하지 않습니다.");
            return ;
          }

          /* 회원가입 */
          await this.$api.post("/signup", {
          userId: this.signupData.id,
          userNickName: this.signupData.nickname,
          userPw: this.signupData.password,
          }).then((response) => {
            if (response.status === 200) {
              alert("회원가입이 완료되었습니다.");
              this.signupData = {
                id: "",
                nickname: "",
                password: "",
                cpassword: "",
              };
              this.signupClose();
            }
          })
          .catch((e) => {
            if (e.response) {
              if (e.response.status === 500) {
                console.error('500 : 회원가입을 실패하였습니다.');
              } else if (e.response.status === 400) {
                console.error('400 : ', e.response.data);
              }
            } else {
              console.error('Network error:', e);
            }
          });
      }
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
  