package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseChatService
import com.prayatna.lookiesapp.data.remote.dto.ChatRoomDto
import com.prayatna.lookiesapp.data.remote.dto.MessageDto
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val supabaseChatService: SupabaseChatService
): ChatRepository {
    override fun getMessages(targetId: String): DataResult<Flow<List<MessageDto>>> {
        return try {
            val result = supabaseChatService.getMessages(targetId = targetId)
            DataResult.Success(result)
        } catch (e: RestException) {
            DataResult.Error(e.error)
        }
    }

    override fun getChatInbox(): DataResult<Flow<List<ChatRoomDto>>> {
        return try {
            flow {
                val inbox = supabaseChatService.getChatInbox()
                emit(inbox)
            }.let { DataResult.Success(it) }

        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error")
        }
    }
}