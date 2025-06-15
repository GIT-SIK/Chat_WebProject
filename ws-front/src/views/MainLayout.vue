<template>
  <v-app>
    <v-row class="h-screen pt-5" style="background-color: #efebe9">
      <template v-if="$vuetify.display.mdAndUp">
        <v-col cols="1" md="1" offset-md="1" class="layout-left-container">
          <LeftNavBar />
        </v-col>
      </template>
      <template v-else>
        <v-navigation-drawer v-model="toggleStatus" width="75" vertical="mini">
          <div class="mt-3">
            <LeftNavBar />
          </div>
        </v-navigation-drawer>
      </template>
      <v-col cols="10" offset="1" offset-md="0" md="9">
        <v-row>
          <TopBar />
        </v-row>
        <v-row class="main-container">
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
import { useNotificationStore } from '@/store/notification'
import { useDisplay } from 'vuetify'
import { storeToRefs } from 'pinia'
import { watch } from 'vue'

export default {
  components: {
    TopBar,
    LeftNavBar,
  },
  setup() {
    const display = useDisplay()
    const navBarStore = useNavBarStore()
    const notificationStore = useNotificationStore()
    const { toggleStatus } = storeToRefs(navBarStore)

    /* navigation-drawer 닫힐 경우 알람창 닫기 */
    watch(toggleStatus, (newStatus) => {
      if (!newStatus) {
        notificationStore.setToggleStatus(false)
      }
    })

    /* display md사이즈 기준 변경시 알람창 닫기 */
    watch(display.mdAndUp, () => {
      notificationStore.setToggleStatus(false)
    })

    return {
      toggleStatus,
      navBarStore,
    }
  },
}
</script>

<style>
.layout-left-container {
  position: relative;
}

.main-container {
  height: calc(100vh - 64px);
}
</style>
