<template>
    <input
      :type="type"
      :style="inputStyle"
      :placeholder="placeholder"
      @input="$emit('update:modelValue', $event.target.value)"
      v-bind="$attrs"
      class="base-input"
    />
  </template>
  
  <script>
  export default {
    name: "BaseInput",
    props: {
      type: {
        type: String,
        default: "text",
      },
      placeholder: {
        type: String,
        default: "",
      },
      borderColor : {
        type: String,
        default: ""
      }
    },
    computed: {
      inputStyle() {
      const borderColor = {
        red: { border: '2px solid red' },
        green: {border: '2px solid green' },
        default : {border: '1px solid #aaaaaa'}
      };
      return borderColor[this.borderColor] || borderColor.default
    },
  },
  };
  </script>
  
  <style scoped>
  .base-input {
    width: 100%;
    padding: 5px;
    border: 1px solid #aaaaaa;
    border-radius: 5px;
    background-color: #f2f2f2;
    color: #333333;
    font-size: 13px;
    outline: none;
  }
  
  .base-input:focus {
    border-color: #666666;
    background-color: #e6e6e6;
  }
  </style>