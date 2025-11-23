package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.domain.model.location.Location
import com.prayatna.lookiesapp.presentation.partner.detailpartner.state.LocationUiModel

fun Location.toUi(): LocationUiModel {
    return LocationUiModel(
        name = this.name,
        locUrl = this.url
    )
}