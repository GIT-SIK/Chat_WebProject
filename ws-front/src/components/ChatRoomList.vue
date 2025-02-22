<template>
    <v-card
      class="mx-auto chat-room-list-card"
      min-width="250"
      max-width="350"
    >
      <v-toolbar color="brown-lighten-2" density="compact">
        <!-- <v-btn icon="mdi-menu" variant="text"></v-btn> -->
  
        <v-toolbar-title>Friend List</v-toolbar-title>
  
        <v-spacer></v-spacer>
  
        <v-btn icon="mdi-magnify" variant="text"></v-btn>
      </v-toolbar>
  
      <v-list
        :items="items"
        lines="three"
        item-props
      >
        <template v-slot:subtitle="{ subtitle }">
          <div v-html="subtitle"></div>
        </template>
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
                props.roomListData.forEach((room, i) => {
                    list.push({
                    prependAvatar : defaultUserImage, title : room.otherUserId, subtitle : room.roomUpdatedT
                    })
                    if(props.roomListData.length - 1 > i ) {
                      list.push({ type: 'divider', inset: true })
                    }
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