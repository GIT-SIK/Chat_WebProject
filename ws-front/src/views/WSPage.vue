<template>
  <div class="ws-container">
    <div class="ws-title">WebSocket</div>
    <WbSkt />
  </div>
</template>

<script>
import WbSkt from '../components/WbSkt.vue'

export default {
  name: 'WSPage',
  components: {
    WbSkt, // nav-bar 등록
  },
}
</script>

<style>
.ws-container {
  width: 100%;
  height: 100vh;
  background-color: #f4f4f9;
}

.ws-title {
  padding-top: 30px;
  font-size: large;
  text-align: center;
  background-color: #f4f4f9;
}
</style>
