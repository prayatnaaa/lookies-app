package com.prayatna.lookiesapp.presentation.refund.refundList.state

sealed class RefundListEvent {
    data class SetRefundComplete(val refundId: String) : RefundListEvent()
    data object DismissError : RefundListEvent()
    data object DismissSuccess : RefundListEvent()
    data object OnBackClick : RefundListEvent()
}
