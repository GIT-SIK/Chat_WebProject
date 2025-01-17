<template>
    <div v-if="isVisible" class="signup-container">
      <div class="signup-content">
        <span class="signup-title">회원가입</span>
        <form @submit.prevent="signup">
        
          <div class="signup-sub-container">
          <BaseInput v-model="signupData.id" :borderColor="validationColors.id" type="text" placeholder="아이디" />
          <BaseButton type="button" @click="userChk('id')">중복 확인</BaseButton>
          </div>
          <div class="signup-sub-container">
          <BaseInput v-model="signupData.nickname" :borderColor="validationColors.nickname" type="text" placeholder="닉네임" />
          <BaseButton type="button" @click="userChk('nick')">중복 확인</BaseButton>
          </div>
          <BaseInput v-model="signupData.password" type="password" placeholder="비밀번호" />
          <BaseInput v-model="signupData.cpassword" type="password" placeholder="비밀번호 확인" />
          <div class="signup-sub-container" style="margin-top : 10px">
          <BaseButton>가입</BaseButton>
          <BaseButton type="button" class="btn-darkgray" @click="signupClose">닫기</BaseButton>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  
  export default {
    name: "SignupModal",
    data() {
      return {
        // 유효성 상태 (red, green, default)
        validationColors: {
        id: 'default',
        nickname: 'default', 
        },

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

    watch: {
    // 아이디 입력값 변경 감지
    "signupData.id"(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.validationColors.id = 'default';
      }
    },
    "signupData.nickname"(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.validationColors.nickname = 'default';
      }
    },
  },

    methods: {

    // 아이디, 닉네임 중복여부  
      async userChk(cType){
        let requestData = {};
        if(cType === 'id' && this.signupData.id != "") {
          requestData = { userId: this.signupData.id };
        } else if(cType === 'nick' && this.signupData.nickname != "") {
          requestData = { userNickName: this.signupData.nickname };
        }

        if (Object.keys(requestData).length !== 0) {
        await this.$api.post("/userchk", requestData)
        .then((response) => {
          if(response.data.id !== undefined) {
            if(response.data.id){
              this.validationColors.id = 'red'
            } else {
               this.validationColors.id = 'green'
            }
          } else if(response.data.nickname !== undefined) {
            if(response.data.nickname){
              this.validationColors.nickname = 'red'
            } else {
               this.validationColors.nickname = 'green'
            }
          }
        })
      }
      },

      // *** 회원 가입
      async signup() {
          if(this.validationColors.id !== 'green' || this.validationColors.nickname !== 'green') {
            alert ("아이디와 닉네임을 확인해주세요.");
            return;
          }

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
      },
                // *** 모달 닫기
      signupClose(){
        this.$emit("signup-close");
      },
    },
  };
  </script>
  
  <style scoped>
    .signup-title {
    display : block;
    margin : 0px 20px 20px 20px;
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
    display : flex;
  }

  .signup-sub-container .base-input {
    margin-right : 5px;
  }
  </style>
  