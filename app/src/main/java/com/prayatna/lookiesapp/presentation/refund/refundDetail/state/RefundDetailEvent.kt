package com.prayatna.lookiesapp.presentation.refund.refundDetail.state

sealed interface RefundDetailEvent {
    data class LoadRefund(val id: String) : RefundDetailEvent
    data class OnTrackingNumberChange(val value: String) : RefundDetailEvent
    data object SubmitTrackingNumber : RefundDetailEvent
    data object ClearError : RefundDetailEvent
}
