package com.prayatna.lookiesapp.presentation.chat.privateChat.state

sealed interface PrivateChatEvent {
    data class InitChat(
        val conversationId: String?,
        val merchantId: String?,
        val otherPartyName: String,
        val metadataType: String? = null,
        val metadataId: String? = null,
        val metadataImageUrl: String? = null,
        val metadataTitle: String? = null
    ) : PrivateChatEvent
    data class InputChanged(val text: String) : PrivateChatEvent
    data object SendMessage : PrivateChatEvent
    data object OnBackClicked : PrivateChatEvent
    data object OnClearMetadata : PrivateChatEvent
}
