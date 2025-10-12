package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaintingDto(

    @SerialName("artist_id")
    val artistId: String,

    @SerialName("title")
    val title: String,

    @SerialName("thumbnail_image_url")
    val thumbnailImageUrl: String?
)
