<template>
  <button :class="['custom-button', type]" :disabled="disabled">클릭</button>
</template>

<script>
export default {
  name: 'BaseButton',
  props: {
    type: {
      type: String,
      default: 'primary',
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
}
</script>

<style scoped>
.custom-button {
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.custom-button.primary {
  background-color: #007bff;
  color: black;
}

.custom-button.secondary {
  background-color: #6c757d;
  color: black;
}

.custom-button.danger {
  background-color: #dc3545;
  color: black;
}

.custom-button:disabled {
  background-color: #dcdcdc;
  cursor: not-allowed;
}
</style>
