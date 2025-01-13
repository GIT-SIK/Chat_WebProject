<template>
    <input
      :type="type"
      :placeholder="placeholder"
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
    },
  };
  </script>
  
  <style scoped>
  .base-input {
    width: 100%;
    padding: 10px;
    border: 1px solid #aaaaaa;
    border-radius: 5px;
    background-color: #f2f2f2;
    color: #333333;
    font-size: 16px;
    outline: none;
  }
  
  .base-input:focus {
    border-color: #666666;
    background-color: #e6e6e6;
  }
  </style>