package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseChatService
import com.prayatna.lookiesapp.data.remote.dto.ForumChannelMessagesViewDto
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
}