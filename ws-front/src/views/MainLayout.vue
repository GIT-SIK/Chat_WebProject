<template>
  <v-layout class="rounded rounded-md">
    <LeftNavBar />
    <v-main class="align-center justify-center vuetify-content" fluid>
      <RouterView />
    </v-main>
  </v-layout>
</template>

<script>
import LeftNavBar from '@/components/LeftNavBar.vue'

export default {
  name: 'MainLayout',
  components: {
    LeftNavBar, // nav-bar 등록
  },
}
</script>
