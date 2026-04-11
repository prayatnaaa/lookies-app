package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.data.remote.dto.ChatRoomDto
import com.prayatna.lookiesapp.data.remote.dto.MessageDto
import com.prayatna.lookiesapp.data.remote.dto.request.chat.CreateMessageRequest
import com.prayatna.lookiesapp.data.remote.dto.response.chat.CreateMessageResponse
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun listenToForumMessages(channelId: String): Flow<List<com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView>>
    suspend fun insertForumsMessage(data: com.prayatna.lookiesapp.domain.model.message.CreateForumMessageInput): DataResult<com.prayatna.lookiesapp.domain.model.message.ForumMessage>
    suspend fun getForums(): DataResult<List<com.prayatna.lookiesapp.domain.model.message.ForumsView>>
    suspend fun getForumChannels(forumId: String): DataResult<List<com.prayatna.lookiesapp.domain.model.message.ForumChannelView>>
}