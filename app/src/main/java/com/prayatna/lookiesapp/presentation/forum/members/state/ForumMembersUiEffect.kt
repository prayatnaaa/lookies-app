package com.prayatna.lookiesapp.presentation.forum.members.state

sealed interface ForumMembersUiEffect {
    data class ShowToast(val message: String) : ForumMembersUiEffect
    data object NavigateBack : ForumMembersUiEffect
}
