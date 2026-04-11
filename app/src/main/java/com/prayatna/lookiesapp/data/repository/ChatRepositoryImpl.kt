package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseChatService
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.message.CreateForumMessageInput
import com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView
import com.prayatna.lookiesapp.domain.model.message.ForumMessage
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val supabaseChatService: SupabaseChatService
): ChatRepository {

    override fun listenToForumMessages(channelId: String): DataResult<Flow<List<ForumChannelMessagesView>>> {
        return try {
            val flow = supabaseChatService.listenToForumMessages(channelId)
                .map { list ->
                    list.map { it.toDomain() }
                }
            DataResult.Success(flow)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error connecting to forum messages")
        }
    }

    override suspend fun insertForumsMessage(data: CreateForumMessageInput): DataResult<ForumMessage> {
        return try {
            val requestDto = data.toDto()
            val result = supabaseChatService.insertForumsMessage(requestDto)
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
}