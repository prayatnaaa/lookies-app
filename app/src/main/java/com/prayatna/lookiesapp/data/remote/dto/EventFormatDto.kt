package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventFormatDto(
    val id: Int,
    val name: String,
    val slug: String
)
