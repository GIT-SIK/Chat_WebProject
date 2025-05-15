<template>
  <v-card variant="text" color="brown-darken-2" class="w-100">
    <v-tabs
      v-model="tab"
      bg-color="transparent"
      color="brown-darken-2"
      align-tabs="center"
    >
      <v-tab
        v-for="(item,index) in items"
        :key="index"
        :text="item.tabName"
        :value="item.tabName"
        >{{item.tabName}}
      </v-tab>
    </v-tabs>

    <v-tabs-window v-model="tab">
      <v-tabs-window-item
        v-for="(item,index) in items"
        :key="index"
        :value="item.tabName"
      >
        <v-card flat color="transparent" class="mt-4">
          <component :is="item.component" v-bind="item.props || {}"/>
        </v-card>
      </v-tabs-window-item>
    </v-tabs-window>
  </v-card>
</template>




<script>
import { ref, watch } from 'vue'
import FriendList from '../components/FriendList.vue'
import FriendSearch from '../components/FriendSearch.vue'
import FriendRequestList from '../components/FriendRequestList.vue'
import { useFriendStore } from '@/store/friend' 
import { storeToRefs } from 'pinia'

export default {
  components: {
    FriendList,
    FriendSearch,
    FriendRequestList
  },
  setup () {
    const friendStore = useFriendStore();
    const { tabStatus } = storeToRefs(friendStore);


    const tab = ref(tabStatus.value)

    watch(tab, (newTabValue) => {
      friendStore.setTabStatus(newTabValue);  // tab 값 변경 시 tabStatus 갱신
    });

    const items = [
    {tabName : '친구 목록', component : FriendList},
    {tabName : '친구 찾기', component : FriendSearch},
    {tabName : '받은 친구 요청', component : FriendRequestList, props: { type: 'received' }},
    {tabName : '내가 보낸 친구 요청', component : FriendRequestList, props: { type: 'sent' }}
    ];

    return {
      tab,
      items,      
    }
  }
}
</script>
