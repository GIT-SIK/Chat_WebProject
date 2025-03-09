<template>
  <template v-if="toggleStatus">
    <v-card class="mx-auto" width="260">
      <v-toolbar class="transparent-bg">
        <v-toolbar-title>Title</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn icon="mdi-close" @click="closeNotification" variant="text"></v-btn>
      </v-toolbar>

      <v-list class="transparent-bg" lines="three">
        <template v-for="(item, index) in items">
          <template v-if="item.prependAvatar !== undefined">
            <v-list-item :key="index">
              <template textv-slot:prepend>
                <v-avatar>
                  <img :src="item.prependAvatar" alt="Avatar" />
                </v-avatar>
              </template>

              <v-list-item-title>{{ item.title }}</v-list-item-title>
              <v-list-item-subtitle>
                <div v-html="item.subtitle"></div>
              </v-list-item-subtitle>
            </v-list-item>
          </template>
          <template v-if="item.type !== undefined">
            <v-divider></v-divider>
          </template>
        </template>
      </v-list>
    </v-card>
  </template>
</template>

<script>
import { storeToRefs } from 'pinia'
import defaultUserImage from '@/assets/default_user.png'
import { useNotificationStore } from '@/store/notification'

export default {
  setup() {
    const notificationStore = useNotificationStore()
    const { toggleStatus } = storeToRefs(notificationStore)
    const items = [
      {
        prependAvatar: defaultUserImage,
        title: 'List Title 1',
        subtitle: `<span class="text-primary">Subtitle 1</span> &mdash; Text`,
      },
      {
        prependAvatar: defaultUserImage,
        title: 'List Title 2',
        subtitle: `<span class="text-primary">Subtitle 2</span> &mdash; Text`,
      },
      {
        prependAvatar: defaultUserImage,
        title: 'List Title 3',
        subtitle: `<span class="text-primary">Subtitle 3</span> &mdash; Text`,
      },
      { type: 'divider', inset: true },
      {
        prependAvatar: 'https://picsum.photos/200/200',
        title: 'List Title 4',
        subtitle: `<span class="text-primary">Subtitle 4</span> &mdash; Text`,
      },
    ]

    const closeNotification = () => {
      notificationStore.toggle()
    }

    return {
      items,
      toggleStatus,
      closeNotification,
    }
  },
}
</script>

<style>
.notification-sub-container {
  background-color: #f5f2f1 !important;
}

.transparent-bg {
  background-color: transparent !important;
}
</style>
