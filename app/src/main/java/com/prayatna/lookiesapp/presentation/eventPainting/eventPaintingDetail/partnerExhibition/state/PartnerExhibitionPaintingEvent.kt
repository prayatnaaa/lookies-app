package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.partnerExhibition.state

sealed class PartnerExhibitionPaintingEvent {

    data class Load(val id: String) : PartnerExhibitionPaintingEvent()

    data class Approve(val id: String) : PartnerExhibitionPaintingEvent()

    data class Reject(
        val id: String,
        val reason: String
    ) : PartnerExhibitionPaintingEvent()

    data class Delete(val id: String) : PartnerExhibitionPaintingEvent()
}