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
      <li v-for="(message, index) in messages" :key="index">{{ message }}</li>
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
    }
  },

  methods: {
    connectWebSocket() {
      const sockJS = new SockJS('http://localhost:8081/ws')
      this.client = new Client({
        webSocketFactory: () => sockJS,
        debug: (msg) => console.log(msg),
        onConnect: () => {
          console.log('WebSocket connected!')
          // /topic/ws1 구독
          this.client.subscribe('/topic/ws1', (message) => {
            this.messages.push(message.body)
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
            console.log('Code:', event.code) // 종료 상태 코드
            console.log('Reason:', event.reason || 'No reason provided')
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
        this.client.publish({
          destination: '/app/ws1',
          body: this.newMessage,
        })
        this.newMessage = ''
      }
    },
  },

  mounted() {
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
