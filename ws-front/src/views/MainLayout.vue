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
        width = "75"
        vertical="mini"
        >
          <LeftNavBar/>
        </v-navigation-drawer>
      </template>
      <v-col cols="12" sm="10" offset-sm="1" offset-md="0" md="9">
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
import { useNotificationStore } from '@/store/notification' 
import { useDisplay } from 'vuetify';
import { storeToRefs } from 'pinia'
import { watch } from 'vue'

export default {
  components: {
    TopBar,
    LeftNavBar,
  },
  setup() {
    const display = useDisplay();
    const navBarStore = useNavBarStore();
    const notificationStore = useNotificationStore();
    const {toggleStatus} = storeToRefs(navBarStore)

   /* navigation-drawer 닫힐 경우 알람창 닫기 */
    watch(toggleStatus, (newStatus) => {
      if (!newStatus) {
        notificationStore.setToggleStatus(false);
      }
    });

  /* display md사이즈 기준 변경시 알람창 닫기 */
    watch(display.mdAndUp, () => {
      notificationStore.setToggleStatus(false); 
    });

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
