package com.prayatna.lookiesapp.domain.model.painting

data class BasePainting(
    val id: Int,
    val artistId: String,
    val title: String,
    val createdAt: String,
    val description: String? = null,
    val dimensionHeight: Double,
    val dimensionWidth: Double,
    val paintingUrl: String,
    val subject: String? = null,
    val updatedAt: String? = null,
    val yearCreated: Int,
    val artStyleId: String? = null,
    val mediumId: String,
    val price: Double,
    val availabilityStatus: String
)
