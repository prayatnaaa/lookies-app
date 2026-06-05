package com.prayatna.lookiesapp.presentation.chat.privateChat.state

import com.prayatna.lookiesapp.domain.model.message.Message

data class PrivateChatUiState(
    val conversationId: String = "",
    val otherPartyName: String = "",
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val messages: List<Message> = emptyList(),
    val currentInputString: String = "",
    val currentUserId: String = "",
    val errorMessage: String? = null
)
