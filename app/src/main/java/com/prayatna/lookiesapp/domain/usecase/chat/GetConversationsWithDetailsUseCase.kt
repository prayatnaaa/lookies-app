package com.prayatna.lookiesapp.domain.usecase.chat

import com.prayatna.lookiesapp.domain.model.message.ConversationWithDetails
import com.prayatna.lookiesapp.domain.usecase.user.GetProfileUseCase
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetConversationsWithDetailsUseCase @Inject constructor(
    private val getConversationsUseCase: GetConversationsUseCase,
    private val getProfileUseCase: GetProfileUseCase
) {
    suspend operator fun invoke(): Flow<DataResult<List<ConversationWithDetails>>> = flow {
        emit(DataResult.Loading)
        
        getProfileUseCase().collect { profileResult ->
            if (profileResult is DataResult.Success) {
                val userId = profileResult.data.id.orEmpty()
                
                when (val convResult = getConversationsUseCase()) {
                    is DataResult.Success -> {
                        val details = convResult.data.map { conv ->
                            ConversationWithDetails(
                                conversation = conv,
                                otherPartyName = conv.merchantName ?: "Merchant",
                                otherPartyImageUrl = conv.merchantPictureUrl,
                                lastMessage = conv.lastMessageContent,
                                lastMessageTime = conv.lastMessageTime
                            )
                        }
                        emit(DataResult.Success(details))
                    }
                    is DataResult.Error -> {
                        emit(DataResult.Error(convResult.error))
                    }
                    else -> {}
                }
            } else if (profileResult is DataResult.Error) {
                emit(DataResult.Error(profileResult.error))
            }
        }
    }
}
