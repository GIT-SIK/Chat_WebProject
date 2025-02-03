<template>
  <FriendSearch />
  <hr />
  <FriendList />
</template>

<script>
import FriendList from '@/components/FriendList.vue'
import FriendSearch from '@/components/FriendSearch.vue'

export default {
  name: 'FriendPage',
  components: {
    FriendList,
    FriendSearch,
  },
}
</script>
