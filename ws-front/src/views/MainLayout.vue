<template>
    <div>
        <NavBar/>
        <RouterView/>
    </div>
</template>

<script>
import NavBar from '../components/NavBar.vue';

export default {
  name: 'MainLayout',
  components: {
    NavBar, // nav-bar 등록
  },
};
</script>