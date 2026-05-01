package com.prayatna.lookiesapp.presentation.partner.main.home.state

sealed interface PartnerHomeEvent {
    data class Load(val businessId: String) : PartnerHomeEvent
    data object Retry : PartnerHomeEvent
    data object CreateEventClicked : PartnerHomeEvent
    data object MyEventsClicked : PartnerHomeEvent
    data object RefundClicked : PartnerHomeEvent
    data object PaintingClicked : PartnerHomeEvent
    data object ShipmentClicked : PartnerHomeEvent
    data object BackClicked : PartnerHomeEvent
}