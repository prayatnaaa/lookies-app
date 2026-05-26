package com.prayatna.lookiesapp.presentation.forum.state

import com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView

data class ForumUiState(
    val currentUserId: String = "",
    val channelId: String = "",
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val errorMessage: String? = null,
    val messages: List<ForumChannelMessagesView> = emptyList(),
    val currentInputString: String = ""
)
