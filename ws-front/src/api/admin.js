import api from '@/utils/api'
export async function saveAllChatMessagesApi() {
  return await api.get('/admin/save/all/chatmessages')
}
