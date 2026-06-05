package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.message.Conversation
import com.prayatna.lookiesapp.domain.model.message.CreateForumChannelResult
import com.prayatna.lookiesapp.domain.model.message.CreateForumMessageInput
import com.prayatna.lookiesapp.domain.model.message.CreateMessageInput
import com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView
import com.prayatna.lookiesapp.domain.model.message.ForumChannelView
import com.prayatna.lookiesapp.domain.model.message.ForumMember
import com.prayatna.lookiesapp.domain.model.message.ForumMessage
import com.prayatna.lookiesapp.domain.model.message.ForumsView
import com.prayatna.lookiesapp.domain.model.message.InitiatedConversation
import com.prayatna.lookiesapp.domain.model.message.Message
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.realtime.PresenceAction
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun listenToMessages(conversationId: String): Flow<List<Message>>
    suspend fun sendMessage(data: CreateMessageInput): DataResult<Message>
    suspend fun getConversations(): DataResult<List<Conversation>>
    suspend fun getMerchantConversations(merchantId: String): DataResult<List<Conversation>>
    suspend fun getOrCreateConversation(merchantId: String): DataResult<InitiatedConversation>

    fun listenToForumMessages(channelId: String): Flow<List<ForumChannelMessagesView>>
    suspend fun insertForumsMessage(data: CreateForumMessageInput): DataResult<ForumMessage>
    suspend fun getForums(): DataResult<List<ForumsView>>
    suspend fun getForumChannels(forumId: String): DataResult<List<ForumChannelView>>
    suspend fun getForumMembers(forumId: String): DataResult<List<ForumMember>>
    fun listenToForumPresence(forumId: String): Flow<PresenceAction>
    suspend fun createForumChannel(forumId: String, name: String, isReadOnlyForMember: Boolean = false): DataResult<CreateForumChannelResult>
    suspend fun updateForumMessage(messageId: String, content: String): DataResult<ForumMessage>
    suspend fun deleteForumMessage(messageId: String): DataResult<Unit>
    suspend fun pinForumMessage(messageId: String, isPinned: Boolean): DataResult<ForumMessage>
}
