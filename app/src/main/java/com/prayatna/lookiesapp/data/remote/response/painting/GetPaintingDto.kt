package com.prayatna.lookiesapp.data.remote.response.painting

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
    val medium: String,
    @SerialName("art_style")
    val artStyle: String,
    val subject: String,
    @SerialName("year_created")
    val yearCreated: Int,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant
)
