package com.prayatna.lookiesapp.data.remote.dto.response.painting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasePaintingDto(
    val id: Int,
    @SerialName("artist_id")
    val artistId: String,
    val title: String,
    @SerialName("created_at")
    val createdAt: String,
    val description: String? = null,
    @SerialName("dimension_height")
    val dimensionHeight: Double,
    @SerialName("dimension_width")
    val dimensionWidth: Double,
    @SerialName("painting_url")
    val paintingUrl: String,
    val subject: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("year_created")
    val yearCreated: Int,
    @SerialName("art_style_id")
    val artStyleId: String? = null,
    @SerialName("medium_id")
    val mediumId: String,
    val price: Double,
    @SerialName("availability_status")
    val availabilityStatus: String
)
