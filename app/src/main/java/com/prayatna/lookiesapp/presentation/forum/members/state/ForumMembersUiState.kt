package com.prayatna.lookiesapp.presentation.forum.members.state

import com.prayatna.lookiesapp.domain.model.message.ForumMember

data class ForumMembersUiState(
    val members: List<ForumMember> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
