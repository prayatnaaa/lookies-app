package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.GetArtistBusinessIdResponse
import com.prayatna.lookiesapp.domain.model.artist.GetArtistBusinessIdOutput

fun GetArtistBusinessIdResponse.toDomain() =
    GetArtistBusinessIdOutput(
        businessId = businessId
    )