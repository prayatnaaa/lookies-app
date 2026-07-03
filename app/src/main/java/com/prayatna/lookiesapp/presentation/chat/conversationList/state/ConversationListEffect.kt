package com.prayatna.lookiesapp.presentation.chat.conversationList.state

sealed interface ConversationListEffect {
    data object NavigateBack : ConversationListEffect
    data class NavigateToChat(val conversationId: String, val otherPartyName: String) : ConversationListEffect
}
