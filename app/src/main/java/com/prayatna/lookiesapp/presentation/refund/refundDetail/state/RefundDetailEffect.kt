package com.prayatna.lookiesapp.presentation.refund.refundDetail.state

sealed interface RefundDetailEffect {
    data class ShowToast(val message: String) : RefundDetailEffect
    data object NavigateBack : RefundDetailEffect
}
