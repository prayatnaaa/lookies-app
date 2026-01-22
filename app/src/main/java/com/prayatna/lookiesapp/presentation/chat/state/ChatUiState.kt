package com.prayatna.lookiesapp.presentation.chat.state

import com.prayatna.lookiesapp.data.remote.dto.MessageDto

data class ChatUiState(
    val messages: List<MessageDto> = emptyList(),
    val messageInput: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentUserId: String = ""
)