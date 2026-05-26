package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.message.ForumMember
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.realtime.PresenceAction
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun listenToForumMessages(channelId: String): Flow<List<com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView>>
    suspend fun insertForumsMessage(data: com.prayatna.lookiesapp.domain.model.message.CreateForumMessageInput): DataResult<com.prayatna.lookiesapp.domain.model.message.ForumMessage>
    suspend fun getForums(): DataResult<List<com.prayatna.lookiesapp.domain.model.message.ForumsView>>
    suspend fun getForumChannels(forumId: String): DataResult<List<com.prayatna.lookiesapp.domain.model.message.ForumChannelView>>
    suspend fun getForumMembers(forumId: String): DataResult<List<ForumMember>>
    fun listenToForumPresence(forumId: String): Flow<PresenceAction>
}
