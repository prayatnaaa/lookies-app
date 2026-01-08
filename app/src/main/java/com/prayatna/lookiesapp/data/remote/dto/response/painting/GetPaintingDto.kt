package com.prayatna.lookiesapp.data.remote.dto.response.painting

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPaintingDto(
    val id: Int,
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
    @SerialName("medium")
    val medium: String,
    @SerialName("art_style")
    val artStyle: String? = null,
    val subject: String? = null,
    @SerialName("year_created")
    val yearCreated: Int,
    @SerialName("created_at")
    val createdAt: Instant? = null,
    @SerialName("updated_at")
    val updatedAt: Instant? = null,
    val price: Double
)
