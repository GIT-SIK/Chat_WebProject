<template>
    <div>
        <NavBar/>
        <RouterView/>
    </div>
</template>

<script>
import NavBar from '../components/NavBar.vue';

export default {
  name: 'Layout',
  components: {
    NavBar, // nav-bar 등록
  },
};
</script>