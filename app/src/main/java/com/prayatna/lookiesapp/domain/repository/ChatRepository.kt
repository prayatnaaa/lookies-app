package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.message.CreateForumChannelResult
import com.prayatna.lookiesapp.domain.model.message.CreateForumMessageInput
import com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView
import com.prayatna.lookiesapp.domain.model.message.ForumChannelView
import com.prayatna.lookiesapp.domain.model.message.ForumMember
import com.prayatna.lookiesapp.domain.model.message.ForumMessage
import com.prayatna.lookiesapp.domain.model.message.ForumsView
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.realtime.PresenceAction
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
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
