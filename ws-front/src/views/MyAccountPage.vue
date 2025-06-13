<template>
  <v-container>
    <v-card variant="text" style="background-color: #ffffff">
      <v-card-title>My Account</v-card-title>
      <v-card-text>
        닉네임 : {{ nickname }}<br />
        가입일 : {{ formatDate(userCreatedAt) }}
      </v-card-text>
      <v-card>
        <v-card-text>
          프로필 검색 공개 여부
          <BaseTooltip
            icon="mdi-help-circle"
            location="right"
            text="친구 검색을 통한 공개여부 설정"
            size="sm"
            color="grey-lighten-1"
          />
          <!-- <span>친구 검색을 통한 공개여부 설정</span> -->
          <v-switch
            v-model="publicStatus"
            @change="chkChange"
            class="custom-switch"
            :label="publicStatus === 'true' ? '공개' : '비공개'"
            :true-value="'true'"
            :false-value="'false'"
            :class="{ on: publicStatus == 'true', off: publicStatus == 'false' }"
          />
          채팅 수신 범위
          <BaseTooltip
            icon="mdi-help-circle"
            location="right"
            text="이미 생성된 채팅방을 제외한 채팅의 수신 범위를 전체, 친구만 인지 설정"
            size="sm"
            color="grey-lighten-1"
          />

          <v-switch
            v-model="chatScope"
            @change="chkChange"
            class="custom-switch"
            :true-value="'friend'"
            :false-value="'all'"
            :label="chatScope === 'friend' ? '친구만 허용' : '전체 허용'"
          />
        </v-card-text>

        <v-card-actions>
          <v-btn :disabled="!userStore.isUpdated" @click="saveUser"> 저장 </v-btn>
        </v-card-actions>
      </v-card>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()

const nickname = ref(userStore.userNickname)
const publicStatus = ref(userStore.isPublic)
const chatScope = ref(userStore.userChatReceiveScope)
const userCreatedAt = ref(userStore.userCreatedAt)

const chkChange = () => {
  if (
    publicStatus.value !== userStore.isPublic ||
    chatScope.value !== userStore.userChatReceiveScope
  ) {
    userStore.setIsUpdated(true)
  } else {
    userStore.setIsUpdated(false)
  }
}

const saveUser = () => {
  userStore.setUserData({
    isPublic: publicStatus.value,
    userChatReceiveScope: chatScope.value,
  })
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)

  /* 날짜 */
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')

  /* 시간 */
  let hours = date.getHours()
  const minutes = String(date.getMinutes()).padStart(2, '0')

  const ampm = hours >= 12 ? 'PM' : 'AM'

  hours = hours % 12
  hours = hours === 0 ? 12 : hours
  const strHours = String(hours).padStart(2, '0')

  return `${year}년 ${month}월 ${day}일`
}
</script>

<style>
.custom-switch .v-switch__track {
  background-color: #3e2723 !important;
}

.custom-switch .v-selection-control__input {
  width: 24px !important;
  height: 24px !important;
  background-color: #3e2723 !important;
}

.custom-switch.on .v-selection-control__input {
  background-color: #208de1 !important;
}
.custom-switch.off .v-selection-control__input {
  background-color: #e63e3e !important;
}
</style>
