<template>
    <v-card
      class="mx-auto chat-room-list-card"
      min-width="250"
      max-width="350"
    >
      <v-toolbar color="brown-lighten-2" density="compact">
        <!-- <v-btn icon="mdi-menu" variant="text"></v-btn> -->
  
        <v-toolbar-title>Messages</v-toolbar-title>
  
        <v-spacer></v-spacer>
  
        <v-btn icon="mdi-magnify" variant="text"></v-btn>
      </v-toolbar>
  
      <v-list lines="three" item-props>
        <v-list-item
          v-for="(item, index) in items"
          :key="index"
          :prepend-avatar="item.prependAvatar"
          @click="$emit('other-user-id', item.title)">
            <v-list-item-title>{{ item.title }}</v-list-item-title>
            <v-list-item-subtitle>{{ item.subtitle }}</v-list-item-subtitle>
        </v-list-item>
      </v-list>
    </v-card>
  </template>

  <script>
    import { computed } from 'vue';
    import defaultUserImage from '@/assets/default_user.png';

    export default {
        props : {
            roomListData : {
                type : Array,
                default: () => [],
            }
        },
        setup(props) {
            const items = computed(() => {
                const list = [];
                props.roomListData.forEach((room) => {
                    list.push({
                    prependAvatar : defaultUserImage, title : room.otherUserId, subtitle : room.roomUpdatedT
                    })
                });    
                return list;
            }) 
            return {
                items,
                defaultUserImage
            }
        }
    }
    </script>

    <style>
    </style>