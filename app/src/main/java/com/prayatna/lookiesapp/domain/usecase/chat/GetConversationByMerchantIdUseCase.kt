package com.prayatna.lookiesapp.domain.usecase.chat

import com.prayatna.lookiesapp.domain.model.message.Conversation
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetConversationByMerchantIdUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(merchantId: String): DataResult<Conversation?> {
        return repository.getConversationByMerchantId(merchantId)
    }
}
