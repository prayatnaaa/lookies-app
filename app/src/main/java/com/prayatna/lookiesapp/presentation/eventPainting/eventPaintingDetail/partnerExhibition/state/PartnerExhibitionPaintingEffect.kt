package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.partnerExhibition.state

sealed class PartnerExhibitionPaintingEffect {

    data class ShowResult(val message: String) : PartnerExhibitionPaintingEffect()

    data class ShowError(val message: String) : PartnerExhibitionPaintingEffect()
}