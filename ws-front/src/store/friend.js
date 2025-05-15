import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useFriendStore = defineStore('friend', () => {
  const tabStatus = ref("")


  async function setTabStatus(newTabStatus) {
    console.log("Pinia New TabStatus");
    console.log(newTabStatus);
    tabStatus.value = newTabStatus;
  }

  return {
    tabStatus,
    setTabStatus,
  }
})
