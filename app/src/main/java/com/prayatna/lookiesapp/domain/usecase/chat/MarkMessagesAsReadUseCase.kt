package com.prayatna.lookiesapp.domain.usecase.chat

import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class MarkMessagesAsReadUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(conversationId: String, isMerchant: Boolean = false): DataResult<Unit> {
        return repository.markMessagesAsRead(conversationId, isMerchant)
    }
}
