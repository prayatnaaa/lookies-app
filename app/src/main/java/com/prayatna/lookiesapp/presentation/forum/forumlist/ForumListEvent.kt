package com.prayatna.lookiesapp.presentation.forum.forumlist

sealed interface ForumListEvent {
    data class OnForumClick(val forumId: String) : ForumListEvent
    data object Refresh : ForumListEvent
    data object ClearError : ForumListEvent
}
