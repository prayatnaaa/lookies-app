package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.ForumChannelMessagesViewDto
import com.prayatna.lookiesapp.data.remote.dto.ForumChannelViewDto
import com.prayatna.lookiesapp.data.remote.dto.ForumMessageDto
import com.prayatna.lookiesapp.data.remote.dto.ForumsViewDto
import com.prayatna.lookiesapp.data.remote.dto.request.chat.CreateForumMessageRequest
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class SupabaseChatService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val realtime: Realtime
) {

    suspend fun insertForumsMessage(request: CreateForumMessageRequest): ForumMessageDto {
        val userId = auth.currentSessionOrNull()?.user?.id ?:
        throw IllegalStateException("User not logged in")

        val finalRequest = request.copy(senderId = userId)
        return postgrest.from("forum_messages").insert(finalRequest) {
            select()
        }.decodeSingle<ForumMessageDto>()
    }

//    fun listenToForumMessages(): Flow<List<ForumMessageDto>> {
//        val channel = realtime.channel("forum_messages_channel")
//
//        return channel.postgresListDataFlow(
//            schema = "public",
//            table = "forum_messages",
//            primaryKey = ForumMessageDto::id
//        ).onStart {
//            channel.subscribe()
//        }
//    }

    @Suppress("DEPRECATION")
    fun listenToForumMessages(channelId: String): Flow<List<ForumChannelMessagesViewDto>> {
        val channel = realtime.channel("forum_messages_channel")

        return channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "forum_messages"
            filter = "channel_id=eq.$channelId"
        }.map {
            getForumChannelMessages(channelId)
        }.onStart {
            channel.subscribe()
            emit(getForumChannelMessages(channelId))
        }
    }

    private suspend fun getForumChannelMessages(channelId: String): List<ForumChannelMessagesViewDto> {
        return postgrest.from("forum_channel_messages_view")
            .select {
                filter {
                    eq("channel_id", channelId)
                }
            }.decodeList<ForumChannelMessagesViewDto>()
    }

    suspend fun getForums(): List<ForumsViewDto> {
        val userId = auth.currentUserOrNull()?.id ?: throw IllegalStateException("User not logged in")
        return postgrest.from("user_forums_view")
            .select {
                filter {
                    eq("user_id", userId)
                }
            }.decodeList<ForumsViewDto>()
    }

    suspend fun getForumChannels(forumId: String): List<ForumChannelViewDto> {
        val userId = auth.currentUserOrNull()?.id ?: throw IllegalStateException("User not logged in")
        return postgrest.from("user_forum_channels_view")
            .select {
                filter {
                    eq("user_id", userId)
                    eq("forum_id", forumId)
                }
            }.decodeList<ForumChannelViewDto>()
    }
}