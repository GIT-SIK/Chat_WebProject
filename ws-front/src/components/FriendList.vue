<template>
  <div>
    <span>{{ userId }} 님의 친구 목록</span>
    <div class="friend-list">
      <template v-for="friend in friendList" :key="friend.id">
        <div class="friend-card">
          <!-- 유저 기본 이미지 -->
          <div class="friend-card-image">
            <img :src="defaultUserImage" alt="유저 이미지" />
          </div>
          <!-- 유저 정보 -->
          <div class="friend-card-info">
            <div class="friend-card-id">
              <li v-if="friend.senderUserId == userId">
                <span>{{ friend.receiverUserId }}</span>
              </li>
              <li v-else>
                <span>{{ friend.senderUserId }}</span>
              </li>
            </div>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
import { onMounted, ref } from 'vue'
import * as friend from '@/api/friend'
import { useUserStore } from '@/store/user'
import defaultUserImage from '@/assets/default_user.png'

export default {
  setup() {
    const authUser = useUserStore()
    const friendList = ref([])

    const getFriendList = async () => {
      authUser.getUserInfo()
      console.log(authUser.userId)
      const response = await friend.getFriendApi(authUser.userId)
      friendList.value = response.data
    }

    onMounted(() => {
      getFriendList()
    })

    return {
      friendList,
      userId: authUser.userId,
      defaultUserImage,
    }
  },
}
</script>

<style scoped>
/* 친구 목록 스타일 */
.friend-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: space-evenly;
}

/* 카드 스타일 */
.friend-card {
  min-width: 100px;
  max-width: 150px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 유저 이미지 스타일 */
.friend-card-image {
  width: 40px;
  height: 40px;
  margin-bottom: 10px;
}

.friend-card-image img {
  width: 100%;
  height: 100%;
  border-radius: 50%; /* 둥근 이미지 */
}

/* 유저 아이디 스타일 */
.friend-card-info {
  font-size: 12px;
}

.friend-card-id {
  list-style-type: none;
  font-weight: bold;
  color: #333;
}
</style>
