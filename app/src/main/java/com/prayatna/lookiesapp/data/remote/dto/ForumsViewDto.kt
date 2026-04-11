package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForumsViewDto(
    @SerialName("forum_id")
    val id: String,
    @SerialName("eventId")
    val eventId: String,
    val title: String,
    @SerialName("banner_image_url")
    val bannerImageUrl: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    val role: String,
    @SerialName("user_id")
    val userId: String
)