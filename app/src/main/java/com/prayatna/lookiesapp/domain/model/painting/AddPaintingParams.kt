package com.prayatna.lookiesapp.domain.model.painting

data class AddPaintingParams (
    val artistId: String,
    val title: String,
    val paintingUrl: String,
    val description: String,
    val dimensionHeight: Double,
    val dimensionWidth: Double,
    val medium: String,
    val artStyle: String? = null,
    val subject: String? = null,
    val yearCreated: Int,
)