package com.prayatna.lookiesapp.data.remote.dto.response.painting

import kotlinx.serialization.Serializable

@Serializable
data class PaintingAttributeResponse (
    val id: String,
    val code: String,
    val name: String
)