package com.prayatna.lookiesapp.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetArtistBusinessIdResponse(
    @SerialName("business_id")
    val businessId: String
)