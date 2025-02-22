<template>
  <div class="ws-chat-container">
    <!-- UserCount 컴포넌트 -->
    <!-- <user-count :user-count="userCount" /> -->
    <ul class="ws-chat-list">
      <li
        v-for="(message, index) in messages"
        :key="index"
        :class="{
          'm-me': message.sender === userId,
          'm-other': message.sender !== userId && message.sender !== 'system',
          'm-system': message.sender === 'system',
        }"
      >
        <span> {{ message.text }} </span>
        <span> {{ message.date }} </span>
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
import { ref, onMounted, onBeforeUnmount } from 'vue'
import chatService from '@/api/chat'
import api from '@/utils/api'
import UserCount from '../components/ChatRoomUC.vue'

export default {
  components: {
    UserCount,
  },

  setup() {
    const messages = ref([])
    const newMessage = ref('')
    const userId = ref('')
    const userCount = ref(0)

    // 스크롤 자동 스크롤
    const scrollAutoDown = () => {
      const lct = document.querySelector('.ws-chat-list')
      requestAnimationFrame(() => {
        lct.scrollTop = lct.scrollHeight
      })
    }

    // 웹소켓 연결 및 메시지 구독, 전송 포함
    const connectWebSocket = () => {
      const token = localStorage.getItem('access_token') // localStorage에서 토큰을 가져옵니다.
      if (!token) {
        console.error('Token not found')
        return
      }

      userId.value = localStorage.getItem('id')
      chatService.connect('/ws', token, 'ws1') // WebSocket 연결

      // 메시지 수신 이벤트 핸들링
      chatService.addListener('ws1', (msg) => {
        console.log(`수신 : ${msg}`)
        if (msg.date) {
          const date = new Date(msg.date)
          const hours = date.getHours()
          const minutes = date.getMinutes()
          const formattedHours = hours % 12 || 12
          const ampm = hours < 12 ? 'AM' : 'PM'
          const formattedMinutes = minutes.toString().padStart(2, '0')
          msg.date = `${ampm} ${formattedHours}:${formattedMinutes}`
        }
        if (msg.type === 'notification') {
          messages.value.push({
            sender: 'system',
            text: msg.message,
          })
        } else {
          messages.value.push(msg)
        }
        scrollAutoDown()
        fetchUserCount()
      })
    }

    const fetchUserCount = () => {
      api
        .get('/api/uc') // REST API 호출
        .then((response) => {
          userCount.value = response.data.userCount // 사용자 수 업데이트
        })
        .catch((error) => {
          console.error('Failed to fetch UC:', error)
        })
    }

    // 메시지 입력
    const sendMessage = () => {
      if (newMessage.value.trim()) {
        const message = {
          sender: localStorage.getItem('id'),
          text: newMessage.value,
          date: new Date().toISOString(), // UTC 기준
        }
        newMessage.value = ''
        chatService.send('ws1', message) // WebSocket을 통한 메시지 전송
      }
    }

    // 웹소켓 연결
    onMounted(() => {
      connectWebSocket()
      console.log('mounted : WS Mounted')
    })

    // 컴포넌트 언마운트 시 웹소켓 종료
    onBeforeUnmount(() => {
      chatService.close()
      console.log('unmount : WS UnMount')
    })

    return {
      messages,
      newMessage,
      userId,
      userCount,
      sendMessage,
    }
  },
}
</script>

<style>
/* 전체 채팅 컨테이너 스타일 */
.ws-chat-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 70vh;
  font-family: Arial, sans-serif;
}

/* 접속자 수 */
.ws-chat-container div {
  width: 100%;
  max-width: 500px;
}

/* 메시지 리스트 스타일 */
.ws-chat-list {
  width: 100%;
  max-width: 500px;
  flex-grow: 1; /* 가변 크기 설정 */
  overflow-y: auto; /* 스크롤 가능 */
  list-style-type: none;
  margin: 0;
  padding: 0 !important;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.4);
  display: flex;
  flex-direction: column;
}

/* 스크롤바 스타일 */
.ws-chat-list::-webkit-scrollbar {
  width: 6px;
}

.ws-chat-list::-webkit-scrollbar-thumb {
  background: #bbb;
  border-radius: 3px;
}

.ws-chat-list::-webkit-scrollbar-thumb:hover {
  background: #999;
}

/* 채팅 메시지 기본 스타일 */

.ws-chat-list li {
  max-width: 100%;
  margin: 11px;
  display: flex;
  flex-direction: column;
}

/* 메시지 스타일 총괄 */
.ws-chat-list span:nth-child(1) {
  padding: 10px 15px;
  font-size: 14px;
  line-height: 1.4;
  border-radius: 15px;
  word-wrap: break-word;
  position: relative;
}

/* 메시지 스타일 시간 */
.ws-chat-list span:nth-child(2) {
  font-size: 12px;
}

/* **** 내 메시지 스타일 **** */
.m-me span {
  margin-left: auto;
}

.m-me span:nth-child(1) {
  align-self: flex-end;
  background-color: #D7CCC8;
  margin-left: auto;
  color: #3E2723;
}

/* 내 메시지 채팅 버블 꼬리 */
.m-me span:nth-child(1)::after {
  content: '';
  position: absolute;
  top: 50%;
  right: -11px;
  transform: translateY(-50%);
  border: 6px solid transparent;
  border-left-color: #D7CCC8;
}

/* **** 상대방 메시지 스타일 **** */

.m-other span {
  margin-right: auto;
}

.m-other span:nth-child(1) {
  align-self: flex-start;
  background-color: #f1f1f1;
  color: #333;
}

/* 상대방 메시지 채팅 버블 꼬리 */
.m-other span:nth-child(1)::after {
  content: '';
  position: absolute;
  top: 50%;
  left: -11px;
  transform: translateY(-50%);
  border: 6px solid transparent;
  border-right-color: #f1f1f1;
}

.m-system span:nth-child(1) {
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
  box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.3);
}

.ws-chat-container input:focus {
  border-color: #8D6E63;
}
</style>
