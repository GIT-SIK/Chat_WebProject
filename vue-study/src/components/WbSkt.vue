<template>
  <div class="ws-chat-container">
    <!-- UserCount 컴포넌트 -->
    <user-count :user-count="userCount" />
    <ul>
      <li
        v-for="(message, index) in messages"
        :class="{
          'm-me': message.sender === userId,
          'm-other': message.sender !== userId && message.sender !== 'system',
          'm-system': message.sender === 'system',
        }"
        :key="index"
      >
        {{ message.text }}
      </li>
    </ul>
    <input
      v-model="newMessage"
      @keyup.enter="sendMessage"
      placeholder="메시지를 입력 후 엔터를 클릭해주세요."
    />
  </div>
</template>

<script>
import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'
import UserCount from './WbSktUC.vue'
import axios from 'axios'; 

export default {
  components: {
    UserCount,
  },

  data() {
    return {
      client: null,
      messages: [],
      newMessage: '',
      userId: '',
      userCount: 0,
    }
  },

  methods: {
    // 임시로 랜덤으로 사용자 아이디 부여
    addUserId() {
      let userId = localStorage.getItem('userId') // 시큐리티 대신 localstorage 로 대체
      if (!userId) {
        userId = Math.random().toString(36).slice(2, 11)
        localStorage.setItem('userId', userId)
      }
      this.userId = userId
    },

    connectWebSocket() {
      if (this.client) {
        console.log('이미 클라이언트가 연결됨.')
        return
      }

      const sockJS = new SockJS('/ws')
      this.client = new Client({
        webSocketFactory: () => sockJS,
        //debug: (msg) => console.log(msg),
        /* ************************************* */
        /* Connect 영역 */
        onConnect: () => {
          console.log('WebSocket connected!')
          // 메시지 관리 (/topic/ws1, 메시지 구독) 
          this.client.subscribe('/topic/ws1', (message) => {
            const msg = JSON.parse(message.body)
            // 입장, 퇴장 메시지를 시스템으로 처리 용도 
            if (msg.type === 'notification') {
              this.messages.push({
                sender: 'system',
                text: msg.message,
              })
            } else {
              // 메시지 전달
              this.messages.push(msg)
            }
            this.fetchUserCount();
          })
          this.fetchUserCount();
        },
        /* ************************************* */
        onStompError: (frame) => {
          console.error('STOMP error: ', frame.headers['message'])
        },
        onWebSocketError: (error) => {
          console.error('WebSocket error: ', error)
        },
        onWebSocketClose: (event) => {
          if (!event.wasClean) {
            console.error('WebSocket closed error!!')
            console.log(event.code + ' : ' + event.reason || 'No reason provided')
          } else {
            console.log('WebSocket closed') 
          }
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
      })

      this.client.activate()
    },

    fetchUserCount() {
      axios
        .get('http://localhost:8081/api/uc') // Spring Boot의 REST API 호출
        .then((response) => {
          console.log('Fetched user count:', response.data);
          this.userCount = response.data.userCount; // 사용자 수 업데이트
        })
        .catch((error) => {
          console.error('Failed to fetch user count:', error);
        });
    },
    sendMessage() {
      if (this.newMessage.trim()) {
        const message = {
          sender: this.userId,
          text: this.newMessage,
        }
        this.client.publish({
          destination: '/app/ws1',
          body: JSON.stringify(message),
        })
        this.newMessage = ''
      }
    },
  },


  mounted() {
    this.addUserId()
    this.connectWebSocket()
    console.log('mounted : WS Mounted')
  },

  beforeUnmount() {
    if (this.client) {
      this.client.deactivate()
      console.log('unmount : WS UnMount')
    }
  },
}
</script>

<style>
/* 전체 채팅 컨테이너 스타일 */
.ws-chat-container {
  display: flex;
  width: 100%;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  height: 70vh;
  padding: 20px;
  background-color: #f4f4f9;
  font-family: Arial, sans-serif;
  
}

.ws-chat-container div {
  width: 100%;
  max-width: 500px;
}

/* 메시지 리스트 스타일 */
.ws-chat-container ul {
  width: 100%;
  max-width: 500px;
  flex-grow: 1; /* 가변 크기 설정 */
  overflow-y: auto; /* 스크롤 가능 */
  list-style-type: none;
  margin: 0;
  padding: 0 !important;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

/* 스크롤바 스타일 */
.ws-chat-container ul::-webkit-scrollbar {
  width: 6px;
}

.ws-chat-container ul::-webkit-scrollbar-thumb {
  background: #bbb;
  border-radius: 3px;
}

.ws-chat-container ul::-webkit-scrollbar-thumb:hover {
  background: #999;
}

/* 채팅 메시지 기본 스타일 */
.ws-chat-container li {
  max-width: 70%;
  margin: 11px;
  padding: 10px 15px;
  font-size: 14px;
  line-height: 1.4;
  border-radius: 15px;
  word-wrap: break-word;
  position: relative;
}

/* **** 내 메시지 스타일 **** */
.m-me {
  align-self: flex-end;
  background-color: #d1e7ff;
  margin-left: auto;
  color: #084298;
}

/* 내 메시지 채팅 버블 꼬리 */
.m-me::after {
  content: '';
  position: absolute;
  top: 50%;
  right: -11px;
  transform: translateY(-50%);
  border: 6px solid transparent;
  border-left-color: #d1e7ff;
}

/* **** 상대방 메시지 스타일 **** */
.m-other {
  align-self: flex-start;
  background-color: #f1f1f1;
  margin-right: auto; /* 왼쪽 정렬 */
  color: #333;
}

/* 상대방 메시지 채팅 버블 꼬리 */
.m-other::after {
  content: '';
  position: absolute;
  top: 50%;
  left: -11px;
  transform: translateY(-50%);
  border: 6px solid transparent;
  border-right-color: #f1f1f1;
}

.m-system {
  text-align: center;
  background-color: transparent;
  color: #000000;
  align-self: center;
}

/* 입력창 스타일 */
.ws-chat-container input {
  width: 100%;
  max-width: 500px;
  padding: 12px 15px;
  margin-top: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
  outline: none;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
}

.ws-chat-container input:focus {
  border-color: #007bff;
}
</style>
