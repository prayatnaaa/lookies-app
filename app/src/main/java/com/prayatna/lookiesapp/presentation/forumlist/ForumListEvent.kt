package com.prayatna.lookiesapp.presentation.forumlist

sealed interface ForumListEvent {
    data class OnForumClick(val forumId: String, val role: String) : ForumListEvent
    data object Refresh : ForumListEvent
    data object ClearError : ForumListEvent
}
