package com.prayatna.lookiesapp.domain.usecase.chat

import com.prayatna.lookiesapp.domain.model.message.Conversation
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMerchantConversationsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(merchantId: String): Flow<DataResult<List<Conversation>>> = flow {
        emit(DataResult.Loading)
        
        when (val result = chatRepository.getMerchantConversations(merchantId)) {
            is DataResult.Success -> emit(DataResult.Success(result.data))
            is DataResult.Error -> emit(DataResult.Error(result.error))
            else -> {}
        }
    }
}
