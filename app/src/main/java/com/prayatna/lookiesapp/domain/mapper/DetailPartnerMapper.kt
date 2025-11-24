package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.domain.model.partner.DetailPartner
import com.prayatna.lookiesapp.presentation.partner.detailpartner.state.DetailPartnerUiModel

fun DetailPartner.toUi(): DetailPartnerUiModel {
    return DetailPartnerUiModel(
        id = this.id,
        name = this.name,
        type = this.type,
        logoUrl = this.logoUrl,
        portfolioLink = this.portfolioLink,
        status = this.status,
        locations = this.locations.map { it.toUi() },
        profile = this.profile?.toUi()
    )

}