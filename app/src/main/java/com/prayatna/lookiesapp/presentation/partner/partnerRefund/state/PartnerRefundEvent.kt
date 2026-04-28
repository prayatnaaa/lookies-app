package com.prayatna.lookiesapp.presentation.partner.partnerRefund.state

sealed class PartnerRefundEvent {
    data class NotesChanged(val value: String): PartnerRefundEvent()
    data class StatusChanged(val value: String): PartnerRefundEvent()
    data object UpdateClicked : PartnerRefundEvent()
    data object BackClicked : PartnerRefundEvent()
}