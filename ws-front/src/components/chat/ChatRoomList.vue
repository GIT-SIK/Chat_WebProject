<template>
  <div class="mx-auto d-sm-flex flex-column crl-container">
    <v-card class="w-100 mb-5 pl-4 pr-4" variant="text" style="background-color: #ffffff">
      <v-text-field
        prepend-icon="mdi-magnify"
        single-line
        label="Search"
        class="chat-search-input mb-3"
        variant="plain"
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
          v-for="item in items"
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
import { computed } from 'vue'
import defaultUserImage from '@/assets/default_user.png'
export default {
  props: {
    roomListData: {
      type: Array,
      default: () => [],
    },
  },
  setup(props) {
    const items = computed(() => {
      const list = []
      props.roomListData.forEach((room) => {
        list.push({
          prependAvatar: defaultUserImage,
          otherUserId: room.otherUserId,
          roomUpdatedT: room.roomUpdatedT,
        })
      })
      return list
    })
    return {
      items,
      defaultUserImage,
    }
  },
}
</script>

<style>
.crl-container {
  height: calc(100vh - 80px);
}

.chat-search-input .v-input__prepend .v-icon,
.chat-search-input .v-input__control .v-label {
  color: #3e2723 !important;
}
</style>
