package com.prayatna.lookiesapp.presentation.forum.state

import com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView

sealed interface ForumEvent {
    data class InitChannel(val channelId: String) : ForumEvent
    data class InputChanged(val text: String) : ForumEvent
    data object SendMessage : ForumEvent
    data object ClearError : ForumEvent
    
    data class StartEditing(val message: ForumChannelMessagesView) : ForumEvent
    data object CancelEditing : ForumEvent
    data class UpdateMessage(val messageId: String, val newContent: String) : ForumEvent
    data class DeleteMessage(val messageId: String) : ForumEvent
    data class TogglePinMessage(val messageId: String, val currentPinned: Boolean) : ForumEvent
}
