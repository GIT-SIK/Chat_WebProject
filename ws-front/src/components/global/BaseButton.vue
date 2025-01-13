<template>
  <button 
  :class="['btn', color]" 
  :style="btnStyle" 
  :disabled="disabled"> <slot></slot></button>
</template>

<script>
export default {
  name: 'BaseButton',
  props: {
    disabled: {
      type: Boolean,
      default: false,
    },
    color: {
      type: String,
      default: '', 
    },  
    size: {
      type: String,
      default: 'medium',
    },
  },
  computed: {
    btnStyle() {
      const size = {
        small: { width: '80px', height: '30px' },
        medium: { width: '120px', height: '30px' },
        large: { width: '160px', height: '30px' },
      };
      return size[this.size] || size.medium;
    },
  },
}
</script>

<style scoped>
.btn {
    color: #444444;
    background: #F3F3F3;
    border: 1px #DADADA solid;
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
    background: #F7F7F7;
}

.btn:active {
    box-shadow: inset 1px 1px 1px #c0c0c0;   
}

/* Blue 스타일 */
.btn.btn-blue {
  color: #FFFFFF;
  background: #007BFF;
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
  color: #FFFFFF;
  background: #FF4B4B;
  border: 1px #D32F2F solid;
}

.btn.btn-red:hover {
  background: #D32F2F;
  border: 1px #A00000 solid;
}

.btn.btn-red:active {
  background: #A00000;
}

/* 기본 반전 색 */
.btn.btn-darkgray {
  color: #F3F3F3; /* 반전된 텍스트 색상 */
  background: #444444; /* 반전된 배경색 */
  border: 1px #333333 solid;
}

.btn.btn-darkgray:hover {
  color: #F7F7F7;
  background: #333333;
  border: 1px #222222 solid;
  box-shadow: inset 1px 1px 1px #222222;
}

.btn.btn-darkgray:active {
  background: #222222;
  box-shadow: inset 1px 1px 1px #1a1a1a;
}
</style>