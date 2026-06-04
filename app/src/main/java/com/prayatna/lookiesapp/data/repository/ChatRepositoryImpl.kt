package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseChatService
import com.prayatna.lookiesapp.data.remote.dto.ForumChannelMessagesViewDto
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.message.CreateForumMessageInput
import com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView
import com.prayatna.lookiesapp.domain.model.message.ForumMember
import com.prayatna.lookiesapp.domain.model.message.ForumMessage
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
            val requestDto = data.toDto()
            val result = supabaseChatService.insertForumsMessage(requestDto)
            Log.d("INSERT_MESSAGE", result.toString())
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error sending forum message")
        }
    }

    override suspend fun getForums(): DataResult<List<com.prayatna.lookiesapp.domain.model.message.ForumsView>> {
        return try {
            val result = supabaseChatService.getForums()
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
