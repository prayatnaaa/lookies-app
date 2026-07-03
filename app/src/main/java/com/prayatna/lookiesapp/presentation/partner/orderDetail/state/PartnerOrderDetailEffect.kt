package com.prayatna.lookiesapp.presentation.partner.orderDetail.state

sealed interface PartnerOrderDetailEffect {
    data object NavigateBack : PartnerOrderDetailEffect
    data class NavigateToArtworkDetail(val artworkId: String) : PartnerOrderDetailEffect
}
