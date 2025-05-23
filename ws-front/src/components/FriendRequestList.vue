<template>
    <div>
    <div v-if="requestList.length === 0">
        <p v-if="type === 'received'">받은 친구 요청이 없습니다.</p>
        <p v-else>보낸 친구 요청이 없습니다.</p>
    </div>

    <v-row v-else>
        <v-col
        v-for="req in requestList"
        :key="req.id"
        cols="12"
        sm="4"
        md="3"
        lg="3"
        >
            
            <v-card
                class="pa-3 d-flex flex-column align-center" flat
            >
            <v-avatar size="56" class="mb-3">
                <v-img :src="defaultUserImage" alt="유저 이미지" />
            </v-avatar>
        
                <div class="text-subtitle-2 font-weight-bold">
                <span>
                    {{
                    type === 'received' ? req.senderUserId : req.receiverUserId
                    }}
                </span>
                </div>

                <template v-if="type==='received'">
                <v-card-actions>
                    <v-btn @click="friendRequestData(req.senderUserId,'ACCEPTED')">
                    수락
                    </v-btn>
                    <v-btn @click="friendRequestData(req.senderUserId,'REJECTED')">
                    거절
                    </v-btn>
                </v-card-actions>
                </template>

            </v-card>
        </v-col>
    </v-row>
    </div>
  </template>
<script setup>
    import { ref, onMounted } from 'vue'
    import * as friend from '@/api/friend'
    import defaultUserImage from '@/assets/default_user.png'
    import { useUserStore } from '@/store/user'
    import { useFriendStore } from '@/store/friend'

    const authUser = useUserStore()
    const friendStore = useFriendStore()
    const requestList = ref([])
    
    const props = defineProps({
        type : {
            type : String,
            required : true
        }
    })

    /* 친구 요청 수락에 따른 목록 데이터 갱신 로직 */
    const friendRequestData = async(senderUserId, status) => {
        await friend.friendRequestApi(senderUserId, status);
        requestList.value = requestList.value.filter(req => req.senderUserId !== senderUserId)
        friendStore.setIsUpdated(true); 
    }


    const fetchRequestData = async() => {
        const response = await friend.getFriendApi(authUser.userId)
        
        if(props.type === 'sent') {
            requestList.value = response.data.filter(req =>
             req.friendStatus === 'PENDING' && req.senderUserId === authUser.userId
            )
        }
        if(props.type === 'received') {
            requestList.value = response.data.filter(req =>
                req.friendStatus === 'PENDING' && req.receiverUserId === authUser.userId
            )
        }

    }

    onMounted(fetchRequestData)

</script>