package com.prayatna.lookiesapp.presentation.partner.eventTransactions.state

sealed interface EventTransactionListEffect {
    data object NavigateBack : EventTransactionListEffect
    data class NavigateToOrderDetail(val orderId: String) : EventTransactionListEffect
}
