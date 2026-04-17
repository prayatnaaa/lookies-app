package com.prayatna.lookiesapp.presentation.forum.state

sealed class ForumEvent {
    data class InitChannel(val channelId: String) : ForumEvent()
    data class InputChanged(val text: String) : ForumEvent()
    data object SendMessage : ForumEvent()
    data object ClearError : ForumEvent()
}