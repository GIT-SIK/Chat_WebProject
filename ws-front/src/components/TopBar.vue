<template>
  <v-toolbar color="transparent">
    <template v-if="!$vuetify.display.mdAndUp">
      <v-btn icon="mdi-menu" @click="toggleNavBar"></v-btn>
    </template>
    <v-spacer></v-spacer>
    <div class="toolbar-user-info">
      <v-avatar size="32">
        <img :src="userImage" alt="user" />
      </v-avatar>
      <div class="ml-4 mr-9 toolbar-username">{{ userName }}</div>
    </div>
  </v-toolbar>
</template>

<script>
import defaultUserImage from '@/assets/default_user.png'
import { useUserStore } from '@/store/user'
import { useNavBarStore } from '@/store/navbar'

export default {
  setup() {
    const userStore = useUserStore()
    const navBarStore = useNavBarStore();

    const toggleNavBar = () =>{
      navBarStore.toggle();
      console.log(navBarStore.toggleStatus);
    }

    return {
      userImage: defaultUserImage,
      userName: userStore.userId,
      toggleNavBar,
    }
  },
}
</script>

<style>
.toolbar-username {
  color: #3e2723 !important;
}

.toolbar-user-info {
  display: flex;
  align-items: center;
}
</style>
