<template>
  <v-row class="layout-container h-screen" no-gutters>
    <v-col cols="1" class="d-none d-md-block"></v-col>
    <v-col cols="auto">
      <LeftNavBar />
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
import TopBar from '@/components/TopBar.vue'
import LeftNavBar from '@/components/LeftNavbar.vue'
export default {
  components: {
    TopBar,
    LeftNavBar,
  },
  setup() {},
}
</script>

<style>
.layout-container {
  background-color: #efebe9;
}
</style>
