package com.prayatna.lookiesapp.presentation.forumchannellist

import com.prayatna.lookiesapp.domain.model.message.ForumChannelView

data class ForumChannelListUiState(
    val forumId: String = "",
    val isLoading: Boolean = false,
    val channels: List<ForumChannelView> = emptyList(),
    val errorMessage: String? = null,
    val userRole: String = "",
    val isCreatingChannel: Boolean = false,
    val isMemberReadOnly: Boolean = false
)
