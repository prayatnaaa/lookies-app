package com.prayatna.lookiesapp.data.remote.dto.request.painting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePaintingRequest (
    val title: String? = null,
    @SerialName("painting_url")
    val paintingUrl: String? = null,
    val description: String? = null,
    @SerialName("dimension_height")
    val dimensionHeight: Double? = null,
    @SerialName("dimension_width")
    val dimensionWidth: Double? = null,
    @SerialName("medium_id")
    val medium: String? = null,
    @SerialName("art_style_id")
    val artStyle: String? = null,
    val subject: String? = null,
    @SerialName("year_created")
    val yearCreated: Int? = null,
    val price: Long? = null
)
