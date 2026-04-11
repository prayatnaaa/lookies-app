package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.data.remote.dto.ChatRoomDto
import com.prayatna.lookiesapp.data.remote.dto.MessageDto
import com.prayatna.lookiesapp.data.remote.dto.request.chat.CreateMessageRequest
import com.prayatna.lookiesapp.data.remote.dto.response.chat.CreateMessageResponse
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessages(targetId: String): DataResult<Flow<List<MessageDto>>>
    fun getChatInbox(): DataResult<Flow<List<ChatRoomDto>>>
    suspend fun createMessage(data: CreateMessageRequest): DataResult<CreateMessageResponse>
    
    fun listenToForumMessages(channelId: String): DataResult<Flow<List<com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView>>>
    suspend fun insertForumsMessage(data: com.prayatna.lookiesapp.domain.model.message.CreateForumMessageInput): DataResult<com.prayatna.lookiesapp.domain.model.message.ForumMessage>
}