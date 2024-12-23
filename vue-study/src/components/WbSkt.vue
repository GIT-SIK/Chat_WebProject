<template>
  <div>
    <h1>입력</h1>
    <input
      v-model="newMessage"
      @keyup.enter="sendMessage"
      placeholder="메시지를 입력 후 엔터를 클릭해주세요."
    />
    <h1>출력</h1>
    <ul>
      <li
        v-for="(message, index) in messages"
        :class="{ 'm-me': message.sender === userId, 'm-other': message.sender !== userId }"
        :key="index"
      >
        {{ message.text }}
      </li>
    </ul>
  </div>
</template>

<script>
import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'

export default {
  data() {
    return {
      client: null,
      messages: [],
      newMessage: '',
      userId: '',
    }
  },

  methods: {
    addUserId() {
      let userId = localStorage.getItem('userId') // 시큐리티 대신 localstorage 로 대체
      if (!userId) {
        userId = Math.random().toString(36).slice(2, 11)
        localStorage.setItem('userId', userId)
      }
      this.userId = userId
    },

    connectWebSocket() {
      const sockJS = new SockJS('http://localhost:8081/ws')
      this.client = new Client({
        webSocketFactory: () => sockJS,
        debug: (msg) => console.log(msg),
        onConnect: () => {
          console.log('WebSocket connected!')
          // /topic/ws1 구독
          this.client.subscribe('/topic/ws1', (message) => {
            this.messages.push(JSON.parse(message.body))
          })
        },
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
ul {
  min-width: 30%;
  width: 70%;
  list-style-type: none;
}

li {
  height: 35px;
}

.m-me {
  text-align: right;
  color: darkblue;
}
.m-other {
  text-align: left;
  color: black;
}
</style>
