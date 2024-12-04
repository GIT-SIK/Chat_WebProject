<template>
  <button :class="['custom-button', type]" :disabled="disabled"><slot></slot></button>
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
  background-color: lightskyblue;
  color: white;
}

.custom-button.primary:hover {
  background-color: lightblue;
}

.custom-button.secondary {
  background-color: #6c757d;
  color: white;
}

.custom-button.danger {
  background-color: #dc3545;
  color: white;
}

.custom-button.danger:hover {
  background-color: lightcoral;
}

.custom-button:disabled {
  background-color: #dcdcdc;
  cursor: not-allowed;
}

.custom-button:active {
  box-shadow: inset 1.5px 1.5px 10px rgb(0, 0, 0, 0.5);
}
</style>
