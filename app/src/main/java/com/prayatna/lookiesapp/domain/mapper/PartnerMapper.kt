package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.domain.model.partner.Partner
import com.prayatna.lookiesapp.presentation.partner.partnerlist.state.PartnerUiModel

fun Partner.toUi(): PartnerUiModel {
    return PartnerUiModel(
        id = this.id,
        name = this.name,
        logoUrl = this.logoUrl,
        status = this.status,
    )
}

fun List<Partner>.toUiList() = map { it.toUi() }