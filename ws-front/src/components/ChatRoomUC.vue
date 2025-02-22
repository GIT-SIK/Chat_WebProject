<template>
  <div class="count-container">
    <div><span>{{ userCount }} 명</span></div>
    <div><span>접속자 수</span></div>
  </div>
</template>

<script>
export default {
  props: {
    userCount: {
      type: Number,
      required: true,
    },
  },
}
</script>

<style scoped>
.count-container {
  max-width: 500px;
  margin-bottom: 10px;
  display: inline-block;
  color: #333;
}

.count-container div {
  float: right;
  height: 30px;
  font-size: 16px;
  margin-left: auto;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);

  /* div내 span태그 정렬을 위해*/
  display: flex;
  justify-content: center;
  align-items: center;
}


.count-container div:nth-child(1) {
  max-width: 45px;
}

.count-container div:nth-child(2) {
  margin-right: 5px;
  width: 70px;
}
</style>
