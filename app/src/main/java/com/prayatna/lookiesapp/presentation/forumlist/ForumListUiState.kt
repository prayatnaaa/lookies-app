package com.prayatna.lookiesapp.presentation.forumlist

import com.prayatna.lookiesapp.domain.model.message.ForumsView

data class ForumListUiState(
    val isLoading: Boolean = true,
    val forums: List<ForumsView> = emptyList(),
    val errorMessage: String? = null
)
