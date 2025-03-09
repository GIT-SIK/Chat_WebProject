<template>
  <v-row class="layout-container h-screen" no-gutters>
    <v-col ref="navCol" cols="1" class="d-none d-md-block"></v-col>
    <v-col cols="auto">
      <LeftNavBar />
      <div class="notification-container" :style="notificationStyle">
        <NotificationList />
      </div>
    </v-col>
    <v-col cols="11" md="9">
      <v-row no-gutters>
        <TopBar />
      </v-row>
      <v-row no-gutters>
        <RouterView />
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
import TopBar from '../components/TopBar.vue'
import NotificationList from '../components/NotificationList.vue'
import LeftNavBar from '../components/LeftNavbar.vue'
export default {
  components: {
    TopBar,
    LeftNavBar,
    NotificationList,
  },
  setup() {},
}
</script>

<style>
.layout-container {
  background-color: #efebe9;
}

.notification-container {
  position: absolute;
  top: 30px;
  left: 50px;
  z-index: 10;
  margin: 0 auto;
  padding: 20px;
}
</style>
