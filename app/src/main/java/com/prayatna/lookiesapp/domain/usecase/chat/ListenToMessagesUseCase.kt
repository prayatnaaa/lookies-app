package com.prayatna.lookiesapp.domain.usecase.chat

import com.prayatna.lookiesapp.domain.model.message.Message
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenToMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(conversationId: String): Flow<List<Message>> {
        return chatRepository.listenToMessages(conversationId)
    }
}
