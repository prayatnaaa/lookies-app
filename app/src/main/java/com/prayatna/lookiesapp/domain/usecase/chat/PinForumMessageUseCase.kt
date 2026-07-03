package com.prayatna.lookiesapp.domain.usecase.chat

import com.prayatna.lookiesapp.domain.model.message.ForumMessage
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class PinForumMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(messageId: String, isPinned: Boolean): DataResult<ForumMessage> {
        return chatRepository.pinForumMessage(messageId, isPinned)
    }
}
