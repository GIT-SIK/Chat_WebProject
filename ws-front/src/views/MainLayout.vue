<template>
    <v-app>
  <v-row class="layout-container h-screen" no-gutters>
    <template v-if="$vuetify.display.mdAndUp"> 
    <v-col cols="1" md="1" offset-md="1" class="layout-left-container">
      <LeftNavBar />
    </v-col>
    </template> 
    <template v-else>
      <v-navigation-drawer
      v-model="toggleStatus"
      :temporary="!$vuetify.display.mdAndUp"
      vartical="mini"
      width = "80"
      >
      <LeftNavBar />
    </v-navigation-drawer>
    </template>
    <v-col cols="12" md="9">
      <v-row no-gutters>
        <TopBar />
      </v-row>
      <v-row no-gutters>
        <RouterView />
      </v-row>
    </v-col>
  </v-row>
</v-app>
</template>

<script>
import TopBar from '../components/TopBar.vue'
import LeftNavBar from '../components/LeftNavbar.vue'
import { useNavBarStore } from '@/store/navbar'
import { storeToRefs } from 'pinia'

export default {
  components: {
    TopBar,
    LeftNavBar,
  },
  setup() {
    const navBarStore = useNavBarStore();
    const {toggleStatus} = storeToRefs(navBarStore)

    return {
      toggleStatus,
      navBarStore
    }
  }, 
}
</script>

<style>
.layout-container {
  background-color: #efebe9;
}

.layout-left-container {
  position: relative;
  
}
</style>
