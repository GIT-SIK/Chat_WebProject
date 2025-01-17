<template>
  <div> 
    <base-input></base-input>
    <base-button class="btn-darkgray" size="large">버튼</base-button>
  </div>

</template>

<script>
export default {
  name: 'TestPage',
  mounted() {
    console.log(this.$api);

  }
}
</script>

<style scoped>
</style>