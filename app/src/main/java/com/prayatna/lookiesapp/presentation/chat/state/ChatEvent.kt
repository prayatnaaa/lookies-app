package com.prayatna.lookiesapp.presentation.chat.state

sealed interface ChatEvent {
    data class UpdateInput(val value: String) : ChatEvent
    data class SendMessage(val receiverId: String) : ChatEvent
}