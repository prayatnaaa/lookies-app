package com.prayatna.lookiesapp.domain.usecase.chat

import com.prayatna.lookiesapp.domain.model.message.CreateForumChannelResult
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class CreateForumChannelUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(forumId: String, name: String, isReadOnlyForMember: Boolean = false): DataResult<CreateForumChannelResult> {
        return chatRepository.createForumChannel(forumId, name, isReadOnlyForMember)
    }
}
