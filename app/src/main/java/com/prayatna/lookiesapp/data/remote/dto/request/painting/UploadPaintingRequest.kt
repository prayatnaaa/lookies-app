package com.prayatna.lookiesapp.data.remote.dto.request.painting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadPaintingRequest (
    @SerialName("artist_id")
    val artistId: String,
    val title: String,
    @SerialName("painting_url")
    val paintingUrl: String,
    val description: String,
    @SerialName("dimension_height")
    val dimensionHeight: Double,
    @SerialName("dimension_width")
    val dimensionWidth: Double,
    @SerialName("medium_id")
    val medium: String,
    @SerialName("art_style_id")
    val artStyle: String? = null,
    val subject: String? = null,
    @SerialName("year_created")
    val yearCreated: Int,
    val price: Long
)