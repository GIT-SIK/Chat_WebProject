<template>
  <div>
    템플
  <IndexTest> </IndexTest>
  </div>
</template>

<script>
import IndexTest from './components/indexTest.vue'
export default {
  name: 'app',
  components: {
    IndexTest
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
