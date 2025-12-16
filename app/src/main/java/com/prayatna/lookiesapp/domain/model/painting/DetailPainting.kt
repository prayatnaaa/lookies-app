package com.prayatna.lookiesapp.domain.model.painting

data class DetailPainting (
    val id: Int,
    val artistId: String,
    val artistName: String?,
    val title: String,
    val createdAt: String,
    val artStyleId: String?,
    val description: String?,
    val dimensionHeight: Double,
    val dimensionWidth: Double,
    val mediumId: String,
    val paintingUrl: String,
    val subject: String?,
    val updatedAt: String?,
    val yearCreated: Int,
    val mediumName: String,
    val artStyleName: String?
)