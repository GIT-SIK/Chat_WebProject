<template>
  <div class="ws-chat-container">
  <template v-if="roomId !== null">
    <div class="ws-chat-otherid">
    {{ otherUserId }}
    </div>
    <ul class="ws-chat-list">
        <li
          v-for="(message, index) in messages"
          :key="index"
          :class="{
            'm-me': message.senderUserId === senderUserId,
            'm-other': message.senderUserId !== senderUserId && message.sender !== 'system',
            'm-system': message.senderUserId === 'system',
          }"
        >
          <span> {{ message.message }} </span>
          <span> {{ message.date }} </span>
        </li>
    </ul>
      <input
        v-model="newMessage"
        @keyup.enter="sendMessage"
        placeholder="메시지를 입력 후 엔터를 클릭해주세요."
      />
  </template>
  <template v-else>
    <div class="ws-chat-noid">
     채팅방을 선택해주세요.
    </div>
  </template>
    <!-- <template v-else>
      <input placeholder="채팅방을 선택해주세요.">
    </template> -->
  </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount, watch, computed, nextTick } from 'vue'
import chatService from '@/api/chat'
import {useChatStore} from '@/store/chat'
import {useUserStore} from '@/store/user'
import { storeToRefs } from 'pinia'

export default {
  setup() {
    /*
    ChatDto
      private String roomId;
      private String senderUserId;
      private String message;
      private LocalDateTime date;
    */
    /* Store Data */
    const chatStore = useChatStore();
    const userStore = useUserStore();
    const {roomId, otherUserId} = storeToRefs(chatStore);
    /* View Data */
    const messages = ref([])
    const newMessage = ref('')
    const senderUserId = ref(null)

    // 스크롤 자동 스크롤
    const scrollAutoDown = () => {
      /* DOM 업데이트 후 함수 실행 */
      nextTick(() => {
        const lct = document.querySelector('.ws-chat-list')
        if (!lct) return
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
      chatService.connect('/ws', token, roomId.value) // WebSocket 연결
    }

    // 이전 메시지 목록 가져와서 date 포멧팅
    const olderMessages = computed(() =>
      chatStore.olderMessages.map((omsg) => ({
        ...omsg,
        date: formatDate(omsg.date),
      }))
    );

    // 구독
    watch(roomId, (newRoomId, oldRoomId) => {
      if (newRoomId !== oldRoomId) {
        chatService.unsubscribe(oldRoomId) // 기존 구독 제거
        chatService.subscribe(newRoomId); // 구독 추가
        chatService.addListener(newRoomId, (msg) => {
        if (msg.date) {
          msg.date = formatDate(msg.date)
        }
        if (msg.type === 'notification') {
          messages.value.push({
            senderUserId: 'system',
            message: msg.message,
          })
        } else {
          messages.value.push(msg)
          scrollAutoDown()
        }
       })
       messages.value = olderMessages.value;
       scrollAutoDown()
      }  else {
          console.log("이미 채팅방에 접속 중 입니다.")
      }
    });

    // 메시지 입력
    const sendMessage = () => {
      if (newMessage.value.trim()) {
        const message = {
          roomId : roomId.value,
          senderUserId: senderUserId.value,
          message: newMessage.value,
          date: new Date().toISOString(), // UTC 기준
        }

        newMessage.value = ''
        chatService.send(message) // WebSocket을 통한 메시지 전송
      }
    }

    // 웹소켓 연결
    onMounted(() => {
      connectWebSocket()
      userStore.getUserInfo();
      senderUserId.value = userStore.userId;
      roomId.value = chatStore.roomId
    })

    // 컴포넌트 언마운트 시 웹소켓 종료
    onBeforeUnmount(() => {
      chatService.close()
      roomId.value = null;
    })

    /* 날짜 변환 */
    const formatDate = (isoDate) => {
      if (!isoDate) return "";
      return new Date(isoDate).toLocaleString("ko-KR", {
        month: "long",
        day: "numeric",
        hour: "2-digit",
        minute: "2-digit",
        hour12: true, 
      });
    };

    return {
      messages,
      roomId,
      newMessage,
      otherUserId,
      senderUserId,
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
  border-radius: 4px;
  box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.4);
}

.ws-chat-otherid {
  min-height : 48px;
  width: 100%;
  display : flex;
  justify-content : center;
  align-items : center;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  background-color: #A1887F;
  color : white;
}

.ws-chat-noid {
  display : flex;
  justify-content : center;
  align-items : center;
  height: 100%
}

/* 메시지 리스트 스타일 */
.ws-chat-list {
  width: 100%;
  flex-grow: 1; /* 가변 크기 설정 */
  overflow-y: auto; /* 스크롤 가능 */
  list-style-type: none;
  margin: 0;
  padding: 0 !important;
  background-color: #ffffff;
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
  padding: 12px 15px;
  margin-top: 10px;
  font-size: 14px;
  border-top: 1px solid #ccc;
  border-radius: 4px;
  outline: none;
}

</style>
