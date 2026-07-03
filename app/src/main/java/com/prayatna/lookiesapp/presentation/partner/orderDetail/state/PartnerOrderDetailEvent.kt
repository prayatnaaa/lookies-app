package com.prayatna.lookiesapp.presentation.partner.orderDetail.state

sealed interface PartnerOrderDetailEvent {
    data class LoadOrderDetail(val orderId: String) : PartnerOrderDetailEvent
    data object OnBackClicked : PartnerOrderDetailEvent
    data object OnFinishClicked : PartnerOrderDetailEvent
}
