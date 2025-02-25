import { Client } from '@stomp/stompjs'
import api from '@/utils/api'

const BASE_URL = 'http://localhost:8081/api'

class WebSocketService {
  constructor() {
    this.socket = null
    this.listeners = {} // 구독자 콜백 함수 저장 
    this.subscriptions = {} // 구독 저장
  }

  connect(endpoint, token, roomId) {
    if (this.socket && this.socket.active) {
      console.log('이미 웹소켓이 연결되어 있습니다.');
      return;
  }
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

  // 구독 추가
  subscribe(roomId) {
    this.unsubscribe(roomId)
    this.subscriptions[roomId] = this.socket.subscribe(`/api/chat/receive/${roomId}`, (message) => {
      const msg = JSON.parse(message.body)
      if (this.listeners[roomId]) {
        this.listeners[roomId](msg)
      }
    })
  }

  // 구독 해제
  unsubscribe(roomId) {
    if (this.subscriptions[roomId]) {
      this.subscriptions[roomId].unsubscribe(); 
      delete this.subscriptions[roomId]; 
    }
  }

  // 구독방 추가
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

  closeAll() {
    this.sockets.forEach((socket) => {
      if (socket && socket.active) {
        socket.deactivate();
        console.log('WebSocket 연결이 종료되었습니다.');
      }
    });
  }
  // 연결 종료
  close() {
    if (this.socket) {

      /* 구독 전체 제거 */
      Object.keys(this.subscriptions).forEach((roomId) => {
        this.unsubscribe(roomId);
      });

      /* 구독방 초기화 */
      this.listeners = {}

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