<template>
  <v-card variant="text" color="brown-darken-2" class="w-100">
    <v-tabs v-model="tab" bg-color="transparent" color="brown-darken-2" align-tabs="center">
      <v-tab v-for="(item, index) in items" :key="index" :text="item.tabName" :value="item.tabName"
        >{{ item.tabName }}
      </v-tab>
    </v-tabs>

    <v-tabs-window v-model="tab">
      <v-tabs-window-item v-for="(item, index) in items" :key="index" :value="item.tabName">
        <v-card flat color="transparent" class="mt-4">
          <component :is="item.component" v-bind="item.props || {}" />
        </v-card>
      </v-tabs-window-item>
    </v-tabs-window>
  </v-card>
</template>

<script>
import { ref, watch, watchEffect } from 'vue'
import FriendList from '../components/friend/FriendList.vue'
import FriendSearch from '../components/friend/FriendSearch.vue'
import FriendRequestList from '../components/friend/FriendRequestList.vue'
import { useFriendStore } from '@/store/friend'
import { useUserStore } from '@/store/user'
import { storeToRefs } from 'pinia'

export default {
  components: {
    FriendList,
    FriendSearch,
    FriendRequestList,
  },
  setup() {
    const friendStore = useFriendStore()
    const authUser = useUserStore()
    const { tabStatus } = storeToRefs(friendStore)

    const tab = ref(tabStatus.value)

    watchEffect(() => {
      tab.value = tabStatus.value
    })

    watch(tab, async (newTabValue) => {
      friendStore.setTabStatus(newTabValue)

      /* 친구 목록 데이터 갱신 로직 */
      if (newTabValue === '친구 목록' && friendStore.isUpdated) {
        await friendStore.fetchFriendList()
        friendStore.setIsUpdated(false)
      }
    })

    const items = [
      { tabName: '친구 목록', component: FriendList },
      { tabName: '친구 찾기', component: FriendSearch },
      { tabName: '받은 친구 요청', component: FriendRequestList, props: { type: 'received' } },
      { tabName: '내가 보낸 친구 요청', component: FriendRequestList, props: { type: 'sent' } },
    ]

    return {
      tab,
      items,
    }
  },
}
</script>
