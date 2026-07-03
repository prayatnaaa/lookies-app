package com.prayatna.lookiesapp.domain.usecase.chat

import com.prayatna.lookiesapp.domain.model.message.InitiatedConversation
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetOrCreateConversationUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(merchantId: String): DataResult<InitiatedConversation> {
        return chatRepository.getOrCreateConversation(merchantId)
    }
}
