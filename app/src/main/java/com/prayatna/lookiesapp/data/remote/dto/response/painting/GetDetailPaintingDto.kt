package com.prayatna.lookiesapp.data.remote.dto.response.painting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetDetailPaintingDto(
    val id: Int,
    @SerialName("artist_id")
    val artistId: String,
    @SerialName("artist_name")
    val artistName: String?,
    val title: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("art_style_id")
    val artStyleId: String?,
    val description: String?,
    @SerialName("dimension_height")
    val dimensionHeight: Double,
    @SerialName("dimension_width")
    val dimensionWidth: Double,
    @SerialName("medium_id")
    val mediumId: String,
    @SerialName("painting_url")
    val paintingUrl: String,
    val subject: String?,
    @SerialName("updated_at")
    val updatedAt: String?,
    @SerialName("year_created")
    val yearCreated: Int,
    @SerialName("medium_name")
    val mediumName: String,
    @SerialName("art_style_name")
    val artStyleName: String?,
    val price: Double,
    @SerialName("availability_status")
    val availabilityStatus: String
)
