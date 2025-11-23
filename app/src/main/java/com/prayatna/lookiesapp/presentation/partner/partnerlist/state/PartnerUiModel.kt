package com.prayatna.lookiesapp.presentation.partner.partnerlist.state

import com.prayatna.lookiesapp.presentation.partner.detailpartner.state.LocationUiModel

data class PartnerUiModel(
    val name: String,
    val type: String,
    val logoUrl: String,
    val status: String,
    val locations: List<LocationUiModel>,
)