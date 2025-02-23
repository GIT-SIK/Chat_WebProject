import { Client } from '@stomp/stompjs'
import api from '@/utils/api'

const BASE_URL = 'http://localhost:8081/api'

class WebSocketService {
  constructor() {
    this.socket = null
    this.listeners = {}
  }

  connect(endpoint, token, roomId) {
    // stomp.js 설정
    this.socket = new Client({
      brokerURL: `${BASE_URL}${endpoint}`,
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      onConnect: () => {
        console.log(`WebSocket connected to ${BASE_URL}${endpoint}`)
        this.subscribe(roomId)
      },
      onDisconnect: () => {
        console.log('Disconnected from WebSocket.')
      },
      onStompError: (frame) => {
        console.error(`STOMP Error: ${frame.headers['message']}`)
      },
      debug: (msg) => {
        console.log(`debug : ${msg}`)
      },
    })

    this.socket.activate()
  }

  // 구독 메시지 처리
  subscribe(roomId) {
    this.socket.subscribe(`/api/chat/receive/${roomId}`, (message) => {
      const msg = JSON.parse(message.body)
      if (this.listeners[roomId]) {
        this.listeners[roomId](msg) // 해당 topic에 msg값 넘김
      } else {
        console.log('지정된 topic이 없습니다.')
      }
    })
  }

  // 구독자 추가
  addListener(roomId, callback) {
    this.listeners[roomId] = callback
  }

  // 메시지 send
  send(data) {
    if (this.socket && this.socket.connected) {
      console.log(data)
      this.socket.publish({
        destination: `/api/chat/send`,
        body: JSON.stringify(data), // 메시지 데이터
      })
    } else {
      console.error('웹 소켓이 연결되지 않았습니다.')
    }
  }

  // 연결 종료
  close() {
    if (this.socket) {
      this.socket.deactivate()
    }
  }
}

const chatService = new WebSocketService()

export default chatService


/* ********** API ********** */

export async function getChatRoomInfoApi(otherUserId) {
  return await api.get('/api/chat/join', { params: { v : otherUserId }})
}

export async function getChatRoomListApi() {
  return await api.get('/api/chat/list')
}