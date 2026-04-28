package com.prayatna.lookiesapp.presentation.partner.partnerRefund.state

sealed class PartnerRefundEffect {
    data class ShowToast(val message: String) : PartnerRefundEffect()
    data object NavigateBack : PartnerRefundEffect()
}