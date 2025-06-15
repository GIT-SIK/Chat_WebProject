<template>
  <v-tooltip :location="location">
    <template #activator="{ props }">
      <span v-bind="props" class="cursor-pointer">
        <v-icon :icon="icon" :color="color" :size="sizeList[size]" />
      </span>
    </template>
    <span>{{ text }}</span>
  </v-tooltip>
</template>

<script setup>
defineOptions({ name: 'BaseTooltip' })

const props = defineProps({
  icon: {
    type: String,
    required: true,
  },

  location: {
    type: String,
    default: 'top',
  },

  text: {
    type: String,
    required: true,
  },

  size: {
    type: String,
    default: 'md',
  },

  color: {
    type: String,
    default: 'grey-lighten-1',
  },
})

const sizeList = {
  sm: 16,
  md: 24,
  lg: 32,
}
</script>
