package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.data.remote.dto.ForumChannelMessagesViewDto
import com.prayatna.lookiesapp.data.remote.dto.ForumChannelViewDto
import com.prayatna.lookiesapp.data.remote.dto.ForumMemberDto
import com.prayatna.lookiesapp.data.remote.dto.ForumMessageDto
import com.prayatna.lookiesapp.data.remote.dto.ForumsViewDto
import com.prayatna.lookiesapp.data.remote.dto.request.chat.CreateForumChannelRequestDto
import com.prayatna.lookiesapp.data.remote.dto.request.chat.CreateForumMessageRequest
import com.prayatna.lookiesapp.data.remote.dto.response.chat.CreateForumChannelResponseDto
import com.prayatna.lookiesapp.domain.model.message.PresenceData
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.PresenceAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.presenceChangeFlow
import io.github.jan.supabase.realtime.track
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
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

    suspend fun updateForumMessage(messageId: String, content: String): ForumMessageDto {
        return postgrest.from("forum_messages").update(
            {
                set("content", content)
                set("edited_at", "now()")
            }
        ) {
            filter {
                eq("id", messageId)
            }
            select()
        }.decodeSingle<ForumMessageDto>()
    }

    suspend fun deleteForumMessage(messageId: String) {
        postgrest.from("forum_messages").delete {
            filter {
                eq("id", messageId)
            }
        }
    }

    suspend fun pinForumMessage(messageId: String, isPinned: Boolean): ForumMessageDto {
        return postgrest.from("forum_messages").update(
            {
                set("is_pinned", isPinned)
            }
        ) {
            filter {
                eq("id", messageId)
            }
            select()
        }.decodeSingle<ForumMessageDto>()
    }

    suspend fun createForumChannel(forumId: String, name: String, isReadOnlyForMember: Boolean = false): CreateForumChannelResponseDto {
        val request = CreateForumChannelRequestDto(
            forumId = forumId,
            name = name,
            isReadOnlyForMember = isReadOnlyForMember
        )
        return postgrest.from("forum_channels").insert(request) {
            select()
        }.decodeSingle<CreateForumChannelResponseDto>()
    }

    @Suppress("DEPRECATION")
    fun listenToForumMessages(channelId: String): Flow<List<ForumChannelMessagesViewDto>> = callbackFlow {

        val channel = realtime.channel("forum_messages_channel$channelId")

        val flow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "forum_messages"
            filter = "channel_id=eq.$channelId"
        }

        channel.subscribe()

        trySend(getForumChannelMessages(channelId))

        val job = launch {
            flow.collect {
                Log.d("CHAT", "EVENT IN: $it")
                trySend(getForumChannelMessages(channelId))
            }
        }

        awaitClose {
            Log.d("CHAT", "UNSUBSCRIBE CHANNEL")
            job.cancel()
            launch {
                realtime.removeChannel(channel)
                channel.unsubscribe()
            }
        }
    }

    private suspend fun getForumChannelMessages(channelId: String): List<ForumChannelMessagesViewDto> {
        val result = postgrest.from("forum_channel_messages_view")
            .select {
                filter {
                    eq("channel_id", channelId)
                }
            }.decodeList<ForumChannelMessagesViewDto>()
        Log.d("CHAT", result.toString())
        Log.d("CHAT", channelId)
        return result
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

    suspend fun getForumMembers(forumId: String): List<ForumMemberDto> {
        return postgrest.from("forum_members_view")
            .select {
                filter {
                    eq("forum_id", forumId)
                }
            }.decodeList<ForumMemberDto>()
    }

    fun listenToForumPresence(forumId: String): Flow<PresenceAction> = callbackFlow {
        val channel = realtime.channel("forum_presence_$forumId")
        val userId = auth.currentUserOrNull()?.id ?: throw IllegalStateException("User not logged in")

        // 1. Setup flow first
        val presenceFlow = channel.presenceChangeFlow()
        val job = launch {
            presenceFlow.collect {
                trySend(it)
            }
        }

        // 2. Subscribe and wait then track
        launch {
            channel.subscribe(blockUntilSubscribed = true)
            channel.track(PresenceData(userId = userId))
        }

        awaitClose {
            job.cancel()
            launch {
                channel.untrack()
                channel.unsubscribe()
                realtime.removeChannel(channel)
            }
        }
    }
}
