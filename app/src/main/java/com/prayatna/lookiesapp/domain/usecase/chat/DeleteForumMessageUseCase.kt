package com.prayatna.lookiesapp.domain.usecase.chat

import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class DeleteForumMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(messageId: String): DataResult<Unit> {
        return chatRepository.deleteForumMessage(messageId)
    }
}
