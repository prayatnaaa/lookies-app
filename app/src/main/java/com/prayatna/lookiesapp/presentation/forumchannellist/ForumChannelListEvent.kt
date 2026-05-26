package com.prayatna.lookiesapp.presentation.forumchannellist

sealed interface ForumChannelListEvent {
    data class OnChannelClick(val channelId: String) : ForumChannelListEvent
    data object Refresh : ForumChannelListEvent
    data object ClearError : ForumChannelListEvent
}
