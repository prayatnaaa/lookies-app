package com.prayatna.lookiesapp.data.repository

import coil.network.HttpException
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseChatService
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.message.Conversation
import com.prayatna.lookiesapp.domain.model.message.CreateForumMessageInput
import com.prayatna.lookiesapp.domain.model.message.CreateMessageInput
import com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView
import com.prayatna.lookiesapp.domain.model.message.ForumMember
import com.prayatna.lookiesapp.domain.model.message.ForumMessage
import com.prayatna.lookiesapp.domain.model.message.InitiatedConversation
import com.prayatna.lookiesapp.domain.model.message.Message
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.realtime.PresenceAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val supabaseChatService: SupabaseChatService
): ChatRepository {
    override suspend fun getForumChannelMessages(channelId: String): DataResult<List<ForumChannelMessagesView>> {
        return try {
            val res = supabaseChatService.getForumChannelMessages(channelId)
            DataResult.Success(res.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: HttpException) {
            DataResult.Error(e.message ?: "Please check your internet connection!")
        }
    }

    override fun listenToMessages(conversationId: String): Flow<List<Message>> {
        return supabaseChatService.listenToMessages(conversationId)
            .map { dtoList ->
                dtoList.map { it.toDomain() }
            }
    }

    override suspend fun sendMessage(data: CreateMessageInput): DataResult<Message> {
        return try {
            val result = supabaseChatService.sendMessage(data.toDto())
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error sending message")
        }
    }

    override suspend fun getConversations(): DataResult<List<Conversation>> {
        return try {
            val result = supabaseChatService.getConversations()
            DataResult.Success(result.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error fetching conversations")
        }
    }

    override suspend fun getMerchantConversations(merchantId: String): DataResult<List<Conversation>> {
        return try {
            val result = supabaseChatService.getMerchantConversations(merchantId)
            DataResult.Success(result.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error fetching merchant conversations")
        }
    }

    override suspend fun getConversationByMerchantId(merchantId: String): DataResult<Conversation?> {
        return try {
            val result = supabaseChatService.getConversationByMerchantId(merchantId)
            DataResult.Success(result?.toDomain())
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error checking existing conversation")
        }
    }

    override suspend fun getOrCreateConversation(merchantId: String): DataResult<InitiatedConversation> {
        return try {
            val id = supabaseChatService.getOrCreateConversation(merchantId)
            DataResult.Success(InitiatedConversation(id))
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error initiating conversation")
        }
    }

    override suspend fun markMessagesAsRead(conversationId: String, isMerchant: Boolean): DataResult<Unit> {
        return try {
            supabaseChatService.markMessagesAsRead(conversationId, isMerchant)
            DataResult.Success(Unit)
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error marking messages as read")
        }
    }

    override fun listenToForumMessages(
        channelId: String
    ): Flow<List<ForumChannelMessagesView>> {

        return supabaseChatService.listenToForumMessages(channelId)
            .map { dtoList ->
                dtoList.map { it.toDomain() }
            }
    }

    override suspend fun insertForumsMessage(data: CreateForumMessageInput): DataResult<ForumMessage> {
        return try {
            val requestDto = com.prayatna.lookiesapp.data.remote.dto.request.chat.CreateForumMessageRequest(
                channelId = data.channelId,
                content = data.content,
                senderId = data.senderId
            )
            val result = supabaseChatService.insertForumsMessage(requestDto)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error sending forum message")
        }
    }

    override suspend fun getForums(title: String?): DataResult<List<com.prayatna.lookiesapp.domain.model.message.ForumsView>> {
        return try {
            val result = supabaseChatService.getForums(title)
            DataResult.Success(result.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error fetching forums")
        }
    }

    override suspend fun getForumChannels(forumId: String): DataResult<List<com.prayatna.lookiesapp.domain.model.message.ForumChannelView>> {
        return try {
            val result = supabaseChatService.getForumChannels(forumId)
            DataResult.Success(result.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error fetching forum channels")
        }
    }

    override suspend fun getForumMembers(forumId: String): DataResult<List<ForumMember>> {
        return try {
            val result = supabaseChatService.getForumMembers(forumId)
            DataResult.Success(result.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error fetching forum members")
        }
    }

    override fun listenToForumPresence(forumId: String): Flow<PresenceAction> {
        return supabaseChatService.listenToForumPresence(forumId)
    }

    override suspend fun createForumChannel(
        forumId: String,
        name: String,
        isReadOnlyForMember: Boolean
    ): DataResult<com.prayatna.lookiesapp.domain.model.message.CreateForumChannelResult> {
        return try {
            val result = supabaseChatService.createForumChannel(forumId, name, isReadOnlyForMember)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error creating forum channel")
        }
    }

    override suspend fun updateForumMessage(
        messageId: String,
        content: String
    ): DataResult<ForumMessage> {
        return try {
            val result = supabaseChatService.updateForumMessage(messageId, content)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error updating forum message")
        }
    }

    override suspend fun deleteForumMessage(messageId: String): DataResult<Unit> {
        return try {
            supabaseChatService.deleteForumMessage(messageId)
            DataResult.Success(Unit)
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error deleting forum message")
        }
    }

    override suspend fun pinForumMessage(
        messageId: String,
        isPinned: Boolean
    ): DataResult<ForumMessage> {
        return try {
            val result = supabaseChatService.pinForumMessage(messageId, isPinned)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error pinning forum message")
        }
    }
}
