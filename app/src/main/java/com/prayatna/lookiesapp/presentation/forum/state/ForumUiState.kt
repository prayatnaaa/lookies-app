package com.prayatna.lookiesapp.presentation.forum.state

import com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView

data class ForumUiState(
    val channelId: String = "",
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val messages: List<ForumChannelMessagesView> = emptyList(),
    val currentInputString: String = "",
    val errorMessage: String? = null,
    val currentUserId: String = "",
    val userRole: String = "",
    val editingMessage: ForumChannelMessagesView? = null
)
