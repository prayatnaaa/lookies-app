package com.prayatna.lookiesapp.presentation.chat.privateChat.state

sealed interface PrivateChatEffect {
    data object NavigateBack : PrivateChatEffect
}
