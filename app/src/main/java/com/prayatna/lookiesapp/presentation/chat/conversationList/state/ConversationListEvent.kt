package com.prayatna.lookiesapp.presentation.chat.conversationList.state

sealed interface ConversationListEvent {
    data object LoadConversations : ConversationListEvent
    data class OnConversationClicked(val conversationId: String, val otherPartyName: String) : ConversationListEvent
    data object OnBackClicked : ConversationListEvent
}
