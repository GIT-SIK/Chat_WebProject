<template>
  <header></header>
  <ToastMessage ref="toast" />
  <router-view :show-toast="showToast" />
  <RouterView />
</template>

<script>
/* 알람 */
import ToastMessage from './components/global/ToastMessage.vue'
import { ref, provide } from 'vue'

export default {
  name: 'App',
  components: {
    ToastMessage,
  },
  setup() {
    const toast = ref(null)

    const showToast = (message) => {
      toast.value?.addToast(message)
    }
    provide('showToast', showToast)

    return {
      showToast,
      toast,
    }
  },
}
</script>

<style scoped></style>
