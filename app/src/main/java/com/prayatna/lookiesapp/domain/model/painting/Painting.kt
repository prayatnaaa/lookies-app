package com.prayatna.lookiesapp.domain.model.painting

import kotlinx.datetime.Instant

data class Painting(
    val id: Int,
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
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val price: Double
)