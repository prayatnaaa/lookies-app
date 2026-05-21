package com.prayatna.lookiesapp.presentation.forum.members.state

sealed interface ForumMembersUiEvent {
    data class LoadMembers(val forumId: String) : ForumMembersUiEvent
}
