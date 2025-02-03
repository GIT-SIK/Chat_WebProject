<template>
  <div>
    <span>친구 찾기</span>
    <div class="search-sub-container">
      <BaseInput type="text" v-model="searchFriendData"> </BaseInput>
      <BaseButton @click="searchFriend()"> 찾기 </BaseButton>
    </div>
    <div class="friend-search-list">
      <template v-if="hasNoFriends"> <span> 검색된 친구가 없습니다. </span> </template>
      <template v-else>
        <template v-for="searchfriendItem in searchFriendList" :key="searchfriendItem.id">
          <div class="friend-search-card">
            <!-- 유저 기본 이미지 -->
            <div class="friend-search-card-image">
              <img :src="defaultUserImage" alt="유저 이미지" />
            </div>
            <!-- 유저 정보 -->
            <div class="friend-search-card-info">
              <div class="friend-search-card-id">
                <span>{{ searchfriendItem.userId }}</span>
              </div>
            </div>
          </div>
        </template>
      </template>
    </div>
  </div>
</template>

<script>
import { ref, inject } from 'vue'
import * as friend from '@/api/friend'
import defaultUserImage from '@/assets/default_user.png'

export default {
  setup() {
    const showToast = inject('showToast')
    const searchFriendList = ref([])
    const searchFriendData = ref()
    const hasNoFriends = ref(false)
    const searchFriend = async () => {
      try {
        const response = await friend.getSearchFriendApi(searchFriendData.value)
        showToast(response.data.length + `명의 친구를 찾았습니다.`)
        searchFriendList.value = response.data
        hasNoFriends.value = false
      } catch (e) {
        if (e.status === 500) {
          hasNoFriends.value = true
        }
      }
      searchFriendData.value = ''
    }

    return {
      hasNoFriends,
      searchFriend,
      showToast,
      searchFriendList,
      searchFriendData,
      defaultUserImage,
    }
  },
}
</script>

<style scoped>
.search-sub-container {
  max-width: 30%;
  display: flex;
}

/* 친구 목록 스타일 */
.friend-search-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: space-evenly;
}

/* 카드 스타일 */
.friend-search-card {
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
.friend-search-card-image {
  width: 40px;
  height: 40px;
  margin-bottom: 10px;
}

.friend-search-card-image img {
  width: 100%;
  height: 100%;
  border-radius: 50%; /* 둥근 이미지 */
}

/* 유저 아이디 스타일 */
.friend-search-card-info {
  font-size: 12px;
}

.friend-search-card-id {
  list-style-type: none;
  font-weight: bold;
  color: #333;
}
</style>
