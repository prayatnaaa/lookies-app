package com.prayatna.lookiesapp.presentation.partner.eventTransactions.state

sealed interface EventTransactionListEvent {
    data class LoadItems(val eventId: Int) : EventTransactionListEvent
    data object OnBackClicked : EventTransactionListEvent
    data class OnItemClicked(val orderId: String) : EventTransactionListEvent
}
