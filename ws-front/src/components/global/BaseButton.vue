<template>
  <button :class="['btn', color]" :style="btnStyle" :disabled="disabled">
    <slot></slot>
  </button>
</template>

<script setup>
import { computed } from 'vue'

defineOptions({ name: 'BaseButton' })

const props = defineProps({
  disabled: { type: Boolean, default: false },
  color: { type: String, default: '' },
  size: { type: String, default: 'medium' },
})

const btnStyle = computed(() => {
  const size = {
    small: { width: '80px', height: '30px' },
    medium: { width: '120px', height: '30px' },
    large: { width: '160px', height: '30px' },
  }
  return size[props.size] || size.medium
})
</script>

<style scoped>
.btn {
  color: #444444;
  background: #f3f3f3;
  border: 1px #dadada solid;
  padding: 5px 10px;
  border-radius: 2px;
  font-weight: bold;
  font-size: 9pt;
  outline: none;
}

.btn:hover {
  border: 1px #c6c6c6 solid;
  box-shadow: inset 1px 1px 1px #cdcdcd;
  color: #333333;
  background: #f7f7f7;
}

.btn:active {
  box-shadow: inset 1px 1px 1px #c0c0c0;
}

/* Blue 스타일 */
.btn.btn-blue {
  color: #ffffff;
  background: #007bff;
  border: 1px #0056b3 solid;
}

.btn.btn-blue:hover {
  background: #0056b3;
  border: 1px #003f7f solid;
}

.btn.btn-blue:active {
  background: #004080;
}

/* Red 스타일 */
.btn.btn-red {
  color: #ffffff;
  background: #ff4b4b;
  border: 1px #d32f2f solid;
}

.btn.btn-red:hover {
  background: #d32f2f;
  border: 1px #a00000 solid;
}

.btn.btn-red:active {
  background: #a00000;
}

/* 기본 반전 색 */
.btn.btn-darkgray {
  color: #f3f3f3;
  background: #444444;
  border: 1px #333333 solid;
}

.btn.btn-darkgray:hover {
  color: #f7f7f7;
  background: #333333;
  border: 1px #222222 solid;
  box-shadow: inset 1px 1px 1px #222222;
}

.btn.btn-darkgray:active {
  background: #222222;
  box-shadow: inset 1px 1px 1px #1a1a1a;
}
</style>
