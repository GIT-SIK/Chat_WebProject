<template>
  <div class="mx-auto d-sm-flex flex-column h-100">
    <v-card class="w-100 mb-5 pl-4 pr-4" variant="text" style="background-color: #ffffff">
      <v-text-field
        prepend-icon="mdi-magnify"
        single-line
        label="Search"
        v-model="search"
        class="chat-search-input mb-3"
        variant="plain"
        clearable
        hide-details
      ></v-text-field>
    </v-card>
    <v-card
      class="w-100 flex-grow-1 overflow-y-auto"
      variant="text"
      style="background-color: #ffffff"
    >
      <v-list lines="three">
        <v-list-subheader> 대화 목록 </v-list-subheader>
        <v-list-item
          v-for="item in searchItems"
          :key="item.value"
          :subtitle="item.roomUpdatedT"
          :title="item.otherUserId"
          @click="$emit('other-user-id', item.otherUserId)"
        ></v-list-item>
      </v-list>
    </v-card>
  </div>
</template>

<script>
import { computed, ref, watch } from 'vue'
import defaultUserImage from '@/assets/default_user.png'
export default {
  props: {
    roomListData: {
      type: Array,
      default: () => [],
    },
  },
  setup(props) {
    const search = ref('')
    const items = computed(() =>
    props.roomListData.map(room => ({
      prependAvatar: defaultUserImage,
      otherUserId: room.otherUserId,
      roomUpdatedT: room.roomUpdatedT,
    }))
  )
  

    const searchItems = ref([...items.value]);

    /*  
    items → SearchItems
    비동기 방식 데이터 처리를 위한 watch 
    */

    watch(items, () => {
      const keyword = (search.value || '').toLowerCase()
      searchItems.value = items.value.filter(item =>
        item.otherUserId.toLowerCase().includes(keyword)
      )
    }, { immediate: true })

    /*
    검색 (실시간 반영)
    */
    watch(search, (newVal) => {
      const keyword = (newVal || '').toLowerCase()
      searchItems.value = items.value.filter(item =>
        item.otherUserId.toLowerCase().includes(keyword)
      )
    })
  
  
    return {
      search,
      searchItems,
      defaultUserImage,
    }
  },
}
</script>

<style>
.chat-search-input .v-input__prepend .v-icon,
.chat-search-input .v-input__control .v-label {
  color: #3e2723 !important;
}
</style>
