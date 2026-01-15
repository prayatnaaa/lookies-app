package com.prayatna.lookiesapp.data.remote.dto.response.painting

import kotlinx.serialization.Serializable

@Serializable
data class UploadPaintingResponse(
    val id: String,
    val title: String
)
