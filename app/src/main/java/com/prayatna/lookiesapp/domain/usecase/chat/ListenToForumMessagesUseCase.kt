package com.prayatna.lookiesapp.domain.usecase.chat

import com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenToForumMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(channelId: String): DataResult<Flow<List<ForumChannelMessagesView>>> {
        return chatRepository.listenToForumMessages(channelId)
    }
}
