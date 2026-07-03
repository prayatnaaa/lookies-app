package com.prayatna.lookiesapp.presentation.forumchannellist

sealed interface ForumChannelListEvent {
    data class OnChannelClick(val channelId: String, val isReadOnly: Boolean) : ForumChannelListEvent
    data object Refresh : ForumChannelListEvent
    data object ClearError : ForumChannelListEvent
    data class CreateChannel(val name: String, val isReadOnly: Boolean) : ForumChannelListEvent
}
