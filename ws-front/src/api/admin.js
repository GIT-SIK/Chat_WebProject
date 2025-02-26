import api from '@/utils/api'
export async function saveAllChatMessagesApi() {
    return await api.get('/api/admin/save/all/chatmessages');
  }