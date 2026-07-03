package com.prayatna.lookiesapp.presentation.chat.conversationList.state

import com.prayatna.lookiesapp.domain.model.message.Conversation

data class ConversationListUiState(
    val isLoading: Boolean = false,
    val conversations: List<Conversation> = emptyList(),
    val errorMessage: String? = null,
    val currentUserId: String = ""
)
