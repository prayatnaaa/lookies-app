package com.prayatna.lookiesapp.domain.model.message

data class ForumsView(
    val id: String,
    val eventId: String,
    val title: String,
    val bannerImageUrl: String,
    val createdAt: String,
    val startDate: String,
    val endDate: String,
    val role: String,
    val userId: String
)