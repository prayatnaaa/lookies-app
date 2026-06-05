package com.prayatna.lookiesapp.domain.usecase.chat

import com.prayatna.lookiesapp.domain.model.message.CreateMessageInput
import com.prayatna.lookiesapp.domain.model.message.Message
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(data: CreateMessageInput): DataResult<Message> {
        return chatRepository.sendMessage(data)
    }
}
