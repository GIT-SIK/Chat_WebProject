<template>
  <div class="toast-container">
    <TransitionGroup name="toast">
      <div v-for="(toast, index) in toasts" :key="index" class="toast">
        {{ toast.message }}
      </div>
    </TransitionGroup>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const toasts = ref([])

const addToast = (message) => {
  toasts.value.push({ message })

  setTimeout(() => {
    toasts.value.shift()
  }, 3000)
}

// 다른 컴포넌트에서 addToast()를 호출할 수 있도록 노출
defineExpose({ addToast })
</script>

<style scoped>
.toast-container {
  z-index: 50;
  position: fixed;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.toast {
  background: #333;
  color: #fff;
  padding: 10px 20px;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  opacity: 1;
  margin-bottom: 0 !important;
  transition: opacity 0.5s ease-in-out;
  display: block !important;
}

/* 애니메이션 효과 */
.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

.toast-enter-to,
.toast-leave-from {
  opacity: 1;
  transform: translateY(0);
}

.toast-enter-active,
.toast-leave-active {
  transition: all 0.5s ease-in-out;
}
</style>
